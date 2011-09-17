package com.vimukti.accounter.core;

import org.hibernate.classic.Lifecycle;

public class PayTDS extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3752025091243352402L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;
	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	
	
	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}
}
