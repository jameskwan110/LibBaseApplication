package com.kwan.base.common.service;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.kwan.base.common.config.Config;
import com.kwan.base.download.DownloadFileBean;


public class VersionUpdateConfig {

	private static VersionUpdateConfig config = new VersionUpdateConfig();
	private DownloadFileBean fileBean;
	private Context context;

	private VersionUpdateConfig() {

	}

	public static VersionUpdateConfig getInstance() {
		return config;
	}

	/**
	 * 设置上下文
	 *
	 * @param context
	 * @return
	 */
	public VersionUpdateConfig setContext(Context context) {
		this.context = context;
		return config;
	}

	/**
	 * 设置文件保存路径
	 *
	 * @param path
	 * @return
	 */
	public VersionUpdateConfig setFileSavePath(String path) {
		Config.downLoadPath = path;
		return config;
	}

	/**
	 * 设置通知标题
	 */
	public VersionUpdateConfig setNotificationTitle(String title) {
		Config.notificationTitle = title;
		return config;
	}

	/**
	 * 设置通知图标
	 */
	public VersionUpdateConfig setNotificationIconRes(int resId) {
		Config.notificaionIconResId = resId;
		return config;
	}

	/**
	 * 设置通知小图标
	 */
	public VersionUpdateConfig setNotificationSmallIconRes(int resId) {
		Config.notificaionSmallIconResId = resId;
		return config;
	}

	/**
	 * 设置下载链接
	 *
	 * @param url
	 * @return
	 */
	public VersionUpdateConfig setDownLoadURL(String url) {
		fileBean = new DownloadFileBean(0,
				"ji_cloud.apk",
				Config.downLoadPath,
				url, 0, 0);
		return config;
	}

	/**
	 * 设置新包的版本号 用于区分是否下载过此包
	 *
	 * @param version 新包的版本号
	 * @return
	 */
	public VersionUpdateConfig setNewVersion(int version) {
		if (fileBean == null) {
			throw new NullPointerException("url cannot be null, you must call setDownLoadURL() before setNewVersion().");
		}
		fileBean.setVersion(version);
		fileBean.setFileName("ji_cloud_" + version+".apk");
		return config;
	}


	/**
	 * 开始下载
	 */
	public void startDownLoad() {
		if (context == null) {
			throw new NullPointerException("context cannot be null, you must first call setContext().");
		}
		if (fileBean == null) {
			throw new NullPointerException("url cannot be null, you must first call setDownLoadURL().");
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			context.startActivity(new Intent(context, TranslucentActivity.class));
		} else {
			passCheck();
		}
	}

	public void passCheck() {
		Intent startIntent = new Intent(context, VersionUpdateService.class);
		startIntent.setAction(VersionUpdateService.ACTION_START);
		startIntent.putExtra("DownloadFileBean", fileBean);
		context.startService(startIntent);
	}


}
