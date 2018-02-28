package com.kwan.base.mvp.presenter;

import java.util.HashMap;

/**
 * Created by Mr.Kwan on 2017-8-15.
 */

public interface IBasePresenter {

	void onNoNetWork();

	void onServerSuccess(int vocational_id, HashMap<String, Object> exData, Object serverMsg);

	void onServerError(int vocational_id, HashMap<String, Object> exData, Throwable e);

	void onServerFailed(String s);

	void onServerUploadError(int vocational_id, HashMap exdata, Throwable e);

	void onServerUploadCompleted(int vocational_id, HashMap exdata);

	void onServerUploadNext(int vocational_id, HashMap exdata, Object s);
}
