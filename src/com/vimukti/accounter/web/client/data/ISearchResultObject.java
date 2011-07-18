/**
 * 
 */
package com.vimukti.accounter.web.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * This Class to be Represented as, the Search Result Objects after a Lucene
 * Search.,
 * 
 * @author Fernandez
 * 
 */
public interface ISearchResultObject extends IsSerializable, Cloneable {

	String getId();

	String getIconPath();

	String getName();

	String getParent();

	String getWorkspace();

	String getSpaceId();

	SearchObjectType getType();

	long getDate();

	// enum Type {
	//
	// TASKNOTE("TASK"),
	//
	// MESSAGE("MESSAGE"),
	//
	// CONTACT("CONTACT"),
	//
	// FILE("FILE/FOLDER"),
	//
	// NOTE("NOTE"),
	//
	// TOPIC("TOPIC"),
	//
	// CALANDER_EVENT("EVENT");
	//
	// private String text;
	//
	// Type(String type) {
	// this.text = type;
	// }
	//
	// public String getText() {
	// return text;
	// }
	// };

}
