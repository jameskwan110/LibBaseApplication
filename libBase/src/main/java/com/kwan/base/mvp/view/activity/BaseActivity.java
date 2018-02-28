package com.kwan.base.mvp.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.kwan.base.R;
import com.kwan.base.common.image.ImageLoader;
import com.kwan.base.common.image.glide.GlideUtil;
import com.kwan.base.common.widget.LoadingDialog;
import com.kwan.base.mvp.model.BaseModel;
import com.kwan.base.mvp.presenter.BasePresenter;
import com.kwan.base.mvp.presenter.IBaseView;
import com.kwan.base.mvp.view.IGetPageName;
import com.kwan.base.util.PermissionUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLinearLayout;


/**
 * 基类Activity 项目所有Activity都应该继承此类
 * Created by Mr.Kwan on 2017-7-19.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView, PermissionUtil.PermissionListener,IGetPageName {

	protected String TAG;
	protected InputMethodManager mInputMethodManager;
	protected View mTitleView, mRootContentView, mRootBottomView, mViewBar;
	protected AutoLinearLayout ll_root_main;
	private boolean mLastVisiable;
	private LoadingDialog mLoadingDialog;
	protected PermissionUtil mPermissionUtil;
	protected BasePresenter mBasePresenter;
	protected ImageLoader mImageLoader;
	private static final String[] VIDEO_PERMISSIONS = {
			Manifest.permission.CAMERA,
			Manifest.permission.RECORD_AUDIO,
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		beForeSetContentView();
		setContentView(R.layout.layout_base);
		initBaseViews();
		//初始化沉浸式状态栏
		initBar();
		initViews();
		initViewSetting();
		initData();
		addOnSoftKeyBoardVisibleListener();
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getPageName());
		MobclickAgent.onResume(this);
		if (mBasePresenter != null)
			mBasePresenter.onActivityResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getPageName());
		MobclickAgent.onPause(this);
		if (mBasePresenter != null) {
			mBasePresenter.onActivityPause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBasePresenter != null)
			mBasePresenter.onActivityDestroy();
	}

	protected void beForeSetContentView() {

		if (isFullScreen()) {
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		init();
	}

	public boolean isLandScreen() {
		return false;
	}

	private void initBaseViews() {

		mLoadingDialog = new LoadingDialog(this);
		mLoadingDialog.setCanceledOnTouchOutside(false);
		ll_root_main = (AutoLinearLayout) findViewById(R.id.ll_base_main);
		mViewBar = findViewById(R.id.view_bar);

		ViewStub stubTitleContent = (ViewStub) findViewById(R.id.view_stub_title);
		ViewStub stubMainContent = (ViewStub) findViewById(R.id.view_stub_content);
		ViewStub stubMainBottom = (ViewStub) findViewById(R.id.view_stub_bottom);

		if (stubMainContent != null) {
			stubMainContent.setLayoutResource(getContentViewId());
			mRootContentView = stubMainContent.inflate();
		}
		//有自定义 title bar
		if (stubTitleContent != null && getTitleBarViewId() != 0) {
			stubTitleContent.setLayoutResource(getTitleBarViewId());
			mTitleView = stubTitleContent.inflate();
		}

		if (stubMainBottom != null && getRootBottomViewId() != 0) {
			stubMainBottom.setLayoutResource(getRootBottomViewId());
			mRootBottomView = stubMainBottom.inflate();
		}

		if (getBackGroundBitmap() != null)
			ll_root_main.setBackgroundDrawable(new BitmapDrawable(getResources(), getBackGroundBitmap()));
		else
			ll_root_main.setBackgroundColor(getBackGroundColor());

		StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"));
	}

	/**
	 * 设置 Activity 是否为全屏
	 *
	 * @return 是否为全屏
	 */

	public boolean isFullScreen() {
		return false;
	}

	private void init() {

		TAG = this.getClass().getSimpleName();
		//初始化图片工具
		mImageLoader = new GlideUtil();
		mPermissionUtil = new PermissionUtil(this);
		mPermissionUtil.setPermissionListener(this);
		//软键盘工具
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mBasePresenter = getBasePresenter();
		//---
		if (mBasePresenter == null)
			mBasePresenter = new BasePresenter(this) {
				@Override
				public BaseModel getBaseModel() {
					return null;
				}
			};
	}

	protected abstract BasePresenter getBasePresenter();

	@Override
	public void toastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNoNetWork() {

	}

	public void go2Activity(Class<? extends Activity> activity, Bundle bundle, boolean isFinish) {

		Intent intent = new Intent(this, activity);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);
		if (isFinish)
			onBackPressed();
	}

	protected void onSoftKeyBoardVisible(boolean visible) {
		Log.e(TAG, "onSoftKeyBoardVisible::" + visible);
	}

	public void go2ActivityForResult(Class<? extends Activity> activity, int requestcode, Bundle bundle, int enterAnim, int exitAnim) {

		Intent intent = new Intent(this, activity);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivityForResult(intent, requestcode);
		overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 监听软键盘状态
	 */
	private void addOnSoftKeyBoardVisibleListener() {
		final View decorView = getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				decorView.getWindowVisibleDisplayFrame(rect);
				int displayHeight = rect.bottom - rect.top;
				int height = decorView.getHeight();
				boolean visible = (double) displayHeight / height < 0.8;
				if (visible != mLastVisiable) {
					onSoftKeyBoardVisible(visible);
				}
				mLastVisiable = visible;
			}
		});
	}

	/**
	 * 设置背景色
	 *
	 * @return 背景色
	 */

	protected int getBackGroundColor() {
		return Color.parseColor("#FFFFFFFF");
	}

	/**
	 * 设置背景图片
	 *
	 * @return 背景图片Bitmap
	 */
	protected Bitmap getBackGroundBitmap() {
		return null;
	}

	public Object getIntentData(String key) {
		if (getIntent().getExtras() != null)
			return getIntent().getExtras().get(key);
		else
			return null;
	}

	public void showProgress(String txt) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
			mLoadingDialog.setText(txt);
		}

		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.setText(txt);
		}
	}

	public void dismissProgress() {
		if (mLoadingDialog != null)
			mLoadingDialog.closeDialog();
	}


