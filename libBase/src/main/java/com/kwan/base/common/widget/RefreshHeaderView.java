package com.kwan.base.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.kwan.base.R;
import com.zhy.autolayout.AutoLinearLayout;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import swipetoloadlayout.SwipeRefreshTrigger;
import swipetoloadlayout.SwipeTrigger;

/**
 * 刷新头部
 * Created by Mr.Kwan on 2016-7-11.
 */
public class RefreshHeaderView extends AutoLinearLayout implements SwipeTrigger, SwipeRefreshTrigger {

	private GifImageView ivRefresh;
	//  private ImageUtil imageUtil = App.getInstance().getImageUtil();
	private GifDrawable mGifDrawable;

	public RefreshHeaderView(Context context) {
		super(context);
	}

	public RefreshHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);


	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		ivRefresh = (GifImageView) findViewById(R.id.ivRefresh);
		if (ivRefresh != null)
			mGifDrawable = (GifDrawable) ivRefresh.getDrawable();
		mGifDrawable.stop();

	}

	@Override
	public void onRefresh() {

		if (mGifDrawable == null)
			mGifDrawable = (GifDrawable) ivRefresh.getDrawable();
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
			mGifDrawable = (GifDrawable) ivRefresh.getDrawable();
		else if (!mGifDrawable.isRunning()) {
			mGifDrawable.start();
		}

	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onReset() {

		if (mGifDrawable != null) {
//            mGifDrawable.reset();
			mGifDrawable.stop();
			mGifDrawable.seekTo(0);
//            Glide.clear(target);
//            mGifDrawable = (GifDrawable) mGifDrawable.getConstantState().newDrawable();
//            ivRefresh.setImageDrawable(mGifDrawable);
		}
//        } else if (mContext != null) {
//            ivRefresh.setImageBitmap(imageUtil.compressBitmapFromResPathBySize(mContext.getResources(), R.drawable.load, ivRefresh.getWidth(), ivRefresh.getHeight()));
//        }
	}
}
