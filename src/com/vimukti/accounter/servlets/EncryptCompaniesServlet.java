package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class EncryptCompaniesServlet extends BaseServlet {

	private static final String ENCRYPT_VIEW = "/WEB-INF/encryptcompany.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}
			Session currentSession = HibernateUtil.getCurrentSession();
			List<Long> userIds = new ArrayList<Long>();
			for (User user : client.getUsers()) {
				if (!user.isDeleted()) {
					userIds.add(user.getID());
				}
			}
			if (!userIds.isEmpty()) {
				List<String> list = currentSession
						.getNamedQuery(
								"get.NonEncrypted.CompanyNames.by.client")
						.setParameterList("userIds", userIds).list();
				req.setAttribute("companeyList", list);
			}
			dispatch(req, resp, ENCRYPT_VIEW);
		} else {
			resp.sendRedirect(LOGIN_URL);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
