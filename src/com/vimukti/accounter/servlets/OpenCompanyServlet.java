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
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.CometManager;

public class OpenCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory.getLog(LoginServlet.class);
	private static final String REDIRECT_PAGE = "/WEB-INF/Redirect.jsp";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String url = request.getRequestURI().toString();
		if (url.equals(ACCOUNTER_OLD_URL)) {
			dispatch(request, response, REDIRECT_PAGE);
			return;
		}
		String emailID = (String) request.getSession().getAttribute(EMAIL_ID);

		if (emailID != null) {
			String serverCompanyID = getCookie(request, COMPANY_COOKIE);
			if (serverCompanyID == null || serverCompanyID.equals("")) {
				response.sendRedirect(COMPANIES_URL);
				return;
			}
			initComet(request.getSession(), Long.parseLong(serverCompanyID),
					emailID);

			Session session = HibernateUtil.openSession();
			Transaction transaction = session.beginTransaction();

			User user = (User) session.getNamedQuery("user.by.emailid")
					.setParameter("emailID", emailID).uniqueResult();
			Activity activity = new Activity(user.getCompany(), user,
					ActivityType.LOGIN);
			session.save(activity);
			transaction.commit();
			session.close();
			// there is no session, so do external redirect to login page
			// response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			// response.setHeader("Location", "/Accounter.jsp");
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/WEB-INF/Accounter.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect(LOGIN_URL);
			// Session is there, so show the main page

		}
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
