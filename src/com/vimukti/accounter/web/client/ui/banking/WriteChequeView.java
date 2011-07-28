package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionUSGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TaxAgencyTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionUSGrid;

public class WriteChequeView extends
		AbstractBankTransactionView<ClientWriteCheck> {

	private PayFromAccountsCombo bankAccSelect;

	private PayeeCombo paytoSelect;
	private AmountField balText;
	private DynamicForm bankAccForm;
	// private TextItem nText;

	private HorizontalPanel labelLayout;
	public AmountLabel amtText, netAmount, totalTxt;
	private TextItem text;
	private TextAreaItem addrArea;
	private DynamicForm payForm;

	protected ClientAccount selectBankAcc;
	protected ClientAccount selectPayToMethod;
	protected boolean isClose;

	private CheckboxItem toprintCheck;
	protected ClientCustomer selectedCustomer;
	protected ClientVendor selectedVendor;

	protected ClientSalesPerson selectedSalesPerson;
	protected ClientAccount selectedBalance;

	protected ClientPayee payee;
	private VerticalPanel mainVLay, nHPanel;

	private LinkedHashMap<String, ClientAddress> billToAddress;

	protected ClientTAXAgency selectedTaxAgency;

	protected TaxAgencyTransactionGrid taxAgencyGrid;

	protected ClientWriteCheck writeCheckTaken;
	private CheckboxItem vatInclusiveCheck;
	private DateField date;

	// private boolean isVendor;
	// private String transactionNumber = "";

	private DynamicForm amtForm;

	private ClientPaySalesTax takenPaySalesTax;

	private ClientCompany company;
	private List<ClientAccount> payFromAccounts;

	private String checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;

	private DynamicForm numForm;

	private ArrayList<DynamicForm> listforms;

	private HorizontalPanel vatPanel;

	private VerticalPanel vPanel;

	private WriteChequeView() {
		super(ClientTransaction.TYPE_WRITE_CHECK, 0);
		this.company = getCompany();
		this.validationCount = 5;
	}

	public static WriteChequeView getInstance() {

		return new WriteChequeView();
	}

	// private void getPayFromAccounts() {
	// payFromAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	// if (account.getType() == ClientAccount.TYPE_CASH
	// || account.getType() == ClientAccount.TYPE_BANK
	// || account.getType() == ClientAccount.TYPE_CREDIT_CARD
	// || account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	//
	// payFromAccounts.add(account);
	// }
	//
	// }
	// bankAccSelect.initCombo(payFromAccounts);
	// }
	public void updateTotals() {
		if (transactionObject == null) {
			transactionVendorGrid.setVisible(true);
			changeGrid(transactionVendorGrid);
			transactionVendorGrid.updateTotals();
		}
	}

	protected void updateAddressAndGrid() {
		// Set<Address> add = null;
		if (payee instanceof ClientCustomer) {
			selectedCustomer = (ClientCustomer) payee;
			addressList = selectedCustomer.getAddress();
			if (transactionObject == null) {
				transactionCustomerGrid.setVisible(true);
				changeGrid(transactionCustomerGrid);
				transactionCustomerGrid.updateTotals();
			}
		} else if (payee instanceof ClientVendor) {

			selectedVendor = (ClientVendor) payee;
			addressList = selectedVendor.getAddress();
			updateTotals();

		} else if (payee instanceof ClientTAXAgency) {
			selectedTaxAgency = (ClientTAXAgency) payee;
			addressList = selectedTaxAgency.getAddress();
			updateTotals();

			/*
			 * The selected TaxAgency required in the taxagency grid to allow
			 * only the taxcodes available fot this taxagency
			 */
			// taxAgencyGrid.setSelectedTaxAgency(selectedTaxAgency);
			// taxAgencyGrid.filteredTaxCodes();
			// if (transactionObject == null) {
			// taxAgencyGrid.setVisible(true);
			// changeGrid(taxAgencyGrid);
			// }

		}
		// getAddreses(add);
		if (writeCheckTaken != null) {
			if (writeCheckTaken.getAddress() != null)
				billToaddressSelected(getAddressById(writeCheckTaken
						.getAddress().getID()));
		}
		initBillToCombo();

	}

	@Override
	protected void reload() {
		if (!saveAndClose)
			try {
				BankingActionFactory.getWriteChecksAction().run(null, true);
			} catch (Throwable e) {

				// //UIUtils.logError("Failed to load Write Check View", e);
			}

	}

	public void changeGrid(ListGrid<ClientTransactionItem> gridView) {
		if (gridView instanceof CustomerTransactionUSGrid) {
			
			CustomerTransactionUSGrid customerGrid = (CustomerTransactionUSGrid) gridView;
			setMenuRequired(true);
			// mainVLay.remove(customerGrid);
			// mainVLay.add(customerGrid);
		} else if (gridView instanceof VendorTransactionUSGrid
				|| gridView instanceof TaxAgencyTransactionGrid) {
			@SuppressWarnings({ "unused" })
			VendorTransactionUSGrid vendorGrid = (VendorTransactionUSGrid) gridView;
			setMenuRequired(true);
		}
		// mainVLay.remove(vendorGrid);
		// mainVLay.add(vendorGrid);
		// } else if (gridView instanceof TaxAgencyTransactionGrid) {
		// 
		// TaxAgencyTransactionGrid taxAgencyGrid = (TaxAgencyTransactionGrid)
		// gridView;
		// setMenuRequired(false);
		// // mainVLay.remove(taxAgencyGrid);
		// // mainVLay.add(taxAgencyGrid);
		// }Ou
		if (transactionVendorGrid != null)
			mainVLay.remove(transactionVendorGrid);
		// if (taxAgencyGrid != null)
		// mainVLay.remove(taxAgencyGrid);
		if (transactionCustomerGrid != null)
			mainVLay.remove(transactionCustomerGrid);
		mainVLay.add(gridView);
		mainVLay.add(vPanel);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			// It should be like thid only,becoz vatPanel is getting add befor
			// the gird.So,we need to remove n add after grid
			mainVLay.remove(vatPanel);
			mainVLay.add(vatPanel);
		}
		// mainVLay.redraw();
	}

	protected void updateBalance() {
		if (selectBankAcc != null)
			balText.setAmount(selectBankAcc.getTotalBalance());
	}

	protected void getAddreses(Set<ClientAddress> allAddress) {
		ClientAddress toBeShown = null;

		if (writeCheckTaken != null && writeCheckTaken.getAddress() != null) {
			addressList = payee.getAddress();
			toBeShown = getAddressById(writeCheckTaken.getAddress().getID());
		} else {
			for (ClientAddress to : allAddress) {
				if (to.getType() == ClientAddress.TYPE_BUSINESS) {
					toBeShown = to;
					break;
				}
			}
		}

		if (toBeShown != null) {
			billToAddress.put("1", toBeShown);
			String toToSet = new String();
			if (toBeShown.getStreet() != null) {
				toToSet = toBeShown.getStreet().toString() + ",\n";
			}
			if (toBeShown.getCity() != null) {
				toToSet += toBeShown.getCity().toString() + ",\n";
			}

			if (toBeShown.getStateOrProvinence() != null) {
				toToSet += toBeShown.getStateOrProvinence() + ",\n";
			}
			if (toBeShown.getZipOrPostalCode() != null) {
				toToSet += toBeShown.getZipOrPostalCode() + ",\n";
			}
			if (toBeShown.getCountryOrRegion() != null) {
				toToSet += toBeShown.getCountryOrRegion();
			}

			addrArea.setValue(toToSet);
		}
	}

	public void initBankaccountCombo() {
		payFromAccounts = bankAccSelect.getAccounts();

		bankAccSelect.initCombo(payFromAccounts);
		if (writeCheckTaken != null) {

			selectBankAcc = this.company.getAccount(writeCheckTaken
					.getBankAccount());

		} else if (takenPaySalesTax != null) {

			selectBankAcc = getCompany().getAccount(
					takenPaySalesTax.getPayFrom());

		}
		if (selectBankAcc != null) {

			bankAccSelect.setComboItem(selectBankAcc);
			updateBalance();
		}

		bankAccSelect.setDisabled(isEdit);
		// bankAccSelect.setShowDisabled(false);

	}

	public void initPayToCombo() {
		List<ClientPayee> payees = getCompany().getActivePayees();

		if (payees != null) {

			paytoSelect.initCombo(payees);
			if (takenPaySalesTax != null) {
				selectedTaxAgency = company.getTaxAgency(takenPaySalesTax
						.getTaxAgency());
				if (selectedTaxAgency != null) {
					// paytoSelect.setPayee((Payee) selectedTaxAgency);
					paytoSelect.setComboItem(selectedTaxAgency);
				}

				paytoSelect.setDisabled(isEdit);
				paytoSelect.setDisabled(false);
				return;
			}
			newPayToMethod();
		}
	}

	protected void newPayToMethod() {
		if (writeCheckTaken != null) {
			switch (writeCheckTaken.getPayToType()) {
			case ClientWriteCheck.TYPE_VENDOR:
				paytoSelect.setComboItem(getCompany().getVendor(
						writeCheckTaken.getVendor()));
				payee = this.company.getVendor(writeCheckTaken.getVendor());

				break;
			case ClientWriteCheck.TYPE_CUSTOMER:
				ClientCompany clientCompany = this.company;
				paytoSelect.setComboItem(clientCompany
						.getCustomer(writeCheckTaken.getCustomer()));
				payee = clientCompany
						.getCustomer(writeCheckTaken.getCustomer());

				break;
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				paytoSelect.setComboItem(getCompany().getTaxAgency(
						writeCheckTaken.getTaxAgency()));
				payee = this.company.getTaxAgency(writeCheckTaken
						.getTaxAgency());
				break;
			}
			paytoSelect.setDisabled(isEdit);
			paytoSelect.setDisabled(false);
			updateAddressAndGrid();

		}

	}

	protected void initTransactionViewData(ClientTransaction transactionObject) {

		if (takenPaySalesTax == null) {

			writeCheckTaken = (ClientWriteCheck) transactionObject;
			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transactionObject
						.isAmountsIncludeVAT());
			}
			initMemoAndReference();

		}

		initTransactionViewData();

	}

	protected void initTransactionViewData() {
		initTransactionNumber();
		initPayToCombo();
		setDisableFields();
		initBankaccountCombo();

	}

	private void setDisableFields() {
		if (transactionObject != null) {
			payForm.setDisabled(true);
			bankAccForm.setDisabled(true);
			if (transactionCustomerGrid != null) {
				transactionCustomerGrid.setCanEdit(false);
			}
			if (transactionVendorGrid != null) {
				transactionVendorGrid.setCanEdit(false);
			}
		}

	}

	// @Override
	// protected void initTransactionNumber() {
	//
	// if (transactionObject != null) {
	//
	// transactionNumber = transactionObject.getNumber();
	// return;
	// }
	//
	// AsyncCallback<String> transactionNumberCallback = new
	// AsyncCallback<String>() {
	//
	// public void onFailure(Throwable caught) {
	// Accounter.showError(FinanceApplication.constants()
	// .failedToGetTransactionNumber());
	//
	// }
	//
	// public void onSuccess(String result) {
	// if (result == null) {
	// onFailure(new Exception());
	// }
	//
	// transactionNumber = result;
	// }
	//
	// };
	//
	// this.rpcUtilService.getNextTransactionNumber(transactionType,
	// transactionNumberCallback);
	//
	// }

	// private void resetElements() {
	// selectBankAcc = null;
	// selectedBalance = null;
	// selectedCustomer = null;
	// selectedSalesPerson = null;
	// selectedTaxAgency = null;
	// selectedVendor = null;
	// selectPayToMethod = null;
	// writeCheckTaken = null;
	// addressList = null;
	// billingAddress = null;
	// billToCombo.setDisabled(isEdit);
	// amtText.setValue(""+UIUtils.getCurrencySymbol() +"0.00");
	// text.setValue(Utility.getNumberInWords("0.00"));
	// date.setValue(new Date());
	// setMemoTextAreaItem("");
	//
	// if (transactionCustomerGrid != null)
	// mainVLay.remove(transactionCustomerGrid);
	//
	// if (taxAgencyGrid != null)
	// mainVLay.remove(taxAgencyGrid);
	// taxAgencyGrid = new TaxAgencyTransactionGrid();
	// transactionCustomerGrid = new CustomerTransactionGrid();
	// if (taxAgencyGrid != null)
	// mainVLay.remove(taxAgencyGrid);
	// taxAgencyGrid = new TaxAgencyTransactionGrid();
	// transactionCustomerGrid = new CustomerTransactionGrid();
	// transactionVendorGrid = new VendorTransactionGrid();
	// vendorTransactionGrid.setTransactionView(this);
	// mainVLay.add(transactionCustomerGrid);

	// }

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientWriteCheck) transactionObject)
					.getTotal();
			if (transactionTotal != null) {
				amtText.setAmount(transactionTotal.doubleValue());
			}

		}

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		if (takenPaySalesTax == null) {

			switch (this.validationCount) {
			case 5:
				return AccounterValidator.validateForm(payForm, false);
			case 4:
				return AccounterValidator.validateForm(bankAccForm, false);
			case 3:
				if (transactionObject == null && payee != null) {
					switch (payee.getType()) {
					case ClientPayee.TYPE_CUSTOMER:
						return AccounterValidator
								.isBlankTransaction(transactionCustomerGrid)
								&& transactionCustomerGrid.validateGrid();
					case ClientPayee.TYPE_VENDOR:
					case ClientPayee.TYPE_TAX_AGENCY:
						return AccounterValidator
								.isBlankTransaction(transactionVendorGrid)
								&& transactionVendorGrid.validateGrid();
						// case ClientPayee.TYPE_TAX_AGENCY:
						// return AccounterValidator
						// .isBlankTransaction(taxAgencyGrid);
					}
				}
				break;
			case 2:
				// if (transactionObject == null && selectBankAcc != null)
				// return AccounterValidator
				// .validate_Total_Exceeds_BankBalance(balText
				// .getAmount(), amtText.getAmount(),
				// selectBankAcc.isIncrease(), this);
				// break;
				return false;

			case 1:
				if (transactionObject == null)
					return validateAmount();
				break;

			default:
				return false;

			}
		}
		return true;
	}

	private boolean validateAmount() throws InvalidEntryException {
		if (payee != null) {
			double total = 0.0;

			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				total = transactionCustomerGrid.getTotal();
				break;
			case ClientPayee.TYPE_VENDOR:
			case ClientPayee.TYPE_TAX_AGENCY:
				total = transactionVendorGrid.getTotal();
				// break;
				// case ClientPayee.TYPE_TAX_AGENCY:
				// total = transactionVendorGrid.getTotal();

			}
			return AccounterValidator.validateAmount(total);
		}
		return false;
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		if (takenPaySalesTax != null) {
			updatePaySalesTax();
			return;
		}
		try {

			ClientWriteCheck writeCheck = transactionObject != null ? (ClientWriteCheck) transactionObject
					: new ClientWriteCheck();
			if (writeCheckTaken != null)
				writeCheck = writeCheckTaken;
			else
				writeCheck = new ClientWriteCheck();
			// Setting Type of the write check
			writeCheck.setType(ClientTransaction.TYPE_WRITE_CHECK);

			writeCheck.setCheckNumber(checkNo);

			writeCheck.setTransactionDate(transactionDate.getTime());

			// Setting Bank account
			writeCheck.setBankAccount(selectBankAcc.getID());

			// Setting Balance
			if (balText.getAmount() != null)
				writeCheck.setBalance(balText.getAmount());
			// setting paymentmethod
			writeCheck.setPaymentMethod("Check");

			// Setting Address
			writeCheck.setAddress(billingAddress);

			// Setting Transactions
			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				writeCheck.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				writeCheck.setCustomer(selectedCustomer.getID());
				writeCheck.setTransactionItems(transactionCustomerGrid
						.getallTransactions(writeCheck));
				writeCheck.setTotal(transactionCustomerGrid.getTotal());

				writeCheck.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				writeCheck.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				writeCheck.setVendor(selectedVendor.getID());
				writeCheck.setTransactionItems(transactionVendorGrid
						.getallTransactions(writeCheck));
				writeCheck.setTotal(transactionVendorGrid.getTotal());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				writeCheck.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);
				writeCheck.setTaxAgency(selectedTaxAgency.getID());
				writeCheck.setTransactionItems(transactionVendorGrid
						.getallTransactions(writeCheck));
				writeCheck.setTotal(transactionVendorGrid.getTotal());
				// for (ClientTransactionItem rec : transactionVendorGrid
				// .getallTransactions(writeCheck)) {
				// rec.setType(ClientTransactionItem.TYPE_SALESTAX);
				// }
				// writeCheck.setTransactionItems(transactionVendorGrid
				// .getallTransactions(writeCheck));
				// writeCheck.setTotal(transactionVendorGrid.getTotal());
				writeCheck.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}

			writeCheck.setAmount(amtText.getAmount());
			writeCheck.setInWords(text.getValue().toString());

			// Setting Date
			if (date != null)
				writeCheck.setDate(((ClientFinanceDate) date.getValue())
						.getTime());
			// setting transactoin number
			writeCheck.setNumber(transactionNumber.getValue().toString());

			// setting checknumber
			writeCheck.setCheckNumber(checkNo);

			// setting Memo
			writeCheck.setMemo(getMemoTextAreaItem());
			if (vatInclusiveCheck != null) {
				writeCheck.setAmountsIncludeVAT((Boolean) vatInclusiveCheck
						.getValue());
			}

			if (toprintCheck.getValue() != null) {
				writeCheck.setToBePrinted((Boolean) toprintCheck.getValue());

			}
			transactionObject = writeCheck;
			// super.saveAndUpdateView();

			if (transactionObject.getID() == 0)
				createObject(transactionObject);
			else

				alterObject(transactionObject);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void updatePaySalesTax() {
		transactionSuccess(takenPaySalesTax);

	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(bankingConstants.writeCheck());
		Label lab1 = new Label(Accounter.constants().writeCheck() + "("
				+ getTransactionStatus() + ")");
		lab1.addStyleName(Accounter.constants().lableTitle());
		if (takenPaySalesTax != null)
			lab1.setText(Accounter.constants().taxAgentPayment());

		transactionNumber = createTransactionNumberItem();
		// nText.setValue(bankingConstants.toBePrinted());
		// nText.setDisabled(true);
		// nText.addBlurHandler(new BlurHandler() {
		//
		// public void onBlur(BlurEvent event) {
		// String temp;
		// if (nText.getValue() != null
		// && !(temp = nText.getValue().toString()).equals("")) {

		// if (!temp.equals(bankingConstants.toBePrinted())) {

		// try {
		//
		// checkNo = temp;
		//
		// } catch (NumberFormatException e) {
		//
		// nText.setValue("");
		//
		// }
		//
		// // }
		//
		// }
		// }
		//
		// });

		date = createTransactionDateItem();
		date.setShowTitle(false);
		date.setColSpan(2);
		date.setDisabled(isEdit);

		numForm = new DynamicForm();
		numForm.setNumCols(4);
		numForm.addStyleName("datenumber-panel");
		numForm.setFields(date, transactionNumber);
		nHPanel = new VerticalPanel();
		nHPanel.setCellHorizontalAlignment(numForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		nHPanel.add(numForm);

		labelLayout = new HorizontalPanel();
		labelLayout.setWidth("100%");
		labelLayout.add(lab1);
		labelLayout.add(nHPanel);
		labelLayout.setCellHorizontalAlignment(nHPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);
		forms.add(numForm);
		formItems.add(date);

		balText = new AmountField(Accounter.constants().balance());
		balText.setWidth(100);
		balText.setDisabled(true);

		bankAccSelect = new PayFromAccountsCombo(Accounter.constants()
				.bankAccount());
		// bankAccSelect.setWidth(100);
		bankAccSelect.setRequired(true);
		bankAccSelect.setDisabled(isEdit);
		bankAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectBankAcc = selectItem;
						updateBalance();
						// if (selectBankAcc != null
						// && !(Boolean) toprintCheck.getValue())
						// if (selectBankAcc != null)
						// {
						// setCheckNumber();
						// } else if (selectBankAcc == null)
						// nText.setValue("");
					}

				});
		bankAccSelect.setDefaultPayFromAccount();

		bankAccForm = new DynamicForm();
		bankAccForm.setFields(bankAccSelect, balText);
		bankAccForm.getCellFormatter().setWidth(0, 0, "232px");
		forms.add(bankAccForm);

		paytoSelect = new PayeeCombo(Accounter.constants().payTo());
		// paytoSelect.setWidth(100);
		paytoSelect.setRequired(true);
		paytoSelect.setDisabled(isEdit);
		paytoSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayee>() {
					public void selectedComboBoxItem(ClientPayee selectItem) {
						text.setValue(Utility.getNumberInWords("0.00"));
						if (payee != null) {
							if (payee instanceof ClientCustomer) {
								if (transactionObject == null)
									transactionCustomerGrid.removeAllRecords();
							} else if (payee instanceof ClientVendor
									|| payee instanceof ClientTAXAgency) {
								if (transactionObject == null)
									transactionVendorGrid.removeAllRecords();
								// } else if (payee instanceof ClientTAXAgency)
								// {
								// taxAgencyGrid.removeAllRecords();
							}
						}

						if (transactionObject != null && payee != null) {
							if (payee.getType() != selectItem.getType()) {
								Accounter
										.showError("On an existing check, if you change information in the Pay to field, Customer, Vendor or Tax Agency type must remain the same. Example, you cannot change a Customer type to Vendor type.");
								paytoSelect.setComboItem(payee);
							} else {
								PayToSelected(selectItem);
								payee = selectItem;
							}
						} else {
							payee = selectItem;
						}
						updateAddressAndGrid();

					}

				});

		formItems.add(paytoSelect);

		billToCombo = createBillToComboItem();
		// billToCombo.setWidth(100);

		amtText = new AmountLabel(Accounter.constants().amount());
		amtText.setWidth(60);
		amtText.setAmount(0.00);
		amtText.setDisabled(isEdit);

		text = new TextItem("");
		text.setWidth(100);
		text.setDisabled(Boolean.TRUE);
		amtText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					text.setValue(Utility.getNumberInWords(amtText.getValue()));
			}

		});

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		toprintCheck = new CheckboxItem(Accounter.constants().toBePrinted());
		toprintCheck.setDisabled(false);
		toprintCheck.setValue(true);
		// toprintCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {
		//
		// @Override
		// public void onValueChange(ValueChangeEvent<Boolean> event) {
		// if ((Boolean) toprintCheck.getValue()) {
		// nText.setDisabled(true);
		// nText.setValue(bankingConstants.toBePrinted());
		// checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;
		// } else if (selectBankAcc != null) {
		// setCheckNumber();
		// nText.setDisabled(false);
		// } else {
		// nText.setValue(transactionNumber);
		// nText.setDisabled(false);
		// }
		// }
		// });

		payForm = new DynamicForm();
		payForm.setWidth("100%");
		payForm.setFields(paytoSelect, billToCombo);
		payForm.getCellFormatter().setWidth(0, 0, "170px");
		forms.add(payForm);

		amtForm = new DynamicForm();
		amtForm.setWidth("50%");
		// amtForm.setFields(amtText, text, memoTextAreaItem, toprintCheck);
		amtForm.setFields(amtText, text);
		amtForm.getCellFormatter().setWidth(0, 0, "100");
		amtForm.getCellFormatter().setWidth(1, 0, "173px");

		HorizontalPanel amtPanel = new HorizontalPanel();
		amtPanel.setWidth("50%");
		amtPanel.add(amtForm);

		HorizontalPanel accPanel = new HorizontalPanel();
		accPanel.setWidth("100%");
		accPanel.add(payForm);
		accPanel.add(bankAccForm);
		accPanel.setCellHorizontalAlignment(bankAccForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel topHLay = new VerticalPanel();
		topHLay.setWidth("100%");

		topHLay.add(labelLayout);
		topHLay.add(accPanel);
		topHLay.add(amtForm);

		vatPanel = new HorizontalPanel();
		vatPanel.setWidth("100%");
		vatInclusiveCheck = getVATInclusiveCheckBox();
		totalTxt = createTransactionTotalNonEditableLabel();
		netAmount = new AmountLabel(Accounter.constants().netAmount());
		DynamicForm totalForm = new DynamicForm();
		totalForm.setFields(totalTxt, netAmount);

		DynamicForm vatCheckForm = new DynamicForm();
		vatCheckForm.setFields(vatInclusiveCheck);
		vatPanel.add(vatCheckForm);
		vatPanel.setCellHorizontalAlignment(vatCheckForm, ALIGN_RIGHT);
		vatPanel.add(totalForm);
		vatPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		vatPanel.setHorizontalAlignment(ALIGN_RIGHT);
		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		// For Editing writeCheck or PaySalesTax.
		// if (transactionObject != null) {
		// transactionItems = transactionObject.getTransactionItems();
		// if (takenPaySalesTax != null) {
		// paytoSelect.setRequired(false);
		// memoTextAreaItem.setDisabled(true);
		// toprintCheck.setDisabled(true);
		// transactionNumber = takenPaySalesTax.getNumber();
		// amtText.setAmount(takenPaySalesTax.getTotal());
		// date.setValue(takenPaySalesTax.getDate());
		// text.setValue(Utility.getNumberInWords(String
		// .valueOf(takenPaySalesTax.getTotal())));
		//
		// setGridType(TAXAGENCY_TRANSACTION_GRID);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.setTransactionView(this);
		// taxAgencyGrid.setCanEdit(true);
		// taxAgencyGrid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(false);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// // taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// // .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(true);
		// mainVLay.add(lab1);
		// nText
		// .setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
		// .toBePrinted()
		// : transactionNumber != null ? transactionNumber
		// : "");
		// mainVLay.add(labelLayout);
		// mainVLay.add(topHLay);
		// mainVLay.add(taxAgencyGrid);
		//
		// }
		// if{
		if (transactionObject != null) {
			transactionItems = transactionObject.getTransactionItems();
			writeCheckTaken = (ClientWriteCheck) transactionObject;
			transactionNumber.setValue(writeCheckTaken.getNumber());

			amtText.setAmount(writeCheckTaken.getTotal());
			memoTextAreaItem.setValue(writeCheckTaken.getMemo());
			date.setValue(writeCheckTaken.getDate());
			text.setValue(writeCheckTaken.getInWords());
			toprintCheck.setValue(writeCheckTaken.isToBePrinted());
			toprintCheck.setDisabled(true);
			// nText.setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
			// .toBePrinted()
			// : transactionNumber != null ? transactionNumber : "");
			switch (writeCheckTaken.getPayToType()) {
			case ClientWriteCheck.TYPE_CUSTOMER:

				setGridType(CUSTOMER_TRANSACTION_GRID);
				transactionCustomerGrid = getGrid();
				transactionCustomerGrid.setTransactionView(this);
				transactionCustomerGrid.setCanEdit(true);
				transactionCustomerGrid
						.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
				transactionCustomerGrid.setVisible(true);
				transactionCustomerGrid.init();
				transactionCustomerGrid.setWidth("100%");
				mainVLay.add(topHLay);
				setMenuRequired(true);
				mainVLay.add(transactionCustomerGrid);
				break;
			case ClientWriteCheck.TYPE_VENDOR:
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				setGridType(VENDOR_TRANSACTION_GRID);
				transactionVendorGrid = getGrid();
				transactionVendorGrid.setTransactionView(this);
				transactionVendorGrid.setVisible(true);
				transactionVendorGrid.setCanEdit(true);
				transactionVendorGrid
						.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
				transactionVendorGrid.isEnable = false;
				transactionVendorGrid.init();
				transactionVendorGrid.setWidth("100%");
				mainVLay.add(topHLay);
				setMenuRequired(true);
				mainVLay.add(transactionVendorGrid);
				break;

			}
			// }

		} else {
			setGridType(CUSTOMER_TRANSACTION_GRID);
			transactionCustomerGrid = getGrid();
			transactionCustomerGrid.setTransactionView(this);
			transactionCustomerGrid.setCanEdit(true);
			transactionCustomerGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
			transactionCustomerGrid.init();
			transactionCustomerGrid.setWidth("100%");
			setGridType(VENDOR_TRANSACTION_GRID);
			transactionVendorGrid = getGrid();
			transactionVendorGrid.setTransactionView(this);
			transactionVendorGrid.setCanEdit(true);
			transactionVendorGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
			transactionVendorGrid.init();
			transactionVendorGrid.setWidth("100%");
			// setGridType(VENDOR_TRANSACTION_GRID);
			// transactionVendorGrid = getGrid();
			// transactionVendorGrid.setTransactionView(this);
			// transactionVendorGrid.setCanEdit(true);
			// transactionVendorGrid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
			// transactionVendorGrid.init();
			transactionCustomerGrid.setVisible(true);

			transactionVendorGrid.setVisible(false);
			// taxAgencyGrid.setVisible(false);
			mainVLay.add(lab1);
			// HorizontalPanel panel = new HorizontalPanel();
			mainVLay.add(topHLay);
			setMenuRequired(true);

			mainVLay.add(transactionCustomerGrid);
		}

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			mainVLay.add(vatPanel);

		vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.add(createAddNewButton());
		vPanel.add(memoForm);
		vPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		vPanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		mainVLay.add(vPanel);
		menuButton.setType(AccounterButton.ADD_BUTTON);

		canvas.setWidth("100%");
		canvas.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(numForm);

		listforms.add(bankAccForm);
		listforms.add(payForm);
		listforms.add(amtForm);

	}

	// protected void setCheckNumber() {
	//
	// rpcUtilService.getNextCheckNumber(selectBankAcc.getID(),
	// new AsyncCallback<Long>() {
	//
	// public void onFailure(Throwable t) {
	// // //UIUtils.logError(
	// // "Failed to get the next check number!!", t);
	// nText.setValue(bankingConstants.toBePrinted());
	// checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;
	// return;
	// }
	//
	// public void onSuccess(Long result) {
	// if (result == null)
	// onFailure(null);
	//
	// checkNo = String.valueOf(result);
	// nText.setValue(result.toString());
	//
	// }
	//
	// });
	//
	// }

	//
	// private void setDisableStaeForFormItems() {
	//
	// for (FormItem formItem : formItems) {
	//
	// if (formItem != null)
	// formItem.setDisabled(isEdit);
	//
	// }
	//
	// }

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(transactionObject.getMemo());

	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	// if (isEdit) {
	// if (!transactionObject.isWriteCheck()) {
	//
	// if (!transactionObject.isPaySalesTax())
	// throw new Exception("Failed to load....");
	// else {
	// takenPaySalesTax = (ClientPaySalesTax) transactionObject;
	// transactionType = ClientTransaction.TYPE_PAY_SALES_TAX;
	//
	// }
	// }
	//
	// }
	//
	// }

	@Override
	public void setData(ClientWriteCheck data) {
		super.setData(data);
		if (isEdit) {
			if (transactionObject.isPaySalesTax()) {
				takenPaySalesTax = (ClientPaySalesTax) transactionObject;
				transactionType = ClientTransaction.TYPE_PAY_SALES_TAX;
			}

		}
	}

	@Override
	public void updateNonEditableItems() {
		if (payee != null) {

			if (payee instanceof ClientCustomer) {
				this.amtText.setAmount(transactionCustomerGrid.getTotal());
				text.setValue(Utility.getNumberInWords(transactionCustomerGrid
						.getTotal().toString()));
				totalTxt.setValue(transactionCustomerGrid.getTotal());
				netAmount.setAmount(transactionCustomerGrid.getGrandTotal());

			} else if (payee instanceof ClientVendor
					|| payee instanceof ClientTAXAgency) {
				this.amtText.setAmount(transactionVendorGrid.getTotal());
				text.setValue(Utility.getNumberInWords(transactionVendorGrid
						.getTotal() + ""));
				totalTxt.setValue(transactionVendorGrid.getTotal());
				netAmount.setAmount(transactionVendorGrid.getGrandTotal());
			}

			// else if (payee instanceof ClientTaxAgency) {
			// this.amtText.setAmount(taxAgencyGrid.getTotal());
			// text.setValue(Utility.getNumberInWords(taxAgencyGrid.getTotal()
			// .toString()));
			// }

		}
		// } else {
		// // if the grid values changed without selecting any payee,then
		// // this'll execute
		//
		// if (transactionVendorGrid != null) {
		// this.amtText.setAmount(transactionVendorGrid.getTotal());
		// text.setValue(Utility.getNumberInWords(transactionVendorGrid
		// .getTotal().toString()));
		// } else if (transactionCustomerGrid != null) {
		// this.amtText.setAmount(transactionCustomerGrid.getTotal());
		// text.setValue(Utility.getNumberInWords(transactionCustomerGrid
		// .getTotal().toString()));
		// } else if (taxAgencyGrid != null) {
		// this.amtText.setAmount(taxAgencyGrid.getTotal());
		// text.setValue(Utility.getNumberInWords(taxAgencyGrid.getTotal()
		// .toString()));
		// }
		// }

	}

	// @Override
	// public void onDraw() {
	// this.nText.setDisabled(true);
	// }

	@Override
	protected void showMenu(AccounterButton button) {
		if (payee != null) {
			switch (payee.getType()) {
			case ClientWriteCheck.TYPE_CUSTOMER:
			case ClientWriteCheck.TYPE_VENDOR:
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
					setMenuItems(button, Accounter.constants().accounts(),
							Accounter.constants().product()
					// FinanceApplication.constants().comment(),
					// FinanceApplication.constants()
					// .salesTax()
					);
				else
					setMenuItems(button, Accounter.constants().accounts(),
							Accounter.constants().product(),
							// FinanceApplication.constants().comment(),
							Accounter.constants().VATItem());
				// break;
				// case ClientWriteCheck.TYPE_VENDOR:
				// if (FinanceApplication.getCompany().getAccountingType() ==
				// ClientCompany.ACCOUNTING_TYPE_US)
				// setMenuItems(FinanceApplication.constants()
				// .nominalCodeItem(), FinanceApplication
				// .constants().product()
				// // FinanceApplication.constants().comment()
				// );
				// else
				// setMenuItems(FinanceApplication.constants()
				// .nominalCodeItem(), FinanceApplication
				// .constants().product(),
				// FinanceApplication.constants().VATItem(),
				// FinanceApplication.constants().comment());
				// break;
				// case ClientWriteCheck.TYPE_TAX_AGENCY:
				// if (FinanceApplication.getCompany().getAccountingType() ==
				// ClientCompany.ACCOUNTING_TYPE_US)
				// setMenuItems(FinanceApplication.constants()
				// .nominalCodeItem(), FinanceApplication
				// .constants().product()
				// // FinanceApplication.constants().comment()
				// );
				// else
				// setMenuItems(FinanceApplication.constants()
				// .nominalCodeItem(), FinanceApplication
				// .constants().product(),
				// FinanceApplication.constants().VATItem(),
				// FinanceApplication.constants().comment());
			}
		} else
			setMenuItems(button, Accounter.constants().accounts(), Accounter
					.constants().product()
			// FinanceApplication.constants().comment(),
			// FinanceApplication.constants().salesTax()
			);

	}

	@Override
	protected void onAddNew(String item) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (payee != null) {
			if (payee.getType() == ClientWriteCheck.TYPE_CUSTOMER) {
				if (item.equals(Accounter.constants().accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants().product())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);

				} else if (item.equals(Accounter.constants().comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				} else if (item.equals("Sales Tax")) {
					transactionItem
							.setType(ClientTransactionItem.TYPE_SALESTAX);
				}
				transactionCustomerGrid.addData(transactionItem);
			} else if (payee.getType() == ClientWriteCheck.TYPE_VENDOR
					|| payee.getType() == ClientWriteCheck.TYPE_TAX_AGENCY) {
				if (item.equals(Accounter.constants().accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants().product())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				} else if (item.equals(Accounter.constants().comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				transactionVendorGrid.addData(transactionItem);
			}
		} else {
			if (item.equals(Accounter.constants().accounts())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			} else if (item.equals(Accounter.constants().product())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			} else if (item.equals(Accounter.constants().comment())) {
				transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
			} else if (item.equals(Accounter.constants().salesTax())) {
				transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
			}
			transactionCustomerGrid.addData(transactionItem);
		}
	}

	@Override
	protected void onAddNew() {
		// ClientTransactionItem transactionItem = new ClientTransactionItem();
		// taxAgencyGrid.addData(transactionItem);
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.paytoSelect.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.bankAccSelect.addComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.PAYEE) {
				this.paytoSelect.addComboItem((ClientPayee) core);
			}

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.bankAccSelect.removeComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.PAYEE) {
				this.paytoSelect.removeComboItem((ClientPayee) core);
			}

			break;

		}
	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		date.setDisabled(isEdit);
		paytoSelect.setDisabled(isEdit);
		billToCombo.setDisabled(isEdit);
		amtText.setDisabled(isEdit);
		toprintCheck.setDisabled(isEdit);
		bankAccSelect.setDisabled(isEdit);
		memoTextAreaItem.setDisabled(false);
		// if (taxAgencyGrid != null) {
		// taxAgencyGrid.setDisabled(isEdit);
		// }
		if (transactionCustomerGrid != null) {
			transactionCustomerGrid.setCanEdit(true);
		}
		if (transactionVendorGrid != null) {
			transactionVendorGrid.setCanEdit(true);
		}

		super.onEdit();

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	public void PayToSelected(ClientPayee selectItem) {
		ClientWriteCheck check = (ClientWriteCheck) this.transactionObject;

		if (selectItem instanceof ClientCustomer) {
			transactionCustomerGrid.removeAllRecords();
			if (check.getCustomer() == selectItem.getID()) {
				transactionCustomerGrid.setRecords(check.getTransactionItems());
			}

		} else if (selectItem instanceof ClientVendor) {
			transactionVendorGrid.removeAllRecords();
			if (check.getVendor() == selectItem.getID()) {
				transactionVendorGrid.setRecords(check.getTransactionItems());
			}

		} else if (selectItem instanceof ClientTAXAgency) {
			transactionVendorGrid.removeAllRecords();
			if (check.getTaxAgency() == selectItem.getID()) {
				transactionVendorGrid.setRecords(check.getTransactionItems());
			}

		}

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().writeCheck();
	}
}
