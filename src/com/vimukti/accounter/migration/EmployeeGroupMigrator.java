package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.utils.HibernateUtil;

public class EmployeeGroupMigrator implements IMigrator<EmployeeGroup>{

	@Override
	public JSONObject migrate(EmployeeGroup obj, MigratorContext context)
			throws JSONException {
		JSONObject employeeGroup = new JSONObject();
		employeeGroup.put("name", obj.getName());
		// setting payStructure of employeeGroup 
		Session session = HibernateUtil.getCurrentSession();
		Criteria createCriteria = session.createCriteria(PayStructure.class,
				"obj");
		createCriteria.add(Restrictions.eq("company", context.getCompany().getId()));
		createCriteria.add(Restrictions.eq("employeeGroup", obj.getID()));
		PayStructure uniqueResult = (PayStructure) createCriteria
				.uniqueResult();
		employeeGroup.put("payStructure", context.get("PayStructure", uniqueResult.getID()));
		return employeeGroup;
	} 
}