package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.SelectPaymentTypeDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.PaymentsListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PaymentListView extends BaseListView<PaymentsList> {
	Label addPayLabel;
	SelectCombo viewSelect;
	DynamicForm form;
	Action action;
	List<PaymentsList> allPayments;
	private List<PaymentsList> listOfPayments;

	private static String NOT_ISSUED = Accounter.constants().notIssued();
	private static String ISSUED = Accounter.constants().issued();
	private static String VOID = Accounter.constants().voided();
	private static String ALL = Accounter.constants().all();
	// private static String DELETED = "Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;
	private static final int STATUS_VOID = 4;

	public PaymentListView() {
		super();
	}

	public static PaymentListView getInstance() {
		return new PaymentListView();
	}

	@Override
	protected Action getAddNewAction() {
		new SelectPaymentTypeDialog().show();
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewPayment();
	}

	@Override
	protected String getListViewHeading() {

		return Accounter.constants().paymentsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getPaymentsList(this);
	}

	@Override
	public void onSuccess(ArrayList<PaymentsList> result) {
		super.onSuccess(result);
		listOfPayments = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void initGrid() {
		grid = new PaymentsListGrid(false);
		grid.init();
		grid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// FinanceApplication.createHomeService().getPaymentsList(this);
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

		viewSelect.setComboItem(NOT_ISSUED);
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

		for (PaymentsList payment : listOfPayments) {
			if (text.equals(NOT_ISSUED)) {
				if ((payment.getStatus() == STATUS_NOT_ISSUED || payment
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!payment.isVoided()))
					grid.addData(payment);
				// else
				// grid.addEmptyMessage("No records to show");
				continue;
			}
			if (text.equals(ISSUED)) {
				if (payment.getStatus() == STATUS_ISSUED
						&& (!payment.isVoided()))
					grid.addData(payment);

				continue;
			}
			if (text.equals(VOID)) {
				if (payment.isVoided()
				// && payment.getStatus()!=ClientTransaction.STATUS_DELETED
				)
					grid.addData(payment);
				continue;
			}
			// if (text.equals(DELETED)) {
			// if(payment.getStatus()==ClientTransaction.STATUS_DELETED)
			// grid.addData(payment);
			// continue;
			// }
			if (text.equals(ALL)) {
				grid.addData(payment);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().payments();
	}

}
