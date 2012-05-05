package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientEmployeePaymentDetails;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.ValidationResult.Error;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.reports.ReportGrid;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class NewPayRunView extends AbstractTransactionBaseView<ClientPayRun> {

	private DateField fromDate, toDate;
	private EmployeesAndGroupsCombo empsAndGroups;
	private ScrollPanel tableLayout;
	protected int sectionDepth = 0;

	private AttendanceManagementTable table;

	IntegerField noOfWorkingDays;

	private final List<Section<ClientEmployeePayHeadComponent>> sections = new ArrayList<Section<ClientEmployeePayHeadComponent>>();
	protected ArrayList<ClientPayHead> payheads = new ArrayList<ClientPayHead>();

	public NewPayRunView() {
		super(ClientTransaction.TYPE_PAY_RUN);
		this.getElement().setId("NewPayRunView");
	}

	@Override
	public void init() {
		super.init();
		setSize("100%", "100%");
		initPayheadList();
	}

	private void initPayheadList() {
		Accounter.createPayrollService().getPayheads(0, -1,
				new AsyncCallback<PaginationList<ClientPayHead>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PaginationList<ClientPayHead> result) {
						payheads = result;
					}
				});

	}

	Button button;
	private ReportGrid<ClientEmployeePayHeadComponent> reportGrid;
	private List<ClientEmployeePayHeadComponent> records = new ArrayList<ClientEmployeePayHeadComponent>();
	private StyledPanel attendanceLay;
	private DynamicForm attendanceForm;
	private Button nextButton, backButton;
	private DynamicForm tableForm;
	private DynamicForm dateForm;

	@Override
	protected void createControls() {
		Label lab1 = new Label(messages.payrun());
		lab1.setStyleName("label-title");

		DynamicForm dateNoForm = new DynamicForm("dateNoForm");

		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		noOfWorkingDays = new IntegerField(this, messages.noOfWorkingDays());
		noOfWorkingDays.setEnabled(!isInViewMode());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new ClientFinanceDate().getDateAsObject());
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		noOfWorkingDays.setValue("" + actualMaximum);

		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.add(noOfWorkingDays);

		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		// ---date--
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);

		empsAndGroups = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"empsAndGroups");
		empsAndGroups
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayStructureDestination selectItem) {
						table.updateList(selectItem);
						selectionChanged();
					}
				});
		empsAndGroups.setEnabled(!isInViewMode());

		button = new Button(messages.update());

		fromDate = new DateField(messages.fromDate(), "fromDate");

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate startDate = new ClientFinanceDate(date.getYear(),
				date.getMonth(), 1);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(new ClientFinanceDate().getDateAsObject());
		endCal.set(Calendar.DAY_OF_MONTH,
				endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate endDate = new ClientFinanceDate(endCal.getTime());

		fromDate.setEnteredDate(startDate);
		fromDate.setEnabled(false);
		fromDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				button.setEnabled(NewPayRunView.this.empsAndGroups
						.getSelectedValue() != null);
			}
		});

		toDate = new DateField(messages.toDate(), "toDate");
		toDate.setEnteredDate(endDate);
		toDate.setEnabled(false);
		toDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				button.setEnabled(NewPayRunView.this.empsAndGroups
						.getSelectedValue() != null);
			}
		});
		dateForm = new DynamicForm("dateForm");

		dateForm.add(empsAndGroups);
		dateForm.add(fromDate);
		dateForm.add(toDate);

		button.setEnabled(false);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectionChanged();
				showPayRunTable();
			}
		});

		table = new AttendanceManagementTable();
		table.setEnabled(!isInViewMode());

		nextButton = new Button(messages.next());
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPayRunTable();
			}
		});

		attendanceLay = new StyledPanel("mainVLay");
		attendanceLay.add(lab1);
		attendanceLay.add(table);
		attendanceLay.add(nextButton);

		attendanceForm = new DynamicForm("attendanceForm");
		attendanceForm.add(attendanceLay);

		this.tableLayout = new ScrollPanel();
		this.tableLayout.addStyleName("tableLayout");

		reportGrid = new ReportGrid<ClientEmployeePayHeadComponent>(
				getColumns(), true);
		reportGrid.setColumnTypes(getColumnTypes());
		reportGrid.init();
		reportGrid.addEmptyMessage(messages.noRecordsToShow());

		backButton = new Button(messages.back());
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectionChanged();
			}
		});

		tableLayout.add(reportGrid);

		tableForm = new DynamicForm("tableForm");

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(dateForm);
		mainVLay.add(button);
		mainVLay.add(attendanceForm);
		mainVLay.add(tableForm);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		this.add(mainVLay);
	}

	protected void itemSelected() {
		Set<ClientEmployeePaymentDetails> payEmployee = data.getPayEmployee();
		boolean isEmployee = data.getEmployee() == 0 ? false : true;
		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		ArrayList<ClientEmployeePayHeadComponent> records = new ArrayList<ClientEmployeePayHeadComponent>();
		if (selectedValue != null) {
			for (ClientEmployeePaymentDetails clientEmployeePaymentDetails : payEmployee) {
				Set<ClientEmployeePayHeadComponent> payHeadComponents = clientEmployeePaymentDetails
						.getPayHeadComponents();
				for (ClientEmployeePayHeadComponent comp : payHeadComponents) {
					if (isEmployee) {
						comp.setEmployee(selectedValue.getName());
					} else {
						ClientEmployeeGroup group = (ClientEmployeeGroup) selectedValue;
						for (ClientEmployee employee : group.getEmployees()) {
							if (employee.getID() == clientEmployeePaymentDetails
									.getEmployee()) {
								comp.setEmployee(employee.getName());
							}
						}
					}
				}
				records.addAll(payHeadComponents);
			}
		}
		initRecords(records);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult validate = super.validate();
		if (data.getPayEmployee() == null || data.getPayEmployee().isEmpty()) {
			validate.addError(reportGrid, messages.noRecordsSelected());
		}
		return validate;
	}

	@Override
	protected boolean needTransactionItems() {
		return false;
	}

	protected void showPayRunTable() {
		if (isInViewMode()) {
			reportGrid.removeAllRows();
			attendanceForm.clear();
			tableForm.add(tableLayout);
			tableForm.add(backButton);
			itemSelected();
			return;
		}
		ValidationResult result = validateAttendance();
		if (result.haveErrors()) {
			for (Error error : result.getErrors()) {
				addError(error.getSource(), error.getMessage());
				lastErrorSourcesFromValidation.add(error.getSource());
			}
			return;
		}

		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue == null) {
			return;
		}

		if (selectedValue instanceof ClientEmployee) {
			data.setEmployee(selectedValue.getID());
		} else {
			data.setEmployeeGroup(selectedValue.getID());
		}

		if (saveAndCloseButton != null) {
			saveAndCloseButton.setVisible(isSaveButtonAllowed());
		}

		if (saveAndNewButton != null) {
			saveAndNewButton.setVisible(isSaveButtonAllowed());
		}
		button.setEnabled(isInViewMode());

		fromDate.setEnabled(!isInViewMode());
		toDate.setEnabled(!isInViewMode());

		reportGrid.removeAllRows();
		attendanceForm.clear();
		tableForm.add(tableLayout);
		tableForm.add(backButton);

		data.setAttendanceItems(table.getAllRows());

		AsyncCallback<ArrayList<ClientEmployeePayHeadComponent>> callback = new AsyncCallback<ArrayList<ClientEmployeePayHeadComponent>>() {

			@Override
			public void onFailure(Throwable caught) {
				return;
			}

			@Override
			public void onSuccess(
					ArrayList<ClientEmployeePayHeadComponent> result) {
				if (result == null)
					onFailure(new Exception());

				try {
					NewPayRunView.this.records = result;
					if (result != null && result.size() > 0) {
						reportGrid.removeAllRows();
						refreshMakeDetailLayout();
						setFromAndToDate(result);
						initRecords(result);
					} else {
						reportGrid.removeAllRows();

						if (result != null && result.size() == 1)
							setFromAndToDate(result);

						reportGrid.addEmptyMessage(messages.noRecordsToShow());
					}

					showRecords();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		Accounter.createPayrollService().getEmployeePayHeadComponents(
				table.getAllRows(), selectedValue, fromDate.getDate(),
				toDate.getDate(), noOfWorkingDays.getNumber(), callback);
	}

	private ValidationResult validateAttendance() {
		for (Object errorSource : lastErrorSourcesFromValidation) {
			clearError(errorSource);
		}
		lastErrorSourcesFromValidation.clear();

		ValidationResult validationResult = new ValidationResult();

		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue == null) {
			validationResult.addError(empsAndGroups,
					messages.pleaseSelect(messages.employeeOrGroup()));
		}

		return validationResult;
	}

	protected void addItem() {
		ClientAttendanceManagementItem transactionItem = new ClientAttendanceManagementItem();
		table.add(transactionItem);
	}

	private int[] getColumnTypes() {
		return new int[] { 1, 2, 1, 1, 1, 1 };
	}

	private String[] getColumns() {
		return new String[] { messages.payhead(), messages.rate(),
				messages.calculationPeriod(), messages.payHeadType(),
				messages.calculationType(), messages.computedOn() };
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayRun());
		} else {
			initViewData(getData());
		}

		initTransactionNumber();

		super.initData();
	}

	@Override
	protected void updateTransaction() {
		updateData();
	}

	private void initViewData(ClientPayRun data) {
		long payPeriodEndDate = data.getPayPeriodEndDate();
		long payPeriodStartDate = data.getPayPeriodStartDate();
		fromDate.setDateWithNoEvent(new ClientFinanceDate(payPeriodStartDate));
		toDate.setDateWithNoEvent(new ClientFinanceDate(payPeriodEndDate));
		empsAndGroups.setEmpGroup(
				data.getEmployee() == 0 ? data.getEmployeeGroup() : data
						.getEmployee(), data.getEmployee() != 0);
		table.setData(data.getAttendanceItems());
		noOfWorkingDays.setValue(data.getNoOfWorkingDays());
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		super.updateTransaction();
		data.setNoOfWorkingDays(noOfWorkingDays.getValue());
		data.setPayPeriodStartDate(fromDate.getDate().getDate());
		data.setPayPeriodEndDate(toDate.getDate().getDate());
		data.setTransactionDate(new ClientFinanceDate().getDate());
		Set<ClientEmployeePaymentDetails> details = new HashSet<ClientEmployeePaymentDetails>();

		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();

		if (selectedValue instanceof ClientEmployee) {
			ClientEmployeePaymentDetails employeePaymentDetails = new ClientEmployeePaymentDetails();
			employeePaymentDetails.setEmployee(selectedValue.getID());
			employeePaymentDetails
					.setPayHeadComponents(new HashSet<ClientEmployeePayHeadComponent>(
							records));
			if (!records.isEmpty()) {
				details.add(employeePaymentDetails);
			}
			data.setEmployee(selectedValue.getID());
		} else {
			ClientEmployeeGroup group = (ClientEmployeeGroup) selectedValue;
			data.setEmployeeGroup(selectedValue.getID());
			for (ClientEmployee employee : group.getEmployees()) {
				ClientEmployeePaymentDetails employeePaymentDetails = new ClientEmployeePaymentDetails();
				employeePaymentDetails.setEmployee(employee.getID());
				Set<ClientEmployeePayHeadComponent> empRecords = getFilteredRecords(employee);
				employeePaymentDetails.setPayHeadComponents(empRecords);
				if (!empRecords.isEmpty()) {
					details.add(employeePaymentDetails);
				}
			}
		}
		data.setCurrency(getCompany().getPrimaryCurrency().getID());
		data.setCurrencyFactor(1.0);
		data.setPayEmployee(details);
	}

	private Set<ClientEmployeePayHeadComponent> getFilteredRecords(
			ClientEmployee employee) {
		Set<ClientEmployeePayHeadComponent> empRecords = new HashSet<ClientEmployeePayHeadComponent>();
		for (ClientEmployeePayHeadComponent record : records) {
			if (record.getName().equals(employee.getName())) {
				empRecords.add(record);
			}
		}
		return empRecords;
	}

	protected void selectionChanged() {
		tableForm.clear();
		fromDate.setEnabled(false);
		toDate.setEnabled(false);
		button.setEnabled(false);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		attendanceForm.clear();
		tableForm.clear();
		table.clear();
		boolean isEmployee = data.getEmployee() == 0 ? false : true;

		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();

		if ((selectedValue instanceof ClientEmployee && isEmployee && data
				.getEmployee() == selectedValue.getID())
				|| (selectedValue instanceof ClientEmployeeGroup && !isEmployee && data
						.getEmployeeGroup() == selectedValue.getID())) {
			table.setAllRows(data.getAttendanceItems());
		} else {
			table.setSelectedEmployeeOrGroup(selectedValue);
			// table.addEmptyRecords();
		}

		attendanceForm.add(attendanceLay);
	}

	protected void showRecords() {
		try {
			if (UIUtils.isMSIEBrowser()) {
				this.tableLayout.setStyleName("tablelayoutInIE1");
			} else {
				this.tableLayout.setStyleName("tablelayout1");
			}

			AccounterDOM.addStyleToparent(this.reportGrid.getElement(),
					"reportGridParent");
		} catch (Exception e) {
		}
	}

	protected void initRecords(ArrayList<ClientEmployeePayHeadComponent> records) {
		reportGrid.removeAllRows();
		this.records = records;

		for (ClientEmployeePayHeadComponent record : records) {
			processRecord(record);
			record.setClientPayHead(getPayhead(record.getPayHead()));
			Object[] values = new Object[this.getColumns().length];
			Object[] updatedValues = new Object[this.getColumns().length];
			for (int x = 0; x < this.getColumns().length; x++) {
				values[x] = getColumnData(record, x);
				updatedValues[x] = getColumnData(record, x);
				if (x == 1) {
					updatedValues[x] = record.getClientPayHead().isEarning() ? record
							.getRate() : -record.getRate();
				}
			}
			updateTotals(updatedValues);
			addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		showRecords();
	}

	private ClientPayHead getPayhead(long payHead) {
		for (ClientPayHead ph : payheads) {
			if (ph.getID() == payHead) {
				return ph;
			}
		}
		return null;
	}

	private Object getColumnData(ClientEmployeePayHeadComponent record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getClientPayHead().getDisplayName();
		case 1:
			return record.getRate();
		case 2:
			ClientPayHead payHead = record.getClientPayHead();
			int type = 0;
			if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
				ClientAttendancePayHead payhead = ((ClientAttendancePayHead) payHead);
				type = payhead.getCalculationPeriod();
			} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
				ClientComputionPayHead payhead = ((ClientComputionPayHead) payHead);
				type = payhead.getCalculationPeriod();
			} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
				ClientFlatRatePayHead payhead = ((ClientFlatRatePayHead) payHead);
				type = payhead.getCalculationPeriod();
			}
			return ClientPayHead.getCalculationPeriod(type);
		case 3:
			payHead = record.getClientPayHead();
			return ClientPayHead.getPayHeadType(payHead.getType());
		case 4:
			payHead = record.getClientPayHead();
			return ClientPayHead.getCalculationType(payHead
					.getCalculationType());
		case 5:
			payHead = record.getClientPayHead();
			if (payHead != null
					&& payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
				ClientComputionPayHead payhead = (ClientComputionPayHead) payHead;
				if (payhead.getComputationType() != ClientComputionPayHead.COMPUTATE_ON_SPECIFIED_FORMULA) {
					return ClientComputionPayHead.getComputationType(payhead
							.getComputationType());
				} else {
					return UIUtils
							.prepareFormula(payhead.getFormulaFunctions());
				}
			}
			return "";
		}
		return null;
	}

	private void endAllSections() {
		try {
			for (int i = this.sections.size() - 1; i >= 0; i--) {
				endSection();
			}
		} catch (Exception e) {
		}
	}

	private void addRow(ClientEmployeePayHeadComponent record, int depth,
			Object[] values, boolean bold, boolean underline, boolean border) {
		this.reportGrid.addRow(record, depth, values, bold, underline, border);
	}

	private void updateTotals(Object[] values) {
		for (Section<ClientEmployeePayHeadComponent> sec : this.sections) {
			sec.update(values);
		}
	}

	private String sectionName = "";

	private void processRecord(ClientEmployeePayHeadComponent record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { messages.total() },
					new int[] { 1 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getName();
			addSection(new String[] { sectionName },
					new String[] { messages.reportTotal(sectionName) },
					new int[] { 1 });
		} else if (sectionDepth == 2) {
			if (!sectionName.equals(record.getName())) {
				endSection();
			} else {
				return;
			}
		}
		processRecord(record);

	}

	public void addSection(String[] sectionTitles, String[] footerTitles,
			int[] sumColumns) {
		Section<ClientEmployeePayHeadComponent> s = new Section<ClientEmployeePayHeadComponent>(
				sectionTitles, footerTitles, sumColumns, getColumns().length,
				reportGrid);
		s.startSection();
		sections.add(s);
		sectionDepth++;
	}

	public void endSection() {
		try {
			this.sectionDepth--;
			if (sectionDepth >= 0 && !sections.isEmpty()) {
				Section<ClientEmployeePayHeadComponent> s = sections
						.remove(sectionDepth);
				s.endSection();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setFromAndToDate(
			ArrayList<ClientEmployeePayHeadComponent> result) {

	}

	protected void refreshMakeDetailLayout() {
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	protected String getViewTitle() {
		return messages.newPayee(messages.payrun());
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		empsAndGroups.setFocus();
	}

	@Override
	protected void initTransactionViewData() {
	}

	@Override
	public void updateNonEditableItems() {
	}

	@Override
	protected void refreshTransactionGrid() {
	}

	@Override
	protected void updateDiscountValues() {
	}

	@Override
	public void updateAmountsFromGUI() {
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		empsAndGroups.setEnabled(!isInViewMode());
		table.setEnabled(!isInViewMode());
		int widgetCount = tableForm.getWidgetCount();
		if (widgetCount != 0) {
			showPayRunTable();
		}
		noOfWorkingDays.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
	}

	@Override
	protected boolean canAddDraftButton() {
		return false;
	}

	@Override
	protected boolean canRecur() {
		return false;
	}
}
