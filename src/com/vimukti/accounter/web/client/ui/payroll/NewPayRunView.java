package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientEmployeePaymentDetails;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewPayRunView extends BaseView<ClientPayRun> {

	private DateField fromDate, toDate;
	private EmployeesAndGroupsCombo empsAndGroups;
	private EmployeePayHeadComponentTable grid;
	protected ClientPayStructureDestination selectedItem;

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

		grid = new EmployeePayHeadComponentTable();
		grid.setEnabled(!isInViewMode());

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(empsAndGroups);
		mainVLay.add(fromDate);
		mainVLay.add(toDate);
		mainVLay.add(button);
		mainVLay.add(grid);

		this.add(mainVLay);
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
				grid.setAllRows(result);
			}

		};
		Accounter.createPayrollService().getEmployeePayHeadComponents(
				selectedItem, fromDate.getDate(), toDate.getDate(), callback);

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
