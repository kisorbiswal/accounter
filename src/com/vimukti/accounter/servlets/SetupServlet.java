package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.utils.HibernateUtil;

public class SetupServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			dispatchMessage("Session was expired", req, resp,
					"companieslist.jsp");
			return;
		}
		String emailId = (String) session.getAttribute("emailId");
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailId);
			if (client == null) {
				dispatchMessage("User is not exists", req, resp,
						"companieslist.jsp");
				return;
			}

			Set<ServerCompany> companies = client.getCompanies();
			if (companies.isEmpty()) {
				openCompaneySetup(client, req, resp);
			} else {
				int size = companies.size();
				if (size == 1) {
					Iterator<ServerCompany> iterator = companies.iterator();
					if (iterator.hasNext()) {
						ServerCompany next = iterator.next();
						openAcounterApplication(client, next.getCompanyName());
					} else {
						openCompaneySetup(client, req, resp);
					}
				} else {
					List<String> companyList = new ArrayList<String>();
					for (ServerCompany company : companies) {
						companyList.add(company.getCompanyName());
					}
					req.setAttribute("companeyList", companyList);
					resp.sendRedirect("companieslist.jsp");
				}
			}
		} catch (Exception e) {
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			dispatchMessage("Session was expired", req, resp,
					"companieslist.jsp");
			return;
		}
		String emailId = (String) session.getAttribute("emailId");
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailId);
			if (client == null) {
				dispatchMessage("User is not exists", req, resp, "setup.jsp");
				return;
			}
			// TODO
			openAcounterApplication(client, "");
		} catch (Exception e) {
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}
	}

	private void openCompaneySetup(Client client, HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	private void openAcounterApplication(Client client, String string) {
		// TODO Auto-generated method stub

	}

}
