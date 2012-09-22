package com.vimukti.accounter.company.initialize;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.NominalCodeRange;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

public class USCompanyInitializer extends CompanyInitializer {

	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	// Account pendingItemReceiptsAccount;
	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	// Account VATFiledLiabilityAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

	/**
	 * Creates new Instance
	 */
	public USCompanyInitializer(Company company) {
		super(company);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	/**
	 * Return the list of Nominal code ranges for the given sub base type of an
	 * Account
	 * 
	 * @param accountSubBaseType
	 * @return Integer[]
	 */
	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (NominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	/**
	 * Initializes all the US default accounts that are useful in the company
	 * 
	 * @param session
	 */
	public void initDefaultUSAccounts() {
		// setDefaultsUSValues();

		// This is the Account created by default for the purpose of US SalesTax
		Account salesTaxPayable = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SALES_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		company.setTaxLiabilityAccount(salesTaxPayable);

		createUSDefaultTaxGroup();
	}

	private void createNominalCodesRanges(Session session) {

		Set<NominalCodeRange> nominalCodesRangeSet = new HashSet<NominalCodeRange>();

		NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
		nominalCodeRange1
				.setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
		nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
		nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange1);

		NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
		nominalCodeRange2
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
		nominalCodeRange2
				.setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
		nominalCodeRange2
				.setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange2);

		NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
		nominalCodeRange3
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
		nominalCodeRange3
				.setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
		nominalCodeRange3
				.setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange3);

		NominalCodeRange nominalCodeRange4 = new NominalCodeRange();
		nominalCodeRange4.setAccountSubBaseType(Account.SUBBASETYPE_EQUITY);
		nominalCodeRange4.setMinimum(NominalCodeRange.RANGE_EQUITY_MIN);
		nominalCodeRange4.setMaximum(NominalCodeRange.RANGE_EQUITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange4);

		NominalCodeRange nominalCodeRange5 = new NominalCodeRange();
		nominalCodeRange5.setAccountSubBaseType(Account.SUBBASETYPE_INCOME);
		nominalCodeRange5.setMinimum(NominalCodeRange.RANGE_INCOME_MIN);
		nominalCodeRange5.setMaximum(NominalCodeRange.RANGE_INCOME_MAX);
		nominalCodesRangeSet.add(nominalCodeRange5);

		NominalCodeRange nominalCodeRange6 = new NominalCodeRange();
		nominalCodeRange6
				.setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
		nominalCodeRange6
				.setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
		nominalCodeRange6
				.setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
		nominalCodesRangeSet.add(nominalCodeRange6);

		NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
		nominalCodeRange7
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
		nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
		nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange7);

		NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
		nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
		nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
		nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange8);

		NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
		nominalCodeRange9
				.setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
		nominalCodeRange9
				.setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
		nominalCodeRange9
				.setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange9);

		NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
		nominalCodeRange10
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
		nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
		nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange10);

		this.setNominalCodeRange(nominalCodesRangeSet);

	}

	public void createUSDefaultTaxGroup() {

		try {

			Session session = HibernateUtil.getCurrentSession();

			// Default TaxGroup Creation
			TAXAgency defaultTaxAgency = new TAXAgency();
			defaultTaxAgency.setCompany(company);
			defaultTaxAgency.setActive(Boolean.TRUE);
			defaultTaxAgency.setName("Tax Agency");
			defaultTaxAgency.setTaxType(TAXAgency.TAX_TYPE_SALESTAX);
			defaultTaxAgency.setPaymentTerm((PaymentTerms) session
					.getNamedQuery("unique.name.PaymentTerms")
					.setEntity("company", company)
					.setParameter("name", "Net Monthly",
							EncryptedStringType.INSTANCE).list().get(0));
			Account salesTaxPayable = (Account) session
					.getNamedQuery("unique.name.Account")
					.setEntity("company", company)
					.setParameter("name", "Sales Tax Payable",
							EncryptedStringType.INSTANCE).list().get(0);
			defaultTaxAgency.setSalesLiabilityAccount(salesTaxPayable);
			defaultTaxAgency.setPurchaseLiabilityAccount(salesTaxPayable);

			defaultTaxAgency.setDefault(true);
			session.save(defaultTaxAgency);

			// Set<TaxRates> taxRates = new HashSet<TaxRates>();
			//
			// TaxRates taxRate = new TaxRates();
			// taxRate.setRate(0);
			// taxRate.setAsOf(new FinanceDate());
			// taxRate.setID(SecureUtils.createID());
			// taxRates.add(taxRate);

			TAXItem defaultTaxItem = new TAXItem(company);
			defaultTaxItem.setActive(Boolean.TRUE);
			defaultTaxItem.setName("None");
			defaultTaxItem.setTaxAgency(defaultTaxAgency);
			defaultTaxItem.setVatReturnBox(null);
			defaultTaxItem.setTaxRate(0);
			defaultTaxItem.setDefault(true);
			session.save(defaultTaxItem);

			// TAXCode defaultTaxCodeforTaxItem = new TAXCode(
			// (TAXItemGroup) defaultTaxItem);
			// session.save(defaultTaxCodeforTaxItem);

			// TAXGroup defaultTaxGroup = new TAXGroup();
			// defaultTaxGroup.setName("Tax Group");
			// defaultTaxGroup.setID(SecureUtils.createID());
			// defaultTaxGroup.setActive(Boolean.TRUE);
			// defaultTaxGroup.setSalesType(true);
			//
			// List<TAXItem> taxItems = new ArrayList<TAXItem>();
			// taxItems.add(defaultTaxItem);
			// defaultTaxGroup.setTAXItems(taxItems);
			// defaultTaxGroup.setGroupRate(0);
			// defaultTaxGroup.setDefault(true);
			// session.save(defaultTaxGroup);
			// TAXCode defaultTaxCodeforTaxGroup = new TAXCode((TAXItemGroup)
			// defaultTaxGroup);
			// session.save(defaultTaxCodeforTaxGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		// super.init();
		initDefaultUSAccounts();
	}

	/*
	 * @Override public void office_expense() { stub
	 * 
	 * }
	 * 
	 * @Override public void motor_veichel_expense() { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * @Override public void travel_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void Cost_of_good_sold() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_direct_cost() { stub
	 * 
	 * }
	 */

	@Override
	String getDateFormat() {
		return AccounterServerConstants.MMddyyyy;
	}

}
