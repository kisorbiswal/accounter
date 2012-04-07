package com.vimukti.accounter.web.client.core;

public class ClientEmployee implements IAccounterCore {

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
	private ClientEmployeeGroup group;

	/**
	 * Category of the Employee
	 */
	private ClientEmployeeCategory category;

	/**
	 * Date of Joining of the Employee
	 */
	private ClientFinanceDate dateofJoining;

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
	private ClientFinanceDate dateOfBirth;

	/**
	 * Gender of the Employee
	 */
	private int gender;

	/**
	 * Address of the Employee
	 */
	private ClientAddress address;

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
	private ClientFinanceDate passportExpiryDate;

	/**
	 * Visa Number of the Employee
	 */
	private String visaNumber;

	/**
	 * Expiry Date of the Visa
	 */
	private ClientFinanceDate visaExpiryDate;

	/** Contact Details */

	/**
	 * Contact Details
	 */
	private ClientContact contactDetail;

	private ClientPayStructure payStructure;

	public ClientEmployeeGroup getGroup() {
		return group;
	}

	public void setGroup(ClientEmployeeGroup group) {
		this.group = group;
	}

	public ClientEmployeeCategory getCategory() {
		return category;
	}

	public void setCategory(ClientEmployeeCategory category) {
		this.category = category;
	}

	public ClientFinanceDate getDateofJoining() {
		return dateofJoining;
	}

	public void setDateofJoining(ClientFinanceDate dateofJoining) {
		this.dateofJoining = dateofJoining;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ClientFinanceDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(ClientFinanceDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getCountryOfIssue() {
		return countryOfIssue;
	}

	public void setCountryOfIssue(String countryOfIssue) {
		this.countryOfIssue = countryOfIssue;
	}

	public ClientFinanceDate getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(ClientFinanceDate passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public ClientFinanceDate getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(ClientFinanceDate visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public ClientContact getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(ClientContact contactDetail) {
		this.contactDetail = contactDetail;
	}

	public ClientPayStructure getPayStructure() {
		return payStructure;
	}

	public void setPayStructure(ClientPayStructure payStructure) {
		this.payStructure = payStructure;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

}
