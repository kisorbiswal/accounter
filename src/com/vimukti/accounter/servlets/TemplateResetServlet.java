package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.bizantra.server.internal.core.BizantraCompany;
import com.bizantra.server.internal.core.CollaberIdentity;
import com.bizantra.server.main.Server;
import com.bizantra.server.storage.HibernateUtil;

public class TemplateResetServlet extends HttpServlet {

	/**
	 * 
	 */
	protected static final Log logger = LogFactory.getLog(LoginServlet.class);

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		if (userName.equals("admin@bizantra.com") && password.equals("***REMOVED***")) {

			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

			List<BizantraCompany> companies = session.getNamedQuery(
					"get.all.companies").list();
			session.close();

			for (BizantraCompany company : companies) {
				Session s = HibernateUtil.openSession(company
						.getCompanyDomainName());

				CollaberIdentity identity = (CollaberIdentity) s.getNamedQuery(
						"get.super.users").list().get(0);
				logger.info("*************** Updating "
						+ company.getCompanyDomainName() + " with admin ID: "
						+ identity.getEmailAddress() + " **************");

				s.close();

			}

			sendResponse("Update successfull.", request, response);

		} else {
			sendResponse("User not authenticated.", request, response);
		}
	}

	private void sendResponse(String msg, HttpServletRequest request,
			HttpServletResponse response) {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/tempateReset.jsp");
		request.setAttribute("status", msg);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
