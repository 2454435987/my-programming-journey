package com.dao;

import com.entity.DiscusssiliaoshoumaiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DiscusssiliaoshoumaiVO;
import com.entity.view.DiscusssiliaoshoumaiView;


/**
 * 宠物用品售卖评论表
 * 
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface DiscusssiliaoshoumaiDao extends BaseMapper<DiscusssiliaoshoumaiEntity> {
	
	List<DiscusssiliaoshoumaiVO> selectListVO(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
	
	DiscusssiliaoshoumaiVO selectVO(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
	
	List<DiscusssiliaoshoumaiView> selectListView(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);

	List<DiscusssiliaoshoumaiView> selectListView(Pagination page,@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
	
	DiscusssiliaoshoumaiView selectView(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
	
}
