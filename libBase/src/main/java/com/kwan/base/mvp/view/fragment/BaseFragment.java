package com.kwan.base.mvp.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.kwan.base.R;
import com.kwan.base.common.image.ImageLoader;
import com.kwan.base.common.image.glide.GlideUtil;
import com.kwan.base.mvp.presenter.IBaseView;
import com.kwan.base.mvp.view.IGetPageName;
import com.kwan.base.mvp.view.activity.BaseActivity;
import com.umeng.analytics.MobclickAgent;


/**
 * fragment基类
 * Created by Mr.Kwan on 2016-5-4.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, IBaseView, IGetPageName {

	public static boolean LOG = false;
	protected LayoutInflater mInflater;
	protected BaseActivity mBaseActivity;
	protected Context mContext;
	/**
	 * fragment最外层包装view
	 */
	protected View mBaseContentView;

	protected View mBottomView;
	protected View mTitleView;
	protected View mContentView;
	protected ImageLoader mImageLoader;

	private boolean isActive;

	protected ImmersionBar mImmersionBar;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		mBaseActivity = (BaseActivity) context;
		mInflater = mBaseActivity.getLayoutInflater();

		mImageLoader = new GlideUtil();
		if (LOG)
			Log.d(getTag(), "onAttach");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseContentView();
		if (LOG)
			Log.d(getTag(), "onCreate");
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (LOG)
			Log.d(getTag(), "onCreateView");
		return mBaseContentView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (LOG)
			Log.d(getTag(), "onViewCreated");
		initImmersionBar();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (LOG)
			Log.d(getTag(), "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		isActive = true;
		if (LOG)
			Log.d(getTag(), "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(mContext);
		if (LOG)
			Log.d(getTag(), "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(mContext);
		isActive = false;
		if (LOG)
			Log.d(getTag(), "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (LOG)
			Log.d(getTag(), "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (LOG)
			Log.d(getTag(), "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (LOG)
			Log.d(getTag(), "onDetach");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mImmersionBar != null)
			mImmersionBar.destroy();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onNoNetWork() {

	}

	@Override
	public void toastMsg(String msg) {
		Toast.makeText(mBaseActivity, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void dismissProgress() {
		mBaseActivity.dismissProgress();
	}

	public void showProgress(String txt) {
		mBaseActivity.showProgress(txt);
	}

	/**
	 * frament 是否暂停
	 *
	 * @return
	 */

	public boolean isActive() {
		return isActive;
	}

	private void initBaseContentView() {

		mBaseContentView = mInflater.inflate(R.layout.layout_base, null);
		ViewStub stubTitleContent = mBaseContentView.findViewById(R.id.view_stub_title);
		ViewStub stubMainContent = mBaseContentView.findViewById(R.id.view_stub_content);
		ViewStub stubMainBottom = mBaseContentView.findViewById(R.id.view_stub_bottom);

		if (stubMainContent != null) {
			stubMainContent.setLayoutResource(getContentViewId());
			mContentView = stubMainContent.inflate();
		}
		//有自定义 title bar
		if (stubTitleContent != null && getTitleBarViewId() != 0) {
			stubTitleContent.setLayoutResource(getTitleBarViewId());
			mTitleView = stubTitleContent.inflate();
		}

		if (stubMainBottom != null && getBottomViewId() != 0) {
			stubMainBottom.setLayoutResource(getBottomViewId());
			mBottomView = stubMainBottom.inflate();
		}


	}

	public void go2ActivityWithLeft(Class<? extends Activity> activity, Bundle bundle, boolean isFinish) {

		Intent intent = new Intent(mBaseActivity, activity);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);

		// if (enterAnim != 0 && exitAnim != 0)
		mBaseActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		if (isFinish)
			mBaseActivity.onBackPressed();

	}

	public void go2Activity(Class<? extends Activity> activity, Bundle bundle, boolean isFinish, int enterAnim, int exitAnim) {

		Intent intent = new Intent(mBaseActivity, activity);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);
		// if (enterAnim != 0 && exitAnim != 0)
		mBaseActivity.overridePendingTransition(enterAnim, exitAnim);
		if (isFinish)
			mBaseActivity.onBackPressed();

	}

	public void go2Activity(Class<? extends Activity> activity, Bundle bundle, boolean isFinish) {

		Intent intent = new Intent(mBaseActivity, activity);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);
		if (isFinish)
			mBaseActivity.onBackPressed();
	}


	protected int getTitleBarViewId() {
		return 0;
	}

	protected int getBottomViewId() {
		return 0;
	}

	protected abstract int getContentViewId();



	/**
	 * 是否对用户可见
	 */
	protected boolean mIsVisible;
	/**
	 * 是否加载完成
	 * 当执行完onViewCreated方法后即为true
	 */
	protected boolean mIsPrepare;

	/**
	 * 是否加载完成
	 * 当执行完onViewCreated方法后即为true
	 */
	protected boolean mIsImmersion;

	/**
	 * 是否懒加载
	 *
	 * @return the boolean
	 */
	protected boolean isLazyLoad() {
		return true;
	}

	/**
	 * 是否在Fragment使用沉浸式
	 *
	 * @return the boolean
	 */
	protected boolean isImmersionBarEnabled() {
		return true;
	}

	/**
	 * 用户可见时执行的操作
	 */
	protected void onVisible() {
		onLazyLoad();
	}

	private void onLazyLoad() {
		if (mIsVisible && mIsPrepare) {
			mIsPrepare = false;
		}
		if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
			initImmersionBar();
		}
	}

	/**
	 * 初始化沉浸式
	 */
	protected void initImmersionBar() {
		mImmersionBar = ImmersionBar.with(this);
		mImmersionBar.navigationBarWithKitkatEnable(false).init();
	}

}
