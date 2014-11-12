package com.xy.xypay.utils;
//Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 2013/11/11 10:44:19
//Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
//Decompiler options: packimports(3) 
//Source File Name:   ResourceUtil.java

import android.content.Context;

/** 
 * TODO 获取资源文件工具类
 * @author  pengsk 
 * @data:  2014年11月10日 下午4:27:26 
 * @version:  V1.0 
 */
public class ResourceUtil {

	public ResourceUtil() {
	}

	
	/**  
	 * TODO 获取布局文件id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());
	}
//
	
	/**  
	 * TODO 获取配置文件字符 id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */
	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}
	/**  
	 * TODO 获取配置文件 图片id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */

	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}
	/**  
	 * TODO 获取配置文件 动画id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */
	public static int getAnimId(Context paramContext,String paramString){
		return paramContext.getResources().getIdentifier(paramString,
				"anim", paramContext.getPackageName());
	}

	/**  
	 * TODO 获取配置文件style id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());
	}
	/**  
	 * TODO 获取配置文件控件id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */

	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	/**  
	 * TODO 获取配置文件颜色id
	 *  @param paramContext
	 *  @param paramString
	 * @return int 
	 */
	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());
	}
}
