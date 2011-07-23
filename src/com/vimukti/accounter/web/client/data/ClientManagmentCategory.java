package com.vimukti.accounter.web.client.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This object will have info about users who has access to this category and
 * mangers for it.
 * 
 * @author Gajendra
 * 
 */

public class ClientManagmentCategory implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7263281725277918341L;

	public Long id;

	/**
	 * Name of category Constant Defined
	 */
	public String categoryName;

	/**
	 * UserID and AceesLevelID(FULL, RESTRICTED)
	 */
	public Map<String, String> users = new HashMap<String, String>();

	public ClientManagmentCategory() {

	}

	public ClientManagmentCategory(String categoryName, long id,
			Set<String> managers, Map<String, String> users) {
		this.categoryName = categoryName;
		this.id = id;

		for (String user : users.keySet()) {
			this.users.put(user, users.get(user));
		}
	}

	public long getId() {
		return id;
	}

	public void setID(long id){
		this.id = id;
	}

	public String getCaetgoryName() {
		return categoryName;
	}

	public void setCaetgoryName(String caetgoryName) {
		this.categoryName = caetgoryName;
	}

	public Map<String, String> getUsers() {
		return users;
	}

	public void setUsers(Map<String, String> users) {
		this.users = users;
	}

}
