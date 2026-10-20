package com.baidu.service;

import com.baidu.pojo.Location;
import com.baidu.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
public interface LocationService extends IService<Location> {

    //根据用户电话进行模糊分页查询和普通分页查询
    public PageResult<Location> getLocationByPage(String phone, Integer start, Integer pageSize,Integer uid);

}
