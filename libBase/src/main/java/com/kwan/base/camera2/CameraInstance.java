package com.kwan.base.camera2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import com.kwan.base.BaseApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 相机实例
 * Created by Mr.Kwan on 2017-8-3.
 */

public class CameraInstance extends CameraDevice.StateCallback {


	/**
	 * A reference to the opened {@link android.hardware.camera2.CameraDevice}.
	 */
	public CameraDevice mCameraDevice;

	private static String TAG = "CameraInstance";

	/**
	 * A {@link Semaphore} to prevent the app from exiting before closing the camera.
	 */
	private Semaphore mCameraOpenCloseLock = new Semaphore(1);
	/**
	 * An additional thread for running tasks that shouldn't block the UI.
	 */
	private HandlerThread mBackgroundThread;

	/**
	 * A {@link Handler} for running tasks in the background.
	 */
	public Handler mBackgroundHandler;
	public Integer mSensorOrientation;

	/**
	 * The {@link android.util.Size} of camera preview.
	 */
	public Size mPreviewSize;

	/**
	 * The {@link android.util.Size} of video recording.
	 */
	public Size mVideoSize;
	private AutoFitTextureView mTextureView;

	public CameraCaptureSession mPreviewSession;
	public CaptureRequest.Builder mPreviewBuilder;

	private Activity mActivity;

	public CameraInstance(Activity activity, AutoFitTextureView textureView) {
		mActivity = activity;
		this.mTextureView = textureView;
	}


