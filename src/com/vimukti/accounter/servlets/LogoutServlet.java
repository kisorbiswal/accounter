package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.CometManager;

public class LogoutServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3008434821899018651L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String userid = (String) req.getSession().getAttribute(EMAIL_ID);
			if (userid != null) {
				String cid = getCookie(req, COMPANY_COOKIE);
				if (cid != null) {

					Session session = HibernateUtil
							.openSession("company" + cid);
					Transaction transaction = session.beginTransaction();

					User user = (User) session.getNamedQuery("user.by.emailid")
							.setParameter("emailID", userid).uniqueResult();
					Activity activity = new Activity(user, ActivityType.LOGOUT);

					session.save(activity);
					transaction.commit();
					session.close();
					long id = Long.parseLong(cid);
					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), id,
							userid);
				}
				deleteCookie(req, resp);
				req.getSession().setAttribute(USER_ID, null);
			}
			// resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			// resp.setHeader("Location", "/login");
		} catch (Exception e) {
			e.printStackTrace();
		}

		req.getSession().invalidate();

		redirectExternal(req, resp, LOGIN_URL);
	}

	private void deleteCookie(HttpServletRequest request,
			HttpServletResponse response) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().endsWith(OUR_COOKIE)
					|| cookie.getName().equals(COMPANY_COOKIE)) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req, response);
	}
}
