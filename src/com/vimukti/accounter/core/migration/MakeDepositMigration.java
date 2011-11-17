package com.vimukti.accounter.core.migration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.utils.HibernateUtil;

public class MakeDepositMigration {

	public void start() {
		System.out.println("Starting MakeDeposit Migration");
		Session session = HibernateUtil.openSession();
		Transaction hibernateTransaction = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(MakeDeposit.class);
			List<MakeDeposit> list = criteria.list();
			for (MakeDeposit md : list) {
				List<TransactionMakeDeposit> tmdList = md
						.getTransactionMakeDeposit();
				if (tmdList == null || tmdList.isEmpty()) {
					continue;
				}
				if (tmdList.size() == 1) {
					md.setDepositFrom(tmdList.get(0).getAccount());
					session.saveOrUpdate(md);
				} else {
					TransactionMakeDeposit tmd1 = tmdList.get(0);
					double amount = md.getTotal() - tmd1.getAmount();
					md.setTotal(tmd1.getAmount());
					md.setDepositFrom(tmd1.getAccount());
					Account depositIn = md.getDepositIn();
					depositIn.updateTotalBalance(-amount, 1);
					depositIn.setCurrenctBalance(-amount);
					TransactionMakeDeposit tmd2 = tmdList.get(1);
					MakeDeposit newMD = createNewMakeDeposit(md, tmd2);
					session.save(newMD);
					Set<AccountTransaction> listAT = md
							.getAccountTransactionEntriesList();
					Set<AccountTransaction> atToDelete = new HashSet<AccountTransaction>();
					for (AccountTransaction at : listAT) {
						Account account = at.getAccount();
						if (account.getID() == md.getDepositIn().getID()) {
							at.setAmount(tmd1.getAmount());
							session.saveOrUpdate(at);
						} else if (account.getID() == tmd2.getAccount().getID()) {
							atToDelete.add(at);
						}
					}
					for (AccountTransaction at : atToDelete) {
						listAT.remove(at);
						session.delete(at);
						Account account = at.getAccount();
						account.updateTotalBalance(-at.getAmount(), 1);
						account.setCurrenctBalance(-at.getAmount());
					}
					session.saveOrUpdate(md);
				}
			}
			hibernateTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			hibernateTransaction.rollback();
		} finally {
			session.close();
		}
		System.out.println("MakeDeposit Migration Completed");
	}

	private MakeDeposit createNewMakeDeposit(MakeDeposit md,
			TransactionMakeDeposit tmd2) {
		AccounterThreadLocal.set(md.getCreatedBy());
		MakeDeposit newMD = new MakeDeposit();
		newMD.setCreatedBy(md.getCreatedBy());
		newMD.setLastModifier(md.getLastModifier());
		newMD.setCreatedDate(md.getCreatedDate());
		newMD.setLastModifiedDate(md.getLastModifiedDate());
		newMD.setCanVoidOrEdit(md.getCanVoidOrEdit());
		newMD.setCashBackMemo(md.getCashBackMemo());
		newMD.setCurrency(md.getCurrency());
		newMD.setCurrencyFactor(md.getCurrencyFactor());
		newMD.setIsDeposited(md.getIsDeposited());
		newMD.setLocation(md.getLocation());
		newMD.setMemo(md.getMemo());
		newMD.setTotal(tmd2.getAmount());
		newMD.setDepositIn(md.getDepositIn());
		newMD.setDepositFrom(tmd2.getAccount());
		newMD.setDate(md.getDate());
		newMD.setCompany(md.getCompany());
		return newMD;
	}

}
