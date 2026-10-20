package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DiscussyiqichongwuxinxiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DiscussyiqichongwuxinxiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DiscussyiqichongwuxinxiView;


/**
 * 遗弃宠物信息评论表
 *
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface DiscussyiqichongwuxinxiService extends IService<DiscussyiqichongwuxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscussyiqichongwuxinxiVO> selectListVO(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
   	
   	DiscussyiqichongwuxinxiVO selectVO(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
   	
   	List<DiscussyiqichongwuxinxiView> selectListView(Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
   	
   	DiscussyiqichongwuxinxiView selectView(@Param("ew") Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscussyiqichongwuxinxiEntity> wrapper);
   	
}

