package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TranslateService extends RemoteService {
	// ArrayList<Status> getStatus();

	// ClientMessage getNext(String lang, int lastMessageId);

	boolean addTranslation(long id, String lang, String value);

	boolean vote(long localMessageId, boolean up);

	ArrayList<ClientMessage> getMessages(String lang, int status, int from,
			int to);

	boolean setApprove(long localMessageId, boolean isApprove);
}
