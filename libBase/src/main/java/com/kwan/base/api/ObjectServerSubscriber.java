package com.kwan.base.api;


import android.util.Log;

import java.util.HashMap;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 基于ServerMsg 扩展的 监听
 *
 * Created by Mr.Kwan on 2016-4-7.
 */
public class ObjectServerSubscriber<T> extends DisposableSubscriber<T> {

	private ServerSubscriberListener<T> mListener;
	public int vocational_id = -1;
	public HashMap<String, Object> mExData;

	public ObjectServerSubscriber(ServerSubscriberListener listener) {
		mListener = listener;
	}

	/**
	 * 事件队列完结
	 * <br>
	 * 在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，
	 * 并且是事件序列中的最后一个。
	 * 需要注意的是，onCompleted() 和 onError() 二者也是互斥的，
	 * 即在队列中调用了其中一个，就不应该再调用另一个。
	 */
	@Override
	public void onComplete() {
		mListener.onServerCompleted(vocational_id, mExData);
	}

	@Override
	public void onError(Throwable e) {
		Log.e("ObjectServerSubscriber", "onError");
		mListener.onServerError(vocational_id, mExData, e);
	}

//	@Override
//	public void onSubscribe(Subscription s) {
//		Log.e("ObjectServerSubscriber", "onSubscribe");
//		s.request(Long.MAX_VALUE);
//	}

	@Override
	public void onNext(T msg) {
		Log.e("ObjectServerSubscriber", "onNext");
		mListener.onServerNext(vocational_id, mExData, msg);
	}

}
