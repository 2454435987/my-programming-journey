package com.entity.view;

import com.entity.YiqichongwuxinxiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 遗弃宠物信息
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
@TableName("yiqichongwuxinxi")
public class YiqichongwuxinxiView  extends YiqichongwuxinxiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public YiqichongwuxinxiView(){
	}
 
 	public YiqichongwuxinxiView(YiqichongwuxinxiEntity yiqichongwuxinxiEntity){
 	try {
			BeanUtils.copyProperties(this, yiqichongwuxinxiEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
