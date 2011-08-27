package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vimukti.accounter.web.server.CometManager;

public class OpenCompanyServlet extends BaseServlet {

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
		String emailID = (String) request.getSession().getAttribute(EMAIL_ID);
		RequestDispatcher dispatcher;
		if (emailID != null) {
			String serverCompanyID = getCookie(request, COMPANY_COOKIE);
			initComet(request.getSession(), Long.parseLong(serverCompanyID),
					emailID);
			// there is no session, so do external redirect to login page
			// response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			// response.setHeader("Location", "/Accounter.jsp");
			dispatcher = getServletContext().getRequestDispatcher(
					"/WEB-INF/Accounter.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect("/login");
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
	private void initComet(HttpSession httpSession, long companyID,
			String emailID) {
		// Stream must be created otherwise user will get data
		// Continuously and browser will struck
		CometSession cometSession = CometServlet.getCometSession(httpSession);
		CometManager.initStream(httpSession.getId(), companyID, emailID,
				cometSession);
	}

}
