package com.vimukti.accounter.android;

import com.vimukti.accounter.android.result.Result;

public interface ConnectionListener {

	void messageSent(String msg);

	void connectionEstablished();

	void messageReceived(Result result);

	void connectionClosed(boolean fromRemote);

	void connecting();

}
