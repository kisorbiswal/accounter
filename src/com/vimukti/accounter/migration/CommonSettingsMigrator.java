package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
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
		commonSettings.put("taxItemInTransactions",
				obj.isTaxPerDetailLine() ? "OnePerDetailLine"
						: "OnePerTransaction");
		commonSettings.put("enableTrackingTaxPaid", obj.isTrackPaidTax());
		commonSettings.put("enableTDS", obj.isTDSEnabled());
		commonSettings.put("useBillable",
				obj.isBillableExpsesEnbldForProductandServices());
		commonSettings.put("productAndServicesTrackingByCustomer",
				obj.isProductandSerivesTrackingByCustomerEnabled());
		boolean enabledMultiCurrency = obj.isEnabledMultiCurrency();
		commonSettings.put("multipleCurrency", enabledMultiCurrency);
		// commonSettings.put("tIN", obj.isEnabledMultiCurrency());
		// commonSettings.put("tAN", obj.isEnabledMultiCurrency());
		// commonSettings.put("pAN", obj.isEnabledMultiCurrency());
		commonSettings.put("depreciationStartDate", obj
				.getDepreciationStartDate().getAsDateObject().getTime());
		commonSettings.put("companyName", obj.getTradingName());
		String legalName = obj.getLegalName();
		if (legalName != null) {
			commonSettings.put("companyHasLegalName", true);
			commonSettings.put("legalName", legalName);
		} else {
			commonSettings.put("companyHasLegalName", false);
		}
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
		Company company = context.getCompany();
		// commonSettings.put("registeredAddress", jsonAddress);
		commonSettings.put("accountPayable", context.get("Account", company
				.getAccountsPayableAccount().getID()));
		// commonSettings.put("centralSalesTaxPayable",);
		Account salariesPayableAccount = company.getSalariesPayableAccount();
		if (salariesPayableAccount != null) {
			commonSettings.put("salariesPayable",
					context.get("Account", salariesPayableAccount.getID()));
		}
		commonSettings.put("taxFiled", context.get("Account", company
				.getTAXFiledLiabilityAccount().getID()));
		commonSettings.put("openingBalances", context.get("Account", company
				.getOpeningBalancesAccount().getID()));
		commonSettings.put("exchangeLossorGain", context.get("Account", company
				.getExchangeLossOrGainAccount().getID()));
		return commonSettings;
	}

}
