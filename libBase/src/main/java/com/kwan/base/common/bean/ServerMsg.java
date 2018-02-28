package com.kwan.base.common.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * 服务器 返回消息
 * Created by Mr.Kwan on 2016-6-28.
 */
public class ServerMsg<T> implements Serializable, MultiItemEntity {

    private int code;
    private boolean isSuc;
    private String message;
    private String name;
    private String nextpublishtime;
    private String servertime;
    private T result;

	//----------------------------------------

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextpublishtime() {
        return nextpublishtime;
    }

    public void setNextpublishtime(String nextpublishtime) {
        this.nextpublishtime = nextpublishtime;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ServerMsg{" +
                "code=" + code +
                ", isSuc=" + isSuc +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", nextpublishtime='" + nextpublishtime + '\'' +
                ", servertime='" + servertime + '\'' +
                ", result=" + result +
                '}';
    }

	@Override
	public int getItemType() {
		return 0;
	}
}
