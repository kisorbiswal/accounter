package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ReceivedPaymentListGrid;

/**
 * 
 * @modified Fernandez
 * 
 */
public class ReceivedPaymentListView extends BaseListView<ReceivePaymentsList> {

	private List<ReceivePaymentsList> listOfRecievePayments;

	private static String ALL = messages.all();
	// private static String OPEN = messages.open();
	// private static String FULLY_APPLIED =
	// messages.fullyApplied();
	private static String VOIDED = messages.voided();
	private static String PAID = messages.paid();
	// private static String DELETED="Deleted";

	private static final int STATUS_UNAPPLIED = 0;
	private static final int STATUS_PARTIALLY_APPLIED = 1;
	private static final int STATUS_APPLIED = 2;

	public ReceivedPaymentListView() {
		super();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getPaymentDialogAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addaNewPayment();

	}

	@Override
	protected String getListViewHeading() {
		return messages.getReceivedPaymentListViewHeading();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getReceivePaymentsList(this);
	}

	@Override
	public void onSuccess(ArrayList<ReceivePaymentsList> result) {
		super.onSuccess(result);
		listOfRecievePayments = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
	}

	@Override
	public void updateInGrid(ReceivePaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new ReceivedPaymentListGrid(false);
		grid.init();

	}

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(ALL);
		listOfTypes.add(PAID);
		// listOfTypes.add(OPEN);
		// listOfTypes.add(FULLY_APPLIED);
		listOfTypes.add(VOIDED);
		viewSelect.initCombo(listOfTypes);
		// ,DELETED
		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("150px");

		viewSelect.setComboItem(PAID);
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
		for (ReceivePaymentsList recievePayment : listOfRecievePayments) {
			// if (text.equals(OPEN)) {
			// if ((recievePayment.getStatus() == STATUS_UNAPPLIED ||
			// recievePayment
			// .getStatus() == STATUS_PARTIALLY_APPLIED)
			// && (!recievePayment.isVoided()))
			// grid.addData(recievePayment);
			//
			// continue;
			// }
			// if (text.equals(FULLY_APPLIED)) {
			// if (recievePayment.getStatus() == STATUS_APPLIED
			// && !recievePayment.isVoided())
			// grid.addData(recievePayment);
			//
			// continue;
			// }
			if (text.equals(PAID)) {
				if (!recievePayment.isVoided()) {
					grid.addData(recievePayment);
				}
			} else if (text.equals(VOIDED)) {
				if (recievePayment.isVoided() && !recievePayment.isDeleted()) {
					grid.addData(recievePayment);
				}
				continue;
			} else if (text.equals(ALL)) {
				grid.addData(recievePayment);
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

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
		return messages.recievePayments();
	}

}