	/**
	 * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
	 */
	@SuppressWarnings("MissingPermission")
	public void openCamera(int width, int height) {


		if (null == mActivity || mActivity.isFinishing()) {
			return;
		}
		CameraManager manager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
		try {
			Log.e(TAG, "tryAcquire");
			if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
				throw new RuntimeException("Time out waiting to lock camera opening.");
			}
			String cameraId = manager.getCameraIdList()[0];

			// Choose the sizes for camera preview and video recording
			CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
			StreamConfigurationMap map = characteristics
					.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
			mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
			if (map == null) {
				throw new RuntimeException("Cannot get available preview/video sizes");
			}
			mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class), width, height);

			mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
					width, height, mVideoSize);

			int orientation = mActivity.getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
			} else {
				mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
			}
			configureTransform(width, height);
			manager.openCamera(cameraId, this, null);
		} catch (CameraAccessException e) {
			Toast.makeText(mActivity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
			mActivity.finish();
		} catch (NullPointerException e) {
			// Currently an NPE is thrown when the Camera2API is used but not supported on the
			// device this code runs.
//			ErrorDialog.newInstance(getString(R.string.camera_error))
//					.show(getChildFragmentManager(), FRAGMENT_DIALOG);
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted while trying to lock camera opening.");
		}
	}


	/**
	 * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
	 * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
	 * <p>
	 * 这里 暂时 写死 16:9 小于 1080 宽
	 * <p>
	 * 这里根据 textureView 的尺寸的 去找 合适的 录像的尺寸 按比例
	 *
	 * @param choices The list of available sizes
	 * @return The video size
	 */
	private static Size chooseVideoSize(Size[] choices, int width, int height) {

		for (Size size : choices) {
			if (size.getWidth() == size.getHeight() * width / height && size.getWidth() <= 1080) {
				return size;
			}
		}
		Log.e(TAG, "Couldn't find any suitable video size");
		return choices[choices.length - 1];
	}

	/**
	 * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
	 * width and height are at least as large as the respective requested values, and whose aspect
	 * ratio matches with the specified value.
	 *
	 * @param choices     The list of sizes that the camera supports for the intended output class
	 * @param width       The minimum desired width
	 * @param height      The minimum desired height
	 * @param aspectRatio The aspect ratio
	 * @return The optimal {@code Size}, or an arbitrary one if none were big enough
	 */
	private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
		// Collect the supported resolutions that are at least as big as the preview Surface
		List<Size> bigEnough = new ArrayList<>();
		int w = aspectRatio.getWidth();
		int h = aspectRatio.getHeight();
		for (Size option : choices) {
			if (option.getHeight() == option.getWidth() * h / w &&
					option.getWidth() >= width && option.getHeight() >= height) {
				bigEnough.add(option);
			}
		}

		// Pick the smallest of those, assuming we found any
		if (bigEnough.size() > 0) {
			return Collections.min(bigEnough, new CompareSizesByArea());
		} else {
			Log.e("kwan", "Couldn't find any suitable preview size");
			return choices[0];
		}
	}

	/**
	 * Compares two {@code Size}s based on their areas.
	 */
	private static class CompareSizesByArea implements Comparator<Size> {

		@Override
		public int compare(Size lhs, Size rhs) {
			// We cast here to ensure the multiplications won't overflow
			return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
					(long) rhs.getWidth() * rhs.getHeight());
		}
	}

	public void closeCamera() {
		try {
			mCameraOpenCloseLock.acquire();
			closePreviewSession();
			if (null != mCameraDevice) {
				mCameraDevice.close();
				mCameraDevice = null;
			}

		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted while trying to lock camera closing.");
		} finally {
			mCameraOpenCloseLock.release();
		}
	}

	/**
	 * Start the camera preview.
	 */
	public void startPreview() {
		if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
			return;
		}
		try {
			closePreviewSession();
			SurfaceTexture texture = mTextureView.getSurfaceTexture();
			assert texture != null;
			texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
			mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

			Surface previewSurface = new Surface(texture);
			mPreviewBuilder.addTarget(previewSurface);
			mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
					new CameraCaptureSession.StateCallback() {
						@Override
						public void onConfigured(@NonNull CameraCaptureSession session) {
							mPreviewSession = session;
							updatePreview();
						}

						@Override
						public void onConfigureFailed(@NonNull CameraCaptureSession session) {
							Toast.makeText(BaseApplication.getInstance(), "Failed", Toast.LENGTH_SHORT).show();
						}
					}, mBackgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the camera preview. {@link #startPreview()} needs to be called in advance.
	 */
	public void updatePreview() {
		if (null == mCameraDevice) {
			return;
		}
		try {
			setUpCaptureRequestBuilder(mPreviewBuilder);
			HandlerThread thread = new HandlerThread("CameraPreview");
			thread.start();
			mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
		builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
	}

	@Override
	public void onOpened(@NonNull CameraDevice cameraDevice) {
		mCameraDevice = cameraDevice;
		startPreview();
		mCameraOpenCloseLock.release();
		if (null != mTextureView) {
			configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
		}
	}

	@Override
	public void onDisconnected(@NonNull CameraDevice cameraDevice) {
		mCameraOpenCloseLock.release();
		cameraDevice.close();
		mCameraDevice = null;
	}


	@Override
	public void onError(@NonNull CameraDevice cameraDevice, int error) {
		mCameraOpenCloseLock.release();
		cameraDevice.close();
		mCameraDevice = null;
		if (null != mActivity) {
			mActivity.finish();
		}
	}

	public void closePreviewSession() {
		if (mPreviewSession != null) {
			mPreviewSession.close();
			mPreviewSession = null;
		}
	}

	/**
	 * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
	 * This method should not to be called until the camera preview size is determined in
	 * openCamera, or until the size of `mTextureView` is fixed.
	 *
	 * @param viewWidth  The width of `mTextureView`
	 * @param viewHeight The height of `mTextureView`
	 */
	public void configureTransform(int viewWidth, int viewHeight) {

		if (null == mTextureView || null == mPreviewSize || null == mActivity) {
			return;
		}
		int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
		Matrix matrix = new Matrix();
		RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
		RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
		float centerX = viewRect.centerX();
		float centerY = viewRect.centerY();
		if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
			bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
			matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
			float scale = Math.max(
					(float) viewHeight / mPreviewSize.getHeight(),
					(float) viewWidth / mPreviewSize.getWidth());
			matrix.postScale(scale, scale, centerX, centerY);
			matrix.postRotate(90 * (rotation - 2), centerX, centerY);
		}
		mTextureView.setTransform(matrix);
	}


	/**
	 * Starts a background thread and its {@link Handler}.
	 */
	public void startBackgroundThread() {
		mBackgroundThread = new HandlerThread("CameraBackground");
		mBackgroundThread.start();
		mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
	}

	/**
	 * Stops the background thread and its {@link Handler}.
	 */
	public void stopBackgroundThread() {
		mBackgroundThread.quitSafely();
		try {
			mBackgroundThread.join();
			mBackgroundThread = null;
			mBackgroundHandler = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
