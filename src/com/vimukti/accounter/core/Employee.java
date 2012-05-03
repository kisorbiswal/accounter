package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * Employee records the relevant information about the employee.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class Employee extends Payee implements PayStructureDestination {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int GENDER_MALE = 1;

	public static final int GENDER_FEMALE = 2;

	/**
	 * Employee belongs to Group
	 */
	private EmployeeGroup group;

	/** General Information */

	/**
	 * Employee Number
	 */
	private String number;

	/**
	 * Designation of the Employee
	 */
	private String designation;

	/**
	 * Location of the Employee
	 */
	private String location;

	/**
	 * Date of Birth of the Employee. And this will be used to find if the
	 * Employee is a senior citizen
	 */
	private FinanceDate dateOfBirth;

	/**
	 * Gender of the Employee
	 */
	private int gender;

	/** Payment Details */

	/** Passport or Visa Details */

	/**
	 * Passport Number of the Employee
	 */
	private String passportNumber;

	/**
	 * Country which is Issuing Passport
	 */
	private String countryOfIssue;

	/**
	 * Expiry Date of the Passport
	 */
	private FinanceDate passportExpiryDate;

	/**
	 * Visa Number of the Employee
	 */
	private String visaNumber;

	/**
	 * Expiry Date of the Visa
	 */
	private FinanceDate visaExpiryDate;

	/** Contact Details */

	/**
	 * @return the group
	 */
	public EmployeeGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(EmployeeGroup group) {
		this.group = group;
	}

	/**
	 * @return the dateOfBirth
	 */
	public FinanceDate getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(FinanceDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param employeeNumber
	 *            the employeeNumber to set
	 */
	public void setNumber(String employeeNumber) {
		this.number = employeeNumber;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the passportNumber
	 */
	public String getPassportNumber() {
		return passportNumber;
	}

	/**
	 * @param passportNumber
	 *            the passportNumber to set
	 */
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	/**
	 * @return the countryOfIssue
	 */
	public String getCountryOfIssue() {
		return countryOfIssue;
	}

	/**
	 * @param countryOfIssue
	 *            the countryOfIssue to set
	 */
	public void setCountryOfIssue(String countryOfIssue) {
		this.countryOfIssue = countryOfIssue;
	}

	/**
	 * @return the passportExpiryDate
	 */
	public FinanceDate getPassportExpiryDate() {
		return passportExpiryDate;
	}

	/**
	 * @param passportExpiryDate
	 *            the passportExpiryDate to set
	 */
	public void setPassportExpiryDate(FinanceDate passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	/**
	 * @return the visaNumber
	 */
	public String getVisaNumber() {
		return visaNumber;
	}

	/**
	 * @param visaNumber
	 *            the visaNumber to set
	 */
	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	/**
	 * @return the visaExpiryDate
	 */
	public FinanceDate getVisaExpiryDate() {
		return visaExpiryDate;
	}

	/**
	 * @param visaExpiryDate
	 *            the visaExpiryDate to set
	 */
	public void setVisaExpiryDate(FinanceDate visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			Session session = HibernateUtil.getCurrentSession();

			Employee employee = (Employee) clientObject;
			Query query = session
					.getNamedQuery("getEmployee.by.Name")
					.setParameter("name", employee.name,
							EncryptedStringType.INSTANCE)
					.setParameter("id", employee.getID())
					.setEntity("company", employee.getCompany());
			List list = query.list();
			if (list != null && list.size() > 0) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
			}
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.employee()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.name(), this.name);
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.EMPLOYEE);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public int getObjType() {
		return IAccounterCore.EMPLOYEE;
	}

	@Override
	public Account getAccount() {
		return getCompany().getSalariesPayableAccount();
	}

	@Override
	protected String getPayeeName() {
		return Global.get().messages().employee();
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(session);
		isOnSaveProccessed = true;
		setType(TYPE_EMPLOYEE);
		return onUpdate(session);
	}
}
