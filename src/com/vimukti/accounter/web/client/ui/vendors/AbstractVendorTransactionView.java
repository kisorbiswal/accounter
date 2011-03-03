/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionUKGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionUSGrid;

/**
 * d
 * 
 * @author Fernandez modified by Ravi Kiran.G
 * 
 *         This Class serves as the Base Class For all Vendor Transactions
 */
public abstract class AbstractVendorTransactionView<T> extends
		AbstractTransactionBaseView<T> {

	protected AbstractVendorTransactionView<T> vendorTransactionViewInstance;

	protected VendorsMessages vendorConstants;
	protected String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

	protected ClientAccount payFromAccount;
	// protected PaymentMethod paymentMethod;

	protected List<ClientVendor> vendors;

	protected DateField deliveryDateItem;
	protected TextItem checkNo;
	protected SelectItem statusSelect;

	// protected TextAreaItem addrTextAreaItem;

	protected Set<ClientContact> contacts;
	protected ClientContact contact;
	protected VendorCombo vendorCombo;
	protected ContactCombo contactCombo;
	protected AddressCombo billToCombo;
	protected PayFromAccountsCombo payFromCombo;
	protected AmountLabel netAmount, transactionTotalNonEditableText,
			vatTotalNonEditableText;

	protected AmountField balanceDueNonEditableText;// protected
	// AbstractAccounterCombo
	// contactCombo,
	// billToCombo,

	// payFromCombo;

	protected ComboBoxItem phoneSelect;
	protected String[] phoneList;
	protected String phoneNo;

	protected Set<ClientAddress> addressListOfVendor;
	protected List<ClientAccount> payFromAccounts;
	protected ClientAddress billingAddress;
	protected List<ClientPaymentTerms> paymentTermsList;

	protected void initTransactionTotalNonEditableItem() {
	}

	/**
	 * provide a Valid Vendor Transaction Type
	 */

	@Override
	protected abstract void initTransactionViewData(
			ClientTransaction transactionObject);

	public AbstractVendorTransactionView(int transactionType,
			int transactionViewType) {
		super(transactionType, transactionViewType);
		vendorTransactionViewInstance = this;

	}

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
		if (gridType == VENDOR_TRANSACTION_GRID)
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				return new VendorTransactionUSGrid();
			else
				return new VendorTransactionUKGrid();

		return null;
	}

	@Override
	protected void initTransactionViewData() {

		initVendors();
		initTransactionTotalNonEditableItem();

	}

	public static void a() {

	}

	@Override
	protected abstract void createControls();

	protected abstract void initMemoAndReference();

	@Override
	protected final void initConstants() {
		vendorConstants = GWT.create(VendorsMessages.class);
	}

	@Override
	protected void showMenu() {
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(FinanceApplication.getVendorsMessages()
					.nominalCodeItem(), FinanceApplication.getVendorsMessages()
					.service(), FinanceApplication.getVendorsMessages()
					.product());
		else
			setMenuItems(FinanceApplication.getVendorsMessages()
					.nominalCodeItem(), FinanceApplication.getVendorsMessages()
					.service(), FinanceApplication.getVendorsMessages()
					.product());
		// FinanceApplication.getVendorsMessages().comment());

	}

	protected void initVendors() {

		if (vendorCombo == null)
			return;
		List<ClientVendor> result = FinanceApplication.getCompany()
				.getActiveVendors();
		vendors = result;

		vendorCombo.initCombo(result);
		vendorCombo.setDisabled(isEdit);

		if (vendor != null)
			vendorCombo.setComboItem(vendor);
	}

	private void getPayFromAccounts() {
		payFromAccounts = payFromCombo.getAccounts();
		payFromCombo.initCombo(payFromAccounts);
	}

	protected void vendorSelected(ClientVendor vendor) {

		if (vendor == null)
			return;
		this.vendor = vendor;
		initContacts(vendor);
		initPhones(vendor);
		paymentMethodSelected(vendor.getPaymentMethod());
		addressListOfVendor = vendor.getAddress();
		initBillToCombo();

	}

	protected void setVendorTaxcodeToAccount() {
		for (ClientTransactionItem item : vendorTransactionGrid.getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT)
				vendorTransactionGrid.setVendorTaxCode(item);
		}
	}

	protected AmountLabel createTransactionTotalNonEditableItem() {

		AmountLabel amountItem = new AmountLabel(FinanceApplication
				.getVendorsMessages().total());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableItem() {

		AmountLabel amountItem = new AmountLabel(FinanceApplication
				.getCustomersMessages().vat());
		amountItem.setDisabled(true);

		return amountItem;

	}

	public void initPhones(ClientVendor vendor) {
		if (phoneSelect == null)
			return;

		phoneSelect.setValueMap("");
		phoneSelect.setValue("");

		Set<String> contactsPhoneList = vendor.getContactsPhoneList();

		this.phoneList = contactsPhoneList.toArray(new String[contactsPhoneList
				.size()]);

		phoneSelect.setValueMap(phoneList);
		phoneSelect.setDisabled(isEdit);
		// phoneSelect.setShowDisabled(false);

		ClientContact primaryContact = vendor.getPrimaryContact();

		if (primaryContact != null) {
			String primaryPhone = primaryContact.getBusinessPhone();
			phoneSelect.setValue(primaryPhone);
		}

	}

	public void initContacts(ClientVendor vendor) {
		if (contactCombo == null)
			return;
		this.contacts = vendor.getContacts();

		// if (transactionObject == null)
		this.contact = vendor.getPrimaryContact();

		if (contacts != null) {
			List<ClientContact> contactList = new ArrayList<ClientContact>();
			contactList.addAll(contacts);
			contactCombo.initCombo(contactList);
			contactCombo.setDisabled(isEdit);

			if (contact != null && contacts.contains(contact)) {
				contactCombo.setComboItem(contact);
				contactSelected(contact);

			} else {
				contactCombo.setValue("");
			}

		} else {
			contactCombo.setValue("");
			contactCombo.setDisabled(true);
		}
	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setDisabled(isEdit);
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
		}
		contactCombo.setDisabled(isEdit);

	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: UIUtils.getVendorString(FinanceApplication
						.getVendorsMessages().supplier(), FinanceApplication
						.getVendorsMessages().vendor()));
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setDisabled(isEdit);
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public VendorCombo createVendorComboItem(String title, boolean isRequired) {

		VendorCombo vendorCombo = new VendorCombo(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplier(),
				FinanceApplication.getVendorsMessages().vendor()));
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(isRequired);
		vendorCombo.setDisabled(isEdit);
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public ContactCombo createContactComboItem() {

		ContactCombo contactCombo = new ContactCombo(FinanceApplication
				.getVendorsMessages().contactName());
		contactCombo.setHelpInformation(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.setDisabled(isEdit);
		// contactCombo.setShowDisabled(false);
		return contactCombo;

	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(FinanceApplication
				.getVendorsMessages().billTo());
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isEdit);
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
		payFromCombo.setDisabled(isEdit);
		// payFromCombo.setShowDisabled(false);
		formItems.add(payFromCombo);
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

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		billToCombo.setDisabled(isEdit);
		// billToCombo.setShowDisabled(false);

		if (isEdit && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setComboItem(null);
			// billToaddressSelected(clientAddress);
		}
	}

	protected void billToaddressSelected(ClientAddress selectedAddress) {
		this.billingAddress = selectedAddress;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue(null);
	}

	protected TextItem createCheckNumberItem(String title) {

		final TextItem checkNo = new TextItem(title);
		checkNo.setHelpInformation(true);
		checkNo.setDisabled(isEdit);
		// checkNo.setShowDisabled(false);
		if (transactionObject != null) {
			if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				ClientCashPurchase clientCashPurchase = (ClientCashPurchase) transactionObject;
				checkNo.setValue(clientCashPurchase.getCheckNumber());
			}
		}
		return checkNo;

	}

	protected String getCheckNoValue() {
		return checkNumber;
	}

	protected DateField createTransactionDeliveryDateItem() {

		final DateField dateItem = new DateField(FinanceApplication
				.getVendorsMessages().deliverydate());
		dateItem.setHelpInformation(true);
		// dateItem.setTitle("Delivery Date");
		// dateItem.setUseTextField(true);
		if (transactionObject == null) {
			dateItem.setEnteredDate(getTransactionDate());
		}

		if (transactionObject != null) {
			long date;
			if (transactionType == ClientTransaction.TYPE_ENTER_BILL) {
				ClientEnterBill enterBill = (ClientEnterBill) transactionObject;
				if ((date = enterBill.getDeliveryDate()) != 0)
					dateItem.setEnteredDate(new ClientFinanceDate(date));
			}

			else if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {

				ClientCashPurchase cashPurchase = (ClientCashPurchase) transactionObject;
				if ((date = cashPurchase.getDeliveryDate()) != 0)
					dateItem.setEnteredDate(new ClientFinanceDate(date));

			}
		}

		dateItem.setDisabled(isEdit);
		// dateItem.setShowDisabled(false);
		// dateItem.setEnforceDate(true);

		return dateItem;
	}

	@Override
	// protected void onAddNew(String menuItem) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (menuItem.equals(FinanceApplication.getVendorsMessages().accounts()))
	// {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK) {
	// transactionItem.setVatCode(vendor != null ? (vendor
	// .getVATCode() != null ? vendor.getVATCode() : "") : "");
	// }
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .VATItem()))
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// vendorTransactionGrid.addData(transactionItem);
	//
	// }
	protected void onAddNew(String menuItem) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (menuItem.equals(FinanceApplication.getVendorsMessages()
				.nominalCodeItem())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& !FinanceApplication.getCompany().getpreferences()
							.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = FinanceApplication.getCompany()
						.getActiveTaxCodes();
				String ztaxCodeStringId = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						ztaxCodeStringId = taxCode.getStringID();
					}
				}
				if (ztaxCodeStringId != null)
					transactionItem.setTaxCode(ztaxCodeStringId);
				// transactionItem.setVatCode(vendor != null ? (vendor
				// .getVATCode() != null ? vendor.getVATCode() : "") : "");
			}
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& FinanceApplication.getCompany().getpreferences()
							.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = FinanceApplication.getCompany()
						.getActiveTaxCodes();
				String staxCodeStringId = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						staxCodeStringId = taxCode.getStringID();
					}
				}
				// if (zvatCodeStringId != null)
				// transactionItem.setVatCode(zvatCodeStringId);
				transactionItem.setTaxCode(vendor != null ? (vendor
						.getTAXCode() != null ? vendor.getTAXCode()
						: staxCodeStringId) : "");
			}
		} else if (menuItem.equals(FinanceApplication.getVendorsMessages()
				.product())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			if (FinanceApplication.getCompany().getpreferences()
					.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = FinanceApplication.getCompany()
						.getActiveTaxCodes();
				String staxCodeStringId = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						staxCodeStringId = taxCode.getStringID();
					}
				}
				transactionItem.setTaxCode(vendor != null ? (vendor
						.getTAXCode() != null ? vendor.getTAXCode()
						: staxCodeStringId) : "");
			}
		} else if (menuItem.equals(FinanceApplication.getVendorsMessages()
				.service())) {
			transactionItem.setType(ClientTransactionItem.TYPE_SERVICE);
			List<ClientTAXCode> taxCodes = FinanceApplication.getCompany()
					.getActiveTaxCodes();
			String ztaxCodeStringId = null;
			if (FinanceApplication.getCompany().getpreferences()
					.getDoYouPaySalesTax()) {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						ztaxCodeStringId = taxCode.getStringID();
					}
				}
				transactionItem.setTaxCode(vendor != null ? (vendor
						.getTAXCode() != null ? vendor.getTAXCode()
						: ztaxCodeStringId) : "");
			} else {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						ztaxCodeStringId = taxCode.getStringID();
					}
				}
				if (ztaxCodeStringId != null)
					transactionItem.setTaxCode(ztaxCodeStringId);
			}
		}
		vendorTransactionGrid.addData(transactionItem);

	}

	protected ClientFinanceDate getTransactionDeliveryDate() {
		return this.deliveryDateItem.getValue();
	}

	@Override
	public void onEdit() {

		if (phoneSelect != null)
			phoneSelect.setDisabled(isEdit);
		if (contactCombo != null)
			contactCombo.setDisabled(isEdit);
		if (payFromCombo != null)
			payFromCombo.setDisabled(isEdit);
		if (billToCombo != null)
			billToCombo.setDisabled(isEdit);
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

}
