package com.kwan.base.camera2;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;



import java.io.IOException;

/**
 * 带录制的视频的 TextureView
 * <p>
 * 使用camera2
 * <p>
 * Created by Mr.Kwan on 2017-8-3.
 */

public class VideoTextureView extends AutoFitTextureView implements TextureView.SurfaceTextureListener, SurfaceTexture.OnFrameAvailableListener {


	private CameraInstance mCameraInstance;
	private Activity mActivity;
	private final String TAG = "VideoTextureView";


	private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
	private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
	private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
	private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

	static {
		DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
		DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
		DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
		DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}

	static {
		INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
		INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
		INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
		INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
	}

	private String mNextVideoAbsolutePath;


	public VideoTextureView(Context context) {
		super(context);
	}

	public VideoTextureView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoTextureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 初始化
	 *
	 * @param activity  activity
	 * @param videoPath 录制完存储视频的Path
	 */

	public void init(@NonNull Activity activity, @NonNull String videoPath) {
		mActivity = activity;
		mNextVideoAbsolutePath = videoPath;
		mCameraInstance = new CameraInstance(mActivity, this);
		mCameraInstance.startBackgroundThread();
		setSurfaceTextureListener(this);

	}




	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
		mCameraInstance.openCamera(width, height);

		surfaceTexture.setOnFrameAvailableListener(this);
		//mFrameRecorder = new CGEFrameRecorder();

		Log.e("kwan", "surfaceTexture::" + width + "--" + height);
		Log.e("kwan", "mVideoSize::" + mCameraInstance.mVideoSize.getWidth()
				+ "--" + mCameraInstance.mVideoSize.getHeight());

//		if (!mFrameRecorder.init(width, height, mCameraInstance.mVideoSize.getWidth(), mCameraInstance.mVideoSize.getHeight())) {
//			Log.e(TAG, "Frame Recorder init failed!");
//		}
//
//		mFrameRecorder.setSrcRotation((float) (Math.PI / 2.0));
//		mFrameRecorder.setSrcFlipScale(1.0f, -1.0f);
//		mFrameRecorder.setRenderFlipScale(1.0f, -1.0f);
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
		mCameraInstance.configureTransform(width, height);
		Log.d("kwan","onSurfaceTextureSizeChanged");
		//mFrameRecorder.srcResize(width, height);
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
		Log.d("kwan","onSurfaceTextureDestroyed");
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
		Log.d("kwan","onSurfaceTextureUpdated");
	}

	public void closeAll() {
		closeCamera();
		mCameraInstance.stopBackgroundThread();
	}

	private void closeCamera() {

		mCameraInstance.closeCamera();
//		if (null != mMediaRecorder) {
//			mMediaRecorder.release();
//			mMediaRecorder = null;
//		}
	}
//
//	public void startRecordingVideo() {
//		if (null == mCameraInstance.mCameraDevice || !isAvailable() || null == mCameraInstance.mPreviewSize) {
//			return;
//		}
//		try {
//			mCameraInstance.closePreviewSession();
//			setUpMediaRecorder();
//			SurfaceTexture texture = getSurfaceTexture();
//			assert texture != null;
//			texture.setDefaultBufferSize(mCameraInstance.mPreviewSize.getWidth(), mCameraInstance.mPreviewSize.getHeight());
//			mCameraInstance.mPreviewBuilder = mCameraInstance.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//			List<Surface> surfaces = new ArrayList<>();
//
//			// Set up Surface for the camera preview
//			Surface previewSurface = new Surface(texture);
//			surfaces.add(previewSurface);
//			mCameraInstance.mPreviewBuilder.addTarget(previewSurface);
//
//			// Set up Surface for the MediaRecorder
//			Surface recorderSurface = mMediaRecorder.getSurface();
//			surfaces.add(recorderSurface);
//			mCameraInstance.mPreviewBuilder.addTarget(recorderSurface);
//
//			// Start a capture session
//			// Once the session starts, we can update the UI and start recording
//			mCameraInstance.mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
//
//				@Override
//				public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//					mCameraInstance.mPreviewSession = cameraCaptureSession;
//					mCameraInstance.updatePreview();
//					mActivity.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							// UI
//
//							mIsRecordingVideo = true;
//							// Start recording
//							mMediaRecorder.start();
//							if (mRecorderListener != null)
//								mRecorderListener.onStartRecorder();
//							// TODO 这里回调 activity 开始录制
//						}
//					});
//				}
//
//				@Override
//				public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//					// TODO 这里回调 activity 开始录制失败
//					if (mRecorderListener != null)
//						mRecorderListener.onStartRecorder();
//				}
//			}, mCameraInstance.mBackgroundHandler);
//		} catch (CameraAccessException | IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	public void stopRecordingVideo() {
//		// UI
//		mIsRecordingVideo = false;
//		//必须将这一句放置到MediaRecorder停止释放的前面，
//		//否则就会造成，接收数据方（Encoder）已经停止了，而发送数据的session还在运行。才会造成以上错误。
//		try {
//			mCameraInstance.mPreviewSession.stopRepeating();
//			mCameraInstance.mPreviewSession.abortCaptures();
//		} catch (CameraAccessException e) {
//			e.printStackTrace();
//		}
//		// Stop recording
//		mMediaRecorder.stop();
//		mMediaRecorder.reset();
//		mMediaRecorder.release();
//
//		if (null != mActivity) {
//			Toast.makeText(mActivity, "Video saved: " + mNextVideoAbsolutePath,
//					Toast.LENGTH_SHORT).show();
//			Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
//		}
//		mNextVideoAbsolutePath = null;
//
//	}


	/**
	 * 设置 录制参数
	 *
	 * @throws IOException
	 */

//	private void setUpMediaRecorder() throws IOException {
//
//		if (null == mActivity) {
//			return;
//		}
//		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//
//
//		mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
//		mMediaRecorder.setVideoEncodingBitRate(1024 * 1024);
//		mMediaRecorder.setVideoFrameRate(30);
//		mMediaRecorder.setVideoSize(mCameraInstance.mVideoSize.getWidth(), mCameraInstance.mVideoSize.getHeight());
//		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//		mMediaRecorder.setMaxDuration(5 * 1000);
//
//		int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
//		switch (mCameraInstance.mSensorOrientation) {
//			case SENSOR_ORIENTATION_DEFAULT_DEGREES:
//				mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
//				break;
//			case SENSOR_ORIENTATION_INVERSE_DEGREES:
//				mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
//				break;
//		}
//		mMediaRecorder.prepare();
//	}

	private RecorderListener mRecorderListener;

	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
Log.e("kwan","onFrameAvailable");
	}

	public interface RecorderListener {

		void onStartRecorder();

		void onStartRecorderFail();
	}

	public void setRecorderListener(RecorderListener recorderListener) {
		this.mRecorderListener = recorderListener;
	}
}
