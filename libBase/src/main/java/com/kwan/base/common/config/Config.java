package com.kwan.base.common.config;

import android.os.Environment;

/**
 *
 */

public class Config {
    /**
     * 通知栏标题
     */
    public static String notificationTitle ;
    /**
     * 通知栏小图标
     */
    public static int notificaionSmallIconResId = 0;
    /**
     * 通知栏图标
     */
    public static int notificaionIconResId = 0;
    /**
     * 文件下载地址
     */
    public static String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/downloads/";

    /**
     * 开始下载
     */
    public final static String ACTION_DOWNLOAD_START = "ACTION_DOWNLOAD_START";
    /**
     * 暂停下载
     */
    public final static String ACTION_DOWNLOAD_PAUSE = "ACTION_DOWNLOAD_PAUSE";
    /**
     * 下载完成
     */
    public final static String ACTION_DOWNLOAD_FININSHED = "ACTION_DOWNLOAD_FININSHED";
    /**
     * 取消下载
     */
    public final static String ACTION_DOWNLOAD_CLOSE = "ACTION_DOWNLOAD_CLOSE";
    /**
     * 刷新下载进度
     */
    public final static String ACTION_DOWNLOAD_REFRESH = "ACTION_DOWNLOAD_REFRESH";
    /**
     * 下载失败
     */
    public final static String ACTION_DOWNLOAD_ERROR = "ACTION_DOWNLOAD_ERROR";
}
