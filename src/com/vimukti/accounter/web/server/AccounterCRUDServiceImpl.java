package com.vimukti.accounter.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.workspace.tool.FinanceTool;

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
		// TODO Auto-generated method stub

		super.service(arg0, arg1);

	}

	@Override
	public String create(IAccounterCore coreObject)
			throws InvalidOperationException, InvalidSessionException

	{

		FinanceTool tool = getFinanceTool();

		return tool.createObject(coreObject);
	}

	@Override
	public Boolean update(IAccounterCore coreObject)
			throws InvalidOperationException, InvalidSessionException

	{
		FinanceTool tool = getFinanceTool();

		return tool.updateObject(coreObject);
	}

	@Override
	public Boolean delete(AccounterCoreType type, String stringID)
			throws InvalidOperationException

	{

		try {

			FinanceTool tool = getFinanceTool();

			return tool.deleteObject(type, stringID);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Boolean updateCompanyPreferences(ClientCompanyPreferences preferences) {

		try {

			FinanceTool tool = getFinanceTool();

			return tool.updateCompanyPreferences(preferences);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Boolean updateCompany(ClientCompany clientCompany) {

		try {

			FinanceTool tool = getFinanceTool();

			return tool.updateCompany(clientCompany);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Boolean voidTransaction(AccounterCoreType accounterCoreType,
			String stringID) throws InvalidOperationException,
			InvalidSessionException {
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByStringID(getSession(), accounterCoreType
						.getServerClassSimpleName(), stringID);
		if (serverCore instanceof Transaction) {
			Transaction trans = (Transaction) serverCore;
			trans.setVoid(true);
			update((IAccounterCore) new ClientConvertUtil().toClientObject(
					serverCore, Util.getClientEqualentClass(serverCore
							.getClass())));

			return true;
		}
		return false;
	}

	@Override
	public Boolean deleteTransaction(AccounterCoreType accounterCoreType,
			String stringID) throws InvalidOperationException,
			InvalidSessionException {
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByStringID(getSession(), accounterCoreType
						.getServerClassSimpleName(), stringID);
		if (serverCore instanceof Transaction) {
			Transaction trans = (Transaction) serverCore;
			trans.setStatus(Transaction.STATUS_DELETED);
			trans.setVoid(true);
			update((IAccounterCore) new ClientConvertUtil().toClientObject(
					serverCore, Util.getClientEqualentClass(serverCore
							.getClass())));

			return true;
		}
		return false;
	}

	@Override
	public Boolean canEdit(AccounterCoreType accounterCoreType, String stringID)
			throws InvalidOperationException {
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByStringID(getSession(), accounterCoreType
						.getServerClassSimpleName(), stringID);
		return serverCore.canEdit(serverCore);
	}

}