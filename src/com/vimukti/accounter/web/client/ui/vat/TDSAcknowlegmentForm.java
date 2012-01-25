package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSAcknowlegmentForm extends BaseDialog {

	private DynamicForm form;
	private DynamicForm form1;

	private SelectCombo formTypeCombo;
	private SelectCombo financialYearCombo;
	private SelectCombo quaterCombo;

	private TextItem ackNoField;

	private DateField dateField;

	public TDSAcknowlegmentForm() {
		super("TDS Acknowledgement Form",
				"Add the acknowledgement no. you get after e TDS filling.");
		setWidth("650px");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form1 = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");
		form1.setHeight("100%");
		form1.setWidth("100%");
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		formTypeCombo = new SelectCombo("Form No.");
		formTypeCombo.setRequired(true);
		formTypeCombo.setHelpInformation(true);
		formTypeCombo.initCombo(getFormTypes());
		formTypeCombo.setSelectedItem(0);

		financialYearCombo = new SelectCombo("Financial Year");
		financialYearCombo.setRequired(true);
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		financialYearCombo.setRequired(true);

		quaterCombo = new SelectCombo("For Quarter");
		quaterCombo.setRequired(true);
		quaterCombo.setHelpInformation(true);
		quaterCombo.initCombo(getFinancialQuatersList());
		quaterCombo.setSelectedItem(0);
		quaterCombo.setRequired(true);

		ackNoField = new TextItem(messages.acknowledgmentNo());
		ackNoField.setRequired(true);
		ackNoField.setHelpInformation(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();
		dateField = new DateField(messages.date());
		dateField.setRequired(true);
		dateField.setHelpInformation(true);
		dateField.setColSpan(1);
		dateField.setTitle(messages.date());
		dateField.setValue(todaysDate);

		form.setItems(formTypeCombo, quaterCombo, financialYearCombo);
		form1.setItems(ackNoField, dateField);

		layout1.add(form);
		layout1.add(form1);
		vPanel.add(layout1);
		setBodyLayout(vPanel);

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		result.add(form1.validate());
		return result;
	}

	@Override
	protected boolean onOK() {

		int formType = 0;
		int quater = 0;
		int startYear = 0;
		int endYear = 0;
		if (formTypeCombo.getSelectedValue() != null) {
			formType = formTypeCombo.getSelectedIndex() + 1;
		}
		if (quaterCombo.getSelectedValue() != null) {
			quater = quaterCombo.getSelectedIndex() + 1;
		}
		if (financialYearCombo.getSelectedValue() != null) {
			String[] tokens = financialYearCombo.getSelectedValue().split("-");
			startYear = Integer.parseInt(tokens[0]);
			endYear = Integer.parseInt(tokens[1]);
		}

		Accounter.createHomeService().updateAckNoForChallans(formType, quater,
				startYear, endYear, ackNoField.getValue(),
				dateField.getValue().getDate(),
				new AccounterAsyncCallback<Boolean>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError("Update Failed");
					}

					@Override
					public void onResultSuccess(Boolean result) {
						if (result) {
							saveSuccess(null);
						} else {
							Accounter.showError("Update Failed");
						}
					}
				});
		return false;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		onCancel();
		this.removeFromParent();
	}

	@Override
	public void setFocus() {
		ackNoField.setFocus();
	}

}
