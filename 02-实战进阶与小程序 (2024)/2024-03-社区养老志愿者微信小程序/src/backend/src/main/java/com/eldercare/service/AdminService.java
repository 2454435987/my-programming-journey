package com.eldercare.service;

import com.eldercare.dao.DbDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员服务: 档案审核 / 任务发布与分配 / 全过程监管 / 积分管理 / 统计报表
 */
@Service
public class AdminService {

    private final DbDao dao;
    private final MessageService messageService;

    public AdminService(DbDao dao, MessageService messageService) {
        this.dao = dao;
        this.messageService = messageService;
    }

    /* ======================= 档案审核 ======================= */

    public List<Map<String, Object>> volunteerAuditList(Integer status) {
        if (status == null) {
            return dao.query("SELECT * FROM volunteer_info ORDER BY audit_status ASC, create_time DESC");
        }
        return dao.query("SELECT * FROM volunteer_info WHERE audit_status=? ORDER BY create_time DESC", status);
    }

    @Transactional
    public Object auditVolunteer(long id, int pass, String remark, long adminId) {
        int st = pass == 1 ? 1 : 2;
        dao.update("UPDATE volunteer_info SET audit_status=?, audit_remark=?, audit_by=?, audit_time=NOW() WHERE id=?",
                st, remark, adminId, id);
        Map<String, Object> v = dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", id);
        if (v != null) {
            dao.update("UPDATE sys_user SET status=? WHERE id=?", st == 1 ? 1 : 0, v.get("user_id"));
            messageService.send(adminId, ((Number) v.get("user_id")).longValue(), "volunteer", "SYSTEM",
                    st == 1 ? "实名认证审核通过" : "实名认证审核未通过",
                    st == 1 ? "恭喜,您的志愿者实名认证已通过" : "很遗憾,审核未通过: " + remark, id);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "AUDIT_VOLUNTEER", "volunteer_info", id, (pass == 1 ? "通过" : "驳回") + "志愿者认证:" + remark);
        return dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", id);
    }

    public List<Map<String, Object>> elderList(Integer status) {
        if (status == null) {
            return dao.query("SELECT e.*, u.openid, u.status AS user_status FROM elder_info e " +
                    "JOIN sys_user u ON u.id = e.user_id ORDER BY e.audit_status ASC, e.create_time DESC");
        }
        return dao.query("SELECT e.*, u.openid, u.status AS user_status FROM elder_info e " +
                "JOIN sys_user u ON u.id = e.user_id WHERE e.audit_status=? ORDER BY e.create_time DESC", status);
    }

    @Transactional
    public Object auditElder(long id, int pass, String remark, long adminId) {
        int st = pass == 1 ? 1 : 2;
        dao.update("UPDATE elder_info SET audit_status=?, audit_remark=?, audit_by=?, audit_time=NOW(), bind_status=1 WHERE id=?",
                st, remark, adminId, id);
        Map<String, Object> e = dao.queryOne("SELECT * FROM elder_info WHERE id=?", id);
        if (e != null) {
            dao.update("UPDATE sys_user SET status=? WHERE id=?", st == 1 ? 1 : 0, e.get("user_id"));
            messageService.send(adminId, ((Number) e.get("user_id")).longValue(), "elder", "SYSTEM",
                    st == 1 ? "绑定审核通过" : "绑定审核未通过",
                    st == 1 ? "您的身份信息已通过社区管理员审核" : "审核未通过: " + remark, id);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "AUDIT_ELDER", "elder_info", id, (pass == 1 ? "通过" : "驳回") + "老人信息:" + remark);
        return dao.queryOne("SELECT * FROM elder_info WHERE id=?", id);
    }

