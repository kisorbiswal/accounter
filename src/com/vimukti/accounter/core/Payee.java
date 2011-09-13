package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;

/**
 * Payee is the object which represents a real-time entity of either
 * {@link Customer}, {@link Vendor}, {@link SalesPerson} or {@link TAXAgency}
 * 
 * @author Chandan
 * 
 */
public abstract class Payee extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3882317049281524783L;

	public static final int TYPE_NONE = 0;

	public static final int TYPE_CUSTOMER = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_TAX_AGENCY = 4;

	public static final int TYPE_EMPLOYEE = 3;

	FinanceDate payeeSince;
	int type;

	FinanceDate date;

	double balance;

	String name;
	String fileAs;

	Set<Address> address = new HashSet<Address>();
	Set<Phone> phoneNumbers = new HashSet<Phone>();
	Set<Fax> faxNumbers = new HashSet<Fax>();
	Set<Contact> contacts = new HashSet<Contact>();
	String webPageAddress;
	boolean isActive = Boolean.TRUE;
	String memo;
	String paymentMethod;
	boolean isOpeningBalanceEditable = Boolean.TRUE;
	private String phoneNo;
	private String faxNo;

	// UKvariables
	// boolean isEUVATExemptPayee;
	String VATRegistrationNumber;
	TAXCode TAXCode;
	TAXItem TAXItem;

	public TAXItem getTAXItem() {
		return TAXItem;
	}

	public void setTAXItem(TAXItem tAXItem) {
		TAXItem = tAXItem;
	}

	private String email;
	boolean isDefault;

	String bankAccountNo;
	String bankName;
	String bankBranch;
	String panNumber;
	String cstNumber;
	String serviceTaxRegistrationNumber;
	String tinNumber;
	String currency;

	public transient boolean isOnSaveProccessed;

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileAs() {
		return fileAs;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the vATRegistrationNumber
	 */
	public String getVATRegistrationNumber() {
		return VATRegistrationNumber;
	}

	/**
	 * @param registrationNumber
	 *            the vATRegistrationNumber to set
	 */
	public void setVATRegistrationNumber(String registrationNumber) {
		VATRegistrationNumber = registrationNumber;
	}

	/**
	 * @return the vATCode
	 */
	public TAXCode getTAXCode() {
		return TAXCode;
	}

	/**
	 * @param code
	 *            the vATCode to set
	 */
	public void setTAXCode(TAXCode code) {
		TAXCode = code;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	protected double openingBalance = 0D;

	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public FinanceDate getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(FinanceDate date) {
		this.date = date;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public Set<Phone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Set<Fax> getFaxNumbers() {
		return faxNumbers;
	}

	public void setFaxNumbers(Set<Fax> faxNumbers) {
		this.faxNumbers = faxNumbers;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isCustomer() {
		return this != null && this instanceof Customer;
	}

	public boolean isVendor() {
		return this != null && this instanceof Vendor;
	}

	public boolean isTaxAgency() {
		return this != null && this instanceof TAXAgency;
	}

	public FinanceDate getPayeeSince() {
		return payeeSince;
	}

	public void setPayeeSince(FinanceDate payeeSince) {
		this.payeeSince = payeeSince;
	}

	public void updateBalance(Session session, Transaction transaction,
			double amount) {

		updateBalance(session, transaction, amount, null);
	}

	/**
	 * 
	 * @param session
	 * @param transaction
	 * @param amount
	 * @param object
	 * 
	 *            method for reverse back effect on the Account related to this
	 *            Payee
	 */

	public void updateBalance(Session session, Transaction transaction,
			double amount, TAXRateCalculation object) {

		/**
		 * To check whether this payee is Customer, Vendor or Tax Agency.
		 * Depending on the type we will either decrease or increase the balance
		 * of the corresponding Payee.
		 */
		String tempStr = this.getName() + " Balance has been updated from "
				+ this.balance;

		if (this.type == TYPE_CUSTOMER) {
			this.balance -= amount;
		} else if (this.type == TYPE_VENDOR || this.type == TYPE_TAX_AGENCY) {
			this.balance += amount;
		}

		FinanceLogger.log("{0} to {1} ", tempStr, String.valueOf(this.balance));

		/**
		 * Getting the Account related to this Payee.
		 */
		Account account = (object == null) ? this.getAccount() : object
				.getSalesLiabilityAccount();

		// /**
		// * In case of ItemReceipt instead of Updating Accounts Payable balance
		// * we should update Pending Item Receipt Account Balance.
		// */
		// if (transaction instanceof ItemReceipt) {
		// List<Account> accounts = (List<Account>) session
		// .createQuery(
		// "from com.vimukti.accounter.core.Account a where a.name = ?")
		// .setParameter(0, AccounterConstants.PENDING_ITEM_RECEIPTS)
		// .list();
		// if (accounts != null && !accounts.isEmpty()
		// && accounts.get(0) != null) {
		// account = accounts.get(0);
		// }
		// }

		/**
		 * Update the account balance by the given amount if the account is not
		 * null
		 */
		if (account != null) {
			account.updateCurrentBalance(transaction, amount);
			session.update(account);
			account.onUpdate(session);
		}

		/**
		 * once if any Payee got created with opening balance then that Payee
		 * balance should not be editable. So we will make it as un editable.
		 */
		isOpeningBalanceEditable = Boolean.FALSE;
		ChangeTracker.put(this);
	}

	public abstract Account getAccount();

	// /*
	// * Is to update Memo in Entry if and only if payee Name was altered
	// */
	// protected void updateEntryMemo(Session session) {
	//
	// Query query = session.getNamedQuery("getPayeename.from.PayeebyId")
	// .setParameter("id", this.getID());
	// String payeeName = (String) query.uniqueResult();
	//
	// if (payeeName != null && !this.getName().equals(payeeName))
	// Entry.updateEntryMemo(session, payeeName, this.getName());
	// }

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getPANno() {
		return panNumber;
	}

	public void setPANno(String pANno) {
		panNumber = pANno;
	}

	public String getCSTno() {
		return cstNumber;
	}

	public void setCSTno(String cSTno) {
		cstNumber = cSTno;
	}

	public String getServiceTaxRegistrationNo() {
		return serviceTaxRegistrationNumber;
	}

	public void setServiceTaxRegistrationNo(String serviceTaxRegistrationNo) {
		this.serviceTaxRegistrationNumber = serviceTaxRegistrationNo;
	}

	public String getTINNumber() {
		return tinNumber;
	}

	public void setTINNumber(String tINNumber) {
		tinNumber = tINNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
