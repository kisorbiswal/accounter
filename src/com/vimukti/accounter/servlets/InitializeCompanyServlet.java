/**
 * 
 */
package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * @author Prasanna Kumar G
 * 
 */
public class InitializeCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String serverCompanyID = req.getParameter(PARAM_SERVER_COMPANY_ID);
		String companyType = req.getParameter(PARAM_COMPANY_TYPE);
		String companName = req.getParameter(PARA_COMPANY_NAME);
		String emailID = req.getParameter(EMAIL_ID);
		Company company = new Company(Integer.parseInt(companyType));
		company.setFullName(companName);
		init(req, resp, company, Long.parseLong(serverCompanyID), emailID);
	}

	private void init(HttpServletRequest request, HttpServletResponse resp,
			Company company, long serverCompnayId, String emailID)
			throws IOException {

		String schemaName = Server.COMPANY + serverCompnayId;

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = session.beginTransaction();
		try {
			Query query = session.createSQLQuery("CREATE SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			serverTransaction.rollback();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Exception while Creating CompanyDataBase");
			return;
		}
		Session companySession = HibernateUtil.openSession(schemaName, true);
		Transaction transaction = companySession.beginTransaction();
		try {

			User user = getAdminUserFromHttpSession(request);
			// Creating User
			companySession.save(user);

			AccounterThreadLocal.set(user);

			company.getUsers().add(user);
			company.setCompanyEmail(user.getEmail());
			company.setConfigured(true);
			companySession.save(company);

			// Create Attachment Directory for company
			File file = new File(ServerConfiguration.getAttachmentsDir(company
					.getFullName()));

			if (!file.exists()) {
				file.mkdir();
			}

			company.initialize(null);

			FinanceTool.createView();

			transaction.commit();
			UsersMailSendar.sendMailToDefaultUser(user, company.getFullName());

		} catch (Exception e) {
			e.printStackTrace();
			dropSchema(schemaName);
			transaction.rollback();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Exception while Creating Company");
			return;
		} finally {
			if (companySession.isOpen()) {
				companySession.close();
			}
		}
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void dropSchema(String schemaName) {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = session.beginTransaction();
		try {
			Query query = session.createSQLQuery("DROP SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			serverTransaction.rollback();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	 * Returns User Object From Client HttpSession
	 */
	private User getAdminUserFromHttpSession(HttpServletRequest request) {
		User user = new User();
		user.setFirstName(request.getParameter(PARAM_FIRST_NAME));
		user.setLastName(request.getParameter(PARAM_LAST_NAME));
		user.setFullName(user.getFirstName() + " " + user.getLastName());
		user.setEmail(request.getParameter(EMAIL_ID));
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		permissions.setTypeOfInvoices(RolePermissions.TYPE_YES);
		permissions.setTypeOfLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		user.setPermissions(permissions);
		return user;
	}
}