    /** 老人信息管理: 更新健康备注/联系方式 */
    @Transactional
    public Object updateElder(long id, String healthNote, String phone, String emergencyContact,
                              String emergencyPhone, String address, long adminId) {
        dao.update("UPDATE elder_info SET health_note=?, phone=?, emergency_contact=?, emergency_phone=?, address=? WHERE id=?",
                healthNote, phone, emergencyContact, emergencyPhone, address, id);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "UPDATE_ELDER", "elder_info", id, "更新老人档案信息");
        return dao.queryOne("SELECT * FROM elder_info WHERE id=?", id);
    }

    /* ======================= 需求审核与监管 ======================= */

    public List<Map<String, Object>> demandList(String status) {
        String base = "SELECT d.*, el.elder_name, el.phone AS elder_phone, v.vol_name " +
                "FROM service_demand d JOIN elder_info el ON el.id = d.elder_id " +
                "LEFT JOIN volunteer_info v ON v.id = d.volunteer_id WHERE 1=1 ";
        if (status != null && !status.isEmpty()) {
            return dao.query(base + "AND d.status=? ORDER BY d.create_time DESC", status);
        }
        return dao.query(base + "ORDER BY d.create_time DESC");
    }

    @Transactional
    public Object auditDemand(long id, int pass, String remark, long adminId) {
        dao.update("UPDATE service_demand SET audit_status=?, audit_remark=? WHERE id=?", pass == 1 ? 1 : 2, remark, id);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "AUDIT_DEMAND", "service_demand", id, (pass == 1 ? "通过" : "驳回") + "服务申请:" + remark);
        return dao.queryOne("SELECT * FROM service_demand WHERE id=?", id);
    }

    /** 分配/匹配志愿者到需求 */
    @Transactional
    public Object assignDemand(long demandId, long volunteerId, long adminId) {
        Map<String, Object> d = dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
        if (d == null) {
            throw new IllegalArgumentException("需求不存在");
        }
        if (!"PENDING".equals(d.get("status"))) {
            throw new IllegalArgumentException("仅待接单需求可分配");
        }
        dao.update("UPDATE service_demand SET volunteer_id=?, status='SERVING', accept_time=NOW() WHERE id=?",
                volunteerId, demandId);
        Map<String, Object> vol = dao.queryOne("SELECT * FROM volunteer_info WHERE id=?", volunteerId);
        Map<String, Object> elder = dao.queryOne("SELECT * FROM elder_info WHERE id=?", d.get("elder_id"));
        if (vol != null) {
            messageService.send(adminId, ((Number) vol.get("user_id")).longValue(), "volunteer", "SYSTEM",
                    "任务分配提醒", "管理员为您分配了服务需求「" + d.get("title") + "」", demandId);
        }
        if (elder != null) {
            messageService.send(adminId, ((Number) elder.get("user_id")).longValue(), "elder", "ACCEPT",
                    "您的需求已匹配志愿者", "管理员为「" + d.get("title") + "」匹配了志愿者" +
                            (vol != null ? vol.get("vol_name") : ""), demandId);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "ASSIGN_DEMAND", "service_demand", demandId,
                "分配需求" + d.get("demand_no") + "给志愿者" + (vol != null ? vol.get("vol_name") : volunteerId));
        return dao.queryOne("SELECT * FROM service_demand WHERE id=?", demandId);
    }

    /* ======================= 志愿任务 ======================= */

    @Transactional
    public Object publishTask(long adminId, String title, String category, String description,
                              String requireSkills, String serviceTime, String address, int requiredCount) {
        String taskNo = "T" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        long id = dao.insert("INSERT INTO volunteer_task(task_no, publisher_id, title, category, description, require_skills, " +
                        "service_time, address, required_count, joined_count, status, create_time) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,0,'OPEN',NOW())",
                taskNo, adminId, title, category, description, requireSkills, serviceTime, address, requiredCount);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "PUBLISH_TASK", "volunteer_task", id, "发布志愿任务:" + title);
        return dao.queryOne("SELECT * FROM volunteer_task WHERE id=?", id);
    }

    public List<Map<String, Object>> taskSignups(long taskId) {
        return dao.query("SELECT s.*, v.vol_name, v.phone, v.skills, v.total_minutes, v.points " +
                "FROM task_signup s JOIN volunteer_info v ON v.id = s.volunteer_id WHERE s.task_id=? " +
                "ORDER BY s.create_time ASC", taskId);
    }

    @Transactional
    public Object approveSignup(long signupId, int pass, long adminId) {
        Map<String, Object> s = dao.queryOne("SELECT * FROM task_signup WHERE id=?", signupId);
        if (s == null) {
            throw new IllegalArgumentException("报名记录不存在");
        }
        String st = pass == 1 ? "ACCEPTED" : "REJECTED";
        dao.update("UPDATE task_signup SET status=? WHERE id=?", st, signupId);
        if (pass == 1) {
            dao.update("UPDATE volunteer_task SET status='MATCHED' WHERE id=? AND status='OPEN'", s.get("task_id"));
        } else {
            dao.update("UPDATE volunteer_task SET joined_count = GREATEST(joined_count-1, 0) WHERE id=?", s.get("task_id"));
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "ASSIGN_TASK", "task_signup", signupId, (pass == 1 ? "通过" : "驳回") + "任务报名");
        return dao.queryOne("SELECT * FROM task_signup WHERE id=?", signupId);
    }

    /* ======================= 时长与积分管理 ======================= */

    public List<Map<String, Object>> volunteerHours() {
        return dao.query("SELECT id, vol_name, phone, skills, total_minutes, points, audit_status FROM volunteer_info ORDER BY total_minutes DESC");
    }

    @Transactional
    public Object adjustPoints(long volunteerId, int changeVal, String type, String reason, long adminId) {
        if (changeVal == 0) {
            throw new IllegalArgumentException("积分变动不能为0");
        }
        dao.update("UPDATE volunteer_info SET points = points + ? WHERE id=?", changeVal, volunteerId);
        long id = dao.insert("INSERT INTO points_record(volunteer_id, change_val, type, reason, create_time) VALUES(?,?,?,?,NOW())",
                volunteerId, changeVal, type, reason);
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, target_id, detail, create_time) " +
                        "VALUES(?,?,?,?,?,?,NOW())",
                adminId, "admin", "POINTS_MANAGE", "points_record", id, "积分调整" + changeVal + ":" + reason);
        return dao.queryOne("SELECT * FROM points_record WHERE id=?", id);
    }

    public List<Map<String, Object>> evalList() {
        return dao.query("SELECT e.*, v.vol_name, el.elder_name, d.title AS demand_title, d.category " +
                "FROM service_eval e JOIN volunteer_info v ON v.id = e.volunteer_id " +
                "JOIN elder_info el ON el.id = e.elder_id JOIN service_demand d ON d.id = e.demand_id " +
                "ORDER BY e.create_time DESC");
    }

    /* ======================= 数据统计报表 ======================= */

    public Map<String, Object> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("userCount", dao.count("SELECT COUNT(*) FROM sys_user"));
        m.put("elderCount", dao.count("SELECT COUNT(*) FROM elder_info"));
        m.put("volunteerCount", dao.count("SELECT COUNT(*) FROM volunteer_info"));
        m.put("demandTotal", dao.count("SELECT COUNT(*) FROM service_demand"));
        m.put("demandDone", dao.count("SELECT COUNT(*) FROM service_demand WHERE status='DONE'"));
        m.put("demandServing", dao.count("SELECT COUNT(*) FROM service_demand WHERE status='SERVING'"));
        m.put("demandPending", dao.count("SELECT COUNT(*) FROM service_demand WHERE status='PENDING'"));
        m.put("taskTotal", dao.count("SELECT COUNT(*) FROM volunteer_task"));
        m.put("sosTotal", dao.count("SELECT COUNT(*) FROM emergency_help"));
        m.put("sosResolved", dao.count("SELECT COUNT(*) FROM emergency_help WHERE status='RESOLVED'"));
        m.put("evalCount", dao.count("SELECT COUNT(*) FROM service_eval"));
        m.put("avgScore", dao.scalar("SELECT IFNULL(AVG(score),0) FROM service_eval"));
        m.put("totalHours", dao.scalar("SELECT IFNULL(SUM(duration_minutes),0) FROM service_log"));
        m.put("totalPoints", dao.scalar("SELECT IFNULL(SUM(points),0) FROM volunteer_info"));
        m.put("categoryStat", dao.query("SELECT category, COUNT(*) AS cnt FROM service_demand GROUP BY category ORDER BY cnt DESC"));
        m.put("statusStat", dao.query("SELECT status, COUNT(*) AS cnt FROM service_demand GROUP BY status"));
        m.put("monthlyHours", dao.query(
                "SELECT DATE_FORMAT(check_in_time,'%Y-%m') AS month, SUM(duration_minutes) AS minutes, COUNT(*) AS cnt " +
                        "FROM service_log WHERE check_in_time IS NOT NULL GROUP BY DATE_FORMAT(check_in_time,'%Y-%m') ORDER BY month"));
        m.put("volunteerRank", dao.query(
                "SELECT v.id, v.vol_name, v.points, v.total_minutes, IFNULL(AVG(e.score),0) AS avg_score, COUNT(e.id) AS eval_cnt " +
                        "FROM volunteer_info v LEFT JOIN service_eval e ON e.volunteer_id = v.id " +
                        "GROUP BY v.id, v.vol_name, v.points, v.total_minutes ORDER BY v.points DESC LIMIT 10"));
        m.put("communityStat", dao.query(
                "SELECT el.community, COUNT(d.id) AS cnt FROM service_demand d JOIN elder_info el ON el.id = d.elder_id " +
                        "GROUP BY el.community"));
        return m;
    }

    /** 消息推送 */
    @Transactional
    public long pushMessage(long adminId, String receiverRole, String title, String content) {
        List<Map<String, Object>> users;
        if (receiverRole == null || receiverRole.isEmpty() || "ALL".equals(receiverRole)) {
            users = dao.query("SELECT id, role FROM sys_user WHERE role != 'admin'");
        } else {
            users = dao.query("SELECT id, role FROM sys_user WHERE role=?", receiverRole);
        }
        long n = 0;
        for (Map<String, Object> u : users) {
            messageService.send(adminId, ((Number) u.get("id")).longValue(), String.valueOf(u.get("role")),
                    "PUSH", title, content, null);
            n++;
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, detail, create_time) " +
                        "VALUES(?,?,?,?,?,NOW())",
                adminId, "admin", "PUSH_MESSAGE", "message", "批量推送:" + title + ",共" + n + "人");
        return n;
    }

    public List<Map<String, Object>> auditLogs() {
        return dao.query("SELECT * FROM audit_log ORDER BY create_time DESC LIMIT 100");
    }
}
