package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.reports.ReportGrid;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class NewPayRunView extends BaseView<ClientPayRun> {

	private DateField fromDate, toDate;
	private EmployeesAndGroupsCombo empsAndGroups;
	private EmployeePayHeadComponentTable grid;
	protected ClientPayStructureDestination selectedItem;
	private ScrollPanel tableLayout;
	protected int sectionDepth = 0;

	private final List<Section<ClientEmployeePayHeadComponent>> sections = new ArrayList<Section<ClientEmployeePayHeadComponent>>();

	public NewPayRunView() {
		this.getElement().setId("NewPayRunView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	Button button;
	private ReportGrid reportGrid;
	private ArrayList<ClientEmployeePayHeadComponent> records;
	private int row;

	private void createControls() {
		Label lab1 = new Label(messages.payrun());
		lab1.setStyleName("label-title");

		empsAndGroups = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"empsAndGroups");
		empsAndGroups
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayStructureDestination selectItem) {
						NewPayRunView.this.selectedItem = selectItem;
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
				button.setEnabled(NewPayRunView.this.selectedItem != null);
			}
		});

		toDate = new DateField(messages.toDate(), "toDate");
		toDate.setEnteredDate(endDate);
		toDate.setEnabled(false);
		toDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				button.setEnabled(NewPayRunView.this.selectedItem != null);
			}
		});

		button.setEnabled(false);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectionChanged();
			}
		});

		this.tableLayout = new ScrollPanel() {
			@Override
			protected void onLoad() {
				super.onLoad();
				// grid.setHeight("450px");
			}
		};
		this.tableLayout.addStyleName("tableLayout");

		reportGrid = reportGrid = new ReportGrid<ClientEmployeePayHeadComponent>(
				getColumns(), true);
		// reportGrid.setReportView(this);
		reportGrid.setColumnTypes(getColumnTypes());
		reportGrid.init();
		reportGrid.addEmptyMessage(messages.noRecordsToShow());
		tableLayout.add(reportGrid);

		grid = new EmployeePayHeadComponentTable();
		grid.setEnabled(!isInViewMode());

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(empsAndGroups);
		mainVLay.add(fromDate);
		mainVLay.add(toDate);
		mainVLay.add(button);
		mainVLay.add(tableLayout);

		this.add(mainVLay);
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
		super.initData();
	}

	private void initViewData(ClientPayRun data) {

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		data.setPayPeriodStartDate(fromDate.getDate().getDate());
		data.setPayPeriodEndDate(toDate.getDate().getDate());

		List<ClientEmployeePaymentDetails> details = new ArrayList<ClientEmployeePaymentDetails>();

		List<ClientEmployeePayHeadComponent> allRows = grid.getAllRows();

		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue instanceof ClientEmployee) {
			ClientEmployeePaymentDetails employeePaymentDetails = new ClientEmployeePaymentDetails();
			employeePaymentDetails.setEmployee(selectedValue.getID());
			employeePaymentDetails.setPayHeadComponents(allRows);

			details.add(employeePaymentDetails);
		} else {
			ClientEmployeeGroup group = (ClientEmployeeGroup) selectedValue;
			for (ClientEmployee employee : group.getEmployees()) {
				ClientEmployeePaymentDetails employeePaymentDetails = new ClientEmployeePaymentDetails();
				employeePaymentDetails.setEmployee(employee.getID());
				employeePaymentDetails.setPayHeadComponents(allRows);

				details.add(employeePaymentDetails);

			}
		}
		data.setPayEmployee(details);
	}

	protected void selectionChanged() {
		grid.clear();
		reportGrid.removeAllRows();
		fromDate.setEnabled(true);
		toDate.setEnabled(true);
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
				selectedItem, fromDate.getDate(), toDate.getDate(), callback);

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
		initGrid();
		reportGrid.removeAllRows();
		row = -1;
		this.records = records;

		for (ClientEmployeePayHeadComponent record : records) {
			processRecord(record);
			Object[] values = new Object[this.getColumns().length];
			for (int x = 0; x < this.getColumns().length; x++) {
				values[x] = getColumnData(record, x);
			}
			updateTotals(values);
			addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		showRecords();
	}

	private Object getColumnData(ClientEmployeePayHeadComponent record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getPayHead().getDisplayName();
		case 1:
			return record.getRate();
		case 2:
			ClientPayHead payHead = record.getPayHead();
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
			} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
				ClientAttendancePayHead payhead = ((ClientAttendancePayHead) payHead);
				type = payhead.getCalculationPeriod();
			}
			return ClientPayHead.getCalculationPeriod(type);
		case 3:
			payHead = record.getPayHead();
			return ClientPayHead.getPayHeadType(payHead.getType());
		case 4:
			payHead = record.getPayHead();
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
		// TODO Auto-generated method stub

	}

	private String sectionName = "";
	private Object columns;

	private void processRecord(ClientEmployeePayHeadComponent record) {
		if (sectionDepth == 0) {
			// First time
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] {},
					new int[] {});

		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
				// endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
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

	private void initGrid() {
		this.columns = getColumns();

		// this.reportGrid.setReportView(this);
	}

	protected void setFromAndToDate(
			ArrayList<ClientEmployeePayHeadComponent> result) {
		// TODO Auto-generated method stub

	}

	protected void refreshMakeDetailLayout() {
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
		return messages.newPayee(messages.payrun());
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		empsAndGroups.setFocus();
	}

}
