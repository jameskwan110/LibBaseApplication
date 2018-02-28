package com.kwan.base.mvp.view.fragment;

import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

/**
 * 防止预加载
 * Created by Mr.Kwan on 2016-7-14.
 */
public abstract class BasePageItemFragment extends BaseFragment {

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

	protected String getItemTag(){
		return "defalt";
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
		Log.d(getItemTag(),"onActivityCreated::"+isViewInitiated);
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
		Log.d(getItemTag(),"setUserVisibleHint::"+isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;

        prepareFetchData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewInitiated =false;
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {

        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

	/**
	 * 获取数据
	 */
	public abstract void fetchData();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }

}
