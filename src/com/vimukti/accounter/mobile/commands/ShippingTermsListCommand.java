package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ShippingTermsListCommand extends NewAbstractCommand {
	private static final String SHIPPING_TERMS = "ShiipingTerms";

	@Override
	public String getId() {
		return null;
	}

	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ShippingTerms>(SHIPPING_TERMS,
				"Please Enter name or number", 10) {
			@Override
			protected Record createRecord(ShippingTerms value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getDescription());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getConstants().shippingTerms()));
			}

			@Override
			protected boolean filter(ShippingTerms e, String name) {
				return true;
			}

			@Override
			protected List<ShippingTerms> getLists(Context context) {
				List<ShippingTerms> list = new ArrayList<ShippingTerms>();
				Set<ShippingTerms> accounts = context.getCompany()
						.getShippingTerms();
				for (ShippingTerms a : accounts) {
					list.add(a);
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().shippingTermList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(ShippingTerms value) {
				return getConstants().update() + " " + value.getName();
			}
		});
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

}
