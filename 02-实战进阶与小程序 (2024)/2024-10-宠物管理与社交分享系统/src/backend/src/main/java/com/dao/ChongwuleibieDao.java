package com.dao;

import com.entity.ChongwuleibieEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ChongwuleibieVO;
import com.entity.view.ChongwuleibieView;


/**
 * 宠物类别
 * 
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface ChongwuleibieDao extends BaseMapper<ChongwuleibieEntity> {
	
	List<ChongwuleibieVO> selectListVO(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
	
	ChongwuleibieVO selectVO(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
	
	List<ChongwuleibieView> selectListView(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);

	List<ChongwuleibieView> selectListView(Pagination page,@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
	
	ChongwuleibieView selectView(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
	
}
