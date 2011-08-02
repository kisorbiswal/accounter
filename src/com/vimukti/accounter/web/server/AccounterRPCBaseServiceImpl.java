/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gwt.rpc.server.RpcServlet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.comet.server.CometManager;

/**
 * 
 * Base RPC Service.
 * 
 * @author Fernandez *
 * 
 */
public class AccounterRPCBaseServiceImpl extends RemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Logger log = Logger.getLogger(AccounterRPCBaseServiceImpl.class);

	protected static final String EMAIL_ID = "emailId";

	protected static final String COMPANY_ID = "companyId";

	public AccounterRPCBaseServiceImpl() {
		super();
		initChangeTracker();
		// financeTool = getFinanceTool();

	}

	private void initChangeTracker() {

		ChangeTracker.init();
		CometManager.initStream("accounter", IAccounterDummyService.class);

	}

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			if (isValidSession(request)) {
				String companyName = getCompanyName(request);
				Session session = HibernateUtil.openSession(companyName);
				try {
					super.service(request, response);

					if (ChangeTracker.getChanges().length > 1) {
						FinanceTool financeTool = (FinanceTool) session.load(
								FinanceTool.class, 1l);
						financeTool.putChangesInCometStream();
					}
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();

			log.error("Failed to Process Request", e);

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");

		}

	}

	/**
	 * 
	 * @return
	 */
	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				EMAIL_ID);
	}

	/**
	 * Returns the Current HttpSesstion
	 * 
	 * @return
	 */
	protected HttpSession getHttpSession() {
		return getThreadLocalRequest().getSession();
	}

	public boolean isValidSession(HttpServletRequest request) {
		return request.getSession().getAttribute(EMAIL_ID) == null ? false
				: true;
	}

	protected FinanceTool getFinanceTool() throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		FinanceTool financeTool = (FinanceTool) session.load(FinanceTool.class,
				1l);
		return financeTool;

	}

	// protected FinanceTool getFinanceTool(HttpServletRequest request) {
	// // Auto-generated method stub
	// return null;
	// }

	protected String getCompanyName(HttpServletRequest req) {
		String cookie = getCookie(req, BaseServlet.COMPANY_COOKIE);
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		ServerCompany serverCompany = (ServerCompany) session
				.getNamedQuery("getServerCompany.by.id")
				.setParameter("id", Long.parseLong(cookie)).uniqueResult();
		if (serverCompany != null) {
			serverCompany.getCompanyName();
		}
		return null;
	}

	protected String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null) {
			for (Cookie cookie : clientCookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	public ClientUser login(String string, String password, Boolean rememberMe,
			int offSet) {

		User user = null;
		if (string != null && password != null) {
			password = HexUtil.bytesToHex(Security.makeHash(string + password));
			Session session = getSession();
			Query query = session
					.getNamedQuery("getuser.from.emailid.and.password");
			query.setParameter("emailid", string);
			query.setParameter("password", password);
			user = (User) query.uniqueResult();
			if (user == null) {
				return null;
			}
			user.setLoginCount(user.getLoginCount() + 1);
			user.setLastLogin(Calendar.getInstance().getTimeInMillis());
			user.setLoginCount(user.getLoginCount());
			user.setActive(true);
			session.saveOrUpdate(user);
			this.getThreadLocalRequest().getSession()
					.setAttribute(EMAIL_ID, user.getEmail());
			this.getThreadLocalRequest()
					.getSession()
					.setAttribute(COMPANY_ID,
							getCompanyName(getThreadLocalRequest()));
			getThreadLocalRequest().getSession().setAttribute("offSet", offSet);
			CometManager.initStream(getThreadLocalRequest().getSession()
					.getId(), user.getEmail(), "bizantra");

			if (rememberMe) {
				setCookies(string, password);
			}
			return user.getClientUser();
		}
		return null;
	}

	private void setCookies(String string, String password) {
		Cookie cookie = new Cookie("_accounter_01_infinity_2711",
				new StringBuffer(string).append(",").append(password)
						.toString());
		cookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		this.getThreadLocalResponse().addCookie(cookie);
	}
}
