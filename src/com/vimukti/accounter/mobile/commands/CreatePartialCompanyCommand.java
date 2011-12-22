package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;

public class CreatePartialCompanyCommand extends AbstractCompanyCommad {

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		super.addRequirements(list);

		addCurrencyRequirements(list);

		list.add(getFiscalYearRequirement());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().company());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(COUNTRY).setDefaultValue("United Kingdom");
		get(STATE).setDefaultValue("Buckinghamshire");
		countrySelected("United Kingdom");
		get(TIME_ZONE).setDefaultValue(getDefaultTzOffsetStr());
		get(ORGANIZATION_REFER).setDefaultValue(getOrganizationTypes().get(0));
		get(FISCAL_YEAR).setDefaultValue(DayAndMonthUtil.april());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().company());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().partialCompanySetup());
	}
}
