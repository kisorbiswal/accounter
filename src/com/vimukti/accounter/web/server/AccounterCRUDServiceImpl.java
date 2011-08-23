package com.vimukti.accounter.web.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.mortbay.util.UrlEncoded;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Fernandez This Service is for All CREATE UPDATE DELETE Operations
 * 
 */

public class AccounterCRUDServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterCRUDService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterCRUDServiceImpl() {
		super();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {

		super.service(arg0, arg1);

	}

	@Override
	public long create(IAccounterCore coreObject) throws AccounterException {

		FinanceTool tool = getFinanceTool();
		String clientClassSimpleName = coreObject.getObjectType()
				.getClientClassSimpleName();

		OperationContext context = new OperationContext(coreObject,
				getUserEmail());
		context.setArg2(clientClassSimpleName);

		return tool.create(context);
	}

	@Override
	public long update(IAccounterCore coreObject) throws AccounterException {
		FinanceTool tool = getFinanceTool();

		String serverClassFullyQualifiedName = coreObject.getObjectType()
				.getServerClassFullyQualifiedName();

		OperationContext context = new OperationContext(coreObject,
				getUserEmail(), String.valueOf(coreObject.getID()),
				serverClassFullyQualifiedName);

		return tool.update(context);
	}

	@Override
	public boolean delete(AccounterCoreType type, long id)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(type,
				String.valueOf(id));
		opContext.setArg1(String.valueOf(id));
		opContext.setArg2(type.getClientClassSimpleName());
		return tool.delete(opContext);

	}

	@Override
	public boolean updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		OperationContext updateComPref = new OperationContext(preferences,
				getUserEmail());

		tool.updateCompanyPreferences(updateComPref);
		return true;
	}

	@Override
	public Long updateCompany(ClientCompany clientCompany)
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(clientCompany,
				getUserEmail());

		return tool.updateCompany(opContext);

	}

	@Override
	public Boolean voidTransaction(AccounterCoreType accounterCoreType, long id)
			throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				accounterCoreType.getServerClassFullyQualifiedName(), id);
		if (serverCore instanceof Transaction) {
			Transaction trans = (Transaction) serverCore;
			trans.setVoid(true);
			update((IAccounterCore) new ClientConvertUtil().toClientObject(
					serverCore,
					Util.getClientEqualentClass(serverCore.getClass())));

			return true;
		}
		return false;
	}

	@Override
	public boolean deleteTransaction(AccounterCoreType accounterCoreType,
			long id) throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				accounterCoreType.getServerClassSimpleName(), id);
		if (serverCore instanceof Transaction) {
			Transaction trans = (Transaction) serverCore;
			trans.setStatus(Transaction.STATUS_DELETED);
			trans.setVoid(true);
			update((IAccounterCore) new ClientConvertUtil().toClientObject(
					serverCore,
					Util.getClientEqualentClass(serverCore.getClass())));

			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(AccounterCoreType accounterCoreType, long id)
			throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				accounterCoreType.getServerClassFullyQualifiedName(), id);
		return serverCore.canEdit(serverCore);
	}

	@Override
	public long inviteUser(IAccounterCore coreObject) throws AccounterException {

		ClientUserInfo invitedser = (ClientUserInfo) coreObject;

		IS2SService s2sSyncProxy = getS2sSyncProxy(ServerConfiguration
				.getServerDomainName());

		// Creating Clien
		try {
			String company = getCookie(BaseServlet.COMPANY_COOKIE);
			s2sSyncProxy.inviteUser(Integer.parseInt(company), invitedser,
					getUserEmail());
		} catch (Exception e) {
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

		// Creating Use in Local Company Database
		ClientUser coreUser = convertUserInfoToUser(invitedser);
		String clientClassSimpleName = coreUser.getObjectType()
				.getClientClassSimpleName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(coreUser,
				getUserEmail(), String.valueOf(coreObject.getID()),
				clientClassSimpleName);
		return financeTool.inviteUser(context);
	}

	private IS2SService getS2sSyncProxy(String domainName) {
		String url = "http://" + domainName + ":"
				+ ServerConfiguration.getMainServerPort() + "/stosservice";
		return (IS2SService) SyncProxy.newProxyInstance(IS2SService.class, url,
				"");
	}

	private ClientUser convertUserInfoToUser(ClientUserInfo clientUserInfo) {
		ClientUser clientUser = new ClientUser();
		clientUser.setCanDoUserManagement(clientUserInfo
				.isCanDoUserManagement());
		clientUser.setFirstName(clientUserInfo.getFirstName());
		clientUser.setLastName(clientUserInfo.getLastName());
		clientUser.setFullName(clientUserInfo.getFullName());
		clientUser.setEmail(clientUserInfo.getEmail());
		clientUser.setUserRole(clientUserInfo.getUserRole());
		clientUser.setPermissions(clientUserInfo.getPermissions());
		// Setting the user permissions
		// new ServerConvertUtil().toServerObject(
		// (IAccounterServerCore) serverUser.getPermissions(),
		// (IAccounterCore) clientUserInfo.getPermissions(), session);
		clientUser.setDisplayName(clientUserInfo.getDisplayName());
		clientUser.setAdmin(clientUserInfo.isAdmin());
		clientUser.setID(clientUserInfo.getID());
		return clientUser;
	}

	@Override
	public long updateUser(IAccounterCore coreObject) throws AccounterException {
		ClientUser coreUser = convertUserInfoToUser((ClientUserInfo) coreObject);
		String serverClassSimpleName = coreUser.getObjectType()
				.getServerClassFullyQualifiedName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(coreUser,
				getUserEmail(), String.valueOf(coreObject.getID()),
				serverClassSimpleName);
		return financeTool.updateUser(context);
	}

	@Override
	public boolean deleteUser(IAccounterCore deletableUser, String senderEmail)
			throws AccounterException {
		String deleteUserurl = getDeleteUserURLString(
				(ClientUserInfo) deletableUser, senderEmail);
		if (deleteUserurl == null) {
			return false;
		}
		try {
			URL url = new URL(deleteUserurl.toString());
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int responseCode = 0;
			try {
				responseCode = connection.getResponseCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (responseCode == 200) {
				ClientUser coreUser = convertUserInfoToUser((ClientUserInfo) deletableUser);
				String clientClassSimpleName = coreUser.getObjectType()
						.getClientClassSimpleName();
				FinanceTool financeTool = new FinanceTool();
				OperationContext context = new OperationContext(coreUser,
						getUserEmail(), String.valueOf(deletableUser.getID()),
						clientClassSimpleName);
				return financeTool.delete(context);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getDeleteUserURLString(ClientUserInfo deletableUser,
			String senderEmail) {
		StringBuffer buffer = new StringBuffer(
				"http://localhost:8890/deleteUser?");
		buffer.append("senderEmailId");
		buffer.append('=');
		buffer.append(new UrlEncoded(senderEmail).encode());
		buffer.append('&');
		buffer.append("deletableUserEmail");
		buffer.append('=');
		buffer.append(new UrlEncoded(deletableUser.getEmail()).encode());
		buffer.append('&');
		buffer.append("serverCompanyId");
		buffer.append('=');
		String serverCompanyId = getCookie(BaseServlet.COMPANY_COOKIE);
		buffer.append(new UrlEncoded(serverCompanyId).encode());
		return buffer.toString();
	}
}