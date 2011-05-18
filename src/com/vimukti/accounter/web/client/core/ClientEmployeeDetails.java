package com.vimukti.accounter.web.client.core;

public class ClientEmployeeDetails implements IAccounterCore {

	/**
	 * EMPLOYEE DETAILS VARIABLES
	 */
	private static final long serialVersionUID = 1L;
	public static final String PAST_EMPLOYEE = null;
	public static final String FUTURE_EMPLOYEE = null;
	public static final String CURRENT_EMPLOYEE = null;
	private String stringID;
	private String name;
	
	private boolean isActive;

	private String employeeNumber;
	private String employeeName;
	private String firstName;
	private String lastName;
	private String title;
	private String jobTitle;
	private String managerName;
	private String employeeType;
	private ClientFinanceDate startDate;
	private ClientFinanceDate endDate;
	private String employeeStatus;
	public String natureOfJob; // (ex: full time)

	/**
	 * EMPLOYEE CONTACT DETAILS VARIABLES
	 */
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String postalCode;
	private String homePhone;
	private String mobilePhone;
	private String homeMailID;

	/**
	 * EMPLOYEE DETAILS METHODS
	 */
	@Override
	public String getClientClassSimpleName() {
		return "Client Employee";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE;
	}

	@Override
	public String getStringID() {
		return stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param managerName
	 *            the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}

	/**
	 * @param employeeNumber
	 *            the employeeNumber to set
	 */
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param employeeType
	 *            the employeeType to set
	 */
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	/**
	 * @return the employeeType
	 */
	public String getEmployeeType() {
		return employeeType;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the startDate
	 */
	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param employeeStatus
	 *            the employeeStatus to set
	 */
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	/**
	 * @return the employeeStatus
	 */
	public String getEmployeeStatus() {
		return employeeStatus;
	}

	/**
	 * @param natureOfJob
	 *            the natureOfJob to set
	 */
	public void setNatureOfJob(String natureOfJob) {
		this.natureOfJob = natureOfJob;
	}

	/**
	 * @return the natureOfJob
	 */
	public String getNatureOfJob() {
		return natureOfJob;
	}

	/**
	 * EMPLOYEE CONTACT DETAILS METHODS
	 */

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param homePhone
	 *            the homePhone to set
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	/**
	 * @return the homePhone
	 */
	public String getHomePhone() {
		return homePhone;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param mobilePhone
	 *            the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param homeMailID
	 *            the homeMailID to set
	 */
	public void setHomeMailID(String homeMailID) {
		this.homeMailID = homeMailID;
	}

	/**
	 * @return the homeMailID
	 */
	public String getHomeMailID() {
		return homeMailID;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

}
