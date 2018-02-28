package com.kwan.base.util;

import android.util.Log;

import com.kwan.base.mvp.view.activity.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 权限管理
 * Created by Mr.Kwan on 2017-8-9.
 */

public class PermissionUtil {

	private BaseActivity mActivity;

	public PermissionUtil(BaseActivity activity) {
		mActivity = activity;
	}

	public static List<String> mPermissionsNeeded = new ArrayList<>();
	private PermissionListener mPermissionListener;

	public void requestPermissions(final String... permissions) {

		RxPermissions rxPermission = new RxPermissions(mActivity);
		rxPermission
				.requestEach(permissions)
				.subscribe(new Consumer<Permission>() {
					@Override
					public void accept(Permission permission) throws Exception {
						if (permission.granted) {
							// 用户已经同意该权限
							Log.e("PermissionUtil", permission.name + " is granted.");
							mPermissionsNeeded.remove(permission.name);
							if (mPermissionListener != null)
								mPermissionListener.onGranted(permission.name);

						} else if (permission.shouldShowRequestPermissionRationale) {
							// 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
							Log.e("PermissionUtil", permission.name + " is denied. More info should be provided.");
							if (mPermissionListener != null)
								mPermissionListener.onShouldShowRequestPermissionRationale(permission.name);
						} else {
							// 用户拒绝了该权限，并且选中『不再询问』
							Log.e("PermissionUtil", permission.name + " is denied.");
							mPermissionsNeeded.add(permission.name);
							if (mPermissionListener != null)
								mPermissionListener.onDenied(permission.name);
						}
					}
				});
	}

	public void requestAllPermission(final String... permissions) {
		RxPermissions rxPermission = new RxPermissions(mActivity);
		rxPermission
				.request(permissions)
				.subscribe(new Consumer<Boolean>() {

					@Override
					public void accept(@NonNull Boolean granted) throws Exception {
						if (granted) {

							Log.e("PermissionUtil", "All granted");
						} else {

							Log.e("PermissionUtil", "At least one permission is denied");
						}
					}
				});
	}

	public boolean isAllPermissionGranted(ArrayList<String> permissions) {
		return !mPermissionsNeeded.containsAll(permissions);
	}

	public interface PermissionListener {
		void onGranted(String permission);

		void onShouldShowRequestPermissionRationale(String permission);

		void onDenied(String permission);
	}

	public void setPermissionListener(PermissionListener listener) {
		mPermissionListener = listener;
	}

}
