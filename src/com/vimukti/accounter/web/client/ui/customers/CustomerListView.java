package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerListGrid;

public class CustomerListView extends BaseListView<PayeeList> {

	CustomersMessages customerConstants;
	private List<PayeeList> listOfCustomers;

	public CustomerListView() {

	}

	@Override
	public void deleteFailed(Throwable caught) {
		super.deleteFailed(caught);
		Accounter.showInformation(FinanceApplication.getCustomersMessages()
				.youCantDeleteThisCustomer());

	}

	@Override
	public void init() {
		customerConstants = GWT.create(CustomersMessages.class);
		super.init();

	}

	@Override
	protected Action getAddNewAction() {

		return CustomersActionFactory.getNewCustomerAction();
	}

	@Override
	protected String getAddNewLabelString() {

		return customerConstants.addaNewCustomer();
	}

	@Override
	protected String getListViewHeading() {

		return customerConstants.customerList();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {
		// grid.addFooterValue(FinanceApplication.getCustomersMessages().total(),
		// 8);
		// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) +
		// "",
		// 9);
		return null;
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_CUSTOMER, this);

	}

	@Override
	protected void initGrid() {
		grid = new CustomerListGrid();
		// grid.addStyleName("listgrid-tl");
		grid.init();
		// listOfCustomers = FinanceApplication.getCompany().getCustomers();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();
			for (PayeeList customer : listOfCustomers) {
				if (isActive) {
					if (customer.isActive() == true)
						grid.addData(customer);
				} else if (customer.isActive() == false) {
					grid.addData(customer);
				}
			}
			if(grid.getRecords().isEmpty())
				grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(List<PayeeList> result) {
		this.listOfCustomers = result;
		super.onSuccess(result);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}
