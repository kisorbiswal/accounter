package com.vimukti.accounter.core.migration;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MigrationUtil {

	public static void migrate(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			for (int version = company.getVersion() + 1; version <= Company.CURRENT_VERSION; version++) {
				IMigrator migrator = (IMigrator) Class.forName(
						"com.vimukti.accounter.core.migration.Migrator"
								+ version).newInstance();
				migrator.migrate(company);
			}
			company.setVersion(Company.CURRENT_VERSION);
			session.save(company);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

	}

}
