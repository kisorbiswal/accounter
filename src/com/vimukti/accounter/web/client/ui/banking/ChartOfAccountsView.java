package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ChartOfAccountsListGrid;

/**
 * 
 * @author Mandeep Singh Modified by Ravi Kira.G
 * 
 */
public class ChartOfAccountsView extends BaseListView<ClientAccount> {
	SelectCombo viewSelect;
	Label addAccLabel, hierLabel, lab1;

	protected List<ClientAccount> allAccounts;
	private ClientAccount toBeDelete;
	private List<ClientAccount> listOfAccounts;
	public int typeOfAccount;

	public ChartOfAccountsView() {
		this(0);
		isDeleteDisable = true;
	}

	public ChartOfAccountsView(int type) {
		super();
		typeOfAccount = type;
		isDeleteDisable = true;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		grid.deleteRecord(toBeDelete);
		allAccounts.remove(toBeDelete);
		// Accounter.showInformation(toBeDelete.getName()
		// + FinanceApplication.constants()
		// .isDeletedSuccessfully());
	}

	public static ChartOfAccountsView getInstance() {
		return new ChartOfAccountsView();
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			if (typeOfAccount == ClientAccount.TYPE_BANK) {
				return ActionFactory.getNewBankAccountAction();
			} else {
				return ActionFactory.getNewAccountAction();
			}
		else
			return null;

	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addNew(messages.Account());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeesList(messages.Accounts());
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getAccounts(typeOfAccount, this);
	}

	@Override
	public void onSuccess(ArrayList<ClientAccount> result) {
		listOfAccounts = result;
		filterList(true);
	}

	@Override
	public void updateInGrid(ClientAccount objectTobeModified) {
		// its not using any where

	}

	@Override
	protected void initGrid() {
		grid = new ChartOfAccountsListGrid(false);
		grid.init();
		// grid.setHeight("200");
	}

	@Override
	protected void filterList(final boolean isActive) {
		grid.removeAllRecords();
		for (ClientAccount account : listOfAccounts) {
			if (isActive) {
				if (account.getIsActive() == true)
					grid.addData(account);
				// if (grid.getRecords().isEmpty()) {
				// grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
				// }
			} else if (account.getIsActive() == false) {
				grid.addData(account);
				if (grid.getRecords().isEmpty()) {
					grid.addEmptyMessage(messages.noRecordsToShow());
				}
			}

		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		Window.scrollTo(0, 0);
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
		return messages.Account();
	}

}
