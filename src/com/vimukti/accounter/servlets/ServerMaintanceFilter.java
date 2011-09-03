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
public class ServerMaintanceFilter implements Filter {

	private static final String MAINTANACE_VIEW = "/WEB-INF/maintananceInfo.jsp";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (ServerConfiguration.isUnderMaintainance()) {
			HttpServletRequest req = (HttpServletRequest) arg0;
			req.getRequestDispatcher(MAINTANACE_VIEW).forward(req,
					(HttpServletResponse) arg1);
		}

		arg2.doFilter(arg0, arg1);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
