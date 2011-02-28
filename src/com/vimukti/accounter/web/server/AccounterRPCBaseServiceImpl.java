/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.bizantra.server.core.IIdentity;
import com.bizantra.server.internal.core.CollaberIdentity;
import com.bizantra.server.services.BizantraService;
import com.bizantra.server.storage.HibernateUtil;
import com.bizantra.server.workspace.IWorkSpace;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.workspace.tool.FinanceTool;
import com.vimukti.accounter.workspace.tool.IFinanceTool;
import com.vimukti.comet.server.CometManager;

/**
 * 
 * Base RPC Service.
 * 
 * @author Fernandez *
 * 
 */
public class AccounterRPCBaseServiceImpl extends BizantraService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected IFinanceTool financeTool;

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

	@Override
	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			if (isIdenityIdExist(request)) {
				super.service(request, response);

				if (ChangeTracker.getChanges().length > 1) {
					if (getIdentity() == null) {
						CollaberIdentity identity = (CollaberIdentity) HibernateUtil
								.openSession(getCompanyFromRequest(request))
								.load(
										CollaberIdentity.class,
										(Serializable) request.getSession()
												.getAttribute("identityID"));

						IWorkSpace workspace = identity.getFinanceWorkspace();

						FinanceTool financeTool = (FinanceTool) workspace
								.getToolByName(IWorkSpace.FINANCE_TOOL);
						financeTool.putChangesInCometStream();
					}
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
	@SuppressWarnings("unused")
	private String getUserID() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				"identityID");
	}

	public boolean isIdenityIdExist(HttpServletRequest request) {
		return request.getSession().getAttribute("identityID") == null ? false
				: true;
	}

	protected long getThreadLocalUserId() {
		if (getThreadLocalRequest() != null)
			return getThreadLocalRequest().getSession().getAttribute(USER_ID) != null ? (Long) getThreadLocalRequest()
					.getSession().getAttribute(USER_ID)
					: 0;
		return 0;
	}

	protected long getThreadLocalCompanyId() throws InvalidSessionException {

		return getFinanceTool().getCompany().getId();

	}

	protected IFinanceTool getFinanceTool() throws InvalidSessionException {

		IIdentity identity = getIdentity();

		if (identity == null) {
			return null;
		}
		IWorkSpace workspace = identity.getFinanceWorkspace();

		if (workspace == null) {
			return null;
		}

		IFinanceTool financeTool = (IFinanceTool) workspace
				.getToolByName(IWorkSpace.FINANCE_TOOL);
		return financeTool;

	}

	@Override
	public CollaberIdentity getIdentity() throws InvalidSessionException {
		if (this.getThreadLocalRequest() != null) {
			return super.getIdentity();
		}
		return null;
	}

}
