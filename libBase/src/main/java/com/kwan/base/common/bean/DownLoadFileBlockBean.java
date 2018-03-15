package com.kwan.base.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/15.
 */
@Entity
public class DownLoadFileBlockBean extends POJO {

	@Id
	private Long id;
	@Property(nameInDb = "START")
	private Long start;
	@Property(nameInDb = "END")
	private Long end;
	@Property(nameInDb = "URL")
	private String url;
	@Property(nameInDb = "FINISHED")
	private Long finished;
	@Generated(hash = 1583844563)

	public DownLoadFileBlockBean(Long id, String url, Long start, Long end, Long finished) {
		this.id = id;
		this.url = url;
		this.start = start;
		this.end = end;
		this.finished = finished;
	}
	@Generated(hash = 1510983810)
	public DownLoadFileBlockBean() {
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStart() {
		return this.start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getEnd() {
		return this.end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getFinished() {
		return this.finished;
	}
	public void setFinished(Long finished) {
		this.finished = finished;
	}
	
}
