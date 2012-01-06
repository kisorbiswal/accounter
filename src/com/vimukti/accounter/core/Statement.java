package com.vimukti.accounter.core;

import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */

public class Statement extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FinanceDate importtedDate;

	private FinanceDate startDate;

	private FinanceDate endDate;

	private double startBalance;

	private double closingBalance;

	private long accountId;

	private List<StatementRecord> StatementRecord;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANK_STATEMENT;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	public double getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public FinanceDate getImporttedDate() {
		return importtedDate;
	}

	public void setImporttedDate(FinanceDate importtedDate) {
		this.importtedDate = importtedDate;
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

	public List<StatementRecord> getStatementRecord() {
		return StatementRecord;
	}

	public void setStatementRecord(List<StatementRecord> statementRecord) {
		StatementRecord = statementRecord;
	}

}
