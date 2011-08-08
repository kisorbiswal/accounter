/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IDeleteCallback {

	public void deleteFailed(AccounterException caught);

	public void deleteSuccess(Boolean result);
}
