package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.BankingMessages;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.MakeDepositAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;

public class MakeDepositTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionMakeDeposit> {
	List<Integer> selectedValues = new ArrayList<Integer>();
	private MakeDepositAccountCombo accountCombo;
	private VendorCombo vendorsCombo;
	private CustomerCombo customersCombo;
	private Double totallinetotal = 0.0;
	private Double grandTotal = 0.0;
	@SuppressWarnings("unused")
	private SelectItem typeCombo;

	public MakeDepositTransactionGrid() {
		super(false, true);
	}

	@Override
	public void init() {
		super.init();
		createControls();
		initTransactionData();
		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			setAllTransactns(transactionObject.getTransactionMakeDeposit());
			if (transactionObject.getID() != null) {
				canDeleteRecord(false);
				// canAddRecord(false);
			}
		}

	}

	private void setAllTransactns(
			List<ClientTransactionMakeDeposit> transactionMakeDeposits) {
		removeAllRecords();
		int count = 0;
		for (ClientTransactionMakeDeposit makeDeposit : transactionMakeDeposits) {
			addData(makeDeposit);
			selectRow(count);
			count++;
		}
		// setRecords(transactionMakeDeposit);
		updateTotals();
	}

	@Override
	protected void onSelectionChanged(ClientTransactionMakeDeposit obj,
			int row, boolean isChecked) {
		updateTotals();
	};

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllRows();
		} else
			resetValues();
		super.onHeaderCheckBoxClick(isChecked);
	};

	public void resetValues() {

		for (ClientTransactionMakeDeposit obj : this.getRecords()) {
			selectedValues.remove((Integer) indexOf(obj));
			totallinetotal = 0.0;
			// super.updateFooterValues(DataUtils
			// .getAmountAsString(totallinetotal), 3);
			transactionView.updateNonEditableItems();
		}
	}

	public void selectAllRows() {
		for (ClientTransactionMakeDeposit obj : this.getRecords()) {
			if (!isSelected(obj)) {
				((CheckBox) this.body.getWidget(indexOf(obj), 0))
						.setValue(true);
				selectedValues.add(indexOf(obj));
				// updateValue(obj);
			}
		}
		updateTotals();
	}

	public boolean isSelected(ClientTransactionMakeDeposit transactionList) {
		return ((CheckBox) getWidget(indexOf(transactionList), 0)).getValue();
	}

	public void updateTotals() {
		List<ClientTransactionMakeDeposit> records = getRecords();
		totallinetotal = 0.0;
		for (ClientTransactionMakeDeposit rec : records)
			totallinetotal += rec.getAmount();

		// super
		// .updateFooterValues(
		// DataUtils.getAmountAsString(totallinetotal), 3);
		transactionView.updateNonEditableItems();
	}

	private void initTransactionData() {
	}

	private void createControls() {
		addRecordClickHandler(new RecordClickHandler<ClientTransactionMakeDeposit>() {

			@Override
			public boolean onRecordClick(ClientTransactionMakeDeposit core,
					int column) {
				if (column == 1) {
					if (core.getAccount() != null
							&& !core.getAccount().equals("")) {
						accountCombo.setComboItem(Accounter
								.getCompany().getAccount(core.getAccount()));
					} else
						accountCombo.setValue("");
				} else if (core.getIsNewEntry() && column == 4) {
					deleteRecord(core);
				}
				return false;
			}
		});

		accountCombo = new MakeDepositAccountCombo(Accounter
				.getBankingsMessages().accounts());
		accountCombo.setGrid(this);
		// accountCombo.setWidth("600");
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAccount(selectItem.getID());
						setText(currentRow, currentCol, selectItem.getName());
					}
				});
		vendorsCombo = new VendorCombo(UIUtils.getVendorString(
				Accounter.getVendorsMessages().supplier(),
				Accounter.getVendorsMessages().vendor()));
		vendorsCombo.setGrid(this);
		vendorsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedObject.setVendor(selectItem.getID());
						setText(currentRow, currentCol, selectItem.getName());
					}
				});
		customersCombo = new CustomerCombo(Accounter
				.getCustomersMessages().customer());
		customersCombo.setGrid(this);
		customersCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						selectedObject.setCustomer(selectItem.getID());
						setText(currentRow, currentCol, selectItem.getName());
					}
				});

		// typeCombo = new SelectItem();
		// typeCombo.setValueMap("Financial Account", "Vendor", "Customer");
		// typeCombo.addChangeHandler(new ChangeHandler() {
		//
		// @Override
		// public void onChange(ChangeEvent event) {
		//
		// }
		// });
		// addFooterValues("", FinanceApplication.getBankingsMessages()
		// .totalAmount(), " ", "", "0.00");
		// this.addFooterValue("Total Amount", 2);
		addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientTransactionMakeDeposit>() {

			@Override
			public void OnCellDoubleClick(ClientTransactionMakeDeposit core,
					int column) {
				switch (column) {
				case 3:
					if (core.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT) {
						if (core.getAccount() != null)
							accountCombo
									.setComboItem(Accounter
											.getCompany().getAccount(
													core.getAccount()));
					} else if (core.getType() == ClientTransactionMakeDeposit.TYPE_CUSTOMER) {
						if (core.getCustomer() != null)
							customersCombo.setComboItem(Accounter
									.getCompany().getCustomer(
											core.getCustomer()));
					} else if (core.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR) {
						if (core.getVendor() != null)
							vendorsCombo.setComboItem(Accounter
									.getCompany().getVendor(core.getVendor()));
					}
					break;
				default:
					break;
				}
			}
		});
	}

	// @Override
	// public void updateFooterValues(String value, int index) {
	// // TODO Auto-generated method stub
	// super.updateFooterValues(value, index);
	// }

	@Override
	public void updateData(ClientTransactionMakeDeposit obj) {
		super.updateData(obj);
	}

	@Override
	public void updateRecord(ClientTransactionMakeDeposit obj, int row, int col) {
		super.updateRecord(obj, row, col);
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		for (ClientTransactionMakeDeposit record : getRecords()) {
			if (((record.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT && record
					.getAccount() == null)
					|| (record.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR && record
							.getVendor() == null) || (record.getType() == ClientTransactionMakeDeposit.TYPE_CUSTOMER && record
					.getCustomer() == null))) {
				throw new InvalidTransactionEntryException(
						"Please Select Valid  "
								+ getTypeAsString(record.getType())
								+ "  in Transaction Grid");
			}

		}
		if (DecimalUtil.isLessThan(totallinetotal, 0.0)) {
			Accounter.showError(AccounterErrorType.InvalidTransactionAmount);
			return false;
		}
		return true;
	}

	private String getTypeAsString(int type) {
		switch (type) {
		case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
			return Accounter.getCustomersMessages().account();
		case ClientTransactionMakeDeposit.TYPE_VENDOR:
			return UIUtils.getVendorString(Accounter
					.getVendorsMessages().supplier(), Accounter
					.getVendorsMessages().vendor());
		case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
			return Accounter.getCustomersMessages().customer();
		default:
			return Accounter.getCustomersMessages().type();
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 4)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		if (index == 0 || index == 2 || index == 3)
			return 150;
		if (index == 1 )
			return 450;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		// case 0:
		// return ListGrid.COLUMN_TYPE_TEXT;
		// case 1:
		// return ListGrid.COLUMN_TYPE_TEXT;
		// case 2:
		// return ListGrid.COLUMN_TYPE_TEXT;
		case 0:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			// FIXME--This delete cell should be disabled for the old records
			// and is enabled for the newly created records.
			return ListGrid.COLUMN_TYPE_IMAGE;
		}

		return 0;
	}

	@Override
	protected void onValueChange(ClientTransactionMakeDeposit obj, int index,
			Object value) {
		if (value.toString().equals("Financial Account")) {
			selectedObject
					.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
			setText(currentRow, currentCol, "Financial Account");
		} else if (value.toString().equals("Vendor")) {
			selectedObject.setType(ClientTransactionMakeDeposit.TYPE_VENDOR);
			setText(currentRow, currentCol, "Vendor");
		} else if (value.toString().equals("Customer")) {
			selectedObject.setType(ClientTransactionMakeDeposit.TYPE_CUSTOMER);
			setText(currentRow, currentCol, "Customer");
		}
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	@Override
	protected Object getColumnValue(ClientTransactionMakeDeposit obj, int col) {

		switch (col) {
		// case 0:
		// return UIUtils.getDateStringFormat(new Date(
		// obj.getDate() != 0 ? obj.getDate() : new Date().getTime()));
		// case 1:
		// return obj.getNumber() != 0 ? obj.getNumber() : "";
		// case 2:
		// return obj.getPaymentMethod() != null ? obj.getPaymentMethod() :
		// "";
		case 0:
			switch (obj.getType()) {
			case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
				// return "Financial Account";
				return Accounter.getBankingsMessages().transfer();
			case ClientTransactionMakeDeposit.TYPE_VENDOR:
				return Accounter.getVendorsMessages().vendor();
			case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
				return Accounter.getCustomersMessages().customer();
			default:
				return "";
			}
		case 1:
			return getNameValue(obj);
		case 2:
			return obj.getReference() != null ? obj.getReference() : "";
		case 3:
			return DataUtils.getAmountAsString(obj.getAmount());
		case 4:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";

	}

	protected void transactionItemRecordDeleted() {
		updateTotals();
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	@Override
	public void editComplete(ClientTransactionMakeDeposit item, Object value,
			int col) {
		// FIXME need to implement warnings
		try {
			switch (col) {
			// case 0:
			//
			// case 1:
			//
			// case 2:
			//
			// case 3:
			//
			// case 4:

			case 2:
				item.setReference(value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "");
				break;

			case 3:
				String lineTotalAmtString = value.toString();
				// if (lineTotalAmtString.contains(""
				// + UIUtils.getCurrencySymbol() + "")) {
				// lineTotalAmtString = lineTotalAmtString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				Double lineTotal = Double.parseDouble(DataUtils
						.getReformatedAmount(lineTotalAmtString)
						+ "");

				if (!AccounterValidator.validateAmount(lineTotal, true)) {
					lineTotal = 0.0D;
					item.setAmount(lineTotal);
				} else {
					item.setAmount(lineTotal);
				}

				break;
			default:
				break;
			}
		} catch (Exception e) {
			Accounter.showError(AccounterErrorType.INVALIDENTRY);
		}
		updateTotals();
		updateData(item);
	}

	@Override
	protected String[] getSelectValues(ClientTransactionMakeDeposit obj, int col) {
		switch (col) {
		case 0:
			if (Accounter.getCompany().getAccountingType() == 1) {
				return new String[] {
						Accounter.getCustomersMessages()
								.financialAccount(),
						Accounter.getVendorsMessages().vendor(),
						Accounter.getCustomersMessages().customer(),
						Accounter.getVATMessages().VAT() };
			} else {
				return new String[] {
						Accounter.getCustomersMessages()
								.financialAccount(),
						Accounter.getVendorsMessages().vendor(),
						Accounter.getCustomersMessages().customer() };
			}

		}
		return null;
	}

	private Object getNameValue(ClientTransactionMakeDeposit obj) {
		String name = "";
		switch (obj.getType()) {
		case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
			ClientAccount account = Accounter.getCompany().getAccount(
					obj.getAccount());
			name = account != null ? account.getName() : "";
			break;
		case ClientTransactionMakeDeposit.TYPE_VENDOR:
			ClientVendor vendor = Accounter.getCompany().getVendor(
					obj.getVendor());
			name = vendor != null ? vendor.getName() : "";
			break;
		case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
			ClientCustomer customer = Accounter.getCompany()
					.getCustomer(obj.getCustomer());
			name = customer != null ? customer.getName() : "";
			break;
		}
		return name;
	}

	@Override
	protected String[] getColumns() {
		BankingMessages bankingConstants = GWT.create(BankingMessages.class);
		return new String[] { // bankingConstants.date(),
				// bankingConstants.no(), bankingConstants.paymentMethod(),
				bankingConstants.receivedFrom(),
				bankingConstants.accountFrom(), bankingConstants.reference(),
				bankingConstants.amount(), "" };
	}

	@Override
	protected boolean isEditable(ClientTransactionMakeDeposit obj, int row,
			int col) {
		if (!obj.getIsNewEntry())
			return false;
		switch (col) {
		// case 0:
		case 1:
		case 2:
		case 3:
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionMakeDeposit obj,
			int colIndex) {
		CustomCombo<E> combo = null;
		switch (colIndex) {
		case 1:

			if (obj.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT) {
				combo = (CustomCombo<E>) accountCombo;
			} else if (obj.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR) {
				combo = (CustomCombo<E>) vendorsCombo;
			} else if (obj.getType() == ClientTransactionMakeDeposit.TYPE_CUSTOMER) {
				combo = (CustomCombo<E>) customersCombo;
			}

			combo.downarrowpanel.getElement().getStyle().setMarginLeft(-20,
					Unit.PX);
		default:
			break;
		}

		return combo;
	}

	// @Override
	// protected void onClick(ClientTransactionMakeDeposit obj, int row, int
	// index) {
	// switch (index) {
	// case 4:
	// if (obj != null && obj.getIsNewEntry())
	// deleteRecord(obj);
	// break;
	// case 1:
	// if (core.getAccount() != null
	// && !core.getAccount().equals("")) {
	// accountCombo.setComboItem(FinanceApplication
	// .getCompany().getAccount(core.getAccount()));
	// } else
	// accountCombo.setValue("");
	// break;
	// default:
	// break;
	// }
	// }

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		return null;
	}

	@Override
	public void setTaxCode(String taxCode) {
		// TODO Auto-generated method stub

	}

}
