package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

public class SalesPerson extends Payee implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7140827426888772170L;
	public static final int TITLE_DR = 1;
	public static final int TITLE_MISS = 2;
	public static final int TITLE_MR = 3;
	public static final int TITLE_MRS = 4;

	public static final int SUFFIX_I = 1;
	public static final int SUFFIX_II = 2;
	public static final int SUFFIX_III = 3;
	public static final int SUFFIX_JR = 4;
	public static final int SUFFIX_SR = 5;

	public static final int GENDER_UNSPECIFIED = 1;
	public static final int GENDER_MALE = 2;
	public static final int GENDER_FEMALE = 3;

	/**
	 * This field stores the title, of the SalesPerson
	 */
	String title;

	/**
	 * This field, stores the First Name of the SalesPerson
	 */
	String firstName;

	/**
	 * This field Stores the Middle Name of the SalesPerson
	 */
	String middleName1;
	String middleName2;
	String middleName3;

	/**
	 * This field Stores the Last Name of the SalesPerson
	 */
	String lastName;

	/**
	 * This field stores the Suffix, of the SalesPerson
	 */
	String suffix;

	/**
	 * This field, stores the Job Title for this SalesPerson
	 */
	String jobTitle;

	/**
	 * Expense {@link Account} for this SalesPerson
	 */
	Account expenseAccount;

	/**
	 * Gender type of the SalesPerson <br>
	 * 1 = UnSpecified, <br>
	 * 2 = Male, <br>
	 * 3 = Female
	 */
	String gender;

	/**
	 * Date of Birth of the Sales Person
	 */
	FinanceDate dateOfBirth;

	/**
	 * Date of Hire of this SalesPerson
	 */
	FinanceDate dateOfHire;

	/**
	 * Date of Last Review
	 */
	FinanceDate dateOfLastReview;

	/**
	 * Date of Release
	 */
	FinanceDate dateOfRelease;

	String createdBy;
	String lastModifier;

	// String createdDate;
	// String lastModifiedDate;

	public SalesPerson() {
		setType(Payee.TYPE_EMPLOYEE);
	}

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the middleName1
	 */
	public String getMiddleName1() {
		return middleName1;
	}

	/**
	 * @return the middleName2
	 */
	public String getMiddleName2() {
		return middleName2;
	}

	/**
	 * @return the middleName3
	 */
	public String getMiddleName3() {
		return middleName3;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @return the address
	 */
	@Override
	public Set<Address> getAddress() {
		return address;
	}

	/**
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the dateOfBirth
	 */
	public FinanceDate getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @return the dateOfHire
	 */
	public FinanceDate getDateOfHire() {
		return dateOfHire;
	}

	/**
	 * @return the dateOfLastReview
	 */
	public FinanceDate getFinanceDateOfLastReview() {
		return dateOfLastReview;
	}

	/**
	 * @return the dateOfRelease
	 */
	public FinanceDate getDateOfRelease() {
		return dateOfRelease;
	}

	@Override
	public Account getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.SALES_PERSON);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		SalesPerson salesPerson = (SalesPerson) clientObject;
		Query query = session.createQuery(
				"from com.vimukti.accounter.core.SalesPerson S where S.name=?")
				.setParameter(0, salesPerson.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			SalesPerson newSalesPerson = (SalesPerson) list.get(0);
			if (salesPerson.id != newSalesPerson.id) {
				throw new InvalidOperationException(
						"A SalesPerson already exists wiht this name");
			}
		}
		return true;
	}

}
