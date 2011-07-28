package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class TAXAgencyCombo extends CustomCombo<ClientTAXAgency> {

	public TAXAgencyCombo(String title) {
		super(title);
		initCombo(getCompany().getActiveTAXAgencies());
	}

	public TAXAgencyCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(getCompany().getActiveTAXAgencies());
	}

	@Override
	public String getDefaultAddNewCaption() {
		if(getCompany().getAccountingType()== 0)
			return Accounter.constants().newTaxAgency();
		else
		return Accounter.constants().newVATAgency();

	}

	@Override
	public void onAddNew() {
		Action action = CompanyActionFactory.getNewTAXAgencyAction();
		action.setActionSource(this);
		
		action.run(null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.TAX_AGENCY; 
	}

	@Override
	protected String getDisplayName(ClientTAXAgency object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientTAXAgency object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
