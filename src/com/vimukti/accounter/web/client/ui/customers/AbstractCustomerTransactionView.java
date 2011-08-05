/**
 * 
 */
package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.PriceLevelCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionGrid;

/**
 * Abstract Class for All Customer Transaction Views
 * 
 * @author Fernandez
 * 
 */
public abstract class AbstractCustomerTransactionView<T> extends
		AbstractTransactionBaseView<T> {

	private AbstractCustomerTransactionView<T> customerTransactionViewInstance;

	AccounterConstants customerConstants = Accounter.constants();

	/**
	 * 
	 */

	@Override
	protected abstract void initTransactionViewData(
			ClientTransaction transactionObject);

	protected List<ClientCustomer> customers;

	protected Set<ClientContact> contacts;

	protected ClientPriceLevel priceLevel;

	protected ClientTAXGroup taxGroup;

	protected ClientTAXItem taxItem;

	protected ClientTAXCode taxCode;

	protected ClientSalesPerson salesPerson;

	protected ClientContact contact;

	protected DateField deliveryDate;
	protected ClientPaymentTerms paymentTerm;

	protected List<ClientSalesPerson> salesPersons;

	protected List<ClientPriceLevel> priceLevels;

	protected List<ClientTAXGroup> taxGroups;

	protected List<ClientTAXCode> taxCodes;

	protected List<ClientPaymentTerms> paymentTermsList;

	// protected AmountField ;

	protected AmountLabel transactionTotalNonEditableText, netAmountLabel,
			vatTotalNonEditableText, balanceDueNonEditableText,
			paymentsNonEditableText, salesTaxTextNonEditable;

	public Double transactionTotal = 0.0D;

	protected Double salesTax = 0.0D;

	// @Override
	// protected abstract void createControls();

	protected CustomerCombo customerCombo;
	protected PaymentTermsCombo payTermsSelect;
	protected SalesPersonCombo salesPersonCombo;
	protected TAXCodeCombo taxCodeSelect;
	protected PriceLevelCombo priceLevelSelect;
	protected ItemCombo itemsSelect;
	protected ContactCombo contactCombo;
	protected ShippingTermsCombo shippingTermsCombo;
	protected AddressCombo billToCombo, shipToCombo;
	protected DepositInAccountCombo depositInCombo;
	protected ShippingMethodsCombo shippingMethodsCombo;
	protected DynamicForm custForm;

	protected SelectCombo statusSelect;

	protected TextItem phoneSelect;

	protected TextAreaItem billToTextArea;

	protected abstract void salesPersonSelected(ClientSalesPerson person);

	protected abstract void priceLevelSelected(ClientPriceLevel priceLevel);

	protected abstract void taxCodeSelected(ClientTAXCode taxCode);

	protected String[] phoneList;

	protected String phoneNo;

	protected String orderNum;

	protected String status;

	protected List<ClientShippingTerms> shippingTerms;

	protected ClientShippingTerms shippingTerm;

	protected Set<ClientAddress> addressListOfCustomer;

	protected List<ClientAccount> undepositedFunds;

	protected ClientAccount depositInAccount;

	private List<ClientShippingMethod> shippingMethods;

	protected ClientShippingMethod shippingMethod;

	protected ClientAddress billingAddress;

	protected ClientAddress shippingAddress;

	protected boolean saveAndClose;

	protected double amount;

	protected List<String> selectComboList;
	/**
	 * This field contains all changed values of customer in particular
	 * transaction
	 * 
	 */
	protected String[] customerChanges;

	private CashSalesView CashSalesView;

	protected ClientCustomer customer;

	@Override
	protected void initTransactionViewData() {

		initTransactionNumber();

		initCustomers();
		if (!(customerTransactionViewInstance instanceof ReceivePaymentView))
			initSalesPersons();

		initPriceLevels();

		initTaxItemGroups();

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

	}

	public AbstractCustomerTransactionView(int transactionType, int gridType) {
		super(transactionType, gridType);
	}

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
		if (gridType == CUSTOMER_TRANSACTION_GRID)
			return new CustomerTransactionGrid();

		return null;
	}

	public double getVATRate(long TAXCodeID) {
		double vatRate = 0.0;
		if (TAXCodeID != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			if (getCompany().getTAXItemGroup(
					getCompany().getTAXCode(TAXCodeID)
							.getTAXItemGrpForPurchases()) instanceof ClientTAXItem) {
				// The selected one is VATItem,so get 'VATRate' from 'VATItem'
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
		}
		return vatRate;
	}

	protected abstract void initMemoAndReference();

	protected abstract void initTransactionTotalNonEditableItem();

	protected abstract void initSalesTaxNonEditableItem();

	protected void initShippingTerms() {
		shippingTerms = getCompany().getShippingTerms();

		shippingTermsCombo.initCombo(shippingTerms);

	}

	protected void initDepositInAccounts() {
		// undepositedFunds = depositInCombo.getAccounts();
		//
		// depositInCombo.initCombo(undepositedFunds);
		depositInCombo.setAccounts();
		depositInAccount = depositInCombo.getSelectedValue();
		if (depositInAccount != null)
			depositInCombo.setComboItem(depositInAccount);

	}

	protected void initShippingMethod() {
		List<ClientShippingMethod> result = getCompany().getShippingMethods();
		if (shippingMethodsCombo != null) {
			shippingMethodsCombo.initCombo(result);

		}

	}

	protected void initPhones(ClientCustomer customer) {

		if (phoneSelect == null)
			return;

		Set<String> contactsPhoneList = customer.getContactsPhoneList();

		this.phoneList = contactsPhoneList.toArray(new String[contactsPhoneList
				.size()]);

		phoneSelect.setValueMap(phoneList);
		if (phoneList != null && phoneList.length > 0)
			this.phoneNo = phoneSelect.getValue().toString();
		else
			this.phoneNo = "";

		ClientContact primaContact = customer.getPrimaryContact();

		if (primaContact != null) {
			String primaryPhone = primaContact.getBusinessPhone();
			this.phoneNo = primaryPhone;
			phoneSelect.setValue(primaryPhone);
		}

		phoneSelect.setDisabled(isEdit);

	}

	protected void initContacts(ClientCustomer customer) {

		if (contactCombo == null)
			return;

		this.contacts = customer.getContacts();
		List<ClientContact> list = new ArrayList<ClientContact>(this.contacts);
		if (contacts != null) {
			contactCombo.initCombo(list);
			contactCombo.setDisabled(isEdit);
		} else {
			contactCombo.setDisabled(true);
			contactCombo.setValue("");
		}
		// if (transactionObject== null) {
		ClientContact clientContact = customer.getPrimaryContact();
		if (clientContact != null) {
			this.contact = clientContact;
			contactCombo.setComboItem(this.contact);
		} else {
			contactCombo.setValue("");
		}

		// }
		// if (transactionObject != null)
		// contactCombo.setComboItem(this.contact);
	}

	protected void contactSelected(ClientContact contact) {
		this.contact = contact;
		if (contact != null) {
			contactCombo.setComboItem(this.contact);
		}

	}

	protected void customerSelected(ClientCustomer customer) {

		this.customer = customer;
		if (customer == null)
			return;

		ClientCompany company = getCompany();

		salesPersonSelected(company.getSalesPerson(customer.getSalesPerson()));
		if (customer.getPaymentTerm() != 0)
			paymentTermsSelected(company.getPaymentTerms(customer
					.getPaymentTerm()));
		else if (this instanceof InvoiceView) {
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					payTermsSelect.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.paymentTerm = payTermsSelect.getSelectedValue();
			paymentTermsSelected(this.paymentTerm);
		}

		shippingTermSelected(company.getShippingTerms(customer
				.getShippingMethod()));

		shippingMethodSelected(company.getShippingMethod(customer
				.getShippingMethod()));
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			taxCodeSelected(company.getTAXCode(customer.getTAXCode()));

		priceLevelSelected(company.getPriceLevel(customer.getPriceLevel()));

		paymentMethodSelected(customer.getPaymentMethod());

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.paymentTerm != null && payTermsSelect != null)
			payTermsSelect.setComboItem(this.paymentTerm);

		if (this.shippingTerm != null && shippingTermsCombo != null)
			shippingTermsCombo.setComboItem(this.shippingTerm);

		if (this.shippingMethod != null && shippingMethodsCombo != null)
			shippingMethodsCombo.setComboItem(this.shippingMethod);

		if (this.taxCode != null && taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getName().equalsIgnoreCase("none"))
			taxCodeSelect.setComboItem(this.taxCode);

		if (this.priceLevel != null && priceLevelSelect != null)
			priceLevelSelect.setComboItem(this.priceLevel);

		if (this.paymentMethod != null && paymentMethodCombo != null)
			paymentMethodCombo.setComboItem(customer.getPaymentMethod());
		// if (transactionObject == null)
		initAddressAndContacts();
	}

	protected void setCustomerTaxCodetoAccount() {
		for (ClientTransactionItem item : customerTransactionGrid.getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT)
				customerTransactionGrid.setCustomerTaxCode(item);
		}
	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.constants().accounts(), Accounter
					.constants().service(), Accounter.constants().product());
		// FinanceApplication.constants().salesTax());
		else
			setMenuItems(button, Accounter.constants().accounts(), Accounter
					.constants().service(), Accounter.constants().product());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().VATItem());

	}

	@Override
	public void saveAndUpdateView() throws Exception {

		super.saveAndUpdateView();

		if (taxCode != null && transactionItems != null) {

			for (ClientTransactionItem item : transactionItems) {
				// if (taxCode instanceof ClientTAXItem)
				// item.setTaxItem((ClientTAXItem) taxCode);
				// if (taxCode instanceof ClientTAXGroup)
				// item.setTaxGroup((ClientTAXGroup) taxCode);
				item.setTaxCode(taxCode.getID());

			}
		}
	}

	protected void initAddressAndContacts() {

		initContacts(customer);
		initPhones(customer);
		addressListOfCustomer = customer.getAddress();
		initBillToCombo();
		initShipToCombo();
	}

	protected void initPaymentTerms() {

		paymentTermsList = getCompany().getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);

	}

	public void initTaxItemGroups() {
		taxCodes = getCompany().getTaxCodes();

		taxCodeSelect.initCombo(taxCodes);

	}

	public ClientTAXGroup getTaxGroup() {
		return taxGroup;
	}

	public void setTaxGroup(ClientTAXGroup taxGroup) {
		this.taxGroup = taxGroup;
	}

	protected void initPriceLevels() {

		priceLevels = getCompany().getPriceLevels();

		priceLevelSelect.initCombo(priceLevels);

	}

	protected void initSalesPersons() {

		salesPersons = getCompany().getActiveSalesPersons();

		salesPersonCombo.initCombo(salesPersons);

	}

	protected void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customers = result;

		customerCombo.initCombo(result);
		// customerCombo.setHelpInformation(true);
		customerCombo.setDisabled(isEdit);

	}

	public CustomerCombo createCustomerComboItem(String title) {

		CustomerCombo customerCombo = new CustomerCombo(title != null ? title
				: Accounter.constants().customer());
		customerCombo.setHelpInformation(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						customerSelected(selectItem);

					}

				});

		customerCombo.setRequired(true);
		customerCombo.setDisabled(isEdit);
		formItems.add(customerCombo);
		return customerCombo;

	}

	public ContactCombo createContactComboItem() {

		ContactCombo contactCombo = new ContactCombo(Accounter.constants()
				.contact());
		contactCombo.setDefaultToFirstOption(false);
		contactCombo.setHelpInformation(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.setDisabled(isEdit);

		formItems.add(contactCombo);

		return contactCombo;

	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(Accounter.constants()
				.billTo(), false);
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isEdit);

		formItems.add(addressCombo);

		return addressCombo;

	}

	protected void initShipToCombo() {

		if (shipToCombo == null || addressListOfCustomer == null)
			return;

		ClientAddress address = getAddress(ClientAddress.TYPE_SHIP_TO);
		Set<IAccounterCore> tempSet = new HashSet<IAccounterCore>();

		if (address != null) {

			tempSet.add(address);
		}
		List<ClientAddress> list = new ArrayList(tempSet);
		shipToCombo.initCombo(list);
		shipToAddressSelected(address);
		shipToCombo.setDisabled(isEdit);

	}

	public ClientAddress getAddress(int type) {
		for (ClientAddress address : addressListOfCustomer) {

			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfCustomer == null)
			return;

		ClientAddress address = getAddress(ClientAddress.TYPE_BILL_TO);
		Set<IAccounterCore> tempSet = new HashSet<IAccounterCore>();
		if (address != null) {

			tempSet.add(address);
		}
		List<ClientAddress> list = new ArrayList(tempSet);
		billToCombo.initCombo(list);
		billToaddressSelected(address);
		billToCombo.setDisabled(isEdit);

	}

	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToCombo != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	protected void billToaddressSelected(ClientAddress selectItem) {

		this.billingAddress = selectItem;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue("");

	}

	public AddressCombo createShipToComboItem() {

		AddressCombo shipToCombo = new AddressCombo(Accounter.constants()
				.shipTo());
		shipToCombo.setHelpInformation(true);
		shipToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						shipToAddressSelected(selectItem);

					}

				});

		shipToCombo.setDisabled(isEdit);
		// shipToCombo.setShowDisabled(false);

		formItems.add(shipToCombo);

		return shipToCombo;

	}

	public SalesPersonCombo createSalesPersonComboItem() {

		SalesPersonCombo salesPersonCombo = new SalesPersonCombo(Accounter
				.constants().salesPerson());
		salesPersonCombo.setHelpInformation(true);
		salesPersonCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {

					public void selectedComboBoxItem(
							ClientSalesPerson selectItem) {

						salesPersonSelected(selectItem);

					}

				});

		salesPersonCombo.setDisabled(isEdit);

		formItems.add(salesPersonCombo);

		return salesPersonCombo;

	}

	public DepositInAccountCombo createDepositInComboItem() {

		DepositInAccountCombo accountCombo = new DepositInAccountCombo(
				Accounter.constants().depositIn());
		accountCombo.setHelpInformation(true);
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {

						depositInAccountSelected(selectItem);

					}

				});
		accountCombo.setDisabled(isEdit);

		formItems.add(accountCombo);

		return accountCombo;

	}

	protected ShippingTermsCombo createShippingTermsCombo() {

		ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				Accounter.constants().shippingTerms());
		shippingTermsCombo.setHelpInformation(true);
		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {

						shippingTermSelected(selectItem);

					}

				});

		shippingTermsCombo.setDisabled(isEdit);

		formItems.add(shippingTermsCombo);

		return shippingTermsCombo;
	}

	protected ShippingMethodsCombo createShippingMethodCombo() {

		ShippingMethodsCombo shippingMethodsCombo = new ShippingMethodsCombo(
				Accounter.constants().shippingMethod());
		shippingMethodsCombo.setHelpInformation(true);
		shippingMethodsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						shippingMethodSelected(selectItem);
					}

				});

		shippingMethodsCombo.setDisabled(isEdit);

		formItems.add(shippingMethodsCombo);

		return shippingMethodsCombo;

	}

	protected DateField createTransactionDeliveryDateItem() {

		final DateField dateItem = new DateField(Accounter.constants()
				.deliveryDate());
		dateItem.setHelpInformation(true);
		dateItem.setTitle(Accounter.constants().deliveryDate());
		dateItem.setColSpan(1);

		dateItem.setDisabled(isEdit);

		formItems.add(dateItem);

		return dateItem;

	}

	protected TAXCodeCombo createTaxCodeSelectItem() {

		TAXCodeCombo taxCodeCombo = new TAXCodeCombo(Accounter.constants()
				.tax(), true);
		taxCodeCombo.setHelpInformation(true);
		taxCodeCombo.setRequired(true);

		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {

						taxCodeSelected(selectItem);

					}

				});

		taxCodeCombo.setDisabled(isEdit);

		formItems.add(taxCodeCombo);

		return taxCodeCombo;

	}

	protected PriceLevelCombo createPriceLevelSelectItem() {

		PriceLevelCombo priceLevelCombo = new PriceLevelCombo(Accounter
				.constants().priceLevel());
		priceLevelCombo.setHelpInformation(true);
		priceLevelCombo.setWidth(100);

		priceLevelCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPriceLevel>() {

					public void selectedComboBoxItem(ClientPriceLevel selectItem) {

						priceLevelSelected(selectItem);

					}

				});

		priceLevelCombo.setDisabled(isEdit);
		// priceLevelCombo.setShowDisabled(false);

		formItems.add(priceLevelCombo);

		return priceLevelCombo;

	}

	public void closeTab() {
		// MainFinanceWindow.removeFromTab(this);

	}

	public PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(Accounter
				.constants().paymentTerms());
		comboItem.setHelpInformation(true);
		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermsSelected(selectItem);

					}

				});
		comboItem.setDisabled(isEdit);
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	protected AmountField createSalesTaxNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter.constants()
				.salesTax());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createSalesTaxNonEditableLabel() {

		AmountLabel amountLabel = new AmountLabel(Accounter.constants()
				.salesTax());

		return amountLabel;

	}

	protected AmountField createTransactionTotalNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter.constants().total());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createTransactionTotalNonEditableLabel() {

		AmountLabel amountLabel = new AmountLabel(Accounter.constants().total());

		return amountLabel;

	}

	protected AmountField createVATTotalNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter.constants().vat());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableLabel() {
		AmountLabel amountLabel = new AmountLabel(Accounter.constants().vat());

		return amountLabel;
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Functinality to be Moved Up here Later...

	}

	@Override
	public void reload() {

		customer = null;
		super.reload();

	}

	@Override
	public boolean validate() {

		ValidationResult result = new ValidationResult();
			if(!AccounterValidator
					.validateTransactionDate(this.transactionDate)) {
				result.addError(transactionDateItem, AccounterErrorType.InvalidTransactionDate);
			}else if(AccounterValidator.isInPreventPostingBeforeDate(this.transactionDate)) {
				result.addError(transactionDateItem, AccounterErrorType.InvalidDate);
			}

		case 8:
			return AccounterValidator.validateForm(custForm, false);

		case 7:
			if (this.transactionType == ClientTransaction.TYPE_ESTIMATE) {
				QuoteView quoteView = (QuoteView) this;
				return AccounterValidator.validate_dueOrDelivaryDates(
						quoteView.quoteExpiryDate.getEnteredDate(),
						this.transactionDate,
						customerConstants.expirationDate());
			}
		case 6:
			if (this.transactionType == ClientTransaction.TYPE_INVOICE)
				return AccounterValidator.validate_dueOrDelivaryDates(
						((InvoiceView) this).dueDateItem.getDate(),
						getTransactionDate(), customerConstants.dueDate());
			return true;

		case 5:

			// This case is for all customer transactions except
			// CustomerRefunds.
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				if (!(this.transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS)) {
					if (!taxCodeSelect.validate()) {
						result.addError(taxCodeSelect, Accounter.messages()
								.pleaseEnter(taxCodeSelect.getTitle()));
					}
				}

			}
			// this is for CustomerRefunds only.
			else if (this instanceof CustomerRefundView) {
				CustomerRefundView view = (CustomerRefundView) this;
				if (!isEdit) {
					return (
					// AccounterValidator.validateForm(view.custForm)
					// &&
					AccounterValidator
							.validate_TaxAgency_FinanceAcount(view.selectedAccount)
							&& AccounterValidator.validateAmount(
									((CustomerRefundView) this).amtText
											.getAmount(), false) && AccounterValidator
								.validateCustomerRefundAmount(this,
										view.amtText.getAmount(),
										view.selectedAccount));
				}
			}
			return true;

			// This case is for CashSales only.
		case 4:

			if (this.transactionType == ClientTransaction.TYPE_CASH_SALES) {
				CashSalesView = (CashSalesView) this;
				return (AccounterValidator.validateFormItem(false,
						CashSalesView.paymentMethodCombo,
						CashSalesView.depositInCombo) && AccounterValidator
						.validate_TaxAgency_FinanceAcount(depositInAccount));
			}
			return true;
			// This is for invoice only.
		case 3:
			// ClientCompany company = FinanceApplication.getCompany();
			// if (company.getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_US)
			// if (this.transactionType == ClientTransaction.TYPE_INVOICE
			// || this.transactionType == ClientTransaction.TYPE_CASH_SALES
			// || this.transactionType ==
			// ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO)
			// return AccounterValidator.validateFormItem(taxCodeSelect);
			// if (!((this.transactionType == ClientTransaction.TYPE_INVOICE
			// ||
			// this.transactionType ==
			// ClientTransaction.TYPE_CUSTOMER_REFUNDS)
			// || (this.transactionType ==
			// ClientTransaction.TYPE_RECEIVE_PAYMENT ||
			// this.transactionType ==
			// ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) ||
			// this.transactionType == ClientTransaction.TYPE_SALES_ORDER))
			// {
			// return AccounterValidator.validate_dueOrDelivaryDates(
			// deliveryDate.getEnteredDate(), this.transactionDate,
			// customerConstants.deliveryDate());
			// }
			return true;
		case 2:
			if (this.transactionType == ClientTransaction.TYPE_RECEIVE_PAYMENT)
				return AccounterValidator.validateGrid(customerTransactionGrid);
			else if (!(this.transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS))
				return AccounterValidator
						.isBlankTransaction(customerTransactionGrid);
			return true;
		case 1:
			if (!(this.transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS))
				return customerTransactionGrid.validateGrid();
			return true;
		default:
			return true;
		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (salesTaxTextNonEditable != null)
			salesTaxTextNonEditable.setAmount(salesTax);

	}

	public Double getSalesTax() {
		return salesTax;
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		this.transactionTotal = transactionTotal;
		if (transactionTotalNonEditableText != null)
			transactionTotalNonEditableText.setAmount(transactionTotal);

	}

	public Double getTransactionTotal() {
		return transactionTotal;
	}

	@Override
	public void init(ViewManager manager) {

		super.init();
	}

	@Override
	// protected void onAddNew(String item) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (item.equals(FinanceApplication.constants().accounts())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// } else if (item.equals(FinanceApplication.constants()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (item.equals(FinanceApplication.constants()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (item.equals(FinanceApplication.constants()
	// .salesTax())
	// || item.equals(FinanceApplication.constants()
	// .VATItem())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// }
	// customerTransactionGrid.addData(transactionItem);
	//
	// }
	protected void onAddNew(String item) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (item.equals(Accounter.constants().accounts())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
			long ztaxCodeid = 0;
			if (getCompany().getPreferences().getDoYouPaySalesTax()) {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						ztaxCodeid = taxCode.getID();
						break;
					}
				}
			} else {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						ztaxCodeid = taxCode.getID();
					}
				}
			}
			transactionItem.setTaxCode(customer != null ? (customer
					.getTAXCode() != 0 ? customer.getTAXCode() : ztaxCodeid)
					: 0);
			// if (zvatCodeid != null)
			// transactionItem.setVatCode(zvatCodeid);
		} else if (item.equals(Accounter.constants().product())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			if (getCompany().getPreferences().getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
				long staxCodeid = 0;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						staxCodeid = taxCode.getID();
					}
				}
				transactionItem
						.setTaxCode(customer != null ? (customer.getTAXCode() != 0 ? customer
								.getTAXCode() : staxCodeid)
								: 0);
			}
		} else if (item.equals(Accounter.constants().service())) {
			transactionItem.setType(ClientTransactionItem.TYPE_SERVICE);
			List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
			long ztaxCodeid = 0;
			if (getCompany().getPreferences().getDoYouPaySalesTax()) {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						ztaxCodeid = taxCode.getID();
					}
				}
			} else {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						ztaxCodeid = taxCode.getID();
					}
				}
			}
			transactionItem.setTaxCode(customer != null ? (customer
					.getTAXCode() != 0 ? customer.getTAXCode() : ztaxCodeid)
					: 0);
			// if (zvatCodeid != null)
			// transactionItem.setVatCode(zvatCodeid);
		}
		customerTransactionGrid.addData(transactionItem);

	}

	protected void shippingMethodSelected(ClientShippingMethod selectItem) {
		this.shippingMethod = selectItem;
		if (shippingMethod != null && shippingMethodsCombo != null) {
			shippingMethodsCombo.setComboItem(getCompany().getShippingMethod(
					shippingMethod.getID()));
			shippingMethodsCombo.setDisabled(isEdit);
		}

	}

	protected void shippingTermSelected(ClientShippingTerms shippingTerm2) {
		this.shippingTerm = shippingTerm2;
		if (shippingTerm != null && shippingTermsCombo != null) {
			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
					shippingTerm.getID()));
			shippingTermsCombo.setDisabled(isEdit);
		}
	}

	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {

	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		this.depositInAccount = depositInAccount2;
		if (depositInAccount != null && depositInCombo != null) {

			depositInCombo.setComboItem(getCompany().getAccount(
					depositInAccount.getID()));
			depositInCombo.setDisabled(isEdit);
		}

	}

	@Override
	public void onEdit() {
		if (shippingMethodsCombo != null)
			shippingMethodsCombo.setDisabled(isEdit);
		if (shippingTermsCombo != null)
			shippingTermsCombo.setDisabled(isEdit);
		if (depositInCombo != null)
			depositInCombo.setDisabled(isEdit);
		if (phoneSelect != null)
			phoneSelect.setDisabled(isEdit);
		if (contactCombo != null)
			contactCombo.setDisabled(isEdit);
		if (shipToCombo != null)
			shipToCombo.setDisabled(isEdit);
		if (billToCombo != null)
			billToCombo.setDisabled(isEdit);
		super.onEdit();
	}

}
