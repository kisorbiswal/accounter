package com.vimukti.accounter.developer.api.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gdevelop.gwt.syncrpc.CookieManager;
import com.gdevelop.gwt.syncrpc.SessionManager;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.developer.api.ApiSerializationFactory;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public abstract class ApiProcessor {
	public static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";
	private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	private ApiResult result = new ApiResult();

	public abstract void process(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	@SuppressWarnings("unchecked")
	protected <T> T getS2sSyncProxy(final HttpServletRequest req, String uri,
			Class<?> clazz) {
		String url = "http://" + ServerConfiguration.getMainServerDomain()
				+ ":" + ServerConfiguration.getMainServerPort() + uri;
		return (T) SyncProxy.newProxyInstance(clazz, url, "",
				new SessionManager() {
					private CookieManager cookieManager = new CookieManager();

					@Override
					public HttpURLConnection openConnection(URL url)
							throws Exception {
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.addRequestProperty("isAPI", "Yes");
						connection.addRequestProperty("companyId", req
								.getAttribute("companyId").toString());
						connection.addRequestProperty("emailId", req
								.getAttribute("emailId").toString());
						return connection;
					}

					@Override
					public void handleResponseHeaders(
							HttpURLConnection connection) throws IOException {
						cookieManager.storeCookies(connection);
					}

					@Override
					public com.gdevelop.gwt.syncrpc.CookieManager getCookieManager() {
						return null;
					}
				});
	}

	protected ApiSerializationFactory getSerializationFactory(
			HttpServletRequest req) throws ServletException {
		String string = getNameFromReq(req, 2);
		if (string.equals("xml")) {
			return new ApiSerializationFactory(false);
		} else if (string.equals("json")) {
			return new ApiSerializationFactory(true);
		}
		throw new ServletException("Wrong Sream Formate");
	}

	protected ClientFinanceDate getClientFinanceDate(String string) {
		if (string == null) {
			return null;
		}
		try {
			return new ClientFinanceDate(format.parse(string));
		} catch (ParseException e) {
			return null;
		}
	}

	protected String getNameFromReq(HttpServletRequest req, int indexFromLast) {
		String url = req.getRequestURI();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - indexFromLast];
		return last;
	}

	protected void setStatus(int status) {
		result.setStatus(status);
	}

	protected void sendResult(Object object) {
		result.setStatus(ApiResult.SUCCESS);
		result.setResult(object);
	}

	protected void sendFail(String errorMessage) {
		result.setStatus(ApiResult.FAIL);
		result.setResult(errorMessage);
	}

	public void sendData(HttpServletRequest req, HttpServletResponse resp) {
		updateDeveloper(req);
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

	private void updateDeveloper(HttpServletRequest req) {
		Developer developer = null;
		long id = ((Long) req.getAttribute("id")).longValue();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			developer = (Developer) session.get(Developer.class, id);
			if (result.getStatus() == ApiResult.SUCCESS) {
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
}
