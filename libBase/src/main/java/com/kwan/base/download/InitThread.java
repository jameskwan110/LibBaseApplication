package com.kwan.base.download;

import android.content.Context;

/**
 * 下载初始化线程
 *
 *  1.获取文件 长度
 *
 */

public class InitThread extends Thread {

    private Context context;
    private FileBean fileBean;

    public InitThread(Context context, FileBean fileBean) {
        this.context = context;
        this.fileBean = fileBean;
    }

    @Override
    public void run() {
      //  HttpURLConnection connection = null;
      //  RandomAccessFile randomAccessFile = null;


        	  //获取长度

//            URL url = new URL(fileBean.getUrl());
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(10000);
//            connection.setRequestMethod("GET");
//            int fileLength = -1;
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                fileLength = connection.getContentLength();
//            }

//			RetrofitAPI.createService(RetrofitZj.class).download(start, url3)
//					.subscribeOn(Schedulers.io())
//					.observeOn(AndroidSchedulers.mainThread())
//					.subscribe(new Observer<ResponseBody>() {
//						@Override
//						public void onCompleted() {
//
//						}
//
//						@Override
//						public void onError(Throwable e) {
//
//						}
//
//						@Override
//						public void onNext(ResponseBody responseBody) {
//							l = responseBody.contentLength();
//							Log.i("=====length=====", "onNext: " + l);
//							downloadFile(l, url2);
//						}
//					});

//
//			// 没有获取到长度 发送异常
//            if (fileLength <= 0) {
//                Intent intent = new Intent(Config.ACTION_DOWNLOAD_ERROR);
//                intent.putExtra("error", context.getString(R.string.error_url));
//                context.sendBroadcast(intent);
//                return;
//            }
//            //判断当前版本的APK是否下载过
//            File downLoadedFile = new File(Config.downLoadPath, fileBean.getFileName());
//            if (downLoadedFile.exists()) {
//                String versionNameFromApk = MD5Util.getVersionNameFromApk(context, downLoadedFile.getPath());
//                Log.w("AAA", "versionNameFromApk:" + versionNameFromApk + "curVersion:" + fileBean.getVersion());
//                if (!TextUtils.isEmpty(versionNameFromApk) &&
//                        !TextUtils.isEmpty(fileBean.getVersion()) &&
//                        versionNameFromApk.equals(fileBean.getVersion())) {
//                    Intent intent = new Intent(Config.ACTION_DOWNLOAD_FININSHED);
//
//                    intent.putExtra("FileBean", fileBean);
//                    context.sendBroadcast(intent);
//                    return;
//                }
//            }
//
//            File dir = new File(Config.downLoadPath);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            File file = new File(dir, fileBean.getFileName());
//            randomAccessFile = new RandomAccessFile(file, "rwd");
//            randomAccessFile.setLength(fileLength);
//            fileBean.setLength(fileLength);
//
//            Intent intent = new Intent(Config.ACTION_DOWNLOAD_START);
//            intent.putExtra("FileBean", fileBean);
//            context.sendBroadcast(intent);



    }
}
