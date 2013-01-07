package com.amaker.wlo.util;

public class OrderUrlUtil {
	/**
	 * 用户注册URL
	 */
	public static final String REGISTER_URL = "servlet/RegisterServlet?";
	
	/**
	 * 用户登陆URL
	 */
	public static final String LOGIN_URL = "servlet/LoginServlet?";
	
	/**
	 * 用户修改基本信息
	 */
	public static final String MODIFY_BASE_INFOR = "servlet/UserBaseModifyServlet?";
	
	/**
	 * 用户修改密码
	 */
	public static final String MODIFY_PASSWORD_INFOR = "servlet/UserModifyPasswordServlet?";
	
	/**
	 * 检查菜单是否需要更新URL
	 */
	public static final String CHECK_ORDER_VERSION = "servlet/CheckOrderVersionServlet?";
	
	/**
	 * 更新菜单
	 */
	public static final String UPDATE_ORDERS = "servlet/UpdateServlet?";
	
	/**
	 * 点菜URL
	 */
	public static final String MAKE_ORDER = "servlet/MakeOrderServlet?";
	
	/**
	 * Google 天气预报API接口URL
	 */
	public static final String WEATHER_URL = "http://www.google.com/ig/api?hl=zh-cn&weather=,,,";
	
	/**
	 * Google 官网地址 URL
	 */
	public static final String GOOGLE_URL = "http://www.google.com/";
}
