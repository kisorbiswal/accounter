package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TranslateServiceAsync {

	// void getStatus(AsyncCallback<ArrayList<Status>> callback);

	// void getNext(String lang, int lastMessageId,
	// AsyncCallback<ClientMessage> callback);

	void addTranslation(long id, String lang, String value,
			AsyncCallback<Boolean> callback);

	void vote(long localMessageId, AsyncCallback<Boolean> callback);

	void getMessages(String lang, int status, int from, int to,
			AsyncCallback<ArrayList<ClientMessage>> callback);

	public void setApprove(long localMessageId, boolean isApprove,
			AsyncCallback<Boolean> callback);

	public void getLanguages(AsyncCallback<List<ClientLanguage>> callback);

	public void canApprove(String lang, AsyncCallback<Boolean> callback);

}
