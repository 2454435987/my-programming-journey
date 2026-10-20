package com.eldercare.service;

import com.eldercare.dao.DbDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 志愿者服务: 实名注册 / 报名接单 / 服务记录 / 积分
 */
@Service
public class VolunteerService {

    private final DbDao dao;
    private final MessageService messageService;

    public VolunteerService(DbDao dao, MessageService messageService) {
        this.dao = dao;
        this.messageService = messageService;
    }

    /** 志愿者注册实名认证 */
    @Transactional
    public Object register(long userId, String volName, String gender, String phone, String idCard,
                           String skills, String servePeriod, String serviceArea, Double lng, Double lat) {
        if (volName == null || volName.isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (idCard == null || idCard.length() < 15) {
            throw new IllegalArgumentException("身份证号不合法");
        }
        Map<String, Object> old = dao.queryOne("SELECT * FROM volunteer_info WHERE user_id=?", userId);
        if (old == null) {
            dao.insert("INSERT INTO volunteer_info(user_id, vol_name, gender, phone, id_card, skills, serve_period, " +
                            "service_area, lng, lat, points, total_minutes, audit_status, create_time) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,0,0,0,NOW())",
                    userId, volName, gender, phone, idCard, skills, servePeriod, serviceArea,
                    lng == null ? 0 : lng, lat == null ? 0 : lat);
        } else {
            dao.update("UPDATE volunteer_info SET vol_name=?, gender=?, phone=?, id_card=?, skills=?, serve_period=?, " +
                            "service_area=?, audit_status=0 WHERE user_id=?",
                    volName, gender, phone, idCard, skills, servePeriod, serviceArea, userId);
        }
        dao.update("UPDATE sys_user SET real_name=?, phone=?, id_card=?, role='volunteer', status=2 WHERE id=?",
                volName, phone, idCard, userId);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, detail, create_time) " +
                        "VALUES(?,?,?,?,?,NOW())",
                userId, "volunteer", "REGISTER", "volunteer_info", "志愿者" + volName + "提交实名认证");
        Map<String, Object> admin = dao.queryOne("SELECT id FROM sys_user WHERE role='admin' LIMIT 1");
        if (admin != null) {
            messageService.send(userId, ((Number) admin.get("id")).longValue(), "admin", "SYSTEM",
                    "志愿者实名认证待审核", "志愿者" + volName + "提交实名认证,请及时审核", null);
        }
        return dao.queryOne("SELECT * FROM volunteer_info WHERE user_id=?", userId);
    }

    /** 志愿者档案 */
    public Map<String, Object> profile(long volunteerId) {
        return dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", volunteerId);
    }

    /** 志愿者服务记录(含老人信息) */
    public List<Map<String, Object>> records(long volunteerId) {
        return dao.query(
                "SELECT d.*, el.elder_name, el.phone AS elder_phone, " +
                        "l.check_in_time, l.check_out_time, l.duration_minutes, l.log_content, " +
                        "e.score AS eval_score, e.content AS eval_content " +
                        "FROM service_demand d " +
                        "JOIN elder_info el ON el.id = d.elder_id " +
                        "LEFT JOIN service_log l ON l.demand_id = d.id " +
                        "LEFT JOIN service_eval e ON e.demand_id = d.id " +
                        "WHERE d.volunteer_id = ? ORDER BY d.create_time DESC", volunteerId);
    }

    /** 积分明细 */
    public Map<String, Object> pointsDetail(long volunteerId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> vol = dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", volunteerId);
        result.put("profile", vol);
        result.put("records", dao.query("SELECT * FROM points_record WHERE volunteer_id=? ORDER BY create_time DESC", volunteerId));
        return result;
    }

    /* ======================= 志愿任务报名 ======================= */

    public List<Map<String, Object>> listTasks() {
        return dao.query("SELECT * FROM volunteer_task ORDER BY create_time DESC");
    }

    @Transactional
    public Object signupTask(long taskId, long volunteerId, String remark) {
        Map<String, Object> t = dao.queryOne("SELECT * FROM volunteer_task WHERE id=?", taskId);
        if (t == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        if (!"OPEN".equals(t.get("status"))) {
            throw new IllegalArgumentException("该任务不在招募中");
        }
        Long exist = dao.count("SELECT COUNT(*) FROM task_signup WHERE task_id=? AND volunteer_id=?", taskId, volunteerId);
        if (exist > 0) {
            throw new IllegalArgumentException("已报名该任务");
        }
        long id = dao.insert("INSERT INTO task_signup(task_id, volunteer_id, status, remark, create_time) VALUES(?,?, 'PENDING', ?, NOW())",
                taskId, volunteerId, remark);
        dao.update("UPDATE volunteer_task SET joined_count = joined_count + 1 WHERE id=?", taskId);
        return dao.queryOne("SELECT * FROM task_signup WHERE id=?", id);
    }

    /** 我的报名 */
    public List<Map<String, Object>> mySignups(long volunteerId) {
        return dao.query(
                "SELECT s.*, t.title, t.category, t.service_time, t.address, t.status AS task_status " +
                        "FROM task_signup s JOIN volunteer_task t ON t.id = s.task_id " +
                        "WHERE s.volunteer_id = ? ORDER BY s.create_time DESC", volunteerId);
    }

    /** 按 userId 取志愿者档案 */
    public Map<String, Object> byUserId(long userId) {
        return dao.queryOne("SELECT * FROM volunteer_info WHERE user_id=?", userId);
    }
}
