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

public class VendorAndPurchasePreferencesCommand extends
		AbstractCompanyPreferencesCommand {
	protected static final String MANAGE_BILLS = "managebills";
	private static final String CHARGE_TRACK_TAX = "chargetracktax";
	protected static final String TAXITEM_TRANSACTIONS = "taxitemtransactions";
	private static final String ENABLE_TRACKING_TAXPAID = "enabletracktax";
	private static final String ENABLE_TDS = "enabletds";
	private static final String TRACKING_QUOTES = "trackquotes";
	private static final String USE_DELAYED_CHARGES = "usedelayedcharges";
	private static final String DO_YOU_Do_SHIPPING = "doyoudoshipping";
	private static final String INCLUDE_ESTIMATES = "includeestimates";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new BooleanRequirement(MANAGE_BILLS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().manageBillsYouOwe();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontManageBillsYouOwe();
			}
		});

		list.add(new BooleanRequirement(CHARGE_TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return getMessages().chargeOrTrackTax();
			}

			@Override
			protected String getFalseString() {
				return getMessages().donotChargeOrTrackTax();
			}
		});

		list.add(new BooleanRequirement(TAXITEM_TRANSACTIONS, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().onepertransaction();
			}

			@Override
			protected String getFalseString() {
				return getMessages().oneperdetailline();
			}
		});

		list.add(new BooleanRequirement(ENABLE_TRACKING_TAXPAID, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().trackingTaxPaid();
			}

			@Override
			protected String getFalseString() {
				return getMessages().donotTrackingTaxPaid();
			}
		});

		list.add(new BooleanRequirement(ENABLE_TDS, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
				if (ischargingtax) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().enabledTDS();
			}

			@Override
			protected String getFalseString() {
				return getMessages().disabledTDS();
			}
		});
		list.add(new BooleanRequirement(TRACKING_QUOTES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackingEstimates();
			}

			@Override
			protected String getFalseString() {
				return getMessages().doNottrackingEstimates();
			}
		});

		list.add(new StringListRequirement(INCLUDE_ESTIMATES, getMessages()
				.accept() + " " + getMessages().estimate(), getMessages()
				.accept() + " " + getMessages().estimate(), true, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean trackquotes = get(TRACKING_QUOTES).getValue();
				if (trackquotes) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(
								getMessages().accept() + " "
										+ getMessages().estimate());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAcceptEstimateList();
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

		list.add(new BooleanRequirement(USE_DELAYED_CHARGES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().delayedCharges();

			}

			@Override
			protected String getFalseString() {
				return getMessages().donotUsedelayedCharges();
			}
		});

		list.add(new BooleanRequirement(DO_YOU_Do_SHIPPING, true) {

			@Override
			protected String getTrueString() {
				return getMessages().iDoShipping();

			}

			@Override
			protected String getFalseString() {
				return getMessages().iDontdoShipping();
			}
		});
	}

	private List<String> getAcceptEstimateList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().dontWantToIncludeEstimates());
		arrayList.add(getMessages().includeAcceptedEstimates());
		arrayList.add(getMessages().includePendingAndAcceptedEstimates());
		return arrayList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		get(MANAGE_BILLS).setValue(preferences.isKeepTrackofBills());
		get(CHARGE_TRACK_TAX).setValue(preferences.isTrackTax());
		boolean ischargingtax = get(CHARGE_TRACK_TAX).getValue();
		if (ischargingtax) {
			get(TAXITEM_TRANSACTIONS)
					.setValue(preferences.isTaxPerDetailLine());
			get(ENABLE_TRACKING_TAXPAID).setValue(preferences.isTrackPaidTax());

			get(ENABLE_TDS).setValue(preferences.isTDSEnabled());
		}
		get(TRACKING_QUOTES).setValue(preferences.isDoyouwantEstimates());
		get(USE_DELAYED_CHARGES)
				.setValue(preferences.isDelayedchargesEnabled());
		get(DO_YOU_Do_SHIPPING).setValue(preferences.isDoProductShipMents());

		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		boolean mangebills = get(MANAGE_BILLS).getValue();
		boolean tax = get(CHARGE_TRACK_TAX).getValue();
		if (tax) {
			boolean taxitemline = get(TAXITEM_TRANSACTIONS).getValue();
			boolean tracktaxpaid = get(ENABLE_TRACKING_TAXPAID).getValue();
			boolean enabletds = get(ENABLE_TDS).getValue();
			preferences.setTaxPerDetailLine(taxitemline);
			preferences.setTrackPaidTax(tracktaxpaid);
			preferences.setTDSEnabled(enabletds);
		}
		boolean trackquotes = get(TRACKING_QUOTES).getValue();
		boolean delayedcharges = get(USE_DELAYED_CHARGES).getValue();
		boolean doyoushipping = get(DO_YOU_Do_SHIPPING).getValue();

		preferences.setKeepTrackofBills(mangebills);
		preferences.setTaxTrack(tax);
		preferences.setDoyouwantEstimates(trackquotes);
		preferences.setDelayedchargesEnabled(delayedcharges);
		preferences.setDoProductShipMents(doyoushipping);

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
