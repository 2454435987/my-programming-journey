package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.YiqichongwuxinxiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.YiqichongwuxinxiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.YiqichongwuxinxiView;


/**
 * 遗弃宠物信息
 *
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public interface YiqichongwuxinxiService extends IService<YiqichongwuxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YiqichongwuxinxiVO> selectListVO(Wrapper<YiqichongwuxinxiEntity> wrapper);
   	
   	YiqichongwuxinxiVO selectVO(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
   	
   	List<YiqichongwuxinxiView> selectListView(Wrapper<YiqichongwuxinxiEntity> wrapper);
   	
   	YiqichongwuxinxiView selectView(@Param("ew") Wrapper<YiqichongwuxinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<YiqichongwuxinxiEntity> wrapper);
   	
}

