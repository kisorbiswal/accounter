package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TranslateService extends RemoteService {

	// ArrayList<Status> getStatus();

	// ClientMessage getNext(String lang, int lastMessageId);

	ClientLocalMessage addTranslation(long id, String lang, String value);

	boolean vote(long localMessageId);

	ArrayList<ClientMessage> getMessages(String lang, int status, int from,
			int to);

	boolean setApprove(long localMessageId, boolean isApprove);

	List<ClientLanguage> getLanguages();

	ClientLanguage getLocalLanguage();

	public boolean validateUserValue(ClientMessage clientMessage, String data);
	
	public Set<String> getServerMatchList();

	boolean canApprove(String lang);
}
