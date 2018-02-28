package com.kwan.base.mvp.view.activity;

import android.graphics.Bitmap;
import android.util.Log;

import com.kwan.base.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import static com.kwan.base.R.id.webView;

/**
 * 腾讯tbs webview框架
 *
 * 引用库：
 * libs/tbs_sdk_thirdapp_v2.6.0.1045.jar
 * jniLibs/liblbs.so
 *
 * Appliction 中需要进行相关初始化
 *
 * Created by Mr.Kwan on 2017-1-4.
 */

public abstract class BaseWebActivity extends BaseCommonActivity {

    protected WebView tbsWebView;
    public static final int MODE_WEB = 1;
    public static final int MODE_CONTENT = 2;
	String mUrl;

    @Override
    protected void beForeSetContentView() {
        super.beForeSetContentView();
		mUrl = getUrl();
    }

	/**
	 * @return 可以是地址 也可以是 html 内容
	 */

	protected abstract String getUrl();

	@Override
    protected Bitmap getBackGroundBitmap() {
        return null;
    }

	@Override
	protected int getMainViewId() {
		 return R.layout.activity_base_web;
	}

	@Override
    protected void initViews() {
        super.initViews();
        tbsWebView = (WebView) findViewById(webView);
    }



    @Override
    protected void initData() {

        if (getMode() == MODE_WEB)
            loadWeb(mUrl);
        else
            loadContent(mUrl);
    }

	protected abstract int getMode();

	protected void loadWeb(final String url) {

        //final String url = "http://m.zufang.com";

        tbsWebView = (WebView) findViewById(webView);
        tbsWebView.loadUrl(url);

        WebSettings settings = tbsWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        // 设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);


        //settings.setUserAgent( );

        tbsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebView ", "shouldOverrideUrlLoading " + url);
                view.loadUrl(url);
                return true;
            }

        });

        tbsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

        });

    }

    private void loadContent(final String content) {

        WebSettings mSetting = tbsWebView.getSettings();
        mSetting.setJavaScriptEnabled(true);
        mSetting.setDefaultTextEncodingName("UTF-8");
        mSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

//        webview.addJavascriptInterface(new ProductsInfoActivityJSService(
//                this),"Javahelper");   // 这在前

        tbsWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                if (mUrl.startsWith("http:") || mUrl.startsWith("https:")) {
                    webView.loadUrl(mUrl);
                    return false;
                } else {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        tbsWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tbsWebView != null)
                    tbsWebView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
            }
        }, 1000);

    }
}
