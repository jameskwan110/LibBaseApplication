package com.example.kwan.libbaseapplication;

import android.os.Environment;
import android.util.Log;

import com.kwan.base.api.BaseAPIUtil;
import com.kwan.base.api.download.FileCallBack;
import com.kwan.base.mvp.model.BaseModel;
import com.kwan.base.mvp.presenter.BasePresenter;
import com.kwan.base.mvp.view.activity.BaseActivity;

import okhttp3.ResponseBody;

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

		Log.e("path","path---"+ Environment.getExternalStorageDirectory().getPath());


		presenter.download("0", url, Environment.getExternalStorageDirectory().getPath(),
				new FileCallBack<ResponseBody>(Environment.getExternalStorageDirectory().getPath(),"mttest.jpg") {
					@Override
					public void onSuccess(ResponseBody responseBody) {

					}

					@Override
					public void onProgress(long progress, long total, boolean done) {

						Log.e("onProgress","progress:"+progress+" total:"+total+" done:"+done);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onCompleted() {
						Log.e(" onCompleted()","main");
					}

					@Override
					public void onError(Throwable e) {

					}
				});


//		presenter.download(url,
//				new FileCallBack<ResponseBody>(Environment.getExternalStorageDirectory().getPath(),"mttest.jpg") {
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
