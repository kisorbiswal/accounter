package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class JobActualCostDetail extends BaseReport implements IsSerializable,
		Serializable {

	private static final long serialVersionUID = 1L;

	private String customerName;
	private String jobName;
	private int type;
	private ClientFinanceDate transactionDate;
	private String number;
	private String memo;
	private ClientAccount account;
	private ClientAccount splitAccount;
	private double total;
	private long transaction;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	public ClientAccount getSplitAccount() {
		return splitAccount;
	}

	public void setSplitAccount(ClientAccount splitAccount) {
		this.splitAccount = splitAccount;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}
}
