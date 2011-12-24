package com.vimukti.accounter.web.client.ui.vat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.TAXAgencyListGrid;

public class TAXAgencyListView extends BaseListView<PayeeList> {

	private List<PayeeList> listOfPayees;
	private int start;

	public TAXAgencyListView() {
		super();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);

		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	public Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewTAXAgencyAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages().addaNew(messages().taxAgency());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages().payeeList(messages().taxAgency());
	}

	@Override
	protected void updateTotal(PayeeList t, boolean add) {

		if (add) {
			total += t.getBalance();
		} else
			total -= t.getBalance();
		totalLabel.setText(messages().totalOutStandingBalance()

		+ DataUtils.getAmountAsStringInPrimaryCurrency(total) + "");
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
		// Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_TAX_AGENCY,
		// this);

	}

	@Override
	protected void initGrid() {
		grid = new TAXAgencyListGrid(false);
		grid.init();
		// listOfPayees = getRecords();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages().active());
		// } else {
		// viewSelect.setComboItem(messages().inActive());
		// }

	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();
		for (PayeeList payee : listOfPayees) {
			if (isActive) {
				if (payee.isActive()) {
					grid.addData(payee);
				}

			} else if (!payee.isActive()) {
				grid.addData(payee);

			}

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages().noRecordsToShow());

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(PaginationList<PayeeList> result) {
		this.listOfPayees = result;
		super.onSuccess(result);
		grid.sort(10, false);
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
		return messages().payees(messages().taxAgencies());
	}
}
