package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.SalesOrder;
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
		Invoice invoice = null;
		if (obj instanceof Invoice) {
			invoice = (Invoice) obj;
		}
		// Migrating childrens of SalesOrder,SalesQuotation,Credit,Charge
		String key = null;
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		if (obj instanceof SalesOrder) {
			key = "transactionItems-SalesOrderItem";
		}
		if (obj instanceof Estimate) {
			Estimate estimate = (Estimate) obj;
			int estimateType = estimate.getEstimateType();
			if (estimateType == Estimate.QUOTES
					|| estimateType == Estimate.CHARGES) {
				key = "transactionItems-SalesQuotationItem";
			} else if (estimateType == Estimate.CREDITS) {
				key = "transactionItems-CreditItem";
			}
		}
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			if (key != null) {
				childrenMap.put(key, list);
			}
		}
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
				if (key != null) {
					// Setting children of SalesOrder,SalesQuotation,Credit to
					// context
					list.add(transactionItem.getID());
				}
				// InvoiceItem migration
				if (invoice != null
						&& transactionItem.getType() == 1
						&& transactionItem.getReferringTransactionItem() != null) {
					long id = transactionItem.getReferringTransactionItem()
							.getID();
					// Setting SalesOrderItem,CreditItem,SalesQuotationItem in
					// InvoiceItem
					Long newId = context.get("SalesOrderItem", id);
					if (newId != null) {
						tItem.put("salesOrderItem", newId);
					}
					if (newId == null) {
						newId = context.get("CreditItem", id);
						if (newId != null) {
							tItem.put("creditItem", newId);
						}
					}
					if (newId == null) {
						newId = context.get("SalesQuotationItem", id);
						if (newId != null) {
							tItem.put("salesQuotationItem", newId);
						}
					}
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

	protected String setJSONObj(JSONObject jsonObj) {
		if (enableDiscount) {
			if (enableTaxCode) {
				if (taxCodeOnePerTransaction && discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWTaxOPT(jsonObj);
					return "wDiscountOPTAndWTaxOPT";
				} else if (taxCodeOnePerTransaction
						&& !discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWTaxOPT(jsonObj);
					return "wDiscountOPDLAndWTaxOPT";
				} else if (!taxCodeOnePerTransaction
						&& discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWTaxOPDL(jsonObj);
					return "wDiscountOPTAndWTaxOPDL";
				} else if (!taxCodeOnePerTransaction
						&& !discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWTaxOPDL(jsonObj);
					return "wDiscountOPDLAndWTaxOPDL";
				}
			} else {
				if (discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPTAndWOTax(jsonObj);
					return "wDiscountOPTAndWOTax";
				} else if (!discountOnePerTransaction) {
					currentTrasMigrationContext
							.addwDiscountOPDLAndWOTax(jsonObj);
					return "wDiscountOPDLAndWOTax";
				}
			}
		} else if (!enableDiscount) {
			if (enableTaxCode) {
				if (taxCodeOnePerTransaction) {
					currentTrasMigrationContext
							.addwODiscountAndWTaxOPT(jsonObj);
					return "wODiscountAndWTaxOPT";
				} else if (!taxCodeOnePerTransaction) {
					currentTrasMigrationContext
							.addwODiscountAndWTaxOPDL(jsonObj);
					return "wODiscountAndWTaxOPDL";
				}
			} else {
				currentTrasMigrationContext.addwODiscountAndWOTax(jsonObj);
				return "wODiscountAndWOTax";
			}
		}
		return null;
	}

	// Using to migrate Children of SalesQuotation,SalesOrder,Credit
	// BasedOn CompanySettings we split Transactions into sub lists like
	// with tax,without discount etc. So for these childrens also we are
	// splitting
	protected void addChildrenBasedOnType(String type, List<Long> list,
			String key) {
		Map<String, Map<String, List<Long>>> childrensMap = currentTrasMigrationContext
				.getChildrensMap();
		Map<String, List<Long>> map = childrensMap.get(key);
		if (map == null) {
			map = new HashMap<String, List<Long>>();
			childrensMap.put(key, map);
		}
		List<Long> oldList = null;
		if (map.containsKey(type)) {
			oldList = map.get(type);
		}
		if (oldList == null || oldList.isEmpty()) {
			oldList = list;
			map.put(type, oldList);
		} else {
			oldList.addAll(list);
			map.put(type, oldList);
		}
	}
}