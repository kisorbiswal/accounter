package com.vimukti.accounter.main;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.vimukti.accounter.core.FinanceLogger;
import com.vimukti.comet.server.CometManager;

public class AccounterSessionListner implements HttpSessionListener

{
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession httpSession = sessionEvent.getSession();
		System.out.println("session for corresponding id "
				+ sessionEvent.getSession().getID());
		httpSession.setMaxInactiveInterval(120);

	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// This method will be called just before session is to be destroyed

		HttpSession httpSession = sessionEvent.getSession();
		String userID = (String) httpSession.getAttribute("userID");
		if (userID != null) {
			// session time out
			FinanceLogger.log("Session expired : " + userID);
			CometManager.destroyStream(httpSession.getID(), userID);
		}
	}

}
