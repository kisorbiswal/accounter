package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.vimukti.accounter.workspace.tool.FinanceTool;

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
			long id) throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectById(type, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

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
	public ClientCompany getCompany()
			throws InvalidSessionException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getClientCompany();
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
	public List<HrEmployee> getHREmployees() throws InvalidSessionException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getHREmployees();
	}

	public List<HelpLink> getHelpLinks(int type) {

		HelpLink link = new HelpLink(
				"How to EnterBill transaction?",
				"Click on the Enter Bill action or in the Supplier drop down select Enter Bill.");
		HelpLink link1 = new HelpLink(
				"How to Invoice transaction?",
				"Click on the New Invoice action or in the Customer drop down select New and then New Invoice");
		HelpLink link2 = new HelpLink(
				"How to Quote transaction?",
				"Click on the Quote action or in the Customer drop down select New and then New Quote");
		HelpLink link3 = new HelpLink("How to Paybill transaction?",
				"Click on the PayBill action or in the Supplier drop down select PayBill");
		HelpLink link4 = new HelpLink("How to CustomerPayment transaction?",
				"In the Customer drop down select Customer payment");
		HelpLink link5 = new HelpLink("How to Makedeposit transaction?",
				"In the Banking drop down select Deposit/Transfer Funds");

		List<HelpLink> helpLinks = new ArrayList<HelpLink>();

		helpLinks.add(link);
		helpLinks.add(link1);
		helpLinks.add(link2);
		helpLinks.add(link3);
		helpLinks.add(link4);
		helpLinks.add(link5);

		return helpLinks;

	}
}