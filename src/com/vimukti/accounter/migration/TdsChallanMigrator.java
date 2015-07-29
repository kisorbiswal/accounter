package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TDSChalanDetail;
import com.vimukti.accounter.core.TDSTransactionItem;

public class TdsChallanMigrator extends TransactionMigrator<TDSChalanDetail> {

	@Override
	public JSONObject migrate(TDSChalanDetail obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("formType", getFromTypeIdentity(obj.getFormType()));
		jsonObject.put("challanSerialNo", obj.getChalanSerialNumber());
		jsonObject.put("challanPeriod", obj.getChalanPeriod());
		jsonObject.put("financialStartYear", obj.getCompany()
				.getFirstMonthOfFiscalYear());
		jsonObject.put("financialEndYear", obj.getCompany()
				.getFirstMonthOfFiscalYear() + 12);
		jsonObject.put("assesmentStartYear", obj.getAssesmentYearStart());
		jsonObject.put("assesmentEndYear", obj.getAssessmentYearEnd());
		jsonObject.put("fromDate", obj.getFromDate().getAsDateObject()
				.getTime());
		jsonObject.put("toDate", obj.getToDate().getAsDateObject().getTime());
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "Cash");
		}
		jsonObject.put("isTdsDepositedByBookEntry", obj.getIsDeposited());
		jsonObject.put("natureOfPayment", obj.getPaymentSection());
		jsonObject.put("chequeOrReferenceNo", obj.getCheckNumber());
		jsonObject.put("dateOnTaxPaid", obj.getDateTaxPaid());
		jsonObject.put("bankBSRCode", obj.getBankBsrCode());
		jsonObject.put("payFrom",
				context.get("Account", obj.getPayFrom().getID()));
		// jsonObject.put("bankBalance", null);
		jsonObject.put("incomeTax", obj.getIncomeTaxAmount());
		jsonObject.put("interestPaid", obj.getInterestPaidAmount());
		jsonObject.put("otherAmountPaid", obj.getOtherAmount());
		// jsonObject.put("payee", null);
		JSONArray array = new JSONArray();
		List<TDSTransactionItem> tdsTransactionItems = obj
				.getTdsTransactionItems();
		for (TDSTransactionItem tdsTransactionItem : tdsTransactionItems) {
			JSONObject transactionJson = new JSONObject();
			transactionJson.put("payBill", tdsTransactionItem.getTransaction());
			transactionJson.put("deducteeName", context.get("Vendor",
					tdsTransactionItem.getVendor().getID()));
			transactionJson.put("surchargeAmount",
					tdsTransactionItem.getSurchargeAmount());
			transactionJson.put("educationCess",
					tdsTransactionItem.getEduCess());
			array.put(transactionJson);
		}
		jsonObject.put("tDSChallanItems", array);
		return jsonObject;
	}

	private String getFromTypeIdentity(int formType) {
		switch (formType) {
		case 1:
			return "Form26Q";
		case 2:
			return "Form27Q";
		case 3:
			return "Form27EQ";
		}
		return null;
	}
}
