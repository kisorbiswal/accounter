package com.vimukti.accounter.mobile.preferences;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.commands.NewAbstractCommand;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;
import com.vimukti.accounter.web.server.managers.CompanyManager;

public class AbstractCompanyPreferencesCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	protected void savePreferences(Context context,
			ClientCompanyPreferences preferences) {
		try {
			CompanyManager companyManager = new FinanceTool()
					.getCompanyManager();
			ClientCompany clientCompany = companyManager.getClientCompany(
					context.getEmailId(), getCompanyId());

			OperationContext opContext = new OperationContext(getCompanyId(),
					clientCompany, context.getEmailId());

			clientCompany.setPreferences(preferences);
			companyManager.updateCompany(opContext);

		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

}
