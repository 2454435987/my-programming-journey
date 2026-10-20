package com.water.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.water.delivery.entity.Region;
import java.util.List;

public interface RegionService extends IService<Region> {
    List<Region> listAll();
}
