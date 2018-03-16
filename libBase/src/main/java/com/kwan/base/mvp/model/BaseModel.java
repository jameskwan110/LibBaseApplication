package com.kwan.base.mvp.model;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kwan.base.api.BaseAPIUtil;
import com.kwan.base.api.ServerSubscriberListener;
import com.kwan.base.api.ServerUploadSubscriber;
import com.kwan.base.common.bean.ServerMsg;
import com.kwan.base.mvp.presenter.IBasePresenter;
import com.kwan.base.util.SysUtil;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.orhanobut.logger.Logger.t;


/**
 * Created by Mr.Kwan on 2016-10-8.
 */

public abstract class BaseModel implements ServerSubscriberListener<Object>, ServerUploadSubscriber.ServerUploadSubscriberListener {

	protected IBasePresenter mIBasePresenter;
	protected BaseAPIUtil mBaseAPIUtil;
	private static ArrayMap<Integer, CompositeDisposable> netManager = new ArrayMap<>();

	public BaseModel(IBasePresenter iBasePresenter) {
		mIBasePresenter = iBasePresenter;
		mBaseAPIUtil = getBaseApiUtil();
	}

	public abstract BaseAPIUtil getBaseApiUtil();

	/**
	 * @param args
	 * @return
	 */
	protected String getJsonStrArg(HashMap<String, Object> args) {
		Gson gson = new Gson();
		String json = gson.toJson(args);
		t("Args::").json(json);
		return json;
	}

	protected static boolean checkNetWorkAvailable() {
		//Log.d("BaseModel", "checkNetWorkAvailable:" + SysUtil.getNetworkState());
		return SysUtil.getNetworkState() == SysUtil.NETWORK_NONE;
	}

	@Override
	public void onServerCompleted(int vocational_id, HashMap<String, Object> exData) {

	}

	@Override
	public void onServerError(int vocational_id, HashMap<String, Object> exData, Throwable throwable) {
		mIBasePresenter.onServerError(vocational_id, exData, throwable);
	}

	@Override
	public void onServerNext(int vocational_id, HashMap<String, Object> exData, Object serverMsg) {
		mIBasePresenter.onServerSuccess(vocational_id, exData, serverMsg);
	}

	@Override
	public void onServerUploadCompleted(int vocational_id, HashMap exdata) {
		mIBasePresenter.onServerUploadCompleted(vocational_id, exdata);
	}

	@Override
	public void onServerUploadNext(int vocational_id, HashMap exdata, Object s) {
		if (s instanceof ServerMsg)
			if (((ServerMsg) s).isSuc()) {
				mIBasePresenter.onServerUploadNext(vocational_id, exdata, s);
			} else {
				mIBasePresenter.onServerFailed(((ServerMsg) s).getMessage());
			}
		else
			mIBasePresenter.onServerUploadNext(vocational_id, exdata, s);
	}

	@Override
	public void onServerUploadError(int vocational_id, HashMap exdata, Throwable e) {
		mIBasePresenter.onServerUploadError(vocational_id, exdata, e);
	}


	private class TransFormJson<T> {

		private Function<String, Flowable<T>> getTransFormer(final java.lang.reflect.Type type) {
			return new Function<String, Flowable<T>>() {
				@Override
				public Flowable<T> apply(@NonNull String s) throws Exception {
					Logger.json(s);
					GsonBuilder gsonBuilder = new GsonBuilder();
					Gson gson = gsonBuilder.create();
					final T msg = gson.fromJson(s, type);
					Log.e("ServerBack::", "fromJson ok");

					return Flowable.create(new FlowableOnSubscribe<T>() {
						@Override
						public void subscribe(@NonNull FlowableEmitter<T> e) throws Exception {
							e.onNext(msg);
						}
					}, BackpressureStrategy.BUFFER);
				}
			};
		}
	}

	/**
	 * @param <T> Json转换器 最终转换 返回的类型
	 */

	public class TransFlowable<T> {

		public FlowableTransformer<String, T> getFlowableTransformer(Class clz) {
			//final java.lang.reflect.Type type = type(ServerMsg.class, clz);
			final java.lang.reflect.Type type = type(clz, clz);

			return new FlowableTransformer<String, T>() {
				@Override
				public Publisher<T> apply(@NonNull Flowable<String> upstream) {
					return upstream
							.retry(5)
							.subscribeOn(Schedulers.io())
							.doOnSubscribe(new Consumer<Subscription>() {
								@Override
								public void accept(@NonNull Subscription subscription) throws Exception {
									//如果无网络连接，则直接取消了
									if (checkNetWorkAvailable()) {
										Log.e("compose", "no_network");
										mIBasePresenter.onNoNetWork();
										subscription.cancel();
										Log.e("compose", "no_network1");
									}
								}
							})
							.subscribeOn(Schedulers.io())
							.flatMap(new TransFormJson<T>().getTransFormer(type))
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.unsubscribeOn(Schedulers.io());
				}
			};
		}
	}

	private static ParameterizedType type(final Class raw, final Type... args) {

		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return raw;
			}
		};
	}


	//为了避免错误的取消了，key建议使用packagename + calssName
	public static void addDisposable(Integer key, Disposable disposable) {
		if (netManager.containsKey(key)) {
			netManager.get(key).add(disposable);
		} else {
			CompositeDisposable compositeDisposable = new CompositeDisposable();
			compositeDisposable.add(disposable);
			netManager.put(key, compositeDisposable);
		}
	}

	public static void removeDisposable(Integer key) {
		if (netManager.containsKey(key)) {
			CompositeDisposable compositeDisposable = netManager.get(key);
			compositeDisposable.clear();
		}
	}

}

