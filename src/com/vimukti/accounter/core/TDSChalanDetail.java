package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSChalanDetail extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double incomeTaxAmount;
	private Double surchangePaidAmount;
	private Double educationCessAmount;
	private Double interestPaidAmount;
	private Double penaltyPaidAmount;
	private Double otherAmount;

	private Integer paymentSection;
	private Integer paymentMethod;
	private Long bankChalanNumber;
	private Long checkNumber;
	private Long bsrCode;

	private final List<TDSTransactionItem> tdsTransactionItem = new ArrayList<TDSTransactionItem>();

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

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
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

	public Integer getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(Integer paymentSection) {
		this.paymentSection = paymentSection;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
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

	public Long getBsrCode() {
		return bsrCode;
	}

	public void setBsrCode(Long bsrCode) {
		this.bsrCode = bsrCode;
	}

	public List<TDSTransactionItem> getTdsTransactionItem() {
		return tdsTransactionItem;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
