package com.kwan.base.rxbus;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Mr.Kwan on 2016-4-6.
 */
public class RxBusSubscriberListener<T> implements Consumer<T> {
	@Override
	public void accept(@NonNull T t) throws Exception {

	}

//     void onRxBusCompleted();
//
//     void onRxBusError(Throwable e);
//
//     void onRxBusNext(T t);

}
