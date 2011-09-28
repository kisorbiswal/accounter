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
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
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
		String userid = (String) req.getSession().getAttribute(EMAIL_ID);
		if (userid != null) {
			String cid = getCookie(req, COMPANY_COOKIE);
			if (cid != null) {
				try {
					updateActivity(userid, cid);

					long id = Long.parseLong(cid);
					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), id,
							userid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			req.getSession().removeAttribute(EMAIL_ID);
		}
		// resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		// resp.setHeader("Location", "/login");

		req.getSession().invalidate();
		deleteCookie(req, resp);
		redirectExternal(req, resp, LOGIN_URL);
	}

	/**
	 * @param cid
	 */
	private void updateActivity(String userid, String cid) {
		Session session = HibernateUtil.openSession();
		Company company = (Company) session.get(Company.class,
				Long.parseLong(cid));
		Transaction transaction = session.beginTransaction();
		try {
			User user = (User) session.getNamedQuery("user.by.emailid")
					.setParameter("emailID", userid)
					.setEntity("company", company).uniqueResult();
			Activity activity = new Activity(user.getCompany(), user,
					ActivityType.LOGOUT);
			session.save(activity);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			session.close();
		}
	}

	private void deleteCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String userKey = getCookie(request, OUR_COOKIE);
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().endsWith(OUR_COOKIE)
					|| cookie.getName().equals(COMPANY_COOKIE)) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setDomain(ServerConfiguration.getServerCookieDomain());
				response.addCookie(cookie);
			}
		}

		if (userKey == null || userKey.isEmpty()) {
			return;
		}
		// Deleting RememberMEKEy from Database
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.getNamedQuery("delete.remembermeKeys")
					.setParameter("key", userKey).executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req, response);
	}
}
