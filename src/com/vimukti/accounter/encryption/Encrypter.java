package com.vimukti.accounter.encryption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public class Encrypter extends Thread {
	private Logger log = Logger.getLogger(Encrypter.class);

	private long companyId;
	private byte[] csk;
	private byte[] d2;
	private byte[] s2;
	private String sessionId;
	private Object emailId;
	private static List<String> classes;

	static {
		classes = new ArrayList<String>();
		File core = new File("src/com/vimukti/accounter/core");
		String[] list = core.list();
		Class<?> create = null;
		try {
			create = Class
					.forName("com.vimukti.accounter.core.CreatableObject");
		} catch (Exception e) {
		}

		List<String> skip = new ArrayList<String>();
		String[] skips = "Currency,Unit,Measurement,Payee".split(",");
		for (String s : skips) {
			skip.add(s);
		}
		for (String string : list) {
			if (string.endsWith(".java")) {
				string = string.substring(0, string.indexOf(".java"));
			} else {
				continue;
			}
			try {
				Class<?> clazz = Class.forName("com.vimukti.accounter.core."
						+ string);
				if (create.isAssignableFrom(clazz)) {
					if (!skip.contains(string)) {
						classes.add(string);
					}
				} else {
					// System.out.println(string);
				}
			} catch (Exception e) {
			}
		}
	}

	public Encrypter(long companyId, String password, byte[] d2,
			String emailId, String sessionId) throws Exception {
		this.companyId = companyId;
		csk = EU.generatePBS(password);
		this.d2 = d2;
		this.emailId = emailId;
		this.sessionId = sessionId;
		s2 = EU.getKey(sessionId);
	}

	@Override
	public void run() {
		try {
			List<List> lists = loadCoreObjects();
			updateAll(lists);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateAll(List<List> lists) {
		Session session = HibernateUtil.openSession();
		AccounterThreadLocal.set(getUser());
		try {
			Transaction beginTransaction = session.beginTransaction();
			for (List l : lists) {
				for (Object o : l) {
					session.replicate(o, ReplicationMode.OVERWRITE);
				}
			}
			initChipher();
			Company company = getCompany();
			company.setLocked(false);
			session.saveOrUpdate(company);
			beginTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private void initChipher() throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany();
		byte[] s3 = EU.generateSymetric();
		byte[] companySecret = EU.encrypt(s3, csk);

		String string = SecureUtils.createID(16);
		byte[] prk = EU.generatePBS(string);

		byte[] encryptedPass = EU.encrypt(csk, prk);
		company.setEncryptedPassword(encryptedPass);
		company.setSecretKey(companySecret);
		session.saveOrUpdate(company);

		byte[] userSecret = EU.encrypt(s3, EU.decrypt(d2, s2));
		EU.storeKey(s2, sessionId);
		EU.createCipher(userSecret, d2, sessionId);
		User user = getUser();
		user.setSecretKey(userSecret);
		session.saveOrUpdate(user);

		log.info("Company password recovery key---" + string);
		sendCompanyPasswordRecoveryKey(string);
	}

	public static void sendCompanyPasswordRecoveryKey(String string) {
		// TODO
	}

	private User getUser() {
		Session session = HibernateUtil.getCurrentSession();
		Query setParameter = session
				.getNamedQuery("getUser.by.mailId.and.companyId")
				.setParameter("emailId", emailId)
				.setParameter("companyId", companyId);
		return (User) setParameter.uniqueResult();
	}

	private Company getCompany() {
		Session session = HibernateUtil.getCurrentSession();
		Object object = session.get(Company.class, companyId);
		return (Company) object;
	}

	private List<List> loadCoreObjects() throws Exception {

		Session session = HibernateUtil.openSession();
		try {
			Company company = getCompany();
			log.info("Encrypting is started (" + company.getTradingName() + ")");
			List<List> lists = new ArrayList<List>();
			// classes.clear();
			// classes.add("FixedAsset");
			for (String cls : classes) {
				Criteria criteria = session.createCriteria(Class
						.forName("com.vimukti.accounter.core." + cls));
				criteria.add(Restrictions.eq("company", company));
				try {
					List list = criteria.list();
					List list2 = new ArrayList();
					for (Object o : list) {
						Object initializeAndUnproxy = HibernateUtil
								.initializeAndUnproxy(o);
						list2.add(initializeAndUnproxy);
					}
					lists.add(list2);
				} catch (Exception e) {
					System.out.println(cls);
				}
			}
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void sendCompanyPasswordRecoveryKey(Client client, String key) {

		try {
			UsersMailSendar.sendMailToUserWithPasswordRecoverKey(client, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
