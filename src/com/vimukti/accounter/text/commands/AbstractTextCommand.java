package com.vimukti.accounter.text.commands;

import org.hibernate.Session;

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
	 * Get Finance Tool
	 * 
	 * @return
	 */
	protected FinanceTool getFinanceTool() {
		return new FinanceTool();
	}

}
