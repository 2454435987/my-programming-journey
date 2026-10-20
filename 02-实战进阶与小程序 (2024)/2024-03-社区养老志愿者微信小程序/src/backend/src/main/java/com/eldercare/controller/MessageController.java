package com.eldercare.controller;

import com.eldercare.common.ApiResponse;
import com.eldercare.service.MessageService;
import org.springframework.web.bind.annotation.*;

/**
 * 消息通知接口
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/list")
    public ApiResponse list(@RequestParam long userId) {
        return ApiResponse.ok(messageService.listByReceiver(userId));
    }

    @GetMapping("/unread")
    public ApiResponse unread(@RequestParam long userId) {
        return ApiResponse.ok(messageService.unreadCount(userId));
    }

    @PostMapping("/read")
    public ApiResponse read(@RequestBody java.util.Map<String, Object> body) {
        long id = ((Number) body.get("id")).longValue();
        long userId = ((Number) body.get("userId")).longValue();
        messageService.markRead(id, userId);
        return ApiResponse.ok();
    }

    @PostMapping("/read-all")
    public ApiResponse readAll(@RequestBody java.util.Map<String, Object> body) {
        messageService.markAllRead(((Number) body.get("userId")).longValue());
        return ApiResponse.ok();
    }
}
