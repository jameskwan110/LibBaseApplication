package com.kwan.base.common.widget;

/*
 *LoadingDialog.java
 *@author Kwan
 * 自定义滚动提示控件
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwan.base.R;
import com.kwan.base.util.ViewUtil;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingDialog extends Dialog implements OnDismissListener {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;

    private TextView msg_txt;
    private ProgressWheel mProBar;

   // private int width;
    private String message = null;
    private LinearLayout.LayoutParams params;
    private OnLoadingDialogResultListener listener;
    private int post_time = 0;
    private int tag = 0;
    private int state;

    public interface OnLoadingDialogResultListener {
        void dialogResult(int tag, int state);
    }

    public LoadingDialog(Activity context) {
        super(context, R.style.LoadDialog);
        message = "正在加载...";
       // params = new LinearLayout.LayoutParams(BaseApplication.SCREEN_WIDTH / 3, BaseApplication.SCREEN_WIDTH / 3);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.common_loading_layout, null);
        msg_txt = (TextView) view.findViewById(R.id.common_loading_msg);
		mProBar = (ProgressWheel) view.findViewById(R.id.common_loading_probar);
        msg_txt.setText(this.message);
        ViewUtil.measureView(msg_txt);
        this.setContentView(view, params);
        this.setOnDismissListener(this);
    }

    public void setText(String message) {
        this.message = message;
        msg_txt.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }



    public void setOnLoadingDialogResultListener(OnLoadingDialogResultListener listener) {
        this.listener = listener;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public void show() {
        super.show();
		mProBar.setVisibility(View.VISIBLE);
        msg_txt.setText(message);
    }

    public void setMsg(String msg, int time) {

        post_time = time;
        state = SUCCESS;
		mProBar.setVisibility(View.GONE);
        msg_txt.setText(msg);
        closeDialog();
    }

    public void setFinishSuccess(String msg, int time) {

        post_time = time;
        state = SUCCESS;
		mProBar.setVisibility(View.GONE);
        msg_txt.setText(msg);
        closeDialog();
    }

    public void setFinishSuccess(String msg) {

        post_time = 2000;
        state = SUCCESS;
		mProBar.setVisibility(View.GONE);
        msg_txt.setText(msg);
        closeDialog();
    }

    public void setFinishFailure(String msg) {

        post_time = 2000;
        state = FAILURE;
		mProBar.setVisibility(View.GONE);
        msg_txt.setText(msg);
        closeDialog();
    }

    public void setFinishFailure(String msg, int time) {

        post_time = time;
        state = FAILURE;
		mProBar.setVisibility(View.GONE);
        msg_txt.setText(msg);
        closeDialog();
    }

    public void closeDialog() {

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                LoadingDialog.this.dismiss();
                t.cancel();
            }
        }, post_time);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (listener != null)
            listener.dialogResult(tag, state);
        state = 0;
    }

    public void setState(int state) {
        this.state = state;
    }
}
