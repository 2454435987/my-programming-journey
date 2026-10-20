package com.eldercare.service;

import com.eldercare.dao.DbDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 消息通知服务
 */
@Service
public class MessageService {

    private final DbDao dao;

    public MessageService(DbDao dao) {
        this.dao = dao;
    }

    /** 发送站内消息 */
    @Transactional
    public long send(Long senderId, long receiverId, String receiverRole, String msgType,
                     String title, String content, Long relatedId) {
        return dao.insert(
                "INSERT INTO message(sender_id, receiver_id, receiver_role, msg_type, title, content, related_id, is_read, create_time) " +
                        "VALUES(?,?,?,?,?,?,?,0,NOW())",
                senderId, receiverId, receiverRole, msgType, title, content, relatedId);
    }

    /** 按接收者查询消息 */
    public List<Map<String, Object>> listByReceiver(long receiverId) {
        return dao.query("SELECT * FROM message WHERE receiver_id = ? ORDER BY create_time DESC, id DESC", receiverId);
    }

    public long unreadCount(long receiverId) {
        return dao.count("SELECT COUNT(*) FROM message WHERE receiver_id=? AND is_read=0", receiverId);
    }

    public void markRead(long id, long receiverId) {
        dao.update("UPDATE message SET is_read=1 WHERE id=? AND receiver_id=?", id, receiverId);
    }

    public void markAllRead(long receiverId) {
        dao.update("UPDATE message SET is_read=1 WHERE receiver_id=? AND is_read=0", receiverId);
    }
}
