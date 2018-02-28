package com.kwan.base.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.kwan.base.R;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 *  Created by Administrator on 2017/7/17.
 */

public class RoundnessProgressDialog extends Dialog{

    private Context mContext;

    private RingProgressBar mProgressBar;
    //private TextView mTvDescribe;


    public RoundnessProgressDialog(Context context) {
        super(context, R.style.upload_dialog_style);
        this.mContext = context;
    }

    public void setProgress(int progress){
        mProgressBar.setProgress(progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_upload_video_progress);
        mProgressBar = (RingProgressBar) findViewById(R.id.view_upload_video_progress);
        //点击外部不隐藏Dialog
        setCanceledOnTouchOutside(false);
        //mTvDescribe = (TextView) findViewById(R.id.tv_upload_video_describe);
    }

    public void oKeyDown() {
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(isShowing()){
                    return true;
                }else{
                    return false;
                }
            }
        });
    }
}

