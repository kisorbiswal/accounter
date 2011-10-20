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

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;

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

	}

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			if (isValidSession(request)) {
				Session session = HibernateUtil.openSession();
				try {
					if (CheckUserExistanceAndsetAccounterThreadLocal(request)) {
						super.service(request, response);
						try {
							String serverCompanyID = (String) request
									.getSession().getAttribute(COMPANY_ID);
							getFinanceTool().putChangesInCometStream(
									Long.parseLong(serverCompanyID));
						} catch (AccounterException e) {
							log.error("Failed to get FinanceTool", e);
						}
						// TODO
						// if (ChangeTracker.getChanges().length > 1) {
						// FinanceTool financeTool = (FinanceTool) session.load(
						// FinanceTool.class, 1l);
						// financeTool.putChangesInCometStream();
						// }
					} else {
						response.sendError(HttpServletResponse.SC_FORBIDDEN,
								"Could Not Complete the Request!");
					}
				} finally {
					session.close();
				}
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Could Not Complete the Request!");
			}
		} catch (Exception e) {

			e.printStackTrace();

			log.error("Failed to Process Request", e);

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");

		}
	}

	/**
	 * @param request
	 */
	private boolean CheckUserExistanceAndsetAccounterThreadLocal(
			HttpServletRequest request) {
		Session session = HibernateUtil.getCurrentSession();
		String serverCompanyID = (String) request.getSession().getAttribute(
				COMPANY_ID);
		if (serverCompanyID == null) {
			return false;
		}
		Company company = (Company) session.get(Company.class,
				Long.valueOf(serverCompanyID));
		if (company == null) {
			return false;
		}
		String userEmail = (String) request.getSession().getAttribute(EMAIL_ID);
		User user = company.getUserByUserEmail(userEmail);
		if (user == null) {
			return false;
		}
		AccounterThreadLocal.set(user);
		return true;
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
		return new FinanceTool();
	}

	// protected FinanceTool getFinanceTool(HttpServletRequest request) {
	// // Auto-generated method stub
	// return null;
	// }

	private String getCompanyName(HttpServletRequest req) {
		String companyID = (String) req.getSession().getAttribute(COMPANY_ID);
		if (companyID == null) {
			// TODO Throw Exception
		}
		Session session = HibernateUtil.openSession();
		try {
			Company company = (Company) session.get(Company.class,
					Long.parseLong(companyID));
			if (company != null) {
				return company.getTradingName();
			}
		} finally {
			session.close();
		}
		return null;
	}

	// private String getCompanyDBName(HttpServletRequest req) {
	// String cid = getCookie(req, BaseServlet.COMPANY_COOKIE);
	// if (cid == null) {
	// // TODO Throw Exception
	// }
	// return Server.COMPANY + cid;
	// }

	protected String getCookie(String cookieName) {
		return getCookie(getThreadLocalRequest(), cookieName);
	}

	private String getCookie(HttpServletRequest req, String cookieName) {
		Cookie[] clientCookies = req.getCookies();
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

			session.saveOrUpdate(user);
			this.getThreadLocalRequest().getSession()
					.setAttribute(EMAIL_ID, user.getClient().getEmailId());
			this.getThreadLocalRequest()
					.getSession()
					.setAttribute(COMPANY_ID,
							getCompanyName(getThreadLocalRequest()));
			getThreadLocalRequest().getSession().setAttribute("offSet", offSet);
			CometSession cometSession = CometServlet
					.getCometSession(getHttpSession());
			CometManager.initStream(getThreadLocalRequest().getSession()
					.getId(), getCompanyId(), user.getClient().getEmailId(),
					cometSession);

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

	protected Object loadObjectById(String className, long id)
			throws AccounterException {
		try {
			Class.forName(className);
			Object object = getSession().get(className, id);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL,
					e.getMessage());
		}
	}

	protected IS2SService getS2sSyncProxy(String domainName) {
		String url = "http://" + domainName + ":"
				+ ServerConfiguration.getMainServerPort()
				+ "/company/stosservice";
		return (IS2SService) SyncProxy.newProxyInstance(IS2SService.class, url,
				"");
	}

	protected Long getCompanyId(HttpServletRequest request) {
		Long companyID = (Long) request.getSession().getAttribute(COMPANY_ID);
		return companyID;
	}

	protected Long getCompanyId() {
		Long companyID = (Long) getThreadLocalRequest().getSession()
				.getAttribute(COMPANY_ID);
		return companyID;
	}

}
