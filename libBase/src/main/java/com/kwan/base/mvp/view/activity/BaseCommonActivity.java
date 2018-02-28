package com.kwan.base.mvp.view.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.kwan.base.R;
import com.kwan.base.util.ViewUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 带有标题栏的Activity
 */

public abstract class BaseCommonActivity extends BaseActivity implements View.OnClickListener {

	protected ImageView iv_title_back;
	protected EditText et_title;
	protected ViewStub stubTitleRight;
	protected AutoLinearLayout ll_title_back;
	protected AutoRelativeLayout layoutTitle;
	protected View mMainView, mTopView, mBottomView;

	@Override
	protected void initViews() {

		if (getTitleTxt() != null) {
			layoutTitle = (AutoRelativeLayout) findViewById(R.id.layout_title);
			ll_title_back = (AutoLinearLayout) findViewById(R.id.ll_title_back);
			iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
			et_title = (EditText) findViewById(R.id.et_title);
			stubTitleRight = (ViewStub) findViewById(R.id.view_stub_common_title_right);

		}
		initCommonView();
	}

	private void initCommonView() {

		ViewStub stubTop = (ViewStub) findViewById(R.id.common_stub_top);
		ViewStub stubMain = (ViewStub) findViewById(R.id.common_stub_main);
		ViewStub stubBottom = (ViewStub) findViewById(R.id.common_stub_bottom);

		if (stubMain != null && getMainViewId() != 0) {
			stubMain.setLayoutResource(getMainViewId());
			mMainView = stubMain.inflate();
		}
		//有自定义 title bar
		if (stubTop != null && getTopViewId() != 0) {
			stubTop.setLayoutResource(getTopViewId());
			mTopView = stubTop.inflate();
		}

		if (stubBottom != null && getBottomViewId() != 0) {
			stubBottom.setLayoutResource(getBottomViewId());
			mBottomView = stubBottom.inflate();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
		ImmersionBar.with(this).destroy();
	}

	protected abstract int getTopViewId();

	protected abstract int getMainViewId();

	protected abstract int getBottomViewId();

	@Override
	protected int getContentViewId() {
		return R.layout.activtity_base_common;
	}

	@Override
	protected void initViewSetting() {

		if (getTitleTxt() != null) {
			ll_title_back.setOnClickListener(this);
			//tv_title.setTypeface(Typeface.DEFAULT_BOLD);
			et_title.setText(getTitleTxt());
		}

		if (getTitleBarRightLayoutId() != 0) {
			stubTitleRight.setLayoutResource(getTitleBarRightLayoutId());
			setUpTitleRightView(stubTitleRight.inflate());
		}

	}

	/**
	 * 设置顶部右标题栏
	 *
	 * @param v view
	 */

	protected void setUpTitleRightView(View v) {

		if (v instanceof ImageView) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtil.px2dip(this, 70), ViewUtil.px2dip(this, 70));
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			//lp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			v.setLayoutParams(lp);
		}
	}

	/**
	 * 设置背景
	 *
	 * @return 要设置的背景bitmap
	 */
	@Override
	protected Bitmap getBackGroundBitmap() {
		return null;
	}

	/**
	 * 设置顶部标题栏
	 *
	 * @return layout id
	 */
	@Override
	protected int getTitleBarViewId() {
		if (getTitleTxt() != null)
			return R.layout.activity_base_common_title_bar;
		else
			return 0;
	}

	@Override
	public void onClick(View v) {
		if (v == ll_title_back) {
			onBackPressed();
		}
	}


	@Override
	protected int getRootBottomViewId() {
		return 0;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	protected abstract String getTitleTxt();

	public void setTitleTxt(String str) {
		et_title.setText(str);
	}

	protected int getTitleBarRightLayoutId() {
		return 0;
	}

	@Override
	public void onNoNetWork() {
		toastMsg("当前无网络环境");
	}


}
