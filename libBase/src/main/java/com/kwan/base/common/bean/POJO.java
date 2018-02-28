package com.kwan.base.common.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 *
 * Created by Mr.Kwan on 2016-6-28.
 */


public class POJO implements Serializable, MultiItemEntity, Cloneable {

	private String tag;
	private int itemType;
	private POJO data;

	public POJO getData() {
		return data;
	}

	public void setData(POJO data) {
		this.data = data;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
