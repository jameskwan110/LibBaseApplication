package com.kwan.base.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.kwan.base.R;

/**
 * Created by Administrator on 2017/3/13.
 */

public class CountDownView extends View {
	//圆轮颜色
	private int mRingColor;
	//圆轮宽度
	private float mRingWidth;
	//圆轮进度值文本大小
	private int mRingProgessTextSize;
	//宽度
	private int mWidth;
	//高度
	private int mHeight;
	private Paint mPaint;
	//圆环的矩形区域
	private RectF mRectF;
	//
	private int mProgessTextColor;
	private float mCountdownTime;
	private float mCurrentProgress;
	private OnCountDownFinishListener mListener;
	private Typeface fontFace;

	public CountDownView(Context context) {
		this(context, null);
	}

	public CountDownView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
		/*
          * 必须事先在assets底下创建一fonts文件夹 并放入要使用的字体文件(.ttf)
           * 并提供相对路径给creatFromAsset()来创建Typeface对象
       */
		fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/pocknum.ttf");
		mRingColor = a.getColor(R.styleable.CountDownView_ringColor, context.getResources().getColor(R.color.color_white));
		mRingWidth = a.getFloat(R.styleable.CountDownView_ringWidths, 5);
		mRingProgessTextSize = a.getDimensionPixelSize(R.styleable.CountDownView_progressTextSize, 80);
		//文字颜色
		mProgessTextColor = a.getColor(R.styleable.CountDownView_progressTextColor, context.getResources().getColor(R.color.color_white));
//        mCountdownTime = a.getInteger(R.styleable.CountDownView_countdownTime, 5);
		a.recycle();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);

		this.setWillNotDraw(false);
	}

	public void setCountdownTime(float mCountdownTime) {
		this.mCountdownTime = mCountdownTime;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mRectF = new RectF(0 + mRingWidth / 2, 0 + mRingWidth / 2,
				mWidth - mRingWidth / 2, mHeight - mRingWidth / 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 *圆环
		 */
		//颜色
		mPaint.setColor(mRingColor);
		//空心
		mPaint.setStyle(Paint.Style.STROKE);
		//宽度
		mPaint.setStrokeWidth(mRingWidth);
		canvas.drawArc(mRectF, -90, mCurrentProgress, false, mPaint);
		//绘制文本
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);

		//double d = 3.1415926;
		//String result = String .format("%.2f",d);
		//%.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型

		String text = String.format("%.1f", mCountdownTime - (mCurrentProgress / 360f * mCountdownTime));

		textPaint.setTextSize(mRingProgessTextSize);
		textPaint.setColor(mProgessTextColor);
		textPaint.setTypeface(fontFace);

		//文字居中显示
		Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
		int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
		canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
	}

	private ValueAnimator getValA(float countdownTime) {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
		valueAnimator.setDuration((long) countdownTime);
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.setRepeatCount(0);
		return valueAnimator;
	}

	/**
	 * 开始倒计时
	 */
	public void startCountDown() {
		setClickable(false);
		ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
				mPaint.setColor(mRingColor);
				mCurrentProgress = (int) (360 * (i / 100f));
				invalidate();
			}
		});
		valueAnimator.start();
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				//倒计时结束回调
				if (mListener != null) {
					mListener.countDownFinished();
				}
				setClickable(true);
			}

		});
	}

	public void setAddCountDownListener(OnCountDownFinishListener mListener) {
		this.mListener = mListener;
	}

	public interface OnCountDownFinishListener {
		void countDownFinished();
	}


}
