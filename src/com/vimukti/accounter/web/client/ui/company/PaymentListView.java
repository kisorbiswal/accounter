package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.SelectPaymentTypeDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.BankingMessages;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.PaymentsListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PaymentListView extends BaseListView<PaymentsList> {
	Label addPayLabel;
	SelectItem viewSelect;
	DynamicForm form;
	Action action;
	List<PaymentsList> allPayments;
	BankingMessages bankingConstants = GWT.create(BankingMessages.class);
	private List<PaymentsList> listOfPayments;

	private static String NOT_ISSUED = FinanceApplication.getVendorsMessages()
			.notIssued();
	private static String ISSUED = FinanceApplication.getVendorsMessages()
			.issued();
	private static String VOID = FinanceApplication.getVendorsMessages()
			.Voided();
	private static String ALL = FinanceApplication.getVendorsMessages().all();
	// private static String DELETED = "Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;

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
		return bankingConstants.addanewPayment();
	}

	@Override
	protected String getListViewHeading() {

		return FinanceApplication.getCompanyMessages().paymentsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getPaymentsList(this);
	}

	@Override
	public void onSuccess(List<PaymentsList> result) {
		super.onSuccess(result);
		listOfPayments = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new PaymentsListGrid(false);
		grid.init();
		grid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// FinanceApplication.createHomeService().getPaymentsList(this);
	}

	protected SelectItem getSelectItem() {
		viewSelect = new SelectItem(FinanceApplication.getBankingsMessages()
				.currentView());
		viewSelect.setHelpInformation(true);
		viewSelect.setValueMap(NOT_ISSUED, ISSUED, VOID, ALL
		// ,DELETED
				);

		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");

		viewSelect.setDefaultValue(NOT_ISSUED);
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

		for (PaymentsList payment : listOfPayments) {
			if (text.equals(NOT_ISSUED)) {
				if (payment.getStatus() == STATUS_NOT_ISSUED
						|| payment.getStatus() == STATUS_PARTIALLY_PAID)
					grid.addData(payment);
				// else
				// grid.addEmptyMessage("No records to show");
				continue;
			}
			if (text.equals(ISSUED)) {
				if (payment.getStatus() == STATUS_ISSUED)
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
	public void updateGrid(IAccounterCore core) {
		initListCallback();
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
		// TODO Auto-generated method stub

	}

}
