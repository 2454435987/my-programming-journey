package com.dao;

import com.entity.YiqichongwuxinxiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.YiqichongwuxinxiVO;
import com.entity.view.YiqichongwuxinxiView;


/**
 * 遗弃宠物信息
 * 
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface YiqichongwuxinxiDao extends BaseMapper<YiqichongwuxinxiEntity> {
	
	List<YiqichongwuxinxiVO> selectListVO(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
	
	YiqichongwuxinxiVO selectVO(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
	
	List<YiqichongwuxinxiView> selectListView(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);

	List<YiqichongwuxinxiView> selectListView(Pagination page,@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
	
	YiqichongwuxinxiView selectView(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
	
}
