package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncryptCompaniesServlet extends BaseServlet {

	private static final String ENCRYPT_VIEW = "/WEB-INF/encryptcompany.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO send messages with info attribute
		List<String> list = new ArrayList<String>();
		list.add("suresh");
		list.add("nagaraj");
		req.setAttribute("companeyList", list);
		dispatch(req, resp, ENCRYPT_VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
