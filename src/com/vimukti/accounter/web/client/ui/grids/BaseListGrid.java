package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IAccounterWidget;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public abstract class BaseListGrid<T> extends ListGrid<T> implements
		IAccounterWidget {

	private List<Integer> cellsWidth = new ArrayList<Integer>();
	protected IAccounterCRUDServiceAsync rpcDoSerivce;
	protected AccounterConstants customerConstants;
	protected AccounterConstants salesPersonConstants;
	protected AccounterConstants vendorConstants;
	protected AccounterConstants bankingContants;
	protected AccounterConstants companyConstants;
	private int[] columnType;
	protected double total;
	protected String viewType;

	// long id;

	BaseListView view;

	public BaseListView getView() {
		return view;

	}

	public void setView(BaseListView view) {
		this.view = view;

	}

	public BaseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		initRPCService();
		this.columnType = setColTypes();
	}

	public BaseListGrid(boolean isMultiSelectionEnable, boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
		initRPCService();
		this.columnType = setColTypes();
	}

	public void init() {
		super.init();
		// addLoadingImagePanel();
	}

	@Override
	protected int getCellWidth(int index) {
		if (cellsWidth.size() > index)
			return cellsWidth.get(index);
		return -1;
	}

	@Override
	protected int getColumnType(int col) {

		return columnType[col];
	}

	@Override
	protected String[] getSelectValues(T obj, int col) {
		return null;
	}

	@Override
	protected boolean isEditable(T obj, int row, int col) {
		return false;
	}

	@Override
	protected int sort(T obj1, T obj2, int index) {
		return 0;
	}

	@Override
	protected void onClick(T obj, int row, int col) {
		if (col == 6 && !this.isVoided(obj)) {
			showWarningDialog(obj, this.getAccounterCoreType(obj),
					this.getTransactionID(obj), col);
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private long getTransactionID(T obj) {
		return 0;
	}

	public boolean isVoided(T obj) {
		return false;
	}

	public AccounterCoreType getAccounterCoreType(T obj) {
		return null;
	}

	protected void showWarningDialog(T obj, final AccounterCoreType coreType,
			final long transactionsID, final int col) {
		String msg = null;
		msg = Accounter.constants().doyouwanttoVoidtheTransaction();
		// else if (col == 7) {
		// if (!viewType.equalsIgnoreCase("Deleted"))
		// msg = "Do you want to Delete the Transaction";
		//
		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return false;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						if (col == 6)
							voidTransaction(coreType, transactionsID);
						// else if (col == 7)
						// deleteTransaction(obj);
						return true;

					}

				});
	}

	protected void voidTransaction(AccounterCoreType coreType,
			long transactionsID) {
		ViewManager.getInstance().voidTransaction(coreType, transactionsID,
				this);
	}

	protected void initRPCService() {
		this.rpcDoSerivce = Accounter.createCRUDService();

	}

	// public void setColumnType(int[] columnType) {
	// this.columnType = setColTypes();
	// }

	abstract protected int[] setColTypes();

	protected void showWarnDialog(final T object) {
		Accounter.showWarning("Do you want To Delete "
				+ ((IAccounterCore) object).getName(), AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						executeDelete(object);
						return true;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return false;
					}
				});
	}

	protected abstract void executeDelete(T object);

	protected void onValueChange(T obj, int col, Object value) {

	}

	public Double getTotal() {
		return 0.0;
	}

	public AccounterCoreType getType() {
		return null;
	}

	protected void onDoubleClick(T obj, int row, int index) {
		onDoubleClick(obj);
	};

	public void setViewType(String type) {
		viewType = type;
	}

	@Override
	public void deleteFailed(Throwable caught) {
		Accounter
				.showError("We Can't Delete .This might be Participating in Transactions");
		caught.fillInStackTrace();
	}

	@Override
	public void deleteSuccess(Boolean result) {
		// Accounter.showInformation("Deleted Successfully");
		deleteRecord(this.getSelection());
	}

	@Override
	public void saveFailed(Throwable exception) {
		if (exception instanceof InvalidOperationException) {
			Accounter.showError(exception.getMessage());
		} else
			Accounter.showError("Updation Failed!");
	}

	// public long getID() {
	// return this.id;
	// }
	//
	// public void setID(long id) {
	// this.id = id;
	// }

	public void setTotal() {
	}

	@Override
	public void addRecords(List<T> list) {
		super.addRecords(list);
	}

	@Override
	public void saveSuccess(IAccounterCore core) {
		if (core != null) {
			view.initListCallback();
		}
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}
}
