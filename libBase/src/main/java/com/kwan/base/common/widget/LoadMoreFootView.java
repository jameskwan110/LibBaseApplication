package com.kwan.base.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kwan.base.R;
import com.zhy.autolayout.AutoLinearLayout;

import pl.droidsonroids.gif.GifDrawable;
import swipetoloadlayout.SwipeLoadMoreTrigger;
import swipetoloadlayout.SwipeTrigger;

/**
 *
 * Created by Mr.Kwan on 2016-7-22.
 */
public class LoadMoreFootView extends AutoLinearLayout implements SwipeLoadMoreTrigger, SwipeTrigger {

    private ImageView ivLoadMore;
    private GifDrawable mGifDrawable;

    public LoadMoreFootView(Context context) {
        this(context, null);
    }

    public LoadMoreFootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivLoadMore = (ImageView) findViewById(R.id.ivLoadMore);
        mGifDrawable = (GifDrawable) ivLoadMore.getDrawable();
        mGifDrawable.stop();
    }

    @Override
    public void onLoadMore() {


        if (mGifDrawable == null)
            mGifDrawable = (GifDrawable) ivLoadMore.getDrawable();
        else if (!mGifDrawable.isRunning()) {
            mGifDrawable.start();
        }
    }

    @Override
    public void onPrepare() {
        mGifDrawable.stop();
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

    }



    @Override
    public void onRelease() {

        if (mGifDrawable == null)
            mGifDrawable = (GifDrawable) ivLoadMore.getDrawable();
        else if (!mGifDrawable.isRunning()) {
            mGifDrawable.start();
        }

    }

    @Override
    public void onComplete() {

    }



    @Override
    public void onReset() {
        if(mGifDrawable!=null) {
            mGifDrawable.stop();
            mGifDrawable.seekTo(0);
        }
    }
}
