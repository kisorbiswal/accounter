package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.License;
import com.vimukti.accounter.license.LicenseManager;
import com.vimukti.accounter.utils.HibernateUtil;

public class ManageLicenseServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String view = "/WEB-INF/LicenseManagement.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}

		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String parameter = req.getParameter("gen");
		if (parameter != null) {
			req.setAttribute("gen", parameter);
		}

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLicensesOf").setParameter(
				"emailId", emailId);
		List<License> licenses = (List<License>) query.list();

		req.setAttribute("licenses", licenses);

		req.setAttribute("fullName", client.getFullName());

		dispatch(req, resp, view);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}

		String organisationName = req.getParameter("orgName");
		String serverID = req.getParameter("serverID");

		License license = new License();
		license.setClient(getClient(emailId));
		license.setServerId(serverID);
		license.setOrganisation(organisationName);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, 1);
		license.setExpiresOn(instance.getTime());

		LicenseManager licenseManager = new LicenseManager();
		license.setLicenseText(licenseManager.generateLicense(license));

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(license);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

		doGet(req, resp);

	}
}
