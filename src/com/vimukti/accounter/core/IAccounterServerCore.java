package com.vimukti.accounter.core;

import java.io.Serializable;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * 
 * A Interface Implemented by All Accounter Server Core classes,
 * 
 * @author Fernandez
 * 
 */

public interface IAccounterServerCore extends Serializable, Cloneable {

	long getID();

	boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException;

}
