package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.google.gwt.dev.util.collect.HashMap;
import com.vimukti.accounter.core.Address;

public class Context {

	private Session session;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Map<String, List<Object>> selectedRecords = new HashMap<String, List<Object>>();

	/**
	 * Creates new Instance
	 */
	public Context(Session hibernateSession) {
		this.session = hibernateSession;
	}

	public void forward(String command) {

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

	public void setInputs(List<String> inputs) {
		// TODO
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

	/**
	 * Adds an Attribute to the Context
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	/**
	 * Gets an Attributes from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Removes an Attribute from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public Object getLast(RequirementType customer) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param record
	 */
	public void putSelection(String name, Object obj) {
		if (selectedRecords.containsKey(name)) {
			selectedRecords.get(name).add(obj);
		} else {
			List<Object> records = new ArrayList<Object>();
			records.add(obj);
			selectedRecords.put(name, records);
		}
	}

	/**
	 * Returns the User Selected Record if isSingleSelect Result
	 * 
	 * @return
	 */
	public <T> T getSelection(String name) {
		return (T) selectedRecords.get(name).get(0);
	}

	/**
	 * Returns the User Selected Records
	 * 
	 * @return
	 */
	public <T> List<T> getSelections(String name) {
		return (List<T>) selectedRecords.get(name);
	}

	public Address getAddress() {
		// TODO Auto-generated method stub
		return null;
	}
}
