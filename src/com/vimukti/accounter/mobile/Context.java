package com.vimukti.accounter.mobile;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

public class Context {

	private Session session;

	public void process(String command) {

	}

	public Date getDate() {
		return null;

	}

	public String getString() {
		return null;

	}

	public List<String> getStrings() {
		return null;

	}

	public List<Date> getDates() {
		return null;

	}

	public Integer getInteger() {
		return null;

	}

	public List<Integer> getIntegers() {
		return null;

	}

	public List<Number> getNumbers() {
		return null;

	}

	public List<Double> getDoubles() {
		return null;

	}

	public Double getDouble() {
		return null;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Result makeResult() {
		return new Result();
	}

	public Object getLast(RequirementType customer) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> getSelections() {
		// TODO Auto-generated method stub
		return null;
	}
}
