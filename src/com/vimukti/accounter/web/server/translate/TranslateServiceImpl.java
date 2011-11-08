package com.vimukti.accounter.web.server.translate;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.translate.Status;
import com.vimukti.accounter.web.client.translate.TranslateService;
import com.vimukti.accounter.web.server.FinanceTool;

public class TranslateServiceImpl extends RemoteServiceServlet implements
		TranslateService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String EMAIL_ID = "emailId";

	@Override
	public ArrayList<Status> getStatus() {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return null;
		}
		return new FinanceTool().getTranslationStatus();
	}

	@Override
	public ClientMessage getNext(String lang, int lastMessageId) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return null;
		}

		try {
			return new FinanceTool().getNextMessage(lang, lastMessageId);
		} catch (AccounterException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addTranslation(int id, String lang, String value) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return false;
		}
		return new FinanceTool().addTranslation(userEmail, id, lang, value);
	}

	@Override
	public boolean vote(int localMessageId, boolean up) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return false;
		}
		return new FinanceTool().addVote(localMessageId, up, userEmail);
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				EMAIL_ID);
	}

	@Override
	public ClientMessage getMessage(String lang, int messageId) {
		String userEmail = getUserEmail();
		if (userEmail == null) {
			return null;
		}
		return new FinanceTool().getMessage(messageId, lang);
	}
}
