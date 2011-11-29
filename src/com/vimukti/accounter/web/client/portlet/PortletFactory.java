package com.vimukti.accounter.web.client.portlet;

import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.ui.BankAccountsPortlet;
import com.vimukti.accounter.web.client.ui.ExpenseClaimPortlet;
import com.vimukti.accounter.web.client.ui.MessagesAndTasksPortlet;
import com.vimukti.accounter.web.client.ui.MoneyComingPortlet;
import com.vimukti.accounter.web.client.ui.MoneyGoingPortlet;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.QuickLinksPortlet;
import com.vimukti.accounter.web.client.ui.RecentTransactionsPortlet;
import com.vimukti.accounter.web.client.ui.WhoIOwePortlet;
import com.vimukti.accounter.web.client.ui.WhoOwesMePortlet;

public class PortletFactory {

	private static PortletFactory factory;

	public static PortletFactory get() {
		if (factory == null) {
			factory = new PortletFactory();
		}
		return factory;
	}

	public Portlet createPortlet(ClientPortletConfiguration pc,
			PortletPage portletPage) {
		String name = pc.getName();
		Portlet portlet = null;
		if (name.equals(Portlet.BANK_ACCOUNT)) {
			portlet = new BankAccountsPortlet(pc);
		} else if (name.equals(Portlet.MONEY_COMING)) {
			portlet = new MoneyComingPortlet(pc);
		} else if (name.equals(Portlet.MONEY_GOING)) {
			portlet = new MoneyGoingPortlet(pc);
		} else if (name.equals(Portlet.EXPENSES_CLAIM)) {
			portlet = new ExpenseClaimPortlet(pc);
		} else if (name.equals(Portlet.WHO_I_OWE)) {
			portlet = new WhoIOwePortlet(pc);
		} else if (name.equals(Portlet.WHO_OWES_ME)) {
			portlet = new WhoOwesMePortlet(pc);
		} else if (name.equals(Portlet.RECENT_TRANSACTIONS)) {
			portlet = new RecentTransactionsPortlet(pc);
		} else if (name.equals(Portlet.QUICK_LINKS)) {
			portlet = new QuickLinksPortlet(pc);
		} else if (name.equals(Portlet.MESSAGES_AND_TASKS)) {
			portlet = new MessagesAndTasksPortlet(pc);
		}
		return portlet;
	}

	public ClientPortletPageConfiguration getDefaultConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			return new ClientPortletPageConfiguration(2, new String[][] {
					{ Portlet.BANK_ACCOUNT, Portlet.EXPENSES_CLAIM },
					{ Portlet.MONEY_COMING, Portlet.MONEY_GOING } });
		}
		return null;
	}

	public ClientPortletPageConfiguration getPrefferedConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			return new ClientPortletPageConfiguration(2, new String[][] {
					{ Portlet.BANK_ACCOUNT, Portlet.EXPENSES_CLAIM,
							Portlet.WHO_I_OWE, Portlet.RECENT_TRANSACTIONS,
							Portlet.MESSAGES_AND_TASKS },
					{ Portlet.MONEY_COMING, Portlet.MONEY_GOING,
							Portlet.WHO_OWES_ME, Portlet.QUICK_LINKS } });
		}
		return null;
	}
}
