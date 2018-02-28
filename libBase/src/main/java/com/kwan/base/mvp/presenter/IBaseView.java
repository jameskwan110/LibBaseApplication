package com.kwan.base.mvp.presenter;

/**
 *
 * Created by Mr.Kwan on 2016-10-8.
 */

public interface IBaseView {

    void onNoNetWork();

	void toastMsg(String msg);

	void dismissProgress();

}
