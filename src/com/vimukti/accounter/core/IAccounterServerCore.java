package com.vimukti.accounter.core;

import java.io.Serializable;

import com.vimukti.accounter.web.client.core.IVersionable;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * A Interface Implemented by All Accounter Server Core classes,
 * 
 * @author Fernandez
 * 
 */

public interface IAccounterServerCore extends Serializable, Cloneable,
		IVersionable {

	long getID();

	boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException;

}
