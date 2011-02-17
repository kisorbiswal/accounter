/**
 * 
 */
package com.vimukti.accounter.workspace.tool;

import com.bizantra.server.workspace.ITool;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.services.IFinanceDAOService;

/**
 * @author Fernandez
 * 
 */
public interface IFinanceTool extends ITool, IFinanceDAOService {

	int CREATE_NEW_ACTION = 777;

	int UPDATE_ACTION = 888;

	int DELETE_ACTION = 999;

	int UPDATE_PREFERENCES = 899;

	int UPDATE_COMPANY = 890;
	
	int UPDATE_COMPANY_STARTDATE = 891;
	
	int UPDATE_DEPRECIATION_STARTDATE = 892;

	Company getCompany();

}
