package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class RecurringTransactionDialog extends
		BaseDialog<ClientRecurringTransaction> {

	private final static AccounterConstants CONSTANTS = Accounter.constants();
	private final static String OPTIONAL_DUE_DATE_OPTION = CONSTANTS.ofTheCurrentMonth();

	private TextItem nameField;
	private DateItem startDateField;
	private DateItem endDateField;
	private TextItem occurrencesField;
	private TextItem daysInAdvanceField;

	private TextAndComboPairForm dueField;

	private IntervalValueInputField intervalValueField;

	private ClientRecurringTransaction existingTransaction;

	private AbstractTransactionBaseView<? extends ClientTransaction> parentView;

	private List<DynamicForm> dynamicForms;

	private VerticalPanel intervalLayout;
	private DynamicForm endDateTypeForm;

	private SelectCombo actionComboField;
	private SelectCombo dayOfWeekCombo;
	private SelectCombo dayOfMonthCombo;
	private SelectCombo weekOfMonthCombo;
	private SelectCombo monthOfYearCombo;
	private SelectCombo intervalTypeCombo;
	private SelectCombo recurringTypeCombo;
	private SelectCombo endDateTypeCombo;

	private RadioButton onSpecificDayRadioBtn;
	private RadioButton onSpecificWeekRadioBtn;

	private CheckboxItem unbilledChargesCkBox;

	private Panel mainLayout;
	
	
	
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
		super(CONSTANTS.recurring(), CONSTANTS.recurringDescription());
		this.parentView = parentView;
		existingTransaction = transaction;
		createControls();
		center();
	}

	private void createControls() {
		mainPanel.setSpacing(15);
		initCombos();
		intervalValueField = new IntervalValueInputField();
		endDateTypeForm = new DynamicForm();

		unbilledChargesCkBox = new CheckboxItem(CONSTANTS.includeUnbilledCharges());

		initRadioBtns();

		recurringTypeCombo
				.addSelectionChangeHandler(new RecurringTypeChangeHandler());

		intervalTypeCombo
				.addSelectionChangeHandler(new IntervalTypeChangeHandler());

		endDateTypeCombo
				.addSelectionChangeHandler(new EndDateTypeChangeHandler());

		dynamicForms = new ArrayList<DynamicForm>();

		DynamicForm form = new DynamicForm();

		nameField = new TextItem(Accounter.constants().name());
		nameField.setRequired(true);
		nameField.setHelpInformation(true);

		daysInAdvanceField = new TextItem(Accounter.constants().daysInAdvance());

		occurrencesField = new TextItem(Accounter.constants().endAfterSpecifiedOccurences());
		occurrencesField.setRequired(false);
		nameField.setHelpInformation(true);

		startDateField = new DateItem(Accounter.constants().startDate());
		startDateField.setRequired(true);
		startDateField.setHelpInformation(true);

		endDateField = new DateItem(Accounter.constants().endDate());
		endDateField.setHelpInformation(true);

		actionComboField = new SelectCombo(Accounter.constants().action());
		actionComboField.initCombo(getActionOptions());
		actionComboField.setRequired(true);
		actionComboField.setHelpInformation(true);

		dueField = new TextAndComboPairForm(Accounter.constants().due(), getDueDateOptions(true));

		form.setFields(recurringTypeCombo, nameField, startDateField,
				actionComboField, daysInAdvanceField, unbilledChargesCkBox,
				intervalTypeCombo);

		dynamicForms.add(form);

		intervalLayout = new VerticalPanel();
		//intervalLayout.setBorderWidth(1);

		mainLayout = new VerticalPanel();
		mainLayout.add(form);
		mainLayout.add(intervalLayout);
		endDateTypeForm.setFields(endDateTypeCombo);
		mainLayout.add(endDateTypeForm);

		if (canIncludeDueDateSection()) {
			dynamicForms.add(dueField);
			mainLayout.add(dueField);
		}

		// --------

		setBodyLayout(mainLayout);

		if (!isNew()) {
			updateValuesFormObject();
		}
	}

	private void initRadioBtns() {
		onSpecificWeekRadioBtn = new RadioButton(Accounter.constants().monthly(), Accounter.constants().onSpecificWeek());
		onSpecificWeekRadioBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// disable
				dayOfMonthCombo.setDisabled(true);

				// enable
				dayOfWeekCombo.setDisabled(false);
				weekOfMonthCombo.setDisabled(false);

				// hide "due date of the current month" option.
				hideOptionalDueDateOption();
			}
		});

		onSpecificDayRadioBtn = new RadioButton(Accounter.constants().monthly(),Accounter.constants().onSpecificDay());
		onSpecificDayRadioBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// disable
				weekOfMonthCombo.setDisabled(true);
				dayOfWeekCombo.setDisabled(true);

				// enable
				dayOfMonthCombo.setDisabled(false);

				// show "due date of the current month" option.
				showOptionalDueDateOption();
			}
		});
	}

	private void hideOptionalDueDateOption() {
		dueField.initCombo(getDueDateOptions(false));

	}

	private void showOptionalDueDateOption() {
		dueField.initCombo(getDueDateOptions(true));
	}

	private void enableAllCombos() {
		dayOfMonthCombo.setDisabled(false);
		dayOfWeekCombo.setDisabled(false);
		weekOfMonthCombo.setDisabled(false);
	}

	private class EndDateTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectItem) {

			int selected = getEndDateTypeOptions().indexOf(selectItem);

			endDateTypeForm.clear();

			switch (selected) {
			case 0:// no end date
				endDateTypeForm.setFields(endDateTypeCombo);
				break;
			case 1:// end after __ occurrences
				endDateTypeForm.setFields(endDateTypeCombo, occurrencesField);

				break;
			case 2: // stop after date
				endDateTypeForm.setFields(endDateTypeCombo, endDateField);
			default:
				break;
			}
		}
	}

	private class RecurringTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectItem) {
			int selected = getRecurringTypeOptions().indexOf(selectItem);
			if (selected == 2) {// none-just template
				// disable rest of fields
				changeFieldsStausForNoneSelection(true);
			} else {
				// schedule or remainder
				// enable rest of fields
				changeFieldsStausForNoneSelection(false);
			}

		}

	}

	private class IntervalTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectedValue) {
			int selected = getIntervalTypeOptions().indexOf(selectedValue);
			CellPanel panel = null;
			showOptionalDueDateOption();

			switch (selected) {
			case 0:// Daily
				panel = getDailyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(Accounter.constants().days());
				break;
			case 1: // weekly
				enableAllCombos();
				panel = getWeeklyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(Accounter.constants().weeks());

				// need to hide the option "due date of the currenct month" in
				// DueField.
				hideOptionalDueDateOption();
				break;
			case 2: // monthly
				panel = getMonthlyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(Accounter.constants().months());

				if (onSpecificWeekRadioBtn.getValue()) {
					hideOptionalDueDateOption();
				}

				break;
			case 3: // yearly
				enableAllCombos();
				panel = getYearlyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(Accounter.constants().years());
				break;
			}

			intervalLayout.clear();

			if (panel != null) {
				intervalLayout.add(panel);
			}

			// set default value if not shown.
			if (intervalValueField.getIngervalValue() == 0) {
				intervalValueField.setIntervalValue(1);
			}

		}
	}

	private void initCombos() {
		recurringTypeCombo = createCombo(CONSTANTS.recurringType(),
				getRecurringTypeOptions());
		recurringTypeCombo.setRequired(true);

		dayOfWeekCombo = createCombo(CONSTANTS.dayOfWeek(), getWeekOptions());
		dayOfMonthCombo = createCombo(CONSTANTS.dayOfMonth(), getMonthDayOptions());
		weekOfMonthCombo = createCombo(CONSTANTS.weekOfMonth(), getMonthWeekOptions());
		monthOfYearCombo = createCombo(CONSTANTS.month(), getMonthOptions());
		intervalTypeCombo = createCombo(CONSTANTS.intervalType(),
				getIntervalTypeOptions());
		intervalTypeCombo.setRequired(true);
		endDateTypeCombo = createCombo(CONSTANTS.endDateType(), getEndDateTypeOptions());
		endDateTypeCombo.setRequired(true);
	}

	private List<String> getEndDateTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.noEndDate());
		options.add(CONSTANTS.endAfterOccurrences());
		options.add(CONSTANTS.endDateAfter());
		return options;
	}

	private List<String> getRecurringTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.schedule());
		options.add(CONSTANTS.remainder());
		options.add(CONSTANTS.noneJustTemplate());
		return options;
	}

	private List<String> getMonthOptions() {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.january());
		options.add(CONSTANTS.february());
		options.add(CONSTANTS.march());
		options.add(CONSTANTS.april());
		options.add(CONSTANTS.may());
		options.add(CONSTANTS.june());
		options.add(CONSTANTS.july());
		options.add(CONSTANTS.august());
		options.add(CONSTANTS.september());
		options.add(CONSTANTS.october());
		options.add(CONSTANTS.november());
		options.add(CONSTANTS.december());
		return options;
	}

	private List<String> getMonthWeekOptions() {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.first());
		options.add(CONSTANTS.second());
		options.add(CONSTANTS.third());
		options.add(CONSTANTS.fourth());
		options.add(CONSTANTS.last());
		return options;
	}

	private List<String> getMonthDayOptions() {
		List<String> days = new ArrayList<String>();
		for (int i = 1; i <= 31; i++) {
			days.add(String.valueOf(i));
		}
		return days;
	}

	private List<String> getWeekOptions() {
		List<String> weeks = new ArrayList<String>();
		weeks.add(CONSTANTS.sunday());
		weeks.add(CONSTANTS.monday());
		weeks.add(CONSTANTS.tuesday());
		weeks.add(CONSTANTS.wednesday());
		weeks.add(CONSTANTS.thursday());
		weeks.add(CONSTANTS.friday());
		weeks.add(CONSTANTS.saturday());
		return weeks;
	}

	private SelectCombo createCombo(String text, List<String> optoins) {
		SelectCombo combo = new SelectCombo(text);
		combo.initCombo(optoins);
		combo.setHelpInformation(true);
		return combo;
	}

	private CellPanel getDailyIntervalLayout() {
		VerticalPanel panel = new VerticalPanel();
		panel.add(intervalValueField);
		return panel;
	}

	private CellPanel getWeeklyIntervalLayout() {
		VerticalPanel panel = new VerticalPanel();
		panel.add(intervalValueField);
		panel.add(dayOfWeekCombo.getMainWidget());
		return panel;
	}

	private CellPanel getMonthlyIntervalLayout() {
		VerticalPanel panel = new VerticalPanel();
		panel.add(intervalValueField);
		panel.add(onSpecificDayRadioBtn);
		panel.add(dayOfMonthCombo.getMainWidget());
		panel.add(onSpecificWeekRadioBtn);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(weekOfMonthCombo.getMainWidget());
		hPanel.add(dayOfWeekCombo.getMainWidget());
		panel.add(hPanel);

		return panel;
	}

	private CellPanel getYearlyIntervalLayout() {
		VerticalPanel panel = new VerticalPanel();		
		panel.add(intervalValueField);

		HorizontalPanel tempPanel = new HorizontalPanel();
		tempPanel.add(monthOfYearCombo.getMainWidget());
		tempPanel.add(dayOfMonthCombo.getMainWidget());

		panel.add(tempPanel);
		return panel;
	}

	private void updateValuesFormObject() {

		recurringTypeCombo.setSelectedItem(existingTransaction.getType());
		nameField.setValue(existingTransaction.getName());
		startDateField.setValue(new ClientFinanceDate(existingTransaction
				.getStartDate()));
		if (canIncludeDueDateSection()) {
			dueField.setTextFieldValue(String.valueOf(existingTransaction
					.getDueDateValue()));
			dueField.setSelectedOption(getDueDateOptions(true).get(
					existingTransaction.getDueDateType()));
		}
		unbilledChargesCkBox.setValue(existingTransaction
				.isUnbilledChargesEnabled());
		daysInAdvanceField.setValue(String.valueOf(existingTransaction
				.getDaysInAdvanceToCreate()));

		actionComboField.setSelected(getActionOptions().get(
				existingTransaction.getActionType()));

		// Interval fields
		updateIntervalValueFieldsFormObject();

		// end day fields
		updateEndDateVaulesFromObject();

	}

	private void updateEndDateVaulesFromObject() {
		int endDateType = existingTransaction.getEndDateType();
		endDateTypeCombo.setSelectedItem(endDateType);
		switch (endDateType) {
		case 0: // no end date
			break;
		case 1: // after __ occurrences
			occurrencesField.setValue(String.valueOf(existingTransaction
					.getOccurencesCount()));
			break;
		case 2: // after date
			endDateField.setValue(new ClientFinanceDate(existingTransaction
					.getEndDate()));
			break;
		default:
			break;
		}

		// manually invoke event on field to update UI
		endDateTypeCombo.getHandler().selectedComboBoxItem(
				endDateTypeCombo.getSelectedValue());
	}

	private void updateIntervalValueFieldsFormObject() {
		int intervalType = existingTransaction.getIntervalType();
		intervalValueField.setIntervalValue(existingTransaction
				.getIntervalPeriod());

		switch (intervalType) {
		case 0:// daily
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(0));

			break;
		case 1: // weekly
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(1));
			dayOfWeekCombo.setSelectedItem(existingTransaction.getWeekDay());
			break;
		case 2: // monthly_day
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(2));
			onSpecificDayRadioBtn.setValue(true);

			dayOfMonthCombo
					.setSelectedItem(existingTransaction.getDayOfMonth());
			break;
		case 3: // monthly_week
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(2));
			onSpecificWeekRadioBtn.setValue(true);

			weekOfMonthCombo.setSelectedItem(existingTransaction
					.getWeekOfMonth());
			dayOfWeekCombo.setSelectedItem(existingTransaction.getWeekDay());
			break;
		case 4: // yearly
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(3));

			dayOfMonthCombo
					.setSelectedItem(existingTransaction.getDayOfMonth());
			monthOfYearCombo.setSelectedItem(existingTransaction.getMonth());
			break;
		default:
			break;
		}

		// manually invoke event on field to update UI
		intervalTypeCombo.getHandler().selectedComboBoxItem(
				intervalTypeCombo.getSelectedValue());
	}

	@Override
	protected boolean onOK() {

		ClientRecurringTransaction recurringTransaction = existingTransaction;
		if (recurringTransaction == null) {
			recurringTransaction = new ClientRecurringTransaction();
		}
		updateValuesFromFields(recurringTransaction);
		saveOrUpdate(recurringTransaction);

		return true;
	}

	/**
	 * Assigns values to object from fields.
	 * 
	 * @param recTransaction
	 */
	private void updateValuesFromFields(
			ClientRecurringTransaction recTransaction) {
		// common fields for all type of intervaltypes
		int index;

		index = getRecurringTypeOptions().indexOf(
				recurringTypeCombo.getSelectedValue());
		recTransaction.setType(index);
		recTransaction.setName(nameField.getValue());
		recTransaction.setStartDate(startDateField.getTime());
		recTransaction.setEndDate(endDateField.getTime());
		// due date
		if (canIncludeDueDateSection()) {
			index = getDueDateOptions(true).indexOf(
					dueField.getSelectedOption());
			recTransaction.setDueDateType(index);
			recTransaction.setDueDateValue(Integer.parseInt(dueField
					.getTextValue()));
		}
		recTransaction.setUnbilledChargesEnabled(unbilledChargesCkBox
				.isChecked());
		try {
			recTransaction.setDaysInAdvanceToCreate(Integer
					.parseInt(daysInAdvanceField.getValue()));
		} catch (NumberFormatException e) {
			// as this field is optional user may not enter value
			recTransaction.setDaysInAdvanceToCreate(0);
		}
		// Action type
		index = getActionOptions().indexOf(actionComboField.getSelectedValue());
		recTransaction.setActionType(index);

		// interval type
		index = getIntervalTypeOptions().indexOf(
				intervalTypeCombo.getSelectedValue());
		updateIntervalFromFields(index, recTransaction);

		// end day type
		index = getEndDateTypeOptions().indexOf(
				endDateTypeCombo.getSelectedValue());
		recTransaction.setEndDateType(index);
		updateEndDayValuesFromFields(index, recTransaction);

		if (isNew()) {
			recTransaction.setReferringTransaction(parentView
					.getTransactionObject().getID());
		}
	}

	private boolean canIncludeDueDateSection() {
		// we can include due date options only when it is invoice or baybill.
		int transactionType;
		if (isNew()) {
			transactionType = parentView.getTransactionObject().getType();
		} else {
			transactionType = existingTransaction.getType();
		}

		return transactionType == ClientTransaction.TYPE_INVOICE
				|| transactionType == ClientTransaction.TYPE_PAY_BILL;
	}

	private void updateEndDayValuesFromFields(int index,
			ClientRecurringTransaction recTransaction) {
		switch (index) {
		case 0:// no end date
			break;
		case 1:// after __ occurrences
			recTransaction.setOccurencesCount(Integer.parseInt(occurrencesField
					.getValue()));
			break;
		case 2:// no end date
			recTransaction.setEndDate(endDateField.getTime());
			break;
		default:
			break;
		}
	}

	private void updateIntervalFromFields(int index,
			ClientRecurringTransaction recTransaction) {

		recTransaction.setIntervalPeriod(intervalValueField.getIngervalValue());
		switch (index) {
		case 0:// daily
			recTransaction
					.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_DAILY);
			break;
		case 1:// Weekly
			recTransaction
					.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_WEEKLY);
			recTransaction.setWeekDay(getWeekOptions().indexOf(
					weekOfMonthCombo.getSelectedValue()));
			break;
		case 2:// monthly
			if (onSpecificDayRadioBtn.getValue()) {
				recTransaction
						.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_MONTHLY_DAY);
				// need only date
				recTransaction.setDayOfMonth(Integer.parseInt(dayOfMonthCombo
						.getSelectedValue()));

			} else if (onSpecificWeekRadioBtn.getValue()) {
				recTransaction
						.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_MONTHLY_WEEK);
				// need selected nth week, and selected week
				recTransaction.setWeekDay(getWeekOptions().indexOf(
						weekOfMonthCombo.getSelectedValue()));

				recTransaction.setWeekOfMonth(getMonthWeekOptions().indexOf(
						weekOfMonthCombo.getSelectedValue()));
			}

			break;
		case 3:// yearly
			recTransaction.setDayOfMonth(Integer.parseInt(dayOfMonthCombo
					.getSelectedValue()));
			recTransaction.setMonth(getMonthOptions().indexOf(
					monthOfYearCombo.getSelectedValue()));
			break;
		default:
			break;
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

		public void initCombo(List<String> list) {
			combo.initCombo(list);
		}

	}

	private boolean isNew() {
		return existingTransaction == null;
	}

	private List<String> getDueDateOptions(boolean optinalRequired) {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.daysAfterTheInvoiceDate());
		options.add(CONSTANTS.ofTheFollowingMonth());
		if (optinalRequired) {
			options.add(OPTIONAL_DUE_DATE_OPTION);
		}
		return options;
	}

	private List<String> getActionOptions() {
		List<String> options = new ArrayList<String>();
		options.add("Save as draft");
		options.add("Approve");
		options.add("Approve and send");
		return options;
	}

	private List<String> getIntervalTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(CONSTANTS.daily());
		options.add(CONSTANTS.weekly());
		options.add(CONSTANTS.monthly());
		options.add(CONSTANTS.yearly());
		return options;
	}

	@Override
	protected ValidationResult validate() {
		if (recurringTypeCombo.getSelectedIndex() == 2) {// none-just template
			return FormItem.validate(nameField);
		}

		ValidationResult validate = super.validate();
		for (DynamicForm form : dynamicForms) {
			validate.add(form.validate());
		}
		// TODO need to add more validations based on intervalType and based on
		// endDateType

		endDateValidations(validate);

		return validate;
	}

	private void endDateValidations(ValidationResult validate) {
		if (dueField.getSelectedOption().equals(OPTIONAL_DUE_DATE_OPTION)) {
			// the due date cannot precede the transaction date here.

			int dueDay;
			try {
				dueDay = Integer.parseInt(dueField.getTextValue());
			} catch (NumberFormatException e) {
				dueDay = 0;
				e.printStackTrace();
			}

			int transactionDay;

			if (intervalTypeCombo.getSelectedValue().equals(CONSTANTS.daily())) {
				transactionDay = startDateField.getDay();
			} else {
				// The interval type either Month_day or yearly.
				transactionDay = Integer.parseInt(dayOfMonthCombo
						.getSelectedValue());
			}

			if (dueDay < transactionDay) {
				// add validation error
				// "The due date can not precede the transaction date"

				ValidationResult validationResult = new ValidationResult();
				validationResult.addError(this,
						"The due date can not precede the transaction date");
				validate.add(validationResult);
			}
		}
	}

	/**
	 * Interval value field. <br>
	 * <br>
	 * Shows like "every [] days/weeks/months"
	 * 
	 * @author vimukti3
	 * 
	 */
	private class IntervalValueInputField extends DynamicForm {
		private TextItem intervalValueField;
		private LabelItem intervalTypeLabel;

		public IntervalValueInputField() {
			// setGroupTitle(title);
			setNumCols(4);
			intervalValueField = new TextItem(CONSTANTS.every());
			intervalValueField.setRequired(true);
			intervalTypeLabel = new LabelItem();
			setFields(intervalValueField, intervalTypeLabel);
		}

		public void setIntervalValue(int intervalValue) {
			intervalValueField.setValue(String.valueOf(intervalValue));
		}

		public int getIngervalValue() {
			try {
				return Integer.parseInt(intervalValueField.getValue());
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		public void setIntervalTypeLabel(String intervalType) {
			intervalTypeLabel.setValue(intervalType);
		}
	}

	private void changeFieldsStausForNoneSelection(boolean selected) {
		startDateField.setDisabled(selected);
		endDateField.setDisabled(selected);
		occurrencesField.setDisabled(selected);
		daysInAdvanceField.setDisabled(selected);
		intervalValueField.setDisabled(selected);
		endDateTypeForm.setDisabled(selected);
		actionComboField.setDisabled(selected);
		dayOfWeekCombo.setDisabled(selected);
		dayOfMonthCombo.setDisabled(selected);
		weekOfMonthCombo.setDisabled(selected);
		monthOfYearCombo.setDisabled(selected);
		intervalTypeCombo.setDisabled(selected);
		endDateTypeCombo.setDisabled(selected);
		onSpecificDayRadioBtn.setEnabled(!selected);
		onSpecificWeekRadioBtn.setEnabled(!selected);
		unbilledChargesCkBox.setDisabled(selected);
		dueField.setDisabled(selected);
	}
}
