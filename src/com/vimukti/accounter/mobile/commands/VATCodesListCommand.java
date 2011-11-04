package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class VATCodesListCommand extends NewAbstractCommand {



	private static final String CURRENT_VIEW = "currentView";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		
		list.add(new ActionRequirement(CURRENT_VIEW, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().active());
				list.add(getConstants().inActive());
				return list;
			}
		});
		
		
		
		list.add(new ShowListRequirement<TAXCode>("vatCodesList", "Please Select Vat Code", 5) {
			@Override
			protected Record createRecord(TAXCode value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getDescription());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getConstants().vatCode()));
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> completeList = getVatCodes(context);
				List<TAXCode> result = new ArrayList<TAXCode>();

				String type = VATCodesListCommand.this.get(CURRENT_VIEW).getValue();
				
			
				for (TAXCode taxCode : completeList) {
					
					if (type.equals("Active")) {
						if (taxCode.isActive())
								
							result.add(taxCode);
					}
					if (type.equals("In-Active")) {
						if (!taxCode.isActive())
							result.add(taxCode);
					}
					
				}
				return result;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().vatCodeList();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected String onSelection(TAXCode value) {
				return null;
			}

		});
		
		list.add(new ActionRequirement(CURRENT_VIEW, null) {
			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().active());
				list.add(getConstants().inActive());
				return list;
			}
		});
	}

	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CURRENT_VIEW).setDefaultValue(getConstants().active());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	private Set<TAXCode> getVatCodes(Context context) {
		return context.getCompany().getTaxCodes();
		
	}

}
