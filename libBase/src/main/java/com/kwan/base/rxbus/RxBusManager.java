package com.kwan.base.rxbus;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;


/**
 * Created by Mr.Kwan on 2017-7-28.
 */

public class RxBusManager {

	public RxBus mRxBus = RxBus.getDefault(); //拿到rxBus
	//管理rxbus订阅
	private Map<String, Subject<?>> mSubjects = new HashMap<>();
	/*管理Observables 和 Subscribers订阅*/
	private HashMap<String, CompositeDisposable> mSubscriptionMap = new HashMap<>();

	/**
	 * RxBus订阅
	 *
	 * @param eventName
	 * @param
	 */
	public <T> void on(String eventName, Class<T> type, Consumer<T> next) {

		Subject<T> mSubject = mRxBus.registerRxBus(eventName);
		mSubjects.put(eventName, mSubject);
		/*订阅管理*/
		addSubscription(eventName, mSubject.toFlowable(BackpressureStrategy.BUFFER)
				.ofType(type)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(next, new Consumer<Throwable>() {
					@Override
					public void accept(@NonNull Throwable throwable) throws Exception {
						Log.e("RXBusManager", "on throwable::" + throwable.toString());
					}
				}));

	}

	/**
	 * 保存订阅后的disposable
	 *
	 * @param key
	 * @param disposable
	 */

	public void addSubscription(String key, Disposable disposable) {

		if (mSubscriptionMap.get(key) != null) {
			mSubscriptionMap.get(key).add(disposable);
		} else {
			//一次性容器,可以持有多个并提供 添加和移除。
			CompositeDisposable disposables = new CompositeDisposable();
			disposables.add(disposable);
			mSubscriptionMap.put(key, disposables);
		}
	}

	/**
	 * 单个presenter生命周期结束，取消订阅和所有rxbus观察
	 */
	public void clear() {


		for (Map.Entry<String, Subject<?>> entry : mSubjects.entrySet()) {
			mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察

			if (mSubscriptionMap.get(entry.getKey()) != null) {
				// 取消所有订阅
				mSubscriptionMap.get(entry.getKey()).dispose();
			}
		}
		mSubjects.clear();
		mSubscriptionMap.clear();
		mSubjects = null;
		mRxBus = null;
		mSubscriptionMap = null;
	}

	//发送rxbus
	public void post(Object tag, Object content) {
		if (mRxBus != null)
			mRxBus.post(tag, content);
	}

}
