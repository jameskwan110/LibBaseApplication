package com.kwan.base.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2018/3/15.
 */
@Entity
public class DownLoadFileBlockBean extends POJO {

	@Id
	private long id;
	@Property(nameInDb = "START")
	private long start;
	@Property(nameInDb = "END")
	private long end;
	@Property(nameInDb = "URL")
	private String url;
	@Property(nameInDb = "FINISHED")
	private long finished;


	@Keep
	public DownLoadFileBlockBean(long id, String url, long start, long end, long finished) {
		this.id = id;
		this.url = url;
		this.start = start;
		this.end = end;
		this.finished = finished;
	}

	public DownLoadFileBlockBean() {
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getStart() {
		return this.start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return this.end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getFinished() {
		return this.finished;
	}
	public void setFinished(long finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "DownLoadFileBlockBean{" +
				"id=" + id +
				", start=" + start +
				", end=" + end +
				", finished=" + finished +
				'}';
	}
}
