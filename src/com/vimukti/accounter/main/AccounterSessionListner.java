package com.vimukti.accounter.main;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.web.server.CometManager;

public class AccounterSessionListner implements HttpSessionListener {
	protected Logger log = Logger.getLogger(AccounterSessionListner.class);

	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession httpSession = sessionEvent.getSession();
		System.out.println("session for corresponding id "
				+ sessionEvent.getSession().getId());
		httpSession.setMaxInactiveInterval(60);

	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// This method will be called just before session is to be destroyed
		log.info("Session expired");

		HttpSession httpSession = sessionEvent.getSession();
		Long companyId = (Long) httpSession
				.getAttribute(BaseServlet.COMPANY_ID);
		String userID = (String) httpSession.getAttribute("userID");
		if (userID != null) {
			log.info("Session expired, Comet destroyed");
			// session time out
			CometManager.destroyStream(httpSession.getId(), companyId, userID);
		}
	}

}