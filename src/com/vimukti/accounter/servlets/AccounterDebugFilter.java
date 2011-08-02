/**
 * 
 */
package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterDebugFilter implements Filter {

	private static final String DEBUG_URL = "?gwt.codesvr=127.0.0.1:9997";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (!ServerConfiguration.isDebugMode) {
			chain.doFilter(req, resp);
			return;
		}
		chain.doFilter(req, resp);
		HttpServletResponse httpResponse = (HttpServletResponse) resp;
		int status = httpResponse.getStatus();
		if (status == 301 || status == 302 || status == 303) {
			String url = httpResponse.getHeader("Localtion");
			if (!url.endsWith(DEBUG_URL)) {
				url = url + DEBUG_URL;
			}
			httpResponse.setHeader("Location", url);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
