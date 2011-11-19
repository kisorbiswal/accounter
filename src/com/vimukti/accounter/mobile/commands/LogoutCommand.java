package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.MobileCookie;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.MobileSession;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.utils.HibernateUtil;

public class LogoutCommand extends Command {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		MobileSession ioSession = context.getIOSession();
		ioSession.setAuthentication(false);
		ioSession.setExpired(true);
		ioSession.removeAllCommands();

		Result result = new Result();
		result.add("You are successfully logged out.");
		result.setCookie("No Cookie");

		MobileCookie mobileCookie = getMobileCookie(context.getNetworkId());
		if (mobileCookie != null) {
			Session hibernateSession = context.getHibernateSession();
			Transaction beginTransaction = hibernateSession.beginTransaction();
			hibernateSession.delete(mobileCookie);
			beginTransaction.commit();
		}
		markDone();
		return result;
	}

	private MobileCookie getMobileCookie(String string) {
		Session session = HibernateUtil.getCurrentSession();
		return (MobileCookie) session.get(MobileCookie.class, string);
	}
}
