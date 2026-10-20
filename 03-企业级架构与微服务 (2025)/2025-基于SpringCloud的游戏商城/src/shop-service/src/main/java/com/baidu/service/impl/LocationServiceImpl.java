package com.baidu.service.impl;

import com.baidu.pojo.Location;
import com.baidu.dao.LocationMapper;
import com.baidu.service.LocationService;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {

    @Autowired
    LocationMapper locationMapper;

    //根据用户电话进行模糊分页查询和普通分页查询
    public PageResult<Location> getLocationByPage(String phone, Integer start, Integer pageSize,Integer uid) {
        System.out.println("----------getLocationByPage-----------");

        int count = locationMapper.myCount(phone,uid);

        //计算起始位置
        Integer realStrat = (start-1)*pageSize;
        List<Location> locationByPage = locationMapper.getLocationByPage(phone, realStrat, pageSize,uid);
        PageResult pageResult = new PageResult();
        pageResult.setCode(0);
        pageResult.setMsg("客户地址分页查询成功");
        pageResult.setCount(count);
        pageResult.setData(locationByPage);
        return pageResult;
    }

}
