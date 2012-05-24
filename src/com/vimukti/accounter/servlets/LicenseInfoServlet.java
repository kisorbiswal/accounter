package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.License;
import com.vimukti.accounter.license.LicenseManager;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class LicenseInfoServlet extends BaseServlet {
	public static final String PAGE = "/WEB-INF/licenseInfo.jsp";
	private License license;
	private String errorMsg;
	AccounterMessages messages = Global.get().messages();
	AccounterMessages2 messages2 = Global.get().messages2();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = (String) req.getAttribute(EMAIL_ID);
		Session session = HibernateUtil.getCurrentSession();
		license = (License) session.get(License.class, email);
		req.setAttribute("license", license);
		dispatch(req, resp, PAGE);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String licenseKey = (String) req.getAttribute("licensekey");
		if (licenseKey == null || licenseKey.isEmpty()) {
			errorMsg = messages.pleaseEnter(messages2.licenseKey());
			req.setAttribute("errorMsg", errorMsg);
			dispatch(req, resp, PAGE);
			return;
		}
		LicenseManager licenseManager = new LicenseManager();
	}
}
