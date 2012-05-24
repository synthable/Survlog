package com.grasscove.survlog.provider.model;

public class BaseModel {

	private boolean isNew = true;

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return isNew;
	}

}
