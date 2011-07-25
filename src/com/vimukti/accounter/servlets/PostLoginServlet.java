package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vimukti.comet.server.CometManager;

public class PostLoginServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory.getLog(LoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String referer = request.getHeader("Referer");
		String url = request.getRequestURL().toString();
		if (url.equals(referer)) {
			return;
		}
		String userID = (String) request.getSession().getAttribute(USER_ID);
		RequestDispatcher dispatcher;
		if (userID != null) {
			initComet(request.getSession().getId(), userID);
			// there is no session, so do external redirect to login page
			// response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			// response.setHeader("Location", "/Accounter.jsp");
			dispatcher = getServletContext().getRequestDispatcher(
					"/Accounter.jsp");
			dispatcher.forward(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", "/site/login");
			// Session is there, so show the main page

		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// No request is supposed to come here

	}

	/**
	 * Initialising comet stuff
	 * 
	 * @param request
	 * @param identity
	 */
	private void initComet(String sessionID, String identityID) {
		// Stream must be created otherwise user will get data
		// Continuously and browser will struck
		CometManager.initStream(sessionID, identityID, "bizantra");

	}

}
