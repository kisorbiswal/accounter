package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class RecurringTransactionDialog extends
		BaseDialog<ClientRecurringTransaction> {

	private TextItem nameField;
	private DateItem startDateField;
	private DateItem endDateField;
	private SelectCombo actionComboField;
	private TextAndComboPairForm howOftenField;
	private TextAndComboPairForm dueField;

	private ClientRecurringTransaction existingTransaction;

	private AbstractTransactionBaseView<? extends ClientTransaction> parentView;

	private List<DynamicForm> dynamicForms;

	/**
	 * for creating new Recurring transaction.
	 * 
	 * @param parentView
	 */
	public RecurringTransactionDialog(
			AbstractTransactionBaseView<? extends ClientTransaction> parentView) {
		this(parentView, null);

	}

	/**
	 * For editing existing recurring transaction.
	 * 
	 * @param parentView
	 * @param transaction
	 */
	public RecurringTransactionDialog(ClientRecurringTransaction transaction) {
		this(null, transaction);
	}

	private RecurringTransactionDialog(
			AbstractTransactionBaseView<? extends ClientTransaction> parentView,
			ClientRecurringTransaction transaction) {
		super("Recurring", "Recurring desc");
		this.parentView = parentView;
		existingTransaction = transaction;
		createControls();
		center();
	}

	private void createControls() {
		mainPanel.setSpacing(15);
		dynamicForms = new ArrayList<DynamicForm>();

		DynamicForm form = new DynamicForm();

		nameField = new TextItem("Name:");
		nameField.setRequired(true);
		nameField.setHelpInformation(true);

		startDateField = new DateItem("Start Date:");
		startDateField.setRequired(true);
		startDateField.setHelpInformation(true);

		endDateField = new DateItem("End Date:");
		endDateField.setRequired(false);
		endDateField.setHelpInformation(true);

		actionComboField = new SelectCombo("Action:");
		actionComboField.initCombo(getActionOptions());
		actionComboField.setRequired(true);
		actionComboField.setHelpInformation(true);

		howOftenField = new TextAndComboPairForm("Frequency: ",
				getHowOfenOptions());
		dueField = new TextAndComboPairForm("Due: ", getDueDateOptions());

		form.setFields(nameField, startDateField, endDateField,
				actionComboField);

		dynamicForms.add(form);
		dynamicForms.add(howOftenField);
		dynamicForms.add(dueField);

		VerticalPanel layout = new VerticalPanel();
		layout.add(form);
		layout.add(howOftenField);
		layout.add(dueField);
		setBodyLayout(layout);

		if (!isNew()) {
			updateValuesFormObject();
		}
	}

	private void updateValuesFormObject() {
		nameField.setValue(existingTransaction.getName());
		startDateField.setValue(new ClientFinanceDate(existingTransaction
				.getStartDate()));
		endDateField.setValue(new ClientFinanceDate(existingTransaction
				.getEndDate()));

		actionComboField.setSelected(getActionOptions().get(
				existingTransaction.getActionType()));

		howOftenField.setTextFieldValue(String.valueOf(existingTransaction
				.getHowOftenValue()));
		howOftenField.setSelectedOption(getHowOfenOptions().get(
				existingTransaction.getHowOftenType()));

		dueField.setTextFieldValue(String.valueOf(existingTransaction
				.getDueDateValue()));
		dueField.setSelectedOption(getDueDateOptions().get(
				existingTransaction.getDueDateType()));
	}

	@Override
	protected boolean onOK() {

		ClientRecurringTransaction recurringTransaction = existingTransaction;
		if (recurringTransaction == null) {
			recurringTransaction = new ClientRecurringTransaction();
		}
		updateValuesFormFields(recurringTransaction);
		saveOrUpdate(recurringTransaction);

		return true;
	}

	/**
	 * Assigns values to object from fields.
	 * 
	 * @param recTransaction
	 */
	private void updateValuesFormFields(
			ClientRecurringTransaction recTransaction) {
		recTransaction.setName(nameField.getValue());
		recTransaction.setStartDate(startDateField.getTime());
		recTransaction.setEndDate(endDateField.getTime());

		// Action type
		int index = getActionOptions().indexOf(
				actionComboField.getSelectedValue());
		recTransaction.setActionType(index);

		// due date type
		index = getDueDateOptions().indexOf(dueField.getSelectedOption());
		recTransaction.setDueDateType(index);
		recTransaction
				.setDueDateValue(Integer.parseInt(dueField.getTextValue()));

		// how often type
		index = getHowOfenOptions().indexOf(howOftenField.getSelectedOption());
		recTransaction.setHowOftenType(index);
		recTransaction.setHowOftenValue(Integer.parseInt(howOftenField
				.getTextValue()));

		if (isNew()) {
			recTransaction.setReferringTransaction(parentView
					.getTransactionObject().getID());
		}
	}

	/**
	 * A custom component for displaying textbox followed by comboitem.
	 * 
	 * @author vimukti3
	 * 
	 */
	private class TextAndComboPairForm extends DynamicForm {
		private TextItem textItem;
		private SelectCombo combo;

		public TextAndComboPairForm(String title, List<String> comboOptions) {
			setGroupTitle(title);
			setNumCols(4);
			textItem = new TextItem(title);
			textItem.setRequired(true);

			combo = new SelectCombo("");
			combo.initCombo(comboOptions);
			setFields(textItem, combo);
		}

		public String getTextValue() {
			return textItem.getValue();
		}

		public void setTextFieldValue(String value) {
			textItem.setValue(value);
		}

		public void setSelectedOption(String option) {
			combo.setSelected(option);
		}

		public String getSelectedOption() {
			return combo.getSelectedValue();
		}		
	}

	private boolean isNew() {
		return existingTransaction == null;
	}

	private List<String> getDueDateOptions() {
		List<String> options = new ArrayList<String>();
		options.add("day(s) after the invoice date");
		options.add("of the following month");
		options.add("of the current month");
		return options;
	}

	private List<String> getActionOptions() {
		List<String> options = new ArrayList<String>();
		options.add("Save as draft");
		options.add("Approve");
		options.add("Approve and send");
		return options;
	}

	private List<String> getHowOfenOptions() {
		List<String> options = new ArrayList<String>();
		options.add("day(s)");
		options.add("week(s)");
		options.add("month(s)");
		return options;

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult validate = super.validate();

		for (DynamicForm form : dynamicForms) {
			validate.add(form.validate());
		}

		return validate;
	}
}
