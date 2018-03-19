package com.kwan.base.download;

import java.io.Serializable;

public class DownloadFileBean implements Serializable {

    private int id;
    private String fileName;
    private String savePath;
    private String url;
    private long length;
    private long finished;
    private int version;

    public DownloadFileBean(int id, String fileName, String savePath , String url, long length, long finished) {
        this.id = id;
        this.fileName = fileName;
        this.savePath = savePath;
        this.url = url;
        this.length = length;
        this.finished = finished;
    }

	public void setFinished(long finished) {
		this.finished = finished;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
}
