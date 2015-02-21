package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Estimate;

public class CashSaleMigrator extends TransactionMigrator<CashSales> {
	@Override
	public JSONObject migrate(CashSales obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		Account depositIn = obj.getDepositIn();
		jsonObject.put("depositIn", context.get("Account", depositIn.getID()));
		jsonObject.put("phone", obj.getPhone());

		JSONObject jsonAddress = new JSONObject();
		Address billingAddress = obj.getBillingAddress();
		jsonAddress.put("street", billingAddress.getStreet());
		jsonAddress.put("city", billingAddress.getCity());
		jsonAddress.put("stateOrProvince",
				billingAddress.getStateOrProvinence());
		jsonAddress.put("zipOrPostalCode", billingAddress.getZipOrPostalCode());
		jsonAddress.put("country", billingAddress.getCountryOrRegion());
		jsonObject.put("billingAddress", jsonAddress);

		JSONObject jsonShipTo = new JSONObject();
		Address shipingTo = obj.getShippingAdress();
		jsonShipTo.put("street", shipingTo.getStreet());
		jsonShipTo.put("city", shipingTo.getCity());
		jsonShipTo.put("stateOrProvince", shipingTo.getStateOrProvinence());
		jsonShipTo.put("zipOrPostalCode", shipingTo.getZipOrPostalCode());
		jsonShipTo.put("country", shipingTo.getCountryOrRegion());
		jsonShipTo.put("shipTo", jsonShipTo);

		List<Estimate> salesOrders = obj.getSalesOrders();
		JSONArray array = new JSONArray();
		for (Estimate quot : salesOrders) {
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("salesOrder",
					context.get("SalesQuotation", quot.getID()));
			// TODO this may change to get SaleQuotations
			array.put(jsonObject2);
		}
		jsonObject.put("shippingTerm",
				context.get("ShippingTerm", obj.getShippingTerm().getID()));
		jsonObject.put("shippingMethod",
				context.get("ShippingMethod", obj.getShippingMethod().getID()));
		jsonObject.put("deliveryDate", obj.getDeliverydate().getAsDateObject());
		return jsonObject;
	}
}
