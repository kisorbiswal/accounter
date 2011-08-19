package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface IAccounterWidget {

	public void saveFailed(AccounterException exception);

	public void saveSuccess(IAccounterCore object);

	public void deleteFailed(AccounterException caught);

	public void deleteSuccess(IAccounterCore result);

	// public long getID();
	//
	// public void setID(long id);

	public void processupdateView(IAccounterCore core, int command);
}
