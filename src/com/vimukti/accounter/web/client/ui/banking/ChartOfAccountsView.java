package com.vimukti.accounter.web.client.ui.banking;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
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

	AccounterConstants bankingConstants = Accounter.constants();
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
		AccounterException accounterException = (AccounterException) caught;
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
			return ActionFactory.getNewAccountAction();
		else
			return null;

	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return bankingConstants.addNewCategory();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().accountsList(Global.get().Account());
	}

	@Override
	public void initListCallback() {

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
		if (typeOfAccount == 0)
			listOfAccounts = getCompany().getAccounts();
		else
			listOfAccounts = getCompany().getAccounts(typeOfAccount);
		filterList(true);
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
					grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
				}
			}

		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
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
	public void updateGrid(IAccounterCore core) {
		if (core.getObjectType() == grid.getType()) {
			List<ClientAccount> accountsList = grid.getRecords();

			IAccounterCore obj = Utility.getObject(accountsList, core.getID());
			switch (cmd) {
			case AccounterCommand.CREATION_SUCCESS:
			case AccounterCommand.UPDATION_SUCCESS:
				if (obj != null)
					accountsList.remove(obj);
				updateAccountsInSortedOrder(accountsList, (ClientAccount) core);
				break;
			case AccounterCommand.DELETION_SUCCESS:
				if (obj != null) {
					grid.deleteRecord(grid.indexOf(obj));
					if (records != null)
						records.remove(obj);
				}
				break;

			}
		}

	}

	private void updateAccountsInSortedOrder(List<ClientAccount> accountsList,
			ClientAccount toBeAddedAccount) {
		String firstNumber = "";
		String nextNumber = "";
		ClientAccount account = null;
		int index;

		String toBeAddedNumber = toBeAddedAccount.getNumber();

		Iterator<ClientAccount> iterator = accountsList.iterator();

		while (iterator.hasNext()) {
			account = iterator.next();
			nextNumber = account.getNumber();
			if (toBeAddedAccount.getType() == account.getType()
					&& firstNumber.compareTo(toBeAddedNumber) < 0
					&& nextNumber.compareTo(toBeAddedNumber) > 0) {
				index = accountsList.indexOf(account);
				accountsList.add(index, toBeAddedAccount);
				break;
			} else {
				firstNumber = nextNumber;
			}
		}

		this.grid.setRecords(accountsList);

	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().accounts(Global.get().Account());
	}

}
