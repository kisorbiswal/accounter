package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CopyBudgetDialogue extends BaseDialog {

	SelectCombo selectBudget;
	List<ClientBudget> budgetList;

	String budgetName;

	public CopyBudgetDialogue(String title, String desc) {
		super(title, desc);
		createControls();
	}

	public CopyBudgetDialogue(String budgetTitle, String string,
			List<ClientBudget> listData) {
		super(budgetTitle, string);
		budgetList = listData;
		createControls();
	}

	private void createControls() {
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidth("400px");

		selectBudget = new SelectCombo(Accounter.constants().copy()
				+ " from existing " + Accounter.constants().budget() + " :");
		selectBudget.setHelpInformation(true);

		for (ClientBudget budget : budgetList) {
			selectBudget.addComboItem(budget.getBudgetName());
		}
		if (budgetList.size() < 1) {
			selectBudget.addComboItem(Accounter.constants().emptyValue());
		}

		selectBudget
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						for (ClientBudget budget : budgetList) {
							if (selectBudget.getSelectedValue().equals(
									budget.getBudgetName())) {
								budgetName = budget.getBudgetName();
								break;
							}
						}

					}

				});

		DynamicForm budgetInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));
		budgetInfoForm.setWidth("100%");

		budgetInfoForm.setFields(selectBudget);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(budgetInfoForm);

		verticalPanel.add(horizontalPanel);

		setBodyLayout(verticalPanel);
		center();
	}

	@Override
	protected boolean onOK() {

		getCallback().actionResult(budgetName);
		return true;
	}
}
