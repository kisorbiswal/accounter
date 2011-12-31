package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSChalanDetail extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double incomeTaxAmount;
	private double surchangePaidAmount;
	private double educationCessAmount;
	private double interestPaidAmount;
	private double penaltyPaidAmount;
	private double otherAmount;

	private String paymentSection;
	private int paymentMethod;
	private long bankChalanNumber;
	private long checkNumber;
	private String bankBsrCode;

	private boolean bookEntry;
	private long dateTaxPaid;
	private int chalanPeriod;
	private long chalanSerialNumber;
	private int formType;
	private int assesmentYearStart;
	private int assessmentYearEnd;

	private List<TDSTransactionItem> tdsTransactionItems = new ArrayList<TDSTransactionItem>();

	@Override
	public String getName() {
		return "TDSChalanDetail";
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public int getObjType() {
		return IAccounterCore.TDSCHALANDETAIL;
	}

	public Double getIncomeTaxAmount() {
		return incomeTaxAmount;
	}

	public void setIncomeTaxAmount(Double incomeTaxAmount) {
		this.incomeTaxAmount = incomeTaxAmount;
	}

	public Double getSurchangePaidAmount() {
		return surchangePaidAmount;
	}

	public void setSurchangePaidAmount(Double surchangePaidAmount) {
		this.surchangePaidAmount = surchangePaidAmount;
	}

	public Double getEducationCessAmount() {
		return educationCessAmount;
	}

	public void setEducationCessAmount(Double educationCessAmount) {
		this.educationCessAmount = educationCessAmount;
	}

	public Double getInterestPaidAmount() {
		return interestPaidAmount;
	}

	public void setInterestPaidAmount(Double interestPaidAmount) {
		this.interestPaidAmount = interestPaidAmount;
	}

	public Double getPenaltyPaidAmount() {
		return penaltyPaidAmount;
	}

	public void setPenaltyPaidAmount(Double penaltyPaidAmount) {
		this.penaltyPaidAmount = penaltyPaidAmount;
	}

	public Double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}

	public int getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Long getBankChalanNumber() {
		return bankChalanNumber;
	}

	public void setBankChalanNumber(Long bankChalanNumber) {
		this.bankChalanNumber = bankChalanNumber;
	}

	public Long getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(Long checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getBankBsrCode() {
		return bankBsrCode;
	}

	public void setBankBsrCode(String bankBsrCode) {
		this.bankBsrCode = bankBsrCode;
	}

	public boolean isBookEntry() {
		return bookEntry;
	}

	public void setBookEntry(boolean bookEntry) {
		this.bookEntry = bookEntry;
	}

	public Long getDateTaxPaid() {
		return dateTaxPaid;
	}

	public void setDateTaxPaid(Long dateTaxPaid) {
		this.dateTaxPaid = dateTaxPaid;
	}

	public int getChalanPeriod() {
		return chalanPeriod;
	}

	public void setChalanPeriod(int chalanPeriod) {
		this.chalanPeriod = chalanPeriod;
	}

	public Long getChalanSerialNumber() {
		return chalanSerialNumber;
	}

	public void setChalanSerialNumber(Long chalanSerialNumber) {
		this.chalanSerialNumber = chalanSerialNumber;
	}

	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public int getAssesmentYearStart() {
		return assesmentYearStart;
	}

	public void setAssesmentYearStart(int assesmentYearStart) {
		this.assesmentYearStart = assesmentYearStart;
	}

	public int getAssessmentYearEnd() {
		return assessmentYearEnd;
	}

	public void setAssessmentYearEnd(int assessmentYearEnd) {
		this.assessmentYearEnd = assessmentYearEnd;
	}

	public List<TDSTransactionItem> getTdsTransactionItems() {
		return tdsTransactionItems;
	}

	public void setTdsTransactionItems(List<TDSTransactionItem> list) {
		this.tdsTransactionItems = list;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		for (TDSTransactionItem item : tdsTransactionItems) {
			item.setCompany(getCompany());
		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		for (TDSTransactionItem item : tdsTransactionItems) {
			item.setCompany(getCompany());
		}
		return super.onUpdate(session);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

}
