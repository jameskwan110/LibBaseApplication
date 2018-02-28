package com.kwan.base.rxbus;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 *
 * Created by Mr.Kwan on 2016-4-6.
 */
public class RxBusSubscriber<T> implements Subscriber<T> {

    private RxBusSubscriberListener<T> mListener;

    public RxBusSubscriber(RxBusSubscriberListener<T> listener) {
        mListener = listener;
    }

    @Override
    public void onComplete() {
       // mListener.onRxBusCompleted();
    }

    @Override
    public void onError(Throwable e) {
       // mListener.onRxBusError(e);
    }

	@Override
	public void onSubscribe(Subscription s) {

	}

	@Override
    public void onNext(T t) {
       // mListener.onRxBusNext(t);
    }

}
