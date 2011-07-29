package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientBankAccount extends ClientAccount {

	private long bank;

	private int bankAccountType;

	private String bankAccountNumber;

	private long lastReconcilationDate;

	public long getBank() {
		return bank;
	}

	public void setBank(long bank) {
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

	public long getLastReconcilationDate() {
		return lastReconcilationDate;
	}

	public void setLastReconcilationDate(long lastReconcilationDate) {
		this.lastReconcilationDate = lastReconcilationDate;
	}
}
