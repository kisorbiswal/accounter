package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewPayHeadView extends BaseView<ClientPayHead> {

	private VerticalPanel panel;
	private TextItem nameItem, aliasItem, payslipNameItem;
	private SelectCombo typeCombo, calculationTypeCombo,
			calculationPeriodCombo, roundingMethodCombo;
	RadioGroupItem affectNetSalarytem;
	String[] types = { "Earnings for Employees", "Deductions for Employees",
			"Employees Statutory Deductions",
			"Employees Statutory Contributions", "Employees Other Charges",
			"Bonus", "Gratuity", "Loans and Advances",
			"Reimbursements to Employees" };

	String[] calType = { "Attendence", "As Computed Value", "Flat Rate",
			"Production", "As User Defined" };

	String[] calPeriod = { messages.days(), messages.weeks(), messages.months() };

	String[] roundingMethods = { "Downword", "Normal", "Upword" };

	List<String> typeList = new ArrayList<String>();
	List<String> calTypeList = new ArrayList<String>();
	List<String> calPeriodList = new ArrayList<String>();
	List<String> roundingList = new ArrayList<String>();

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayHead());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayHead data) {
		// TODO Auto-generated method stub

	}

	protected void createControls() {
		panel = new VerticalPanel();

		for (int i = 0; i < types.length; i++) {
			typeList.add(types[i]);
		}

		for (int i = 0; i < calType.length; i++) {
			calTypeList.add(calType[i]);
		}

		for (int i = 0; i < calPeriod.length; i++) {
			calPeriodList.add(calPeriod[i]);
		}

		for (int i = 0; i < roundingMethods.length; i++) {
			roundingList.add(roundingMethods[i]);
		}

		nameItem = new TextItem(messages.name(), "nameItem");
		aliasItem = new TextItem(messages.alias(), "aliasItem");
		typeCombo = new SelectCombo(messages.type(), false);
		typeCombo.initCombo(typeList);
		calculationTypeCombo = new SelectCombo(messages.calculationType(),
				false);
		calculationTypeCombo.initCombo(calTypeList);
		calculationPeriodCombo = new SelectCombo(messages.calculationPeriod(),
				false);
		calculationPeriodCombo.initCombo(calPeriodList);
		roundingMethodCombo = new SelectCombo(messages.roundingMethod(), false);
		roundingMethodCombo.initCombo(roundingList);
		affectNetSalarytem = new RadioGroupItem(messages.affectNetSalary());
		affectNetSalarytem.setValueMap(messages.yes(), messages.no());
		payslipNameItem = new TextItem(messages.paySlipName(),
				"payslipNameItem");

		DynamicForm form = new DynamicForm("form");
		form.add(nameItem, aliasItem, typeCombo, calculationTypeCombo,
				calculationPeriodCombo, roundingMethodCombo,
				affectNetSalarytem, payslipNameItem);

		panel.add(form);
		this.add(panel);

		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public ValidationResult validate() {
		return super.validate();
	}

	private void updateData() {
		ClientPayHead head = new ClientPayHead();
		head.setName(nameItem.getValue());
		head.setAlias(aliasItem.getValue());
		head.setType(typeCombo.getSelectedIndex());
		head.setCalculationType(calculationTypeCombo.getSelectedIndex());
		// head.setCalculationPeriod(calculationPeriodCombo.getSelectedIndex());
		head.setRoundingMethod(roundingMethodCombo.getSelectedIndex());
		head.setNameToAppearInPaySlip(payslipNameItem.getValue());
		saveOrUpdate(head);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.newPayHead();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
