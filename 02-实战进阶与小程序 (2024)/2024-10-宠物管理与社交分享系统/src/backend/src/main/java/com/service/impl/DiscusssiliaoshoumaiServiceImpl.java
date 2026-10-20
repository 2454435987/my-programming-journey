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


import com.dao.DiscusssiliaoshoumaiDao;
import com.entity.DiscusssiliaoshoumaiEntity;
import com.service.DiscusssiliaoshoumaiService;
import com.entity.vo.DiscusssiliaoshoumaiVO;
import com.entity.view.DiscusssiliaoshoumaiView;

@Service("discusssiliaoshoumaiService")
public class DiscusssiliaoshoumaiServiceImpl extends ServiceImpl<DiscusssiliaoshoumaiDao, DiscusssiliaoshoumaiEntity> implements DiscusssiliaoshoumaiService {
	

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscusssiliaoshoumaiEntity> page = this.selectPage(
                new Query<DiscusssiliaoshoumaiEntity>(params).getPage(),
                new EntityWrapper<DiscusssiliaoshoumaiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusssiliaoshoumaiEntity> wrapper) {
		  Page<DiscusssiliaoshoumaiView> page =new Query<DiscusssiliaoshoumaiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DiscusssiliaoshoumaiVO> selectListVO(Wrapper<DiscusssiliaoshoumaiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DiscusssiliaoshoumaiVO selectVO(Wrapper<DiscusssiliaoshoumaiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DiscusssiliaoshoumaiView> selectListView(Wrapper<DiscusssiliaoshoumaiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscusssiliaoshoumaiView selectView(Wrapper<DiscusssiliaoshoumaiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

}
