package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientMakeDeposit extends ClientTransaction {

	String depositIn;

	String cashBackAccount;

	String cashBackMemo;

	double cashBackAmount;

	/**
	 * @return the cashBackAccount
	 */
	public String getCashBackAccount() {
		return cashBackAccount;
	}

	/**
	 * @param cashBackAccount
	 *            the cashBackAccount to set
	 */
	public void setCashBackAccount(String cashBackAccount) {
		this.cashBackAccount = cashBackAccount;
	}

	/**
	 * @return the depositIn
	 */
	public String getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(String depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the cashBackMemo
	 */
	public String getCashBackMemo() {
		return cashBackMemo;
	}

	/**
	 * @param cashBackMemo
	 *            the cashBackMemo to set
	 */
	public void setCashBackMemo(String cashBackMemo) {
		this.cashBackMemo = cashBackMemo;
	}

	/**
	 * @return the cashBackAmount
	 */
	public double getCashBackAmount() {
		return cashBackAmount;
	}

	/**
	 * @param cashBackAmount
	 *            the cashBackAmount to set
	 */
	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientMakeDeposit";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.MAKEDEPOSIT;
	}

}
