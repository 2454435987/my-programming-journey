package com.water.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.water.delivery.common.exception.BusinessException;
import com.water.delivery.dto.CourierDTO;
import com.water.delivery.entity.Courier;
import com.water.delivery.entity.Region;
import com.water.delivery.entity.SysUser;
import com.water.delivery.mapper.CourierMapper;
import com.water.delivery.mapper.RegionMapper;
import com.water.delivery.mapper.SysUserMapper;
import com.water.delivery.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl extends ServiceImpl<CourierMapper, Courier> implements CourierService {

    private final RegionMapper regionMapper;
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<Map<String, Object>> queryPage(int pageNum, int pageSize, String keyword, Long regionId, Integer status) {
        Page<Courier> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Courier> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Courier::getName, keyword).or().like(Courier::getCourierNo, keyword).or().like(Courier::getPhone, keyword));
        }
        if (regionId != null) {
            wrapper.eq(Courier::getRegionId, regionId);
        }
        if (status != null) {
            wrapper.eq(Courier::getStatus, status);
        }
        wrapper.orderByDesc(Courier::getId);

        Page<Courier> selectPage = baseMapper.selectPage(page, wrapper);
        
        // 组装关联片区名称
        List<Map<String, Object>> records = new ArrayList<>();
        Map<Long, String> regionCache = new HashMap<>();

        for (Courier c : selectPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("userId", c.getUserId());
            map.put("courierNo", c.getCourierNo());
            map.put("name", c.getName());
            map.put("phone", c.getPhone());
            map.put("regionId", c.getRegionId());
            map.put("vehicleType", c.getVehicleType());
            map.put("status", c.getStatus());
            map.put("entryDate", c.getEntryDate());
            map.put("idCard", c.getIdCard());
            map.put("createTime", c.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getCreateTime()) : "");

            String regionName = regionCache.computeIfAbsent(c.getRegionId(), rid -> {
                Region r = regionMapper.selectById(rid);
                return r != null ? r.getRegionName() : "未划分片区";
            });
            map.put("regionName", regionName);
            records.add(map);
        }

        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize, selectPage.getTotal());
        resultPage.setRecords(records);
        return resultPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateCourier(CourierDTO dto) {
        if (dto.getId() == null) {
            // 新增送水工: 同步创建系统用户账号 (默认角色2=ROLE_COURIER, 密码123456)
            String courierNo = dto.getCourierNo();
            if (!StringUtils.hasText(courierNo)) {
                courierNo = "SG" + (int)((Math.random() * 9 + 1) * 1000);
            }

            SysUser user = new SysUser();
            user.setUsername(courierNo);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRealName(dto.getName() + "(送水工)");
            user.setPhone(dto.getPhone());
            user.setRoleId(2L);
            user.setStatus(1);
            user.setCreateTime(new Date());
            userMapper.insert(user);

            Courier courier = new Courier();
            courier.setUserId(user.getId());
            courier.setCourierNo(courierNo);
            courier.setName(dto.getName());
            courier.setPhone(dto.getPhone());
            courier.setRegionId(dto.getRegionId());
            courier.setVehicleType(dto.getVehicleType() != null ? dto.getVehicleType() : "电动三轮车");
            courier.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
            courier.setEntryDate(LocalDate.now());
            courier.setIdCard(dto.getIdCard());
            courier.setCreateTime(new Date());
            baseMapper.insert(courier);
        } else {
            Courier courier = baseMapper.selectById(dto.getId());
            if (courier == null) throw new BusinessException("配送员不存在");
            courier.setName(dto.getName());
            courier.setPhone(dto.getPhone());
            courier.setRegionId(dto.getRegionId());
            if (dto.getVehicleType() != null) courier.setVehicleType(dto.getVehicleType());
            if (dto.getStatus() != null) courier.setStatus(dto.getStatus());
            if (dto.getIdCard() != null) courier.setIdCard(dto.getIdCard());
            baseMapper.updateById(courier);
        }
        return true;
    }

    @Override
    public boolean assignRegion(Long courierId, Long regionId) {
        Courier courier = baseMapper.selectById(courierId);
        if (courier == null) throw new BusinessException("配送员不存在");
        if (courier.getStatus() == 0) throw new BusinessException("该配送员处于停用状态，无法分配片区");
        courier.setRegionId(regionId);
        baseMapper.updateById(courier);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStatus(Long courierId, Integer status) {
        Courier courier = baseMapper.selectById(courierId);
        if (courier == null) throw new BusinessException("配送员不存在");
        courier.setStatus(status);
        baseMapper.updateById(courier);

        // 同步修改 sys_user 状态
        SysUser user = userMapper.selectById(courier.getUserId());
        if (user != null) {
            user.setStatus(status == 1 ? 1 : 0);
            userMapper.updateById(user);
        }
        return true;
    }

    @Override
    public List<Courier> listActiveByRegion(Long regionId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Courier>()
                .eq(Courier::getRegionId, regionId)
                .eq(Courier::getStatus, 1));
    }
}
