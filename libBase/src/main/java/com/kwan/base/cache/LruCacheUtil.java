package com.kwan.base.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.kwan.base.BaseApplication;
import com.kwan.base.rxbus.RxBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Mr.Kwan on 2016-4-1.
 */

public class LruCacheUtil<T> {

    private LruCache mLruCache;
    private DiskLruCache mDiskLruCache = null;
    private final long MAX_CACHEDATA_3DAY = 3 * 24 * 60 * 60 * 1000;
    private String mCachePath;

    public LruCacheUtil() {

        //初始化 内存缓存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mLruCache = new LruCache(maxMemory / 8);

        //初始化硬盘缓存
        try {

            File cacheDir = getDiskCacheDir(BaseApplication.getInstance(), "object");
            mCachePath = cacheDir.getPath();
            Log.d("LruCacheUtil", "mCachePath:" + cacheDir.getPath());
            Log.d("LruCacheUtil", "mCachePath:" + cacheDir.exists());
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            // 第一个参数指定的是数据的缓存地址，
            // 第二个参数指定当前应用程序的版本号，
            // 第三个参数指定同一个key可以对应多少个缓存文件，基本都是传1，
            // 第四个参数指定最多可以缓存多少字节的数据。

            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(BaseApplication.getInstance()), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 可以看到，当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。前者获取到的就是 /sdcard/Android/data/<application package>/com.kwan.base.cache 这个路径，
     * 而后者获取到的是 /data/data/<application package>/com.kwan.base.cache 这个路径。
     * 接着又将获取到的路径和一个uniqueName进行拼接，作为最终的缓存路径返回。
     * 那么这个uniqueName又是什么呢？其实这就是为了对不同类型的数据进行区分而设定的一个唯一值，
     * 比如说在网易新闻缓存路径下看到的bitmap、object等文件夹。
     *
     * @param context    上下文
     * @param uniqueName 对不同类型的数据进行区分而设定的一个唯一值
     * @return File
     */

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = context.getCacheDir().getPath();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null)
                cachePath = context.getExternalCacheDir().getPath();
        }


        return new File(cachePath + File.separator + uniqueName);
    }

    public void remove(String name) {
        try {

            String key = hashKeyForDisk(name);
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean isObjectCacheOutOfDate(String name) {

        File file = new File(mCachePath + File.separator + hashKeyForDisk(name) + ".0");

		Log.d("LruCacheUtil", file.getPath());
        Log.d("LruCacheUtil", "file.exists() " + file.exists());

        if (file.exists()) {

            long lastTime = file.lastModified();
            Log.d("LruCacheUtil", "lastModified:" + lastTime + " diff: " + (System.currentTimeMillis() - lastTime));
            return (System.currentTimeMillis() - lastTime) > MAX_CACHEDATA_3DAY;

        } else {
            return true;
        }


    }


    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 将图片写入缓存
     *
     * @param object 要写入的对象
     * @param name   写入对象的唯一名称
     */

    public void write(final T object, final String name) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {

                    String key = hashKeyForDisk(name);
                    //这个key将会成为缓存文件的文件名
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);

                    if (editor != null) {

                        OutputStream outputStream = editor.newOutputStream(0);
                        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                        oos.writeObject(object);
                        oos.close();
                        editor.commit();

                        Log.d("LruCacheUtil", "write");

                    }


                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    /**
     * 缓存中读取一个对象
     *
     * @param name 对象唯一名称
     * @return 对象
     */


    public void read(final String name) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    T object = null;
                    String key = hashKeyForDisk(name);
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);


//                    if (snapShot != null) {
                    InputStream is = snapShot.getInputStream(0);
                    ObjectInputStream ois = new ObjectInputStream(is);
                    object = (T) ois.readObject();
                    //通过RXBus发送
                    Log.d("LruCacheUtil", "read over post");
                    // RxBus.getDefault().post(object);
                    RxBus.getDefault().post("LruCacheUtil",object);
                } catch (Exception e) {
                    try {
                        mDiskLruCache.remove(name);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    RxBus.getDefault().error("LruCacheUtil",e);
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public interface OnCacheWrittenListener {
        void onCacheWritten();
    }

    public interface OnCacheReadOverListener<T> {
        void onCacheReadOver(T t);
    }


    OnCacheReadOverListener<T> readOverListener;

    public void setOnCacheReadOverListener(OnCacheReadOverListener<T> listener) {
        readOverListener = listener;
    }

}
