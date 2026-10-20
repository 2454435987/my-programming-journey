package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DiscusssiliaoshoumaiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DiscusssiliaoshoumaiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DiscusssiliaoshoumaiView;


/**
 * 宠物用品售卖评论表
 *
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface DiscusssiliaoshoumaiService extends IService<DiscusssiliaoshoumaiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscusssiliaoshoumaiVO> selectListVO(Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
   	
   	DiscusssiliaoshoumaiVO selectVO(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
   	
   	List<DiscusssiliaoshoumaiView> selectListView(Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
   	
   	DiscusssiliaoshoumaiView selectView(@Param("ew") Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscusssiliaoshoumaiEntity> wrapper);
   	
}

