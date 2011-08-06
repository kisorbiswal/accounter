/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface ISaveCallback {

	public void saveFailed(Throwable exception);

	public void saveSuccess(IAccounterCore object);

}
