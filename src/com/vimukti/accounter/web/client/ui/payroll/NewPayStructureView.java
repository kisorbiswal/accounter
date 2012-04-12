package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewPayStructureView extends BaseView<ClientPayStructure> {

	private EmployeesAndGroupsCombo empsAndGroups;
	private PayStructureTable grid;
	private AddNewButton itemTableButton;

	public NewPayStructureView() {
		this.getElement().setId("NewPayStructureView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label lab1 = new Label(messages.payStructure());
		lab1.setStyleName("label-title");

		empsAndGroups = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"empsAndGroups");
		ArrayList<ClientPayStructureDestination> arrayList = new ArrayList<ClientPayStructureDestination>();
		arrayList.addAll(getCompany().getEmployees());
		arrayList.addAll(getCompany().getEmployeeGroups());
		empsAndGroups.initCombo(arrayList);

		grid = new PayStructureTable(false);

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(empsAndGroups);
		mainVLay.add(grid);
		mainVLay.add(itemTableButton);

		this.add(mainVLay);
	}

	protected void addItem() {
		ClientPayStructureItem transactionItem = new ClientPayStructureItem();
		grid.add(transactionItem);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayStructure());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayStructure data) {
		ClientPayStructureDestination employee = getCompany().getEmployee(
				data.getEmployee());
		if (employee == null) {
			employee = getCompany().getEmployeeGroup(data.getEmployeeGroup());
		}
		empsAndGroups.setValue(employee);

		grid.setAllRows(data.getItems());
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
		return messages.newPayee(messages.payStructure());
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
