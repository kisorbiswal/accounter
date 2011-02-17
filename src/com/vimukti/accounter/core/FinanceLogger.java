package com.vimukti.accounter.core;

import org.apache.log4j.Logger;

import com.bizantra.server.storage.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.workspace.tool.FinanceTool;

@SuppressWarnings("serial")
public class FinanceLogger implements IAccounterServerCore {

	private static Logger log = Logger.getLogger(FinanceLogger.class);
	private static ThreadLocal<FinanceLogger> cache = new ThreadLocal<FinanceLogger>();

	public FinanceLogger() {
		strBuffer = new StringBuffer();
	}

	private long id;
	private String description;
	private FinanceDate createdDate;
	private String createdBy;
	private String logMessge;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(FinanceDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogMessge() {
		return logMessge;
	}

	public void setLogMessge(String logMessge) {
		this.logMessge = logMessge;
	}

	private StringBuffer strBuffer;

	public void startLogger(IAccounterCore data, String createdBy, int comand) {
		getInstance();
		String cmdType = (comand == FinanceTool.CREATE_NEW_ACTION ? "Creation of "
				: (comand == FinanceTool.UPDATE_ACTION ? "Updation of "
						: (comand == FinanceTool.DELETE_ACTION ? "Deletion of "
								: "")));
		if (data != null)
			this.description = cmdType
					+ Util.getServerEqivalentClass(data.getClass())
							.getSimpleName()
					+ ":"
					+ (data instanceof ClientTransaction ? "Number "
							+ ((ClientTransaction) data).getNumber() : data
							.getName());
		else
			this.description = cmdType;

		log.info("---------------------------------------------" + description
				+ "-------------------------------------------------\n");
		this.createdBy = createdBy;
		this.createdDate = new FinanceDate();
		log(description);
	}

	public void closeLogger() {
		log("Logging is completed");

		log
				.info("--------------------------------------------- End of Logging-------------------------------------------------\n");

		this.setLogMessge(this.strBuffer.toString());

		HibernateUtil.getCurrentSession().save(cache.get());
		cache.set(null);
	}

	public static void log(String description, String... objects) {

		for (int i = 0; i < objects.length; i++) {
			description = description.trim().replace("{" + i + "}", objects[i]);
		}
		getInstance().strBuffer.append("Operation :" + description + "\n");
		log.info(description + "\n");
	}

	public static void log(String description) {
		getInstance().strBuffer.append("Operation :" + description + "\n");
		log.info(description + "\n");
	}

	public static FinanceLogger getInstance() {
		if (cache.get() == null)
			cache.set(new FinanceLogger());
		return cache.get();
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringID(String stringID) {

	}

	@Override
	public void setImported(boolean isImported) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
