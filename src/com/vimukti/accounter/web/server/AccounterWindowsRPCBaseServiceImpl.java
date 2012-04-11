package com.vimukti.accounter.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.vimukti.accounter.utils.HibernateUtil;

public class AccounterWindowsRPCBaseServiceImpl extends RemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterWindowsRPCBaseServiceImpl() {
		super();
	}

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Session session = HibernateUtil.openSession();
			try {
				super.service(request, response);
			} catch (Exception e) {
			} finally {
				session.close();
			}
		} catch (Exception e) {
		}

	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		moduleBaseURL = moduleBaseURL.replace("ms-wwa://", "http://");
		return super.doGetSerializationPolicy(request, moduleBaseURL,
				strongName);
	}

}
