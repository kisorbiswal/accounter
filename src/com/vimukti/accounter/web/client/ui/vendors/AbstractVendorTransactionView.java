/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * d
 * 
 * @author Fernandez modified by Ravi Kiran.G
 * 
 *         This Class serves as the Base Class For all Vendor Transactions
 */
public abstract class AbstractVendorTransactionView<T extends ClientTransaction>
		extends AbstractTransactionBaseView<T> {

	protected AbstractVendorTransactionView<T> vendorTransactionViewInstance;

	protected String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

	protected ClientAccount payFromAccount;
	// protected PaymentMethod paymentMethod;

	protected List<ClientVendor> vendors;

	protected DateField deliveryDateItem;
	protected TextItem checkNo;
	protected SelectCombo statusSelect;

	// protected TextAreaItem addrTextAreaItem;
	protected ClientTAXCode taxCode;
	protected TAXCodeCombo taxCodeSelect;
	protected Set<ClientContact> contacts;
	protected ClientContact contact;
	protected VendorCombo vendorCombo;
	protected ContactCombo contactCombo;
	protected AddressCombo billToCombo;
	protected PayFromAccountsCombo payFromCombo;
	protected AmountLabel netAmount, transactionTotalNonEditableText,
			vatTotalNonEditableText, paymentsNonEditableText,
			salesTaxTextNonEditable;

	protected AmountField balanceDueNonEditableText;// protected

	// AbstractAccounterCombo
	// contactCombo,
	// billToCombo,

	// payFromCombo;
	protected abstract void taxCodeSelected(ClientTAXCode taxCode);

	protected TextItem phoneSelect;
	protected String[] phoneList;
	protected String phoneNo;

	protected List<String> phoneSelectItemList;
	protected Set<ClientAddress> addressListOfVendor;
	protected List<ClientAccount> payFromAccounts;
	protected ClientAddress billingAddress;
	protected List<ClientPaymentTerms> paymentTermsList;

	protected ClientVendor vendor;

	protected void initTransactionTotalNonEditableItem() {
	}

	public AbstractVendorTransactionView(int transactionType) {
		super(transactionType);
		vendorTransactionViewInstance = this;

	}

	@Override
	protected void initTransactionViewData() {
		initVendors();
		initTransactionTotalNonEditableItem();
		if (transaction.getTransactionItems() != null
				&& !transaction.getTransactionItems().isEmpty()) {
			removeAllRecordsFromGrid();
			addAllRecordToGrid(transaction.getTransactionItems());
		}

	}

	protected abstract void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems);

	protected abstract void removeAllRecordsFromGrid();

	@Override
	protected abstract void createControls();

	protected abstract void initMemoAndReference();

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button,
				Accounter.messages().accounts(Global.get().Account()),
				Accounter.constants().productOrServiceItem());
	}

	protected void initVendors() {

		if (vendorCombo == null)
			return;
		List<ClientVendor> result = getCompany().getActiveVendors();
		vendors = result;

		vendorCombo.initCombo(result);
		vendorCombo.setDisabled(isInViewMode());

		if (getVendor() != null)
			vendorCombo.setComboItem(getVendor());
	}

	private void getPayFromAccounts() {
		payFromAccounts = payFromCombo.getAccounts();
		payFromCombo.initCombo(payFromAccounts);
	}

	protected void vendorSelected(ClientVendor vendor) {

		if (vendor == null)
			return;
		this.setVendor(vendor);
		initContacts(vendor);

		Iterator<ClientContact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			contactCombo.setValue(iterator.next().getName());
			break;
		}

		// initPhones(vendor);
		paymentMethodSelected(vendor.getPaymentMethod());
		addressListOfVendor = vendor.getAddress();
		initBillToCombo();
		if (taxCodeSelect != null) {
			long taxCodeID = vendor.getTAXCode();
			taxCodeSelect.setSelectedObj(taxCodeID);
		}

	}

	protected AmountLabel createTransactionTotalNonEditableItem() {

		AmountLabel amountItem = new AmountLabel(Accounter.constants().total());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountField createSalesTaxNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter.constants()
				.salesTax(), this);
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableItem() {

		AmountLabel amountItem = new AmountLabel(Accounter.constants().tax());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createSalesTaxNonEditableLabel() {

		AmountLabel amountLabel = new AmountLabel(Accounter.constants()
				.salesTax());

		return amountLabel;
	}

	public void initPhones(ClientVendor vendor) {
		if (phoneSelect == null)
			return;
		// phoneSelect.setValue("");
		Set<String> contactsPhoneList = vendor.getContactsPhoneList();

		this.phoneList = contactsPhoneList.toArray(new String[contactsPhoneList
				.size()]);

		phoneSelect.setDisabled(isInViewMode());
		ClientContact primaryContact = vendor.getPrimaryContact();

		if (primaryContact != null) {
			String primaryPhone = primaryContact.getBusinessPhone();
			phoneSelect.setValue(primaryPhone);
			this.phoneNo = primaryPhone;
		}
	}

	public void initContacts(ClientVendor vendor) {

		if (contactCombo == null)
			return;
		this.contacts = vendor.getContacts();

		this.contact = vendor.getPrimaryContact();

		if (contacts != null && contacts.size() > 0) {
			List<ClientContact> contactList = new ArrayList<ClientContact>();
			contactList.addAll(contacts);
			contactCombo.initCombo(contactList);
			contactCombo.setDisabled(isInViewMode());

			if (contact != null && contacts.contains(contact)) {
				contactCombo.setComboItem(contact);
				contactSelected(contact);
			}

		} else {
			contactCombo.initCombo(null);
			contactCombo.setDisabled(isInViewMode());
			contactCombo.setValue("");
			// contactCombo.setDisabled(true);

		}
	}

	protected TAXCodeCombo createTaxCodeSelectItem() {

		TAXCodeCombo taxCodeCombo = new TAXCodeCombo(Accounter.constants()
				.tax(), true);
		taxCodeCombo.setHelpInformation(true);
		taxCodeCombo.setRequired(true);
		taxCodeCombo.addStyleName("tax_combo");

		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {

						taxCodeSelected(selectItem);

					}

				});

		taxCodeCombo.setDisabled(isInViewMode());

		// formItems.add(taxCodeCombo);

		return taxCodeCombo;

	}

	// private void taxCodeSelected(ClientTAXCode selectItem) {
	// // TODO Auto-generated method stub
	//
	// }

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setDisabled(isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}

	protected void contactSelected(ClientContact contact) {
		if (contact == null)
			return;
		this.contact = contact;
		this.phoneNo = contact.getBusinessPhone();
		if (this.phoneNo != null) {
			phoneSelect.setValue(this.phoneNo);
			contactCombo.setValue(contact.getName());
		}

		// contactCombo.setDisabled(isEdit);

	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setDisabled(isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);
						if (contactCombo != null)
							contactCombo.setDisabled(false);
					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public ContactCombo createContactComboItem() {

		ContactCombo contactCombo = new ContactCombo(Accounter.constants()
				.contactName(), true);
		contactCombo.setHelpInformation(true);
		contactCombo.setDisabled(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToVendor(value);
				initContacts(vendorCombo.getSelectedValue());

			}
		});
		// contactCombo.setShowDisabled(false);
		return contactCombo;

	}

	protected void addContactToVendor(final ClientContact value) {
		final ClientVendor selectedVendor = vendorCombo.getSelectedValue();
		if (selectedVendor == null) {
			return;
		}

		selectedVendor.addContact(value);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				selectedVendor.setVersion(selectedVendor.getVersion() + 1);
				contactSelected(value);
			}

		};
		Accounter.createCRUDService().update(selectedVendor, asyncallBack);
	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(Accounter.constants()
				.billTo(), false);
		addressCombo.setDefaultToFirstOption(false);
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isInViewMode());
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		payFromCombo.setHelpInformation(true);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFromCombo.setDisabled(isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;
		this.paymentMethod = paymentMethod;
		// selectedPaymentMethod(paymentMethod);

	}

	protected void accountSelected(ClientAccount account) {
		if (account == null)
			return;
		this.payFromAccount = account;
	}

	public ClientAddress getAddress(int type) {
		if (addressListOfVendor != null) {
			for (ClientAddress address : addressListOfVendor) {

				if (address.getType() == type) {
					return address;
				}

			}
		}
		return null;
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

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {
				if (address != null) {
					tempSet.add(address);
					clientAddress = address;
					break;
				}
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		billToCombo.setDisabled(isInViewMode());
		billToCombo.setDefaultToFirstOption(false);

		if (isInViewMode() && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setValue(null);
			// billToaddressSelected(clientAddress);
		}
	}

	protected void billToaddressSelected(ClientAddress selectItem) {

		this.billingAddress = selectItem;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue("");

	}

	protected TextItem createCheckNumberItem(String title) {

		final TextItem checkNo = new TextItem(title);
		checkNo.setToolTip(Accounter.messages()
				.giveNoTo(this.getAction().getViewName())
				.replace(Accounter.constants().no(), title));
		checkNo.setHelpInformation(true);
		checkNo.setDisabled(isInViewMode());
		// checkNo.setShowDisabled(false);
		if (transaction != null) {
			if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				ClientCashPurchase clientCashPurchase = (ClientCashPurchase) transaction;
				checkNo.setValue(clientCashPurchase.getCheckNumber());
			}
		}
		return checkNo;

	}

	protected String getCheckNoValue() {
		return checkNumber;
	}

	protected DateField createTransactionDeliveryDateItem() {

		final DateField dateItem = new DateField(Accounter.constants()
				.deliveryDate());
		dateItem.setHelpInformation(true);
		// dateItem.setTitle("Delivery Date");
		// dateItem.setUseTextField(true);
		if (transaction == null) {
			dateItem.setEnteredDate(getTransactionDate());
		}

		if (transaction != null) {
			long date;
			if (transactionType == ClientTransaction.TYPE_ENTER_BILL) {
				ClientEnterBill enterBill = (ClientEnterBill) transaction;
				if ((date = enterBill.getDeliveryDate()) != 0)
					dateItem.setEnteredDate(new ClientFinanceDate(date));
			}

			else if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {

				ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
				if ((date = cashPurchase.getDeliveryDate()) != 0)
					dateItem.setEnteredDate(new ClientFinanceDate(date));

			}
		}

		dateItem.setDisabled(isInViewMode());
		// dateItem.setShowDisabled(false);
		// dateItem.setEnforceDate(true);

		return dateItem;
	}

	@Override
	// protected void onAddNew(String menuItem) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (menuItem.equals(FinanceApplication.constants().accounts()))
	// {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK) {
	// transactionItem.setVatCode(vendor != null ? (vendor
	// .getVATCode() != null ? vendor.getVATCode() : "") : "");
	// }
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .VATItem()))
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// vendorTransactionGrid.addData(transactionItem);
	//
	// }
	protected void onAddNew(String menuItem) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		long defaultTaxCode = getCompany().getPreferences().getDefaultTaxCode();
		if (menuItem.equals(Accounter.messages().accounts(
				Global.get().Account()))) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);

			transactionItem.setTaxCode(getVendor() != null ? (getVendor()
					.getTAXCode() > 0 ? getVendor().getTAXCode()
					: defaultTaxCode) : defaultTaxCode);
		} else if (menuItem
				.equals(Accounter.constants().productOrServiceItem())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			transactionItem.setTaxCode(getVendor() != null ? (getVendor()
					.getTAXCode() > 0 ? getVendor().getTAXCode()
					: defaultTaxCode) : defaultTaxCode);
		}
		addNewData(transactionItem);

	}

	protected abstract void addNewData(ClientTransactionItem transactionItem);

	protected ClientFinanceDate getTransactionDeliveryDate() {
		return this.deliveryDateItem.getValue();
	}

	@Override
	public void onEdit() {

		if (phoneSelect != null)
			phoneSelect.setDisabled(isInViewMode());
		if (contactCombo != null)
			contactCombo.setDisabled(isInViewMode());
		if (payFromCombo != null)
			payFromCombo.setDisabled(isInViewMode());
		if (billToCombo != null)
			billToCombo.setDisabled(isInViewMode());
		super.onEdit();

	}

	// @Override
	// public void saveAndUpdateView() throws Exception {
	//
	// // if (getTransactionTotal() <= 0) {
	// // throw new
	// InvalidOperationException("Transaction total cannot be 0 or less than 0");
	// // }
	// super.saveAndUpdateView();
	// }

	protected abstract Double getTransactionTotal();

	public ClientVendor getVendor() {
		return vendor;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setTaxCode(taxCode.getID());
			}
		}
	}

	public abstract List<ClientTransactionItem> getAllTransactionItems();

	@Override
	protected void addAccount() {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
		long defaultTaxCode = getCompany().getPreferences().getDefaultTaxCode();
		transactionItem.setTaxCode(getVendor() != null ? (getVendor()
				.getTAXCode() > 0 ? getVendor().getTAXCode() : defaultTaxCode)
				: defaultTaxCode);
		addAccountTransactionItem(transactionItem);
	}

	@Override
	protected void addItem() {
		ClientTransactionItem transactionItem = new ClientTransactionItem();

		transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		long defaultTaxCode = getPreferences().getDefaultTaxCode();
		transactionItem.setTaxCode(getVendor() != null ? (getVendor()
				.getTAXCode() > 0 ? getVendor().getTAXCode() : defaultTaxCode)
				: defaultTaxCode);
		addItemTransactionItem(transactionItem);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		if (vendorCombo.getSelectedValue() == null) {
			vendorCombo.setValue("");
		}
		return result;
	}

	protected abstract void addAccountTransactionItem(ClientTransactionItem item);

	protected abstract void addItemTransactionItem(ClientTransactionItem item);
}
