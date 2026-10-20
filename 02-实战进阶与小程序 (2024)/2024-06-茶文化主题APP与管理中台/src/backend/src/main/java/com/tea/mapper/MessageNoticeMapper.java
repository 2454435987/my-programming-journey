package com.tea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.entity.MessageNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息通知 Mapper
 */
@Mapper
public interface MessageNoticeMapper extends BaseMapper<MessageNotice> {
}
