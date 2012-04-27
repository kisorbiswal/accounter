package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.developer.api.core.ApiProcessor;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.developer.api.process.CreateProcessor;
import com.vimukti.accounter.developer.api.process.CreateUserSecretProcessor;
import com.vimukti.accounter.developer.api.process.DeleteProcessor;
import com.vimukti.accounter.developer.api.process.EncryptCompanyProcessor;
import com.vimukti.accounter.developer.api.process.ReadProcessor;
import com.vimukti.accounter.developer.api.process.ReportsListProcessor;
import com.vimukti.accounter.developer.api.process.UpdateProcessor;
import com.vimukti.accounter.developer.api.process.lists.AccountsProcessor;
import com.vimukti.accounter.developer.api.process.lists.CurrenciesProcessor;
import com.vimukti.accounter.developer.api.process.lists.CustomersProcessor;
import com.vimukti.accounter.developer.api.process.lists.InvoicesProcessor;
import com.vimukti.accounter.developer.api.process.lists.ItemsProcessor;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;

public class ApiBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<String, ApiProcessor> processors = new HashMap<String, ApiProcessor>();

	@Override
	public void init() throws ServletException {

		// Operations
		processors.put("usersecret", new CreateUserSecretProcessor());
		processors.put("encryptcompany", new EncryptCompanyProcessor());
		processors.put("reportslist", new ReportsListProcessor());

		// CRUD
		processors.put("create", new CreateProcessor());
		processors.put("read", new ReadProcessor());
		processors.put("update", new UpdateProcessor());
		processors.put("delete", new DeleteProcessor());

		// Lists
		processors.put("Customer", new CustomersProcessor());
		processors.put("Account", new AccountsProcessor());
		processors.put("Invoice", new InvoicesProcessor());
		processors.put("Item", new ItemsProcessor());
		processors.put("Currency", new CurrenciesProcessor());

		// Reports
		processors.put("salesbycustomersummary", null);
		super.init();
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp,
			String type) throws IOException {
		ApiProcessor processor = processors.get(type);
		if (processor == null) {
			sendFail(req, resp, "Wrong request type.");
			return;
		}
		Session session = HibernateUtil.openSession();
		try {

			String authentication = req.getParameter("authentication");
			if (authentication == null || !isAuthenticated(authentication)) {
				sendFail(req, resp,
						"authentication fail. 'authentication' is not present");
				return;
			}
			User user = getUser((String) req.getAttribute("emailId"),
					(Long) req.getAttribute("companyId"));
			byte[] d2 = Base64.decode(authentication);
			if (user != null && user.getSecretKey() != null) {
				EU.createCipher(user.getSecretKey(), d2,
						req.getParameter("ApiKey"));
			}
			processor.beforeProcess(req, resp);
			processor.process(req, resp);
			sendData(req, resp, processor.getResult());
			EU.removeCipher();
		} catch (AccounterException e) {
			String msg = AccounterExceptions.getErrorString(e.getErrorCode());
			if (msg == null) {
				msg = "";
			}
			String m = e.getMessage();
			if (m != null) {
				if (!msg.isEmpty()) {
					msg += ", ";
				}
				msg += m;
			}
			sendFail(req, resp, msg);
		} catch (Exception e) {
			sendFail(req, resp, e.getMessage());
			return;
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static User getUser(String emailId, Long serverCompanyID) {
		if (serverCompanyID == null) {
			return null;
		}
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session
				.getNamedQuery("getUser.by.mailId.and.companyId");
		namedQuery.setParameter("emailId", emailId).setParameter("companyId",
				serverCompanyID);
		User user = (User) namedQuery.uniqueResult();
		return user;
	}

	private boolean isAuthenticated(String authentication) {

		return true;
	}

	protected void sendFail(HttpServletRequest req, HttpServletResponse resp,
			String result) {
		try {
			ApiResult apiResult = new ApiResult();
			apiResult.setStatus(ApiResult.FAIL);
			apiResult.setResult(result);
			sendData(req, resp, apiResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(HttpServletRequest req, HttpServletResponse resp,
			ApiResult result) {
		updateDeveloper(req, result.getStatus());
		try {
			ApiSerializationFactory factory = getSerializationFactory(req);
			String string = factory.serialize(result);
			ServletOutputStream outputStream;
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateDeveloper(HttpServletRequest req, int status) {
		Developer developer = null;
		long id = ((Long) req.getAttribute("id")).longValue();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			developer = (Developer) session.get(Developer.class, id);
			if (status == ApiResult.SUCCESS) {
				developer.succeedRequests++;
			} else {
				developer.failureRequests++;
			}
			session.saveOrUpdate(developer);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public static ApiSerializationFactory getSerializationFactory(
			HttpServletRequest req) throws ServletException {
		String string = getNameFromReq(req, 2);
		if (string.equals("xml")) {
			return new ApiSerializationFactory(false);
		} else if (string.equals("json")) {
			return new ApiSerializationFactory(true);
		}
		throw new ServletException("Wrong Stream Formate");
	}

	private static String getNameFromReq(HttpServletRequest req,
			int indexFromLast) {
		String url = req.getRequestURI();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - indexFromLast];
		return last;
	}

}
