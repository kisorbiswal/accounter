package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSChalanDetail extends Transaction implements
		IAccounterServerCore {

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
	private Account payFrom;
	private String etdsfillingAcknowledgementNo;

	private List<TDSTransactionItem> tdsTransactionItems = new ArrayList<TDSTransactionItem>();

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
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		if (!isDraftOrTemplate()) {
			Account account = getTDSTaxAgencyAccount();
			if (account != null) {
				account.updateCurrentBalance(this, -total, currencyFactor);
				session.saveOrUpdate(account);
				account.onUpdate(session);
			}
		}
		return false;
	}

	private Account getTDSTaxAgencyAccount() {
		for (TDSTransactionItem item : tdsTransactionItems) {
			if (item.getTransaction() instanceof PayBill) {
				PayBill paybill = (PayBill) item.getTransaction();
				if (paybill.getTdsTaxItem() != null) {
					TAXAgency taxAgency = paybill.getTdsTaxItem()
							.getTaxAgency();
					return taxAgency.getPurchaseLiabilityAccount();
				}
			}
		}
		return null;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		super.onDelete(session);
		if (!isVoid() && !isTemplate()) {
			Account account = getTDSTaxAgencyAccount();
			if (account != null) {
				account.updateCurrentBalance(this, total, currencyFactor);
				session.saveOrUpdate(account);
				account.onUpdate(session);
			}
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
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

	public Account getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public Account getEffectingAccount() {
		return this.payFrom;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_TDS_CHALLAN;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEtdsfillingAcknowledgementNo() {
		return etdsfillingAcknowledgementNo;
	}

	public void setEtdsfillingAcknowledgementNo(
			String etdsfillingAcknowledgementNo) {
		this.etdsfillingAcknowledgementNo = etdsfillingAcknowledgementNo;
	}

}
