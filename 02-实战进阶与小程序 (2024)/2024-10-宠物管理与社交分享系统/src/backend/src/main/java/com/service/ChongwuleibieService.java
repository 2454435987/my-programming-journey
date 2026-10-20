package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ChongwuleibieEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ChongwuleibieVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.ChongwuleibieView;


/**
 * 宠物类别
 *
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface ChongwuleibieService extends IService<ChongwuleibieEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ChongwuleibieVO> selectListVO(Wrapper<ChongwuleibieEntity> wrapper);
   	
   	ChongwuleibieVO selectVO(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
   	
   	List<ChongwuleibieView> selectListView(Wrapper<ChongwuleibieEntity> wrapper);
   	
   	ChongwuleibieView selectView(@Param("ew") Wrapper<ChongwuleibieEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ChongwuleibieEntity> wrapper);
   	
}

