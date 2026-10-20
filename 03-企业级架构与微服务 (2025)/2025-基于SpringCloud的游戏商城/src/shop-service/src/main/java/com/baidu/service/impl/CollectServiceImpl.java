package com.baidu.service.impl;

import com.baidu.dao.CollectMapper;
import com.baidu.pojo.Collect;
import com.baidu.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
}
