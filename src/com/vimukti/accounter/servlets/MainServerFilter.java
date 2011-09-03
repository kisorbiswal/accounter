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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MainServerFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (!ServerConfiguration.getCurrentServerDomain().equals(
				ServerConfiguration.getMainServerDomain())) {
			((HttpServletResponse) arg1)
					.sendRedirect(buildMainServerURL(((HttpServletRequest) arg0)
							.getServletPath()));
		} else {
			arg2.doFilter(arg0, arg1);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	protected String buildMainServerURL(String url) {
		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(ServerConfiguration.getMainServerDomain());
		mainServerURL.append(':');
		mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}
}
