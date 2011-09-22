package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

/**
 * A VATGroup is a sub class of VATItemGroup. It consists of list of VATItems
 * and the groupRate to indicate the average of the vat rates of the list
 * 
 * @author Chandan
 */
public class TAXGroup extends TAXItemGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055386258774310462L;
	private Company company;
	/**
	 * VAT group consists of a list of VAT Items. {@link TAXItem}
	 */
	List<TAXItem> taxItems;

	double groupRate;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the groupRate
	 */
	public double getGroupRate() {
		return groupRate;
	}

	/**
	 * @param groupRate
	 *            the groupRate to set
	 */
	public void setGroupRate(double groupRate) {
		this.groupRate = groupRate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the vatItems
	 */
	public List<TAXItem> getTAXItems() {
		return taxItems;
	}

	/**
	 * @param vatItems
	 *            the vatItems to set
	 */
	public void setTAXItems(List<TAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (Company.getCompany() != null
				&& Company.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = new TAXCode((TAXItemGroup) this);
			session.saveOrUpdate(taxCode);
		}
		super.onSave(session);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (Company.getCompany() != null
				&& Company.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {

			Query query = session.getNamedQuery("getTaxCode.by.id")
					.setParameter("id", this.id);
			TAXCode taxCode = (TAXCode) query.uniqueResult();
			if (taxCode != null) {

				taxCode.setName(this.getName());
				taxCode.setDescription(this.getDescription());
				taxCode.setActive(this.isActive());
				session.saveOrUpdate(taxCode);
			}
			this.isSalesType = true;
		}
		super.onSave(session);
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		FinanceLogger.log("Vat Group with name: {0} has been deleted",
				this.getName());

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.TAX_GROUP);
		ChangeTracker.put(accounterCore);

		return super.onDelete(arg0);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
