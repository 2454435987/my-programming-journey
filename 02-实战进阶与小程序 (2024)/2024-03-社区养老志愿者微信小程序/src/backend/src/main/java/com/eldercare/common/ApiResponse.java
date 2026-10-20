package com.eldercare.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应结构: {code, message, data}
 */
public class ApiResponse extends HashMap<String, Object> {

    public ApiResponse() {
        put("code", 200);
        put("message", "success");
    }

    public static ApiResponse ok(Object data) {
        ApiResponse r = new ApiResponse();
        r.put("data", data);
        return r;
    }

    public static ApiResponse ok() {
        return ok(null);
    }

    public static ApiResponse fail(int code, String message) {
        ApiResponse r = new ApiResponse();
        r.put("code", code);
        r.put("message", message);
        return r;
    }

    public static ApiResponse fail(String message) {
        return fail(500, message);
    }

    public static ApiResponse page(Object list, long total) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return ok(data);
    }
}
