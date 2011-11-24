package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXItemGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;

public class NewVATCodeCommand extends NewAbstractCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String VATITEM_FOR_SALES = "vatItemForSales";
	private static final String VATITEM_FOR_PURCHASE = "vatItemForPurchase";
	private static final String IS_ACTIVE = "isActive";
	private static final String TAX_CODE = "Tax Code";

	private ClientTAXCode taxCode;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(TAX_CODE, getMessages().pleaseEnter(
				getMessages().taxCode() + getMessages().name()), getMessages()
				.taxCode() + getMessages().name(), false, true));

		list.add(new NameRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().taxCode()), getMessages().description(), true,
				true));

		list.add(new BooleanRequirement(IS_TAXABLE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}
		});

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(new ListRequirement<TAXItemGroup>(VATITEM_FOR_SALES,
				getMessages().pleaseSelect(getMessages().taxItemForSales()),
				"Vat item or Group for Sales", true, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean value = get(IS_TAXABLE).getValue();
				if (value.booleanValue()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().vatItemsList());
			}

			@Override
			protected String getSetMessage() {
				return "vat item or group has been selected";
			}

			@Override
			protected Record createRecord(TAXItemGroup value) {
				Record record = new Record(value);
				record.add("", value.getName());
				return record;
			}

			@Override
			protected String getDisplayValue(TAXItemGroup value) {
				return value != null ? value.getName() : "";
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create VAT Item");
				list.add("New Tax Group");
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().taxItemForSales());
			}

			@Override
			protected boolean filter(TAXItemGroup e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<TAXItemGroup> getLists(Context context) {
				return getFilteredVATItems(context, true);
			}
		});

		list.add(new ListRequirement<TAXItemGroup>(
				VATITEM_FOR_PURCHASE,
				getMessages().pleaseSelect(getMessages().taxItemForPurchases()),
				"Vat item or Group for Purchases", false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean value = get(IS_TAXABLE).getValue();
				if (value.booleanValue()
						&& context.getPreferences().isTrackPaidTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSetMessage() {
				return "vat item or group has been selected";
			}

			@Override
			protected Record createRecord(TAXItemGroup value) {
				Record record = new Record(value);
				record.add("", value.getName());
				return record;
			}

			@Override
			protected String getDisplayValue(TAXItemGroup value) {
				return value != null ? value.getName() : "";
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create VAT Item");
				list.add("New Tax Group");
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().taxItemForPurchases());
			}

			@Override
			protected boolean filter(TAXItemGroup e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<TAXItemGroup> getLists(Context context) {
				return getFilteredVATItems(context, false);
			}
		});

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(TAX_CODE).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();

		taxCode.setName(name);
		taxCode.setDescription(description);
		taxCode.setTaxable(isTaxable);
		taxCode.setActive(isActive);
		if (isTaxable) {
			TAXItemGroup salesVatItem = get(VATITEM_FOR_SALES).getValue();
			TAXItemGroup purchaseVatItem = get(VATITEM_FOR_PURCHASE).getValue();
			taxCode.setTAXItemGrpForSales(salesVatItem.getID());
			taxCode.setTAXItemGrpForPurchases(purchaseVatItem.getID());
		}

		create(taxCode, context);
		return null;

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a VAT Code to update.");
				return "VAT Codes List";
			}
			taxCode = (ClientTAXCode) CommandUtils.getClientObjectById(
					Long.parseLong(string), AccounterCoreType.TAX_CODE,
					getCompanyId());

			if (taxCode == null) {
				addFirstMessage(context, "Select a VAT Code to update.");
				return "VAT Codes List " + string;
			}
			get(TAX_CODE).setValue(taxCode.getName());
			get(DESCRIPTION).setValue(taxCode.getDescription());
			get(IS_ACTIVE).setValue(taxCode.isActive());
			get(IS_TAXABLE).setValue(taxCode.isTaxable());
			get(VATITEM_FOR_SALES).setValue(
					CommandUtils.getServerObjectById(
							taxCode.getTAXItemGrpForSales(),
							AccounterCoreType.TAX_ITEM_GROUP));
			get(VATITEM_FOR_PURCHASE).setValue(
					CommandUtils.getServerObjectById(
							taxCode.getTAXItemGrpForPurchases(),
							AccounterCoreType.TAX_ITEM_GROUP));
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(TAX_CODE).setValue(string);
			}
			taxCode = new ClientTAXCode();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return taxCode.getID() == 0 ? "New vat code command is activated"
				: "Update VAT Code command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return taxCode.getID() == 0 ? "New vat code commond is ready to create with the following values"
				: "VAT Code is ready to update with following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(IS_TAXABLE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return taxCode.getID() == 0 ? "New vat code commond is created successfully"
				: "VAT Code updated successfully";
	}

	private List<TAXItemGroup> getFilteredVATItems(Context context,
			boolean salesItems) {
		List<TAXItemGroup> vatItmsList = new ArrayList<TAXItemGroup>();
		Set<TAXItemGroup> taxItemGroups = context.getCompany()
				.getTaxItemGroups();
		Set<TAXItem> taxItems = context.getCompany().getTaxItems();
		List<TAXItem> activeItems = new ArrayList<TAXItem>();
		for (TAXItem taxItem : taxItems) {
			if (taxItem.isActive()) {
				activeItems.add(taxItem);
			}
		}
		taxItemGroups.addAll(activeItems);
		for (TAXItem vatItem : activeItems) {
			if (vatItem.isPercentage()) {
				TAXAgency taxAgency = vatItem.getTaxAgency();
				if (salesItems) {
					if (taxAgency.getSalesLiabilityAccount() != null) {
						vatItmsList.add(vatItem);
					}
				}
				if (!salesItems) {
					if (taxAgency.getPurchaseLiabilityAccount() != null) {
						vatItmsList.add(vatItem);
					}
				}
			}
		}

		return vatItmsList;
	}
}
