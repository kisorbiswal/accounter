package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;

public class NewVATItemCommand extends NewAbstractCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_ACTIVE = "isActive";
	private static final String VAT_RETURN_BOX = "vatReturnBox";
	private static final String TAX_ITEM_NAME = "Tax Item Name";
	private static final String TAX_RATE = "Tax Rate";
	private static final String TAX_AGENCY = "Tax Agency";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(TAX_ITEM_NAME, "Enter Tax/Vat Item Name",
				"Tax/Vat Item Name", false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new NameRequirement(DESCRIPTION, "Enter Description",
				"Description", true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new AmountRequirement(TAX_RATE, "Enter Rate", "Tax Rate",
				false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new TaxAgencyRequirement(TAX_AGENCY, "Create New Tax Agency",
				"Tax Agency:", false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return "Tax/Vat Agency has been selected";
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new StringListRequirement(VAT_RETURN_BOX, null,
				"vat return box", false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {

				if (context.getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isVatAvailable()
						&& context
								.getCompany()
								.getCountry()
								.equals(CountryPreferenceFactory.UNITED_KINGDOM)) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return "vat return box has been Selected.";
			}

			@Override
			protected String getSelectString() {
				return "Slect a Vat return box";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getVatReturnBoxes(context.getCompany());
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "item is active";
			}

			@Override
			protected String getFalseString() {
				return "item is in active";
			}
		});
	}

	protected List<String> getVatReturnBoxes(Company clientCompany) {
		Set<VATReturnBox> vatReturnBoxList = clientCompany.getVatReturnBoxes();
		List<String> vatReturnBoxListNames = new ArrayList<String>();
		if (!vatReturnBoxListNames.isEmpty()) {
			for (VATReturnBox vatReturnBox : vatReturnBoxList) {
				vatReturnBoxListNames.add(vatReturnBox.getName());
			}
			return vatReturnBoxListNames;
		}
		return null;
	}

	protected List<String> getTaxagencyList(Company company) {
		Set<TAXAgency> taxAgencyList = company.getTaxAgencies();
		List<String> taxAgencyListNames = new ArrayList<String>();
		if (!taxAgencyListNames.isEmpty()) {
			for (TAXAgency taxAgency : taxAgencyList) {
				if (taxAgency.isActive())
					taxAgencyListNames.add(taxAgency.getName());
			}
			return taxAgencyListNames;
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXItem taxItem = new ClientTAXItem();
		String name = (String) get(TAX_ITEM_NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		double taxRate = Double.parseDouble((String) get(TAX_RATE).getValue());
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		taxItem.setPercentage(true);
		if (context.getPreferences().isTrackTax()) {
			ClientVATReturnBox vatReturnBox = (ClientVATReturnBox) get(
					VAT_RETURN_BOX).getValue();
			taxItem.setVatReturnBox(vatReturnBox.getID());
		}
		taxItem.setName(name);
		taxItem.setDescription(description);
		taxItem.setTaxRate(taxRate);
		taxItem.setActive(isActive);
		taxItem.setTaxAgency(taxAgency.getID());
		create(taxItem, context);
		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "New Vat Item commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "New vat item is ready to create with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return "New vat Item created Successfully";
	}

}
