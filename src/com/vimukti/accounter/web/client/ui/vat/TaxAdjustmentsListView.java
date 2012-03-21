package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.TaxAdjustmentListGrid;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class TaxAdjustmentsListView extends
		TransactionsListView<ClientTAXAdjustment> {

	public int viewId;

	public TaxAdjustmentsListView() {
		super(messages.all());
		this.getElement().setId("TAXAgencyListView");
		setViewType(messages.all());
	}

	@Override
	public void updateInGrid(ClientTAXAdjustment objectTobeModified) {

	}
	
	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void initGrid() {
		grid = new TaxAdjustmentListGrid(false);
		grid.init();
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeeList(messages.taxAdjustment());
	}

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().getUserRole()
				.equals(RolePermissions.FINANCIAL_ADVISER)) {
			return ActionFactory.getAdjustTaxAction();
		} else {
			return null;
		}
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getTaxAdjustmentsList(getViewId(),
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, this);
	}

	private int checkViewType(String view) {
		if (getViewType().equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}

		return getViewId();
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().getUserRole()
				.equals(RolePermissions.FINANCIAL_ADVISER))
			return messages.addaNew(messages.taxAdjustment());
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return messages.payees(messages.taxAdjustment());
	}

	@Override
	public void onSuccess(PaginationList<ClientTAXAdjustment> result) {

		if (result.isEmpty()) {
			grid.removeAllRecords();
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());

	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (text.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (text.equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}
		onPageChange(0, getPageSize());
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
}
