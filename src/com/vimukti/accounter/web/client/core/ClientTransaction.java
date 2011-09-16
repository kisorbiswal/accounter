package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.UIUtils;

public abstract class ClientTransaction implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MEMO_OPENING_BALANCE = 0;
	public static final int TYPE_CASH_SALES = 1;
	public static final int TYPE_CASH_PURCHASE = 2;
	public static final int TYPE_CREDIT_CARD_CHARGE = 3;
	public static final int TYPE_CUSTOMER_CREDIT_MEMO = 4;
	public static final int TYPE_CUSTOMER_REFUNDS = 5;
	public static final int TYPE_ENTER_BILL = 6;
	public static final int TYPE_ESTIMATE = 7;

	public static final int TYPE_INVOICE = 8;
	public static final int TYPE_ISSUE_PAYMENT = 9;
	public static final int TYPE_MAKE_DEPOSIT = 10;
	public static final int TYPE_PAY_BILL = 11;
	public static final int TYPE_RECEIVE_PAYMENT = 12;
	public static final int TYPE_TRANSFER_FUND = 13;
	public static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	public static final int TYPE_WRITE_CHECK = 15;
	public static final int TYPE_JOURNAL_ENTRY = 16;
	public static final int TYPE_PAY_SALES_TAX = 17;
	public static final int TYPE_EXPENSE = 18;
	public static final int TYPE_PAY_EXPENSE = 19;
	public static final int TYPE_VAT_RETURN = 20;
	public static final int TYPE_MISC_FORM = 21;

	public static final int TYPE_SALES_ORDER = 21;
	public static final int TYPE_PURCHASE_ORDER = 22;
	public static final int TYPE_ITEM_RECEIPT = 23;

	public static final int TYPE_ADJUST_VAT_RETURN = 24;
	public static final int TYPE_PAY_VAT = 30;

	public static final int TYPE_CASH_EXPENSE = 26;
	public static final int TYPE_CREDIT_CARD_EXPENSE = 27;
	public static final int TYPE_EMPLOYEE_EXPENSE = 28;
	public static final int TYPE_CUSTOMER_PREPAYMENT = 29;
	public static final int TYPE_ADJUST_SALES_TAX = 32;
	/*
	 * There is no seperate transaction for VendorPayment.VendorPayment saved as
	 * a Paybill.So,to open the VendorPaymentView in edit mode we use this
	 * transaction.This constant not exited @ server side.
	 */
	public static final int TYPE_VENDOR_PAYMENT = 25;
	public static final int TYPE_RECEIVE_VAT = 31;

	public static final int STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED = 0;
	public static final int STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED = 1;
	public static final int STATUS_PAID_OR_APPLIED_OR_ISSUED = 2;
	public static final int STATUS_DELETED = 3;

	public static final int STATUS_OPEN = 101;
	public static final int STATUS_COMPLETED = 102;
	public static final int STATUS_CANCELLED = 103;

	public static final int CATEGORY_CUSTOMER = 1;
	public static final int CATEGORY_VENDOR = 2;
	public static final int CATEGORY_BANKING = 3;

	public static final int TYPE_MISC_SAMPLE_FORM = 33;
	/**
	 * in Edit mode of transaction, if any transaction has reference in
	 * VATRETURN(FILE VAT), then edit should be disabled.user cant edit them,
	 * this variable value be assign at time of conversion server to client
	 * object. by default can edit all transactions.
	 */
	public boolean canEdit = true;
	private long location;
	int version;
	public long id;
	int type;
	long transactionDate;
	String number = "";
	// String checkNumber = "";

	List<ClientTransactionItem> transactionItems;
	List<ClientTransactionMakeDeposit> transactionMakeDeposit;
	List<ClientTransactionPayBill> transactionPayBill;
	List<ClientTransactionReceivePayment> transactionReceivePayment;
	List<ClientTransactionIssuePayment> transactionIssuePayment;
	List<ClientTransactionPaySalesTax> transactionPaySalesTax;

	ClientCreditsAndPayments creditsAndPayments;

	List<ClientEntry> entry;

	int status = 0;
	boolean canVoidOrEdit = true;
	boolean isDeposited = false;

	boolean isVoid;
	boolean isDeleted;
	boolean isEdited;
	double total;
	double netAmount;

	double previousTotal;
	String paymentMethod;
	protected String memo;
	String reference;

	// For Sales Tax Liability Report
	double subTotal;
	double totalTaxableAmount;
	double totalNonTaxableAmount;

	private long recurringTransaction;

	// For UK version only

	boolean amountsIncludeVAT;

	long transactionMakeDepositEntries;

	private String currencyCode;
	/**
	 * Multiplication factor to convert selected currency to base currency
	 */
	private double currencyFactor;

	/** This Transaction belongs to Which reconciliation */
	private ClientReconciliation reconciliation;

	private ClientAccounterClass accounterClass;

	private ClientActivity lastActivity;

	// boolean showPricesWithVATOrVATInclusive;
	// boolean EUVATExemptPayee;
	// boolean isVATreturned;
	//
	// double netAmount;
	// double totalVAT;
	//
	//
	//
	// public double getNetAmount() {
	// return netAmount;
	// }
	//
	// public void setNetAmount(double netAmount) {
	// this.netAmount = netAmount;
	// }
	//
	// public double getTotalVAT() {
	// return totalVAT;
	// }
	//
	// public void setTotalVAT(double totalVAT) {
	// this.totalVAT = totalVAT;
	// }

	public ClientTransaction() {
		transactionItems = new ArrayList<ClientTransactionItem>();
	}

	public ClientCreditsAndPayments getCreditsAndPayments() {
		return creditsAndPayments;
	}

	public void setCreditsAndPayments(
			ClientCreditsAndPayments creditsAndPayments) {
		this.creditsAndPayments = creditsAndPayments;
	}

	public double getAllLineTotal() {
		return subTotal;
	}

	public List<ClientTransactionPaySalesTax> getTransactionPaySalesTax() {
		return transactionPaySalesTax;
	}

	public void setTransactionPaySalesTax(
			List<ClientTransactionPaySalesTax> transactionPaySalesTax) {
		this.transactionPaySalesTax = transactionPaySalesTax;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
	}

	// public boolean isShowPricesWithVATOrVATInclusive() {
	// return showPricesWithVATOrVATInclusive;
	// }
	//
	// public void setShowPricesWithVATOrVATInclusive(
	// boolean showPricesWithVATOrVATInclusive) {
	// this.showPricesWithVATOrVATInclusive = showPricesWithVATOrVATInclusive;
	// }
	//
	// public boolean isEUVATExemptPayee() {
	// return EUVATExemptPayee;
	// }
	//
	// public void setEUVATExemptPayee(boolean exemptPayee) {
	// EUVATExemptPayee = exemptPayee;
	// }
	//
	// public boolean isVATreturned() {
	// return isVATreturned;
	// }
	//
	// public void setVATreturned(boolean isVATreturned) {
	// this.isVATreturned = isVATreturned;
	// }

	public long getTransactionMakeDepositEntries() {
		return transactionMakeDepositEntries;
	}

	public void setTransactionMakeDepositEntries(
			long transactionMakeDepositEntries) {
		this.transactionMakeDepositEntries = transactionMakeDepositEntries;
	}

	/**
	 * @return the amountsIncludeVAT
	 */
	public boolean isAmountsIncludeVAT() {
		return amountsIncludeVAT;
	}

	/**
	 * @param amountsIncludeVAT
	 *            the amountsIncludeVAT to set
	 */
	public void setAmountsIncludeVAT(boolean amountsIncludeVAT) {
		this.amountsIncludeVAT = amountsIncludeVAT;
	}

	public String getPaymentMethod() {
		return UIUtils.getpaymentMethodCheckBy_CompanyType(paymentMethod);
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * @return the isVoidBefore
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @param isVoidBefore
	 *            the isVoidBefore to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

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
	public ClientFinanceDate getDate() {
		if (transactionDate == 0.0)
			return null;
		else
			return new ClientFinanceDate(this.transactionDate);
	}

	/**
	 * @param date2
	 *            the date to set
	 */
	public void setDate(long date) {
		this.transactionDate = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	// /**
	// * @return the checkNumber
	// */
	//
	// public String getCheckNumber(){
	// return checkNumber;
	// }
	//
	// /**
	// * @param checkNumber
	// * the checkNumber to set
	// */
	// public void setCheckNumber(String checkNumber){
	// this.checkNumber=checkNumber;
	// }

	/**
	 * @return the transactionItems
	 */
	public List<ClientTransactionItem> getTransactionItems() {
		return transactionItems;
	}

	/**
	 * @param transactionItems
	 *            the transactionItems to set
	 */
	public void setTransactionItems(List<ClientTransactionItem> transactionItems) {
		this.transactionItems = transactionItems;

		if (transactionItems == null)
			return;

		for (ClientTransactionItem transactionItem : transactionItems) {
			transactionItem.setTransaction(this);
		}
	}

	/**
	 * @return the transactionMakeDeposit
	 */
	public List<ClientTransactionMakeDeposit> getTransactionMakeDeposit() {
		return transactionMakeDeposit;
	}

	/**
	 * @param transactionMakeDeposit
	 *            the transactionMakeDeposit to set
	 */
	public void setTransactionMakeDeposit(
			List<ClientTransactionMakeDeposit> transactionMakeDeposit) {
		this.transactionMakeDeposit = transactionMakeDeposit;
	}

	/**
	 * @return the transactionPayBill
	 */
	public List<ClientTransactionPayBill> getTransactionPayBill() {
		return transactionPayBill;
	}

	/**
	 * @param transactionPayBill
	 *            the transactionPayBill to set
	 */
	public void setTransactionPayBill(
			List<ClientTransactionPayBill> transactionPayBill) {
		this.transactionPayBill = transactionPayBill;
	}

	/**
	 * @return the transactionReceivePayment
	 */
	public List<ClientTransactionReceivePayment> getTransactionReceivePayment() {
		return this.transactionReceivePayment;
	}

	/**
	 * @param transactionReceivePayment
	 *            the transactionReceivePayment to set
	 */
	public void setTransactionReceivePayment(
			List<ClientTransactionReceivePayment> transactionReceivePayment) {
		this.transactionReceivePayment = transactionReceivePayment;
	}

	/**
	 * @return the entry
	 */
	public List<ClientEntry> getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(List<ClientEntry> entry) {
		this.entry = entry;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the transactionIssuePayment
	 */
	public List<ClientTransactionIssuePayment> getTransactionIssuePayment() {
		return transactionIssuePayment;
	}

	/**
	 * @param transactionIssuePayment
	 *            the transactionIssuePayment to set
	 */
	public void setTransactionIssuePayment(
			List<ClientTransactionIssuePayment> transactionIssuePayment) {
		this.transactionIssuePayment = transactionIssuePayment;
	}

	/**
	 * @return the isDeposited
	 */
	public Boolean getIsDeposited() {
		return isDeposited;
	}

	/**
	 * @param isDeposited
	 *            the isDeposited to set
	 */
	public void setIsDeposited(boolean isDeposited) {
		this.isDeposited = isDeposited;
	}

	/**
	 * @return the canVoidOrEdit
	 */
	public boolean getCanVoidOrEdit() {
		return canVoidOrEdit;
	}

	/**
	 * @param canVoidOrEdit
	 *            the canVoidOrEdit to set
	 */
	public void setCanVoidOrEdit(boolean canVoidOrEdit) {
		this.canVoidOrEdit = canVoidOrEdit;
	}

	protected double getDiscountTotalSum() {

		double discountAmt = 0.00D;

		if (transactionItems == null) {
			return discountAmt;
		}

		for (ClientTransactionItem item : transactionItems) {

			if (item.getType() != ClientTransactionItem.TYPE_ITEM)
				continue;

			discountAmt = discountAmt + item.getDiscount();

		}

		return discountAmt;
	}

	protected double getTaxableLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (ClientTransactionItem txItem : transactionItems) {

			if (txItem.getType() == ClientTransactionItem.TYPE_COMMENT)
				continue;
			// FIXME ::: check it
			// if (txItem.getItemTax() != null &&
			// txItem.getItemTax().isTaxable()) {
			//
			// amount += ((txItem.getLineTotal() != null) ? txItem
			// .getLineTotal() : 0.00D);
			//
			// }

		}

		return amount;

	}

	protected double getLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (ClientTransactionItem txItem : transactionItems) {

			if (txItem.getType() == ClientTransactionItem.TYPE_COMMENT)
				continue;
			// FIXME :: check it
			// amount += ((txItem.getLineTotal() != null) ?
			// txItem.getLineTotal()
			// : 0.00D);

		}

		return amount;

	}

	public boolean isNotNull() {

		return this != null;
	}

	public boolean isInvoice() {

		return this != null && this instanceof ClientInvoice;
	}

	public boolean isQuote() {

		return this != null && this instanceof ClientEstimate;
	}

	public boolean isCashSale() {

		return this != null && this instanceof ClientCashSales;
	}

	public boolean isCashPurchase() {

		return this != null && this instanceof ClientCashPurchase;
	}

	public boolean isEnterBill() {

		return this != null && this instanceof ClientEnterBill;
	}

	public boolean isVendorCreditMemo() {

		return this != null && this instanceof ClientVendorCreditMemo;
	}

	public boolean isPayBill() {

		return this != null && this instanceof ClientPayBill;
	}

	public boolean isCreditCardCharge() {

		return this != null && this instanceof ClientCreditCardCharge;
	}

	public boolean isWriteCheck() {

		return this != null && this instanceof ClientWriteCheck;

	}

	public boolean isMakeDeposit() {

		return this != null && this instanceof ClientMakeDeposit;
	}

	protected boolean isBecameVoid() {
		return isVoid && !this.isDeleted;
	}

	public boolean isReceivePayment() {

		return this != null && this instanceof ClientReceivePayment;
	}

	public boolean isPaySalesTax() {

		return this != null && this instanceof ClientPaySalesTax;
	}

	public boolean isPayVAT() {

		return this != null && this instanceof ClientPayVAT;
	}

	public boolean isReceiveVAT() {

		return this != null && this instanceof ClientReceiveVAT;
	}

	@Override
	public String getDisplayName() {
		return "Transaction";
	}

	@Override
	public String getName() {
		return "Transaction";
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public double getCurrencyFactor() {
		return currencyFactor;
	}

	public ClientTransaction clone() {
		ClientTransaction clientTransactionClone = (ClientTransaction) this
				.clone();
		// transactionItems list
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		clientTransactionClone.transactionItems = transactionItems;

		// transactionMakeDeposit list
		List<ClientTransactionMakeDeposit> transactionMakeDeposit = new ArrayList<ClientTransactionMakeDeposit>();
		for (ClientTransactionMakeDeposit clientTransactionMakeDeposit : this.transactionMakeDeposit) {
			transactionMakeDeposit.add(clientTransactionMakeDeposit.clone());
		}
		clientTransactionClone.transactionMakeDeposit = transactionMakeDeposit;

		// transactionPayBill list
		List<ClientTransactionPayBill> transactionPayBillList = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBillList.add(clientTransactionPayBill.clone());
		}
		clientTransactionClone.transactionPayBill = transactionPayBillList;

		// transactionReceivePayment list
		List<ClientTransactionReceivePayment> transactionReceivePaymentList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePaymentList.add(clientTransactionReceivePayment
					.clone());
		}
		clientTransactionClone.transactionReceivePayment = transactionReceivePaymentList;
		// transactionIssuePayment list
		List<ClientTransactionIssuePayment> transactionIssuePayment = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayment.add(clientTransactionIssuePayment.clone());
		}
		clientTransactionClone.transactionIssuePayment = transactionIssuePayment;
		// transactionPaySalestax list
		List<ClientTransactionPaySalesTax> transactionPaySalesTax = new ArrayList<ClientTransactionPaySalesTax>();
		for (ClientTransactionPaySalesTax clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTax.add(clientTransactionPaySalesTax.clone());
		}
		clientTransactionClone.transactionPaySalesTax = transactionPaySalesTax;

		return clientTransactionClone;
	}

	public void setRecurringTransaction(long recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}

	public long getRecurringTransaction() {
		return recurringTransaction;
	}

	public long getLocation() {
		return location;
	}

	public void setLocation(long location) {
		this.location = location;
	}

	/**
	 * @return the reconciliation
	 */
	public ClientReconciliation getReconciliation() {
		return reconciliation;
	}

	/**
	 * @param reconciliation
	 *            the reconciliation to set
	 */
	public void setReconciliation(ClientReconciliation reconciliation) {
		this.reconciliation = reconciliation;
	}

	/*
	 * protected ClientTransaction(ClientTransaction original){
	 * amountsIncludeVAT = original.amountsIncludeVAT; canEdit =
	 * original.canEdit; canVoidOrEdit = original.canVoidOrEdit;
	 * if(original.creditsAndPayments!=null){ creditsAndPayments = new
	 * ClientCreditsAndPayments(original.creditsAndPayments); } currencyCode =
	 * original.currencyCode; currencyFactor = original.currencyFactor;
	 * 
	 * if(original.entry!=null){ entry = new ArrayList<ClientEntry>(); for
	 * (ClientEntry e : original.entry) { entry.add(new ClientEntry(e)); } }
	 * 
	 * // id = original.id; isDeleted = original.isDeleted; isDeposited =
	 * original.isDeposited; isEdited = original.isEdited; isVoid =
	 * original.isVoid; memo = original.memo; netAmount = original.netAmount;
	 * number = original.number; paymentMethod = original.paymentMethod;
	 * previousTotal = original.previousTotal; recurringTransaction =
	 * original.recurringTransaction; reference = original.reference; status =
	 * original.status; subTotal = original.subTotal; total = original.total;
	 * totalNonTaxableAmount = original.totalNonTaxableAmount;
	 * totalTaxableAmount = original.totalTaxableAmount; transactionDate =
	 * original.transactionDate; // transactionIssuePayment //
	 * if(original.transactionIssuePayment!=null){ // transactionIssuePayment =
	 * new ArrayList<ClientTransactionIssuePayment>(); // for
	 * (ClientTransactionIssuePayment e : original.transactionIssuePayment) { //
	 * transactionIssuePayment.add(new ClientTransactionIssuePayment(e)); // }
	 * // }
	 * 
	 * // transactionItems // if(original.transactionItems!=null){ //
	 * transactionItems = new ArrayList<ClientTransactionItem>(); // for
	 * (ClientTransactionItem e : original.transactionItems) { //
	 * transactionItems.add(new ClientTransactionItem(e)); // } // }
	 * 
	 * // transactionMakeDeposit if(original.transactionMakeDeposit!=null){
	 * transactionMakeDeposit = new ArrayList<ClientTransactionMakeDeposit>();
	 * for (ClientTransactionMakeDeposit e: original.transactionMakeDeposit) {
	 * transactionMakeDeposit.add(new ClientTransactionMakeDeposit(e)); } }
	 * 
	 * transactionMakeDepositEntries = original.transactionMakeDepositEntries;
	 * // transactionPayBill if(original.transactionPayBill!=null){
	 * transactionPayBill = new ArrayList<ClientTransactionPayBill>(); for
	 * (ClientTransactionPayBill e : original.transactionPayBill) {
	 * transactionPayBill.add(new ClientTransactionPayBill(e)); } }
	 * 
	 * // transactionPaySalesTax if(original.transactionPaySalesTax!=null){
	 * transactionPaySalesTax = new ArrayList<ClientTransactionPaySalesTax>();
	 * for (ClientTransactionPaySalesTax e : original.transactionPaySalesTax) {
	 * transactionPaySalesTax.add(new ClientTransactionPaySalesTax(e)); } } //
	 * transactionReceivePayment if(original.transactionReceivePayment!=null){
	 * transactionReceivePayment = new
	 * ArrayList<ClientTransactionReceivePayment>(); for
	 * (ClientTransactionReceivePayment e : original.transactionReceivePayment)
	 * { transactionReceivePayment.add(new ClientTransactionReceivePayment(e));
	 * } }
	 * 
	 * type = original.type; version = original.version; }
	 */
	/*
	 * protected ClientTransaction(ClientTransaction original){
	 * amountsIncludeVAT = original.amountsIncludeVAT; canEdit =
	 * original.canEdit; canVoidOrEdit = original.canVoidOrEdit;
	 * if(original.creditsAndPayments!=null){ creditsAndPayments = new
	 * ClientCreditsAndPayments(original.creditsAndPayments); } currencyCode =
	 * original.currencyCode; currencyFactor = original.currencyFactor;
	 * 
	 * if(original.entry!=null){ entry = new ArrayList<ClientEntry>(); for
	 * (ClientEntry e : original.entry) { entry.add(new ClientEntry(e)); } }
	 * 
	 * // id = original.id; isDeleted = original.isDeleted; isDeposited =
	 * original.isDeposited; isEdited = original.isEdited; isVoid =
	 * original.isVoid; memo = original.memo; netAmount = original.netAmount;
	 * number = original.number; paymentMethod = original.paymentMethod;
	 * previousTotal = original.previousTotal; recurringTransaction =
	 * original.recurringTransaction; reference = original.reference; status =
	 * original.status; subTotal = original.subTotal; total = original.total;
	 * totalNonTaxableAmount = original.totalNonTaxableAmount;
	 * totalTaxableAmount = original.totalTaxableAmount; transactionDate =
	 * original.transactionDate; // transactionIssuePayment //
	 * if(original.transactionIssuePayment!=null){ // transactionIssuePayment =
	 * new ArrayList<ClientTransactionIssuePayment>(); // for
	 * (ClientTransactionIssuePayment e : original.transactionIssuePayment) { //
	 * transactionIssuePayment.add(new ClientTransactionIssuePayment(e)); // }
	 * // }
	 * 
	 * // transactionItems // if(original.transactionItems!=null){ //
	 * transactionItems = new ArrayList<ClientTransactionItem>(); // for
	 * (ClientTransactionItem e : original.transactionItems) { //
	 * transactionItems.add(new ClientTransactionItem(e)); // } // }
	 * 
	 * // transactionMakeDeposit if(original.transactionMakeDeposit!=null){
	 * transactionMakeDeposit = new ArrayList<ClientTransactionMakeDeposit>();
	 * for (ClientTransactionMakeDeposit e: original.transactionMakeDeposit) {
	 * transactionMakeDeposit.add(new ClientTransactionMakeDeposit(e)); } }
	 * 
	 * transactionMakeDepositEntries = original.transactionMakeDepositEntries;
	 * // transactionPayBill if(original.transactionPayBill!=null){
	 * transactionPayBill = new ArrayList<ClientTransactionPayBill>(); for
	 * (ClientTransactionPayBill e : original.transactionPayBill) {
	 * transactionPayBill.add(new ClientTransactionPayBill(e)); } }
	 * 
	 * // transactionPaySalesTax if(original.transactionPaySalesTax!=null){
	 * transactionPaySalesTax = new ArrayList<ClientTransactionPaySalesTax>();
	 * for (ClientTransactionPaySalesTax e : original.transactionPaySalesTax) {
	 * transactionPaySalesTax.add(new ClientTransactionPaySalesTax(e)); } } //
	 * transactionReceivePayment if(original.transactionReceivePayment!=null){
	 * transactionReceivePayment = new
	 * ArrayList<ClientTransactionReceivePayment>(); for
	 * (ClientTransactionReceivePayment e : original.transactionReceivePayment)
	 * { transactionReceivePayment.add(new ClientTransactionReceivePayment(e));
	 * } }
	 * 
	 * type = original.type; version = original.version; }
	 */

	public ClientAccounterClass getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(ClientAccounterClass accounterClass) {
		this.accounterClass = accounterClass;
	}

	/**
	 * @return the lastActivity
	 */
	public ClientActivity getLastActivity() {
		return lastActivity;
	}

	/**
	 * @param lastActivity
	 *            the lastActivity to set
	 */
	public void setLastActivity(ClientActivity lastActivity) {
		this.lastActivity = lastActivity;
	}
}
