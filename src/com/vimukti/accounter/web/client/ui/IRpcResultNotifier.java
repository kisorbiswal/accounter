/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IRpcResultNotifier {

	public void onSaveSuccess(IAccounterCore codeObj);

	public void onSaveFailure(AccounterException exception);

	public void onDeleteSuccess(boolean delete);

	public void onDeleteFailure(AccounterException exception);

}
