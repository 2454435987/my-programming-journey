package com.water.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.water.delivery.dto.CourierDTO;
import com.water.delivery.entity.Courier;
import java.util.List;
import java.util.Map;

public interface CourierService extends IService<Courier> {
    Page<Map<String, Object>> queryPage(int page, int size, String keyword, Long regionId, Integer status);
    boolean saveOrUpdateCourier(CourierDTO dto);
    boolean assignRegion(Long courierId, Long regionId);
    boolean updateStatus(Long courierId, Integer status);
    List<Courier> listActiveByRegion(Long regionId);
}
