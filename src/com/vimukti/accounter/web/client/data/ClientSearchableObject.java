/**
 * 
 */
package com.vimukti.accounter.web.client.data;

import java.util.Date;

/**
 * @author vimukti5
 * 
 */
public class ClientSearchableObject implements ISearchResultObject, Cloneable {

	public static final String ID = "id";

	public static final String ICON_PATH = "iconpath";
	public static final String NAME = "name";
	public static final String PARENT = "parent";
	public static final String WORKSPACE = "workspace";
	public static final String DATE = "date";

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the iconPath
	 */
	public String getIconPath() {
		return iconPath;
	}

	/**
	 * @param iconPath
	 *            the iconPath to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the workspace
	 */
	public String getWorkspace() {
		return workspace;
	}

	/**
	 * @param workspace
	 *            the workspace to set
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	public void setDate(Date date) {
		if (date != null)
			this.date = date.getTime();
	}

	String id;

	String iconPath;

	String name;

	String parent;

	String workspace;

	String spaceId;

	SearchObjectType type;

	long date;

	public ClientSearchableObject() {

	}

	@Override
	public SearchObjectType getType() {

		return type;
	}

	public void setType(SearchObjectType type) {
		this.type = type;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceId() {
		return spaceId;
	}

}
