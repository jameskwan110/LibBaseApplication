package com.kwan.base.api;

import java.util.HashMap;

/**
 * Created by Mr.Kwan on 2016-4-7.
 */

public interface ServerSubscriberListener<T> {

	void onServerCompleted(int vocational_id, HashMap<String, Object> exData);

	void onServerError(int vocational_id, HashMap<String, Object> exData, Throwable throwable);

	void onServerNext(int vocational_id, HashMap<String, Object> exData, T serverMsg);

}
