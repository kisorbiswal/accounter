package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.grids.ItemsListGrid;

/**
 * 
 * @author Mandeep Singh modified by Rajesh.A
 * 
 */
public class ItemListView extends BaseListView<ClientItem> {
	ArrayList<ClientItem> allItems = new ArrayList<ClientItem>();
	private Double total = 0.00;
	private ClientItem toBeDeletedItem;
	private ArrayList<ClientItem> listOfItems = new ArrayList<ClientItem>();

	private int actionType;
	private String catageory;

	public boolean isSalesType = true;
	/*
	 * To Identify from which view the request is generated.i.e SalesItemview or
	 * PurchaseItemview
	 */
	public static boolean isPurchaseType = false;

	public ItemListView() {
		super();
	}

	public static ItemListView getInstance() {
		return new ItemListView();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = (AccounterException) caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

		allItems.remove(toBeDeletedItem);
		refreshTotal();
		// Accounter.showInformation(FinanceApplication.constants()
		// .itemDeletedSuccessfully());

	}

	public void refreshTotal() {

		total = 0.00;
		for (ClientItem item : allItems) {
			if (!DecimalUtil.isEquals(item.getSalesPrice(), 0))
				total += item.getSalesPrice();
		}
		if (totalLabel != null) {
			totalLabel.setText(Accounter.constants().totalSalesPrice() + " = "
					+ DataUtils.getAmountAsString(total));
		}
	}

	@Override
	protected NewItemAction getAddNewAction() {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return null;
		else {
			NewItemAction action = ActionFactory.getNewItemAction(true);
			if (this.catageory.equals(Global.get().customer())) {
				action.setType(3);
				return action;
			} else if (this.catageory.equals(Accounter.constants().vendor())
					|| this.catageory.equals(Accounter.constants().vendor())) {
				action.setType(3);
				return action;
			}

			action.setType(3);
			return action;
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return Accounter.constants().addNewItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().productList();
	}

	@Override
	public void initListCallback() {
	}

	@Override
	public void updateInGrid(ClientItem objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new ItemsListGrid(false);
		grid.init();

		// isPurchaseType = !isSalesType;
		if (isSalesType && isPurchaseType) {
			listOfItems = getCompany().getAllItems();
		} else if (isPurchaseType)
			listOfItems = getCompany().getPurchaseItems();
		else if (isSalesType)
			listOfItems = getCompany().getSalesItems();

		filterList(true);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientItem item : listOfItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
	}

	public void setCatageoryType(String catagory) {
		this.catageory = catagory;
		if (this.catageory.equals(Global.get().customer())) {
			isSalesType = true;
			isPurchaseType = false;
		} else if (this.catageory.equals(Accounter.constants().vendor())
				|| this.catageory.equals(Accounter.constants().vendor())) {
			isPurchaseType = true;
			isSalesType = false;
		} else if (this.catageory.equals(Accounter.messages()
				.bothCustomerAndVendor(Global.get().Customer(),
						Global.get().Vendor()))) {
			isSalesType = isPurchaseType = true;
		}
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
		return Accounter.constants().items();
	}

}
