package com.kwan.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.kwan.base.util.SysUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


//import com.tencent.smtt.sdk.QbSdk;
//import com.tencent.smtt.sdk.TbsListener;


/**
 * Application 基类
 * Created by Mr.Kwan on 2016-3-30.
 */
public abstract class BaseApplication extends MultiDexApplication {

	/**
	 * APP实例调用
	 **/
	private static BaseApplication mBaseApplication;
	//private static BaseConfiguration mConfiguration;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int STATUS_BAR_HEIGHT;
	public static int TITLE_BAR_HEIGHT;
	//private ImageUtil mImageUtil;
	private Map<String, Integer> mFaceMap = new LinkedHashMap<>();
	private ArrayList<String> mFaceString = new ArrayList<>();

	public static String uniqueId;

	@Override
	public void onCreate() {
		super.onCreate();
		mBaseApplication = this;
		initApp();
		//initCrashHandler();
		//初始化日志框架
		initLogger("kwan");
		initTsweb();
		initFont();
		initFaceMap();
		initFaceString();
	}

	private void initTsweb() {
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		//TbsDownloader.needDownload(getApplicationContext(), false);
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				Log.e("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub

			}
		};
		QbSdk.setTbsListener(new TbsListener() {
			@Override
			public void onDownloadFinish(int i) {
				Log.d("app", "onDownloadFinish");
			}

			@Override
			public void onInstallFinish(int i) {
				Log.d("app", "onInstallFinish");
			}

			@Override
			public void onDownloadProgress(int i) {
				Log.d("app", "onDownloadProgress:" + i);
			}
		});
		QbSdk.initX5Environment(getApplicationContext(), cb);

	}

	private void initApp() {

		// mImageUtil = new ImageUtil();
		//  mConfiguration = new BaseConfiguration();

		uniqueId = SysUtil.getUniqueId(this);
		STATUS_BAR_HEIGHT = getStatusBarHeight();

		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);

		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		TITLE_BAR_HEIGHT = (int) getResources().getDimension(R.dimen.title_bar_height);


	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 为自定义APP实例赋予Crash记录功能
	 */
	private void initCrashHandler() {

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
	}

	/**
	 * 初始化日志框架
	 *
	 * @param logTag 日志 tag
	 */
	private void initLogger(String logTag) {

		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
				.methodCount(2)         // (Optional) How many method line to show. Default 2
				.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
				//.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
				.tag(logTag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

	}

	/**
	 * 初始化字体
	 */
	private void initFont() {
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/xhjt.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}


	/**
	 * 提供APP实例
	 *
	 * @return
	 */
	public static BaseApplication getInstance() {
		return mBaseApplication;
	}

	//public ImageUtil getImageUtil() {
//		return mImageUtil;
//	}

	//public BaseConfiguration getConfiguration() {
//		return mConfiguration;
//	}

	public Map<String, Integer> getFaceMap() {
		return mFaceMap;
	}

	public ArrayList<String> getFaceString() {
		return mFaceString;
	}

	private void initFaceMap() {

//		mFaceMap.put("[呲牙]", R.drawable.f_static_000);
//		mFaceMap.put("[调皮]", R.drawable.f_static_001);
//		mFaceMap.put("[流汗]", R.drawable.f_static_002);
//		mFaceMap.put("[偷笑]", R.drawable.f_static_003);
//		mFaceMap.put("[再见]", R.drawable.f_static_004);
//		mFaceMap.put("[敲打]", R.drawable.f_static_005);
//		mFaceMap.put("[擦汗]", R.drawable.f_static_006);
//		mFaceMap.put("[猪头]", R.drawable.f_static_007);
//		mFaceMap.put("[玫瑰]", R.drawable.f_static_008);
//		mFaceMap.put("[流泪]", R.drawable.f_static_009);
//		mFaceMap.put("[大哭]", R.drawable.f_static_010);
//		mFaceMap.put("[嘘]", R.drawable.f_static_011);
//		mFaceMap.put("[酷]", R.drawable.f_static_012);
//		mFaceMap.put("[抓狂]", R.drawable.f_static_013);
//		mFaceMap.put("[委屈]", R.drawable.f_static_014);
//		mFaceMap.put("[便便]", R.drawable.f_static_015);
//		mFaceMap.put("[炸弹]", R.drawable.f_static_016);
//		mFaceMap.put("[菜刀]", R.drawable.f_static_017);
//		mFaceMap.put("[可爱]", R.drawable.f_static_018);
//		mFaceMap.put("[色]", R.drawable.f_static_019);
//
//		mFaceMap.put("[害羞]", R.drawable.f_static_020);
//		mFaceMap.put("[得意]", R.drawable.f_static_021);
//		mFaceMap.put("[吐]", R.drawable.f_static_022);
//		mFaceMap.put("[微笑]", R.drawable.f_static_023);
//		mFaceMap.put("[发怒]", R.drawable.f_static_024);
//		mFaceMap.put("[尴尬]", R.drawable.f_static_025);
//		mFaceMap.put("[惊恐]", R.drawable.f_static_026);
//		mFaceMap.put("[冷汗]", R.drawable.f_static_027);
//		mFaceMap.put("[爱心]", R.drawable.f_static_028);
//		mFaceMap.put("[示爱]", R.drawable.f_static_029);
//		mFaceMap.put("[白眼]", R.drawable.f_static_030);
//		mFaceMap.put("[傲慢]", R.drawable.f_static_031);
//		mFaceMap.put("[难过]", R.drawable.f_static_032);
//		mFaceMap.put("[惊讶]", R.drawable.f_static_033);
//		mFaceMap.put("[疑问]", R.drawable.f_static_034);
//		mFaceMap.put("[睡]", R.drawable.f_static_035);
//		mFaceMap.put("[亲亲]", R.drawable.f_static_036);
//		mFaceMap.put("[憨笑]", R.drawable.f_static_037);
//		mFaceMap.put("[爱情]", R.drawable.f_static_038);
//		mFaceMap.put("[衰]", R.drawable.f_static_039);
//
//		mFaceMap.put("[撇嘴]", R.drawable.f_static_040);
//		mFaceMap.put("[阴险]", R.drawable.f_static_041);
//		mFaceMap.put("[奋斗]", R.drawable.f_static_042);
//		mFaceMap.put("[发呆]", R.drawable.f_static_043);
//		mFaceMap.put("[右哼哼]", R.drawable.f_static_044);
//		mFaceMap.put("[拥抱]", R.drawable.f_static_045);
//		mFaceMap.put("[坏笑]", R.drawable.f_static_046);
//		mFaceMap.put("[飞吻]", R.drawable.f_static_047);
//		mFaceMap.put("[鄙视]", R.drawable.f_static_048);
//		mFaceMap.put("[晕]", R.drawable.f_static_049);
//		mFaceMap.put("[大兵]", R.drawable.f_static_050);
//		mFaceMap.put("[可怜]", R.drawable.f_static_051);
//		mFaceMap.put("[强]", R.drawable.f_static_052);
//		mFaceMap.put("[弱]", R.drawable.f_static_053);
//		mFaceMap.put("[握手]", R.drawable.f_static_054);
//		mFaceMap.put("[胜利]", R.drawable.f_static_055);
//		mFaceMap.put("[抱拳]", R.drawable.f_static_056);
//		mFaceMap.put("[凋谢]", R.drawable.f_static_057);
//		mFaceMap.put("[饭]", R.drawable.f_static_058);
//		mFaceMap.put("[蛋糕]", R.drawable.f_static_059);
//
//		mFaceMap.put("[西瓜]", R.drawable.f_static_060);
//		mFaceMap.put("[啤酒]", R.drawable.f_static_061);
//		mFaceMap.put("[飘虫]", R.drawable.f_static_062);
//		mFaceMap.put("[勾引]", R.drawable.f_static_063);
//		mFaceMap.put("[OK]", R.drawable.f_static_064);
//		mFaceMap.put("[爱你]", R.drawable.f_static_065);
//		mFaceMap.put("[咖啡]", R.drawable.f_static_066);
//		mFaceMap.put("[钱]", R.drawable.f_static_067);
//		mFaceMap.put("[月亮]", R.drawable.f_static_068);
//		mFaceMap.put("[美女]", R.drawable.f_static_069);
//		mFaceMap.put("[刀]", R.drawable.f_static_070);
//		mFaceMap.put("[发抖]", R.drawable.f_static_071);
//		mFaceMap.put("[差劲]", R.drawable.f_static_072);
//		mFaceMap.put("[拳头]", R.drawable.f_static_073);
//		mFaceMap.put("[心碎]", R.drawable.f_static_074);
//		mFaceMap.put("[太阳]", R.drawable.f_static_075);
//		mFaceMap.put("[礼物]", R.drawable.f_static_076);
//		mFaceMap.put("[足球]", R.drawable.f_static_077);
//		mFaceMap.put("[骷髅]", R.drawable.f_static_078);
//		mFaceMap.put("[挥手]", R.drawable.f_static_079);
//
//		mFaceMap.put("[闪电]", R.drawable.f_static_080);
//		mFaceMap.put("[饥饿]", R.drawable.f_static_081);
//		mFaceMap.put("[困]", R.drawable.f_static_082);
//		mFaceMap.put("[咒骂]", R.drawable.f_static_083);
//		mFaceMap.put("[折磨]", R.drawable.f_static_084);
//		mFaceMap.put("[抠鼻]", R.drawable.f_static_085);
//		mFaceMap.put("[鼓掌]", R.drawable.f_static_086);
//		mFaceMap.put("[糗大了]", R.drawable.f_static_087);
//		mFaceMap.put("[左哼哼]", R.drawable.f_static_088);
//		mFaceMap.put("[哈欠]", R.drawable.f_static_089);
//		mFaceMap.put("[快哭了]", R.drawable.f_static_090);
//		mFaceMap.put("[吓]", R.drawable.f_static_091);
//		mFaceMap.put("[篮球]", R.drawable.f_static_092);
//		mFaceMap.put("[乒乓球]", R.drawable.f_static_093);
//		mFaceMap.put("[NO]", R.drawable.f_static_094);
//		mFaceMap.put("[跳跳]", R.drawable.f_static_095);
//		mFaceMap.put("[怄火]", R.drawable.f_static_096);
//		mFaceMap.put("[转圈]", R.drawable.f_static_097);
//		mFaceMap.put("[磕头]", R.drawable.f_static_098);
//		mFaceMap.put("[回头]", R.drawable.f_static_099);
//
//		mFaceMap.put("[跳绳]", R.drawable.f_static_100);
//		mFaceMap.put("[激动]", R.drawable.f_static_101);
//		mFaceMap.put("[街舞]", R.drawable.f_static_102);
//		mFaceMap.put("[献吻]", R.drawable.f_static_103);
//		mFaceMap.put("[左太极]", R.drawable.f_static_104);
//		mFaceMap.put("[右太极]", R.drawable.f_static_105);
//		mFaceMap.put("[闭嘴]", R.drawable.f_static_106);
	}

	private void initFaceString() {

		Set<String> set = mFaceMap.keySet();
		for (String key : set) {
			mFaceString.add(key);
		}
	}
}
