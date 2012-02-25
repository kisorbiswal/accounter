package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.gdevelop.gwt.syncrpc.CookieManager;
import com.gdevelop.gwt.syncrpc.SessionManager;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.main.ServerConfiguration;

public class ApiBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	protected String getNameFromReq(HttpServletRequest req, int indexFromLast) {
		String url = req.getRequestURI();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - indexFromLast];
		return last;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getS2sSyncProxy(final HttpServletRequest req, String uri,
			Class<?> clazz) throws URISyntaxException {
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
}
