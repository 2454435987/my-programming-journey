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


import com.dao.DiscussyiqichongwuxinxiDao;
import com.entity.DiscussyiqichongwuxinxiEntity;
import com.service.DiscussyiqichongwuxinxiService;
import com.entity.vo.DiscussyiqichongwuxinxiVO;
import com.entity.view.DiscussyiqichongwuxinxiView;

@Service("discussyiqichongwuxinxiService")
public class DiscussyiqichongwuxinxiServiceImpl extends ServiceImpl<DiscussyiqichongwuxinxiDao, DiscussyiqichongwuxinxiEntity> implements DiscussyiqichongwuxinxiService {
	

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscussyiqichongwuxinxiEntity> page = this.selectPage(
                new Query<DiscussyiqichongwuxinxiEntity>(params).getPage(),
                new EntityWrapper<DiscussyiqichongwuxinxiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscussyiqichongwuxinxiEntity> wrapper) {
		  Page<DiscussyiqichongwuxinxiView> page =new Query<DiscussyiqichongwuxinxiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DiscussyiqichongwuxinxiVO> selectListVO(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DiscussyiqichongwuxinxiVO selectVO(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DiscussyiqichongwuxinxiView> selectListView(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscussyiqichongwuxinxiView selectView(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

}
