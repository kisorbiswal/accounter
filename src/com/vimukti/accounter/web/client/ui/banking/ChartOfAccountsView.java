package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
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
	Label addAccLabel, hierLabel, lab1;

	protected List<ClientAccount> allAccounts;
	private ClientAccount toBeDelete;
	private List<ClientAccount> listOfAccounts;
	public int typeOfAccount;
	public int start, length;

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
			return messages().addNew(messages().Account());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages().payeesList(messages().Accounts());
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
	}

	@Override
	protected int getPageSize() {
		return 10;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {
		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		if (isActiveAccounts) {
			viewSelect.setComboItem(messages().active());
		} else {
			viewSelect.setComboItem(messages().inActive());
		}
	}

	private boolean isActiveAccounts = true;

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getAccounts(typeOfAccount,
				isActiveAccounts, start, length, this);
	}

	@Override
	public void onSuccess(PaginationList<ClientAccount> result) {
		listOfAccounts = result;
		start = result.getStart();
		grid.removeAllRecords();
		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
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
	protected List<String> getViewSelectTypes() {
		List<String> selectTypes = new ArrayList<String>();
		selectTypes.add(messages().active());
		selectTypes.add(messages().inActive());
		return selectTypes;
	}

	@Override
	protected void filterList(boolean isActive) {
		isActiveAccounts = isActive;
		onPageChange(0, getPageSize());
		// grid.removeAllRecords();
		// for (ClientAccount account : listOfAccounts) {
		// if (isActive) {
		// if (account.getIsActive() == true)
		// grid.addData(account);
		// // if (grid.getRecords().isEmpty()) {
		// // grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
		// // }
		// } else if (account.getIsActive() == false) {
		// grid.addData(account);
		// }
		// }
		// if (grid.getRecords().isEmpty()) {
		// grid.addEmptyMessage(messages().noRecordsToShow());
		// }
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
		return messages().Account();
	}

}
