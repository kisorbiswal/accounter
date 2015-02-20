package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;

public class TransactionMigrator<T extends Transaction> implements IMigrator<T> {

	@Override
	public JSONObject migrate(T obj, MigratorContext context)
			throws JSONException {
		JSONObject transaction = new JSONObject();
		transaction.put("date", obj.getDate());
		transaction.put("number", obj.getNumber());
		AccounterClass accounterClass = obj.getAccounterClass();
		if (accounterClass != null) {
			transaction.put("accountClass",
					context.get("AccountClass", accounterClass.getID()));
		}
		Location location = obj.getLocation();
		if (location != null) {
			transaction.put("location",
					context.get("Location", location.getID()));
		}
		transaction.put("currencyFactor", obj.getCurrencyFactor());
		transaction.put("notes", obj.getMemo());
		List<TransactionItem> transactionItems = obj.getTransactionItems();
		JSONArray tItems = new JSONArray();
		for (TransactionItem transactionItem : transactionItems) {
			JSONObject tItem = new JSONObject();
			if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT) {
				tItem.put("account", context.get("Account", transactionItem
						.getItem().getID()));
			} else {
				tItem.put("item",
						context.get("Item", transactionItem.getItem().getID()));
				{
					Quantity quantity = transactionItem.getQuantity();
					JSONObject quantityJSON = new JSONObject();
					quantityJSON.put("value", quantity.getValue());
					Unit unit = quantity.getUnit();
					quantityJSON.put("unitFactor", unit.getFactor());
					quantityJSON.put("unit", unit);
					tItem.put("quantityItem", quantityJSON);
				}
			}
			tItem.put("description", transactionItem.getDescription());
			tItem.put("unitPrice", transactionItem.getUnitPrice());
			AccounterClass classCategory = transactionItem.getAccounterClass();
			if (classCategory != null) {
				tItem.put("accountClass",
						context.get("AccountClass", classCategory.getID()));
			}
			tItem.put("taxCode", context.get("TaxCode", transactionItem
					.getTaxCode().getID()));
			tItem.put("taxable", transactionItem.isTaxable());
			tItem.put("discount", transactionItem.getDiscount());
			tItems.put(tItem);
		}
		if (!transactionItems.isEmpty()) {
			transaction.put("transactionItems", tItems);
			transaction.put("amountIncludesTax", transactionItems.get(0)
					.isAmountIncludeTAX());
			boolean taxPerDetailLine = context.getCompany().getPreferences()
					.isTaxPerDetailLine();
			if (!taxPerDetailLine) {
				TAXCode taxCode = transactionItems.get(0).getTaxCode();
				if (taxCode != null) {
					transaction.put("taxCode",
							context.get("TaxCode", taxCode.getID()));
				}
			}
			boolean discountPerDetailLine = context.getCompany()
					.getPreferences().isDiscountPerDetailLine();
			if (!discountPerDetailLine) {
				Double discount = transactionItems.get(0).getDiscount();
				if (discount != null) {
					transaction.put("discount", discount);
				}
			}
		}
		return transaction;
	}
}