/**
 * 
 */
package com.vimukti.accounter.main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ServerAllocatorImpl implements IServerAllocator {

	@Override
	public Server allocateServer(int companyType, String companyName,
			String sourceAddress) {

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = session.beginTransaction();
		try {
			List list = session.getNamedQuery("get.Prioritize.Servers").list();
			if (!list.isEmpty()) {
				return (Server) list.get(0);
			}
			Server server = new Server(
					ServerConfiguration.getMainServerDomain());
			session.save(server);
			transaction.commit();
			return server;
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			session.close();
		}
		return null;
	}

}
