package com.kwan.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kwan.base.BaseApplication;

/**
 * Created by Mr.Kwan on 2017-4-10.
 */

public class SPUtil {

	static{

		BZ_SP_APPINFO = BaseApplication.getInstance().getSharedPreferences("SP_APPINFO_FILE", Context.MODE_PRIVATE);
		BZ_SP_USERINFO = BaseApplication.getInstance().getSharedPreferences("SP_USERINFO_FILE", Context.MODE_PRIVATE);
	}

	/** APP信息：包括是否是第一次启动APP等 **/
	private static SharedPreferences BZ_SP_APPINFO;
	private static SharedPreferences.Editor sp_appinfo_editor;


	/** 用户信息：包括是否已登录等 **/
	private static SharedPreferences BZ_SP_USERINFO;
	private static SharedPreferences.Editor sp_userinfo_editor;


	static String KEY_IS_FIRST_CACHE = "is_first";
	static String KEY_IS_LOGIN = "is_login";

	/**
	 * 设置本应用是否第一次启动
	 * @param isFirstIn 是否第一次启动
	 * @return 是否提交成功
	 */
	public static boolean setIsFirstIn(boolean isFirstIn){
		sp_appinfo_editor = BZ_SP_APPINFO.edit();
		sp_appinfo_editor.putBoolean(KEY_IS_FIRST_CACHE, isFirstIn);
		return sp_appinfo_editor.commit();
	}

	/**
	 * 获取本应用是否第一次启动(默认是true，是第一次启动，直接启动Splash效果)
	 * @return 是否第一次启动 默认是true
	 */
	public static boolean getIsFirstIn(){
		return BZ_SP_APPINFO.getBoolean(KEY_IS_FIRST_CACHE, true);
	}


	//--------------------USERINFO--------------------------------------


	/** 1474358881557 1474359702385
	 * 保存用户的登录状态
	 * @param isLogin true：登录，false：未登录
	 */
	public static void setIsLogin(boolean isLogin) {
		sp_userinfo_editor = BZ_SP_USERINFO.edit();
		sp_userinfo_editor.putBoolean(KEY_IS_LOGIN, isLogin);
		sp_userinfo_editor.apply();

	}

	/**
	 * 获取用户的登录状态( 默认为 false，未登录)
	 * @return
	 */
	public static boolean getIsLogin() {
		return BZ_SP_USERINFO.getBoolean(KEY_IS_LOGIN, false);
	}

}
