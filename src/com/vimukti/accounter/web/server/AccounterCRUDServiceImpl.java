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

		String clientClassSimpleName = coreObject.getObjectType()
				.getClientClassSimpleName();

		OperationContext context = new OperationContext(coreObject,
				getUserEmail(), String.valueOf(coreObject.getID()),
				clientClassSimpleName);

		return tool.update(context);
	}

	@Override
	public boolean delete(AccounterCoreType type, long id)
			throws AccounterException {

		try {

			FinanceTool tool = getFinanceTool();
			OperationContext opContext = new OperationContext(type,
					String.valueOf(id));
			return tool.delete(opContext);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public long updateCompanyPreferences(ClientCompanyPreferences preferences) {

		try {

			FinanceTool tool = getFinanceTool();
			OperationContext updateComPref = new OperationContext(preferences);

			return tool.updateCompanyPreferences(updateComPref);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public long updateCompany(ClientCompany clientCompany) {

		try {

			FinanceTool tool = getFinanceTool();
			OperationContext opContext = new OperationContext(clientCompany);

			return tool.updateCompany(opContext);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public Boolean voidTransaction(AccounterCoreType accounterCoreType, long id)
			throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByid(getSession(),
						accounterCoreType.getServerClassSimpleName(), id);
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
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByid(getSession(),
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
		IAccounterServerCore serverCore = (IAccounterServerCore) Util
				.loadObjectByid(getSession(),
						accounterCoreType.getServerClassSimpleName(), id);
		try {
			return serverCore.canEdit(serverCore);
		} catch (InvalidOperationException e) {
			e.printStackTrace();
		}
		return false;
	}
}