package com.kwan.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.kwan.base.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class SysUtil {

	public static final int NETWORK_NONE = 0;
	public static final int NETWORK_MOBILE = 1;
	public static final int NETWORK_WIFI = 2;

	public static String getAppPackageName() {
		return BaseApplication.getInstance().getPackageName();
	}

	public static int getNetworkState() {

		ConnectivityManager connManager = (ConnectivityManager) BaseApplication
				.getInstance().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORK_WIFI;
		}

		// 3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORK_MOBILE;
		}
		return NETWORK_NONE;
	}

	public static String milliSecond2Date(String template, long time) {
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
		return sdf.format(d);
	}

	public static String getUniqueId(Context context) {
		//将使用Android的ID必要作为UniqueID的组成部分
		String device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		System.out.println("ANDROID_ID:" + device_id);

		try {
			//如果有电话设备，将加入DeviceId作为UniqueID的组成部分
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			if (tm != null) {
				device_id += tm.getDeviceId();
				System.out.println("DEVICE_ID:" + tm.getDeviceId());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//如果有WIFI设备，将加入WIFI设备MAC作为UniqueID的组成部分
		if (null != getMacAddress(context)) {
			String mac = getMacAddress(context);
			System.out.println("MAC:" + mac);
			if (mac != null)
				device_id += mac;
		}

		System.out.println("UNIQUE_ID:" + (device_id = MD5(device_id)));
		return device_id;
	}

	public static String getMacAddress(Context context) {
		String mac = null;
		if (Build.VERSION.SDK_INT < 23) {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null)
				mac = info.getMacAddress();
		} else {
			try {
				Class sysConfig;
				sysConfig = Class.forName("android.os.SystemProperties");
				String deviceName = (String) sysConfig.getMethod("get", String.class, String.class).invoke(null, "wifi.interface", "wlan0");
				if (deviceName == null)
					return null;
				if (null == NetworkInterface.getByName(deviceName)) {
					return null;
				}
				if (null == NetworkInterface.getByName(deviceName).getHardwareAddress()) {
					return null;
				}
				byte[] bytesMac = NetworkInterface.getByName(deviceName).getHardwareAddress();
				StringBuilder buf = new StringBuilder();
				for (byte b : bytesMac) {
					buf.append(String.format("%02X:", b));
				}
				if (buf.length() > 0) {
					buf.deleteCharAt(buf.length() - 1);
				}
				mac = buf.toString();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			}

		}
		return mac;
	}

	public static String MD5(String s) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String md5(String string) {
		if (TextUtils.isEmpty(string)) {
			return "";
		}
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(string.getBytes());
			String result = "";
			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取当前的本地时间戳
	 */
	public static String getLocalTimestamp() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Random random = new Random();
		int number = random.nextInt(100000000);
		return sDateFormat.format(new java.util.Date()) + number;
	}


}
