package com.vimukti.accounter.core;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class JournalEntry extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7677695043049426006L;
	/**
	 * 
	 */
	public static final int TYPE_NORMAL_JOURNAL_ENTRY = 1;
	public static final int TYPE_CASH_BASIS_JOURNAL_ENTRY = 2;

	/**
	 * Transaction Journal Entry Debit Total
	 */
	double debitTotal = 0D;

	/**
	 * Transaction Journal Entry Credit Total
	 */
	double creditTotal = 0D;

	/**
	 * Type of {@link JournalEntry} , Either TYPE_NORMAL_JOURNAL_ENTRY or
	 * TYPE_CASH_BASIS_JOURNAL_ENTRY
	 */
	int journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;

	double balanceDue = 0d;

	/**
	 * A Journal Entry Has List of Entries List of Entries unlike Transaction
	 * Items,
	 * 
	 * @see Entry
	 */
	List<Entry> entry;

	@ReffereredObject
	Transaction transaction;

	public Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	//

	public JournalEntry() {
		setType(Transaction.TYPE_JOURNAL_ENTRY);
	}

	public JournalEntry(int journalEntryType) {
		this.journalEntryType = journalEntryType;
	}

	public JournalEntry(Customer customer, String number, int journalEntryType) {

		setCompany(customer.getCompany());
		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = customer.balanceAsOf;
		this.memo = "Opening Balance";
		this.balanceDue = customer.getOpeningBalance();

		List<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();

		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_CUSTOMER);
		entry1.setAccount(getCompany().getOpeningBalancesAccount());
		entry1.setMemo(customer.getName());
		entry1.setDebit(0D);
		entry1.setCredit(customer.getOpeningBalance());
		entry1.setJournalEntry(this);

		Entry entry2 = new Entry();
		entry2.setVoucherNumber(voucherNumber);
		entry2.setType(Entry.TYPE_CUSTOMER);
		entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_CUSTOMER);
		entry2.setCustomer(customer);
		entry2.setMemo(AccounterServerConstants.MEMO_OPENING_BALANCE);
		entry2.setDebit(customer.getOpeningBalance());
		entry2.setCredit(0D);
		entry2.setJournalEntry(this);

		entries.add(entry1);
		entries.add(entry2);
		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		this.setCreditTotal(entry1.getCredit() + entry2.getCredit());

	}

	public JournalEntry(Vendor vendor, String number, int journalEntryType) {
		setCompany(vendor.getCompany());
		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = vendor.balanceAsOf;
		this.memo = AccounterServerConstants.MEMO_OPENING_BALANCE;
		this.balanceDue = vendor.getOpeningBalance();

		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		Entry entry1 = new Entry();
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_VENDOR);
		entry1.setAccount(getCompany().getOpeningBalancesAccount());
		entry1.setMemo(vendor.getName());
		entry1.setDebit(vendor.getOpeningBalance());
		entry1.setCredit(0D);
		entry1.setJournalEntry(this);

		Entry entry2 = new Entry();
		entry2.setVoucherNumber(voucherNumber);
		entry2.setType(Entry.TYPE_VENDOR);
		entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_VENDOR);
		entry2.setVendor(vendor);
		entry2.setMemo("Opening Balance");
		entry2.setDebit(0D);
		entry2.setCredit(vendor.getOpeningBalance());
		entry2.setJournalEntry(this);

		entries.add(entry1);
		entries.add(entry2);
		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
	}

	public JournalEntry(Account account, String number, int journalEntryType) {
		setCompany(account.getCompany());
		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = account.asOf;

		this.memo = AccounterServerConstants.MEMO_OPENING_BALANCE;

		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		Entry entry1 = new Entry();
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		entry1.setAccount(getCompany().getOpeningBalancesAccount());
		entry1.setMemo(account.getName());
		entry1.setJournalEntry(this);

		Entry entry2 = new Entry();
		entry2.setVoucherNumber(voucherNumber);
		entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		entry2.setAccount(account);
		entry2.setMemo("Opening Balance");
		entry2.setJournalEntry(this);

		if (account.isIncrease()) {

			entry2.setDebit(0D);
			entry2.setCredit(account.getOpeningBalance());
			entry1.setDebit(account.getOpeningBalance());
			entry1.setCredit(0D);
		} else {

			entry2.setDebit(account.getOpeningBalance());
			entry2.setCredit(0D);
			entry1.setDebit(0D);
			entry1.setCredit(account.getOpeningBalance());

		}

		entries.add(entry1);
		entries.add(entry2);
		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		this.setCreditTotal(entry1.getCredit() + entry2.getCredit());

	}

	public JournalEntry(FiscalYear fiscalYear, double netIncome,
			List<AccountTransactionByAccount> accountTransactionList,
			String number, int journalEntryType) {

		double debitTotal = 0D;
		double creditTotal = 0D;

		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = fiscalYear.getEndDate();
		this.memo = "Closing Fiscal Year";

		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		Entry entry = null;
		for (AccountTransactionByAccount accountTransactionByTaxCode : accountTransactionList) {
			Account account = accountTransactionByTaxCode.getAccount();

			entry = new Entry();
			entry.setVoucherNumber(voucherNumber);
			entry.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
			entry.setAccount(account);
			entry.setMemo("Balance moved to Retained Earnings");
			entry.setJournalEntry(this);

			if (account.isIncrease == true) {
				if (DecimalUtil.isGreaterThan(
						accountTransactionByTaxCode.getAmount(), 0.0)) {
					entry.setDebit(Math.abs(accountTransactionByTaxCode
							.getAmount()));
				} else {
					entry.setCredit(Math.abs(accountTransactionByTaxCode
							.getAmount()));
				}
			} else {
				if (DecimalUtil.isGreaterThan(
						accountTransactionByTaxCode.getAmount(), 0.0)) {
					entry.setCredit(Math.abs(accountTransactionByTaxCode
							.getAmount()));
				} else {
					entry.setDebit(Math.abs(accountTransactionByTaxCode
							.getAmount()));
				}
			}
			entries.add(entry);
			debitTotal += entry.getDebit();
			creditTotal += entry.getCredit();
		}

		Entry retainedEarningsEntry = new Entry();
		retainedEarningsEntry.setVoucherNumber(voucherNumber);
		retainedEarningsEntry.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		retainedEarningsEntry
				.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		retainedEarningsEntry.setAccount(getCompany().retainedEarningsAccount);
		retainedEarningsEntry.setMemo("Net income");
		retainedEarningsEntry.setJournalEntry(this);
		if (DecimalUtil.isGreaterThan(netIncome, 0.0)) {
			retainedEarningsEntry.setCredit(Math.abs(netIncome));
			creditTotal += retainedEarningsEntry.getCredit();

		} else {
			retainedEarningsEntry.setDebit(Math.abs(netIncome));
			debitTotal += retainedEarningsEntry.getDebit();
		}

		entries.add(retainedEarningsEntry);
		this.setEntry(entries);

		this.setDebitTotal(debitTotal);
		this.setCreditTotal(creditTotal);
	}

	public JournalEntry(FiscalYear fiscalYear,
			List<AccountTransactionByAccount> cashBasisAccountEntries,
			String number, int journalEntryType) {
		double debitTotal = 0D;
		double creditTotal = 0D;

		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = fiscalYear.getEndDate();
		this.memo = "Closing Fiscal Year";

		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());

		Entry entry = null;

		for (AccountTransactionByAccount cashBasisEntry : cashBasisAccountEntries) {

			Account account = cashBasisEntry.getAccount();

			entry = new Entry();
			entry.setVoucherNumber(voucherNumber);
			entry.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
			entry.setAccount(account);
			if (account.getName().equals(
					AccounterServerConstants.RETAINED_EARNINGS)) {
				entry.setMemo("Net income");
			} else {
				entry.setMemo("Balance moved to Retained Earnings");
			}
			entry.setJournalEntry(this);

			if (account.isIncrease == true) {
				if (DecimalUtil.isGreaterThan(cashBasisEntry.getAmount(), 0.0)) {
					entry.setDebit(Math.abs(cashBasisEntry.getAmount()));
				} else {
					entry.setCredit(Math.abs(cashBasisEntry.getAmount()));
				}
			} else {
				if (DecimalUtil.isGreaterThan(cashBasisEntry.getAmount(), 0.0)) {
					entry.setCredit(Math.abs(cashBasisEntry.getAmount()));
				} else {
					entry.setDebit(Math.abs(cashBasisEntry.getAmount()));
				}
			}
			entries.add(entry);
			debitTotal += entry.getDebit();
			creditTotal += entry.getCredit();

		}

		Entry otherCashIncomeEntry = new Entry();
		otherCashIncomeEntry.setVoucherNumber(voucherNumber);
		otherCashIncomeEntry.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		otherCashIncomeEntry
				.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		otherCashIncomeEntry.setAccount(getCompany().otherCashIncomeAccount);
		otherCashIncomeEntry.setMemo("Balance moved to Retained Earnings");
		otherCashIncomeEntry.setJournalEntry(this);

		if (DecimalUtil.isGreaterThan((debitTotal - creditTotal), 0.0)) {
			otherCashIncomeEntry.setCredit(debitTotal - creditTotal);
			creditTotal += otherCashIncomeEntry.getCredit();
		} else {
			otherCashIncomeEntry.setDebit(Math.abs(debitTotal - creditTotal));
			debitTotal += otherCashIncomeEntry.getDebit();
		}

		entries.add(0, otherCashIncomeEntry);
		this.setEntry(entries);
		this.setDebitTotal(debitTotal);
		this.setCreditTotal(creditTotal);
	}

	public JournalEntry(FixedAsset fixedAsset, FinanceDate date, String number,
			double amount) {
		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.number = number;
		this.transactionDate = date;
		this.memo = "Fixed Asset - " + fixedAsset.getAssetNumber()
				+ " Depreciation Journal Entry";

		// To avoid the Voiding of this Journal Entry, because it is for
		// Depreciation.
		this.reference = AccounterServerConstants.JOURNAL_ENTRY_FOR_DEPRECIATION;

		List<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();

		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setAccount(fixedAsset.getAssetAccount()
				.getLinkedAccumulatedDepreciationAccount());
		entry1.setMemo("Depreciation");
		entry1.setDebit(0D);
		entry1.setCredit(amount);
		entry1.setJournalEntry(this);

		Entry entry2 = new Entry();
		entry2.setVoucherNumber(voucherNumber);
		entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry2.setAccount(fixedAsset.getDepreciationExpenseAccount());
		entry2.setMemo("Depreciation");
		entry2.setDebit(amount);
		entry2.setCredit(0D);
		entry2.setJournalEntry(this);

		entries.add(entry1);
		entries.add(entry2);
		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
	}

	public JournalEntry(FixedAsset fixedAsset, String number) {

		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.number = number;
		this.transactionDate = fixedAsset.getSoldOrDisposedDate();
		this.memo = "Fixed Asset Depreciation";
		// To avoid the Voiding of this Journal Entry, because it is for
		// Depreciation.
		this.reference = AccounterServerConstants.JOURNAL_ENTRY_FOR_DEPRECIATION;

		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		double salesPrice = fixedAsset.getSalePrice();
		double purchasePrice = fixedAsset.getPurchasePrice();

		FixedAssetSellOrDisposeReviewJournal reviewJournal = null;
		try {
			reviewJournal = fixedAsset.getReviewJournal();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// double lessAccumulatedDepreciationAmount = fixedAsset
		// .getAccumulatedDepreciationAmount();

		double lessAccumulatedDepreciationAmount = 0.0;
		if (reviewJournal != null
				&& reviewJournal.getDisposalJournal().containsKey(
						fixedAsset.getAssetAccount()
								.getLinkedAccumulatedDepreciationAccount()
								.getName())) {
			lessAccumulatedDepreciationAmount = (Double) reviewJournal
					.getDisposalJournal().get(
							fixedAsset.getAssetAccount()
									.getLinkedAccumulatedDepreciationAccount()
									.getName());
		}

		double totalCapitalGain = Double
				.parseDouble(decimalFormat
						.format((salesPrice > purchasePrice) ? (salesPrice - purchasePrice)
								: 0.0));

		double lossOrGainOnDisposal = Double.parseDouble(decimalFormat
				.format((salesPrice + Math
						.abs(lessAccumulatedDepreciationAmount))
						- purchasePrice - totalCapitalGain));

		List<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();

		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setAccount(fixedAsset.getAssetAccount());
		entry1.setMemo("Disposal of FixedAsset " + fixedAsset.getName());
		entry1.setDebit(0D);
		entry1.setCredit(fixedAsset.getPurchasePrice());
		entry1.setJournalEntry(this);
		entries.add(entry1);

		Entry entry2 = null;

		if (!DecimalUtil.isEquals(fixedAsset.getSalePrice(), 0)) {

			entry2 = new Entry();
			entry2.setVoucherNumber(voucherNumber);
			entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry2.setAccount(fixedAsset.getAccountForSale());
			entry2.setMemo("Depreciation");
			entry2.setDebit(fixedAsset.getSalePrice());
			entry2.setCredit(0d);
			entry2.setJournalEntry(this);
			entries.add(entry2);

		}

		Entry entry3 = null;

		if (!DecimalUtil.isEquals(lessAccumulatedDepreciationAmount, 0)) {

			entry3 = new Entry();
			entry3.setVoucherNumber(voucherNumber);
			entry3.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry3.setAccount(fixedAsset.getAssetAccount()
					.getLinkedAccumulatedDepreciationAccount());
			entry3.setMemo("Depreciation");
			entry3.setDebit(Math.abs(lessAccumulatedDepreciationAmount));
			entry3.setCredit(0D);
			entry3.setJournalEntry(this);
			entries.add(entry3);
		}

		Entry entry4 = null;
		if (!DecimalUtil.isEquals(lossOrGainOnDisposal, 0)) {
			entry4 = new Entry();
			entry4.setVoucherNumber(voucherNumber);
			entry4.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry4.setAccount(fixedAsset.getLossOrGainOnDisposalAccount());
			entry4.setMemo("Depreciation");
			if (DecimalUtil.isLessThan(lossOrGainOnDisposal, 0)) {
				entry4.setDebit(Math.abs(lossOrGainOnDisposal));
				entry4.setCredit(0);
			} else {
				entry4.setDebit(0);
				entry4.setCredit(Math.abs(lossOrGainOnDisposal));
			}
			entry4.setJournalEntry(this);
			entries.add(entry4);
		}

		Entry entry5 = null;

		if (!DecimalUtil.isEquals(totalCapitalGain, 0.0)) {
			entry5 = new Entry();
			entry5.setVoucherNumber(voucherNumber);
			entry5.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
			entry5.setAccount(fixedAsset.getTotalCapitalGain());
			entry5.setMemo("Depreciation");
			if (DecimalUtil.isLessThan(totalCapitalGain, 0)) {
				entry5.setDebit(Math.abs(totalCapitalGain));
				entry5.setCredit(0);
			} else {
				entry5.setDebit(0);
				entry5.setCredit(Math.abs(totalCapitalGain));
			}
			entry5.setJournalEntry(this);
			entries.add(entry5);
		}

		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit()
				+ (entry2 != null ? entry2.getDebit() : 0)
				+ (entry3 != null ? entry3.getDebit() : 0)
				+ (entry4 != null ? entry4.getDebit() : 0)
				+ (entry5 != null ? entry5.getDebit() : 0));
		this.setCreditTotal(entry1.getCredit()
				+ (entry2 != null ? entry2.getCredit() : 0)
				+ (entry3 != null ? entry3.getCredit() : 0)
				+ (entry4 != null ? entry4.getCredit() : 0)
				+ (entry5 != null ? entry5.getCredit() : 0));

	}

	public JournalEntry(TAXAdjustment adjustment, String number,
			int journalEntryType) {
		setCompany(adjustment.getCompany());
		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = journalEntryType;
		this.number = number;
		this.transactionDate = adjustment.transactionDate;
		this.balanceDue = adjustment.total;

		this.transaction = adjustment;
		this.memo = "VAT Adjustment";

		Account liabilityAccount;
		boolean isSalesType;

		if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			liabilityAccount = adjustment.getTaxAgency()
					.getSalesLiabilityAccount();
			isSalesType = true;
			this.memo = "TAX Adjustment";

		} else {
			liabilityAccount = adjustment.taxItem.isSalesType == true ? adjustment.taxItem.taxAgency
					.getSalesLiabilityAccount() : adjustment.taxItem.taxAgency
					.getPurchaseLiabilityAccount();
			isSalesType = adjustment.taxItem.isSalesType;
		}

		Account adjustmentAccount = adjustment.getAdjustmentAccount();

		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());

		Entry entry1 = new Entry();
		entry1.setVoucherNumber(voucherNumber);
		entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		entry1.setAccount(liabilityAccount);
		entry1.setMemo(adjustmentAccount.getName());
		entry1.setJournalEntry(this);
		entry1.setTotal(adjustment.total);
		entry1.setEntryDate(adjustment.getDate());

		Entry entry2 = new Entry();
		entry2.setVoucherNumber(voucherNumber);
		entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
		entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		entry2.setAccount(adjustmentAccount);
		entry2.setMemo(liabilityAccount.getName());
		entry2.setJournalEntry(this);
		entry2.setTotal(adjustment.total);
		entry2.setEntryDate(adjustment.getDate());

		if (adjustment.increaseVATLine) {
			if (isSalesType) {
				entry1.setDebit(0d);
				entry1.setCredit(adjustment.total);
				entry2.setDebit(adjustment.total);
				entry2.setCredit(0d);
			} else {
				entry1.setDebit(adjustment.total);
				entry1.setCredit(0d);
				entry2.setCredit(adjustment.total);
				entry2.setDebit(0d);
			}
		} else {
			if (isSalesType) {
				entry1.setDebit(adjustment.total);
				entry1.setCredit(0d);
				entry2.setCredit(adjustment.total);
				entry2.setDebit(0d);
			} else {
				entry1.setDebit(0d);
				entry1.setCredit(adjustment.total);
				entry2.setDebit(adjustment.total);
				entry2.setCredit(0d);
			}
		}

		entries.add(entry1);
		entries.add(entry2);
		this.setEntry(entries);
		this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
		this.setCreditTotal(entry1.getCredit() + entry2.getCredit());

	}

	// public JournalEntry(TAXAdjustment adjustment, String number,
	// int journalEntryType) {
	//
	// this.id = SecureUtils.createID();
	// this.type = Transaction.TYPE_JOURNAL_ENTRY;
	// this.journalEntryType = journalEntryType;
	// this.number = number;
	// this.transactionDate = adjustment.transactionDate;
	//
	// this.transaction = adjustment;
	// this.memo = "TAX Adjustment";
	//
	// Account liabilityAccount =
	// adjustment.taxAgency.getSalesLiabilityAccount();
	// Account adjustmentAccount = adjustment.getAdjustmentAccount();
	//
	// List<Entry> entries = new ArrayList<Entry>();
	// String voucherNumber = NumberUtils.getNextVoucherNumber();
	//
	// Entry entry1 = new Entry();
	// entry1.setVoucherNumber(voucherNumber);
	// entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
	// entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// entry1.setAccount(liabilityAccount);
	// entry1.setMemo(adjustmentAccount.getName());
	// entry1.setJournalEntry(this);
	// entry1.setTotal(adjustment.total);
	// entry1.setEntryDate(adjustment.getDate());
	//
	// Entry entry2 = new Entry();
	// entry2.setVoucherNumber(voucherNumber);
	// entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
	// entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// entry2.setAccount(adjustmentAccount);
	// entry2.setMemo(liabilityAccount.getName());
	// entry2.setJournalEntry(this);
	// entry2.setTotal(adjustment.total);
	// entry2.setEntryDate(adjustment.getDate());
	//
	// if (adjustment.increaseTAXLine) {
	// entry1.setDebit(0d);
	// entry1.setCredit(adjustment.total);
	// entry2.setDebit(adjustment.total);
	// entry2.setCredit(0d);
	//
	// } else {
	// entry1.setDebit(adjustment.total);
	// entry1.setCredit(0d);
	// entry2.setDebit(0d);
	// entry2.setCredit(adjustment.total);
	// }
	//
	// entries.add(entry1);
	// entries.add(entry2);
	// this.setEntry(entries);
	// this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
	// this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
	//
	// }

	public JournalEntry(VATReturn return1) {

		this.type = Transaction.TYPE_JOURNAL_ENTRY;
		this.journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;
		this.number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_JOURNAL_ENTRY, getCompany());
		this.transactionDate = return1.transactionDate;
		this.transaction = return1;

		double creditTotal = 0;
		double debitTotal = 0;
		List<Entry> entries = new ArrayList<Entry>();
		String voucherNumber = NumberUtils.getNextVoucherNumber(getCompany());
		for (Box b : return1.boxes) {
			// if (b.getTaxRateCalculations() != null
			// && b.getTaxRateCalculations().size() > 0) {

			if (b.getAmount() != 0
					&& (b.getBoxNumber() == 1 || b.getBoxNumber() == 2)) {

				Entry e = new Entry();

				e.setVoucherNumber(voucherNumber);
				if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
					e.setDebit(b.amount);
					debitTotal += b.amount;
				} else {
					creditTotal += (-1 * b.amount);
					e.setCredit(-1 * b.amount);
				}
				e.setAccount(return1.getTaxAgency().getSalesLiabilityAccount());

				e.setEntryDate(return1.getDate());
				e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
				e.setMemo("Filed VAT amount of " + b.amount + " for Box "
						+ b.boxNumber);
				e.setTaxItem(b.getTaxRateCalculations() != null ? (b
						.getTaxRateCalculations().get(0)).getTaxItem() : null);
				e.setTotal(b.getAmount());
				entries.add(e);

			} else if (b.getAmount() != 0 && b.getBoxNumber() == 4) {

				Entry e = new Entry();
				e.setVoucherNumber(voucherNumber);
				e.setTotal(b.getAmount());
				if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
					e.setCredit(b.amount);
					creditTotal += b.amount;
				} else {
					debitTotal += (-1 * b.amount);
					e.setDebit(-1 * b.amount);
				}
				e.setAccount(return1.getTaxAgency()
						.getPurchaseLiabilityAccount());
				e.setEntryDate(return1.getDate());
				e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
				e.setMemo("Filed VAT amount of " + b.amount + " for Box "
						+ b.boxNumber);
				e.setTaxItem(b.getTaxRateCalculations() != null ? (b
						.getTaxRateCalculations().get(0)).getTaxItem() : null);
				entries.add(e);
			} else if (b.getAmount() != 0
					&& (b.getBoxNumber() == 6 || b.boxNumber == 7
							|| b.boxNumber == 8 || b.boxNumber == 9)) {

				Entry e = new Entry();
				e.setVoucherNumber(voucherNumber);
				if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
					e.setDebit(0d);
					// debitTotal += b.amount;
				} else {
					// creditTotal += b.amount;
					e.setCredit(0d);
				}
				e.setTotal(b.getAmount());
				e.setTaxItem(b.getTaxRateCalculations() != null ? (b
						.getTaxRateCalculations().get(0)).getTaxItem() : null);
				e.setAccount(return1.getTaxAgency()
						.getPurchaseLiabilityAccount());
				e.setEntryDate(return1.getDate());
				e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
				e.setMemo("Filed net amount of " + b.amount + " for Box "
						+ b.boxNumber);
				entries.add(e);

			} else if (b.getAmount() != 0 && b.boxNumber == 10) {

				Entry e = new Entry();
				e.setVoucherNumber(voucherNumber);
				if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
					e.setDebit(b.amount);
					debitTotal += b.amount;
				} else {
					creditTotal += (-1 * b.amount);
					e.setCredit(-1 * b.amount);
				}
				e.setTotal(b.getAmount());
				e.setTaxItem(b.getTaxRateCalculations() != null ? (b
						.getTaxRateCalculations().get(0)).getTaxItem() : null);
				e.setAccount(return1.getTaxAgency()
						.getPurchaseLiabilityAccount());
				e.setEntryDate(return1.getDate());
				e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
				e.setMemo("Filed net amount of " + b.amount + " for Box "
						+ b.boxNumber);
				entries.add(e);
			}
			// }
		}
		double amount = return1.getBoxes().get(4).getAmount()
				+ return1.getBoxes().get(return1.getBoxes().size() - 1)
						.getAmount();

		Entry e = new Entry();
		e.setVoucherNumber(voucherNumber);
		e.setTotal(amount);
		e.setEntryDate(return1.getDate());
		e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
		e.setMemo("Filed VAT Amount");
		if (DecimalUtil.isGreaterThan(amount, 0)) {
			e.setCredit(amount);
			creditTotal += amount;

		} else {
			debitTotal += (-1 * amount);
			e.setDebit(-1 * amount);
		}

		e.setAccount(getCompany().getVATFiledLiabilityAccount());
		entries.add(e);
		this.setDebitTotal(debitTotal);
		this.setCreditTotal(creditTotal);

		this.setEntry(entries);

	}

	public JournalEntry(TransferFund transferFund) {
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return id;
	}

	@Override
	public FinanceDate getDate() {
		return transactionDate;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public double getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}

	@Override
	public boolean isDebitTransaction() {

		return false;
	}

	@Override
	public boolean isPositiveTransaction() {

		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_JOURNAL_ENTRY;
	}

	/**
	 * 
	 * @return the entry
	 */
	public List<Entry> getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		this.isVoidBefore = isVoid;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;

		this.total = this.debitTotal;
		this.creditTotal = 0.0;
		this.debitTotal = 0.0;

		// written by kumar.
		for (Entry entry : this.entry) {
			entry.setJournalEntry(this);

			if (entry.getType() == Entry.TYPE_CUSTOMER) {

				if (!DecimalUtil.isEquals(entry.getCredit(), 0)) {

					this.creditsAndPayments = new CreditsAndPayments(this);
					session.save(creditsAndPayments);
				} else {
					this.balanceDue = entry.getDebit();
				}

			} else if (entry.getType() == Entry.TYPE_VENDOR) {

				if (!DecimalUtil.isEquals(entry.getDebit(), 0)) {

					this.creditsAndPayments = new CreditsAndPayments(this);
					session.save(creditsAndPayments);
				} else {
					this.balanceDue = entry.getCredit();
				}
			}

		}

		// Creating Activity
		Activity activity = new Activity(getCompany(),
				AccounterThreadLocal.get(), ActivityType.ADD, this);
		session.save(activity);
		this.setLastActivity(activity);

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		// if (isBecameVoid()) {
		//
		// // If this Journal Entry has any Credits then make them as null.
		// this.creditsAndPayments = null;
		//
		// // If this Journal entry has been paid in any receive payments then
		// // roll back the payments and Update the credits balances.
		// if (this.transactionReceivePayments != null) {
		// for (TransactionReceivePayment trp : this.transactionReceivePayments)
		// {
		// trp.onVoidTransaction(session);
		// session.saveOrUpdate(trp);
		// }
		// }
		//
		// // If this Journal entry has been paid in any payments then roll
		// // back the payments and and Update the credits balances.
		// for (TransactionPayBill tx : this.transactionPayBills) {
		// tx.makeVoid(session);
		// }
		//
		// this.balanceDue = 0.0;
		//
		// if (this.transaction instanceof VATAdjustment
		// || this.transaction instanceof VATReturn) {
		// this.transaction.setVoid(this.isVoid);
		// this.transaction.onUpdate(session);
		// session.saveOrUpdate(this.transaction);
		// }
		// }
		// if (this.entry != null) {
		// for (Entry ti : this.entry) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }

		return false;
	}

	protected boolean isBecameVoid() {
		return isVoid && !this.isVoidBefore;
	}

	@Override
	public Payee getInvolvedPayee() {

		if (this.getEntry() != null) {
			for (Entry entry : this.getEntry()) {
				if (entry.customer != null) {
					return entry.customer;
				} else if (entry.vendor != null) {
					return entry.vendor;
				}
			}
		}
		return null;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {

		this.balanceDue += amount2;
	}

	private boolean equals(JournalEntry journalEntry) {

		for (int i = 0; i < this.transactionItems.size(); i++) {
			if (!this.transactionItems.get(i).equals(
					journalEntry.transactionItems.get(i))) {
				return false;
			}
			return true;
		}
		return false;
	}

	private void doVoidEffect(Session session, JournalEntry journalEntry) {

		/* If this Journal Entry has any Credits then make them as null. */
		if (this.creditsAndPayments != null)
			this.creditsAndPayments = null;

		/*
		 * If this Journal entry has been paid in any receive payments then roll
		 * back the payments and Update the credits balances.
		 */
		if (this.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				trp.onVoidTransaction(session);
				session.saveOrUpdate(trp);
			}
		}

		/*
		 * If this Journal entry has been paid in any payments then roll back
		 * the payments and and Update the credits balances.
		 */
		for (TransactionPayBill tx : this.transactionPayBills) {
			tx.makeVoid(session);
		}

		this.balanceDue = 0.0;

		if (this.transaction instanceof TAXAdjustment
				|| this.transaction instanceof VATReturn) {
			this.transaction.setVoid(this.isVoid);
			this.transaction.onUpdate(session);
			session.saveOrUpdate(this.transaction);
		}

		if (this.entry != null) {
			for (Entry ti : this.entry) {
				if (ti instanceof Lifecycle) {
					Lifecycle lifeCycle = (Lifecycle) ti;
					lifeCycle.onUpdate(session);
				}
			}
		}

	}

	@Override
	public void onEdit(Transaction clonedObject) {

		JournalEntry journalEntry = (JournalEntry) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if ((this.isVoid && !journalEntry.isVoid)
				|| (this.isDeleted() && !journalEntry.isDeleted())) {

			journalEntry.isEdited = false;
			this.isEdited = false;

			for (Entry voidEntry : this.entry) {

				voidEntry.updateAccountBalances(session, true);
			}

			doVoidEffect(session, this);

		} else {

			journalEntry.isEdited = true;
			this.isEdited = true;

			/* To rollback the effect of clonedObject JournalEntry */

			for (Entry preEntry : journalEntry.entry) {
				for (Entry newEntry : this.entry) {
					if (newEntry.id == preEntry.id) {
						if (!DecimalUtil.isEquals(preEntry.credit, 0.0)
								&& !DecimalUtil.isEquals(newEntry.debit, 0.0)) {
							newEntry.credit = 0.0;
						}
						if (!DecimalUtil.isEquals(preEntry.debit, 0.0)
								&& !DecimalUtil.isEquals(newEntry.credit, 0.0)) {
							newEntry.debit = 0.0;
						}
					}
				}

				preEntry.updateEntryAccountBalances(session, true);
			}

			/* Creates the effect for New JournalEntry */

			for (Entry newEntry : this.entry) {

				// newEntry.journalEntry.creditsAndPayments = null;
				newEntry.updateEntryAccountBalances(session, false);
			}
		}

		// super.onEdit(journalEntry);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		if (this.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

}
