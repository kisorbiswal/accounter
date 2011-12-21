package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientTDSChalanDetail implements IAccounterCore {

	/**
	 * this class is used to maintain the details of chalan
	 */
	private static final long serialVersionUID = 1L;
	private int version;
	private long id;

	private double incomeTaxAmount;
	private double surchangePaidAmount;
	private double educationCessAmount;
	private double interestPaidAmount;
	private double penaltyPaidAmount;
	private double otherAmount;

	private int paymentSection;
	private int paymentMethod;
	private long bankChalanNumber;
	private long checkNumber;
	private long bsrCode;

	private List<ClientTDSTransactionItem> budgetItems = new ArrayList<ClientTDSTransactionItem>();

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "ClientTDSChalanDetail";
	}

	@Override
	public String getDisplayName() {
		return "ClientTDSChalanDetail";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TDSCHALANDETAIL;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientTDSChalanDetail";
	}

	public double getIncomeTaxAmount() {
		return incomeTaxAmount;
	}

	public void setIncomeTaxAmount(double incomeTaxAmount) {
		this.incomeTaxAmount = incomeTaxAmount;
	}

	public double getSurchangePaidAmount() {
		return surchangePaidAmount;
	}

	public void setSurchangePaidAmount(double surchangePaidAmount) {
		this.surchangePaidAmount = surchangePaidAmount;
	}

	public double getEducationCessAmount() {
		return educationCessAmount;
	}

	public void setEducationCessAmount(double educationCessAmount) {
		this.educationCessAmount = educationCessAmount;
	}

	public double getInterestPaidAmount() {
		return interestPaidAmount;
	}

	public void setInterestPaidAmount(double interestPaidAmount) {
		this.interestPaidAmount = interestPaidAmount;
	}

	public double getPenaltyPaidAmount() {
		return penaltyPaidAmount;
	}

	public void setPenaltyPaidAmount(double penaltyPaidAmount) {
		this.penaltyPaidAmount = penaltyPaidAmount;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public int getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(int paymentSection) {
		this.paymentSection = paymentSection;
	}

	public int getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public long getBankChalanNumber() {
		return bankChalanNumber;
	}

	public void setBankChalanNumber(long bankChalanNumber) {
		this.bankChalanNumber = bankChalanNumber;
	}

	public long getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(long checkNumber) {
		this.checkNumber = checkNumber;
	}

	public long getBsrCode() {
		return bsrCode;
	}

	public void setBsrCode(long bsrCode) {
		this.bsrCode = bsrCode;
	}

	public List<ClientTDSTransactionItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<ClientTDSTransactionItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

}
