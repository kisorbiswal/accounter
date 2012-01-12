package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Form16ApdfTemplate {

	FormDetails formdetails;

	public Form16ApdfTemplate() {
		formdetails = new FormDetails();
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		FormDetails formDetails = new FormDetails();

		try {

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("payment.amountPaid");
			headersMetaData.addFieldAsList("payment.natureOfPayment");
			headersMetaData.addFieldAsList("payment.dateOfPayment");
			report.setFieldsMetadata(headersMetaData);
			List<PaymentSummary> paymentSummaryList = new ArrayList<PaymentSummary>();

			for (int i = 0; i < 4; i++) {
				paymentSummaryList.add(new PaymentSummary("", "", ""));
			}
			context.put("payment", paymentSummaryList);
			context.put("form", formDetails);

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
		private String assesmentYear;
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

		public String getAssesmentYear() {
			return assesmentYear;
		}

		public void setAssesmentYear(String assesmentYear) {
			this.assesmentYear = assesmentYear;
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

	}

	public class PaymentSummary {

		private String amountPaid;
		private String natureOfPayment;
		private String dateOfPayment;

		public PaymentSummary(String string, String string2, String string3) {
			amountPaid = string;
			natureOfPayment = string2;
			dateOfPayment = string3;
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

	}

	public void setVendorandCompanyData(Vendor vendor,
			ClientTDSDeductorMasters tdsDeductorMasterDetails) {

		formdetails.setDeductorAddress(tdsDeductorMasterDetails
				.getBuildingName()
				+ " "
				+ tdsDeductorMasterDetails.getCity()
				+ " "
				+ tdsDeductorMasterDetails.getRoadName()
				+ " "
				+ tdsDeductorMasterDetails.getState()
				+ " "
				+ tdsDeductorMasterDetails.getPinCode());

		formdetails.setDeducteePan(tdsDeductorMasterDetails.getPanNumber());
		formdetails.setDeductorTan(tdsDeductorMasterDetails.getTanNumber());

		Set<Address> address = vendor.getAddress();
		for (Address address2 : address) {
			formdetails.setDeducteeAddress(address2.getCity() + " "
					+ address2.getStreet() + " "
					+ address2.getStateOrProvinence() + " "
					+ address2.getCountryOrRegion() + " "
					+ address2.getZipOrPostalCode());
		}

		formdetails.setDeducteePan(vendor.getTaxId());

	}

	public void setDateYear(String dateRangeString) {

		formdetails.setFromDate(dateRangeString);
		formdetails.setToDate(dateRangeString);
	}
}
