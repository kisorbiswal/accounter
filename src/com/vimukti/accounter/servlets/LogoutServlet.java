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
import com.vimukti.accounter.core.EU;
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
		Boolean isSupportedUser = (Boolean) req.getSession().getAttribute(
				IS_SUPPORTED_USER);
		Long cid = (Long) req.getSession().getAttribute(COMPANY_ID);
		if (userid != null) {
			if (cid != null) {
				try {
					if (!isSupportedUser) {
						updateActivity(userid, cid);
					}

					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), cid,
							userid);
					EU.removeCipher();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// if is Support user then put his email Id back
			if (isSupportedUser) {
				req.getSession().setAttribute(EMAIL_ID,
						req.getSession().getAttribute(SUPPORTED_EMAIL_ID));
			} else {
				req.getSession().removeAttribute(EMAIL_ID);
			}
			req.getSession().removeAttribute(USER_ID);
		}
		// Support user and OpendCompany
		if (isSupportedUser != null && isSupportedUser && cid != null) {
			redirectExternal(req, resp, COMPANIES_URL);
			req.getSession().removeAttribute(COMPANY_ID);
		} else {
			req.getSession().invalidate();
			deleteCookie(req, resp);
			redirectExternal(req, resp, LOGIN_URL);
		}
	}

	/**
	 * @param cid
	 */
	private void updateActivity(String userid, Long cid) {
		Session session = HibernateUtil.openSession();
		Company company = (Company) session.get(Company.class, cid);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.getNamedQuery("user.by.emailid")
					.setParameter("emailID", userid)
					.setEntity("company", company).uniqueResult();
			Activity activity = new Activity(user.getCompany(), user,
					ActivityType.LOGOUT);
			session.save(activity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}
	}

	private void deleteCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String userKey = getCookie(request, OUR_COOKIE);
		removeCookie(request, response, OUR_COOKIE);

		if (userKey == null || userKey.isEmpty()) {
			return;
		}
		// Deleting RememberMEKEy from Database
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.getNamedQuery("delete.remembermeKeys")
					.setParameter("key", userKey).executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}

	}

	private void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String ourCookie) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().endsWith(OUR_COOKIE)) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setDomain(ServerConfiguration.getServerCookieDomain());
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
