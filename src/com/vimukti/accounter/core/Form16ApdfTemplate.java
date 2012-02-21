package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Form16ApdfTemplate {

	private FormDetails formdetails;

	private List<BookEntry> bookentries = new ArrayList<BookEntry>();

	private List<BookEntry> chalaLists = new ArrayList<BookEntry>();

	private List<PaymentSummary> summaryOfPayments = new ArrayList<PaymentSummary>();

	private List<SummaryOfTax> summaryOfTax = new ArrayList<SummaryOfTax>();

	private Verification verification;

	private Totals totals;

	public Form16ApdfTemplate() {
		totals = new Totals();
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			FieldsMetadata summaryOfTaxData = new FieldsMetadata();
			summaryOfTaxData.addFieldAsList("tax.quarter");
			summaryOfTaxData.addFieldAsList("tax.receiptNumber");
			summaryOfTaxData.addFieldAsList("tax.amount");
			summaryOfTaxData.addFieldAsList("tax.deposited");
			summaryOfTaxData.addFieldAsList("payment.amountPaid");
			summaryOfTaxData.addFieldAsList("payment.natureOfPayment");
			summaryOfTaxData.addFieldAsList("payment.dateOfPayment");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.srNo");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.taxDeposited");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.bsrCode");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.dateTaxDeposited");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.serialNumber");
			summaryOfTaxData.addFieldAsList("chalan.srNo");
			summaryOfTaxData.addFieldAsList("chalan.taxDeposited");
			summaryOfTaxData.addFieldAsList("chalan.bsrCode");
			summaryOfTaxData.addFieldAsList("chalan.dateTaxDeposited");
			summaryOfTaxData.addFieldAsList("chalan.serialNumber");
			report.setFieldsMetadata(summaryOfTaxData);

			context.put("form", formdetails);
			context.put("payment", summaryOfPayments);
			context.put("tax", summaryOfTax);
			context.put("bookEntrychalan", bookentries);
			context.put("chalan", chalaLists);
			context.put("verification", verification);
			context.put("total", totals);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public class FormDetails {

		private String deductorAddress;
		private String deducteeAddress;
		private String deductorPan;
		private String deductorTan;
		private String deducteePan;
		private String cit;
		private String assessmentYear;
		private String fromDate;
		private String toDate;

		public String getDeductorAddress() {
			return deductorAddress;
		}

		public void setDeductorAddress(String deductorAddress) {
			this.deductorAddress = deductorAddress;
		}

		public String getDeducteeAddress() {
			return deducteeAddress;
		}

		public void setDeducteeAddress(String deducteeAddress) {
			this.deducteeAddress = deducteeAddress;
		}

		public String getDeductorPan() {
			return deductorPan;
		}

		public void setDeductorPan(String deductorPan) {
			this.deductorPan = deductorPan;
		}

		public String getDeductorTan() {
			return deductorTan;
		}

		public void setDeductorTan(String deductorTan) {
			this.deductorTan = deductorTan;
		}

		public String getDeducteePan() {
			return deducteePan;
		}

		public void setDeducteePan(String deducteePan) {
			this.deducteePan = deducteePan;
		}

		public String getCit() {
			return cit;
		}

		public void setCit(String cit) {
			this.cit = cit;
		}

		public String getFromDate() {
			return fromDate;
		}

		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}

		public String getToDate() {
			return toDate;
		}

		public void setToDate(String toDate) {
			this.toDate = toDate;
		}

		public String getAssessmentYear() {
			return assessmentYear;
		}

		public void setAssessmentYear(String assessmentYear) {
			this.assessmentYear = assessmentYear;
		}

	}

	public class PaymentSummary {

		private String amountPaid;
		private String natureOfPayment;
		private String dateOfPayment;

		public PaymentSummary(String amountPaid, String natureOfPayment,
				String dateOfPayment) {
			setAmountPaid(amountPaid);
			setNatureOfPayment(natureOfPayment);
			setDateOfPayment(dateOfPayment);
		}

		public String getAmountPaid() {
			return amountPaid;
		}

		public void setAmountPaid(String amountPaid) {
			this.amountPaid = amountPaid;
		}

		public String getNatureOfPayment() {
			return natureOfPayment;
		}

		public void setNatureOfPayment(String natureOfPayment) {
			this.natureOfPayment = natureOfPayment;
		}

		public String getDateOfPayment() {
			return dateOfPayment;
		}

		public void setDateOfPayment(String dateOfPayment) {
			this.dateOfPayment = dateOfPayment;
		}

	}

	public class SummaryOfTax {

		private String quarter;
		private String receiptNumber;
		private String amount;
		private String deposited;

		public String getQuarter() {
			return quarter;
		}

		public void setQuarter(String quarter) {
			this.quarter = quarter;
		}

		public String getReceiptNumber() {
			return receiptNumber;
		}

		public void setReceiptNumber(String receiptNumber) {
			this.receiptNumber = receiptNumber;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getDeposited() {
			return deposited;
		}

		public void setDeposited(String deposited) {
			this.deposited = deposited;
		}

	}

	public class BookEntry {

		private String srNo;
		private String taxDeposited;
		private String bsrCode;
		private String dateTaxDeposited;
		private String serialNumber;

		public String getTaxDeposited() {
			return taxDeposited;
		}

		public void setTaxDeposited(String taxDeposited) {
			this.taxDeposited = taxDeposited;
		}

		public String getBsrCode() {
			return bsrCode;
		}

		public void setBsrCode(String bsrCode) {
			this.bsrCode = bsrCode;
		}

		public String getDateTaxDeposited() {
			return dateTaxDeposited;
		}

		public void setDateTaxDeposited(String dateTaxDeposited) {
			this.dateTaxDeposited = dateTaxDeposited;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getSrNo() {
			return srNo;
		}

		public void setSrNo(String srNo) {
			this.srNo = srNo;
		}

	}

	public class Totals {
		private String totalTaxDeductedThroughBookEntry;

		private String totalTaxDeductedThroughChallan;

		public String getTotalTaxDeductedThroughBookEntry() {
			return totalTaxDeductedThroughBookEntry;
		}

		public void setTotalTaxDeductedThroughBookEntry(
				String totalTaxDeductedThroughBookEntry) {
			this.totalTaxDeductedThroughBookEntry = totalTaxDeductedThroughBookEntry;
		}

		public String getTotalTaxDeductedThroughChallan() {
			return totalTaxDeductedThroughChallan;
		}

		public void setTotalTaxDeductedThroughChallan(
				String totalTaxDeductedThroughChallan) {
			this.totalTaxDeductedThroughChallan = totalTaxDeductedThroughChallan;
		}
	}

	public class Verification {

		private String place;
		private String signature;
		private String date;
		private String fullName;
		private String designation;
		private String name;
		private String rupees;
		private String amountinwords;

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setRupees(String rupees) {
			this.rupees = rupees;
		}

		public String getRupees() {
			return rupees;
		}

		public String getAmountinwords() {
			return amountinwords;
		}

		public void setAmountinwords(String amountinwords) {
			this.amountinwords = amountinwords;
		}
	}

	public FormDetails getFormdetails() {
		return formdetails;
	}

	public void setFormdetails(FormDetails formdetails) {
		this.formdetails = formdetails;
	}

	public List<PaymentSummary> getSummaryOfPayments() {
		return summaryOfPayments;
	}

	public void setSummaryOfPayments(List<PaymentSummary> summaryOfPayments) {
		this.summaryOfPayments = summaryOfPayments;
	}

	public List<SummaryOfTax> getSummaryOfTax() {
		return summaryOfTax;
	}

	public void setSummaryOfTax(List<SummaryOfTax> summaryOfTax) {
		this.summaryOfTax = summaryOfTax;
	}

	public List<BookEntry> getBookentries() {
		return bookentries;
	}

	public void setBookentries(List<BookEntry> bookentries) {
		this.bookentries = bookentries;
	}

	public List<BookEntry> getChalaLists() {
		return chalaLists;
	}

	public void setChalaLists(List<BookEntry> chalaLists) {
		this.chalaLists = chalaLists;
	}

	public Verification getVerification() {
		return verification;
	}

	public void setVerification(Verification verification) {
		this.verification = verification;
	}

	public Totals getTotals() {
		return totals;
	}

	public void setTotals(Totals totals) {
		this.totals = totals;
	}

	public void setTotalTaxDeductedThroughChallan(String total) {
		totals.setTotalTaxDeductedThroughChallan(total);
	}

	public void setTotalTaxDeductedThroughBookEntry(String total) {
		totals.setTotalTaxDeductedThroughBookEntry(total);
	}
}
