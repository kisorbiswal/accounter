package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Warehouse;

public class TransactionMigrator<T extends Transaction> implements IMigrator<T> {
	boolean enableTaxCode = false;
	boolean enableDiscount = false;
	boolean taxCodeOnePerTransaction = false;
	boolean discountOnePerTransaction = false;
	TransactionMigrationContext currentTrasMigrationContext;

	@Override
	public JSONObject migrate(T obj, MigratorContext context)
			throws JSONException {
		currentTrasMigrationContext = context.getCurrentTrasMigrationContext();
		boolean checkTransactionType = (currentTrasMigrationContext != null);

		TAXCode taxCodeObj = null;
		Double discountAmount = null;
		List<TransactionItem> transactionItems = obj.getTransactionItems();
		if (!transactionItems.isEmpty()) {
			enableTaxCode = transactionItems.stream().anyMatch(
					i -> i.getTaxCode() != null);
			enableTaxCode = transactionItems.stream().anyMatch(
					i -> i.getDiscount() != 0 && i.getDiscount() != null);
			TAXCode taxCode = transactionItems.stream().findFirst().get()
					.getTaxCode();
			taxCodeObj = taxCode;
			Double discount = transactionItems.stream().findFirst().get()
					.getDiscount();
			discountAmount = discount;

			taxCodeOnePerTransaction = transactionItems.stream().allMatch(
					i -> i.getTaxCode() == taxCode);
			discountOnePerTransaction = transactionItems.stream().allMatch(
					i -> i.getDiscount().equals(discount));
		}
		if (checkTransactionType) {

		}
		
		JSONObject transaction = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, transaction, context);
		transaction.put("date", obj.getDate().getAsDateObject().getTime());
		transaction.put("number", obj.getNumber());
		if (obj.isDraft()) {
			transaction.put("objStatus", "Draft");
		}
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
		Currency currency = obj.getCurrency();
		if (currency != null) {
			JSONObject currencyJSON = new JSONObject();
			currencyJSON.put("identity", currency.getFormalName());
			transaction.put("currency", currencyJSON);
		}
		transaction.put("currencyFactor", obj.getCurrencyFactor());
		transaction.put("notes", obj.getMemo());
		int type = obj.getType();
		transaction.put("transactionType", PicklistUtilMigrator
				.getTransactionTypeIdentifier(
						type,
						type == Transaction.TYPE_ESTIMATE ? ((Estimate) obj)
								.getEstimateType() : 0));
		Job job = obj.getJob();
		if (job != null) {
			transaction.put("project", context.get("Job", job.getID()));
		}
		if (obj instanceof JournalEntry) {
			return transaction;
		}
		if (!transactionItems.isEmpty()) {
			JSONArray tItems = new JSONArray();
			for (TransactionItem transactionItem : transactionItems) {
				JSONObject tItem = new JSONObject();
				if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT) {
					tItem.put("type", "Account");
					tItem.put("account", context.get("Account", transactionItem
							.getAccount().getID()));
				} else {
					tItem.put("type", "Item");
					tItem.put("item", context.get("Item", transactionItem
							.getItem().getID()));
					{
						Quantity quantity = transactionItem.getQuantity();
						Unit unit = quantity.getUnit();
						if (unit != null) {
							tItem.put("unit", context.get("Unit", unit.getID()));
						}
						tItem.put("quantity", quantity.getValue());
					}
				}

				tItem.put("description", transactionItem.getDescription());
				tItem.put("unitPrice", transactionItem.getUnitPrice());
				AccounterClass classCategory = transactionItem
						.getAccounterClass();
				if (classCategory != null) {
					tItem.put("accountClass",
							context.get("AccountClass", classCategory.getID()));
				}
				TAXCode itemTaxCode = transactionItem.getTaxCode();
				if (itemTaxCode != null) {
					tItem.put("taxCode",
							context.get("TaxCode", itemTaxCode.getID()));
				}
				tItem.put("taxable", transactionItem.isTaxable());
				double itemDiscount = transactionItem.getDiscount();
				if (itemDiscount != 0) {
					tItem.put("discount", itemDiscount / 100);
				}
				Warehouse wareHouse = transactionItem.getWareHouse();
				if (wareHouse != null) {
					tItem.put("warehouse",
							context.get("Warehouse", wareHouse.getID()));
				}
				tItems.put(tItem);
			}
			transaction.put("transactionItems", tItems);
			transaction.put("amountIncludesTax", transactionItems.get(0)
					.isAmountIncludeTAX());
			if (enableTaxCode && taxCodeOnePerTransaction) {
				if (taxCodeObj != null) {
					transaction.put("taxCode",
							context.get("TaxCode", taxCodeObj.getID()));
				}
			}
			if (enableDiscount && discountOnePerTransaction) {
				if (discountAmount != 0) {
					transaction.put("discount",
							discountAmount.doubleValue() / 100);
				}
			}
		}
		return transaction;
	}

	protected void setJSONObj(JSONObject jsonObj) {
		if (enableDiscount) {
			if (enableTaxCode) {
				if (taxCodeOnePerTransaction && discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWTaxOPT(jsonObj);
				} else if (taxCodeOnePerTransaction
						&& !discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWTaxOPT(jsonObj);
				} else if (!taxCodeOnePerTransaction
						&& discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWTaxOPDL(jsonObj);
				} else if (!taxCodeOnePerTransaction
						&& !discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWTaxOPDL(jsonObj);
				}
			} else {
				if (discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWOTax(jsonObj);
				} else if (!discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWOTax(jsonObj);
				}
			}
		} else if (!enableDiscount) {
			if (enableTaxCode) {
				if (taxCodeOnePerTransaction) {
					currentTrasMigrationContext
							.addwODiscountAndWTaxOPT(jsonObj);
				} else if (!taxCodeOnePerTransaction) {
					currentTrasMigrationContext
							.addwODiscountAndWTaxOPDL(jsonObj);
				}
			} else {
				currentTrasMigrationContext.addwODiscountAndWOTax(jsonObj);
			}

		}
	}
}