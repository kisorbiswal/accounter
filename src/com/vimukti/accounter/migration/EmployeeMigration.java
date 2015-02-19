package com.vimukti.accounter.migration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.utils.HibernateUtil;

public class EmployeeMigration implements IMigrator<Employee> {

	@Override
	public JSONObject migrate(Employee obj, MigratorContext context)
			throws JSONException {
		JSONObject employee = new JSONObject();
		employee.put("pANorEIN", obj.getPANno());
		EmployeeGroup group = obj.getGroup();
		if (group != null) {
			employee.put("employeeGroup",
					context.get("EmployeeGroup", group.getID()));
		}
		employee.put("designation", obj.getDesignation());
		employee.put("workingLocation", obj.getLocation());
		Session session = HibernateUtil.getCurrentSession();
		Criteria createCriteria = session.createCriteria(PayStructure.class,
				"obj");
		createCriteria.add(Restrictions.eq("company", context.getCompany().getId()));
		createCriteria.add(Restrictions.eq("employee", obj.getID()));
		PayStructure uniqueResult = (PayStructure) createCriteria
				.uniqueResult();
		employee.put("payStructure", context.get("PayStructure", uniqueResult.getID()));
		employee.put("bankAccountNumber", obj.getBankAccountNo());
		employee.put("bankName", obj.getBankName());
		employee.put("bankBranch", obj.getBankBranch());
		
		return employee;
	}
}
