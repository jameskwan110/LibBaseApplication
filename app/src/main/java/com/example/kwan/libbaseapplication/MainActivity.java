package com.example.kwan.libbaseapplication;

import com.kwan.base.api.BaseAPIUtil;
import com.kwan.base.common.service.VersionUpdateConfig;
import com.kwan.base.mvp.model.BaseModel;
import com.kwan.base.mvp.presenter.BasePresenter;
import com.kwan.base.mvp.view.activity.BaseActivity;

public class MainActivity extends BaseActivity {

	// Used to load the 'native-lib' library on application startup.
	static {
		//System.loadLibrary("native-lib");
	}


	@Override
	protected BasePresenter getBasePresenter() {
		return null;
	}

	@Override
	protected int getRootBottomViewId() {
		return 0;
	}

	@Override
	protected int getTitleBarViewId() {
		return 0;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void initViewSetting() {

	}

	@Override
	protected void initData() {
		// Example of a call to a native method
//		TextView tv = (TextView) findViewById(R.id.sample_text);
//		tv.setText(stringFromJNI());


		BasePresenter presenter = new BasePresenter(this) {
			@Override
			public BaseModel getBaseModel() {
				return new BaseModel(this) {
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
				};
			}
		};

		String url = "http://112.29.152.47/imtt.dd.qq.com/16891/7693A213D6163F3C2B1505130BBCFB08.apk?mkey=5a0930034113e38d&f=1907&c=0&fsname=com.happyelements.AndroidAnimal.qq_1.50_50.apk&csr=1bbd&p=.apk";


//		Log.e("path","path---"+ Environment.getExternalStorageDirectory().getPath());
//
//
//		presenter.download("0", url, Environment.getExternalStorageDirectory().getPath(),
//				new DownloadFileCallBack<ResponseBody>(Environment.getExternalStorageDirectory().getPath(),"mttest.jpg") {
//					@Override
//					public void onSuccess(ResponseBody responseBody) {
//
//					}
//
//					@Override
//					public void onProgress(long progress, long total, boolean done) {
//
//						Log.e("onProgress","progress:"+progress+" total:"+total+" done:"+done);
//					}
//
//					@Override
//					public void onStart() {
//
//					}
//
//					@Override
//					public void onCompleted() {
//						Log.e(" onCompleted()","main");
//					}
//
//					@Override
//					public void onError(Throwable e) {
//
//					}
//				});


//		presenter.download(url,
//				new DownloadFileCallBack<ResponseBody>(Environment.getExternalStorageDirectory().getPath(),"mttest.jpg") {
//					@Override
//					public void onSuccess(ResponseBody responseBody) {
//
//					}
//
//					@Override
//					public void onProgress(long progress, long total, boolean done) {
//					//	Log.e("onProgress","progress:"+progress+" total:"+total+" done:"+done);
//						Log.e("onProgress","progress:"+progress+" total:"+total+" done:"+done);
//					}
//
//					@Override
//					public void onStart() {
//
//					}
//
//					@Override
//					public void onCompleted() {
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//
//					}
//				});
		passCheck();
	}


	/**
	 * 开始下载
	 */
	public void startDownLoad() {
//		if (context == null) {
//			throw new NullPointerException("context cannot be null, you must first call setContext().");
//		}
//		if (fileBean == null) {
//			throw new NullPointerException("url cannot be null, you must first call setDownLoadURL().");
//		}


			passCheck();

	}

	public void passCheck() {

		String url = "http://112.29.152.47/imtt.dd.qq.com/16891/7693A213D6163F3C2B1505130BBCFB08.apk?mkey=5a0930034113e38d&f=1907&c=0&fsname=com.happyelements.AndroidAnimal.qq_1.50_50.apk&csr=1bbd&p=.apk";

//		Intent startIntent = new Intent(this, VersionUpdateService.class);
//		startIntent.setAction(VersionUpdateService.ACTION_START);
//		DownloadFileBean downloadFileBean = new DownloadFileBean(1, "xx.apk", Environment.getExternalStorageDirectory().getPath(), url, 0, 0);
//		startIntent.putExtra("DownloadFileBean", downloadFileBean);
//		startService(startIntent);

		VersionUpdateConfig.getInstance()//获取配置实例
				.setContext(MainActivity.this)//设置上下文
				.setDownLoadURL(url)//设置文件下载链接
				.setNewVersion(1)//设置即将下载的APK的版本号
				.setNotificationIconRes(R.mipmap.ic_launcher)//设置通知大图标
				.setNotificationSmallIconRes(R.mipmap.ic_launcher)//设置通知小图标
				.setNotificationTitle("版本升级Demo")//设置通知标题
				.startDownLoad();//开始下载


	}

	@Override
	protected String getTitleTxt() {
		return null;
	}

	/**
	 * A native method that is implemented by the 'native-lib' native library,
	 * which is packaged with this application.
	 */
	public native String stringFromJNI();

	@Override
	public String getPageName() {
		return null;
	}
}
