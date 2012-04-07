package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Employee records the relevant information about the employee.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class Employee extends CreatableObject implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int GENDER_MALE = 1;

	public static final int GENDER_FEMALE = 2;

	/**
	 * Name of the Employee
	 */
	private String name;

	/**
	 * Employee belongs to Group
	 */
	private EmployeeGroup group;

	/**
	 * Category of the Employee
	 */
	private EmployeeCategory category;

	/**
	 * Date of Joining of the Employee
	 */
	private FinanceDate dateofJoining;

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

	/**
	 * Address of the Employee
	 */
	private Address address;

	/**
	 * Contact Number of the Employee
	 */
	private String contactNumber;

	/**
	 * Email of the Employee
	 */
	private String email;

	/** Payment Details */

	/**
	 * Bank Name of the Employee
	 */
	private String bankName;

	/**
	 * Bank Account Number of the Employee
	 */
	private String bankAccountNumber;

	private String branch;

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
	 * Contact Details
	 */
	private Contact contactDetail;

	private PayStructure payStructure;

	private List<CustomField> customeFields = new ArrayList<CustomField>();

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
	 * @return the category
	 */
	public EmployeeCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(EmployeeCategory category) {
		this.category = category;
	}

	/**
	 * @return the dateofJoining
	 */
	public FinanceDate getDateofJoining() {
		return dateofJoining;
	}

	/**
	 * @param dateofJoining
	 *            the dateofJoining to set
	 */
	public void setDateofJoining(FinanceDate dateofJoining) {
		this.dateofJoining = dateofJoining;
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
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * @param contactNumber
	 *            the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	/**
	 * @param bankAccountNumber
	 *            the bankAccountNumber to set
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * @param branch
	 *            the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
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

	/**
	 * @return the contactDetail
	 */
	public Contact getContactDetail() {
		return contactDetail;
	}

	/**
	 * @param contactDetail
	 *            the contactDetail to set
	 */
	public void setContactDetail(Contact contactDetail) {
		this.contactDetail = contactDetail;
	}

	/**
	 * @return the payStructure
	 */
	public PayStructure getPayStructure() {
		return payStructure;
	}

	/**
	 * @param payStructure
	 *            the payStructure to set
	 */
	public void setPayStructure(PayStructure payStructure) {
		this.payStructure = payStructure;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the customeFields
	 */
	public List<CustomField> getCustomeFields() {
		return customeFields;
	}

	/**
	 * @param customeFields
	 *            the customeFields to set
	 */
	public void setCustomeFields(List<CustomField> customeFields) {
		this.customeFields = customeFields;
	}
}
