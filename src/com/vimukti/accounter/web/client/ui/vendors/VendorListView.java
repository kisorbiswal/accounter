package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorListGrid;

/**
 * 
 * @author venki.p modified by Rajesh.A
 * @modified Fernandez
 * 
 */
public class VendorListView extends BaseListView<PayeeList> {

	private List<PayeeList> listOfPayees;

	public VendorListView() {
		super();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);

		AccounterException accounterException = (AccounterException) caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	public Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewVendorAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addANewVendor(Global.get().vendor());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages.vendorList(Global.get().Vendor());
	}

	// protected List<ClientPayee> getRecords() {
	//
	// List<ClientVendor> vendors = FinanceApplication.getCompany()
	// .getVendors();
	// List<ClientTaxAgency> taxAgencies = FinanceApplication.getCompany()
	// .gettaxAgencies();
	//
	// List<ClientVATAgency> vatAgencies = FinanceApplication.getCompany()
	// .getVatAgencies();
	//
	// List<ClientPayee> records = new ArrayList<ClientPayee>();
	// if (vendors != null)
	// records.addAll(vendors);
	//
	// if (FinanceApplication.getCompany().isUKAccounting()
	// && vatAgencies != null) {
	// records.addAll(vatAgencies);
	// } else if (!FinanceApplication.getCompany().isUKAccounting()
	// && taxAgencies != null) {
	// records.addAll(taxAgencies);
	// }
	//
	// return records;
	// }

	@Override
	protected void updateTotal(PayeeList t, boolean add) {

		if (add) {
			total += t.getBalance();
		} else
			total -= t.getBalance();
		totalLabel.setText(Accounter.constants().totalOutStandingBalance()

		+ DataUtils.getAmountAsString(total) + "");
	}

	@Override
	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {

		// grid.addFooterValue(FinanceApplication.constants().total(),
		// 8);
		// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) +
		// "",
		// 9);

		return null;
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_VENDOR, this);

	}

	@Override
	protected void initGrid() {
		grid = new VendorListGrid(false);
		grid.init();
		// listOfPayees = getRecords();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();
		for (PayeeList payee : listOfPayees) {
			if (isActive) {
				if (payee.isActive() == true)
					grid.addData(payee);

			} else if (payee.isActive() == false) {
				grid.addData(payee);

			}

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(ArrayList<PayeeList> result) {
		this.listOfPayees = result;
		super.onSuccess(result);
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.vendors(Global.get().Vendor());
	}
}
