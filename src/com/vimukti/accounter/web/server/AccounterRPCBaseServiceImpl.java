/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.google.gwt.rpc.server.RpcServlet;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.workspace.tool.AccounterException;
import com.vimukti.accounter.workspace.tool.FinanceTool;
import com.vimukti.comet.server.CometManager;

/**
 * 
 * Base RPC Service.
 * 
 * @author Fernandez *
 * 
 */
public class AccounterRPCBaseServiceImpl extends RpcServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Logger log = Logger.getLogger(this.getClass());

	protected static final String USER_ID = "userId";

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
	protected long getUserID() {
		return (Long) getThreadLocalRequest().getSession()
				.getAttribute(USER_ID);
	}

	public boolean isValidSession(HttpServletRequest request) {
		return request.getSession().getAttribute(USER_ID) == null ? false
				: true;
	}

	protected FinanceTool getFinanceTool() throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		FinanceTool financeTool = (FinanceTool) session.load(FinanceTool.class,
				1l);
		return financeTool;

	}

	protected FinanceTool getFinanceTool(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getCompanyName(HttpServletRequest req) {
		return null;

	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}
}
