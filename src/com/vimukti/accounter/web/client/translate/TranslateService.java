package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TranslateService extends RemoteService {
	ArrayList<Status> getStatus();

	// ClientMessage getNext(String lang, int lastMessageId);

	boolean addTranslation(int id, String lang, String value);

	boolean vote(int localMessageId, boolean up);

	ArrayList<ClientMessage> getMessages(String lang, int status);
}
