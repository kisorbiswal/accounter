package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CompanyPreferences;

public class CommonSettingsMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject commonSettings = new JSONObject();
		commonSettings.put("taxId", obj.getTaxId());
		commonSettings.put("autoApplycredits",
				obj.isCreditsApplyAutomaticEnable());
		commonSettings.put("chargeOrTrackTax", obj.isChargeSalesTax());
		commonSettings.put(
				"taxItemInTransactions",
				context.getPickListContext().get(
						"TaxItemInTransactions",
						obj.isTaxPerDetailLine() ? "OnePerDetailLine"
								: "OnePerTransaction" ));
		commonSettings.put("enableTrackingTaxPaid", obj.isTrackPaidTax());
		commonSettings.put("enableTDS", obj.isTDSEnabled());
		commonSettings.put("useBillable",
				obj.isBillableExpsesEnbldForProductandServices());
		commonSettings.put("productAndServicesTrackingByCustomer",
				obj.isProductandSerivesTrackingByCustomerEnabled());
		commonSettings.put("currency",
				context.get("Currency", obj.getPrimaryCurrency().getID()));
		commonSettings.put("firstMonthOfYourFinancialYear",
				obj.getFiscalYearFirstMonth());
		commonSettings.put("depreciationStartDate", obj
				.getDepreciationStartDate().getAsDateObject());
		Address tradingAddress = obj.getTradingAddress();
		JSONObject jsonAddress = new JSONObject();
		jsonAddress.put("street", tradingAddress.getStreet());
		jsonAddress.put("city", tradingAddress.getCity());
		jsonAddress.put("stateOrProvince",
				tradingAddress.getStateOrProvinence());
		jsonAddress.put("zipOrPostalCode", tradingAddress.getZipOrPostalCode());
		jsonAddress.put("country", tradingAddress.getCountryOrRegion());
		commonSettings.put("tradingAddress", jsonAddress);
		commonSettings.put("companyHasRegisteredAddress",
				obj.isShowRegisteredAddress());
		// registeredAddress is not found
		commonSettings.put("defaultTaxCode",
				context.get("TaxCode", obj.getDefaultTaxCode().getID()));
		return commonSettings;
	}

}
