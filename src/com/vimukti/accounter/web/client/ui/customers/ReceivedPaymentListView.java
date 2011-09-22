package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
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
	AccounterConstants customerConstants = Accounter.constants();

	private List<ReceivePaymentsList> listOfRecievePayments;

	private static String ALL = Accounter.constants().all();
	private static String OPEN = Accounter.constants().open();
	private static String FULLY_APPLIED = Accounter.constants().fullyApplied();
	private static String VOIDED = Accounter.constants().voided();
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
		return customerConstants.addaNewPayment();

	}

	@Override
	protected String getListViewHeading() {
		return customerConstants.getReceivedPaymentListViewHeading();
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
		viewSelect = new SelectCombo(Accounter.constants().currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(ALL);
		listOfTypes.add(OPEN);
		listOfTypes.add(FULLY_APPLIED);
		listOfTypes.add(VOIDED);
		viewSelect.initCombo(listOfTypes);
		// ,DELETED
		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");

		viewSelect.setComboItem(OPEN);
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
			if (text.equals(OPEN)) {
				if ((recievePayment.getStatus() == STATUS_UNAPPLIED || recievePayment
						.getStatus() == STATUS_PARTIALLY_APPLIED)
						&& (!recievePayment.isVoided()))
					grid.addData(recievePayment);

				continue;
			}
			if (text.equals(FULLY_APPLIED)) {
				if (recievePayment.getStatus() == STATUS_APPLIED
						&& !recievePayment.isVoided())
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
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

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
		return Accounter.constants().recievePayments();
	}

}
