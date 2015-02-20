package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;

public class EnterBillMigrator implements IMigrator<EnterBill> {

	@Override
	public JSONObject migrate(EnterBill obj, MigratorContext context)
			throws JSONException {
		JSONObject enterBill = new JSONObject();
		enterBill.put("date", obj.getDate());
		enterBill.put("Number", obj.getNumber());
		enterBill.put("payee", context.get("Vendor", obj.getVendor().getID()));
		enterBill.put("contact", obj.getContact());
		AccounterClass accounterClass = obj.getAccounterClass();
		if (accounterClass != null) {
			enterBill.put("accountClass",
					context.get("AccountClass", accounterClass.getID()));
		}
		Location location = obj.getLocation();
		if (location != null) {
			enterBill
					.put("location", context.get("Location", location.getID()));
		}
		enterBill.put("currencyFactor", obj.getCurrencyFactor());
		enterBill.put("notes", obj.getMemo());
		enterBill.put("isReconciled", "");
		enterBill.put("dueDate", obj.getDueDate().getAsDateObject());
		enterBill.put("phone", obj.getPhone());
		Address vendorAddress = obj.getVendorAddress();
		if (vendorAddress != null) {
			JSONObject addressJSON = new JSONObject();
			addressJSON.put("street", vendorAddress.getStreet());
			addressJSON.put("city", vendorAddress.getCity());
			addressJSON.put("stateOrProvince",
					vendorAddress.getStateOrProvinence());
			addressJSON.put("zipOrPostalCode",
					vendorAddress.getZipOrPostalCode());
			addressJSON.put("country", vendorAddress.getCountryOrRegion());
			enterBill.put("billTo", vendorAddress);
		}
		enterBill.put("paymentTerm",
				context.get("PaymentTerm", obj.getPaymentTerm().getID()));
		enterBill.put("deliveryDate", obj.getDeliveryDate().getAsDateObject());
		List<TransactionItem> transactionItems = obj.getTransactionItems();
		JSONArray billItems = new JSONArray();
		for (TransactionItem transactionItem : transactionItems) {
			JSONObject billItem = new JSONObject();
			if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT) {
				billItem.put("account", context.get("Account", transactionItem
						.getItem().getID()));
			} else {
				billItem.put("item",
						context.get("Item", transactionItem.getItem().getID()));
				{
					Quantity quantity = transactionItem.getQuantity();
					JSONObject quantityJSON = new JSONObject();
					quantityJSON.put("value", quantity.getValue());
					Unit unit = quantity.getUnit();
					quantityJSON.put("unitFactor", unit.getFactor());
					quantityJSON.put("unit", unit);
					billItem.put("quantityItem", quantityJSON);
				}
			}
			billItem.put("description", transactionItem.getDescription());
			billItem.put("unitPrice", transactionItem.getUnitPrice());
			AccounterClass classCategory = transactionItem.getAccounterClass();
			if (classCategory != null) {
				billItem.put("accountClass",
						context.get("AccountClass", classCategory.getID()));
			}
			billItem.put("taxCode", context.get("TaxCode", transactionItem
					.getTaxCode().getID()));
			billItem.put("taxable", transactionItem.isTaxable());
			billItem.put("discount", transactionItem.getDiscount());
			billItems.put(billItem);
		}
		enterBill.put("amountIncludesTax", transactionItems.get(0)
				.isAmountIncludeTAX());
		boolean taxPerDetailLine = context.getCompany().getPreferences()
				.isTaxPerDetailLine();
		if (!taxPerDetailLine) {
			TAXCode taxCode = transactionItems.get(0).getTaxCode();
			if (taxCode != null) {
				enterBill.put("taxCode",
						context.get("TaxCode", taxCode.getID()));
			}
		}
		boolean discountPerDetailLine = context.getCompany().getPreferences()
				.isDiscountPerDetailLine();
		if (!discountPerDetailLine) {
			Double discount = transactionItems.get(0).getDiscount();
			if (discount != null) {
				enterBill.put("discount", discount);
			}
		}
		return enterBill;
	}
}
