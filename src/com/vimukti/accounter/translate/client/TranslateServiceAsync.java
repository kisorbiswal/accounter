package com.vimukti.accounter.translate.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TranslateServiceAsync {

	void getStatus(AsyncCallback<ArrayList<Status>> callback);

	void getNext(String lang, AsyncCallback<ClientMessage> callback);

	void addTranslation(int id, String lang, String value,
			AsyncCallback<Boolean> callback);

	void vote(int localMessageId, boolean up, AsyncCallback<Boolean> callback);

}