//	1、方法一(如果输入法在窗口上已经显示，则隐藏，反之则显示)
//[java] view plain copy print?
//	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//2、方法二（view为接受软键盘输入的视图，SHOW_FORCED表示强制显示）
//			[java] view plain copy print?
//	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//[java] view plain copy print?
//			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
//
//3、调用隐藏系统默认的输入法
//[java] view plain copy print?
//			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(WidgetSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  (WidgetSearchActivity是当前的Activity)
//
//			4、获取输入法打开的状态
//[java] view plain copy print?
//	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//	boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开


	protected void toggleSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected abstract int getRootBottomViewId();

	protected abstract int getTitleBarViewId();

	protected abstract int getContentViewId();

	protected abstract void initViews();

	protected abstract void initViewSetting();

	protected abstract void initData();

	protected abstract String getTitleTxt();

	//------------------- 权限相关-------------------------

	@Override
	public void onGranted(String permission) {

	}

	@Override
	public void onShouldShowRequestPermissionRationale(String permission) {
		showRationaleDialog(getResources().getString(R.string.app_name) + "需要请求相关权限" + "", permission);
	}

	@Override
	public void onDenied(String permission) {

	}

	private void showRationaleDialog(String message, final String permission) {
		new AlertDialog.Builder(this)
				.setPositiveButton("请求", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(@NonNull DialogInterface dialog, int which) {
						//
						mPermissionUtil.requestPermissions(permission);
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(@NonNull DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setCancelable(false)
				.setMessage(message)
				.show();
	}

	private void initBar() {
		if (isImmersionBarEnabled())
			initImmersionBar();
		if (OSUtils.isEMUI3_1()) {
			//第一种
			getContentResolver().registerContentObserver(Settings.System.getUriFor
					(NAVIGATIONBAR_IS_MIN), true, mNavigationStatusObserver);
			//第二种,禁止对导航栏的设置
			//mImmersionBar.navigationBarEnable(false).init();
		}
	}


	public ImmersionBar mImmersionBar;

	private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";

	/**
	 * 是否可以使用沉浸式
	 * Is immersion bar enabled boolean.
	 *
	 * @return the boolean
	 */
	protected boolean isImmersionBarEnabled() {
		return true;
	}

	protected void initImmersionBar() {
		//在BaseActivity里初始化
		mImmersionBar = ImmersionBar.with(this);
		mImmersionBar.statusBarView(mViewBar);
		mImmersionBar.init();
	}

	private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			int navigationBarIsMin = Settings.System.getInt(getContentResolver(),
					NAVIGATIONBAR_IS_MIN, 0);
			if (navigationBarIsMin == 1) {
				//导航键隐藏了
				mImmersionBar.transparentNavigationBar().init();
			} else {
				//导航键显示了
				mImmersionBar.navigationBarColor(android.R.color.black) //隐藏前导航栏的颜色
						.fullScreen(false)
						.init();
			}
		}
	};

}
