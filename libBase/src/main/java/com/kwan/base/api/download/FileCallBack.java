package com.kwan.base.api.download;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by Mr.Kwan on 2016-6-29.
 */
public abstract class FileCallBack<T> {


	public String destFileDir;
	public String destFileName;


	public FileCallBack(String destFileDir, String destFileName) {
		this.destFileDir = destFileDir;
		this.destFileName = destFileName;
	}


	public abstract void onSuccess(T t);

	/**
	 * @param progress 已经下载或上传字节数
	 * @param total    总字节数
	 * @param done     是否完成
	 */
	public abstract void onProgress(long progress, long total, boolean done);

	public abstract void onStart();

	public abstract void onCompleted();

	public abstract void onError(Throwable e);

	public void saveFile(ResponseBody body) {
		InputStream is = null;
		byte[] buf = new byte[2048];
		int len;
		FileOutputStream fos = null;
		try {
			is = body.byteStream();
			File dir = new File(destFileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, destFileName);
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			//unsubscribe();
			onCompleted();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
				if (fos != null) fos.close();
			} catch (IOException e) {
				Log.e("saveFile", e.getMessage());
			}
		}
	}

}
