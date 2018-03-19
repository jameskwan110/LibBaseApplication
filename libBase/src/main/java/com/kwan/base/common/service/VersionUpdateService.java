package com.kwan.base.common.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kwan.base.R;
import com.kwan.base.common.config.Config;
import com.kwan.base.download.DownloadFileBean;
import com.kwan.base.download.DownloadTask;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VersionUpdateService extends Service {

	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_PAUSE = "ACTION_PAUSE";

	/**
	 * 下载任务集合 多任务（每个任务都是多线程断点续传）
	 */
	private List<DownloadTask> downloadTasks = new ArrayList<>();
	public static ExecutorService executorService = Executors.newCachedThreadPool();
	private DownLoadBroadcastReceiver receiver;
	private DownloadFileBean curDownloadFileBean;


	/**
	 * 网络状态
	 */
	private boolean netWorkStatus;

	/**
	 * 手动暂停下载
	 */
	private boolean isUserPause;
	//private ArrayList<DownloadTask> downloadTasks = new ArrayList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new DownLoadBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Config.ACTION_DOWNLOAD_START);
		intentFilter.addAction(Config.ACTION_DOWNLOAD_REFRESH);
		intentFilter.addAction(Config.ACTION_DOWNLOAD_FININSHED);
		intentFilter.addAction(Config.ACTION_DOWNLOAD_PAUSE);
		intentFilter.addAction(Config.ACTION_DOWNLOAD_ERROR);
		intentFilter.addAction(Config.ACTION_DOWNLOAD_CLOSE);
		intentFilter.addAction(BUTTON_ACTION);
		intentFilter.addAction(BUTTON_CLOSE_ACTION);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent.getAction().equals(ACTION_START)) {
			isDownLoading = true;
			curDownloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");

			for (DownloadTask downloadTask : downloadTasks) {
				if (downloadTask.getFileBean().getUrl().equals(curDownloadFileBean.getUrl())) {
					//如果下载任务中以后该文件的下载任务 则直接返回
					return super.onStartCommand(intent, flags, startId);
				}
			}

			Intent intent2 = new Intent(Config.ACTION_DOWNLOAD_START);
			intent2.putExtra("DownloadFileBean", curDownloadFileBean);
			sendBroadcast(intent2);


		} else if (intent.getAction().equals(ACTION_PAUSE)) {

			DownloadFileBean downloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
			DownloadTask pauseTask = null;
			for (DownloadTask downloadTask : downloadTasks) {
				if (downloadTask.getFileBean().getUrl().equals(downloadFileBean.getUrl())) {
					downloadTask.pauseDownload();
					pauseTask = downloadTask;
					break;
				}
			}
			//将下载任务移除
			downloadTasks.remove(pauseTask);
		}
		return super.onStartCommand(intent, flags, startId);
	}


	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private NotificationManager notificationManager;
	private RemoteViews remoteView;
	private NotificationCompat.Builder builder;
	private final int notificationId = 100001;
	private boolean isDownLoading = false;
	private final String BUTTON_ACTION = "BUTTON_ACTION";
	private final String BUTTON_CLOSE_ACTION = "BUTTON_CLOSE_ACTION";

	/**
	 * 发送通知
	 */
	private void sendDefaultNotification() {

		remoteView = new RemoteViews(getPackageName(), R.layout.layout_notifi);

		if (TextUtils.isEmpty(Config.notificationTitle)) {
			remoteView.setTextViewText(R.id.textView, getString(R.string.notification_title));
		} else {
			remoteView.setTextViewText(R.id.textView, Config.notificationTitle);
		}
		if (Config.notificaionIconResId != 0) {
			remoteView.setImageViewResource(R.id.icon, Config.notificaionIconResId);
		}
		Intent buttonAction = new Intent(BUTTON_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, buttonAction, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.btn1, pendingIntent);

		Intent closeAction = new Intent(BUTTON_CLOSE_ACTION);
		PendingIntent closeIntent = PendingIntent.getBroadcast(this, 1, closeAction, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.btnClose, closeIntent);

		builder = new NotificationCompat.Builder(this);
		if (Config.notificaionSmallIconResId == 0) {
			builder.setSmallIcon(R.mipmap.ic_launcher);
		} else {
			builder.setSmallIcon(Config.notificaionSmallIconResId);
		}
		builder.setTicker(getString(R.string.notification_ticker));
		builder.setContent(remoteView);
		builder.setAutoCancel(true);
		builder.setOngoing(true);
		builder.setPriority(Notification.PRIORITY_MAX);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, builder.build());
	}

	private void updataNofication(int progress, long size, String speed) {
		if (isDownLoading) {
			remoteView.setImageViewResource(R.id.btn1, R.mipmap.ic_pause);
		} else {
			remoteView.setImageViewResource(R.id.btn1, R.mipmap.ic_continue);
		}
		remoteView.setProgressBar(R.id.progressBar, 100, progress, false);
		DecimalFormat df = new DecimalFormat("#.##");

		remoteView.setTextViewText(R.id.textSize, df.format(b2mbDouble(size)) + "");
		remoteView.setTextViewText(R.id.textSpeed, speed + "kb/s");
		notificationManager.notify(notificationId, builder.build());
	}

	public double b2mbDouble(long b) {
		return b * 1.0 / 1024 / 1024;
	}

	/**
	 * 关闭通知
	 */
	private void cancleNotification() {
		notificationManager.cancel(notificationId);
	}

	private class DownLoadBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Config.ACTION_DOWNLOAD_START)) { //开始下载
				isDownLoading = true;
				DownloadFileBean downloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
				//开始下载
				DownloadTask curDownloadTask= new DownloadTask(VersionUpdateService.this, downloadFileBean).startDownload();
				downloadTasks.add(curDownloadTask);

				//发送通知
				if (notificationManager == null) {
					sendDefaultNotification();
				}
			} else if (action.equals(Config.ACTION_DOWNLOAD_FININSHED)) {//下载完成

				DownloadFileBean downloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
				installPage(downloadFileBean);
				cancleNotification();
				curDownloadFileBean = null;


			} else if (action.equals(Config.ACTION_DOWNLOAD_REFRESH)) {//下载更新

				DownloadFileBean downloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
				DecimalFormat df = new DecimalFormat("#.##");
				int progress = (int) (downloadFileBean.getFinished() * 1.0f / downloadFileBean.getLength() * 1.0f * 100);
				int speed = intent.getIntExtra("Speed", 0);
				String format = df.format(speed * 1.0 / 1024);
				updataNofication(progress, downloadFileBean.getLength(), format);


			} else if (action.equals(Config.ACTION_DOWNLOAD_PAUSE)) {//下载暂停

				isDownLoading = false;
				DownloadFileBean fileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
				int progress = (int) (fileBean.getFinished() * 1.0f / fileBean.getLength() * 1.0f * 100);
				updataNofication(progress, fileBean.getLength(), "0");


			} else if (action.equals(BUTTON_CLOSE_ACTION)) {//点击取消下载按钮
				if (curDownloadFileBean != null) {

					DownloadTask task = null;
					for (DownloadTask downloadTask : downloadTasks) {
						if (downloadTask.getFileBean().getUrl().equals(curDownloadFileBean.getUrl())) {
							downloadTask.closeDownload();
							task = downloadTask;
							break;
						}
					}
					if (task != null) {
						downloadTasks.remove(task);
					} else {
						DownloadTask downloadTask = new DownloadTask(VersionUpdateService.this, curDownloadFileBean);
						downloadTask.closeDownload();
					}
				}


			} else if (action.equals(Config.ACTION_DOWNLOAD_CLOSE)) {//取消下载
				DownloadFileBean downloadFileBean = (DownloadFileBean) intent.getSerializableExtra("DownloadFileBean");
				File file = new File(Config.downLoadPath, downloadFileBean.getFileName());
				if (file.exists()) {
					file.delete();
				}
				cancleNotification();
			} else if (action.equals(BUTTON_ACTION)) {
				if (!netWorkStatus) {
					Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
					return;
				}
				if (isDownLoading) {
					isUserPause = true;
					Intent startIntent = new Intent(context, VersionUpdateService.class);
					startIntent.setAction(VersionUpdateService.ACTION_PAUSE);
					startIntent.putExtra("DownloadFileBean", curDownloadFileBean);
					startService(startIntent);
				} else {
					isUserPause = false;
					Intent startIntent = new Intent(context, VersionUpdateService.class);
					startIntent.setAction(VersionUpdateService.ACTION_START);
					startIntent.putExtra("DownloadFileBean", curDownloadFileBean);
					startService(startIntent);
				}
			} else if (action.equals(Config.ACTION_DOWNLOAD_ERROR)) {
				Toast.makeText(context, intent.getStringExtra("error"), Toast.LENGTH_SHORT).show();
			} else {
				if (curDownloadFileBean == null) return;
				ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
					//网络连接已断开
					netWorkStatus = false;
					if (!isDownLoading) return;
					Intent startIntent = new Intent(context, VersionUpdateService.class);
					startIntent.setAction(VersionUpdateService.ACTION_PAUSE);
					startIntent.putExtra("DownloadFileBean", curDownloadFileBean);
					startService(startIntent);
				} else {
					netWorkStatus = true;
					//网络连接已连接
					if (isDownLoading || isUserPause) return;
					Intent startIntent = new Intent(context, VersionUpdateService.class);
					startIntent.setAction(VersionUpdateService.ACTION_START);
					startIntent.putExtra("DownloadFileBean", curDownloadFileBean);
					startService(startIntent);

				}
			}
		}
	}

	/**
	 * 自动安装
	 */
	private void installPage(DownloadFileBean filebean) {

		String filePath = filebean.getSavePath() + "/" + filebean.getFileName();
		File file = new File(filePath);

		Log.e("kwan", "path:" + filePath);

		if (!file.exists()) {
			throw new NullPointerException(
					"The package cannot be found");
		}
		Intent install = new Intent(Intent.ACTION_VIEW);
		// 调用系统自带安装环境
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
			install.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		getApplicationContext().startActivity(install);

	}


}

