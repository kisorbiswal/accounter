package com.vimukti.accounter.core.reports;


import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;

public class TrialBalanceReport extends AbstractReport<TrialBalance> {

	private Date endDate;
	private Date startDate;
	private TrialBalance trialBalance;

	public TrialBalanceReport(Session session, Date startDate, Date endDate) {
		super(session);
		this.startDate = startDate;
		this.endDate = endDate;
		run();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Query execute() {
		return session
				.createQuery(
						"from AccountTransaction at where at.transaction.transactionDate between :startDate and :endDate order by at.account.number")
				.setParameter("startDate", startDate).setParameter("endDate",
						endDate).setReadOnly(true);
	}

	@Override
	protected TrialBalance process(Object obj) {
		AccountTransaction at = (AccountTransaction) obj;
		// Check if it for the previous account
		if(trialBalance!=null && trialBalance.getAccountId()==at.getAccount().getID()){
			// then update that amount and continue
			trialBalance.addAmount(at.getAmount());
			return null;
		}
		trialBalance=new TrialBalance();
		//TODO assign all other fields
		trialBalance.setIsIncrease(at.getAccount().isIncrease());
		trialBalance.setAccountId(at.getAccount().getID());
		trialBalance.setAccountName(at.getAccount().getName());
		trialBalance.setAccountNumber(at.getAccount().getNumber());
		trialBalance.addAmount(at.getAmount());
		
		return trialBalance;
	}

}
