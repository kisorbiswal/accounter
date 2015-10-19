package com.vimukti.accounter.migration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Depreciation;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.utils.HibernateUtil;

public class DepreciationMigrator implements IMigrator<Depreciation> {
	public JSONObject migrate(Depreciation depreciation, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(depreciation, jsonObject,
				context);
		jsonObject.put("status", PicklistUtilMigrator
				.depreciationStatusIdentity(depreciation.getStatus()));
		jsonObject.put("depreciateFrom", depreciation.getDepreciateFrom()
				.getAsDateObject().getTime());
		jsonObject.put("depreciateTo", depreciation.getDepreciateTo()
				.getAsDateObject().getTime());
		FixedAsset fa = depreciation.getFixedAsset();
		jsonObject.put("fixedAsset", context.get("FixedAsset", fa.getID()));
		jsonObject.put("depreciationFor", PicklistUtilMigrator
				.depreciationForIdentity(depreciation.getDepreciationFor()));
		try {
			jsonObject.put("depreciatedAmount",
					getDepreciationAmount(fa, depreciation, context));
		} catch (ParseException e) {
		}
		// FinanceDate rollBackDepreciationDate = depreciation
		// .getRollBackDepreciationDate().getAsDateObject();
		// TODO above value all ways null because it is not saving in database
		jsonObject.put("rollBackDepreciationDate", new Date().getTime());

		return jsonObject;
	}

	private double getDepreciationAmount(FixedAsset fa,
			Depreciation depreciation, MigratorContext context)
			throws ParseException {
		Session session = HibernateUtil.getCurrentSession();
		Criteria criteria = session.createCriteria(Depreciation.class, "obj");
		this.addRestrictions(criteria);
		List<Depreciation> depreciations = criteria
				.add(Restrictions.eq("company", context.getCompany()))
				.add(Restrictions.lt("depreciateFrom",
						depreciation.getDepreciateFrom()))
				.addOrder(Order.asc("depreciateFrom")).list();
		FinanceDate fromDate = null;
		if (!depreciations.isEmpty()) {
			fromDate = depreciations.get(0).getDepreciateFrom();
		}
		if (fromDate == null) {
			fromDate = context.getCompany().getPreferences()
					.getDepreciationStartDate();
		}
		FinanceDate toDate = depreciation.getDepreciateTo();
		FinanceDate startDate = context.getCompany().getPreferences()
				.getDepreciationStartDate();
		fromDate = withDayOfMonth(fromDate);
		double depreciatedAmount = 0;
		while (fromDate.compareTo(toDate) <= 0) {
			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date by the actual bookvalue which represents the
			 * updated bookvalue at the end of each month.
			 */
			if (fromDate.getMonth() == startDate.getMonth()) {
				fa.setOpeningBalanceForFiscalYear(fa.getBookValue());
			}
			// DepreciationMethod.StraightLine=1
			double amount = (fa.getDepreciationMethod() == 1 ? fa
					.getPurchasePrice() : fa.getOpeningBalanceForFiscalYear());
			depreciatedAmount += ((amount * fa.getDepreciationRate()) / 12);
			fromDate.setMonth(fromDate.getMonth() + 1);
		}
		return depreciatedAmount;
	}

	private FinanceDate withDayOfMonth(FinanceDate fromDate)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = dateFormat.parse(fromDate.toString());
		Calendar c = Calendar.getInstance();
		c.setTime(convertedDate);
		return new FinanceDate(convertedDate.getYear(),
				convertedDate.getMonth(),
				c.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}