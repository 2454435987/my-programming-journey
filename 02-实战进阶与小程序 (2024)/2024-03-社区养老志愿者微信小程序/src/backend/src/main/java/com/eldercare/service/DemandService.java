package com.eldercare.service;

import com.eldercare.dao.DbDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 养老服务需求业务: 发布 / 接单 / 签到签退 / 评价 / 紧急求助
 */
@Service
public class DemandService {

    private static final DateTimeFormatter NO_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DbDao dao;
    private final MessageService messageService;

    public DemandService(DbDao dao, MessageService messageService) {
        this.dao = dao;
        this.messageService = messageService;
    }

    /* ========================= 老人端 ========================= */

    /** 老人发布服务需求 */
    @Transactional
    public Map<String, Object> publish(long elderId, String category, String title, String description,
                                       String serviceTime, String address, Double lng, Double lat) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("服务类别不能为空");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("需求标题不能为空");
        }
        String demandNo = "D" + LocalDateTime.now().format(NO_FMT) + String.format("%02d",
                dao.count("SELECT COUNT(*) FROM service_demand WHERE demand_no LIKE ?", "D" + LocalDateTime.now().format(NO_FMT) + "%") + 1);
        long id = dao.insert(
                "INSERT INTO service_demand(demand_no, elder_id, category, title, description, service_time, address, lng, lat, status, audit_status, create_time) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,'PENDING',1,NOW())",
                demandNo, elderId, category, title, description, serviceTime, address,
                lng == null ? 0 : lng, lat == null ? 0 : lat);
        Map<String, Object> elder = dao.queryOne("SELECT * FROM elder_info WHERE id=?", elderId);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                elder != null ? ((Number) elder.get("user_id")).longValue() : 0, "elder",
                "PUBLISH_DEMAND", "service_demand", id, "老人发布需求" + demandNo);
        // 通知管理员
        Map<String, Object> admin = dao.queryOne("SELECT id FROM sys_user WHERE role='admin' LIMIT 1");
        if (admin != null) {
            messageService.send(null, ((Number) admin.get("id")).longValue(), "admin", "SYSTEM",
                    "新服务需求待审核", "老人" + (elder != null ? elder.get("elder_name") : "") + "发布了「" + title + "」", id);
        }
        return dao.queryOne("SELECT * FROM service_demand WHERE id=?", id);
    }

    /** 老人查看自己的需求列表(含志愿者信息) */
    public List<Map<String, Object>> listByElder(long elderId) {
        return dao.query(
                "SELECT d.*, v.vol_name, v.phone AS vol_phone, v.skills AS vol_skills, " +
                        "e.score AS eval_score, e.content AS eval_content " +
                        "FROM service_demand d " +
                        "LEFT JOIN volunteer_info v ON v.id = d.volunteer_id " +
                        "LEFT JOIN service_eval e ON e.demand_id = d.id " +
                        "WHERE d.elder_id = ? ORDER BY d.create_time DESC", elderId);
    }

    /** 需求详情 */
    public Map<String, Object> detail(long id) {
        Map<String, Object> d = dao.queryOne(
                "SELECT d.*, v.vol_name, v.phone AS vol_phone, v.skills AS vol_skills, v.audit_status AS vol_audit, " +
                        "el.elder_name, el.phone AS elder_phone, el.address AS elder_address, el.health_note, " +
                        "e.score AS eval_score, e.content AS eval_content " +
                        "FROM service_demand d " +
                        "LEFT JOIN volunteer_info v ON v.id = d.volunteer_id " +
                        "LEFT JOIN elder_info el ON el.id = d.elder_id " +
                        "LEFT JOIN service_eval e ON e.demand_id = d.id " +
                        "WHERE d.id = ?", id);
        if (d == null) {
            throw new IllegalArgumentException("需求不存在");
        }
        return d;
    }

    /* ========================= 志愿者端 ========================= */

    /** 志愿者浏览可接单需求 */
    public List<Map<String, Object>> listOpen() {
        return dao.query(
                "SELECT d.*, el.elder_name, el.phone AS elder_phone, el.health_note, el.community " +
                        "FROM service_demand d JOIN elder_info el ON el.id = d.elder_id " +
                        "WHERE d.status='PENDING' AND d.audit_status=1 ORDER BY d.service_time ASC");
    }

    /** 志愿者已接/在服务需求 */
    public List<Map<String, Object>> listByVolunteer(long volunteerId) {
        return dao.query(
                "SELECT d.*, el.elder_name, el.phone AS elder_phone, el.address AS elder_addr, el.health_note " +
                        "FROM service_demand d JOIN elder_info el ON el.id = d.elder_id " +
                        "WHERE d.volunteer_id = ? ORDER BY d.create_time DESC", volunteerId);
    }

    /** 志愿者接单 */
    @Transactional
    public Map<String, Object> accept(long demandId, long volunteerId) {
        Map<String, Object> d = dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
        if (d == null) {
            throw new IllegalArgumentException("需求不存在");
        }
        if (!"PENDING".equals(d.get("status"))) {
            throw new IllegalArgumentException("该需求当前状态不可接单");
        }
        dao.update("UPDATE service_demand SET status='SERVING', volunteer_id=?, accept_time=NOW(), start_time=NOW() WHERE id=?",
                volunteerId, demandId);
        Map<String, Object> vol = dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", volunteerId);
        Map<String, Object> elder = dao.queryOne("SELECT * FROM elder_info WHERE id=?", d.get("elder_id"));
        if (elder != null && vol != null) {
            messageService.send(((Number) vol.get("user_id")).longValue(),
                    ((Number) elder.get("user_id")).longValue(), "elder", "ACCEPT",
                    "您的需求已被接单", "志愿者" + vol.get("vol_name") + "已接单「" + d.get("title") + "」,请注意查收", demandId);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                vol != null ? ((Number) vol.get("user_id")).longValue() : 0, "volunteer",
                "ACCEPT_DEMAND", "service_demand", demandId, "志愿者接单需求" + d.get("demand_no"));
        return detail(demandId);
    }

    /** 服务签到 */
    @Transactional
    public Map<String, Object> checkIn(long demandId, long volunteerId, Double lng, Double lat) {
        Map<String, Object> d = dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
        if (d == null) {
            throw new IllegalArgumentException("需求不存在");
        }
        if (!"SERVING".equals(d.get("status"))) {
            throw new IllegalArgumentException("需求未在服务中,无法签到");
        }
        Long exist = (Long) dao.count("SELECT COUNT(*) FROM service_log WHERE demand_id=? AND status='IN'", demandId);
        if (exist > 0) {
            throw new IllegalArgumentException("已签到,请勿重复签到");
        }
        dao.insert("INSERT INTO service_log(demand_id, volunteer_id, check_in_time, check_in_lng, check_in_lat, status, create_time) " +
                        "VALUES(?,?,?,?,?, 'IN', NOW())",
                demandId, volunteerId, LocalDateTime.now().toString().replace('T', ' ').substring(0, 19),
                lng == null ? 0 : lng, lat == null ? 0 : lat);
        dao.update("UPDATE service_demand SET start_time=NOW() WHERE id=? AND start_time IS NULL", demandId);
        return detail(demandId);
    }

    /** 服务签退 + 服务日志 */
    @Transactional
    public Map<String, Object> checkOut(long demandId, long volunteerId, Double lng, Double lat, String logContent) {
        Map<String, Object> log = dao.queryOne("SELECT * FROM service_log WHERE demand_id=? AND status='IN'", demandId);
        if (log == null) {
            throw new IllegalArgumentException("尚未签到,无法签退");
        }
        long minutes = dao.count(
                "SELECT TIMESTAMPDIFF(MINUTE, check_in_time, NOW()) FROM service_log WHERE id=?", ((Number) log.get("id")).longValue());
        if (minutes < 0) {
            minutes = 0;
        }
        dao.update("UPDATE service_log SET check_out_time=NOW(), check_out_lng=?, check_out_lat=?, duration_minutes=?, " +
                        "log_content=?, status='OUT' WHERE id=?",
                lng == null ? 0 : lng, lat == null ? 0 : lat, minutes, logContent, ((Number) log.get("id")).longValue());
        dao.update("UPDATE service_demand SET status='DONE', finish_time=NOW() WHERE id=?", demandId);

        // 服务时长与积分: 每服务1分钟计1分,上限按类别
        Map<String, Object> d = dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
        int points = (int) Math.max(10, minutes);
        dao.update("UPDATE volunteer_info SET total_minutes = total_minutes + ?, points = points + ? WHERE id=?",
                minutes, points, volunteerId);
        dao.insert("INSERT INTO points_record(volunteer_id, change_val, type, reason, related_id, create_time) " +
                        "VALUES(?,?, 'EARN', ?, ?, NOW())",
                volunteerId, points, "完成" + d.get("category") + "服务(" + d.get("demand_no") + ")", demandId);

        Map<String, Object> vol = dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", volunteerId);
        Map<String, Object> elder = dao.queryOne("SELECT * FROM elder_info WHERE id=?", d.get("elder_id"));
        if (elder != null && vol != null) {
            messageService.send(((Number) vol.get("user_id")).longValue(),
                    ((Number) elder.get("user_id")).longValue(), "elder", "SYSTEM",
                    "服务已完成", "志愿者" + vol.get("vol_name") + "已完成「" + d.get("title") + "」服务,欢迎评价", demandId);
        }
        return detail(demandId);
    }

    /* ========================= 评价 ========================= */

    /** 老人服务评价 */
    @Transactional
    public Object evaluate(long demandId, long elderId, int score, String content) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("评分须在1-5之间");
        }
        Map<String, Object> d = dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
        if (d == null || ((Number) d.get("elder_id")).longValue() != elderId) {
            throw new IllegalArgumentException("需求不存在或不属于当前老人");
        }
        if (!"DONE".equals(d.get("status"))) {
            throw new IllegalArgumentException("服务完成后才能评价");
        }
        Long exist = dao.count("SELECT COUNT(*) FROM service_eval WHERE demand_id=?", demandId);
        if (exist > 0) {
            throw new IllegalArgumentException("该需求已评价过");
        }
        long id = dao.insert("INSERT INTO service_eval(demand_id, elder_id, volunteer_id, score, content, create_time) VALUES(?,?,?,?,?,NOW())",
                demandId, elderId, d.get("volunteer_id"), score, content);
        return dao.queryOne("SELECT * FROM service_eval WHERE id=?", id);
    }

    /* ========================= 紧急求助 ========================= */

    /** 一键紧急求助: 推送管理员与就近志愿者并附位置 */
    @Transactional
    public Map<String, Object> sos(long elderId, String helpType, String description,
                                   String address, Double lng, Double lat) {
        Map<String, Object> elder = dao.queryOne("SELECT * FROM elder_info WHERE id=?", elderId);
        if (elder == null) {
            throw new IllegalArgumentException("老人档案不存在");
        }
        double lngV = lng != null ? lng : ((Number) elder.get("lng")).doubleValue();
        double latV = lat != null ? lat : ((Number) elder.get("lat")).doubleValue();

        // 就近志愿者: 按距离平方排序取前3(演示用平面近似)
        List<Map<String, Object>> nearby = dao.query(
                "SELECT id, vol_name, user_id, " +
                        "((lng-?)*(lng-?)+(lat-?)*(lat-?)) AS dist2 FROM volunteer_info " +
                        "WHERE audit_status=1 ORDER BY dist2 ASC LIMIT 3",
                lngV, lngV, latV, latV);
        StringBuilder volIds = new StringBuilder();
        for (Map<String, Object> v : nearby) {
            if (volIds.length() > 0) {
                volIds.append(",");
            }
            volIds.append(v.get("id"));
        }

        Map<String, Object> admin = dao.queryOne("SELECT * FROM sys_user WHERE role='admin' LIMIT 1");
        long adminId = admin != null ? ((Number) admin.get("id")).longValue() : 0;

        long id = dao.insert(
                "INSERT INTO emergency_help(elder_id, help_type, description, address, lng, lat, status, notify_admin_id, nearby_vol_ids, create_time) " +
                        "VALUES(?,?,?,?,?,?, 'PENDING', ?, ?, NOW())",
                elderId, helpType == null ? "SOS" : helpType, description,
                address != null ? address : elder.get("address"), lngV, latV, adminId, volIds.toString());

        String locText = "位置(" + lngV + ", " + latV + ") " + (address != null ? address : elder.get("address"));
        if (admin != null) {
            messageService.send(((Number) elder.get("user_id")).longValue(), adminId, "admin", "EMERGENCY",
                    "紧急求助报警", elder.get("elder_name") + "发起紧急求助(" + helpType + ")," + locText, id);
        }
        for (Map<String, Object> v : nearby) {
            messageService.send(((Number) elder.get("user_id")).longValue(),
                    ((Number) v.get("user_id")).longValue(), "volunteer", "EMERGENCY",
                    "紧急求助通知", elder.get("elder_name") + "发出紧急求助,距离您较近,请协助。" + locText, id);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                ((Number) elder.get("user_id")).longValue(), "elder", "SOS", "emergency_help", id,
                "老人" + elder.get("elder_name") + "发起紧急求助");
        return dao.queryOne("SELECT * FROM emergency_help WHERE id=?", id);
    }

    public List<Map<String, Object>> listSos() {
        return dao.query("SELECT h.*, e.elder_name, e.phone AS elder_phone FROM emergency_help h " +
                "JOIN elder_info e ON e.id = h.elder_id ORDER BY h.create_time DESC");
    }

    @Transactional
    public Object resolveSos(long id, String note) {
        dao.update("UPDATE emergency_help SET status='RESOLVED', resolve_time=NOW(), resolve_note=? WHERE id=?", note, id);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(1,'admin','HANDLE_EMERGENCY','emergency_help',?,?,NOW())", id, note);
        return dao.queryOne("SELECT * FROM emergency_help WHERE id=?", id);
    }

    public List<Map<String, Object>> logsByDemand(long demandId) {
        return dao.query("SELECT * FROM service_log WHERE demand_id=? ORDER BY create_time DESC", demandId);
    }

    public Map<String, Object> statsOfDemand() {
        Map<String, Object> m = new HashMap<>();
        m.put("total", dao.count("SELECT COUNT(*) FROM service_demand"));
        return m;
    }
}
