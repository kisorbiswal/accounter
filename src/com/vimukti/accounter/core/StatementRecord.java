package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti10
 * 
 */
public class StatementRecord extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FinanceDate statementDate;

	private String description;

	private String referenceNumber;

	private double spentAmount;

	private double receivedAmount;

	private double closingBalance;
	private Statement statement;

	public FinanceDate getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(FinanceDate statementDate) {
		this.statementDate = statementDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public double getSpentAmount() {
		return spentAmount;
	}

	public void setSpentAmount(double spentAmount) {
		this.spentAmount = spentAmount;
	}

	public double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BANKSTATEMENT_RECORD;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

}
