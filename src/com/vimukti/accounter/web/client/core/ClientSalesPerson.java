package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientSalesPerson extends ClientPayee {

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

	String title;
	String firstName;
	String middleName1;
	String middleName2;
	String middleName3;
	String lastName;
	String suffix;

	String jobTitle;

	long expenseAccount;

	String gender;
	long dateOfBirth;
	long dateOfHire;
	long dateOfLastReview;
	long dateOfRelease;

	public ClientSalesPerson() {
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName1
	 */
	public String getMiddleName1() {
		return middleName1;
	}

	/**
	 * @param middleName1
	 *            the middleName1 to set
	 */
	public void setMiddleName1(String middleName1) {
		this.middleName1 = middleName1;
	}

	/**
	 * @return the middleName2
	 */
	public String getMiddleName2() {
		return middleName2;
	}

	/**
	 * @param middleName2
	 *            the middleName2 to set
	 */
	public void setMiddleName2(String middleName2) {
		this.middleName2 = middleName2;
	}

	/**
	 * @return the middleName3
	 */
	public String getMiddleName3() {
		return middleName3;
	}

	/**
	 * @param middleName3
	 *            the middleName3 to set
	 */
	public void setMiddleName3(String middleName3) {
		this.middleName3 = middleName3;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the expenseAccount
	 */

	/**
	 * @param expenseAccount
	 *            the expenseAccount to set
	 */
	public void setExpenseAccount(long expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	@Override
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the isActive
	 */
	@Override
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the dateOfBirth
	 */
	public long getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the dateOfHire
	 */
	public long getDateOfHire() {
		return dateOfHire;
	}

	/**
	 * @param dateOfHire
	 *            the dateOfHire to set
	 */
	public void setDateOfHire(long dateOfHire) {
		this.dateOfHire = dateOfHire;
	}

	/**
	 * @return the dateOfLastReview
	 */
	public long getDateOfLastReview() {
		return dateOfLastReview;
	}

	/**
	 * @param dateOfLastReview
	 *            the dateOfLastReview to set
	 */
	public void setDateOfLastReview(long dateOfLastReview) {
		this.dateOfLastReview = dateOfLastReview;
	}

	/**
	 * @return the dateOfRelease
	 */
	public long getDateOfRelease() {
		return dateOfRelease;
	}

	/**
	 * @param dateOfRelease
	 *            the dateOfRelease to set
	 */
	public void setDateOfRelease(long dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}

	@Override
	public String getName() {

		return firstName;
	}

	public long getExpenseAccount() {
		return this.expenseAccount;
	}

	public void setDateOfHire(ClientFinanceDate date) {
		this.dateOfHire = date.getTime();
	}

	public void setDateOfBirth(ClientFinanceDate date) {
		this.dateOfBirth = date.getTime();
	}

	public void setDateOfLastReview(ClientFinanceDate date) {
		this.dateOfLastReview = date.getTime();
	}

	public void setDateOfRelease(ClientFinanceDate date) {
		this.dateOfRelease = date.getTime();
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.SALES_PERSON;
	}

	@Override
	public String getDisplayName() {

		StringBuffer buffer = new StringBuffer();

		buffer.append(String.valueOf(firstName));

		if (jobTitle != null) {
			buffer.append("( " + String.valueOf(jobTitle) + " )");
		}

		return buffer.toString();
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return "ClientSalesPerson";
	}

}
