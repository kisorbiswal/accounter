package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

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

		if (userName.equals("admin@accounterlive.com")
				&& password.equals("***REMOVED***")) {

			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
			List<Company> companies = new ArrayList<Company>();
			try {
				companies = session.getNamedQuery("get.all.companies").list();
			} finally {
				session.close();
			}

			for (Company company : companies) {
				Session s = HibernateUtil.openSession(company.getCompanyID());
				try {
					User user = (User) s.getNamedQuery("get.admin.users")
							.list().get(0);
					logger.info("*************** Updating "
							+ company.getCompanyID() + " with admin ID: "
							+ user.getEmail() + " **************");
				} finally {
					s.close();
				}
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
