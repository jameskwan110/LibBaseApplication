package com.kwan.base.common.image.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kwan.base.BaseApplication;
import com.kwan.base.R;
import com.kwan.base.common.image.ImageLoader;
import com.zhy.autolayout.AutoFrameLayout;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Glide 图片加载工具类
 * Created by Mr.Kwan on 2017-7-29.
 */

public class GlideUtil implements ImageLoader {

	private Context mContext;

	private RequestListener<Drawable> mRequestListener = new RequestListener<Drawable>() {
		@Override
		public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

			if (e != null) {
				e.printStackTrace();
				Log.d("ImageUtil onException", "Exception: " + e.getMessage() + " model: " + model);
			}
			return false;
		}

		@Override
		public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
			return false;
		}
	};

	private RequestOptions mFitCenterOptions = new RequestOptions()
			//.centerCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.placeholder(R.mipmap.item_default)
			.error(R.mipmap.item_default);

	public GlideUtil() {
		mContext = BaseApplication.getInstance();
	}

	@Override
	public void loadLocalResource(int resourceId, final ImageView imageView) {
		Glide.with(mContext)
				.load(resourceId)
				.listener(mRequestListener)
				.apply(mFitCenterOptions)
				.into(new SimpleTarget<Drawable>() {
					@Override
					public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
						imageView.setBackgroundDrawable(resource);
					}
				});
	}

	@Override
	public void loadGif(Object url, final ImageView imageView, final int width) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if (mContext instanceof Activity && ((Activity) mContext).isDestroyed())
				return;
		}

		Glide.with(mContext)
				.as(byte[].class)
				.load(url)
				.apply(mFitCenterOptions)
				.into(new SimpleTarget<byte[]>() {

					@Override
					public void onLoadStarted(Drawable placeholder) {
						super.onLoadStarted(placeholder);
						imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
						imageView.setImageDrawable(placeholder);
					}

					@Override
					public void onResourceReady(byte[] resource, Transition<? super byte[]> transition) {
						try {

							pl.droidsonroids.gif.GifDrawable gifFromBytes = new GifDrawable(resource);
							float scale = gifFromBytes.getMinimumHeight() / (gifFromBytes.getMinimumWidth() * 1.0f);
							int viewHeight = (int) (width * scale);
							imageView.setLayoutParams(new AutoFrameLayout.LayoutParams(width, viewHeight));
							imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
							imageView.setImageDrawable(gifFromBytes);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void loadUrl(String url, final ImageView imageView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if (mContext instanceof Activity && ((Activity) mContext).isDestroyed())
				return;
		}
		Glide.with(mContext)
				.asBitmap()
				.load(url)
				.apply(mFitCenterOptions)
				.into(imageView);

		//new ViewTarget<ImageView, Bitmap>(imageView) {
//		@Override
//		public void onLoadStarted(Drawable placeholder) {
//			//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//			imageView.setImageDrawable(placeholder);
//		}

		//@Override
		//public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

//						ViewUtil.measureView(imageView);
//						int width = imageView.getMeasuredWidth();
//
//						Log.e("imageView","imageView::"+imageView.getMeasuredWidth());
//
//						float scale = resource.getMinimumHeight() / (resource.getMinimumWidth() * 1.0f);
//						int viewHeight = (int) (width * scale);
//
//						ViewGroup.LayoutParams layoutParam = imageView.getLayoutParams();
//						layoutParam.width = width;
//						layoutParam.height = viewHeight;
//						imageView.setLayoutParams(layoutParam);

//			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//			imageView.setImageBitmap(resource);
//		}
//	}
	}

	public void loadFile(String path, ImageView imageView) {
		loadFile(new File(path), imageView);
	}

	public void loadFile(File file, ImageView imageView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if (mContext instanceof Activity && ((Activity) mContext).isDestroyed())
				return;
		}
		Glide.with(mContext)
				.load(file)
				.apply(mFitCenterOptions)
				.into(imageView);
	}

	public void downLoadImage(String url, RequestListener<File> listener) {
		Glide.with(mContext).download(url).listener(listener);
	}

}
