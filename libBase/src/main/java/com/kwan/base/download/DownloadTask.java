package com.kwan.base.download;

import android.content.Context;
import android.content.Intent;

import com.kwan.base.common.bean.DownLoadFileBlockBean;
import com.kwan.base.common.config.Config;
import com.kwan.base.mvp.model.db.BaseDao;
import com.kwan.base.mvp.presenter.IBasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;

/**
 *
 * Created by Administrator on 2018/3/16.
 */

public class DownloadTask implements IBasePresenter, DownloadFileCallBack<ResponseBody> {

	private long taskId;
	private DownloadFileBean fileBean;
	private DownLoadModel downLoadModel;
	private Context context;
	private List<DownLoadFileBlockBean> blockBeans = new ArrayList<>();
	private List<DownLoadFileBlockBean> blockBeans2delete = new ArrayList<>();

	public DownloadFileBean getFileBean() {
		return fileBean;
	}

	public DownloadTask(Context context, DownloadFileBean fileBean) {
		this.fileBean = fileBean;
		this.context = context;
		downLoadModel = new DownLoadModel(this, this);
	}

	public DownloadTask startDownload() {
		downLoadModel.download(fileBean);
		return this;
	}


	@Override
	public void onNoNetWork() {

	}

	@Override
	public void onServerSuccess(int vocational_id, HashMap<String, Object> exData, Object serverMsg) {

	}

	@Override
	public void onServerError(int vocational_id, HashMap<String, Object> exData, Throwable e) {

	}

	@Override
	public void onServerFailed(String s) {

	}

	@Override
	public void onServerUploadError(int vocational_id, HashMap exdata, Throwable e) {

	}

	@Override
	public void onServerUploadCompleted(int vocational_id, HashMap exdata) {

	}

	@Override
	public void onServerUploadNext(int vocational_id, HashMap exdata, Object s) {

	}

	@Override
	public void onDownloadSuccess(ResponseBody responseBody) {

	}

	/**
	 * 总下载完成进度
	 */
	private int finishedProgress = 0;
	private long curTime = 0;
	private int speed = 0;

	@Override
	public void onDownloadProgress(long length) {

		finishedProgress += length;
		speed += length;
		//每500毫秒发送刷新进度事件
		long time = System.currentTimeMillis() - curTime;
		if (time > 500 || finishedProgress == fileBean.getLength()) {

			fileBean.setFinished(finishedProgress);
			Intent intent = new Intent(Config.ACTION_DOWNLOAD_REFRESH);
			speed = (int) (speed / (time * 0.001));
			intent.putExtra("Speed", speed);
			intent.putExtra("DownloadFileBean", fileBean);
			context.sendBroadcast(intent);
			curTime = System.currentTimeMillis();
			speed = 0;

		}

	}

	//取消下载 删除
	@Override
	public void closeCallBack(DownLoadFileBlockBean blockBean) {
		new BaseDao<DownLoadFileBlockBean>().deleteObject(blockBean);
		Intent intent = new Intent(Config.ACTION_DOWNLOAD_CLOSE);
		intent.putExtra("DownloadFileBean", fileBean);
		context.sendBroadcast(intent);
	}

	@Override
	public void pauseCallBack(DownLoadFileBlockBean blockBean) {

		new BaseDao<DownLoadFileBlockBean>().updateObject(blockBean);
		Intent intent = new Intent(Config.ACTION_DOWNLOAD_PAUSE);
		intent.putExtra("DownloadFileBean", fileBean);
		context.sendBroadcast(intent);
	}

	@Override
	public synchronized void blockDownLoadFinished(DownLoadFileBlockBean blockBean) {

		for (DownLoadFileBlockBean bean : blockBeans) {
			if (bean.getId() == blockBean.getId()) {
				//从列表中将已下载完成的线程信息移除
				blockBeans.remove(bean);
				break;
			}
		}

		if (blockBeans.size() == 0) {//如果列表size为0 则所有线程已下载完成

			for (DownLoadFileBlockBean bean : blockBeans2delete) {
				//删除数据库中的信息
				new BaseDao<DownLoadFileBlockBean>().deleteObject(bean);
				//发送下载完成事件
			}

			blockBeans.clear();
			blockBeans2delete.clear();
			blockBeans = null;
			blockBeans2delete = null;

			Intent intent = new Intent(Config.ACTION_DOWNLOAD_FININSHED);
			intent.putExtra("DownloadFileBean", fileBean);
			context.sendBroadcast(intent);

		}

	}

	@Override
	public void onDownloadPrepare(List<DownLoadFileBlockBean> blockBeans,int finishedProgress) {
		this.finishedProgress = finishedProgress;
		this.blockBeans.addAll(blockBeans);
		blockBeans2delete.addAll(blockBeans);
	}


	/**
	 * 暂停下载
	 */
	public void pauseDownload() {
		downLoadModel.setPause(true);
	}

	/**
	 * 取消下载
	 */
	public void closeDownload() {
		downLoadModel.setClose(true);
	}
}
