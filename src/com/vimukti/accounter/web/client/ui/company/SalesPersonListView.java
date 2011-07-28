package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;

public class SalesPersonListView extends BaseListView<ClientPayee> {

	CustomersMessages salesPersonConstants;
	private List<ClientSalesPerson> listOfsalesPerson;

	public SalesPersonListView() {

	}

	@Override
	public void deleteFailed(Throwable caught) {
		super.deleteFailed(caught);
		Accounter.showInformation(Accounter.constants()
				.youCantDeleteThisCustomer());

	}

	@Override
	public void init() {
		salesPersonConstants = GWT.create(CustomersMessages.class);
		super.init();

	}

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return CustomersActionFactory.getNewSalesperSonAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return salesPersonConstants.addaNewsalesPerson();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return salesPersonConstants.salesPersonList();
	}

	// @Override
	// protected HorizontalPanel getTotalLayout(BaseListGrid grid) {
	// grid.addFooterValue(FinanceApplication.constants().total(),
	// 7);
	// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) + "",
	// 8);
	// return null;
	// }
	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new SalesPersonListGrid();
		// grid.addStyleName("listgrid-tl");
		grid.init();
		listOfsalesPerson = getCompany().getsalesPerson();
		filterList(true);
		getTotalLayout(grid);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();
		for (ClientSalesPerson salesPerson : listOfsalesPerson) {
			if (isActive) {
				if (salesPerson.isActive() == true)
					grid.addData(salesPerson);
			} else if (salesPerson.isActive() == false) {
				grid.addData(salesPerson);
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		List<ClientSalesPerson> salesPersons = new ArrayList<ClientSalesPerson>(
				this.grid.getRecords());
		salesPersons.add((ClientSalesPerson) object);
		Collections.sort(salesPersons, new Comparator<ClientSalesPerson>() {

			@Override
			public int compare(ClientSalesPerson o1, ClientSalesPerson o2) {
				return o1.getName().compareTo(o1.getName());
			}
		});
		this.grid.removeAllRecords();
		if (salesPersons != null)
			this.grid.setRecords(salesPersons);
		else
			this.grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

		super.saveSuccess(object);
	}

	@Override
	public void updateInGrid(ClientPayee objectTobeModified) {
		// its not using any where return null;

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// its not using any where return null;

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().SalesPersons();
	}

}
