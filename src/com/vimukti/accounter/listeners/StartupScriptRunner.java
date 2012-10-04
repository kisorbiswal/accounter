package com.vimukti.accounter.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.utils.HibernateUtil;

public class StartupScriptRunner {

	Logger log = Logger.getLogger(StartupScriptRunner.class);

	public void run() {
		deleteAllIncorrectAutoTransactions();
	}

	@SuppressWarnings("unchecked")
	private void deleteAllIncorrectAutoTransactions() {
		log.info("Started Deleting all incorrect automatic transactions.");
		Session getSession = HibernateUtil.openSession();
		List<Long> transactions = new ArrayList<Long>();
		try {
			Query query = getSession
					.getNamedQuery("get.All.Incorrect.Auto.Transactions");
			transactions = query.list();
		} finally {
			getSession.close();

		}

		for (Long tId : transactions) {
			Session session = HibernateUtil.openSession();
			org.hibernate.Transaction hTransaction = session.beginTransaction();
			try {
				Transaction aTransaction = (Transaction) session.get(
						Transaction.class, tId);
				AccounterThreadLocal.set(aTransaction.getCreatedBy());
				session.delete(aTransaction);
				hTransaction.commit();
			} catch (Exception e) {
				log.error(
						"Error while deleting incorrect automatic transaction :"
								+ tId, e);
				hTransaction.rollback();
			} finally {
				session.close();
			}

		}
		log.info("Completed Deleting all incorrect automatic transactions.");
	}
}
