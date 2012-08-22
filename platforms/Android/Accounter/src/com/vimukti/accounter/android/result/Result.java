package com.vimukti.accounter.android.result;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private List<Object> resultParts = new ArrayList<Object>();
	private String cookie;
	private String title;
	private boolean hideCancel;
	private boolean showBack;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isHideCancel() {
		return hideCancel;
	}

	public void setHideCancel(boolean hideCancel) {
		this.hideCancel = hideCancel;
	}

	public boolean isShowBack() {
		return showBack;
	}

	public void setShowBack(boolean showBack) {
		this.showBack = showBack;
	}

	public void setResultParts(List<Object> resultParts) {
		this.resultParts = resultParts;
	}

	public List<Object> getResultParts() {
		return resultParts;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}
}
