package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.web.client.Global;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class CCExpensePdfGeneration {

	private CreditCardCharge creditCardCharge;
	private Company company;

	public CCExpensePdfGeneration(CreditCardCharge creditCardCharge,
			Company company) {
		this.company = company;
		this.creditCardCharge = creditCardCharge;
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			DummyExpense i = new DummyExpense();
			i.setTitle(Global.get().messages().creditCardExpense());
			i.setVendorNBillingAddress(getBillingAddress());
			i.setNumber(creditCardCharge.getNumber());
			i.setDate(Utility.getDateInSelectedFormat(creditCardCharge
					.getDate()));
			i.setPayFrom(creditCardCharge.getPayFrom().getName());
			Currency currency = creditCardCharge.getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.name");
			headersMetaData.addFieldAsList("item.description");
			headersMetaData.addFieldAsList("item.quantity");
			headersMetaData.addFieldAsList("item.itemUnitPrice");
			headersMetaData.addFieldAsList("item.discount");
			headersMetaData.addFieldAsList("item.itemTotalPrice");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = creditCardCharge
					.getTransactionItems();
			String symbol = creditCardCharge.getCurrency().getSymbol();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				StringBuffer data = new StringBuffer();
				Quantity quantity = item.getQuantity();
				if (quantity != null) {
					if (item.getItem().getType() == Item.TYPE_INVENTORY_PART
							|| item.getItem().getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
						data.append(String.valueOf(quantity.getValue()));
						if (company.getPreferences().isUnitsEnabled()) {
							Unit unit = item.getQuantity().getUnit();
							if (unit != null) {
								data.append(" ");
								data.append(unit.getType());
							}
						}
					} else {
						data.append(String.valueOf(quantity.getValue()));
					}
				}

				String unitPrice = Utility.decimalConversation(
						item.getUnitPrice(), "");
				String totalPrice = Utility.decimalConversation(
						item.getLineTotal(), "");

				Double vaTfraction = item.getVATfraction();
				String vatAmount = " ";
				if (vaTfraction != null) {
					vatAmount = Utility.decimalConversation(
							item.getVATfraction(), "");
				}
				String name = item.getItem() != null ? item.getItem().getName()
						: "Others";

				String discount = Utility.decimalConversation(
						item.getDiscount(), "");

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double rate = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(rate) + " %";
				}
				itemList.add(new ItemList(name, description, data.toString(),
						unitPrice, discount, totalPrice, vatRate, vatAmount));
			}

			String total = Utility.decimalConversation(
					creditCardCharge.getTotal(), symbol);
			i.setTotal(total);
			String subtotal = Utility.decimalConversation(
					creditCardCharge.getNetAmount(), symbol);
			i.setNetAmount(subtotal);
			i.setMemo(creditCardCharge.getMemo());
			i.setRegistrationAddress(getRegistrationAddress());

			context.put("expense", i);
			context.put("item", itemList);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getRegistrationAddress() {
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();
		if (reg != null) {
			regestrationAddress = (reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forNullValue(reg.getCountryOrRegion()) + ".");
		} else {
			regestrationAddress = (company.getTradingName() + "\n " + ((company
					.getRegistrationNumber() != null && !company
					.getRegistrationNumber().equals("")) ? "\n Company Registration No: "
					+ company.getRegistrationNumber()
					: ""));
		}

		String phoneStr = forNullValue(company.getPreferences().getPhone());
		if (phoneStr.trim().length() > 0) {
			regestrationAddress = regestrationAddress + "\n"
					+ Global.get().messages().phone() + " : " + phoneStr;
		}

		String website = forNullValue(company.getPreferences().getWebSite());
		if (website.trim().length() > 0) {
			regestrationAddress = regestrationAddress + "\n"
					+ Global.get().messages().webSite() + " : " + website
					+ " .";
		}
		return regestrationAddress;
	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		Contact selectedContact = creditCardCharge.getContact();
		if (selectedContact != null) {
			cname = selectedContact.getName().trim();
			if (selectedContact.getBusinessPhone().trim().length() > 0)
				phone = selectedContact.getBusinessPhone();
			if (phone.trim().length() > 0) {
				// If phone variable has value, then only we need to display
				// the text 'phone'
				hasPhone = true;
			}
		}

		// setting billing address
		Address bill = creditCardCharge.getVendorAddress();
		String customerName = forUnusedAddress(
				creditCardCharge.getVendor() != null ? creditCardCharge
						.getVendor().getName() : "", false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(cname, false)
					+ customerName
					+ forUnusedAddress(bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false));
			if (hasPhone) {
				billAddress.append(forUnusedAddress("Phone : " + phone, false));
			}

			String billAddres = billAddress.toString();

			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		} else {
			// If there is no Bill Address, then display only customer and
			// contact name
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
		}
		return "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return add + ", ";
		} else {
			if (add != null && !add.equals(""))
				return add + "\n";
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	public class DummyExpense {

		private String title;
		private String vendorNBillingAddress;
		private String number;
		private String date;
		private String currency;
		private String total;
		private String netAmount;
		private String memo;
		private String registrationAddress;
		private String payFrom;

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRegistrationAddress() {
			return registrationAddress;
		}

		public void setRegistrationAddress(String registrationAddress) {
			this.registrationAddress = registrationAddress;
		}

		public String getVendorNBillingAddress() {
			return vendorNBillingAddress;
		}

		public void setVendorNBillingAddress(String vendorNBillingAddress) {
			this.vendorNBillingAddress = vendorNBillingAddress;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getPayFrom() {
			return payFrom;
		}

		public void setPayFrom(String payFrom) {
			this.payFrom = payFrom;
		}

	}

	public class ItemList {
		private String name;
		private String description;
		private String quantity;
		private String itemUnitPrice;
		private String discount;
		private String itemTotalPrice;
		private String itemVatRate;
		private String itemVatAmount;

		ItemList(String name, String description, String quantity,
				String itemUnitPrice, String discount, String itemTotalPrice,
				String itemVatRate, String itemVatAmount) {
			this.name = name;
			this.description = description;
			this.quantity = quantity;
			this.itemUnitPrice = itemUnitPrice;
			this.discount = discount;
			this.itemTotalPrice = itemTotalPrice;
			this.itemVatRate = itemVatRate;
			this.itemVatAmount = itemVatAmount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public String getItemUnitPrice() {
			return itemUnitPrice;
		}

		public void setItemUnitPrice(String itemUnitPrice) {
			this.itemUnitPrice = itemUnitPrice;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getItemTotalPrice() {
			return itemTotalPrice;
		}

		public void setItemTotalPrice(String itemTotalPrice) {
			this.itemTotalPrice = itemTotalPrice;
		}

		public String getItemVatRate() {
			return itemVatRate;
		}

		public void setItemVatRate(String itemVatRate) {
			this.itemVatRate = itemVatRate;
		}

		public String getItemVatAmount() {
			return itemVatAmount;
		}

		public void setItemVatAmount(String itemVatAmount) {
			this.itemVatAmount = itemVatAmount;
		}

	}
}
