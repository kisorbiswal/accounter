package com.vimukti.accounter;

import com.vimukti.accounter.result.Result;

public interface ConnectionListener {

	void messageSent(String msg);

	void connectionEstablished();

	void messageReceived(Result result);

	void connectionClosed(boolean fromRemote);

	void connecting();

}
