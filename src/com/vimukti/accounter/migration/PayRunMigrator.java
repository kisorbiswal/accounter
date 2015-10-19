package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AttendanceManagementItem;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeePaymentDetails;
import com.vimukti.accounter.core.PayRun;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayStructureItem;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.utils.HibernateUtil;

public class PayRunMigrator implements IMigrator<PayRun> {
	@Override
	public JSONObject migrate(PayRun obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Session session = HibernateUtil.getCurrentSession();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("date", obj.getDate().getAsDateObject().getTime());
		jsonObject.put("number", obj.getNumber());
		Currency currency = obj.getCurrency();
		if (currency != null) {
			JSONObject currencyJSON = new JSONObject();
			currencyJSON.put("identity", currency.getFormalName());
			jsonObject.put("currency", currencyJSON);
		}
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		jsonObject.put("notes", obj.getMemo());
		jsonObject.put("transactionType",
				Utility.getTransactionName(obj.getType()));
		jsonObject.put("fromDate", obj.getPayPeriodStartDate()
				.getAsDateObject().getTime());
		jsonObject.put("toDate", obj.getPayPeriodEndDate().getAsDateObject()
				.getTime());
		if (obj.getEmployee() == null) {
			jsonObject.put("type", "EmployeeGroup");
			jsonObject.put("employeeGroup", context.get("EmployeeGroup", obj
					.getEmployeeGroup().getID()));

		} else {
			jsonObject.put("type", "Employee");
			jsonObject.put("employee",
					context.get("Employee", obj.getEmployee().getID()));
		}
		jsonObject.put("workingDays", obj.getNoOfWorkingDays());
		Set<EmployeePaymentDetails> payEmployees = obj.getPayEmployee();
		/*
		 * JSONArray amiItems = new JSONArray(); for (EmployeePaymentDetails
		 * item : payEmployees) { // item.getAttendanceOrProductionItems(); //
		 * item.getUserDefinedPayheads(); //
		 * obj.getPayEmployee();//emppayrunitem JSONObject ami = new
		 * JSONObject(); Employee emp = item.getEmployee(); ami.put("Employee",
		 * context.get("Employee", emp.getID())); JSONArray attItems = new
		 * JSONArray(); Criteria criteria =
		 * session.createCriteria(PayStructure.class, "obj");
		 * this.addRestrictions(criteria); List<PayStructure> objects = new
		 * ArrayList<PayStructure>(); objects = criteria
		 * .add(Restrictions.eq("company", context.getCompany()))
		 * .add(Restrictions.eq("employee", item.getEmployee())) .list();
		 * List<PayStructureItem> items = objects .stream() .map(i -> i
		 * .getItems() .stream() .filter(j -> j.getEffectiveFrom().before(
		 * obj.getPayPeriodStartDate())) .collect(Collectors.toList()))
		 * .flatMap(l -> l.stream()).collect(Collectors.toList()); for
		 * (PayStructureItem payStructureItem : items) { JSONObject
		 * attendanceManagementItems = new JSONObject(); // TODO // number //
		 * type // amount attendanceManagementItems.put( "payStructureItem",
		 * context.get("PayStructureItem", payStructureItem.getID()));
		 * attItems.put(attendanceManagementItems); } ami.put("attendanceItems",
		 * attItems); amiItems.put(ami); }
		 * jsonObject.put("attendanceManagementItems", amiItems);
		 */
		JSONArray payRunItems = new JSONArray();
		for (EmployeePaymentDetails item : payEmployees) {
			JSONObject payRunItem = new JSONObject();
			Employee emp = item.getEmployee();
			payRunItem.put("employee", context.get("Employee", emp.getID()));
			AttendanceManagementItem attendanceManagementItemByEmp = getAttendanceManagementItemByEmp(
					obj, emp);
			payRunItem.put("absentDays",
					attendanceManagementItemByEmp.getAbscentDays());
			// EmployeePayRunItems
			Criteria criteria = session.createCriteria(PayStructure.class,
					"obj");
			this.addRestrictions(criteria);
			List<PayStructure> objects = new ArrayList<PayStructure>();
			objects = criteria
					.add(Restrictions.eq("company", context.getCompany()))
					.add(Restrictions.eq("employee", item.getEmployee()))
					.list();
			List<PayStructureItem> items = new ArrayList<PayStructureItem>();
			for (PayStructure payStructure : objects) {
				List<PayStructureItem> payStructureItemList = payStructure
						.getItems();
				for (PayStructureItem payStructureItem : payStructureItemList) {
					if (payStructureItem.getEffectiveFrom().before(
							obj.getPayPeriodStartDate())) {
						items.add(payStructureItem);
					}
				}
			}

			if (!items.isEmpty()) {
				JSONArray empPayRunItems = new JSONArray();
				for (PayStructureItem payStructureItem : items) {
					JSONObject empPayRunItem = new JSONObject();
					empPayRunItem.put(
							"structureItem",
							context.get("PayStructureItem",
									payStructureItem.getID()));
				}
				payRunItem.put("empPayRunItems", empPayRunItems);
			}
			payRunItems.put(payRunItem);
		}
		jsonObject.put("payRunItems", payRunItems);
		return jsonObject;
	}

	private AttendanceManagementItem getAttendanceManagementItemByEmp(
			PayRun obj, Employee emp) {
		AttendanceManagementItem attendanceManagementItem = null;
		for (AttendanceManagementItem ami : obj.getAttendanceItems()) {
			if (ami.getEmployee().equals(emp)) {
				attendanceManagementItem = ami;
				break;
			}
		}
		return attendanceManagementItem;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
