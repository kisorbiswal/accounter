package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
					long id = Long.parseLong(cid);
					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), id,
							userid);
				}
				deleteCookie(req, resp);
				req.getSession().setAttribute(USER_ID, null);
			}
			req.getSession().invalidate();

			redirectExternal(req, resp, LOGIN_URL);

			// resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			// resp.setHeader("Location", "/login");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
