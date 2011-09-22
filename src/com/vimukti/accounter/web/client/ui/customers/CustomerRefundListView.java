package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.CustomerRefundListGrid;

/**
 * 
 * @author Fernandez
 * @param <T>
 * 
 */
public class CustomerRefundListView extends BaseListView<CustomerRefundsList> {
	AccounterConstants customerConstants = Accounter.constants();
	protected List<CustomerRefundsList> transactions;
	private List<CustomerRefundsList> listOfCustomerRefund;

	private static String NOT_ISSUED = Accounter.constants().notIssued();
	private static String ISSUED = Accounter.constants().issued();
	private static String VOID = Accounter.constants().voided();
	private static String ALL = Accounter.constants().all();
	// private static String DELETED="Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;

	public CustomerRefundListView() {
		super();

	}

	@Override
	protected Action getAddNewAction() {

		return ActionFactory.getCustomerRefundAction();
	}

	@Override
	protected String getAddNewLabelString() {

		return Accounter.messages().addaNewCustomerRefund(
				Global.get().Customer());
	}

	@Override
	protected String getListViewHeading() {

		return Accounter.messages().getCustomersRefundListViewHeading(
				Global.get().Customer());
	}

	@Override
	protected void initGrid() {
		grid = new CustomerRefundListGrid(false);
		grid.init();

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getCustomerRefundsList(this);
	}

	@Override
	public void onSuccess(ArrayList<CustomerRefundsList> result) {
		super.onSuccess(result);
		listOfCustomerRefund = result;
		filterList(viewSelect != null ? viewSelect.getSelectedValue()
				: NOT_ISSUED);
		grid.setViewType(viewSelect != null ? viewSelect.getSelectedValue()
				: NOT_ISSUED);
	}

	@Override
	public void updateInGrid(CustomerRefundsList objectTobeModified) {
		// its not using any where

	}

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.constants().currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(NOT_ISSUED);
		listOfTypes.add(ISSUED);
		listOfTypes.add(VOID);
		listOfTypes.add(ALL);
		viewSelect.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");

		viewSelect.setComboItem(ISSUED);
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
						}

					}
				});

		return viewSelect;
	}

	private void filterList(String text) {

		grid.removeAllRecords();

		for (CustomerRefundsList customerRefund : listOfCustomerRefund) {
			if (text.equals(NOT_ISSUED)) {
				if ((customerRefund.getStatus() == STATUS_NOT_ISSUED || customerRefund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!customerRefund.isVoided()))
					grid.addData(customerRefund);
				continue;
			}
			if (text.equals(ISSUED)) {
				if (customerRefund.getStatus() == STATUS_ISSUED
						&& (!customerRefund.isVoided()))
					grid.addData(customerRefund);
				continue;
			}
			if (text.equals(VOID)) {
				if (customerRefund.isVoided() && !customerRefund.isDeleted())
					grid.addData(customerRefund);
				continue;
			}
			// if (text.equals(DELETED)) {
			// if (customerRefund.isDeleted())
			// grid.addData(customerRefund);
			// continue;
			// }
			if (text.equals(ALL)) {

				grid.addData(customerRefund);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
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
		return Accounter.messages().customerRefunds(Global.get().Customer());
	}
}
