package com.vimukti.accounter.core.migration;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.utils.HibernateUtil;

public abstract class AbstractMigrator implements IMigrator {

	protected Logger log = Logger.getLogger(getClass());

	protected String getNextAccountNumber(long companyId, int accountType) {
		return NumberUtils.getNextAccountNumber(companyId, accountType);
	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

}
