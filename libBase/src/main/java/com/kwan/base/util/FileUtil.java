package com.kwan.base.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;


/**
 * Created by Mr.Kwan on 2017-7-25.
 */

public class FileUtil {

	public static void Unzip(String srcfile, String unrarPath) {
		if (TextUtils.isEmpty(srcfile) || TextUtils.isEmpty(unrarPath))
			return;
		File src = new File(srcfile);
		if (!src.exists())
			return;
		try {
			ZipFile zFile = new ZipFile(srcfile);
			zFile.setFileNameCharset("GBK");
			if (!zFile.isValidZipFile())
				throw new ZipException("文件不合法!");

			File destDir = new File(unrarPath);
			if (destDir.isDirectory() && !destDir.exists()) {
				destDir.mkdirs();
			}

//            zFile.extractAll(unrarPath);
			FileHeader fh = null;
			final int total = zFile.getFileHeaders().size();
			for (int i = 0; i < zFile.getFileHeaders().size(); i++) {
				fh = (FileHeader) zFile.getFileHeaders().get(i);
//                String entrypath = "";
//                if (fh.isFileNameUTF8Encoded()) {//解決中文乱码
//                    entrypath = fh.getFileName().trim();
//                } else {
//                    entrypath = fh.getFileName().trim();
//                }
//                entrypath = entrypath.replaceAll("\\\\", "/");
//
//                File file = new File(unrarPath + entrypath);
//                Log.d(TAG, "unrar entry file :" + file.getPath());

				zFile.extractFile(fh,unrarPath);

			}
		} catch (ZipException e1) {
			e1.printStackTrace();
		}
	}


//
//	/**
//	 * 解压Zip文件
//	 *
//	 * @param path 文件目录
//	 */
//	public static void Unzip(String path, String savepath) {
//		int count;
//		//String savepath = "";
//		final int buffer = 2048;
//		File file;
//		InputStream is = null;
//		FileOutputStream fos = null;
//		BufferedOutputStream bos = null;
//
//		//savepath = path.substring(0, path.lastIndexOf(".")) + File.separator; //保存解压文件目录
//
//		Log.e("kwan", " savepath " + savepath + " -- " + new File(savepath).mkdirs()); //创建保存目录
//		ZipFile zipFile = null;
//		try {
//			zipFile = new ZipFile(path); //解决中文乱码问题
//
//			Enumeration<?> entries = zipFile.entries();
//
//			while (entries.hasMoreElements()) {
//
//				byte buf[] = new byte[buffer];
//				ZipEntry entry = (ZipEntry) entries.nextElement();
//				String filename = entry.getName();
//
//				Log.e("kwan", "filename:" + filename);
//
//				boolean ismkdir = false;
//				if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
//					ismkdir = true;
//				}
//				filename = savepath + filename;
//
//				if (entry.isDirectory()) { //如果是文件夹先创建
//					file = new File(filename);
//					file.mkdirs();
//					continue;
//				}
//				file = new File(filename);
//				if (!file.exists()) { //如果是目录先创建
//					if (ismkdir) {
//						new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
//					}
//				}
//				file.createNewFile(); //创建文件
//
//				is = zipFile.getInputStream(entry);
//				fos = new FileOutputStream(file);
//				bos = new BufferedOutputStream(fos, buffer);
//
//				while ((count = is.read(buf)) > -1) {
//					bos.write(buf, 0, count);
//				}
//				bos.flush();
//				bos.close();
//				fos.close();
//
//				is.close();
//			}
//
//			zipFile.close();
//
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		} finally {
//			try {
//				if (bos != null) {
//					bos.close();
//				}
//				if (fos != null) {
//					fos.close();
//				}
//				if (is != null) {
//					is.close();
//				}
//				if (zipFile != null) {
//					zipFile.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static void CopyAssets(Resources resources, String assetDir, String dir) {

		Log.e("kwan", "CopyAssets");

		String[] files;
		try {
			// 获得Assets一共有几多文件
			files = resources.getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// 如果文件路径不存在
		if (!mWorkingPath.exists()) {
			// 创建文件夹
			if (!mWorkingPath.mkdirs()) {
				// 文件夹创建不成功时调用
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				// 获得每个文件的名字
				String fileName = files[i];
				// 根据路径判断是文件夹还是文件
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(resources, fileName, dir + fileName + "/");
					} else {
						CopyAssets(resources, assetDir + "/" + fileName, dir + "/"
								+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in;
				if (0 != assetDir.length())
					in = resources.getAssets().open(assetDir + "/" + fileName);
				else
					in = resources.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static final int VIDEO_TYPE_NETWORK = 1;
	public static final int VIDEO_TYPE_LOCAL = 2;

	//获取视频缩略图
	public static Bitmap createVideoThumbnail(String url, int type) {
		Bitmap bitmap = null;
		long duration = 0;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			//将网络文件以及本地文件区分开来设置
			if (type == VIDEO_TYPE_NETWORK) {
				retriever.setDataSource(url, new HashMap<String, String>());
			} else if (type == VIDEO_TYPE_LOCAL) {
				retriever.setDataSource(url);
			}

			String strDuration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
			duration = Long.valueOf(strDuration) / 2;
			bitmap = retriever.getFrameAtTime(duration, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

		} catch (IllegalArgumentException ex) {
			Log.e("kwan", ex.toString());
			Log.e("kwan", "获取视频缩略图失败");
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				Log.e("kwan", ex.toString());
				Log.e("kwan", "释放MediaMetadataRetriever资源失败");
			}
		}
		return bitmap;
	}


	public static File bitmapToFile(Bitmap bitmap, String path) {

		File file = new File(path);//将要保存图片的路径
		try {

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void deleteFile(File file) {

		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			Log.e("kwan", "delete 文件不存在！");
		}
	}
}
