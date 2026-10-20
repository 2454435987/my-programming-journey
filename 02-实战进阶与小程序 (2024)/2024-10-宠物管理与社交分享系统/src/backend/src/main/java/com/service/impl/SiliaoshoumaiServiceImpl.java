package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.SiliaoshoumaiDao;
import com.entity.SiliaoshoumaiEntity;
import com.service.SiliaoshoumaiService;
import com.entity.vo.SiliaoshoumaiVO;
import com.entity.view.SiliaoshoumaiView;

@Service("siliaoshoumaiService")
public class SiliaoshoumaiServiceImpl extends ServiceImpl<SiliaoshoumaiDao, SiliaoshoumaiEntity> implements SiliaoshoumaiService {
	

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SiliaoshoumaiEntity> page = this.selectPage(
                new Query<SiliaoshoumaiEntity>(params).getPage(),
                new EntityWrapper<SiliaoshoumaiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<SiliaoshoumaiEntity> wrapper) {
		  Page<SiliaoshoumaiView> page =new Query<SiliaoshoumaiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<SiliaoshoumaiVO> selectListVO(Wrapper<SiliaoshoumaiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public SiliaoshoumaiVO selectVO(Wrapper<SiliaoshoumaiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<SiliaoshoumaiView> selectListView(Wrapper<SiliaoshoumaiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public SiliaoshoumaiView selectView(Wrapper<SiliaoshoumaiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

}
