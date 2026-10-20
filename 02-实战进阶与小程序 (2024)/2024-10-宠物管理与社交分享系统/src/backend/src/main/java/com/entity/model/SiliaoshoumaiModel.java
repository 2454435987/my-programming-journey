package com.entity.model;

import com.entity.SiliaoshoumaiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
 

/**
 * 宠物用品售卖
 * 接收传参的实体类  
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了） 
 * 取自ModelAndView 的model名称
 * @author 
 * @email 
 * @date 2026-02-28 01:03:37
 */
public class SiliaoshoumaiModel  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 宠物用品名称
	 */
	
	private String siliaomingcheng;
		
	/**
	 * 图片
	 */
	
	private String tupian;
		
	/**
	 * 宠物用品规格
	 */
	
	private String siliaoguige;
		
	/**
	 * 适合宠物
	 */
	
	private String shihechongwu;
		
	/**
	 * 注意事项
	 */
	
	private String zhuyishixiang;
		
	/**
	 * 宠物用品详情
	 */
	
	private String siliaoxiangqing;
		
	/**
	 * 赞
	 */
	
	private Integer thumbsupnum;
		
	/**
	 * 踩
	 */
	
	private Integer crazilynum;
		
	/**
	 * 最近点击时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date clicktime;
		
	/**
	 * 价格
	 */
	
	private Float price;
				
	
	/**
	 * 设置：宠物用品名称
	 */
	 
	public void setSiliaomingcheng(String siliaomingcheng) {
		this.siliaomingcheng = siliaomingcheng;
	}
	
	/**
	 * 获取：宠物用品名称
	 */
	public String getSiliaomingcheng() {
		return siliaomingcheng;
	}
				
	
	/**
	 * 设置：图片
	 */
	 
	public void setTupian(String tupian) {
		this.tupian = tupian;
	}
	
	/**
	 * 获取：图片
	 */
	public String getTupian() {
		return tupian;
	}
				
	
	/**
	 * 设置：宠物用品规格
	 */
	 
	public void setSiliaoguige(String siliaoguige) {
		this.siliaoguige = siliaoguige;
	}
	
	/**
	 * 获取：宠物用品规格
	 */
	public String getSiliaoguige() {
		return siliaoguige;
	}
				
	
	/**
	 * 设置：适合宠物
	 */
	 
	public void setShihechongwu(String shihechongwu) {
		this.shihechongwu = shihechongwu;
	}
	
	/**
	 * 获取：适合宠物
	 */
	public String getShihechongwu() {
		return shihechongwu;
	}
				
	
	/**
	 * 设置：注意事项
	 */
	 
	public void setZhuyishixiang(String zhuyishixiang) {
		this.zhuyishixiang = zhuyishixiang;
	}
	
	/**
	 * 获取：注意事项
	 */
	public String getZhuyishixiang() {
		return zhuyishixiang;
	}
				
	
	/**
	 * 设置：宠物用品详情
	 */
	 
	public void setSiliaoxiangqing(String siliaoxiangqing) {
		this.siliaoxiangqing = siliaoxiangqing;
	}
	
	/**
	 * 获取：宠物用品详情
	 */
	public String getSiliaoxiangqing() {
		return siliaoxiangqing;
	}
				
	
	/**
	 * 设置：赞
	 */
	 
	public void setThumbsupnum(Integer thumbsupnum) {
		this.thumbsupnum = thumbsupnum;
	}
	
	/**
	 * 获取：赞
	 */
	public Integer getThumbsupnum() {
		return thumbsupnum;
	}
				
	
	/**
	 * 设置：踩
	 */
	 
	public void setCrazilynum(Integer crazilynum) {
		this.crazilynum = crazilynum;
	}
	
	/**
	 * 获取：踩
	 */
	public Integer getCrazilynum() {
		return crazilynum;
	}
				
	
	/**
	 * 设置：最近点击时间
	 */
	 
	public void setClicktime(Date clicktime) {
		this.clicktime = clicktime;
	}
	
	/**
	 * 获取：最近点击时间
	 */
	public Date getClicktime() {
		return clicktime;
	}
				
	
	/**
	 * 设置：价格
	 */
	 
	public void setPrice(Float price) {
		this.price = price;
	}
	
	/**
	 * 获取：价格
	 */
	public Float getPrice() {
		return price;
	}
			
}
