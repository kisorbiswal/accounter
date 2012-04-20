package com.vimukti.accounter.developer.api.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdevelop.gwt.syncrpc.CookieManager;
import com.gdevelop.gwt.syncrpc.SessionManager;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.main.ServerConfiguration;
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
						connection.addRequestProperty("ApiKey",
								req.getParameter("ApiKey"));
						connection.addRequestProperty("authentication",
								req.getParameter("authentication"));
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

	protected void setStatus(int status) {
		result.setStatus(status);
	}

	protected void sendResult(Object object) {
		result.setStatus(ApiResult.SUCCESS);
		result.setResult(object);
	}

	public ApiResult getResult() {
		return result;
	}

	protected void sendFail(String errorMessage) {
		result.setStatus(ApiResult.FAIL);
		result.setResult(errorMessage);
	}
}
