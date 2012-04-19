package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class PayBillPdfGeneration {

	private final PayBill payBill;
	private final Company company;

	public PayBillPdfGeneration(PayBill payBill, Company company) {
		this.payBill = payBill;
		this.company = company;
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			PaybillTemplete receivePaymentTmplt = new PaybillTemplete();

			receivePaymentTmplt.setTitle("Pay Bill");
			receivePaymentTmplt.setCheckNo(payBill.getCheckNumber());
			receivePaymentTmplt.setDate(payBill.getDate().toString());
			String currencySymbol = payBill.getCurrency().getSymbol();
			receivePaymentTmplt.setTotal(DataUtils.getAmountAsStringInCurrency(
					payBill.getTotal(), currencySymbol));
			receivePaymentTmplt.setPaymentMethod(payBill.getPaymentMethod());

			Address regAdr = company.getRegisteredAddress();

			String regAddress = forAddress(regAdr.getAddress1(), false)
					+ forAddress(regAdr.getStreet(), false)
					+ forAddress(regAdr.getCity(), false)
					+ forAddress(regAdr.getStateOrProvinence(), false)
					+ forAddress(regAdr.getZipOrPostalCode(), false)
					+ forAddress(regAdr.getCountryOrRegion(), true);

			receivePaymentTmplt.setRegisteredAddress(regAddress);

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("invoice.invoiceDate");
			headersMetaData.addFieldAsList("invoice.invoiceNumber");
			headersMetaData.addFieldAsList("invoice.amountApplied");
			report.setFieldsMetadata(headersMetaData);
			List<TransactionPayBill> transactionPayBills = payBill
					.getTransactionPayBill();
			List<UsedTempletes> templetes = new ArrayList<UsedTempletes>();
			for (TransactionPayBill bill : transactionPayBills) {
				UsedTempletes usedTempletes = new UsedTempletes();
				if (bill.getEnterBill() != null) {
					usedTempletes.setNumber(bill.getEnterBill().getNumber());
					usedTempletes.setDate(bill.getEnterBill().getDate()
							.toString());
					usedTempletes.setAmountApplied(DataUtils
							.getAmountAsStringInCurrency(bill.getEnterBill()
									.getPayments(), currencySymbol));
					usedTempletes.setName(bill.getEnterBill().getVendor()
							.getName());
				}
				if (bill.getJournalEntry() != null) {
					usedTempletes.setNumber(bill.getJournalEntry().getNumber());
					usedTempletes.setDate(bill.getJournalEntry().getDate()
							.toString());
					usedTempletes.setAmountApplied(DataUtils
							.getAmountAsStrings(bill.getJournalEntry()
									.getTotal()
									- bill.getJournalEntry().getBalanceDue()));
					usedTempletes.setName("");
				}

				templetes.add(usedTempletes);
			}

			context.put("bill", receivePaymentTmplt);
			context.put("invoice", templetes);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String forAddress(String address, boolean isFooter) {
		if (address == null) {
			return "";
		}
		if (address.trim().length() == 0) {
			return "";
		}
		if (isFooter) {
			return address + "." + "\n";
		} else {
			return address + "," + "\n";
		}
	}

	public class PaybillTemplete {

		private String title;
		private String registeredAddress;
		private String date;
		private String currency;
		private String paymentMethod;
		private String amountApplied;
		private String checkNo;
		private String total;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRegisteredAddress() {
			return registeredAddress;
		}

		public void setRegisteredAddress(String registeredAddress) {
			this.registeredAddress = registeredAddress;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getAmountApplied() {
			return amountApplied;
		}

		public void setAmountApplied(String amountApplied) {
			this.amountApplied = amountApplied;
		}

		public String getCheckNo() {
			return checkNo;
		}

		public void setCheckNo(String checkNo) {
			this.checkNo = checkNo;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}
	}

	public class UsedTempletes {
		private String date;

		private String number;

		private String name;

		private String amountApplied;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getAmountApplied() {
			return amountApplied;
		}

		public void setAmountApplied(String amountApplied) {
			this.amountApplied = amountApplied;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
