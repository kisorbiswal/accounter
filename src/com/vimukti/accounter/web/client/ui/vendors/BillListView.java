package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;

/**
 * 
 * @author venki.p
 * @modified by Ravi kiran.G
 * 
 */
public class BillListView extends TransactionsListView<BillsList> {
	protected List<BillsList> allEnterBills;
	private int transactionType;

	private BillListView() {
		super(Accounter.messages().all());
	}

	public BillListView(String viewType) {
		super(viewType);
	}

	public BillListView(String viewType, int listType) {
		super(viewType);
		this.transactionType = listType;
	}

	public static BillListView getInstance() {
		return new BillListView();
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
				return ActionFactory.getNewCreditMemoAction();
			}
			return ActionFactory.getEnterBillsAction();
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()
				&& getCompany().getPreferences().isKeepTrackofBills()) {
			if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
				return messages().customerCreditNote(Global.get().Vendor());
			}
			return messages().addaNewBill();
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) {
			return messages().payeeCreditNotes(Global.get().Vendor());
		}
		return messages().billsAndItemReceiptsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getBillsAndItemReceiptList(false,
				transactionType, getStartDate().getDate(),
				getEndDate().getDate(), this);
	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
	}

	// @Override
	// protected void createControls() {
	// super.createControls();
	// ((VerticalPanel) gridLayout.getParent()).add(table);
	// HorizontalPanel panel = new HorizontalPanel();
	// panel.add(table);
	// panel.setWidth("100%");
	// panel.setCellHeight(table, "100%");
	// panel.setCellWidth(table, "70%");
	// table.setWidth("100%");
	// ((VerticalPanel) gridLayout.getParent()).add(panel);
	// // add(table);
	// }

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false, transactionType);
		grid.init();
	}

	@Override
	public void onSuccess(ArrayList<BillsList> result) {
		super.onSuccess(result);
		allEnterBills = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
		grid.sort(10, false);
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().open());
		listOfTypes.add(messages().voided());
		if (transactionType == 0) {
			listOfTypes.add(messages().overDue());
		}
		listOfTypes.add(messages().all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(messages().open())) {
			ArrayList<BillsList> openRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if ((rec.getType() == ClientTransaction.TYPE_ENTER_BILL || rec
						.getType() == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO)
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					if (!rec.isDeleted() && !rec.isVoided()) {
						openRecs.add(rec);
					}
				}
			}
			grid.setRecords(openRecs);

		} else if (text.equalsIgnoreCase(messages().voided())) {
			ArrayList<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (text.equalsIgnoreCase(messages().overDue())) {
			ArrayList<BillsList> overDueRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.getType() == ClientTransaction.TYPE_ENTER_BILL
						&& new ClientFinanceDate().after(rec.getDueDate())
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					overDueRecs.add(rec);
				}
			}
			grid.setRecords(overDueRecs);
		}
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<BillsList> deletedRecs = new ArrayList<BillsList>();
		// List<BillsList> allRecs = initialRecords;
		// for (BillsList rec : allRecs) {
		// if (rec.isDeleted()) {
		// deletedRecs.add(rec);
		// }
		// }
		//
		// grid.setRecords(deletedRecs);
		// }
		if (text.equalsIgnoreCase(messages().all())) {
			ArrayList<BillsList> list = new ArrayList<BillsList>();
			list.addAll(initialRecords);
			grid.setRecords(initialRecords);
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages().noRecordsToShow());
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
	}

	@Override
	protected String getViewTitle() {
		return messages().billsAndItemReceipts();
	}
}
