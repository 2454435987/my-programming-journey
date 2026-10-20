package com.eldercare.controller;

import com.eldercare.common.ApiResponse;
import com.eldercare.service.AdminService;
import com.eldercare.service.DemandService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员端接口
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final DemandService demandService;

    public AdminController(AdminService adminService, DemandService demandService) {
        this.adminService = adminService;
        this.demandService = demandService;
    }

    /* ---------- 志愿者档案审核 ---------- */

    @GetMapping("/volunteer/list")
    public ApiResponse volunteerList(@RequestParam(required = false) Integer status) {
        return ApiResponse.ok(adminService.volunteerAuditList(status));
    }

    @PostMapping("/volunteer/audit")
    public ApiResponse auditVolunteer(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.auditVolunteer(
                num(b, "id"), ((Number) b.get("pass")).intValue(),
                str(b, "remark"), num(b, "adminId")));
    }

    /* ---------- 老人信息管理 ---------- */

    @GetMapping("/elder/list")
    public ApiResponse elderList(@RequestParam(required = false) Integer status) {
        return ApiResponse.ok(adminService.elderList(status));
    }

    @PostMapping("/elder/audit")
    public ApiResponse auditElder(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.auditElder(
                num(b, "id"), ((Number) b.get("pass")).intValue(),
                str(b, "remark"), num(b, "adminId")));
    }

    @PostMapping("/elder/update")
    public ApiResponse updateElder(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.updateElder(
                num(b, "id"), str(b, "healthNote"), str(b, "phone"),
                str(b, "emergencyContact"), str(b, "emergencyPhone"), str(b, "address"), num(b, "adminId")));
    }

    /* ---------- 需求审核与监管 ---------- */

    @GetMapping("/demand/list")
    public ApiResponse demandList(@RequestParam(required = false) String status) {
        return ApiResponse.ok(adminService.demandList(status));
    }

    @PostMapping("/demand/audit")
    public ApiResponse auditDemand(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.auditDemand(
                num(b, "id"), ((Number) b.get("pass")).intValue(), str(b, "remark"), num(b, "adminId")));
    }

    @PostMapping("/demand/assign")
    public ApiResponse assign(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.assignDemand(num(b, "demandId"), num(b, "volunteerId"), num(b, "adminId")));
    }

    /* ---------- 志愿任务 ---------- */

    @PostMapping("/task/publish")
    public ApiResponse publishTask(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.publishTask(
                num(b, "adminId"), str(b, "title"), str(b, "category"), str(b, "description"),
                str(b, "requireSkills"), str(b, "serviceTime"), str(b, "address"),
                b.get("requiredCount") == null ? 1 : ((Number) b.get("requiredCount")).intValue()));
    }

    @GetMapping("/task/signups")
    public ApiResponse signups(@RequestParam long taskId) {
        return ApiResponse.ok(adminService.taskSignups(taskId));
    }

    @PostMapping("/task/signup/approve")
    public ApiResponse approveSignup(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.approveSignup(
                num(b, "signupId"), ((Number) b.get("pass")).intValue(), num(b, "adminId")));
    }

    /* ---------- 时长积分与评价 ---------- */

    @GetMapping("/hours")
    public ApiResponse hours() {
        return ApiResponse.ok(adminService.volunteerHours());
    }

    @PostMapping("/points/adjust")
    public ApiResponse adjustPoints(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(adminService.adjustPoints(
                num(b, "volunteerId"), ((Number) b.get("changeVal")).intValue(),
                str(b, "type"), str(b, "reason"), num(b, "adminId")));
    }

    @GetMapping("/eval/list")
    public ApiResponse evalList() {
        return ApiResponse.ok(adminService.evalList());
    }

    /* ---------- 统计与推送 ---------- */

    @GetMapping("/stats")
    public ApiResponse stats() {
        return ApiResponse.ok(adminService.stats());
    }

    @PostMapping("/message/push")
    public ApiResponse push(@RequestBody Map<String, Object> b) {
        long n = adminService.pushMessage(
                num(b, "adminId"), str(b, "receiverRole"), str(b, "title"), str(b, "content"));
        return ApiResponse.ok(Map.of("pushed", n));
    }

    @GetMapping("/audit-logs")
    public ApiResponse auditLogs() {
        return ApiResponse.ok(adminService.auditLogs());
    }

    /* ---------- 紧急求助监管 ---------- */

    @GetMapping("/sos/list")
    public ApiResponse sosList() {
        return ApiResponse.ok(demandService.listSos());
    }

    @PostMapping("/sos/resolve")
    public ApiResponse sosResolve(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.resolveSos(num(b, "id"), str(b, "note")));
    }

    private static long num(Map<String, Object> b, String k) {
        return ((Number) b.get(k)).longValue();
    }

    private static String str(Map<String, Object> b, String k) {
        Object v = b.get(k);
        return v == null ? "" : String.valueOf(v);
    }
}
