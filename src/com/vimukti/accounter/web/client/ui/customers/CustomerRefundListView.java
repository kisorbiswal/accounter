package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.CustomerRefundListGrid;

/**
 * 
 * @author Fernandez
 * @param <T>
 * 
 */
public class CustomerRefundListView extends
		TransactionsListView<CustomerRefundsList> implements IPrintableView {
	protected List<CustomerRefundsList> transactions;
	private List<CustomerRefundsList> listOfCustomerRefund = new ArrayList<CustomerRefundsList>();

	// private static String DELETED="Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;

	public CustomerRefundListView() {
		super(messages.issued());
	}

	@Override
	protected Action getAddNewAction() {
		return new CustomerRefundAction();
	}

	@Override
	protected String getAddNewLabelString() {
		if (Utility
				.isUserHavePermissions(ClientTransaction.TYPE_CUSTOMER_REFUNDS)) {
			return messages.addaNew(messages.customerRefund(Global.get()
					.Customer()));
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		return messages.getCustomersRefundListViewHeading(Global.get()
				.Customer());
	}

	@Override
	protected void initGrid() {
		grid = new CustomerRefundListGrid(false);
		grid.init();

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getCustomerRefundsList(
				getStartDate().getDate(), getEndDate().getDate(), this);
	}

	@Override
	public void onSuccess(PaginationList<CustomerRefundsList> result) {
		super.onSuccess(result);
		listOfCustomerRefund = result;
		filterList(viewSelect != null ? viewSelect.getSelectedValue()
				: messages.notIssued());
		grid.setViewType(viewSelect != null ? viewSelect.getSelectedValue()
				: messages.notIssued());
		grid.sort(10, false);
	}

	@Override
	public void updateInGrid(CustomerRefundsList objectTobeModified) {
		// its not using any where

	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("currentView", viewSelect.getValue().toString());
		map.put("dateRange", dateRangeSelector.getValue().toString());
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		String currentView = (String) map.get("currentView");
		viewSelect.setComboItem(currentView);
		this.setViewType(currentView);
		String dateRange1 = (String) map.get("dateRange");
		dateRangeSelector.setComboItem(dateRange1);
		ClientFinanceDate startDate1 = (ClientFinanceDate) map.get("startDate");
		setStartDate(startDate1);
		ClientFinanceDate endDate1 = (ClientFinanceDate) map.get("endDate");
		setEndDate(endDate1);
		start = (Integer) map.get("start");
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		for (CustomerRefundsList customerRefund : listOfCustomerRefund) {
			if (text.equals(messages.notIssued())) {
				if ((customerRefund.getStatus() == STATUS_NOT_ISSUED || customerRefund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!customerRefund.isVoided())) {
					grid.addData(customerRefund);
				}
				continue;
			}
			if (text.equals(messages.issued())) {
				if (customerRefund.getStatus() == STATUS_ISSUED
						&& (!customerRefund.isVoided())
						&& customerRefund.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {
					grid.addData(customerRefund);
				}
				continue;
			}
			if (text.equals(messages.voided())) {
				if (customerRefund.isVoided() && !customerRefund.isDeleted()) {
					grid.addData(customerRefund);
				}
				continue;
			}
			if (text.equalsIgnoreCase(messages.drafts())) {
				if (customerRefund.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
					grid.addData(customerRefund);
				}
				continue;
			}
			// if (text.equals(DELETED)) {
			// if (customerRefund.isDeleted())
			// grid.addData(customerRefund);
			// continue;
			// }
			if (text.equals(messages.all())) {
				grid.addData(customerRefund);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
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
		// its not using any where

	}

	@Override
	protected String getViewTitle() {
		return messages.customerRefunds(Global.get().Customer());
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getCustomerRefundsListExportCsv(
				getStartDate().getDate(),
				getEndDate().getDate(),
				getExportCSVCallback(messages.customerRefund(Global.get()
						.Customer())));
	}
}
