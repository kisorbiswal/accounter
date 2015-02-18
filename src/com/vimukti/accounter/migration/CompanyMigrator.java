package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyMigrator {

	private static Logger log = Logger.getLogger(CompanyMigrator.class);

	private Company company;

	public CompanyMigrator(Company company) {
		this.company = company;
	}

	public void migrate() {
		long companyID = company.getId();
		log.info("***Migrating Company Id: " + companyID);
		Session session = HibernateUtil.getCurrentSession();
		// Organization
		String organizationJson = createOrganization();
		// TODO Users Migration
		// Accounts
		MigratorContext context = new MigratorContext();
		Map<Long, Long> migratedObjects = migrateObjects(Account.class,
				new AccountMigrator(), context);
		context.put("Account", migratedObjects);
		// salesPersons
		migratedObjects = migrateObjects(SalesPerson.class,
				new SalesPersonMigrator(), context);
		context.put("SalesPerson", migratedObjects);
		// taxAgencies
		migratedObjects = migrateObjects(TAXAgency.class,
				new TaxAgencyMigrator(), context);
		context.put("TAXAgency", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects(TAXItem.class, new TaxItemMigrator(),
				context);
		context.put("TAXItem", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects(TAXGroup.class,
				new TAXGroupMigrator(), context);
		context.put("TAXGroup", migratedObjects);
		// taxCodes
		migratedObjects = migrateObjects(TAXCode.class, new TAXCodeMigrator(),
				context);
		context.put("TAXCode", migratedObjects);
		// Currencies
		migratedObjects = migrateObjects(Currency.class,
				new CurrencyMigrator(), context);
		context.put("Currency", migratedObjects);
		// paymentTerms
		migratedObjects = migrateObjects(PaymentTerms.class,
				new PaymentTermsMigrator(), context);
		context.put("PaymentTerms", migratedObjects);
		// Customer Groups
		migratedObjects = migrateObjects(CustomerGroup.class,
				new CustomerGroupMigrator(), context);
		context.put("CustomerGroup", migratedObjects);
		// Shipping Methods
		migratedObjects = migrateObjects(ShippingMethod.class,
				new ShippingMethodMigrator(), context);
		context.put("ShippingMethod", migratedObjects);

		// Shipping Methods
		migratedObjects = migrateObjects(ShippingTerms.class,
				new ShippingTermsMigrator(), context);
		context.put("ShippingTerms", migratedObjects);
		// PriceLevels
		migratedObjects = migrateObjects(PriceLevel.class,
				new PriceLevelMigrator(), context);
		context.put("PriceLevel", migratedObjects);
		// Customers
		migratedObjects = migrateObjects(Customer.class,
				new CustomerMigrator(), context);
		context.put("Customer", migratedObjects);
		// Vendor Groups
		migratedObjects = migrateObjects(VendorGroup.class,
				new VendorGroupMigrator(), context);
		context.put("VendorGroup", migratedObjects);
		// locations
		migratedObjects = migrateObjects(Location.class,
				new LocationMigrator(), context);
		context.put("Location", migratedObjects);
		// AccounterClasses
		migratedObjects = migrateObjects(AccounterClass.class,
				new AccounterClassMigrator(), context);
		context.put("AccounterClass", migratedObjects);
		// Jobs
		migratedObjects = migrateObjects(Job.class, new JobMigrator(), context);
		context.put("Job", migratedObjects);
		// Item groups
		migratedObjects = migrateObjects(ItemGroup.class,
				new ItemGroupMigrator(), context);
		context.put("ItemGroup", migratedObjects);
		// Items
		migratedObjects = migrateObjects(Item.class, new ItemMigrator(),
				context);
		context.put("Item", migratedObjects);

		// TODO Transactions

	}

	@SuppressWarnings("unchecked")
	private <T extends CreatableObject> Map<Long, Long> migrateObjects(
			Class<T> clazz, IMigrator<T> migrator, MigratorContext context) {
		List<Long> ids = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray accountsJSON = new JSONArray();
		List<T> accounts = session.createCriteria(clazz, "obj")
				.add(Restrictions.eq("company", company.getId())).list();
		for (T obj : accounts) {
			accountsJSON.put(migrator.migrate(obj, context));
			ids.add(obj.getID());
		}
		// TODO send Rest API Request
		Map<Long, Long> idss = new HashMap<Long, Long>();
		return idss;
	}

	private static String createOrganization() {
		// TODO Create Organization for each company
		return null;
	}
}