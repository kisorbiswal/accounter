package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class ProductAndServicePreferencesCommand extends
		AbstractCompanyPreferencesCommand {
	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
	private static final String INVENTORY_TRACKING = "inventorytracking";
	private static final String MULTIPLE_WAREHOUSES = "multiplewarehouses";
	private static final String AGING_DETAILS = "aagingdetails";
	private static final String TRACK_EXPENSEANDPRODUCT = "trackexpenses";
	private static final String BILLABLE_EXPENSES = "billableexpenses";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringListRequirement(SERVICE_PRODUCTS_BOTH, getMessages()
				.productAndService(), getMessages().productAndService(), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productAndService());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getServiceProductBothList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().productAndServices());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(INVENTORY_TRACKING, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH)
						.getValue();
				Integer servProBoth = getServiceProductBothList().indexOf(
						serviceProductBoth);
				if (servProBoth != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().inventoryTracking() + " "
						+ getMessages().enabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inventoryTracking() + " "
						+ getMessages().disabled();
			}
		});

		list.add(new BooleanRequirement(MULTIPLE_WAREHOUSES, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean inventoryTracking = get(INVENTORY_TRACKING).getValue();
				if (inventoryTracking) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().haveMultipleWarehouses() + " "
						+ getMessages().enabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().haveMultipleWarehouses() + " "
						+ getMessages().disabled();
			}
		});

		list.add(new BooleanRequirement(AGING_DETAILS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().ageingforduedate();
			}

			@Override
			protected String getFalseString() {
				return getMessages().ageingfortransactiondate();
			}
		});

		list.add(new BooleanRequirement(TRACK_EXPENSEANDPRODUCT, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackProducandServicesbyCustomer() + " "
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackProducandServicesbyCustomer() + " "
						+ getMessages().inActive();
			}
		});

		list.add(new BooleanRequirement(BILLABLE_EXPENSES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().use() + " " + getMessages().billabe()
						+ " " + getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().use() + " " + getMessages().billabe()
						+ " " + getMessages().inActive();
			}
		});

	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().services_labelonly());
		arrayList.add(getMessages().products_labelonly());
		arrayList.add(getMessages().bothservicesandProduct_labelonly());
		return arrayList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		String string = null;
		if (preferences.isSellProducts()) {
			string = getMessages().products_labelonly();
		}

		if (preferences.isSellServices()) {
			string = getMessages().services_labelonly();
		}

		if (preferences.isSellProducts() && preferences.isSellServices()) {
			string = getMessages().bothservicesandProduct_labelonly();
		}
		get(SERVICE_PRODUCTS_BOTH).setValue(string);
		if (preferences.isSellProducts()) {
			get(INVENTORY_TRACKING).setValue(preferences.isInventoryEnabled());
		}
		if (preferences.isInventoryEnabled()) {
			get(MULTIPLE_WAREHOUSES).setValue(preferences.iswareHouseEnabled());
		}

		if (preferences.getAgeingFromTransactionDateORDueDate() == 1) {
			get(AGING_DETAILS).setValue(true);
		} else {
			get(AGING_DETAILS).setValue(false);
		}

		get(TRACK_EXPENSEANDPRODUCT).setValue(
				preferences.isTrackEmployeeExpenses());
		get(TRACK_EXPENSEANDPRODUCT).setValue(
				preferences.isDoProductShipMents());
		get(BILLABLE_EXPENSES).setValue(preferences.isDoProductShipMents());

		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH).getValue();
		Integer servProBoth = getServiceProductBothList().indexOf(
				serviceProductBoth);
		if (servProBoth == 0) {
			preferences.setSellServices(true);
			preferences.setSellProducts(false);
		} else if (servProBoth == 1) {
			preferences.setSellProducts(true);
			preferences.setSellServices(false);
		} else {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		boolean trackquotes = get(MULTIPLE_WAREHOUSES).getValue();
		if (servProBoth != 0) {
			boolean inventoryTrack = get(INVENTORY_TRACKING).getValue();
			preferences.setInventoryEnabled(inventoryTrack);
			if (inventoryTrack) {
				preferences.setwareHouseEnabled(trackquotes);
			}
		}
		boolean agingdetails = get(AGING_DETAILS).getValue();
		boolean trackexpenses = get(TRACK_EXPENSEANDPRODUCT).getValue();
		boolean billableexpenses = get(BILLABLE_EXPENSES).getValue();

		if (agingdetails) {
			preferences.setAgeingFromTransactionDateORDueDate(1);
		} else {
			preferences.setAgeingFromTransactionDateORDueDate(2);
		}
		preferences
				.setProductandSerivesTrackingByCustomerEnabled(trackexpenses);
		preferences
				.setBillableExpsesEnbldForProductandServices(billableexpenses);

		savePreferences(context, preferences);
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Updating Preferences";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToUpdate(getMessages().companyPreferences());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().updateSuccessfully(
				getMessages().companyPreferences());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
