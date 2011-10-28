package com.vimukti.accounter.core;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author vimukti2
 * 
 */
public class Entity {

	private long id;

	private Date sqlDate;

	private boolean sticky;

	private String url;

	private String newsText;

	public boolean isSticky() {
		return sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}

	public Date getSqlDate() {
		return sqlDate;
	}

	public void setSqlDate(Timestamp sqlDate) {
		this.sqlDate = sqlDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
