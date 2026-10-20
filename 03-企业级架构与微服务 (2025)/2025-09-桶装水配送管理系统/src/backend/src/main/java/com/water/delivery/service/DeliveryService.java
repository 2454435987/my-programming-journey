package com.water.delivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water.delivery.dto.DeliveryHistoryQueryDTO;
import com.water.delivery.dto.VerifyDeliverDTO;
import com.water.delivery.vo.AiInspectVO;
import com.water.delivery.vo.DeliveryOrderVO;
import com.water.delivery.vo.DeliverySummaryVO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface DeliveryService {
    Map<String, Object> verifyAndDeliver(VerifyDeliverDTO dto, Long operatorUserId);
    AiInspectVO aiInspectBarrel(MultipartFile file, String testSample);
    Page<DeliveryOrderVO> queryHistoryPage(DeliveryHistoryQueryDTO queryDTO);
    DeliverySummaryVO aggregateSummary(DeliveryHistoryQueryDTO queryDTO);
    List<Map<String, Object>> getPendingTasks(Long courierId);
}
