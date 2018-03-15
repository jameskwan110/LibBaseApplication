package com.kwan.base.mvp.presenter;

import android.support.annotation.CallSuper;
import android.util.Log;

import com.kwan.base.api.download.FileCallBack;
import com.kwan.base.mvp.model.BaseModel;
import com.kwan.base.rxbus.RxBusManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Presenter 基类
 * Created by Mr.Kwan on 2016-9-30.
 */

public abstract class BasePresenter implements IBasePresenter {

	protected IBaseView mIBaseView;
	protected BaseModel mBaseModel;
	private RxBusManager busManager;

	public BasePresenter(IBaseView iView) {
		mIBaseView = iView;
		mBaseModel = getBaseModel();
		busManager = new RxBusManager();
	}

	/**
	 * 下载 文件
	 *
	 * @param url      全路径地址
	 * @param callBack 回调
	 */
	public void download(String url, FileCallBack<ResponseBody> callBack) {
		mBaseModel.download(url, callBack);
	}

	public void download(final String start, final String url, final String savePath, FileCallBack<ResponseBody> callBack) {
		mBaseModel.download(start, url, savePath, callBack);
	}

	public <T> void regRxBus(String eventName, Class<T> type, Consumer<T> next) {
		busManager.on(eventName, type, next);
	}

	public void postRxBus(Object tag, Object content) {
		busManager.post(tag, content);
	}

	@CallSuper
	public void onActivityCreate() {

	}

	@CallSuper
	public void onActivityResume() {
	}

	@CallSuper
	public void onActivityPause() {

	}

	@CallSuper
	public void onActivityDestroy() {
		busManager.clear();
	}

	@Override
	public void onNoNetWork() {
		mIBaseView.toastMsg("当前没有网络！");
	}

	@Override
	public void onServerSuccess(int vocational_id, HashMap<String, Object> exData, Object serverMsg) {
		Log.e("BasePresenter", "onServerSuccess");
	}

	@Override
	public void onServerError(int vocational_id, HashMap exdata, Throwable throwable) {
		if (throwable instanceof SocketTimeoutException) {
			mIBaseView.toastMsg("连接超时,请稍后重试！");
		} else if (throwable instanceof ConnectException) {
			mIBaseView.toastMsg("网络连接异常，请检查您的网络状态！");
		} else if (throwable instanceof UnknownHostException) {
			mIBaseView.toastMsg("网络异常，UnknownHostException！");
		} else {
			mIBaseView.toastMsg("网络错误！");
		}
		throwable.printStackTrace();
		Log.e("onServerError", throwable.getMessage());
		mIBaseView.dismissProgress();
	}


	@Override
	public void onServerFailed(String s) {
		mIBaseView.toastMsg(s);
	}


	@Override
	public void onServerUploadNext(int vocational_id, HashMap exdata, Object s) {
	}

	@Override
	public void onServerUploadCompleted(int vocational_id, HashMap exdata) {
	}

	@Override
	public void onServerUploadError(int vocational_id, HashMap exdata, Throwable throwable) {
		throwable.printStackTrace();
		if (throwable instanceof java.net.SocketTimeoutException) {
			mIBaseView.toastMsg("连接超时,请稍后重试！");
		} else {
			mIBaseView.toastMsg("网络错误！");
			Log.e("onServerError", throwable.getMessage());
		}
		mIBaseView.dismissProgress();
	}

	public abstract BaseModel getBaseModel();

}
