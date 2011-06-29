package com.vimukti.accounter.web.client.ui.banking;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.grids.ChartOfAccountsListGrid;

/**
 * 
 * @author Mandeep Singh Modified by Ravi Kira.G
 * 
 */
public class ChartOfAccountsView extends BaseListView<ClientAccount> {
	SelectCombo viewSelect;
	Label addAccLabel, hierLabel, lab1;

	BankingMessages bankingConstants = GWT.create(BankingMessages.class);
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
	public void deleteFailed(Throwable caught) {
		Accounter.showInformation(FinanceApplication.getBankingsMessages()
				.youCantDeleteThisAccount());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteSuccess(Boolean result) {
		grid.deleteRecord(toBeDelete);
		allAccounts.remove(toBeDelete);
		// Accounter.showInformation(toBeDelete.getName()
		// + FinanceApplication.getBankingsMessages()
		// .isDeletedSuccessfully());
	}

	public static ChartOfAccountsView getInstance() {
		return new ChartOfAccountsView();
	}

	@Override
	protected Action getAddNewAction() {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return CompanyActionFactory.getNewAccountAction();
		else
			return null;

	}

	@Override
	protected String getAddNewLabelString() {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return bankingConstants.addNewCategory();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return bankingConstants.financeCategoryList();
	}

	@Override
	public void initListCallback() {

	}

	@Override
	public void updateInGrid(ClientAccount objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new ChartOfAccountsListGrid(false);
		grid.init();
		// grid.setHeight("200");
		if (typeOfAccount == 0)
			listOfAccounts = FinanceApplication.getCompany().getAccounts();
		else
			listOfAccounts = FinanceApplication.getCompany().getAccounts(
					typeOfAccount);
		filterList(true);
	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public void updateGrid(IAccounterCore core) {
		if (core.getObjectType() == grid.getType()) {
			List<ClientAccount> accountsList = grid.getRecords();

			IAccounterCore obj = Utility.getObject(accountsList, core
					.getStringID());
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

	@SuppressWarnings("unchecked")
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
		return FinanceApplication.getCompanyMessages().accounts();
	}

}
