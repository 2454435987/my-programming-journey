package com.eldercare.controller;

import com.eldercare.common.ApiResponse;
import com.eldercare.dao.DbDao;
import com.eldercare.service.DemandService;
import com.eldercare.service.VolunteerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 志愿者端接口
 */
@RestController
@RequestMapping("/api/volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final DemandService demandService;
    private final DbDao dao;

    public VolunteerController(VolunteerService volunteerService, DemandService demandService, DbDao dao) {
        this.volunteerService = volunteerService;
        this.demandService = demandService;
        this.dao = dao;
    }

    private long volunteerId(long userId) {
        Map<String, Object> v = dao.queryOne("SELECT id FROM volunteer_info WHERE user_id=?", userId);
        if (v == null) {
            throw new IllegalArgumentException("志愿者档案不存在,请先完成实名注册");
        }
        return ((Number) v.get("id")).longValue();
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(volunteerService.register(
                num(b, "userId"), str(b, "volName"), str(b, "gender"), str(b, "phone"),
                str(b, "idCard"), str(b, "skills"), str(b, "servePeriod"), str(b, "serviceArea"),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue()));
    }

    @GetMapping("/profile")
    public ApiResponse profile(@RequestParam long userId) {
        return ApiResponse.ok(volunteerService.byUserId(userId));
    }

    @GetMapping("/demand/open")
    public ApiResponse openDemands() {
        return ApiResponse.ok(demandService.listOpen());
    }

    @GetMapping("/demand/mine")
    public ApiResponse myDemands(@RequestParam long userId) {
        return ApiResponse.ok(demandService.listByVolunteer(volunteerId(userId)));
    }

    @PostMapping("/demand/accept")
    public ApiResponse accept(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.accept(num(b, "demandId"), volunteerId(num(b, "userId"))));
    }

    @PostMapping("/checkin")
    public ApiResponse checkin(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.checkIn(
                num(b, "demandId"), volunteerId(num(b, "userId")),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue()));
    }

    @PostMapping("/checkout")
    public ApiResponse checkout(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.checkOut(
                num(b, "demandId"), volunteerId(num(b, "userId")),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue(),
                str(b, "logContent")));
    }

    @GetMapping("/records")
    public ApiResponse records(@RequestParam long userId) {
        return ApiResponse.ok(volunteerService.records(volunteerId(userId)));
    }

    @GetMapping("/points")
    public ApiResponse points(@RequestParam long userId) {
        return ApiResponse.ok(volunteerService.pointsDetail(volunteerId(userId)));
    }

    @GetMapping("/task/list")
    public ApiResponse taskList() {
        return ApiResponse.ok(volunteerService.listTasks());
    }

    @PostMapping("/task/signup")
    public ApiResponse signup(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(volunteerService.signupTask(
                num(b, "taskId"), volunteerId(num(b, "userId")), str(b, "remark")));
    }

    @GetMapping("/task/my")
    public ApiResponse mySignups(@RequestParam long userId) {
        return ApiResponse.ok(volunteerService.mySignups(volunteerId(userId)));
    }

    private static long num(Map<String, Object> b, String k) {
        return ((Number) b.get(k)).longValue();
    }

    private static String str(Map<String, Object> b, String k) {
        Object v = b.get(k);
        return v == null ? "" : String.valueOf(v);
    }
}
