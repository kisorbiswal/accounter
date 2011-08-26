/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Implemented by All Client Core Class
 * 
 * @author Fernandez
 * 
 * 
 */
public interface IAccounterCore extends IsSerializable, Serializable, Cloneable,IVersionable {

	String getName();

	// Display name as in Combo
	String getDisplayName();

	AccounterCoreType getObjectType();

	void setID(long id);

	long getID();

	String getClientClassSimpleName();

}
