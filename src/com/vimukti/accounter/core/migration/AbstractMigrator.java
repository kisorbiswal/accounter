package com.vimukti.accounter.core.migration;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.utils.HibernateUtil;

public abstract class AbstractMigrator implements IMigrator {

	protected Logger log = Logger.getLogger(getClass());

	protected String getNextAccountNumber(long companyId, int subbaseType) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Query query = session.getNamedQuery("getNextAccountNumber")
					.setParameter("companyId", companyId)
					.setParameter("subbaseType", subbaseType);
			List list = query.list();
			Collections.sort(list);
			String maxNumber = (String) list.get(list.size() - 1);
			return NumberUtils.getStringwithIncreamentedDigit(maxNumber);
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

}
