package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.VendorPaymentsListGrid;

/**
 * @modified by Ravi kiran.G
 * 
 */

public class VendorPaymentsListView extends BaseListView<PaymentsList> {

	protected List<PaymentsList> allPayments;
	private VendorsMessages vendorConstants = GWT.create(VendorsMessages.class);
	private SelectItem currentView;

	private VendorPaymentsListView() {

		super();
	}

	public static VendorPaymentsListView getInstance() {

		return new VendorPaymentsListView();
	}

	@Override
	protected Action getAddNewAction() {

		return VendorsActionFactory.getNewVendorPaymentAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.addANewSupplierPayment(), FinanceApplication
				.getVendorsMessages().addANewVendorPayment());
	}

	@Override
	protected String getListViewHeading() {

		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.supplierPaymentList(), FinanceApplication.getVendorsMessages()
				.vendorPaymentsList());
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getVendorPaymentsList(this);

	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false);
		grid.init();
		grid.setViewType(FinanceApplication.getVendorsMessages().notIssued());
	}

	@Override
	public void onSuccess(List<PaymentsList> result) {
		super.onSuccess(result);
		grid.setViewType(FinanceApplication.getVendorsMessages().all());
	}

	@Override
	protected SelectItem getSelectItem() {
		currentView = new SelectItem(FinanceApplication.getVendorsMessages()
				.currentView());
		currentView.setHelpInformation(true);
		currentView.setValueMap(FinanceApplication.getVendorsMessages()
				.notIssued(), FinanceApplication.getVendorsMessages().issued(),
				FinanceApplication.getVendorsMessages().Voided(),
				FinanceApplication.getVendorsMessages().all()
		// "Deleted"
				);

		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");

		currentView.setDefaultValue(FinanceApplication.getVendorsMessages()
				.all());
		currentView.addChangeHandler(new ChangeHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onChange(ChangeEvent event) {
				if (viewSelect.getValue() != null) {
					grid.setViewType(currentView.getValue().toString());
					filterList(currentView.getValue().toString());
				}
			}
		});
		return currentView;
	}

	protected void filterList(String text) {
		grid.removeAllRecords();
		if (currentView.getValue().toString().equalsIgnoreCase("Not Issued")) {
			List<PaymentsList> notIssuedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Not Issued")) {
					notIssuedRecs.add(rec);
				}
			}
			grid.setRecords(notIssuedRecs);
		} else if (currentView.getValue().toString().equalsIgnoreCase("Issued")) {
			List<PaymentsList> issued = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Issued")) {
					issued.add(rec);
				}
			}
			grid.setRecords(issued);
		} else if (currentView.getValue().toString().equalsIgnoreCase("Voided")) {
			List<PaymentsList> voidedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (rec.isVoided()
						&& rec.getStatus() != ClientTransaction.STATUS_DELETED) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);
		}
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<PaymentsList> deletedRecs = new
		// ArrayList<PaymentsList>();
		// List<PaymentsList> allRecs = initialRecords;
		// for (PaymentsList rec : allRecs) {
		// if (rec.getStatus() == ClientTransaction.STATUS_DELETED) {
		// deletedRecs.add(rec);
		// }
		// }
		// grid.setRecords(deletedRecs);
		//
		// }
		if (currentView.getValue().toString().equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);
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
