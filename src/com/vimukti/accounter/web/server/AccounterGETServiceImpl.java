package com.vimukti.accounter.web.server;

import java.util.List;

import com.bizantra.client.data.InvaliedSessionException;
import com.bizantra.server.utils.SecureUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.workspace.tool.FinanceTool;
import com.vimukti.accounter.workspace.tool.IFinanceTool;

/**
 * @author Fernandez
 * 
 */

public class AccounterGETServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterGETService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterGETServiceImpl() {
		super();

	}

	@Override
	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			String stringID) throws InvaliedSessionException {

		IFinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectById(type, stringID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws InvaliedSessionException {

		IFinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectByName(type, name);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> List<T> getObjects(AccounterCoreType type) {

		try {

			return getFinanceTool().getObjects(type);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientCompany getCompany() throws InvaliedSessionException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getClientCompany();
	}

	@Override
	public String getStringID() {
		return SecureUtils.createID();
	}

	@Override
	public KeyFinancialIndicators getKeyFinancialIndicators() {
		KeyFinancialIndicators keyFinancialIndicators = new KeyFinancialIndicators();
		try {
			keyFinancialIndicators = getFinanceTool()
					.getKeyFinancialIndicators();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyFinancialIndicators;
	}

	@Override
	public List<HrEmployee> getHREmployees() throws InvaliedSessionException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getHREmployees();
	}
}