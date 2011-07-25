package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.mail.UsersMailSendar;

public class SupportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String view = "/sites/support.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (sendMailToSupport(request)) {
			request.setAttribute("message", "We've received your information");
		} else {
			request.setAttribute("errormessage", "please enter valid details");
		}
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}

	private boolean sendMailToSupport(HttpServletRequest request) {

		String name = request.getParameter("name");
		String emailId = request.getParameter("emailId");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		if (name == null || name.isEmpty() || emailId == null
				|| emailId.isEmpty() || subject == null || subject.isEmpty()
				|| message == null || message.isEmpty()) {
			return false;
		}

		UsersMailSendar.sendMailToSupport(name, emailId, subject, message);
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}
