package com.vimukti.accounter.translate.server;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.translate.client.ClientMessage;
import com.vimukti.accounter.translate.client.Status;
import com.vimukti.accounter.translate.client.TranslateServiceAsync;

public class TranslateServiceImpl implements TranslateServiceAsync {

	@Override
	public void getStatus(AsyncCallback<ArrayList<Status>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNext(String lang, AsyncCallback<ClientMessage> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTranslation(int id, String lang, String value,
			AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vote(int localMessageId, boolean up,
			AsyncCallback<Boolean> callback) {
		// TODO Auto-generated method stub
		
	}

}
