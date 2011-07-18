package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2062165649089031647L;

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher requestDispatcher = null;
		requestDispatcher = request.getRequestDispatcher("/liveLogin");
		requestDispatcher.forward(request, response);
	}

}
