package com.vimukti.accounter.core.change;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ChangeTracker {
	static Logger log = Logger.getLogger(Account.class);

	public static List<IAccounterCore> clientobjects = new ArrayList<IAccounterCore>();

	public static void put(IAccounterServerCore obj) {
		if (obj == null || obj.getID() == 0)
			return;
		IAccounterCore core;
		try {
			core = (IAccounterCore) new ClientConvertUtil().toClientObject(obj,
					Util.getClientEqualentClass(obj.getClass()));
		} catch (AccounterException e) {
			e.printStackTrace();
			return;
		}
		put(core, false);
	}

	public static void put(IAccounterCore obj) {
		put(obj, false);
	}

	public static void put(AccounterCommand obj) {
		if (obj == null)
			return;
		log.info("Put Object:" + obj.toString());
		clientobjects.add(obj);
	}

	public static void init() {
		clientobjects = new ArrayList<IAccounterCore>();
	}

	public static IAccounterCore[] getChanges() {
		return clientobjects.toArray(new IAccounterCore[clientobjects.size()]);
	}

	public static void clearChanges() {
		clientobjects.clear();
	}

	public static void put(IAccounterServerCore serverObject,
			boolean isTransaction) {
		if (serverObject == null)
			return;

		Class<?> d = Util.getClientEqualentClass(serverObject.getClass());
		if (d == null)
			return;
		IAccounterCore core;
		try {
			core = (IAccounterCore) new ClientConvertUtil().toClientObject(
					serverObject, d);
		} catch (AccounterException e) {
			e.printStackTrace();
			return;
		}

		put(core, isTransaction);

	}

	public static void put(IAccounterCore core, boolean isTransaction) {
		if (core == null) {
			return;
		}
		log.info("Put Object:" + core.toString());

		AccounterCommand cmd = new AccounterCommand();
		cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);

		if (!isTransaction)
			cmd.setData(core);

		cmd.setID(core.getID());
		cmd.setObjectType(core.getObjectType());
		Utility.updateClientList(cmd, clientobjects);
	}

}
