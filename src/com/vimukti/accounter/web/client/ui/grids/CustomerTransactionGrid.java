package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.customers.AbstractCustomerTransactionView;

public class CustomerTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	private SalesAccountsCombo accountsCombo;
	ServiceCombo serviceItemCombo;
	ProductCombo productItemCombo;
	TAXCodeCombo taxCodeCombo;
	VATItemCombo vatItemCombo;
	SelectCombo us_taxCombo;
	protected boolean isSalesOrderTransaction;
	private Double totallinetotal = 0.0D;
	private Double totalVat;
	private double grandTotal;

	private ClientPriceLevel priceLevel;
	private ClientTransactionItem selectedRecord;

	private List<ClientAccount> gridAccounts;
	private double taxableTotal;

	int transactionDomain;
	boolean isBankingTransaction = false;

	protected int accountingType;
	private double totalValue;

	private boolean isAddNewRequired = true;
	private long ztaxCodeid;
	protected int maxDecimalPoint;
	protected long taxCode;
	AccounterConstants accounterConstants = Accounter.constants();

	public CustomerTransactionGrid() {
		super(false, true);
		this.accountingType = getCompany().getAccountingType();

	}

	public CustomerTransactionGrid(boolean isAddNewRequired) {
		super(false, true);
		this.isAddNewRequired = isAddNewRequired;
		this.accountingType = getCompany().getAccountingType();

	}

	@Override
	public String[] getColumnNamesForPrinting() {
		if (getCompany().getPreferences().isChargeSalesTax()) {
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				return new String[] { Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().totalPrice(),
						Accounter.constants().vatRate(),
						Accounter.constants().vatAmount() };
			} else {
				return new String[] { Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().totalPrice(),
						Accounter.constants().isTaxable() };
			}
		} else {
			return new String[] { Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().totalPrice() };
		}
	}

	@Override
	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 9).contains(index))
				return "";
		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 6, 9).contains(index))
				return "";
		}
		switch (index) {
		case 0:
			return item.getDescription();
		case 1:
			return item.getQuantity() + "";
		case 2:
			return amountAsString(getAmountInForeignCurrency(item
					.getUnitPrice()));
		case 3:
			return amountAsString(item.getLineTotal());
		case 4:
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				return amountAsString(UIUtils.getVATItem(item.getTaxCode(),
						true).getTaxRate());
			} else {
				return item.isTaxable() ? Accounter.constants().taxable()
						: Accounter.constants().nonTaxable();
			}
		default:
			return amountAsString(getVatTotal());
		}

	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			if (getCompany().getPreferences().isChargeSalesTax()) {
				if (getCompany().getPreferences().isRegisteredForVAT())
					return ListGrid.COLUMN_TYPE_SELECT;
				else {
					if (transactionView instanceof WriteChequeView)
						return ListGrid.COLUMN_TYPE_IMAGE;
					else
						return ListGrid.COLUMN_TYPE_SELECT;
				}
			} else {
				return ListGrid.COLUMN_TYPE_IMAGE;
			}
		case 8:
			if (getCompany().getPreferences().isRegisteredForVAT())
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 9:
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		return 0;

	}

	@Override
	protected int getCellWidth(int index) {
		if (getCompany().getPreferences().isChargeSalesTax()) {
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				return getUKGridCellWidth(index);
			} else {
				return getUSGridCellWidth(index);
			}
		} else {
			return getDefaultGridCellWidth(index);
		}

	}

	private int getDefaultGridCellWidth(int index) {
		if (index == 7 || index == 0)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;

		else if (index == 2)
			return 150;
		else if (index == 4 || index == 6)
			return 100;
		else if (index == 3 || index == 5)
			return 80;
		return 0;
	}

	private int getUSGridCellWidth(int index) {
		if (transactionView instanceof WriteChequeView) {
			if (index == 7 || index == 0)
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			// else if (index == 2)
			// return 150;
			else if (index == 4 || index == 6)
				return 100;
			else if (index == 3 || index == 5)
				return 80;
		} else {
			if (index == 8 || index == 0)
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;

			else if (index == 2)
				return 150;
			else if (index == 4 || index == 6)
				return 100;
			else if (index == 3 || index == 5 || index == 7)
				return 80;
		}
		return -1;
	}

	private int getUKGridCellWidth(int index) {
		if (index == 0 || index == 9)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 3 || index == 4)
			return 90;
		// else if (index == 2)
		// return 120;
		else if (index == 5)
			return 80;
		else if (index == 7)
			if (getCompany().getPreferences().isChargeSalesTax()) {
				return 70;
			} else {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
		else if (index == 8)
			return 60;
		else if (index == 6)
			return 100;
		else if (index == 9)
			return 15;
		else if (index == 2) {
			if (Accounter.isMacApp()) {
				int var = Window.getClientWidth();
				return var - 830;
			} else {
				if (UIUtils.isMSIEBrowser()) {
					return 110;
				} else {
					return 130;
				}
			}
		}
		return -1;
	}

	@Override
	public void init() {
		super.isEnable = false;
		super.init();
		// if (FinanceApplication.getCompany().getAccountingType() !=
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		createControls();

		initTransactionData();

		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			// setAllTransactionItems(transactionObject.getTransactionItems());
			if (transactionObject.getID() != 0) {
				// ITS Edit Mode
				// setShowMenu(false);
				// isEdit = true;
				canDeleteRecord(false);
				// canAddRecord(false);
				// setEditDisableCells(new int[] { 0, 1, 2, 3, 4 });
			}
			// }
		}
	}

	@Override
	protected String[] getSelectValues(ClientTransactionItem obj, int index) {
		switch (index) {
		case 7:
			return new String[] { Accounter.constants().taxable(),
					Accounter.constants().nonTaxable() };

		default:
			break;
		}
		return super.getSelectValues(obj, index);
	}

	protected void initTransactionData() {

		List<ClientItem> result = getCompany().getActiveItems();
		// if (isCustomerTransaction || isBankingTransaction) {
		List<ClientItem> customerItems = new ArrayList<ClientItem>();
		for (ClientItem item : result) {
			if (item.isISellThisItem())
				customerItems.add(item);
		}
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : customerItems) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		serviceItemCombo.initCombo(serviceitems);

		productItemCombo.initCombo(productitems);
		// itemCombo.initCombo(customerItems);
	}

	protected void createControls() {
		setWidth("100%");
		// setSize("100%", "250px");

		// Passing 1 for Customer, 2 For Vendor For Item View- Raj Vimal
		serviceItemCombo = new ServiceCombo(Accounter.constants().item(), 1,
				isAddNewRequired);
		serviceItemCombo.setGrid(this);
		serviceItemCombo.setRequired(true);
		serviceItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getSalesPrice());
							selectedObject.setTaxable(selectItem.isTaxable());
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							// it should be here only for vat
							// calculations(it
							// needs line total for this,linetotal
							// calculated in
							// editcomplete()`

							// database always has the currency values in base
							// currency.
							editComplete(selectedObject,
									selectItem.getSalesPrice(), 4);
							applyPriceLevel(selectedObject);

						}
					}
				});
		if (getCompany().getPreferences().isChargeSalesTax()) {
			us_taxCombo = new SelectCombo(Accounter.constants().tax());
			us_taxCombo.addItem(Accounter.constants().taxable());
			us_taxCombo.addItem(Accounter.constants().nonTaxable());
			us_taxCombo.setGrid(this);
			us_taxCombo.setRequired(true);
			us_taxCombo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (selectItem != null) {
								selectedObject.setTaxitem(selectItem);
								editComplete(selectedObject, selectItem, 7);
							}
						}
					});
		}

		productItemCombo = new ProductCombo(
				Accounter.constants().productItem(), 1, isAddNewRequired);
		productItemCombo.setGrid(this);
		productItemCombo.setRequired(true);
		productItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getSalesPrice());
							if (transactionView instanceof WriteChequeView) {
								selectedObject.setTaxable(false);
							} else {
								selectedObject.setTaxable(selectItem
										.isTaxable());
							}
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							// it should be here only for vat
							// calculations(it
							// needs line total for this,linetotal
							// calculated in
							// editcomplete()
							editComplete(selectedObject,
									selectItem.getSalesPrice(), 4);
							applyPriceLevel(selectedObject);

						}
					}
				});

		// itemCombo.addFilter(new Filter() {
		//
		// @Override
		// public boolean canAdd(IAccounterCore core) {
		// ClientItem item = (ClientItem) core;
		//
		// if (item == null)
		// return false;
		// if (!item.isISellThisItem()) {
		// Accounter
		// .showInformation(AccounterErrorType.cannotUsePurchaseItem);
		// return false;
		// }
		//
		// // else if (isVendorTransaction && !item.isIBuyThisItem()) {
		// // Accounter
		// // .showInformation(AccounterErrorType.cannotUseSalesItem);
		// // return false;
		// // }
		// return true;
		//
		// // return isCustomerTransaction ? item.isISellThisItem() : item
		// // .isIBuyThisItem();
		// }
		//
		// });

		accountsCombo = new SalesAccountsCombo(Accounter.messages().accounts(
				Global.get().Account()), isAddNewRequired);
		accountsCombo.setGrid(this);
		accountsCombo.setRequired(true);
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAccount(selectItem.getID());
						if (getCompany().getAccountingType() == 1)
							selectedObject.setTaxable(true);
						setText(currentRow, currentCol, selectItem.getName());
						updateData(selectedObject);
						if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
							if (getCompany().getPreferences()
									.isChargeSalesTax())
								setCustomerTaxCode(selectedObject);
						}
					}
				});
		// accountsCombo.setWidth("530");

		// salesTaxCombo = new TaxCodeCombo(FinanceApplication
		// .constants().salesTax(), isAddNewRequired);
		// salesTaxCombo.setGrid(this);
		// salesTaxCombo.setRequired(true);
		// salesTaxCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientTaxCode>() {
		//
		// @Override
		// public void selectedComboBoxItem(ClientTaxCode selectItem) {
		// selectedObject.setTaxCode(selectItem.getID());
		// setText(currentRow, currentCol, selectItem.getName());
		// }
		// });

		// column count from '1'
		// if (!isBankingTransaction)
		// this.addFooterValues(new String[] { "", "", "Sub Total",
		// amountAsString(0.00), "", "Discount: 0%",
		// "Line Total: " + amountAsString(0.00), });
		// else
		// this.addFooterValues(new String[] { "", "", "Sub Total",
		// amountAsString(0.00),
		// "Line Total: " + amountAsString(0.00), });
		// FXIME check it for VAT implementation
		if (getCompany().getPreferences().isRegisteredForVAT())
			createVATItemAndTaxCodeCombo();

		if (!isBankingTransaction && !isSalesOrderTransaction) {
			// this.addFooterValues(new String[] { "", "", "", "", "",
			// FinanceApplication.constants().total(),
			// amountAsString(0.00), });
		} else if (isSalesOrderTransaction) {

			// this.addFooterValues(new String[] { "", "", "", "", "", "",
			// amountAsString(0.00), "" });

		} else {
			// this.addFooterValues(new String[] { "", "", "",
			// amountAsString(0.00), "" });
		}
		if (getCompany().getAccountingType() == 1) {
			if (!isBankingTransaction)
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					// this.addFooterValue(FinanceApplication.constants()
					// .VAT(), 7);
					// this.addFooterValue(amountAsString(0.00),
					// 8);
				}
			// setBottomLabelTitle("VAT: "
			// + amountAsString(totalVat), 8);
		}

		addRecordClickHandler(new RecordClickHandler<ClientTransactionItem>() {

			@Override
			public boolean onRecordClick(ClientTransactionItem core, int column) {
				switch (column) {
				case 1:
					if (core.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
						if (core.getAccount() != 0)
							accountsCombo.setComboItem(getCompany().getAccount(
									core.getAccount()));
						else
							accountsCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SERVICE) {
						if (core.getItem() != 0)
							serviceItemCombo.setComboItem(getCompany().getItem(
									core.getItem()));
						else
							serviceItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_ITEM) {
						if (core.getItem() != 0)
							productItemCombo.setComboItem(getCompany().getItem(
									core.getItem()));
						else
							productItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SALESTAX) {
						// if
						// (FinanceApplication.getCompany().getAccountingType()
						// == ClientCompany.ACCOUNTING_TYPE_US) {
						// if (core.getTaxCode() != null)
						// salesTaxCombo.setComboItem(FinanceApplication
						// .getCompany().getTaxCode(
						// core.getTaxCode()));
						// else
						// salesTaxCombo.setValue("");
						// } else {
						// if (core.getTaxCode() != null)
						// salesTaxCombo.setComboItem(FinanceApplication
						// .getCompany().getTaxCode(
						// core.getTaxCode()));
						// else
						// salesTaxCombo.setValue("");
						// }
					}
					break;
				default:
					break;

				}
				// in US-version,column '7' is Taxable,so if user doubleclick on
				// it thatcombo should be enbled,for avoiding columnindex
				// collisions of 7th column in uk,the code for VATCodeCombo
				// is written here
				// if (FinanceApplication.getCompany().getAccountingType() ==
				// ClientCompany.ACCOUNTING_TYPE_UK
				// && column == 7) {
				// vatCodeCombo.setComboItem(FinanceApplication.getCompany()
				// .getVATCode(core.getVatCode()));
				// }
				if (column == getColumnsCount() - 1) {
					if (!transactionView.isEdit()) {
						deleteRecord(core);
						updateTotals();
					}
					return true;
				}
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
						&& column == 7) {
					taxCodeCombo.setComboItem(getCompany().getTAXCode(
							core.getTaxCode()));
					return true;
				}
				return true;
			}
		});
	}

	protected void applyPriceLevel(ClientTransactionItem item) {
		priceLevelSelected(priceLevel);
		selectedRecord = item;
		if (priceLevel != null)
			setUnitPriceForSelectedItem(getCompany().getItem(item.getItem()));
	}

	@Override
	public void setCustomerTaxCode(ClientTransactionItem selectedObject) {
		List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.getName().equals("S")) {
				ztaxCodeid = taxCode.getID();
			}
		}
		AbstractCustomerTransactionView<?> view = (AbstractCustomerTransactionView<?>) transactionView;
		if (transactionView.getTransactionObject() == null) {
			if (view.getCustomer() != null)
				selectedObject
						.setTaxCode(selectedObject.getTaxCode() != 0 ? selectedObject
								.getTaxCode()
								: view.getCustomer().getTAXCode() > 0 ? view
										.getCustomer().getTAXCode()
										: ztaxCodeid);
			else
				selectedObject.setTaxCode(ztaxCodeid);
		}
		updateTotals();
		updateData(selectedObject);
	}

	@Override
	public void updateData(ClientTransactionItem obj) {
		super.updateData(obj);
	}

	@Override
	public void updateRecord(ClientTransactionItem obj, int row, int col) {
		super.updateRecord(obj, row, col);
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 9).contains(index))
				return "";
		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 6, 9).contains(index))
				return "";
		}
		switch (index) {
		case 0:
			return getImageByType(item.getType());
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return item.getQuantity();
			else {
				return (item.getQuantity() != null || item.getLineTotal() == 0) ? item
						.getQuantity() : "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return amountAsString(getAmountInForeignCurrency(item
						.getUnitPrice()));
			else {
				double amount = getAmountInForeignCurrency(item.getUnitPrice());
				return (amount != 0 || item.getLineTotal() == 0) ? amountAsString(amount)
						: "";
			}
		case 5:
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
		case 6:
			return amountAsString(item.getLineTotal());
		case 7:
			if (getCompany().getPreferences().isChargeSalesTax()) {
				if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK)
					return getTAXCodeName(item.getTaxCode());
				else {
					if (transactionView instanceof WriteChequeView)
						return Accounter.getFinanceMenuImages().delete();
					else
						return item.isTaxable() ? Accounter.constants()
								.taxable() : Accounter.constants().nonTaxable();
				}
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
		case 8:
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				return amountAsString(item.getVATfraction());
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
		case 9:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	protected ImageResource getImageByType(int type) {
		switch (type) {
		case TYPE_ITEM:
			// return
			// FinanceApplication.getFinanceMenuImages().items().getURL();
			return Accounter.getFinanceImages().itemsIcon();
		case TYPE_SERVICE:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return Accounter.getFinanceImages().salesTaxIcon();
		case TYPE_ACCOUNT:
			// return FinanceApplication.getFinanceMenuImages().Accounts()
			// .getURL();
			return Accounter.getFinanceImages().AccountsIcon();
		case TYPE_COMMENT:
			// return FinanceApplication.getFinanceMenuImages().comments()
			// .getURL();
			return Accounter.getFinanceImages().CommentsIcon();
		case TYPE_SALESTAX:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return Accounter.getFinanceImages().salesTaxIcon();
		default:
			break;
		}
		return Accounter.getFinanceImages().errorImage();
	}

	protected String getNameValue(ClientTransactionItem item) {
		switch (item.getType()) {
		// FIXME--need to check for default selection values(eg:- a combo
		// should
		// have a default value selected.)
		case TYPE_ITEM:
			ClientItem itm = getCompany().getItem(item.getItem());
			return itm != null ? itm.getName() : "";
		case TYPE_SERVICE:
			ClientItem itm1 = getCompany().getItem(item.getItem());
			return itm1 != null ? itm1.getName() : "";
		case TYPE_ACCOUNT:

			ClientAccount account = getCompany().getAccount(item.getAccount());
			return account != null ? account.getDisplayName() : "";
		case TYPE_COMMENT:
			return item.getDescription() != null ? item.getDescription() : "";
		case TYPE_SALESTAX:
			if (getCompany().getPreferences().isChargeSalesTax()) {
				// ClientTaxCode taxCode = FinanceApplication.getCompany()
				// .getTaxCode(item.getTaxCode());
				// return taxCode != null ? taxCode.getName() : "";
			} else {
				ClientTAXItem vatItem = getCompany().getTaxItem(
						item.getVatItem());
				return vatItem != null ? vatItem.getName() : "";
			}
		default:
			break;
		}
		return "";
	}

	protected void setUnitPriceForSelectedItem(ClientItem selectedItem) {

		ClientTransactionItem record = selectedRecord;

		if (record == null)
			return;
		Double calculatedUnitPrice = Utility
				.getCalculatedItemUnitPriceForPriceLevel(selectedItem,
						priceLevel, false);

		if (calculatedUnitPrice == null)
			return;

		int index = getSelectedRecordIndex();

		int col = 4;
		record.setItem(selectedItem.getID());

		record.setUnitPrice(calculatedUnitPrice);

		record.setDescription(selectedItem.getName());

		stopEditing(col);

		if (getCompany().getAccountingType() == 1) {
			// if (selectedItem.getVatCode() != null) {
			// record.setTaxCode(selectedItem.getVatCode());
			// refreshVatValue(record);
			// }
		} else {
			record.setTaxable(selectedItem.isTaxable());

		}
		double lt = record.getQuantity().getValue() * record.getUnitPrice();
		double disc = record.getDiscount();
		record.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
				* disc / 100)) : lt);
		updateData(record);
		// startEditing(index);

	}

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = getRecords();
		int totaldiscount = 0;
		totallinetotal = 0.0;
		taxableTotal = 0.0;
		totalVat = 0.0;
		int accountType = getCompany().getAccountingType();
		for (ClientTransactionItem citem : allrecords) {

			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;
			if (getCompany().getPreferences().isChargeSalesTax()) {
				if (citem != null && citem.isTaxable())
					taxableTotal += lineTotalAmt;
			}
			totalVat += citem.getVATfraction();
		}

		if (accountType == ClientCompany.ACCOUNTING_TYPE_US)
			grandTotal = totalVat + totallinetotal;
		else {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		}

		transactionView.updateNonEditableItems();

		// if (!isBankingTransaction)
		// this.updateFooterValues("Discount: " + totaldiscount + "%", 5);
		// this.updateFooterValues(amountAsString(totallinetotal),
		// 6);
		//
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// if (!isBankingTransaction) {
		// this.updateFooterValues(amountAsString(totalVat),
		// 8);
		// }
		// }
	}

	public Map<Integer, Map<String, String>> getVATDetailsMapForPrinting() {
		Map<Integer, Map<String, String>> vatMap = new HashMap<Integer, Map<String, String>>();
		Map<String, String> vat = new HashMap<String, String>();
		;
		double totalVATAmount = 0.0;
		int r = 0;
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != 0) {
				vat = new HashMap<String, String>();
				String taxCodeWidRate = getTAXCodeName(rec.getTaxCode())
						+ "@"
						+ DataUtils.getNumberAsPercentString(getVATRate(rec)
								+ "");
				double vatAmount = getVATAmount(rec.getTaxCode(), rec);
				totalVATAmount += vatAmount;
				vat.put(taxCodeWidRate, amountAsString(vatAmount));
				vatMap.put(r++, vat);
			}
		}
		vat = new HashMap<String, String>();

		vat.put(Accounter.constants().totalVAT(),
				amountAsString(totalVATAmount));

		vatMap.put(r++, vat);
		return vatMap;
	}

	public double getTotalVATAmountForPrinting() {
		double vatAmount = 0.0;
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != 0) {
				vatAmount += getVATAmount(rec.getTaxCode(), rec);
			}
		}
		return vatAmount;
	}

	public double getVATRate(ClientTransactionItem rec) {
		double vatRate = 0.0;

		long TAXCodeID = rec.getTaxCode();
		if (TAXCodeID != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise
			// 'GroupRate
			try {
				if (getCompany().getTAXItemGroup(
						getCompany().getTAXCode(TAXCodeID)
								.getTAXItemGrpForSales()) instanceof ClientTAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((ClientTAXItem) getCompany().getTAXItemGroup(
							getCompany().getTAXCode(TAXCodeID)
									.getTAXItemGrpForSales())).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((ClientTAXGroup) getCompany().getTAXItemGroup(
							getCompany().getTAXCode(TAXCodeID)
									.getTAXItemGrpForSales())).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vatRate;
	}

	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {

		return getRecords();
	}

	public void setAllTransactionItems(
			List<ClientTransactionItem> transactionItems) {

		// removeAllRecords();
		for (int i = 0; i < transactionItems.size(); i++) {

			CustomCombo<?> combo = null;
			combo = getCustomCombo(transactionItems.get(i), 1);
			/* If the record type is COMMENT,then combo value is null */
			if (combo != null) {
				String itemName = "";
				if (combo instanceof TAXCodeCombo) {
					itemName = getTAXCodeName(transactionItems.get(i)
							.getTaxCode());
				} else {
					itemName = getNameValue(transactionItems.get(i));
				}
				combo.setSelected(itemName);
				if (getCompany().getPreferences().isRegisteredForVAT()) {
					combo = getCustomCombo(transactionItems.get(i), 7);
					if (combo != null) {
						String taxCodeName = "";
						if (combo instanceof TAXCodeCombo) {
							taxCodeName = getTAXCodeName(transactionItems
									.get(i).getTaxCode());
						} else {
							taxCodeName = getNameValue(transactionItems.get(i));
						}
						combo.setSelected(taxCodeName);
					}
				}

			}
		}
		if (!(this instanceof SalesOrderGrid)) {
			for (ClientTransactionItem item : transactionItems) {
				item.setID(0);
			}
		}
		addRecords(transactionItems);
		updateTotals();

	}

	protected String getTAXCodeName(long taxCode) {
		ClientTAXCode t = null;
		if (taxCode != 0)
			t = getCompany().getTAXCode(taxCode);
		return t != null ? t.getName() : "";
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public void priceLevelSelected(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;

	}

	public void updatePriceLevel() {
		for (ClientTransactionItem item : getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				selectedRecord = item;
				if (priceLevel != null)
					setUnitPriceForSelectedItem(getCompany().getItem(
							item.getItem()));

			}
		}

		updateTotals();
	}

	public Double getTaxableLineTotal() {
		return this.taxableTotal;

	}

	public Double getVatTotal() {
		return this.totalVat;
	}

	protected void transactionItemRecordDeleted() {
		updateTotals();
	}

	public void resetGridEditEvent() {

	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void refreshVatValue(ClientTransactionItem obj) {
		ClientTransactionItem record = (ClientTransactionItem) obj;

		long taxCode = getTaxCode(obj);
		if (taxCode == 0)
			return;
		record.setVATfraction(getVATAmount(taxCode, record));
		updateTotals();
		updateData(record);
	}

	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
		refreshVatValue();
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		if (TAXCodeID != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			try {
				ClientTAXItemGroup item = getCompany().getTAXItemGroup(
						getCompany().getTAXCode(TAXCodeID)
								.getTAXItemGrpForSales());
				if (item == null) {
					vatRate = 0.0;
				} else if (item instanceof ClientTAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((ClientTAXItem) item).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((ClientTAXGroup) item).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Double vat = 0.0;
		if (transactionView.isShowPriceWithVat()) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	public void refreshVatValue() {
		List<ClientTransactionItem> allrecords = getRecords();
		for (ClientTransactionItem record : allrecords) {
			// if (record.getTaxCode() != null)
			refreshVatValue(record);
		}
		updateTotals();

	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {
		try {
			boolean isItem = (item.getType() == ClientTransactionItem.TYPE_ITEM || item
					.getType() == ClientTransactionItem.TYPE_SERVICE) ? true
					: false;
			switch (col) {
			case 1:

			case 2:
				item.setDescription(toStringValue(value));
				if (item.getType() == ClientTransactionItem.TYPE_COMMENT)
					return;
				break;
			case 3:
				String qty = value.toString() != null
						&& (value.toString().length() != 0 && value.toString()
								.length() <= 8) ? value.toString()
						: isItem ? "1" : "1";

				if (qty.startsWith("" + UIUtils.getCurrencySymbol() + "")) {
					qty = qty.replaceAll("" + UIUtils.getCurrencySymbol() + "",
							"");
				}
				qty = qty.replaceAll(",", "");
				// int q = 0;
				// try {
				// q = Integer.parseInt(qty);
				// } catch (Exception e) {
				// Accounter.showError(AccounterErrorType.INVALIDENTRY);
				// }
				double total = DataUtils.getReformatedAmount(qty);
				Double quantity = total;
				if (quantity == 0) {
					quantity = 1.0D;
					qty = "1";
				}
				try {
					ClientQuantity quant = new ClientQuantity();
					if (AccounterValidator.isValidGridQuantity(0)) {
						quant.setValue(Double.parseDouble(qty));
						item.setQuantity(quant);
						update_quantity_inAllRecords(item.getQuantity()
								.getValue());
					} else {
						quant.setValue(isItem ? 1 : 1);
						item.setQuantity(quant);
						transactionView.addError(this, Accounter.constants()
								.quantity());
					}
				} catch (InvalidTransactionEntryException e) {
					e.printStackTrace();
				}

				// FIXME need to implement warnings
				break;
			case 4:
				// TODO if its new entry don't do anything here. If not, then
				// convert to selected currency
				String unitPriceString = null;

				unitPriceString = value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "0";

				// if (unitPriceString.startsWith("" +
				// UIUtils.getCurrencySymbol()
				// + "")) {
				// unitPriceString = unitPriceString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				// unitPriceString = unitPriceString.replaceAll(",", "");
				Double d = DataUtils.getReformatedAmount(unitPriceString);// Double.parseDouble(unitPriceString);

				// TODO the value came from the UI component. So, we need to
				// convert this value to base currency, if the selected currency
				// is not in baseCurreny.

				if (AccounterValidator.isValidGridUnitPrice(d)) {
					d = getAmountInBaseCurrency(d);
					// the value is in BaseCurrency now.
					item.setUnitPrice(d);
				} else {
					d = 0.0D; // zero. no need conversions.
					item.setUnitPrice(d);
					transactionView.addError(this,
							accounterConstants.unitPrice());
				}

				break;
			case 5:
				String discount = value.toString();

				// if (discount.contains("%")) {
				// discount = discount.replaceAll("%", "");
				// }
				// discount = discount.replaceAll(",", "");
				Double discountRate = Double.parseDouble(DataUtils
						.getReformatedAmount(discount) + "");
				if (AccounterValidator.isNegativeAmount(discountRate)) {
					item.setUnitPrice(0.0D);
					discountRate = 0.0D;
				}
				if (!DecimalUtil.isGreaterThan(discountRate, 100))
					item.setDiscount(discountRate);
				break;
			case 6:
				String lineTotalAmtString = value.toString();
				// if (lineTotalAmtString.contains(""
				// + UIUtils.getCurrencySymbol() + "")) {
				// lineTotalAmtString = lineTotalAmtString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				if (item.getType() == TYPE_SALESTAX
						|| item.getType() == TYPE_SERVICE
						|| item.getType() == TYPE_ACCOUNT) {
					// lineTotalAmtString =
					// lineTotalAmtString.replaceAll(",",
					// "");
					Double lineTotal = Double.parseDouble(DataUtils
							.getReformatedAmount(lineTotalAmtString) + "");
					try {
						if ((!AccounterValidator
								.isValidGridLineTotal(lineTotal))
								&& (!AccounterValidator
										.isAmountTooLarge(lineTotal))) {
							item.setLineTotal(lineTotal);
							// TODO doubt here, whether to convert this into
							// baseCurrency or not.
							item.setUnitPrice(isItem ? lineTotal : 0);
							ClientQuantity quant = new ClientQuantity();
							quant.setValue(isItem ? 1 : 1);
							item.setQuantity(quant);
						}
					} catch (Exception e) {

						if (e instanceof InvalidEntryException) {
							item.setLineTotal(0.0D);
							item.setUnitPrice(0.0D);
							ClientQuantity quant = new ClientQuantity();
							quant.setValue(isItem ? 1 : 1);
							item.setQuantity(quant);
							Accounter.showError(e.getMessage());

						}
					}
				}

				break;
			case 7:
				if (getCompany().getPreferences().isChargeSalesTax()) {
					if (value.equals("Taxable")) {
						item.setTaxable(true);
					} else
						item.setTaxable(false);
				}
				break;
			case 8:

				break;

			default:
				break;
			}
		} catch (Exception e) {
			// Accounter.showError(AccounterErrorType.INVALIDENTRY);
		}
		if (accountingType == ClientCompany.ACCOUNTING_TYPE_UK
				&& !getCompany().getPreferences().isChargeSalesTax()) {
			if (item.getType() == TYPE_SERVICE
					|| item.getType() == TYPE_ACCOUNT
					|| item.getType() == TYPE_ITEM) {
				if (ztaxCodeid != 0)
					item.setTaxCode(ztaxCodeid);
				else {
					List<ClientTAXCode> taxCodes = Accounter.getCompany()
							.getActiveTaxCodes();
					for (ClientTAXCode taxCode : taxCodes) {
						if (taxCode.getName().equals("Z")) {
							ztaxCodeid = taxCode.getID();
						}
					}
					if (ztaxCodeid != 0)
						item.setTaxCode(ztaxCodeid);
				}

			}
		}
		if (item.getType() != TYPE_SALESTAX && col != 6 && col != 7) {
			// TODO doubt, currencyConversion.
			double lt = item.getQuantity().getValue() * item.getUnitPrice();
			double disc = item.getDiscount();
			item.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		updateTotals();
		updateData(item);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (Arrays.asList(3, 4, 5, 6, 7).contains(col))
				refreshVatValue(item);
		}
	}

	@Override
	protected void onValueChange(ClientTransactionItem item, int index,
			Object value) {
		if (index == 1) {
			int type = item.getType();
			switch (type) {
			case TYPE_ITEM:
				setUnitPriceForSelectedItem((ClientItem) value);
				break;
			case TYPE_ACCOUNT:
				if (item != null) {
					item.setTaxable(false);
				}
			default:
				break;
			}
		}
		super.onValueChange(item, index, value);
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (obj.getType() == TYPE_SERVICE
				&& !getCompany().getPreferences().isChargeSalesTax()) {
			if (col == 7 || col == 8)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			case 3:
			case 4:
			case 5:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_COMMENT:
			switch (col) {
			case 2:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 6:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SALESTAX:
			switch (col) {
			case 1:
				return true;
			case 2:
				return true;
			case 6:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_SERVICE:

			switch (col) {
			case 6:
				return false;
			default:
				return true;
			}
		}
		return true;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		CustomCombo<E> combo = null;
		switch (colIndex) {
		case 1:
			if (obj.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				combo = (CustomCombo<E>) accountsCombo;

			} else if (obj.getType() == ClientTransactionItem.TYPE_ITEM) {
				combo = (CustomCombo<E>) productItemCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_SERVICE) {
				combo = (CustomCombo<E>) serviceItemCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_SALESTAX) {
				if (getCompany().getPreferences().isChargeSalesTax()) {
					// return (CustomCombo<E>) salesTaxCombo;
				} else
					combo = (CustomCombo<E>) vatItemCombo;
			}

			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-7, Unit.PX);
			// } else {
			// if (this instanceof SalesOrderGrid)
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-7, Unit.PX);
			// else
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-13, Unit.PX);
			// }

			break;
		case 7:
			combo = (CustomCombo<E>) taxCodeCombo;
			if (getCompany().getPreferences().isChargeSalesTax()) {
				combo = (CustomCombo<E>) us_taxCombo;
			} else {

			}
			break;
		default:
			break;
		}

		return combo;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public double getTotalValue() {
		return totalValue;
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validations
		// 1. checking for the name of the transaction item
		// 2. checking for the vat code if the company is of type UK
		// TODO::: check whether this validation is working or not
		for (ClientTransactionItem item : this.getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
				continue;
			}
			if (AccounterValidator.isEmpty(this.getColumnValue(item, 1))) {
				result.addError(
						"GridItem-" + item.getAccount(),
						Accounter.messages().pleaseEnter(
								UIUtils.getTransactionTypeName(item.getType())));
			}
			if (accountingType == ClientCompany.ACCOUNTING_TYPE_UK
					&& item.getType() != ClientTransactionItem.TYPE_SALESTAX) {
				if (AccounterValidator.isEmpty(this.getColumnValue(item, 7))) {
					result.addError(
							"GridItemUK-" + item.getAccount(),
							Accounter.messages().pleaseEnter(
									Accounter.constants().vatCode()));
				}

			}
		}
		return result;
	}

	private void update_quantity_inAllRecords(double d) {
		int decimalpoints_count = getMaxDecimals((int) d);
		if (maxDecimalPoint < decimalpoints_count) {
			maxDecimalPoint = decimalpoints_count;
			for (ClientTransactionItem item : this.getRecords()) {
				updateData(item);
			}
		}
	}

	public int getMaxDecimals(int quantity) {
		String qty = String.valueOf(quantity);
		String max = qty.substring(qty.indexOf(".") + 1);
		return max.length();
	}

	@Override
	protected String[] getColumns() {
		if (getCompany().getPreferences().isChargeSalesTax()) {
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK) {
				return new String[] { "", Accounter.constants().name(),
						Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().discPerc(),
						Accounter.constants().total(),
						Accounter.constants().vatCode(),
						Accounter.constants().vat(), " " };
			} else {
				if (transactionView instanceof WriteChequeView)
					return new String[] { "", Accounter.constants().name(),
							Accounter.constants().description(),
							Accounter.constants().quantity(),
							Accounter.constants().unitPrice(),
							Accounter.constants().discPerc(),
							Accounter.constants().total(), " " };
				else
					return new String[] { "", Accounter.constants().name(),
							Accounter.constants().description(),
							Accounter.constants().quantity(),
							Accounter.constants().unitPrice(),
							Accounter.constants().discPerc(),
							Accounter.constants().total(),
							Accounter.constants().tax(), " " };
			}
		} else {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().discPerc(),
					Accounter.constants().total(), " " };
		}
	}

	protected void createVATItemAndTaxCodeCombo() {

		vatItemCombo = new VATItemCombo(Accounter.constants().vatItem(),
				isAddNewRequired);
		vatItemCombo.initCombo(getVatItems());
		vatItemCombo.setGrid(this);
		vatItemCombo.setRequired(true);
		vatItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null) {
							if (!isPreviuslyUsed(selectItem)) {
								Accounter
										.showError(Accounter
												.constants()
												.vatitemslctdalreadyusedinVATEnterdiffVATItem());
							}
							selectedObject.setVatItem(selectItem.getID());
							setText(currentRow, currentCol,
									selectItem.getName());
						}
					}
				});

		taxCodeCombo = new TAXCodeCombo(Accounter.constants().vatCode(),
				isAddNewRequired, true);
		taxCodeCombo.setGrid(this);
		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						if (selectItem != null) {

							selectedObject.setTaxCode(selectItem.getID());
							if (selectedObject.getType() == TYPE_ACCOUNT)
								editComplete(selectedObject,
										selectedObject.getLineTotal(), 7);
							else

								editComplete(
										selectedObject,
										getAmountInForeignCurrency(selectedObject
												.getUnitPrice()), 4);
						} else
							selectedObject.setTaxCode(0);
					}
				});

	}

	private List<ClientTAXItem> getVatItems() {
		List<ClientTAXItem> customerVATItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getCompany().getActiveTaxItems()) {
			if (vatItem.isSalesType())
				customerVATItems.add(vatItem);
		}
		return customerVATItems;
	}

	public boolean isPreviuslyUsed(ClientTAXItem selectedVATItem) {
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != 0) {
				long vatItem = getCompany().getTAXCode(rec.getTaxCode())
						.getTAXItemGrpForSales();
				if (selectedVATItem.getID() == vatItem) {
					return false;
				}
			}
		}
		return true;
	}

	public long getTaxCode(ClientTransactionItem item) {
		return item.getTaxCode();
	}
}
