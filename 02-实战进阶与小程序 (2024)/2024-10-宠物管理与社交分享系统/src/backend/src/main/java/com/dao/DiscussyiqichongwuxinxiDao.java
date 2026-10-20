package com.dao;

import com.entity.DiscussyiqichongwuxinxiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DiscussyiqichongwuxinxiVO;
import com.entity.view.DiscussyiqichongwuxinxiView;


/**
 * 遗弃宠物信息评论表
 * 
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface DiscussyiqichongwuxinxiDao extends BaseMapper<DiscussyiqichongwuxinxiEntity> {
	
	List<DiscussyiqichongwuxinxiVO> selectListVO(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
	
	DiscussyiqichongwuxinxiVO selectVO(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
	
	List<DiscussyiqichongwuxinxiView> selectListView(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);

	List<DiscussyiqichongwuxinxiView> selectListView(Pagination page,@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
	
	DiscussyiqichongwuxinxiView selectView(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
	
}
