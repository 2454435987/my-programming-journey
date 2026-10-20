package com.baidu.dao;

import com.baidu.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    // 联表查询评论和用户名
    List<Comment> selectCommentsWithUserName(@Param("pid") Integer pid);
    
    // 检查用户是否有支付过的订单（直接拿一条支付过的 oid）
    Integer checkUserPurchased(@Param("uid") Integer uid);
}
