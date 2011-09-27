package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;
import com.vimukti.accounter.web.client.ui.grids.BillsTable;

/**
 * 
 * @author venki.p
 * @modified by Ravi kiran.G
 * 
 */
public class BillListView extends BaseListView<BillsList> {
	SelectItem viewSelect;
	DynamicForm form;
	Label lab1;
	protected List<BillsList> allEnterBills;
	private SelectCombo currentView;
	public String viewType;
	public int transactionType;
	private BillsTable table;

	private BillListView() {
		super();
	}

	public BillListView(String viewType) {
		super();
		this.viewType = viewType;
	}

	public static BillListView getInstance() {
		return new BillListView();
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getEnterBillsAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()
				&& getCompany().getPreferences().isKeepTrackofBills())
			return Accounter.constants().addaNewBill();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().billsAndItemReceiptsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getBillsAndItemReceiptList(false, this);

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
		grid = new BillsListGrid(false);
		grid.init();

		table = new BillsTable();
		table.init();
	}

	@Override
	public void onSuccess(ArrayList<BillsList> result) {
		super.onSuccess(result);
		allEnterBills = result;
		filterList(currentView.getValue().toString());
		grid.setViewType(currentView.getValue().toString());
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(Accounter.constants().currentView());
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(Accounter.constants().open());
		listOfTypes.add(Accounter.constants().voided());
		listOfTypes.add(Accounter.constants().overDue());
		listOfTypes.add(Accounter.constants().all());
		currentView.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");

		if (this.viewType != null && !viewType.equals(""))
			currentView.setComboItem(viewType);
		else {
			currentView.setComboItem(Accounter.constants().all());
		}
		currentView
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (currentView.getSelectedValue() != null) {
							grid.setViewType(currentView.getSelectedValue());
							filterList(currentView.getSelectedValue());
						}
					}
				});
		return currentView;
	}

	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(Accounter.getFinanceConstants().open())) {
			ArrayList<BillsList> openRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if ((rec.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE
						|| rec.getType() == ClientTransaction.TYPE_CASH_EXPENSE || rec
						.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
						|| ((rec.getType() == ClientTransaction.TYPE_ENTER_BILL || rec
								.getType() == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO) && DecimalUtil
								.isGreaterThan(rec.getBalance(), 0))) {
					if (!rec.isDeleted() && !rec.isVoided())
						openRecs.add(rec);
				}
			}
			grid.setRecords(openRecs);
			table.setData(openRecs);

		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants()
				.voided())) {
			ArrayList<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);
			table.setData(voidedRecs);

		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants()
				.overDue())) {
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
			table.setData(overDueRecs);

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
		if (text.equalsIgnoreCase(Accounter.getFinanceConstants().all())) {
			ArrayList<BillsList> list = new ArrayList<BillsList>();
			list.addAll(initialRecords);
			grid.setRecords(initialRecords);
			table.setData(list);
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
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().billsAndItemReceipts();
	}

}
