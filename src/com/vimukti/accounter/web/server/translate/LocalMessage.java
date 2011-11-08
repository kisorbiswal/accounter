package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;
import java.sql.Date;

import com.vimukti.accounter.core.Client;

public class LocalMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int id;

	Message message;

	String lang;

	String value;

	int ups;

	int downs;

	boolean isApproved;

	Client createdBy;

	Date createdDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getUps() {
		return ups;
	}

	public void setUps(int ups) {
		this.ups = ups;
	}

	public int getDowns() {
		return downs;
	}

	public void setDowns(int downs) {
		this.downs = downs;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Client getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Client createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
