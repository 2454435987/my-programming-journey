package com.water.delivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.dto.TicketCancelRefundDTO;
import com.water.delivery.dto.TicketRechargeDTO;
import com.water.delivery.entity.TicketFlow;
import com.water.delivery.entity.TicketType;
import java.util.List;
import java.util.Map;

public interface TicketService {
    List<TicketType> listActiveTypes();
    boolean recharge(TicketRechargeDTO dto, Long operatorId);
    boolean cancelOrRefund(TicketCancelRefundDTO dto, Long operatorId);
    Page<Map<String, Object>> queryFlowPage(int page, int size, Long customerId, String keyword, Integer bizType, String startDate, String endDate);
}
