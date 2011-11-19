package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ClientLocalMessage;

public interface TranslateServiceAsync {

	// void getStatus(AsyncCallback<ArrayList<Status>> callback);

	// void getNext(String lang, int lastMessageId,
	// AsyncCallback<ClientMessage> callback);

	void addTranslation(long id, String lang, String value,
			AsyncCallback<ClientLocalMessage> callback);

	void vote(long localMessageId, AsyncCallback<Boolean> callback);

	void getMessages(String lang, int status, int from, int to,
			AsyncCallback<ArrayList<ClientMessage>> callback);

	public void setApprove(long localMessageId, boolean isApprove,
			AsyncCallback<Boolean> callback);

	public void getLanguages(AsyncCallback<List<ClientLanguage>> callback);

	public void getLocalLanguage(AsyncCallback<ClientLanguage> callback);

	void validateUserValue(ClientMessage clientMessage, String data,
			AsyncCallback<Boolean> callback);

	void getServerMatchList(AsyncCallback<Set<String>> callback);

	public void canApprove(String lang, AsyncCallback<Boolean> callback);

}
