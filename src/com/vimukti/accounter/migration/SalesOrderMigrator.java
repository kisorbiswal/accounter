package com.vimukti.accounter.migration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class SalesOrderMigrator extends TransactionMigrator<Estimate> {
	@Override
	public JSONObject migrate(Estimate estimate, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(estimate, context);
		Customer customer = estimate.getCustomer();
		if (customer != null) {
			jsonObj.put("payee", context.get("Customer", customer.getID()));
		}

		Contact contact = estimate.getContact();
		if (contact != null) {
			jsonObj.put("contanct", context.get("Contanct", contact.getID()));
		}
		// Estimate estimate = obj.getEstimate();
		if (estimate != null) {
			jsonObj.put("quotation",
					context.get("SalesQuotation", estimate.getID()));
		}
		jsonObj.put("phone", estimate.getPhone());
		JSONObject jsonShippingAddr = new JSONObject();
		Address shipingAddr = estimate.getShippingAdress();
		if (shipingAddr != null) {
			jsonShippingAddr.put("street", shipingAddr.getStreet());
			jsonShippingAddr.put("city", shipingAddr.getCity());
			jsonShippingAddr.put("stateOrProvince",
					shipingAddr.getStateOrProvinence());
			jsonShippingAddr.put("zipOrPostalCode",
					shipingAddr.getZipOrPostalCode());
			jsonShippingAddr.put("country", shipingAddr.getCountryOrRegion());
			jsonObj.put("shipTo", jsonShippingAddr);
		}

		JSONObject jsonBillingAddr = new JSONObject();
		Address billingAddr = estimate.getAddress();
		if (billingAddr != null) {
			jsonBillingAddr.put("street", billingAddr.getStreet());
			jsonBillingAddr.put("city", billingAddr.getCity());
			jsonBillingAddr.put("stateOrProvince",
					billingAddr.getStateOrProvinence());
			jsonBillingAddr.put("zipOrPostalCode",
					billingAddr.getZipOrPostalCode());
			jsonBillingAddr.put("country", billingAddr.getCountryOrRegion());
			jsonObj.put("billTo", jsonBillingAddr);
		}
		jsonObj.put("customerReference", estimate.getReference());

		PaymentTerms paymentTerm = estimate.getPaymentTerm();
		if (paymentTerm != null) {
			jsonObj.put("paymentTerm",
					context.get("PaymentTerm", paymentTerm.getID()));
		}
		ShippingMethod shippingMethod = estimate.getShippingMethod();
		if (shippingMethod != null) {
			jsonObj.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		ShippingTerms shippingTerm = estimate.getShippingTerm();
		if (shippingTerm != null) {
			jsonObj.put("shippingTerm",
					context.get("ShippingTerm", shippingTerm.getID()));
		}
		jsonObj.put("deliveryDate", estimate.getDeliveryDate()
				.getAsDateObject().getTime());
		jsonObj.put("remarks", estimate.getMemo());
		if (estimate.getStatus() == Estimate.STATUS_REJECTED) {
			jsonObj.put("salesOrderStatus", "Cancelled");
		}
		jsonObj.put("customerOrderNo", estimate.getCustomerOrderNumber());
		String type = super.setJSONObj(jsonObj);
		// BasedOn CompanySettings we split Transactions into sub lists like
		// with tax,without discount etc. So for these childrens also we are
		// splitting
		String childKey = "transactionItems-SalesOrderItem";
		List<Long> list = context.getChildrenMap().get(childKey);
		super.addChildrenBasedOnType(type, list, childKey);
		context.getChildrenMap().remove(childKey);
		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("estimateType", Estimate.SALES_ORDER));
	}
}
