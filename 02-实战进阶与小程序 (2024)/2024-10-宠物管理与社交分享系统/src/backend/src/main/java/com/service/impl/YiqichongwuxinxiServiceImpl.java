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


import com.dao.YiqichongwuxinxiDao;
import com.entity.YiqichongwuxinxiEntity;
import com.service.YiqichongwuxinxiService;
import com.entity.vo.YiqichongwuxinxiVO;
import com.entity.view.YiqichongwuxinxiView;

@Service("yiqichongwuxinxiService")
public class YiqichongwuxinxiServiceImpl extends ServiceImpl<YiqichongwuxinxiDao, YiqichongwuxinxiEntity> implements YiqichongwuxinxiService {
	

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YiqichongwuxinxiEntity> page = this.selectPage(
                new Query<YiqichongwuxinxiEntity>(params).getPage(),
                new EntityWrapper<YiqichongwuxinxiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<YiqichongwuxinxiEntity> wrapper) {
		  Page<YiqichongwuxinxiView> page =new Query<YiqichongwuxinxiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<YiqichongwuxinxiVO> selectListVO(Wrapper<YiqichongwuxinxiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YiqichongwuxinxiVO selectVO(Wrapper<YiqichongwuxinxiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YiqichongwuxinxiView> selectListView(Wrapper<YiqichongwuxinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YiqichongwuxinxiView selectView(Wrapper<YiqichongwuxinxiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

}
