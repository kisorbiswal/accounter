package com.vimukti.accounter.main;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.mail.EmailManager;
import com.vimukti.accounter.utils.HibernateUtil;

public class LiveServer extends Server {

	private static Logger LOG = Logger.getLogger(LiveServer.class);
	/**
	 * Key = workspaceID, value=Set of Companies
	 */

	private HashSet<String> companies = new HashSet<String>();

	public LiveServer() {
		super();
		server = this;
	}

	public LiveServer(boolean create) throws Exception {
		super(create);
		server = this;
	}

	@Override
	public void startServer() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	protected void afterStartBasicConfigurations() {
		super.afterStartBasicConfigurations();
		EmailManager manager = EmailManager.getInstance();
		manager.run();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		EmailManager manager = EmailManager.getInstance();
		manager.stopThread();
	}

	@SuppressWarnings("unchecked")
	public static Collection<String> getAllCompanies() {
		Session bizantraDatabaseSession = HibernateUtil.openSession(
				Server.LOCAL_DATABASE, false);

		if (bizantraDatabaseSession != null) {
			List<String> companyList = bizantraDatabaseSession.getNamedQuery(
					"get.allcompany").list();
			return companyList;
		}
		return new HashSet<String>();
	}

	public boolean isCompanyExists(String companyName) {
		return this.companies.contains(companyName);
	}

	public static LiveServer getInstance() {
		try {
			return (LiveServer) server;
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}

	public String getServerForCompanyName(String companyName) {
		try {
			return InetAddress.getByName(companyName + ".bizantra.com")
					.getHostAddress();
		} catch (Exception e) {
			LOG.error("Unable to resolve domain name", e);
		}
		return null;
	}

	public void addCompany(String companyName) {
		this.companies.add(companyName);
	}
}
