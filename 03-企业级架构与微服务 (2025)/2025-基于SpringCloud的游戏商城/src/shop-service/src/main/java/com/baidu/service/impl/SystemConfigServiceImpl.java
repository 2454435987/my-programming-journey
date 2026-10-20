package com.baidu.service.impl;

import com.baidu.dao.SystemConfigMapper;
import com.baidu.pojo.SystemConfig;
import com.baidu.service.SystemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {
}
