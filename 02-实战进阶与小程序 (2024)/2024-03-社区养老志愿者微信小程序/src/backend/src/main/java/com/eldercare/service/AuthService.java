package com.eldercare.service;

import com.eldercare.dao.DbDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证与身份绑定服务
 */
@Service
public class AuthService {

    private final DbDao dao;

    public AuthService(DbDao dao) {
        this.dao = dao;
    }

    /**
     * 微信授权登录(演示版): code 即模拟 openid
     * H5 演示可传 demo 字符串按角色落入已审核的种子用户
     */
    @Transactional
    public Map<String, Object> login(String code, String role) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("code 不能为空");
        }
        String cleanCode = code.trim();
        boolean isMockOrDemo = cleanCode.startsWith("demo_")
                || "the code is a mock one".equalsIgnoreCase(cleanCode)
                || cleanCode.toLowerCase().contains("mock");

        Map<String, Object> user = null;
        if (isMockOrDemo) {
            String demoRole = (role != null && !role.isEmpty()) ? role : (cleanCode.startsWith("demo_") ? cleanCode.replace("demo_", "") : "elder");
            String openid = switch (demoRole) {
                case "admin" -> "wx_openid_admin_001";
                case "volunteer" -> "wx_openid_vol_001";
                default -> "wx_openid_elder_001";
            };
            user = dao.queryOne("SELECT * FROM sys_user WHERE openid = ?", openid);
        } else {
            user = dao.queryOne("SELECT * FROM sys_user WHERE openid = ?", cleanCode);
        }

        if (user == null) {
            // 新用户: 自动注册并初始化档案
            String r = (role == null || role.isEmpty()) ? "elder" : role;
            long id = dao.insert(
                    "INSERT INTO sys_user(openid, nickname, role, status, create_time) VALUES(?,?,?,1,NOW())",
                    cleanCode, "微信用户", r);
            user = dao.queryOne("SELECT * FROM sys_user WHERE id = ?", id);

            if ("elder".equals(r)) {
                dao.insert("INSERT INTO elder_info(user_id, elder_name, gender, phone, address, community, health_note, emergency_contact, emergency_phone, bind_status, audit_status, create_time) VALUES(?,?,?,?,?,?,?,?,?,1,1,NOW())",
                        id, "微信老人", "男", "13800000000", "幸福里社区1栋101", "幸福里社区", "健康良好", "紧急联系人", "13900000000");
            } else if ("volunteer".equals(r)) {
                dao.insert("INSERT INTO volunteer_info(user_id, vol_name, gender, phone, id_card, skills, serve_period, service_area, points, total_minutes, audit_status, create_time) VALUES(?,?,?,?,?,?,?,?,0,0,1,NOW())",
                        id, "微信志愿者", "男", "13800000000", "110101199001011234", "上门陪伴,助餐", "周末全天", "幸福里社区");
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("token", "TK" + user.get("id"));
        result.put("user", user);
        result.put("profile", loadProfile(user));
        return result;
    }

    /** 按角色加载业务档案 */
    public Map<String, Object> loadProfile(Map<String, Object> user) {
        long userId = ((Number) user.get("id")).longValue();
        String role = String.valueOf(user.get("role"));
        return switch (role) {
            case "elder" -> dao.queryOne("SELECT * FROM elder_info WHERE user_id = ?", userId);
            case "volunteer" -> dao.queryOne("SELECT * FROM volunteer_info WHERE user_id = ?", userId);
            case "admin" -> dao.queryOne("SELECT * FROM sys_user WHERE id = ?", userId);
            default -> null;
        };
    }

    /** 老人身份绑定(提交管理员审核) */
    @Transactional
    public Object bindElder(long userId, String elderName, String gender, String phone,
                            String address, String community, String healthNote,
                            String emergencyContact, String emergencyPhone, Double lng, Double lat) {
        Map<String, Object> u = dao.queryOne("SELECT * FROM sys_user WHERE id = ?", userId);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        Map<String, Object> old = dao.queryOne("SELECT * FROM elder_info WHERE user_id = ?", userId);
        if (old == null) {
            dao.insert("INSERT INTO elder_info(user_id, elder_name, gender, phone, address, community, health_note, " +
                            "emergency_contact, emergency_phone, lng, lat, bind_status, audit_status, create_time) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,1,0,NOW())",
                    userId, elderName, gender, phone, address, community, healthNote,
                    emergencyContact, emergencyPhone, lng == null ? 0 : lng, lat == null ? 0 : lat);
            dao.update("UPDATE sys_user SET real_name=?, phone=?, status=2 WHERE id=?", elderName, phone, userId);
        } else {
            dao.update("UPDATE elder_info SET elder_name=?, gender=?, address=?, community=?, health_note=?, " +
                            "emergency_contact=?, emergency_phone=?, bind_status=1, audit_status=0 WHERE user_id=?",
                    elderName, gender, address, community, healthNote, emergencyContact, emergencyPhone, userId);
            dao.update("UPDATE sys_user SET real_name=?, phone=?, status=2 WHERE id=?", elderName, phone, userId);
        }
        dao.insert("INSERT INTO audit_log(operator_id, operator_role, action, target_type, detail, create_time) " +
                        "VALUES(?,?,?,?,?,NOW())",
                userId, "elder", "BIND_SUBMIT", "elder_info", "老人" + elderName + "提交身份绑定申请");
        return dao.queryOne("SELECT * FROM elder_info WHERE user_id = ?", userId);
    }

    /** 更新老人联系方式 */
    public Object updateContact(long userId, String phone, String emergencyContact, String emergencyPhone) {
        int n = dao.update("UPDATE elder_info SET phone=?, emergency_contact=?, emergency_phone=? WHERE user_id=?",
                phone, emergencyContact, emergencyPhone, userId);
        if (n == 0) {
            throw new IllegalArgumentException("档案不存在");
        }
        dao.update("UPDATE sys_user SET phone=? WHERE id=?", phone, userId);
        return dao.queryOne("SELECT * FROM elder_info WHERE user_id = ?", userId);
    }
}
