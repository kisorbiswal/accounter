package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.ReceivedPaymentListGrid;

/**
 * 
 * @modified Fernandez
 * 
 */
public class ReceivedPaymentListView extends BaseListView<ReceivePaymentsList> {
	CustomersMessages customerConstants = GWT.create(CustomersMessages.class);

	private List<ReceivePaymentsList> listOfRecievePayments;

	private static String ALL = FinanceApplication.getCustomersMessages().all();
	private static String OPEN = FinanceApplication.getCustomersMessages()
			.open();
	private static String FULLY_APPLIED = FinanceApplication
			.getCustomersMessages().fullyApplied();
	private static String VOIDED = FinanceApplication.getVendorsMessages()
			.Voided();
	// private static String DELETED="Deleted";

	private static final int STATUS_UNAPPLIED = 0;
	private static final int STATUS_PARTIALLY_APPLIED = 1;
	private static final int STATUS_APPLIED = 2;

	public ReceivedPaymentListView() {
		super();
	}

	@Override
	protected Action getAddNewAction() {
		return CustomersActionFactory.getReceivePaymentAction();

	}

	@Override
	protected String getAddNewLabelString() {
		return customerConstants.addaNewPayment();

	}

	@Override
	protected String getListViewHeading() {
		return customerConstants.getReceivedPaymentListViewHeading();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getReceivePaymentsList(this);
	}

	@Override
	public void onSuccess(List<ReceivePaymentsList> result) {
		super.onSuccess(result);
		listOfRecievePayments = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	public void updateInGrid(ReceivePaymentsList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new ReceivedPaymentListGrid(false);
		grid.init();

	}

	protected SelectItem getSelectItem() {
		viewSelect = new SelectItem(FinanceApplication.getCustomersMessages()
				.currentView());
		viewSelect.setValueMap(ALL, OPEN, FULLY_APPLIED, VOIDED
		// ,DELETED
				);
		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");

		viewSelect.setDefaultValue(VOIDED);
		viewSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (viewSelect.getValue() != null) {
					grid.setViewType(viewSelect.getValue().toString());
					filterList(viewSelect.getValue().toString());
				}

			}
		});

		return viewSelect;
	}

	@SuppressWarnings("unchecked")
	private void filterList(String text) {

		grid.removeAllRecords();
		for (ReceivePaymentsList recievePayment : listOfRecievePayments) {
			if (text.equals(OPEN)) {
				if ((recievePayment.getStatus() == STATUS_UNAPPLIED
						|| recievePayment.getStatus() == STATUS_PARTIALLY_APPLIED))
					grid.addData(recievePayment);
				
				continue;
			}
			if (text.equals(FULLY_APPLIED)) {
				if (recievePayment.getStatus() == STATUS_APPLIED)
					grid.addData(recievePayment);
				
				continue;
			}
			if (text.equals(VOIDED)) {
				if (recievePayment.isVoided() && !recievePayment.isDeleted())
					grid.addData(recievePayment);
				continue;
			}
			// if(text.equals(DELETED)){
			// if (recievePayment.isDeleted() == true)
			// grid.addData(recievePayment);
			// continue;
			// }
			if (text.equals(ALL)) {
				grid.addData(recievePayment);
			}
		}
		if(grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);


	}

	@Override
	public void updateGrid(IAccounterCore core) {
		initListCallback();
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
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
