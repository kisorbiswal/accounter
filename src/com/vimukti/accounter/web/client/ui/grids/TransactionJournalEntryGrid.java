package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionJournalEntryGrid extends
		AbstractTransactionGrid<ClientEntry> {

	private OtherAccountsCombo accountsCombo;
	private CustomerCombo customerCombo;
	private VendorCombo vendorCombo;
	private TAXCodeCombo taxcodeCombo;
	double debit, credit;
	boolean isEdit;
	AccounterConstants accounterConstants = Accounter.constants();
	/*
	 * holds all records debit n credit totals
	 */
	private double creditTotal;
	private double debitTotal;

	private String voucherNumber;

	/**
	 * holds actual voucher number that taken from server
	 */
	private String tempVoucherNumber;

	private int voucherType;

	public TransactionJournalEntryGrid(boolean isEdit) {
		super(false, true);
		this.isEdit = isEdit;
	}

	@Override
	public void init() {
		isEnable = false;
		super.init();
		createControls();
		initTransactionData();
	}

	private void createControls() {
		setWidth("100%");
		// setSize("100%", "200px");
		addRecordClickHandler(new RecordClickHandler<ClientEntry>() {

			@Override
			public boolean onRecordClick(ClientEntry core, int column) {
				// if (column == 7
				// && !(FinanceApplication.getCompany()
				// .getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				// && !isEdit)
				if (column == 2 && !isEdit) {
					if (core.getAccount() != 0) {
						accountsCombo.setComboItem(Accounter.getCompany()
								.getAccount(core.getAccount()));
					} else
						accountsCombo.setValue("");
				} else if (column == 6 && !isEdit)

					refreshValuesOnDeleteRecord(core);
				// else if (column == 8
				// && FinanceApplication.getCompany().getAccountingType() ==
				// ClientCompany.ACCOUNTING_TYPE_UK
				// && !isEdit)
				// refreshValuesOnDeleteRecord(core);
				return false;
			}
		});

		vendorCombo = new VendorCombo("");
		vendorCombo.setGrid(this);
		vendorCombo.setRequired(true);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor vendor) {
						selectedObject.setVendor(vendor.getID());
						selectedObject.setCustomer(0);
						selectedObject.setAccount(0);
						setText(currentRow, currentCol, vendor.getName());

					}

				});
		accountsCombo = new OtherAccountsCombo("");
		accountsCombo.setGrid(this);
		accountsCombo.setRequired(true);
		// accountsCombo.downarrowpanel.getElement().getStyle()
		// .setMarginLeft(-13, Unit.PX);
		// accountsCombo.setWidth("600");
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount account) {
						selectedObject.setAccount(account.getID());
						selectedObject.setVendor(0);
						selectedObject.setCustomer(0);
						setText(currentRow, currentCol, account.getName());
					}

				});
		customerCombo = new CustomerCombo("");
		customerCombo.setGrid(this);
		customerCombo.setRequired(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					public void selectedComboBoxItem(ClientCustomer customer) {
						selectedObject.setCustomer(customer.getID());
						selectedObject.setAccount(0);
						selectedObject.setVendor(0);
						setText(currentRow, currentCol, customer.getName());
					}

				});
		// vatcodeCombo = new VatCodeCombo("");
		// vatcodeCombo.setGrid(this);
		// vatcodeCombo.setRequired(true);
		// vatcodeCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientVATCode>() {
		//
		// public void selectedComboBoxItem(ClientVATCode taxCode) {
		// selectedObject.setTaxCode(taxCode.getID());
		// setText(currentRow, currentCol, taxCode.getName());
		// }
		//
		// });

		// this.addFooterValues(FinanceApplication.constants()
		// .totalAmount(), "", "", "", "0.00", "0.00", "");
		// this.addFooterValue("Total Amount", 0);

		addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientEntry>() {

			@Override
			public void OnCellDoubleClick(ClientEntry core, int column) {
				switch (column) {
				case 3:
					// if (core.getType() == ClientEntry.TYPE_FINANCIAL_ACCOUNT)
					// {
					if (core.getAccount() != 0)
						accountsCombo.setComboItem(Accounter.getCompany()
								.getAccount(core.getAccount()));
					// } else if (core.getType() == ClientEntry.TYPE_CUSTOMER) {
					// if (core.getCustomer() != null)
					// customerCombo.setComboItem(FinanceApplication
					// .getCompany().getCustomer(
					// core.getCustomer()));
					// } else if (core.getType() == ClientEntry.TYPE_VENDOR) {
					// if (core.getVendor() != null)
					// vendorCombo.setComboItem(FinanceApplication
					// .getCompany().getVendor(core.getVendor()));
					// }
					// // to be Implemented for VAT in UK else if
					// else if (core.getType() == ClientEntry.TYPE_VAT) {
					// if (core.getVendor() != null)
					// accountsCombo
					// .setComboItem(FinanceApplication
					// .getCompany().getAccount(
					// core.getAccount()));
					// }

					break;

				default:
					break;
				}
			}
		});
	}

	public void initTransactionData() {
		initAccountsCombo();
		// initCustomersCombo();
		// initTaxCodesCombo();
		// addVendorGroupList();

	}

	private void initAccountsCombo() {
		List<ClientAccount> accounts = getCompany().getActiveAccounts();
		if (accounts != null) {
			accountsCombo.initCombo(accounts);
		}

	}

	private void initCustomersCombo() {
		List<ClientCustomer> customers = getCompany().getActiveCustomers();
		if (customers != null) {
			customerCombo.initCombo(customers);
		}
	}

	private void initTaxCodesCombo() {
		List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
		if (taxCodes != null)
			taxcodeCombo.initCombo(taxCodes);
	}

	public void addVendorGroupList() {
		List<ClientVendor> vendors = getCompany().getActiveVendors();
		if (vendors != null) {
			vendorCombo.initCombo(vendors);
		}
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
		this.tempVoucherNumber = voucherNumber;
	}

	public double getTotalCredittotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}

	public double getTotalDebittotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	/*
	 * This method invoked when a record deleted from the grid.And it invokes
	 * all mothods necessary to update footer & totals
	 */
	private void refreshValuesOnDeleteRecord(ClientEntry entry) {
		deleteRecord(entry);
		updateFooterValues();
		// To reset the vocher number if all the records deleted.
		if (getRecords().size() == 0)
			tempVoucherNumber = voucherNumber;
	}

	@Override
	protected String[] getSelectValues(ClientEntry obj, int col) {
		switch (col) {
		case 2:
			return new String[] {
					"",
					Accounter.messages().financialAccount(
							Global.get().Account()), Global.get().Vendor(),
					Global.get().customer() };
		}

		return null;
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();

		// Validates account name
		List<ClientEntry> entrylist = this.getRecords();
		for (ClientEntry entry : entrylist) {
			int row = this.objects.indexOf(entry);
			if (AccounterValidator.isEmpty(getColumnValue(entry, 2))) {
				if (!result.haveErrors()) {
					result.addError(
							row + "," + 2,
							Accounter.messages().pleaseEnter(
									getTypeAsString(entry, entry.getType())));
				}
			}

		}
		return result;

	}

	public boolean isValidTotal() {
		if (getTotalCredittotal() == getTotalDebittotal())
			return true;
		else {
			return false;
		}
	}

	@Override
	protected void onValueChange(ClientEntry obj, int index, Object value) {
		switch (index) {
		case 2:
			// if (value.toString().equals("Financial Account")) {
			// voucherType = ClientEntry.TYPE_FINANCIAL_ACCOUNT;
			// selectedObject.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
			// setText(currentRow, currentCol, "Financial Account");
			// } else if (value.toString().equals(
			// UIUtils.getVendorString("Supplier", "Vendor"))) {
			// voucherType = ClientEntry.TYPE_VENDOR;
			// selectedObject.setType(ClientEntry.TYPE_VENDOR);
			// setText(currentRow, currentCol, UIUtils.getVendorString(
			// "supplier", "Vendor"));
			// } else if (value.toString().equals("Customer")) {
			// voucherType = ClientEntry.TYPE_CUSTOMER;
			// selectedObject.setType(ClientEntry.TYPE_CUSTOMER);
			// setText(currentRow, currentCol, "Customer");
			// } else if (value.toString().equals("VAT")) {
			// voucherType = ClientEntry.TYPE_VAT;
			// selectedObject.setType(ClientEntry.TYPE_VAT);
			// setText(currentRow, currentCol, "VAT");
			// }
			break;
		// case 7:
		// selectedObject.setVatCode(value.toString());
		// setText(currentRow, currentCol, value.toString());
		// break;
		default:
			break;
		}

		super.onValueChange(obj, index, value);
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 80;
		if (index == 2)

			return 200;
		if (index == 3)
			return 100;
		if (index == 1 || index == 4 || index == 5)
			return 100;
		if (index == 6)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_DATE;
			// case 2:
			// return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK)
			// return ListGrid.COLUMN_TYPE_SELECT;
			// else
			return ListGrid.COLUMN_TYPE_IMAGE;
			// case 8:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK)
			// return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		}
	}

	@Override
	protected Object getColumnValue(ClientEntry entry, int col) {
		switch (col) {
		case 0:
			return entry.getVoucherNumber() + "";
		case 1:
			return entry.getEntryDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(entry
							.getEntryDate())) : null;
			// case 2:
			// return getTypeAsString(entry, entry.getType());
		case 2:
			return getAccountAsString(entry, entry.getType());
		case 3:
			return entry.getMemo() + "";
		case 4:
			return amountAsString(getAmountInForeignCurrency(entry.getDebit()));
		case 5:
			return amountAsString(getAmountInForeignCurrency(entry.getCredit()));
		case 6:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// return entry.getVatCode() != null ? entry.getVatCode() : "";
			// } else
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
			// case 8:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK)
			// return FinanceApplication.getFinanceMenuImages().delete()
			// .getURL();
		default:
			break;
		}
		return null;
	}

	/*
	 * This method returns the string(which'll display in the record) for
	 * selected vochertype .
	 */
	private String getTypeAsString(ClientEntry entry, int type) {
		switch (type) {
		case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
			return Accounter.messages().account(Global.get().account());
		case ClientEntry.TYPE_CUSTOMER:
			return Global.get().customer();
		case ClientEntry.TYPE_VENDOR:
			return Global.get().vendor();
			// case ClientEntry.TYPE_VAT:
			// return "VAT";
		}
		return null;
	}

	/*
	 * This method returns the display name of the type selected from the
	 * "Account" column of the grid
	 */
	private String getAccountAsString(ClientEntry entry, int vocherType) {
		IAccounterCore coreObj = null;

		switch (vocherType) {

		case ClientEntry.TYPE_FINANCIAL_ACCOUNT:
			coreObj = getCompany().getAccount(entry.getAccount());
			break;
		case ClientEntry.TYPE_CUSTOMER:
			coreObj = getCompany().getCustomer(entry.getCustomer());
			break;
		case ClientEntry.TYPE_VENDOR:
			coreObj = getCompany().getVendor(entry.getVendor());
			break;
		case ClientEntry.TYPE_VAT:
			coreObj = getCompany().getTAXCode(entry.getTaxCode());
		}
		return coreObj != null ? coreObj.getName() : "";
	}

	@Override
	protected String[] getColumns() {
		AccounterConstants companyConstants = Accounter.constants();
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// return new String[] { companyConstants.voucherNo(),
		// companyConstants.date(), companyConstants.voucherType(),
		// companyConstants.account(), companyConstants.memo(),
		// companyConstants.debit(), companyConstants.credit(),
		// "VAT Code", "" };
		// } else
		return new String[] {
				companyConstants.voucherNo(),
				companyConstants.date(),
				// companyConstants.voucherType(),
				Accounter.messages().account(Global.get().account()),
				companyConstants.memo(), companyConstants.debit(),
				companyConstants.credit(), "" };
	}

	@Override
	protected boolean isEditable(ClientEntry obj, int row, int col) {
		switch (col) {
		case 0:
			return false;
			// case 7:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// if (voucherType ==
			// ClientEntry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT
			// || voucherType == ClientEntry.TYPE_VAT) {
			// return true;
			// } else {
			// return false;
			// }
			// }

		default:
			break;
		}
		return true;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientEntry obj, int colIndex) {
		switch (colIndex) {
		case 2:
			// if (obj.getType() == ClientEntry.TYPE_FINANCIAL_ACCOUNT)
			return (CustomCombo<E>) accountsCombo;
			// else if (obj.getType() == ClientEntry.TYPE_CUSTOMER)
			// return (CustomCombo<E>) customerCombo;
			// else if (obj.getType() == ClientEntry.TYPE_VENDOR)
			// return (CustomCombo<E>) vendorCombo;
			// else if (obj.getType() == ClientEntry.TYPE_VAT)
			// return (CustomCombo<E>) accountsCombo;
			// break;
			// case 7:
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// return (CustomCombo<E>) vatcodeCombo;
			// }
		default:
			break;
		}
		return null;
	}

	public void setAllRecords(List<ClientEntry> records) {
		for (int i = 0; i < records.size(); i++) {
			CustomCombo<?> combo = getCustomCombo(records.get(i), 3);
			if (combo != null) {
				String itemName = "";
				itemName = getAccountAsString(records.get(i), records.get(i)
						.getType());
				combo.setSelected(itemName);
			}
		}
		setRecords(records);
	}

	@Override
	public void editComplete(ClientEntry editingRecord, Object value, int col) {

		try {
			switch (col) {
			case 0:
				editingRecord.setVoucherNumber(value.toString());
				setVoucherNumber(value.toString());
				break;
			case 1:
				editingRecord.setEntryDate(UIUtils.stringToDate(
						value.toString()).getDate());
				break;
			case 3:
				editingRecord.setMemo(value.toString());
				break;
			case 4:
				Double val = DataUtils.getReformatedAmount(value.toString());
				editingRecord.setCredit(0.0d);
				editingRecord.setDebit(getAmountInBaseCurrency(val));
				break;
			case 5:
				Double crd = DataUtils.getReformatedAmount(value.toString());

				editingRecord.setDebit(0.0d);
				editingRecord.setCredit(getAmountInBaseCurrency(crd));
				break;

			// case 7:

			default:
				break;
			}
			updateData(editingRecord);
		} catch (Exception e) {
			e.printStackTrace();
			Accounter.showError(accounterConstants.invalidateEntry());
			switch (col) {
			case 0:
				editingRecord
						.setVoucherNumber(editingRecord.getVoucherNumber());
			case 1:
				editingRecord
						.setEntryDate(editingRecord.getEntryDate() != 0 ? editingRecord
								.getEntryDate() : new ClientFinanceDate()
								.getDate());
				break;
			case 4:
				editingRecord.setDebit(editingRecord.getDebit());
				break;
			case 5:
				editingRecord.setCredit(editingRecord.getCredit());
				break;
			}
			updateData(editingRecord);
		}
		super.editComplete(editingRecord, value, col);
		updateFooterValues();
	}

	public void onWidgetValueChanged(Widget widget, Object value) {
		if (widget instanceof DateBox) {
			this.remove(widget);

			// setText(currentRow, currentCol, value.toString());

			editComplete(selectedObject, value, 1);
		} else {
			this.remove(widget);

			setText(currentRow, currentCol, value.toString());

			editComplete(selectedObject, value, currentCol);
			currentCol = 0;
			currentRow = 0;
		}
	}

	@Override
	public void addData(ClientEntry obj) {
		if (!isEdit)
			onRecordAdd(obj);
		super.addData(obj);
		updateFooterValues();
	}

	public double getGroupRecordsDueTotal() {
		List<ClientEntry> listGrpRecs = getRecords();
		double debTotal = 0.0;
		double credTotal = 0.0;
		int counter = 0;
		String startNum = "0";
		// initialize start number with 1st record's vocher number n compare it
		// with successive records until the unmached vocher number is reached.
		// After that move to next group(means which has same vocher number) of
		// records n repeat the same process
		// until all records are processed
		if (listGrpRecs.size() != 0)
			startNum = listGrpRecs.get(0).getVoucherNumber();
		if (listGrpRecs.size() != 0)

			for (ClientEntry rec : listGrpRecs) {
				// calculate the debit total n credittotal for the records which
				// has same vochernumber.
				if (startNum == rec.getVoucherNumber()) {
					debTotal += rec.getDebit();
					credTotal += rec.getCredit();
				} else {
					// if the vocher number doesn't match then move to nextgroup
					// of records n reset vochernumber,debit n credit totals to
					// changed group's 1st record n repeat the same process.
					startNum = rec.getVoucherNumber();
					debTotal = rec.getDebit();
					credTotal = rec.getCredit();
				}
				counter++;
				tempVoucherNumber = rec.getVoucherNumber();
			}
		// if the counter reaches to end(means no more records) then increase
		// vocher number
		if (counter == listGrpRecs.size()) {
			// if debit n credit totals are same(!=0),then increase the vocher
			// number
			if (DecimalUtil.isEquals(debTotal, credTotal)) {
				if (!DecimalUtil.isEquals(debTotal, 0)
						&& !DecimalUtil.isEquals(credTotal, 0))
					tempVoucherNumber = UIUtils
							.getStringwithIncreamentedDigit(tempVoucherNumber);
				return 0.0;
			} else
				return debTotal - credTotal;
		}
		return 0.0;
	}

	/**
	 * Checks record's debit & credit before adding to grid.
	 */
	protected void onRecordAdd(ClientEntry record) {

		double dueAmt = getGroupRecordsDueTotal();
		//
		// if (DecimalUtil.isEquals(dueAmt, 0.0)) {
		// record.setDebit(0.0);
		// record.setCredit(0.0);
		// record.setVoucherNumber(tempVoucherNumber);
		// } else if (DecimalUtil.isGreaterThan(dueAmt, 0)) {
		// record.setCredit(dueAmt);
		// record.setDebit(0.0);
		// record.setVoucherNumber(tempVoucherNumber);
		// } else if (DecimalUtil.isLessThan(dueAmt, 0.0)) {
		// record.setCredit(0.0);
		// record.setDebit(-1 * dueAmt);
		// record.setVoucherNumber(tempVoucherNumber);
		// }
	}

	/**
	 * This method invoked each time when a new record added or existing
	 * record's debit/credit values changed
	 */
	public void updateFooterValues() {
		creditTotal = 0;
		debitTotal = 0;
		for (ClientEntry rec : getRecords()) {
			creditTotal += rec.getCredit();
			debitTotal += rec.getDebit();
		}
		// updateFooterValues(amountAsString(debitTotal), 4);
		// updateFooterValues(amountAsString(creditTotal), 5);
		setCreditTotal(creditTotal);
		setDebitTotal(debitTotal);
		transactionView.updateNonEditableItems();
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {

		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {

	}

	/*
	 * Journal Entries of same voucher number should use only one customer. This
	 * method provides validation for journal entries having same voucher number
	 */
	// public boolean validateCustomers() throws
	// InvalidTransactionEntryException {
	// String tempNumber = voucherNumber;
	// List<ClientEntry> entryList = new ArrayList<ClientEntry>();
	// List<ClientEntry> gridRecords = this.getRecords();
	// ClientEntry entry1 = null;
	// ClientEntry entry2;
	// int count = 1;
	// for (ClientEntry entry : gridRecords) {
	// if (gridRecords.size() == count
	// && tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// } else if (tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// continue;
	// }
	// int i;
	// for (i = 0; i < entryList.size(); i++) {
	// entry1 = entryList.get(0);
	// if (entry1.getType() == ClientEntry.TYPE_CUSTOMER)
	// break;
	// }
	// for (int j = i; j < entryList.size(); j++) {
	//
	// entry2 = entryList.get(j);
	// if (entry2.getType() == ClientEntry.TYPE_CUSTOMER
	// && !entry1.getCustomer().equalsIgnoreCase(
	// entry2.getCustomer()))
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.journalEntryCustomer);
	//
	// }
	//
	// tempNumber = entry.getVoucherNumber();
	// entryList.clear();
	// entryList.add(entry);
	// count++;
	//
	// }
	//
	// return true;
	// }

	/*
	 * Journal Entries of same voucher number should use only one vendor. This
	 * method provides validation for journal entries having same voucher number
	 */

	// public boolean validateVendors() throws InvalidTransactionEntryException
	// {
	//
	// String tempNumber = voucherNumber;
	// List<ClientEntry> entryList = new ArrayList<ClientEntry>();
	// List<ClientEntry> gridRecords = this.getRecords();
	// ClientEntry entry1 = null;
	// ClientEntry entry2;
	// int count = 1;
	// for (ClientEntry entry : gridRecords) {
	// if (gridRecords.size() == count
	// && tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// } else if (tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// continue;
	// }
	// int i;
	// for (i = 0; i < entryList.size(); i++) {
	// entry1 = entryList.get(0);
	// if (entry1.getType() == ClientEntry.TYPE_VENDOR)
	// break;
	// }
	// for (int j = i; j < entryList.size(); j++) {
	//
	// entry2 = entryList.get(j);
	// if (entry2.getType() == ClientEntry.TYPE_VENDOR
	// && !entry1.getVendor().equalsIgnoreCase(
	// entry2.getVendor()))
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.journalEntryVendor);
	//
	// }
	//
	// tempNumber = entry.getVoucherNumber();
	// entryList.clear();
	// entryList.add(entry);
	// count++;
	//
	// }
	//
	// return true;
	// }

	// public boolean validateAccounts() throws
	// InvalidTransactionEntryException {
	// String tempNumber = voucherNumber;
	// List<ClientEntry> entryList = new ArrayList<ClientEntry>();
	// List<ClientEntry> gridRecords = this.getRecords();
	// ClientEntry entry1 = null;
	// ClientEntry entry2=null;
	// int count = 1;
	// for (ClientEntry entry : gridRecords) {
	// if (gridRecords.size() == count
	// && tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// } else if (tempNumber == entry.getVoucherNumber()) {
	// entryList.add(entry);
	// count++;
	// continue;
	// }
	// int i;
	// for (i = 0; i < entryList.size(); i++) {
	// entry1 = entryList.get(0);
	// if (entry1.getType() == ClientEntry.TYPE_FINANCIAL_ACCOUNT)
	// break;
	// }
	// for (int j =i+1; j < entryList.size(); j++) {
	//
	// entry2 = entryList.get(j);
	// if (entry1.getAccount().equalsIgnoreCase(
	// entry2.getAccount()))
	// throw new InvalidTransactionEntryException(
	// AccounterErrorType.journalEntryAccount);
	//
	// }
	//
	// tempNumber = entry.getVoucherNumber();
	// entryList.clear();
	// entryList.add(entry);
	// count++;
	//
	// }
	//
	// return true;
	// }

}