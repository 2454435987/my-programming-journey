package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.SiliaoshoumaiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.SiliaoshoumaiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.SiliaoshoumaiView;


/**
 * 宠物用品售卖
 *
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface SiliaoshoumaiService extends IService<SiliaoshoumaiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<SiliaoshoumaiVO> selectListVO(Wrapper<SiliaoshoumaiEntity> wrapper);
   	
   	SiliaoshoumaiVO selectVO(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
   	
   	List<SiliaoshoumaiView> selectListView(Wrapper<SiliaoshoumaiEntity> wrapper);
   	
   	SiliaoshoumaiView selectView(@Param("ew") Wrapper<SiliaoshoumaiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<SiliaoshoumaiEntity> wrapper);
   	
}

