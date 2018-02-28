package com.kwan.base.api;


import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 服务器 接口
 * Created by Mr.Kwan on 2016-6-28.
 */
public interface BaseServerAPI {

	@Streaming
	@GET
	Flowable<ResponseBody> download(@Url String url);
	int DOWN_LOAD_VOCATIONAL_ID = 0x000000;

}
