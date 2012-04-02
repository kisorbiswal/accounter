package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class BankAccount extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Bank bank;

	private int bankAccountType;

	private String bankAccountNumber;

	private FinanceDate lastReconcilationDate;

	/**
	 * Creates new Instance
	 */
	public BankAccount() {
	}

	public BankAccount(int type, String number, String name, boolean isActive,
			Account parent, int cashFlowCategory, double openingBalance,
			boolean isConsiderAsCashAccount, String comment, Bank bank,
			int bankAccountType, String bankAccountNumber, double creditLimit,
			String cardOrLoanNumber, boolean isIncrease,
			boolean isOpeningBalanceEditable, Account openingBalanceAccount,
			String flow, boolean isDefaultAccount, FinanceDate asOf) {

		super(type, number, name, isActive, parent, cashFlowCategory,
				openingBalance, isConsiderAsCashAccount, comment, creditLimit,
				cardOrLoanNumber, isIncrease, isOpeningBalanceEditable,
				openingBalanceAccount, flow, isDefaultAccount, asOf);

		this.bank = bank;
		this.bankAccountType = bankAccountType;
		this.bankAccountNumber = bankAccountNumber;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(int bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public FinanceDate getLastReconcilationDate() {
		return lastReconcilationDate;
	}

	public void setLastReconcilationDate(FinanceDate lastReconcilationDate) {
		this.lastReconcilationDate = lastReconcilationDate;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANK_ACCOUNT;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		super.checkNullValues();
		if (type == Account.TYPE_BANK) {
			if (bankAccountType <= 0) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, Global.get()
								.messages().bankAccountType());
			}
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		session.getNamedQuery("deleteBankChequeLayouts")
				.setParameter("account", this).executeUpdate();
		return super.onDelete(session);
	}
}
