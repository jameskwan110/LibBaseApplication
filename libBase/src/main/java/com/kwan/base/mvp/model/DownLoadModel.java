package com.kwan.base.mvp.model;

import android.util.Log;

import com.kwan.base.api.BaseAPIUtil;
import com.kwan.base.api.BaseServerAPI;
import com.kwan.base.api.ObjectServerSubscriber;
import com.kwan.base.api.download.DownloadProgressInterceptor;
import com.kwan.base.api.download.FileCallBack;
import com.kwan.base.common.bean.DownLoadFileBlockBean;
import com.kwan.base.download.FileBean;
import com.kwan.base.mvp.model.db.BaseDao;
import com.kwan.base.mvp.presenter.IBasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/3/15.
 */

public class DownLoadModel extends BaseModel {

	public DownLoadModel(IBasePresenter iBasePresenter) {
		super(iBasePresenter);
	}

	@Override
	public BaseAPIUtil getBaseApiUtil() {
		return new BaseAPIUtil() {
			@Override
			protected String getBaseUrl() {
				return null;
			}

			@Override
			protected String getBaseTokenUrl() {
				return null;
			}

			@Override
			protected String getBaseUpLoadUrl() {
				return null;
			}

			@Override
			protected String getToken() {
				return null;
			}
		};
	}


	public void download(ArrayList<FileBean> fileBeans,final FileCallBack<ResponseBody> callBack) {
		for (FileBean fileBean : fileBeans)
			download(fileBean,callBack);
	}

	public void download(FileBean fileBean,final FileCallBack<ResponseBody> callBack) {
		getDownLoadLength(fileBean, callBack);
	}

	/**
	 * 获取下载文件的长度
	 */
	private void getDownLoadLength(final FileBean fileBean,final FileCallBack<ResponseBody> callBack) {

		final ObjectServerSubscriber<ResponseBody> subscriber = new ObjectServerSubscriber<>(this);
		subscriber.vocational_id = BaseServerAPI.RANGE_DOWN_LOAD_VOCATIONAL_ID;

		mBaseAPIUtil.download("0", fileBean.getUrl(), new DownloadProgressInterceptor.DownloadProgressListener() {
			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(Schedulers.io())
				.doOnNext(new Consumer<ResponseBody>() {
					@Override
					public void accept(@NonNull ResponseBody responseBody) throws Exception {
						Log.e("kwan", "download accept::" + responseBody.contentLength());

						fileBean.setLength(responseBody.contentLength());
						prepareDownloadFile(fileBean,callBack);

					}
				})
				.subscribeWith(subscriber);

	}


	/**
	 * 多线程下载
	 *
	 * @param start
	 * @param url
	 * @param callBack
	 */


	private void download(final String start, String url, final FileCallBack<ResponseBody> callBack) {

		final ObjectServerSubscriber<ResponseBody> subscriber = new ObjectServerSubscriber<>(this);
		subscriber.vocational_id = BaseServerAPI.RANGE_DOWN_LOAD_VOCATIONAL_ID;

		mBaseAPIUtil.download(start, url, new DownloadProgressInterceptor.DownloadProgressListener() {
			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
				callBack.onProgress(bytesRead, contentLength, done);
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(Schedulers.io())
				.doOnNext(new Consumer<ResponseBody>() {
					@Override
					public void accept(@NonNull ResponseBody responseBody) throws Exception {
						Log.e("kwan", "download accept::" + responseBody.contentLength());





					}
				})
				.subscribeWith(subscriber);
	}


	int downloadThreadCount = 3;
	/**
	 * 总下载完成进度
	 */
	private int finishedProgress = 0;


	/**
	 * 准备下载 平分分线程
	 *
	 * @param fileBean
	 */

	private void prepareDownloadFile(FileBean fileBean,final FileCallBack<ResponseBody> callBack) {

		BaseDao<DownLoadFileBlockBean> dao = new BaseDao<>();
		//查询数据库中的下载线程信息
		List<DownLoadFileBlockBean> blockBeans = dao.QueryObject(DownLoadFileBlockBean.class, "URL=?", fileBean.getUrl());

		if (blockBeans.size() == 0) {//如果列表没有数据 则为第一次下载
			//根据下载的线程总数平分各自下载的文件长度
			long length = fileBean.getLength() / downloadThreadCount;
			for (long i = 0; i < downloadThreadCount; i++) {

				DownLoadFileBlockBean thread = new DownLoadFileBlockBean(i, fileBean.getUrl(), i * length,
						(i + 1) * length - 1, 0L);

				if (i == downloadThreadCount - 1) {
					thread.setEnd(fileBean.getLength());
				}
				//将下载线程保存到数据库
				dao.insertObject(thread);
				blockBeans.add(thread);
			}
		}
		//创建下载线程开始下载
		for (DownLoadFileBlockBean blockBean : blockBeans) {

			finishedProgress += blockBean.getFinished();
			//DownloadThread downloadThread = new DownloadThread(fileBean, thread, this);
			//开始下载
			//VersionUpdateService.executorService.execute(downloadThread);
			long start = blockBean.getStart() + blockBean.getFinished();
			//connection.setRequestProperty("Range","bytes="+start+"-"+threadBean.getEnd());
			download("bytes="+start+"-"+blockBean.getEnd(),blockBean.getUrl(),callBack);
			//downloadThreads.add(downloadThread);
		}
	}



}
