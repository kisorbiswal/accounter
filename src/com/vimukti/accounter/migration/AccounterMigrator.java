package com.vimukti.accounter.migration;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.utils.HibernateUtil;

public class AccounterMigrator {

	protected Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public void migrate() throws IOException {
		try {
			Session session = HibernateUtil.openSession();
			Query query = session
					.createSQLQuery("SELECT id from company order by id ");
			List<BigInteger> ids = query.list();
			List<String> emails = new ArrayList<String>();
			PickListTypeContext typeContext = new PickListTypeContext();
			for (BigInteger id : ids) {
				Company company = (Company) session.load(Company.class,
						Long.valueOf(id.longValue()));
				CompanyMigrator migrator = new CompanyMigrator(company);
				migrator.migrate(emails, typeContext);
			}
			session.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
