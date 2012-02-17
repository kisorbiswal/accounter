package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;

public class JobActualCostDetail extends BaseReport implements IsSerializable,
		Serializable {

	private static final long serialVersionUID = 1L;

	private String customerName;
	private String jobName;
	private long jobid;
	private int type;
	private ClientFinanceDate transactionDate;
	private String number;
	private String memo;
	private long account;
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

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public ClientAccount getSplitAccount() {
		ClientAccount account = Accounter.getCompany().getAccount(
				Accounter.getCompany().getAccountsReceivableAccountId());
		return account;
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

	public long getJobid() {
		return jobid;
	}

	public void setJobid(long jobid) {
		this.jobid = jobid;
	}
}
