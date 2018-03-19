package com.kwan.base.download;

import android.util.Log;

import com.kwan.base.api.BaseAPIUtil;
import com.kwan.base.api.BaseServerAPI;
import com.kwan.base.api.ObjectServerSubscriber;
import com.kwan.base.common.bean.DownLoadFileBlockBean;
import com.kwan.base.mvp.model.BaseModel;
import com.kwan.base.mvp.model.db.BaseDao;
import com.kwan.base.mvp.presenter.IBasePresenter;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 *
 * Created by Administrator on 2018/3/15.
 */

public class DownLoadModel extends BaseModel {

	private DownloadFileCallBack callBack;
	private Boolean isPause = false;
	private Boolean isClose = false;
	public int downloadThreadCount = 3;

	public void setPause(Boolean pause) {
		isPause = pause;
	}

	public void setClose(Boolean close) {
		isClose = close;
	}

	public DownLoadModel(IBasePresenter iBasePresenter, DownloadFileCallBack callBack) {
		super(iBasePresenter);
		this.callBack = callBack;
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

	public void download(DownloadFileBean downloadFileBean) {
		getDownLoadLength(downloadFileBean);
	}

	/**
	 * 获取下载文件的长度
	 */
	private void getDownLoadLength(final DownloadFileBean downloadFileBean) {

		mBaseAPIUtil.download("0", downloadFileBean.getUrl(), new DownloadProgressInterceptor.DownloadProgressListener() {
			@Override
			public void update(long bytesRead, long contentLength, boolean done) {

			}
		}).subscribeOn(Schedulers.io())
				.observeOn(Schedulers.io())
				.doOnNext(new Consumer<ResponseBody>() {
					@Override
					public void accept(@NonNull ResponseBody responseBody) throws Exception {
						downloadFileBean.setLength(responseBody.contentLength());
						prepareDownloadFile(downloadFileBean);
					}}).subscribe();
	}


	/**
	 * 多线程下载
	 *
	 * @param start 开始位置
	 * @param url   下载地址
	 */

	private void download(final String start, String url, final DownLoadFileBlockBean blockBean, final DownloadFileBean fileBean) {

		Disposable disposable = mBaseAPIUtil.download(start, url, new DownloadProgressInterceptor.DownloadProgressListener() {
			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
			}
		}).subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
				.observeOn(Schedulers.io()) //指定线程保存文件
				.doOnNext(new Consumer<ResponseBody>() {
					@Override
					public void accept(@NonNull ResponseBody responseBody) throws Exception {
						saveMergerFile(responseBody, blockBean, fileBean);
					}
				})//在主线程中更新ui
				.subscribe();

		addDisposable(BaseServerAPI.RANGE_DOWN_LOAD_VOCATIONAL_ID, disposable);
	}

	/**
	 * 单线程下载
	 *
	 * @param url      下载地址
	 * @param callBack 下载回调
	 */

	public void download(final String url, final DownloadFileCallBack<ResponseBody> callBack) {

		final ObjectServerSubscriber<ResponseBody> subscriber = new ObjectServerSubscriber<>(this);
		subscriber.vocational_id = BaseServerAPI.DOWN_LOAD_VOCATIONAL_ID;

		Disposable disposable = mBaseAPIUtil.download(url, new DownloadProgressInterceptor.DownloadProgressListener() {
			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
				Log.e("kwan", "normal down:" + bytesRead);
				//	callBack.onDownloadProgress(bytesRead, contentLength, done);
			}
		}).subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
				.observeOn(Schedulers.io()) //指定线程保存文件
				.doOnNext(new Consumer<ResponseBody>() {
					@Override
					public void accept(@NonNull ResponseBody responseBody) throws Exception {
						Log.e("kwan", "download accept");
						//	saveFile(responseBody);
					}
				})
				.observeOn(AndroidSchedulers.mainThread()) //在主线程中更新ui
				.subscribeWith(subscriber);

		addDisposable(BaseServerAPI.DOWN_LOAD_VOCATIONAL_ID, disposable);

	}


	/**
	 * 准备下载 平分分线程
	 *
	 * @param downloadFileBean
	 */

	private void prepareDownloadFile(DownloadFileBean downloadFileBean) {

		BaseDao<DownLoadFileBlockBean> dao = new BaseDao<>();
		//查询数据库中的下载线程信息
		List<DownLoadFileBlockBean> blockBeans = dao.QueryObject(DownLoadFileBlockBean.class, "where URL=?", downloadFileBean.getUrl());

		if (blockBeans.size() == 0) {//如果列表没有数据 则为第一次下载
			//根据下载的线程总数平分各自下载的文件长度
			long length = downloadFileBean.getLength() / downloadThreadCount;
			for (long i = 0; i < downloadThreadCount; i++) {

				DownLoadFileBlockBean blockBean = new DownLoadFileBlockBean(i, downloadFileBean.getUrl(), i * length,
						(i + 1) * length - 1, 0L);

				if (i == downloadThreadCount - 1) {
					blockBean.setEnd(downloadFileBean.getLength());
				}
				//将下载线程保存到数据库
				dao.insertObject(blockBean);
				blockBeans.add(blockBean);
			}
		}

		//创建下载线程开始下载
		int  finishedProgress = 0;
		for (DownLoadFileBlockBean blockBean : blockBeans) {
			finishedProgress += blockBean.getFinished();
			//开始下载
			long start = blockBean.getStart() + blockBean.getFinished();
			download("bytes=" + start + "-" + blockBean.getEnd(), blockBean.getUrl(), blockBean, downloadFileBean);
		}

		callBack.onDownloadPrepare(blockBeans,finishedProgress);
	}




//	public void saveFile(ResponseBody body) {
//
//		InputStream is = null;
//		byte[] buf = new byte[2048];
//		int len;
//		FileOutputStream fos = null;
//		try {
//			is = body.byteStream();
//			fos = new FileOutputStream(getFile(fileBean));
//			while ((len = is.read(buf)) != -1) {
//				fos.write(buf, 0, len);
//			}
//			fos.flush();
//			//unsubscribe();
//			//callBack.onDownloadCompleted();
//
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (fos != null) fos.close();
//			} catch (IOException e) {
//				Log.e("saveFile", e.getMessage());
//			}
//		}
//	}

	public void saveMergerFile(ResponseBody body, DownLoadFileBlockBean blockBean, DownloadFileBean fileBean) {

		try {

			//设置写入位置
			File file = getFile(fileBean);
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(blockBean.getStart() + blockBean.getFinished());

			//开始下载
			InputStream inputStream = body.byteStream();
			byte[] bytes = new byte[1024];
			int len;
			while ((len = inputStream.read(bytes)) != -1) {
				raf.write(bytes, 0, len);
				//将加载的进度回调出去
				callBack.onDownloadProgress(len);
				//保存进度
				blockBean.setFinished(blockBean.getFinished() + len);
				//在下载暂停的时候将下载进度保存到数据库
				if (isClose) {
					callBack.closeCallBack(blockBean);
					return;
				}
				if (isPause) {
					callBack.pauseCallBack(blockBean);
					return;
				}
			}
			callBack.blockDownLoadFinished(blockBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private File getFile(DownloadFileBean fileBean) {
		File dir = new File(fileBean.getSavePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return new File(dir, fileBean.getFileName());
	}

}
