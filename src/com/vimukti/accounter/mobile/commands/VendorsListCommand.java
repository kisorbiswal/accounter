package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorsListCommand extends NewAbstractCommand {
	private static final String VENDOR_TYPE = "vendorType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VENDOR_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<PayeeList>("vendorssList",
				"Please Select vendor", 20) {

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// PayeeList value) {
			// commandList.add(new UserCommand("update vendor ", value
			// .getPayeeName()));
			// commandList.add(new UserCommand("deleteVendor", value.getID()));
			// }

			@Override
			protected String onSelection(PayeeList value) {
				if (value.getType() == Payee.TYPE_CUSTOMER) {
					return "Update Customer " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_VENDOR) {
					return "Update vendor " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_TAX_AGENCY) {
					return "Update TAX Agency " + value.getPayeeName();
				}
				return "";
			}

			@Override
			protected String getShowMessage() {
				return "Vendor List";
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PayeeList value) {
				Record record = new Record(value);
				record.add(getMessages().payeeName(Global.get().Vendor()),
						value.getPayeeName());
				record.add(getMessages().balance(), value.getBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("New Vendor");
			}

			@Override
			protected boolean filter(PayeeList e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<PayeeList> getLists(Context context) {
				ArrayList<PayeeList> completeList = getVendorList(context);
				ArrayList<PayeeList> result = new ArrayList<PayeeList>();

				String type = get(VENDOR_TYPE).getValue();

				for (PayeeList payee : completeList) {
					if (type.equalsIgnoreCase("Active")) {
						if (payee.isActive())
							result.add(payee);
					}
					if (type.equalsIgnoreCase("In-Active")) {
						if (!payee.isActive())
							result.add(payee);
					}
				}
				return result;
			}

		});
	}

	@Override
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
		get(VENDOR_TYPE).setDefaultValue("Active");
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	private ArrayList<PayeeList> getVendorList(Context context) {
		FinanceTool financeTool = new FinanceTool();
		try {
			return financeTool.getPayeeList(ClientTransaction.CATEGORY_VENDOR,
					context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;

	}

}
