package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
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
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
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
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public class WriteChequeView extends
		AbstractBankTransactionView<ClientWriteCheck> {

	private PayFromAccountsCombo bankAccSelect;

	private PayeeCombo paytoSelect;
	private AmountField balText, amtText;
	private DynamicForm bankAccForm;

	private HorizontalPanel labelLayout;
	public AmountLabel netAmount, totalTxt;

	AmountLabel vatTotalNonEditableText;
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

	private TaxItemCombo vendorTDSTaxCode;

	protected ClientPayee payee;
	private VerticalPanel mainVLay, nHPanel;

	private LinkedHashMap<String, ClientAddress> billToAddress;

	protected ClientTAXAgency selectedTaxAgency;

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

	private VerticalPanel vatPanel;
	private VerticalPanel amountPanel;
	private VerticalPanel vPanel;

	private boolean locationTrackingEnabled;

	private VendorAccountTransactionTable transactionVendorAccountTable;
	private VendorItemTransactionTable transactionVendorItemTable;
	private AddNewButton accountTableButton, itemTableButton;
	private DisclosurePanel vendorAccountsDisclosurePanel,
			vendorItemsDisclosurePanel;

	private TAXCodeCombo taxCodeSelect;

	private ClientTAXCode taxCode;

	private double salesTax = 0.0D;

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
	// public void updateTotals() {
	// // if (transaction == null) {
	// transactionVendorTable.setVisible(true);
	// transactionVendorTable.setDisabled(isInViewMode());
	// changeGrid(transactionVendorTable);
	// transactionVendorTable.updateTotals();
	// // }
	// }

	protected void updateAddressAndGrid() {
		// Set<Address> add = null;

		if (payee instanceof ClientCustomer) {
			selectedCustomer = (ClientCustomer) payee;
			addressList = selectedCustomer.getAddress();
			// if (transaction == null) {
			// customerAccountsDisclosurePanel.setVisible(true);
			// customerItemsDisclosurePanel.setVisible(true);
			// changeGrid(customerAccountsDisclosurePanel,
			// customerItemsDisclosurePanel);
			// }
		} else if (payee instanceof ClientVendor) {

			selectedVendor = (ClientVendor) payee;
			addressList = selectedVendor.getAddress();
			// vendorAccountsDisclosurePanel.setVisible(true);
			// vendorItemsDisclosurePanel.setVisible(true);
			// changeGrid(vendorAccountsDisclosurePanel,
			// vendorItemsDisclosurePanel);

		} else if (payee instanceof ClientTAXAgency) {
			selectedTaxAgency = (ClientTAXAgency) payee;
			addressList = selectedTaxAgency.getAddress();
			// vendorAccountsDisclosurePanel.setVisible(true);
			// vendorItemsDisclosurePanel.setVisible(true);
			// changeGrid(vendorAccountsDisclosurePanel,
			// vendorItemsDisclosurePanel);

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
		transactionVendorAccountTable.updateTotals();
		transactionVendorItemTable.updateTotals();
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

	// public void changeGrid(DisclosurePanel gridView, DisclosurePanel
	// gridView2) {
	// setMenuRequired(true);
	// // mainVLay.remove(vendorGrid);
	// // mainVLay.add(vendorGrid);
	// // } else if (gridView instanceof TaxAgencyTransactionGrid) {
	// //
	// // TaxAgencyTransactionGrid taxAgencyGrid = (TaxAgencyTransactionGrid)
	// // gridView;
	// // setMenuRequired(false);
	// // // mainVLay.remove(taxAgencyGrid);
	// // // mainVLay.add(taxAgencyGrid);
	// // }Ou
	// if (transactionVendorAccountTable != null)
	// mainVLay.remove(vendorAccountsDisclosurePanel);
	// if (transactionVendorItemTable != null)
	// mainVLay.remove(vendorItemsDisclosurePanel);
	// // if (taxAgencyGrid != null)
	// // mainVLay.remove(taxAgencyGrid);
	// if (transactionCustomerAccountTable != null)
	// mainVLay.remove(customerAccountsDisclosurePanel);
	// if (transactionCustomerItemTable != null)
	// mainVLay.remove(customerItemsDisclosurePanel);
	// mainVLay.add(gridView);
	// // mainVLay.add(accountTableButton);
	// mainVLay.add(gridView2);
	// // mainVLay.add(itemTableButton);
	// mainVLay.add(vPanel);
	// if (getCompany().getPreferences().isRegisteredForVAT()) {
	// // It should be like thid only,becoz vatPanel is getting add befor
	// // the gird.So,we need to remove n add after grid
	// mainVLay.remove(vatPanel);
	// mainVLay.add(vatPanel);
	// }
	// // mainVLay.redraw();
	// }

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
				ClientCustomer customer = null;
				if (transaction.getPayToType() == ClientPayee.TYPE_CUSTOMER) {
					customer = getCompany().getCustomer(
							transaction.getCustomer());
					payee = customer;
					paytoSelect.setComboItem(customer);
				} else if (transaction.getPayToType() == ClientPayee.TYPE_VENDOR) {
					ClientVendor vendor2 = getCompany().getVendor(
							transaction.getVendor());
					payee = vendor2;
					paytoSelect.setComboItem(vendor2);
				} else if (transaction.getPayToType() == ClientPayee.TYPE_TAX_AGENCY) {
					ClientTAXAgency taxAgency = getCompany().getTaxAgency(
							transaction.getTaxAgency());
					paytoSelect.setComboItem(taxAgency);
				}
				paytoSelect.setDisabled(isInViewMode());
				transactionVendorAccountTable
						.setRecords(getAccountTransactionItems(transaction
								.getTransactionItems()));
				transactionVendorItemTable
						.setRecords(getItemTransactionItems(transaction
								.getTransactionItems()));
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

		result.add(DynamicForm.validate(payForm));
		result.add(DynamicForm.validate(bankAccForm));

		// FIXME Need to validate grids.
		if (transactionVendorAccountTable.getAllRows().isEmpty()
				&& transactionVendorItemTable.getAllRows().isEmpty()) {
			result.addError(transactionVendorAccountTable,
					accounterConstants.blankTransaction());
		} else {
			result.add(transactionVendorAccountTable.validateGrid());
			result.add(transactionVendorItemTable.validateGrid());
		}

		if (!validateAmount()) {
			result.addError(memoTextAreaItem,
					accounterConstants.transactiontotalcannotbe0orlessthan0());
		}
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							accounterConstants.enterTaxCode());
				}

			}
		}

		return result;
	}

	private boolean validateAmount() {
		if (payee != null) {
			double total = 0.0;
			total += transactionVendorAccountTable.getGrandTotal();
			total += transactionVendorItemTable.getGrandTotal();
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
		// FIXME Need to assign transaction Items from to tables.
		if (payee != null) {
			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				transaction.setCustomer(selectedCustomer.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				transaction.setVendor(selectedVendor.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				transaction.setTaxAgency(selectedTaxAgency.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
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
		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
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

		vendorTDSTaxCode = new TaxItemCombo(Accounter.constants().tds(),
				ClientTAXItem.TAX_TYPE_TDS);

		vendorTDSTaxCode.setDisabled(true);

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

		if (getCompany().getPreferences().isTDSEnabled()) {
			bankAccForm.setFields(bankAccSelect, balText, vendorTDSTaxCode);
		} else {
			bankAccForm.setFields(bankAccSelect, balText);

		}
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			bankAccForm.setFields(classListCombo);
		}
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
						vendorTDSTaxCode.setSelected("");
						if (payee != null) {
							vendorTDSTaxCode.setSelected(vendorTDSTaxCode
									.getDisplayName(getCompany().getTAXItem(
											payee.getTaxItemCode())));

							transactionVendorAccountTable.resetRecords();
							transactionVendorItemTable.resetRecords();
							// } else if (payee instanceof ClientTAXAgency)
							// {
							// taxAgencyGrid.removeAllRecords();
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
							vendorTDSTaxCode.setSelected(vendorTDSTaxCode
									.getDisplayName(getCompany().getTAXItem(
											payee.getTaxItemCode())));

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

		vatPanel = new VerticalPanel();
		amountPanel = new VerticalPanel();
		vatPanel.setWidth("100%");
		amountPanel.setWidth("100%");
		vatinclusiveCheck = getVATInclusiveCheckBox();
		totalTxt = createTransactionTotalNonEditableLabel();
		vatTotalNonEditableText = new AmountLabel("Tax");

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		// DynamicForm totalForm = new DynamicForm();
		// totalForm.setFields(netAmount, totalTaxtxt, totalTxt);
		// totalForm.addStyleName("invoice-total");
		// DynamicForm vatCheckForm = new DynamicForm();

		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setFields(netAmount);
		if (isTrackTax()) {
			totalForm.setFields(vatTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				DynamicForm vatCheckForm = new DynamicForm();
				taxCodeSelect = createTaxCodeSelectItem();
				// taxCodeSelect.setVisible(isInViewMode());
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect);
				vatPanel.setCellHorizontalAlignment(vatCheckForm, ALIGN_CENTER);
				vatPanel.add(form);
				if (isTrackPaidTax()) {
					vatCheckForm.setFields(vatinclusiveCheck);
					vatCheckForm.addStyleName("boldtext");
				}
				vatPanel.add(vatCheckForm);
				vatPanel.setCellHorizontalAlignment(vatCheckForm, ALIGN_RIGHT);
				// } else {

			}
		}
		totalForm.setFields(totalTxt);
		totalForm.addStyleName("boldtext");
		amountPanel.add(totalForm);
		amountPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		amountPanel.setHorizontalAlignment(ALIGN_RIGHT);
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

		transactionVendorAccountTable = new VendorAccountTransactionTable(
				false, isTrackTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				WriteChequeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return WriteChequeView.this.isShowPriceWithVat();
			}
		};
		transactionVendorAccountTable.setDisabled(isInViewMode());

		transactionVendorItemTable = new VendorItemTransactionTable(false,
				isTrackTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				WriteChequeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return WriteChequeView.this.isShowPriceWithVat();
			}
		};
		transactionVendorItemTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel vendorAccountFlowPanel = new FlowPanel();
		vendorAccountsDisclosurePanel = new DisclosurePanel(
				"Itemize by Account");
		vendorAccountFlowPanel.add(transactionVendorAccountTable);
		vendorAccountFlowPanel.add(accountTableButton);
		vendorAccountsDisclosurePanel.setContent(vendorAccountFlowPanel);
		vendorAccountsDisclosurePanel.setOpen(true);
		vendorAccountsDisclosurePanel.setWidth("100%");

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		FlowPanel vendorItemsFlowPanel = new FlowPanel();
		vendorItemsDisclosurePanel = new DisclosurePanel(
				"Itemize by Product/Service");
		vendorItemsFlowPanel.add(transactionVendorItemTable);
		vendorItemsFlowPanel.add(itemTableButton);
		vendorItemsDisclosurePanel.setContent(vendorItemsFlowPanel);
		vendorItemsDisclosurePanel.setWidth("100%");

		if (isInViewMode()) {
			transactionItems = transaction.getTransactionItems();
			transactionNumber.setValue(transaction.getNumber());

			amtText.setAmount(transaction.getTotal());
			memoTextAreaItem.setValue(transaction.getMemo());
			date.setValue(transaction.getDate());
			toprintCheck.setValue(transaction.isToBePrinted());
			toprintCheck.setDisabled(true);
			// nText.setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
			// .toBePrinted()
			// : transactionNumber != null ? transactionNumber : "");
			// }

		}

		mainVLay.add(topHLay);
		mainVLay.add(vendorAccountsDisclosurePanel);
		mainVLay.add(vendorItemsDisclosurePanel);

		vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		// vPanel.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);

		// HorizontalPanel bottomPanel = new HorizontalPanel();
		// bottomPanel.setWidth("100%");
		// bottomPanel.add(memoForm);
		// if (isTrackTax()) {
		// if (!isTaxPerDetailLine()) {
		// taxCodeSelect = createTaxCodeSelectItem();
		// // taxCodeSelect.setVisible(isInViewMode());
		// DynamicForm form = new DynamicForm();
		// form.setFields(taxCodeSelect);
		// bottomPanel.add(form);
		// }
		// }
		// // if (getCompany().getPreferences().isRegisteredForVAT()) {
		bottomPanel.add(vatPanel);
		bottomPanel.add(amountPanel);

		// }

		vPanel.add(bottomPanel);
		bottomPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		vPanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		mainVLay.add(vPanel);

		this.setWidth("100%");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(numForm);

		listforms.add(bankAccForm);
		listforms.add(payForm);
		listforms.add(amtForm);
		settabIndexes();

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

		if (transactionVendorAccountTable == null
				|| transactionVendorItemTable == null) {
			return;
		}
		double total = transactionVendorAccountTable.getGrandTotal()
				+ transactionVendorItemTable.getGrandTotal();
		this.amtText.setAmount(total);
		amtText.setValue(String.valueOf(total));
		totalTxt.setValue(String.valueOf(total));
		double grandTotal = transactionVendorAccountTable.getLineTotal()
				+ transactionVendorItemTable.getLineTotal();
		if (getPreferences().isTrackPaidTax()) {
			vatTotalNonEditableText.setAmount(total - grandTotal);
		}
		netAmount.setAmount(grandTotal);

	}

	// @Override
	// public void onDraw() {
	// this.nText.setDisabled(true);
	// }

	@Override
	public void showMenu(Widget button) {
		// if (payee != null) {
		// switch (payee.getType()) {
		// case ClientWriteCheck.TYPE_CUSTOMER:
		// case ClientWriteCheck.TYPE_VENDOR:
		// case ClientWriteCheck.TYPE_TAX_AGENCY:
		// setMenuItems(button, Accounter.messages().accounts(
		// Global.get().Account()), Accounter.constants()
		// .productItem()
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants()
		// .salesTax()
		// );
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
		// }
		// } else
		setMenuItems(button, Global.get().Account(), Accounter.constants()
				.productOrServiceItem());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().salesTax()
		// );

	}

	@Override
	protected void onAddNew(String item) {
		// FIXME Need to add new transaction item to appropriate grid.
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (payee != null) {
			if (payee.getType() == ClientWriteCheck.TYPE_CUSTOMER) {
				if (item.equals(Accounter.messages().accounts(
						Global.get().Account()))) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants()
						.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);

				} else if (item.equals(Accounter.constants().comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// else if (item.equals("Sales Tax")) {
				// transactionItem
				// .setType(ClientTransactionItem.TYPE_SALESTAX);
				// }
				// transactionCustomerTable.add(transactionItem);
			} else if (payee.getType() == ClientWriteCheck.TYPE_VENDOR
					|| payee.getType() == ClientWriteCheck.TYPE_TAX_AGENCY) {
				if (item.equals(Accounter.messages().accounts(
						Global.get().Account()))) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(Accounter.constants()
						.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				} else if (item.equals(Accounter.constants().comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// transactionVendorTable.add(transactionItem);
			}
		} else {
			if (item.equals(Accounter.messages().accounts(
					Global.get().Account()))) {
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			} else if (item
					.equals(Accounter.constants().productOrServiceItem())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			} else if (item.equals(Accounter.constants().comment())) {
				transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
			}
			// else if (item.equals(Accounter.constants().salesTax())) {
			// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
			// }
			// transactionCustomerTable.add(transactionItem);
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
		if (transactionVendorAccountTable != null)
			transactionVendorAccountTable.setDisabled(isInViewMode());
		if (transactionVendorItemTable != null)
			transactionVendorItemTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(false);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				taxCodeSelect.setDisabled(isInViewMode());
			}
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

		// FIXME Need to set transaction items.
		// if (selectItem instanceof ClientCustomer) {
		// transactionCustomerTable.removeAllRecords();
		// if (check.getCustomer() == selectItem.getID()) {
		// transactionCustomerTable
		// .setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientVendor) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getVendor() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientTAXAgency) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getTaxAgency() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// }

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().writeCheck();
	}

	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientWriteCheck());
		} else {

			// if (getPreferences().isTrackPaidTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }
			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText.setAmount(transaction.getTotal()
							- transaction.getNetAmount());
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
					}
				}
			}

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());

			}
			initMemoAndReference();
			initAccounterClass();
		}
		initTransactionNumber();
		initPayToCombo();

		setDisableFields();
		initBankaccountCombo();
		updateAddressAndGrid();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(transactionVendorAccountTable.getAllRows());
		list.addAll(transactionVendorItemTable.getAllRows());
		return list;

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// FIXME
		// if (ClientWriteCheck.TYPE_CUSTOMER == transaction.getPayToType()) {
		// transactionCustomerTable.add(transactionItem);
		// } else {
		// transactionVendorTable.add(transactionItem);
		// }
	}

	@Override
	protected void refreshTransactionGrid() {
		if (transactionVendorAccountTable != null) {
			transactionVendorAccountTable.updateTotals();
		}
		if (transactionVendorItemTable != null) {
			transactionVendorItemTable.updateTotals();
		}
	}

	private void settabIndexes() {
		paytoSelect.setTabIndex(1);
		billToCombo.setTabIndex(2);
		amtText.setTabIndex(3);
		date.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		bankAccSelect.setTabIndex(6);
		balText.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		vatinclusiveCheck.setTabIndex(9);
		// menuButton.setTabIndex(10);
		saveAndCloseButton.setTabIndex(11);
		saveAndNewButton.setTabIndex(12);
		cancelButton.setTabIndex(13);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		transactionVendorAccountTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		transactionVendorItemTable.add(item);
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			transactionVendorAccountTable.setTaxCode(taxCode.getID(), true);
			transactionVendorItemTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}

	}
}
