package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.water.delivery.entity.Region;
import com.water.delivery.mapper.RegionMapper;
import com.water.delivery.service.RegionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Override
    public List<Region> listAll() {
        return baseMapper.selectList(new LambdaQueryWrapper<Region>().orderByAsc(Region::getId));
    }
}
