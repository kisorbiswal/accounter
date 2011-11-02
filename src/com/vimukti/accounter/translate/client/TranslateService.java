package com.vimukti.accounter.translate.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TranslateService extends RemoteService {
	ArrayList<Status> getStatus();
	
	ClientMessage getNext(String lang);
	
	boolean addTranslation(int id,String lang, String value);
	
	boolean vote(int localMessageId, boolean up);
}
