package com.kwan.base.download;

import com.kwan.base.common.bean.DownLoadFileBlockBean;

import java.util.List;

/**
 * Created by Mr.Kwan on 2016-6-29.
 */
public interface DownloadFileCallBack<T> {

	void onDownloadSuccess(T t);

	/**
	 * @param progress 已经下载或上传字节数
	 */
	void onDownloadProgress(long progress);

	void closeCallBack(DownLoadFileBlockBean blockBean);

	void pauseCallBack(DownLoadFileBlockBean blockBean);

	void blockDownLoadFinished(DownLoadFileBlockBean blockBean);

	void onDownloadPrepare(List<DownLoadFileBlockBean> blockBeans,int finishedProgress);
}
