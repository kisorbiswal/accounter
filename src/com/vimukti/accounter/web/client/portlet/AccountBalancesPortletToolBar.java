package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.reports.PortletToolBar;

public abstract class AccountBalancesPortletToolBar extends PortletToolBar {
	private SelectCombo accountTypeCombo;
	private List<String> accountTypesList;

	public AccountBalancesPortletToolBar() {
		createControls();
	}

	private void createControls() {
		accountTypeCombo = new SelectCombo(messages.accountType());
		accountTypesList = new ArrayList<String>();
		int[] accountTypes = UIUtils.accountTypes;
		for (int i = 0; i < accountTypes.length; i++) {
			accountTypesList.add(Utility.getAccountTypeString(accountTypes[i]));
		}
		accountTypeCombo.initCombo(accountTypesList);
		this.addItems(accountTypeCombo);
		accountTypeCombo.setSelected(updateComboData());
		accountTypeChanged(updateComboData());
		accountTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						accountTypeChanged(selectItem);
					}
				});
	}

	protected abstract void accountTypeChanged(String selectItem);

	protected abstract String updateComboData();

}
