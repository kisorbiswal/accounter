package com.vimukti.accounter.text.commands;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class AbstractTextCommand implements ITextCommand {

	protected Company getCompany() {
		return AccounterThreadLocal.getCompany();
	}

	protected User getUser() {
		return AccounterThreadLocal.get();
	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	/**
	 * Getting the user Email Id
	 * 
	 * @return {@link String} Email ID
	 */
	protected String getUserEmailID() {
		return getUser().getClient().getEmailId();

	}

	/**
	 * Get Finance Tool
	 * 
	 * @return
	 */
	protected FinanceTool getFinanceTool() {
		return new FinanceTool();
	}

	/**
	 * Load the Object
	 * 
	 * @param cls
	 * @param uniqueField
	 * @param value
	 * @return
	 */
	protected <T> T getObject(Class<?> cls, String uniqueField, Object value) {
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(
				cls);
		criteria.add(Restrictions.eq("company", getCompany()));
		criteria.add(Restrictions.eq(uniqueField, value));
		return (T) criteria.uniqueResult();
	}
}
