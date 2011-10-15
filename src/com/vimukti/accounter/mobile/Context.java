package com.vimukti.accounter.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.utils.StringUtils;
import com.vimukti.accounter.web.client.core.ClientAddress;

public class Context {

	private MobileSession session;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Map<String, List<Object>> selectedRecords = new HashMap<String, List<Object>>();

	/**
	 * Creates new Instance
	 */
	public Context(MobileSession mobileSession) {
		this.session = mobileSession;
	}

	public Result forward(String command) {
		return null;
	}

	/**
	 * Returns the Date in the Inputs if Any
	 * 
	 * @return
	 */
	public Date getDate() {
		List<Date> dates = getDates();
		if (dates != null && !dates.isEmpty()) {
			return dates.get(0);
		}
		return null;

	}

	/**
	 * Returns the String in the Inputs if Any
	 * 
	 * @return
	 */
	public String getString() {
		List<String> strings = getStrings();
		if (strings != null && !strings.isEmpty()) {
			return strings.get(0);
		}
		return null;
	}

	public String getNumber() {
		List<Number> numbers = getNumbers();
		if (numbers != null && !numbers.isEmpty()) {
			return numbers.get(0).toString();
		}
		return null;
	}

	/**
	 * Returns the Strings in the Inputs if Any
	 * 
	 * @return
	 */
	public List<String> getStrings() {
		return (List<String>) this.attributes.get("string");

	}

	/**
	 * Returns the Dates in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Date> getDates() {
		return (List<Date>) this.attributes.get("dates");

	}

	/**
	 * Returns the Integer in the Inputs if Any
	 * 
	 * @return
	 */
	public Integer getInteger() {
		List<Integer> integers = getIntegers();
		if (integers != null && !integers.isEmpty()) {
			return integers.get(0);
		}
		return null;

	}

	/**
	 * Returns the Integers in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Integer> getIntegers() {
		return (List<Integer>) this.attributes.get("integers");

	}

	/**
	 * Returns the Numbers in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Number> getNumbers() {
		return (List<Number>) this.attributes.get("numbers");

	}

	/**
	 * Returns the Doubles in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Double> getDoubles() {
		return (List<Double>) this.attributes.get("doubles");

	}

	/**
	 * Returns the Double in the Inputs if Any
	 * 
	 * @return
	 */
	public Double getDouble() {
		List<Double> doubles = getDoubles();
		if (doubles != null && !doubles.isEmpty()) {
			return doubles.get(0);
		}
		return null;
	}

	public void setInputs(List<String> inputs) throws AccounterMobileException {
		try {
			List<Number> numbers = new ArrayList<Number>();
			List<Integer> integers = new ArrayList<Integer>();
			List<Double> doubles = new ArrayList<Double>();
			List<String> strings = new ArrayList<String>();
			List<Date> dates = new ArrayList<Date>();

			for (String string : inputs) {
				if (StringUtils.isInteger(string)) {
					int parseInt = Integer.parseInt(string);
					integers.add(parseInt);
					numbers.add(parseInt);
				}
				if (StringUtils.isDouble(string)) {
					double parseInt = Double.parseDouble(string);
					doubles.add(parseInt);
					numbers.add(parseInt);
				}
				if (StringUtils.isDate(string)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					dateFormat.parse(string);
					Date date = dateFormat.parse(string);
					dates.add(date);
				}
				strings.add(string);
			}
			this.attributes.put("numbers", numbers);
			this.attributes.put("integers", integers);
			this.attributes.put("doubles", doubles);
			this.attributes.put("string", strings);
			this.attributes.put("dates", dates);
		} catch (Exception e) {
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_INVALID_INPUTS, e);
		}
	}

	public Session getHibernateSession() {
		return session.getHibernateSession();
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
		this.session.setAttribute(name, value);
	}

	/**
	 * Gets an Attributes from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return this.session.getAttribute(name);
	}

	/**
	 * Removes an Attribute from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object removeAttribute(String name) {
		return session.removeAttribute(name);
	}

	public Object getLast(RequirementType type) {
		return this.session.getLast(type);
	}

	public void setLast(RequirementType type, Object obj) {
		this.session.setLast(type, obj);
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
		List<Object> list = selectedRecords.get(name);
		if (list != null && !list.isEmpty()) {
			return (T) selectedRecords.get(name).get(0);
		}
		return null;
	}

	/**
	 * Returns the User Selected Records
	 * 
	 * @return
	 */
	public <T> List<T> getSelections(String name) {
		return (List<T>) selectedRecords.get(name);
	}

	public ClientAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public MobileSession getIOSession() {
		return session;
	}

	public User getUser() {
		return session.getUser();
	}

	public Company getCompany() {
		return session.getCompany();
	}
}
