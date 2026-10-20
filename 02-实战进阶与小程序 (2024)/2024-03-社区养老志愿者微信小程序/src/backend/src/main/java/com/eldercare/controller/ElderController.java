package com.eldercare.controller;

import com.eldercare.common.ApiResponse;
import com.eldercare.dao.DbDao;
import com.eldercare.service.DemandService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 老人端接口
 */
@RestController
@RequestMapping("/api/elder")
public class ElderController {

    private final DemandService demandService;
    private final DbDao dao;

    public ElderController(DemandService demandService, DbDao dao) {
        this.demandService = demandService;
        this.dao = dao;
    }

    private long elderId(long userId) {
        Map<String, Object> e = dao.queryOne("SELECT id FROM elder_info WHERE user_id=?", userId);
        if (e == null) {
            throw new IllegalArgumentException("老人档案不存在,请先完成身份绑定");
        }
        return ((Number) e.get("id")).longValue();
    }

    @GetMapping("/profile")
    public ApiResponse profile(@RequestParam long userId) {
        Map<String, Object> e = dao.queryOne("SELECT * FROM elder_info WHERE user_id=?", userId);
        return ApiResponse.ok(e);
    }

    @PostMapping("/demand/publish")
    public ApiResponse publish(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.publish(
                elderId(num(b, "userId")),
                str(b, "category"), str(b, "title"), str(b, "description"),
                str(b, "serviceTime"), str(b, "address"),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue()));
    }

    @GetMapping("/demand/list")
    public ApiResponse demandList(@RequestParam long userId) {
        return ApiResponse.ok(demandService.listByElder(elderId(userId)));
    }

    @GetMapping("/demand/{id}")
    public ApiResponse demandDetail(@PathVariable long id) {
        return ApiResponse.ok(demandService.detail(id));
    }

    @PostMapping("/sos")
    public ApiResponse sos(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.sos(
                elderId(num(b, "userId")),
                str(b, "helpType"), str(b, "description"), str(b, "address"),
                b.get("lng") == null ? null : ((Number) b.get("lng")).doubleValue(),
                b.get("lat") == null ? null : ((Number) b.get("lat")).doubleValue()));
    }

    @PostMapping("/eval")
    public ApiResponse evaluate(@RequestBody Map<String, Object> b) {
        return ApiResponse.ok(demandService.evaluate(
                num(b, "demandId"), elderId(num(b, "userId")),
                b.get("score") == null ? 5 : ((Number) b.get("score")).intValue(),
                str(b, "content")));
    }

    @GetMapping("/sos/list")
    public ApiResponse sosList() {
        return ApiResponse.ok(demandService.listSos());
    }

    private static long num(Map<String, Object> b, String k) {
        return ((Number) b.get(k)).longValue();
    }

    private static String str(Map<String, Object> b, String k) {
        Object v = b.get(k);
        return v == null ? "" : String.valueOf(v);
    }
}
