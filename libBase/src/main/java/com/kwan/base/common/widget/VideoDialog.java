package com.kwan.base.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kwan.base.R;

/**
 * Created by Mr.Kwan on 2017-7-31.
 */

public class VideoDialog extends Dialog {
	private  LinearLayout.LayoutParams params ;
	Context context;
	private View dialogView;

	public VideoDialog(Context context) {
		super(context);
		this.context = context;
	}

	public VideoDialog(Context context, int theme){
		super(context, theme);
		this.context = context;
		LayoutInflater inflater= LayoutInflater.from(context);
		dialogView = inflater.inflate(R.layout.layout_dialog_video, null);
		//params = new LinearLayout.LayoutParams(BaseApplication.SCREEN_WIDTH, 600);
	}

	@Override
	public void show() {
		super.show();
		/**
		 * 设置宽度全屏，要设置在show的后面
		 */
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.gravity= Gravity.CENTER;
		layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;

		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(layoutParams);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(dialogView);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public View findViewById(int id) {
		//重写findViewById方法获取对话框中控件
		return super.findViewById(id);
	}

	public View getDialogView() {
		//获得对话框view
		return dialogView;
	}
}
