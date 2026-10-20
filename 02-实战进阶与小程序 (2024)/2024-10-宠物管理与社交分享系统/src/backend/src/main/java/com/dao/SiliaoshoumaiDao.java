package com.dao;

import com.entity.SiliaoshoumaiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.SiliaoshoumaiVO;
import com.entity.view.SiliaoshoumaiView;


/**
 * 宠物用品售卖
 * 
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface SiliaoshoumaiDao extends BaseMapper<SiliaoshoumaiEntity> {
	
	List<SiliaoshoumaiVO> selectListVO(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
	
	SiliaoshoumaiVO selectVO(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
	
	List<SiliaoshoumaiView> selectListView(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);

	List<SiliaoshoumaiView> selectListView(Pagination page,@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
	
	SiliaoshoumaiView selectView(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
	
}
