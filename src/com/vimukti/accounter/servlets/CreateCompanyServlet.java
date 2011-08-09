package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mortbay.util.UrlEncoded;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.HibernateUtil;

public class CreateCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/CreateCompany.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			request.setAttribute("errormessage",
					"Company creation failed because of Invalid session.");
			dispatch(request, response, view);
			return;
		}
		String status = (String) session.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			response.sendRedirect("/companystatus");
			return;
		}
		doCreateCompany(request, response, emailID);

	}

	private void doCreateCompany(HttpServletRequest request,
			HttpServletResponse response, String emailID) throws IOException {
		String companyName = request.getParameter("name");
		String companyTypeStr = request.getParameter("companyType");
		if (!isValidCompanyName(companyName)) {
			request.setAttribute("errormessage",
					"Invalid Company Name. Name Should be grater than 5 characters");
			dispatch(request, response, view);
			return;
		}
		int companyType = getCompanyType(companyTypeStr);
		if (companyType < 0) {
			request.setAttribute("errormessage", "Invalid Company Type.");
			dispatch(request, response, view);
			return;
		}
		ServerCompany serverCompany = null;
		Client client = null;
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = session.beginTransaction();
		try {
			serverCompany = getServerCompany(companyName);
			if (serverCompany != null) {
				request.setAttribute("errormessage",
						"Company exist with the same name.");
				dispatch(request, response, view);
				return;
			}
			client = getClient(emailID);
			if (client == null) {
				request.setAttribute("errormessage",
						"Your session was expired. please login again.");
				dispatch(request, response, view);
				return;
			}

			serverCompany = new ServerCompany();
			serverCompany.getClients().add(client);
			serverCompany.setCompanyName(companyName);
			serverCompany.setCompanyType(companyType);
			serverCompany.setConfigured(false);
			serverCompany.setCreatedDate(new Date());
			serverCompany.setServerAddress("");// TODO
			session.saveOrUpdate(serverCompany);

			client.getCompanies().add(serverCompany);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}

		final ServerCompany rollBackServerCompany = serverCompany;
		// sdfsd
		final Client rollBackClient = client;
		final String urlString = getUrlString(serverCompany, emailID, client);
		final HttpSession httpSession = request.getSession(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					httpSession.setAttribute(COMPANY_CREATION_STATUS,
							"Creating");
					URL url = new URL(urlString.toString());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						httpSession.setAttribute(COMPANY_CREATION_STATUS,
								"Success");
					} else {
						rollback(rollBackServerCompany, rollBackClient);
						httpSession.setAttribute(COMPANY_CREATION_STATUS,
								"Fail");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
		response.sendRedirect("/companystatus");
		return;
	}

	private ServerCompany getServerCompany(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getServerCompany.by.name")
				.setParameter("name", companyName);
		return (ServerCompany) query.uniqueResult();
	}

	/**
	 * @param serverCompany
	 * @param client
	 */
	private void rollback(ServerCompany serverCompany, Client client) {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		try {
			client.getCompanies().remove(serverCompany);
			session.save(client);
			session.delete(serverCompany);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * @return
	 */
	private String getUrlString(ServerCompany serverCompany, String emailID,
			Client client) {
		StringBuffer buffer = new StringBuffer(
				"http://localhost:8890/initialzeCompany?");

		buffer.append(PARAM_SERVER_COMPANY_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(String.valueOf(serverCompany.getID()))
				.encode());

		buffer.append('&');
		buffer.append(PARA_COMPANY_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(serverCompany.getCompanyName()).encode());

		buffer.append('&');
		buffer.append(PARAM_COMPANY_TYPE);
		buffer.append('=');
		buffer.append(new UrlEncoded(String.valueOf(serverCompany
				.getCompanyType())).encode());

		buffer.append('&');
		buffer.append(EMAIL_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(emailID).encode());

		buffer.append('&');
		buffer.append(PARAM_FIRST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(client.getFirstName()).encode());

		buffer.append('&');
		buffer.append(PARAM_LAST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded((client.getLastName())).encode());

		return buffer.toString();

	}

	private int getCompanyType(String companyType) {
		if (companyType != null) {
			int type = Integer.parseInt(companyType);
			if (type == Company.ACCOUNTING_TYPE_UK
					|| type == Company.ACCOUNTING_TYPE_US) {
				return type;
			}
		}
		return -1;
	}

	private boolean isValidCompanyName(String companyId) {
		return companyId.matches("^[a-z][a-z0-9]{5,}$");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null) {
			String status = (String) session
					.getAttribute(COMPANY_CREATION_STATUS);
			if (status != null) {
				response.sendRedirect("/companystatus");
				return;
			}
		}
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		dispatch(request, response, view);
	}
}
