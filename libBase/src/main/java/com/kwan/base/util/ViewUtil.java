package com.kwan.base.util;

/*
 *ViewUtil.java
 *@author 谢明峰
 * 控件工具类
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;

import java.lang.reflect.Field;

public class ViewUtil {

	private  int barHeight = -1;

	//初始化工具箱
	private static class SingletonViewUtil{
		private static final ViewUtil viewUtil = new ViewUtil();
	}

	//获取工具箱单例
	public static ViewUtil getViewUtil(){
		return SingletonViewUtil.viewUtil;
	}
	

	public static int getScreenWidth(Activity activity){

        DisplayMetrics dm = new DisplayMetrics();
        activity. getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity. getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * 状态条的像素值
	 * @return > 0 success; <= 0 fail
	 */
	public static int getStatusHeight(Activity activity) {

        int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	public static void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
		}
		view.measure(childWidthSpec, childHeightSpec);
	}
	
	/**
	 *
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	/**
	 *
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	 /** 
     * 将px值转换为sp值，保证文字大小不变 
     * @param pxValue 标准像素值
     * @return 
     */  
    public static int pxToSp(Context context , float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        int fontSp = (int) (pxValue / fontScale + 0.5f);
        return fontSp;
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     * @param spValue 比例像素值
     * @return 
     */  
    public static int spToPx(Context context , float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        int fontPx = (int) (spValue * fontScale + 0.5f);
        return fontPx;
    }

	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return 状态栏高度
	 */
	public  int getBarHeight(Context context) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			barHeight = 0;
		}

		if (barHeight == -1) {
			Class<?> c = null;
			Object obj = null;
			Field field = null;
			int x = 0;

			try {
				c = Class.forName("com.android.internal.R$dimen");
				obj = c.newInstance();
				field = c.getField("status_bar_height");
				x = Integer.parseInt(field.get(obj).toString());
				barHeight = context.getResources().getDimensionPixelSize(x);

			} catch (Exception e1) {
				e1.printStackTrace();
				return 0;
			}
		}

		return barHeight;
	}

	/**
	 * 获取屏幕截屏 【不包含状态栏】
	 *
	 * @param activity
	 * @param containTopBar 是否包含状态栏
	 * @return
	 */
	public  Bitmap getScreenshot(Activity activity, boolean containTopBar) {
		try {
			Window window = activity.getWindow();
			View view = window.getDecorView();
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(true);
			Bitmap bmp1 = view.getDrawingCache();
			/**
			 * 除去状态栏和标题栏
			 **/
			int contentTop = 100;
			int height = containTopBar ? 0 : getBarHeight(activity);
			height = height+contentTop;
			return Bitmap.createBitmap(bmp1, 0, height, bmp1.getWidth(), bmp1.getHeight() - height);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Activity截图
	 *
	 * @param activity
	 * @return bitmap 截图
	 */
	public  Bitmap getDrawing(Activity activity) {
		View view = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
		return getDrawing(view);
	}

	/**
	 * 获取View截图
	 *
	 * @param view
	 * @return 截图
	 */
	public  Bitmap getDrawing(View view) {
		try {
			view.setDrawingCacheEnabled(true);
			Bitmap tBitmap = view.getDrawingCache();
			// 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
			tBitmap = tBitmap.createBitmap(tBitmap);
			view.setDrawingCacheEnabled(false);
			return tBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
