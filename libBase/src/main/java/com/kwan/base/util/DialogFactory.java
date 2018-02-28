package com.kwan.base.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kwan.base.BaseApplication;
import com.kwan.base.R;


public class DialogFactory {
	
	/**
	 * 底部弹出菜单选择对话框(用于如底部弹出)
	 * 
	 * @param act
	 * @param view
	 * @return
	 */
	public static Dialog showMenuDialog(final Activity act, View view) {
		Dialog dialog;
		
		dialog = new Dialog(act, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = BaseApplication.SCREEN_WIDTH;

		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
		dialog.show();
		return dialog;
	}

    public static Dialog showCustomDialog(final Activity act, View view) {

		Dialog dialog;
		dialog = new Dialog(act, R.style.MenuDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (ViewUtil.getScreenWidth(act) * 9 / 10.0);
		lp.height =  (int) (ViewUtil.getScreenHeight(act) * 2 / 3.0);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

	public static Dialog showCustomDialog2(final Activity act, View view) {

		Dialog dialog;
		dialog = new Dialog(act, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
//
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = (int) (ViewUtil.getScreenWidth(act) * 8 / 10.0);
		lp.height =  (int) (ViewUtil.getScreenHeight(act) * 2 / 3.0);
		window.setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		return dialog;
	}

	public static Dialog showCustomDialog3(final Activity act, View view) {

		Dialog dialog;
		dialog = new Dialog(act, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		return dialog;
	}

	public static Dialog showCustomDialog3(final Activity act, View view, int w, int h) {

		Dialog dialog;
		dialog = new Dialog(act, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = w;
		lp.height =  h;
		window.setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		return dialog;
	}

	/**
	 * 隐藏
	 * 
	 * @param mDialog
	 */
	public static void dismissDialog(Dialog mDialog) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.cancel();
		}
	}

	/**
	 * 彻底销毁，包括实例置空
	 * 
	 * @param mDialog
	 */
	public static void destoryDialog(Dialog mDialog) {
		dismissDialog(mDialog);
	}
	
	/**
	 * 销毁一切已出现的对话框
	 */
	//public static void destoryAllDialog(){
		//destoryDialog(dialog);
	//}




}
