package com.vimukti.accounterbb.result;

import java.util.Vector;

public class Result {
	private String title;
	private boolean showBack;
	private boolean hideCancel;
	
	private Vector resultParts = new Vector();
	private String cookie;
	
	public void setResultParts(Vector resultParts) {
		this.resultParts = resultParts;
	}

	public Vector getResultParts() {
		return resultParts;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setHideCancel(boolean hideCancel) {
		this.hideCancel = hideCancel;
	}

	public boolean isHideCancel() {
		return hideCancel;
	}

	public void setShowBack(boolean showBack) {
		this.showBack = showBack;
	}

	public boolean isShowBack() {
		return showBack;
	}


}
