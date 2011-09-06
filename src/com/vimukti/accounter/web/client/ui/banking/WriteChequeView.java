package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TaxAgencyTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionGrid;

public class WriteChequeView extends
		AbstractBankTransactionView<ClientWriteCheck> {

	private PayFromAccountsCombo bankAccSelect;

	private PayeeCombo paytoSelect;
	private AmountField balText, amtText;
	private DynamicForm bankAccForm;

	private HorizontalPanel labelLayout;
	public AmountLabel netAmount, totalTxt;
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

	private CheckboxItem vatInclusiveCheck;
	private DateField date;
	AccounterConstants accounterConstants = Accounter.constants();
	// private boolean isVendor;
	// private String transactionNumber = "";

	private DynamicForm amtForm;

	private ClientCompany company;
	private List<ClientAccount> payFromAccounts;

	private String checkNo = ClientWriteCheck.IS_TO_BE_PRINTED;

	private DynamicForm numForm;

	private ArrayList<DynamicForm> listforms;

	private HorizontalPanel vatPanel;

	private VerticalPanel vPanel;

	private boolean locationTrackingEnabled;

	private CustomerTransactionGrid transactionCustomerGrid;
	private VendorTransactionGrid transactionVendorGrid;

	private WriteChequeView() {
		super(ClientTransaction.TYPE_WRITE_CHECK);
		this.company = getCompany();
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

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
		// if (transaction == null) {
		transactionVendorGrid.setVisible(true);
		changeGrid(transactionVendorGrid);
		transactionVendorGrid.updateTotals();
		// }
	}

	protected void updateAddressAndGrid() {
		// Set<Address> add = null;
		if (payee instanceof ClientCustomer) {
			selectedCustomer = (ClientCustomer) payee;
			addressList = selectedCustomer.getAddress();
			// if (transaction == null) {
			transactionCustomerGrid.setVisible(true);
			changeGrid(transactionCustomerGrid);
			transactionCustomerGrid.updateTotals();
			// }
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
		if (isInViewMode()) {
			if (transaction.getAddress() != null)
				billToaddressSelected(getAddressById(transaction.getAddress()
						.getID()));
		}
		initBillToCombo();

	}

	@Override
	protected void reload() {
		if (!saveAndClose)
			try {
				ActionFactory.getWriteChecksAction().run(null, true);
			} catch (Throwable e) {

				// //UIUtils.logError("Failed to load Write Check View", e);
			}

	}

	public void changeGrid(ListGrid<ClientTransactionItem> gridView) {
		if (gridView instanceof CustomerTransactionGrid) {

			CustomerTransactionGrid customerGrid = (CustomerTransactionGrid) gridView;
			setMenuRequired(true);
			// mainVLay.remove(customerGrid);
			// mainVLay.add(customerGrid);
		} else if (gridView instanceof VendorTransactionGrid
				|| gridView instanceof TaxAgencyTransactionGrid) {
			@SuppressWarnings({ "unused" })
			VendorTransactionGrid vendorGrid = (VendorTransactionGrid) gridView;
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

		if (isInViewMode() && transaction.getAddress() != null) {
			addressList = payee.getAddress();
			toBeShown = getAddressById(transaction.getAddress().getID());
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
		if (isInViewMode()) {

			selectBankAcc = this.company.getAccount(transaction
					.getBankAccount());

		}
		// else if (takenPaySalesTax != null) {
		//
		// selectBankAcc = getCompany().getAccount(
		// takenPaySalesTax.getPayFrom());
		//
		// }
		if (selectBankAcc != null) {

			bankAccSelect.setComboItem(selectBankAcc);
			updateBalance();
		}

		bankAccSelect.setDisabled(isInViewMode());
		// bankAccSelect.setShowDisabled(false);

	}

	public void initPayToCombo() {
		List<ClientPayee> payees = getCompany().getActivePayees();

		if (payees != null) {

			paytoSelect.initCombo(payees);
			if (isInViewMode()) {
				selectedTaxAgency = company.getTaxAgency(transaction
						.getTaxAgency());
				if (selectedTaxAgency != null) {
					// paytoSelect.setPayee((Payee) selectedTaxAgency);
					paytoSelect.setComboItem(selectedTaxAgency);
				}

				paytoSelect.setDisabled(isInViewMode());
				paytoSelect.setDisabled(false);
				return;
			}
			newPayToMethod();
		}
	}

	protected void newPayToMethod() {
		if (isInViewMode()) {
			switch (transaction.getPayToType()) {
			case ClientWriteCheck.TYPE_VENDOR:
				paytoSelect.setComboItem(getCompany().getVendor(
						transaction.getVendor()));
				payee = this.company.getVendor(transaction.getVendor());

				break;
			case ClientWriteCheck.TYPE_CUSTOMER:
				ClientCompany clientCompany = this.company;
				paytoSelect.setComboItem(clientCompany.getCustomer(transaction
						.getCustomer()));
				payee = clientCompany.getCustomer(transaction.getCustomer());

				break;
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				paytoSelect.setComboItem(getCompany().getTaxAgency(
						transaction.getTaxAgency()));
				payee = this.company.getTaxAgency(transaction.getTaxAgency());
				break;
			}
			paytoSelect.setDisabled(isInViewMode());
			paytoSelect.setDisabled(false);
			updateAddressAndGrid();

		}

	}

	private void setDisableFields() {
		if (isInViewMode()) {
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
	// AccounterAsyncCallback<String> transactionNumberCallback = new
	// AccounterAsyncCallback<String>() {
	//
	// public void onException(AccounterException caught) {
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
		if (isInViewMode()) {
			Double transactionTotal = ((ClientWriteCheck) transaction)
					.getTotal();
			if (transactionTotal != null) {
				amtText.setAmount(transactionTotal.doubleValue());
			}

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// Validations
		// 1. payForm validation
		// 2. bankAccForm validation
		// 3. if(isBlankTransaction(transactionVendorGrid or
		// transactionCustomerGrid)) ERROR
		// else transactionVendorGrid or transactionCustomerGrid validation
		// 4. if(!validPositiveAmount(gridTotalAmount)) ERROR
		// if (result.getErrors().size() > 0)
		// return result;

		result.add(DynamicForm.validate(payForm, bankAccForm));

		if (payee != null) {
			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				if (AccounterValidator
						.isBlankTransaction(transactionCustomerGrid)) {
					result.addError(transactionCustomerGrid,
							accounterConstants.blankTransaction());
				} else
					result.add(transactionCustomerGrid.validateGrid());
				break;
			case ClientPayee.TYPE_VENDOR:
			case ClientPayee.TYPE_TAX_AGENCY:
				if (AccounterValidator
						.isBlankTransaction(transactionVendorGrid)) {
					result.addError(transactionVendorGrid,
							accounterConstants.blankTransaction());
				} else
					result.add(transactionVendorGrid.validateGrid());
				break;
			// case ClientPayee.TYPE_TAX_AGENCY:
			// return AccounterValidator
			// .isBlankTransaction(taxAgencyGrid);
			}
		}

		if (!validateAmount()) {
			result.addError(memoTextAreaItem,
					accounterConstants.transactiontotalcannotbe0orlessthan0());
		}

		return result;
	}

	private boolean validateAmount() {
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
			return AccounterValidator.isPositiveAmount(total);
		}
		return false;
	}

	@Override
	public void saveAndUpdateView() {

		if (isInViewMode()) {
			updatePaySalesTax();
			return;
		}

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of the write check
		transaction.setType(ClientTransaction.TYPE_WRITE_CHECK);

		transaction.setCheckNumber(checkNo);

		transaction.setTransactionDate(transactionDate.getDate());

		// Setting Bank account
		selectBankAcc = bankAccSelect.getSelectedValue();
		if (selectBankAcc != null) {
			transaction.setBankAccount(selectBankAcc.getID());
		}

		// Setting Balance
		if (balText.getAmount() != null)
			transaction.setBalance(balText.getAmount());
		// setting paymentmethod
		transaction.setPaymentMethod("Check");

		// Setting Address
		transaction.setAddress(billingAddress);

		// Setting Transactions
		if (payee != null)
			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				transaction.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				transaction.setCustomer(selectedCustomer.getID());
				transaction.setTransactionItems(transactionCustomerGrid
						.getallTransactionItems(transaction));
				transaction.setTotal(transactionCustomerGrid.getTotal());

				transaction.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				transaction.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				transaction.setVendor(selectedVendor.getID());
				transaction.setTransactionItems(transactionVendorGrid
						.getallTransactionItems(transaction));
				transaction.setTotal(transactionVendorGrid.getTotal());
				transaction.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				transaction.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);
				transaction.setTaxAgency(selectedTaxAgency.getID());
				transaction.setTransactionItems(transactionVendorGrid
						.getallTransactionItems(transaction));
				transaction.setTotal(transactionVendorGrid.getTotal());
				// for (ClientTransactionItem rec : transactionVendorGrid
				// .getallTransactions(transaction)) {
				// rec.setType(ClientTransactionItem.TYPE_SALESTAX);
				// }
				// transaction.setTransactionItems(transactionVendorGrid
				// .getallTransactions(transaction));
				// transaction.setTotal(transactionVendorGrid.getTotal());
				transaction.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
		transaction.setTotal(amtText.getAmount());
		transaction.setAmount(amtText.getAmount());
		transaction.setInWords(amtText.getValue().toString());

		// Setting Date
		if (date != null)
			transaction
					.setDate(((ClientFinanceDate) date.getValue()).getDate());
		// setting transactoin number
		transaction.setNumber(transactionNumber.getValue().toString());

		// setting checknumber
		transaction.setCheckNumber(checkNo);

		// setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		if (vatInclusiveCheck != null) {
			transaction.setAmountsIncludeVAT((Boolean) vatInclusiveCheck
					.getValue());
		}

		if (toprintCheck.getValue() != null) {
			transaction.setToBePrinted((Boolean) toprintCheck.getValue());

		}
	}

	private void updatePaySalesTax() {
		transactionSuccess(transaction);

	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(bankingConstants.writeCheck());
		Label lab1 = new Label(Accounter.constants().writeCheck() + "("
				+ getTransactionStatus() + ")");
		lab1.addStyleName(Accounter.constants().labelTitle());
		if (isInViewMode())
			lab1.setText(Accounter.constants().taxAgentPayment());

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(isInViewMode());
		date = createTransactionDateItem();
		date.setShowTitle(false);
		date.setColSpan(2);
		date.setDisabled(isInViewMode());

		numForm = new DynamicForm();
		numForm.setNumCols(6);
		numForm.addStyleName("datenumber-panel");
		numForm.setFields(date, transactionNumber);
		if (locationTrackingEnabled)
			numForm.setFields(locationCombo);
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

		balText = new AmountField(Accounter.constants().balance(), this);
		balText.setWidth(100);
		balText.setDisabled(true);

		bankAccSelect = new PayFromAccountsCombo(Accounter.messages()
				.bankAccount(Global.get().Account()));
		// bankAccSelect.setWidth(100);
		bankAccSelect.setRequired(true);
		bankAccSelect.setDisabled(isInViewMode());
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
		// forms.add(bankAccForm);

		paytoSelect = new PayeeCombo(Accounter.constants().payTo());
		// paytoSelect.setWidth(100);
		paytoSelect.setRequired(true);
		paytoSelect.setDisabled(isInViewMode());
		paytoSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayee>() {
					public void selectedComboBoxItem(ClientPayee selectItem) {
						amtText.setValue("0.00");
						if (payee != null) {
							if (payee instanceof ClientCustomer) {
								if (transaction == null)
									transactionCustomerGrid.removeAllRecords();
							} else if (payee instanceof ClientVendor
									|| payee instanceof ClientTAXAgency) {
								if (transaction == null)
									transactionVendorGrid.removeAllRecords();
								// } else if (payee instanceof ClientTAXAgency)
								// {
								// taxAgencyGrid.removeAllRecords();
							}
						}

						if (isInViewMode() && payee != null) {
							if (payee.getType() != selectItem.getType()) {
								Accounter
										.showError(Accounter
												.messages()
												.youcannotchangeaCustomertypetoVendortype(
														Global.get().customer(),
														Global.get().Vendor()));
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

		// formItems.add(paytoSelect);

		billToCombo = createBillToComboItem();
		billToCombo.setDisabled(true);
		// billToCombo.setWidth(100);

		amtText = new AmountField(Accounter.constants().amount(), this);
		amtText.setWidth(100);
		amtText.setAmount(0.00);
		amtText.setDisabled(isInViewMode());

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
		payForm.setFields(paytoSelect, billToCombo, amtText);
		payForm.getCellFormatter().setWidth(0, 0, "170px");
		// forms.add(payForm);

		// amtForm = new DynamicForm();
		// amtForm.setWidth("50%");
		// // amtForm.setFields(amtText, text, memoTextAreaItem, toprintCheck);
		// amtForm.setFields(amtText);
		// amtForm.getCellFormatter().setWidth(0, 0, "100");
		// amtForm.getCellFormatter().setWidth(1, 0, "170px");

		// HorizontalPanel amtPanel = new HorizontalPanel();
		// amtPanel.setWidth("50%");
		// amtPanel.add(amtForm);

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
		// topHLay.add(amtForm);

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
		if (isInViewMode()) {
			transactionItems = transaction.getTransactionItems();
			transaction = (ClientWriteCheck) transaction;
			transactionNumber.setValue(transaction.getNumber());

			amtText.setAmount(transaction.getTotal());
			memoTextAreaItem.setValue(transaction.getMemo());
			date.setValue(transaction.getDate());
			toprintCheck.setValue(transaction.isToBePrinted());
			toprintCheck.setDisabled(true);
			// nText.setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
			// .toBePrinted()
			// : transactionNumber != null ? transactionNumber : "");
			switch (transaction.getPayToType()) {
			case ClientWriteCheck.TYPE_CUSTOMER:

				transactionCustomerGrid = new CustomerTransactionGrid();
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
				transactionVendorGrid = new VendorTransactionGrid();
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
			transactionCustomerGrid = new CustomerTransactionGrid();
			transactionCustomerGrid.setTransactionView(this);
			transactionCustomerGrid.setCanEdit(true);
			transactionCustomerGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
			transactionCustomerGrid.init();
			transactionCustomerGrid.setWidth("100%");
			transactionVendorGrid = new VendorTransactionGrid();
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

		AddNewButton addNewButton = createAddNewButton();
		vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.add(addNewButton);
		addNewButton.getElement().getParentElement()
				.setClassName("Writecheck_addNew");

		vPanel.add(memoForm);
		vPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		vPanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		mainVLay.add(vPanel);

		this.setWidth("100%");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(numForm);

		listforms.add(bankAccForm);
		listforms.add(payForm);
		listforms.add(amtForm);

	}

	// protected void setCheckNumber() {
	//
	// rpcUtilService.getNextCheckNumber(selectBankAcc.getID(),
	// new AccounterAsyncCallback<Long>() {
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
		setMemoTextAreaItem(transaction.getMemo());

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
	public void updateNonEditableItems() {
		if (payee != null) {

			if (payee instanceof ClientCustomer) {
				this.amtText.setAmount(transactionCustomerGrid.getTotal());
				amtText.setValue(String.valueOf(transactionCustomerGrid
						.getTotal()));
				totalTxt.setValue(String.valueOf(transactionCustomerGrid
						.getTotal()));
				netAmount.setAmount(transactionCustomerGrid.getGrandTotal());

			} else if (payee instanceof ClientVendor
					|| payee instanceof ClientTAXAgency) {
				this.amtText.setAmount(transactionVendorGrid.getTotal());
				amtText.setValue(String.valueOf(transactionVendorGrid
						.getTotal()));
				totalTxt.setValue(String.valueOf(transactionVendorGrid
						.getTotal()));
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
	public void showMenu(Widget button) {
		if (payee != null) {
			switch (payee.getType()) {
			case ClientWriteCheck.TYPE_CUSTOMER:
			case ClientWriteCheck.TYPE_VENDOR:
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
					setMenuItems(
							button,
							Accounter.messages().accounts(
									Global.get().Account()), Accounter
									.constants().productItem()
					// FinanceApplication.constants().comment(),
					// FinanceApplication.constants()
					// .salesTax()
					);
				else
					setMenuItems(
							button,
							Accounter.messages().accounts(
									Global.get().Account()), Accounter
									.constants().productItem(),
							// FinanceApplication.constants().comment(),
							Accounter.constants().vatItem());
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
			setMenuItems(button,
					Accounter.messages().accounts(Global.get().Account()),
					Accounter.constants().productItem()
			// FinanceApplication.constants().comment(),
			// FinanceApplication.constants().salesTax()
			);

	}

	@Override
	protected void onAddNew(String item) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (payee != null) {
			if (payee.getType() == ClientWriteCheck.TYPE_CUSTOMER) {
				if (item.equals(Accounter.messages().accounts(
						Global.get().Account()))) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants().productItem())) {
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
				if (item.equals(Accounter.messages().accounts(
						Global.get().Account()))) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants().productItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				} else if (item.equals(Accounter.constants().comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				transactionVendorGrid.addData(transactionItem);
			}
		} else {
			if (item.equals(Accounter.messages().accounts(
					Global.get().Account()))) {
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			} else if (item.equals(Accounter.constants().productItem())) {
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
	public void onAddNew() {
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		paytoSelect.setDisabled(isInViewMode());
		billToCombo.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());
		toprintCheck.setDisabled(isInViewMode());
		bankAccSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(false);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
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
		ClientWriteCheck check = (ClientWriteCheck) this.transaction;

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

	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientWriteCheck());
		} else {
			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());

			}
			initMemoAndReference();

		}
		initTransactionNumber();
		initPayToCombo();
		setDisableFields();
		initBankaccountCombo();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
	}

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getTransactionGrid() {
		return taxAgencyGrid;
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		taxAgencyGrid.addData(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		taxAgencyGrid.refreshAllRecords();

	}
}
