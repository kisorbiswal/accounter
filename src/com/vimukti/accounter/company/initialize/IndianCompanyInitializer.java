package com.vimukti.accounter.company.initialize;

import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

public class IndianCompanyInitializer extends CompanyInitializer {

	/**
	 * @return the preferences
	 */
	public CompanyPreferences getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences
	 *            the preferences to set
	 */
	public void setPreferences(CompanyPreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Creates new Instance
	 */
	public IndianCompanyInitializer(Company company) {
		super(company);
	}

	@Override
	public void init() {
		initDefaultIndiaAccounts();
	}

	private void initDefaultIndiaAccounts() {
		createDefaults();
	}

	public void createDefaults() {

		Account tdsPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.TDS_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Account serviceTaxPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SERVICE_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Account cstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.CENTRAL_SALES_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Session session = HibernateUtil.getCurrentSession();
		PaymentTerms paymentTerms = (PaymentTerms) session
				.getNamedQuery("unique.name.PaymentTerms")
				.setEntity("company", company)
				.setParameter("name", "Net Monthly",
						EncryptedStringType.INSTANCE).uniqueResult();

		TAXAgency defaultTDSAgency = new TAXAgency();
		defaultTDSAgency.setActive(Boolean.TRUE);
		defaultTDSAgency.setTaxType(TAXAgency.TAX_TYPE_TDS);
		defaultTDSAgency.setName("TDS Tax Agency");
		defaultTDSAgency.setVATReturn(0);
		defaultTDSAgency.setPurchaseLiabilityAccount(tdsPayable);
		defaultTDSAgency.setSalesLiabilityAccount(tdsPayable);
		defaultTDSAgency.setDefault(true);
		defaultTDSAgency.setCompany(company);
		defaultTDSAgency.setPaymentTerm(paymentTerms);
		session.save(defaultTDSAgency);

		TAXAgency defaultServiceTAXAgency = new TAXAgency();
		defaultServiceTAXAgency.setActive(Boolean.TRUE);
		defaultServiceTAXAgency.setTaxType(TAXAgency.TAX_TYPE_SERVICETAX);
		defaultServiceTAXAgency.setName("Service Tax Agency");
		defaultServiceTAXAgency.setVATReturn(0);
		defaultServiceTAXAgency.setSalesLiabilityAccount(serviceTaxPayable);
		defaultServiceTAXAgency.setPurchaseLiabilityAccount(serviceTaxPayable);
		defaultServiceTAXAgency.setDefault(true);
		defaultServiceTAXAgency.setCompany(company);
		defaultServiceTAXAgency.setPaymentTerm(paymentTerms);
		session.save(defaultServiceTAXAgency);

		TAXAgency defaultCSTAgency = new TAXAgency();
		defaultCSTAgency.setActive(Boolean.TRUE);
		defaultCSTAgency.setTaxType(TAXAgency.TAX_TYPE_SALESTAX);
		defaultCSTAgency.setName("Central Sales Tax Agency");
		defaultCSTAgency.setVATReturn(0);
		defaultCSTAgency.setSalesLiabilityAccount(cstPayable);
		defaultCSTAgency.setDefault(true);
		defaultCSTAgency.setCompany(company);
		defaultCSTAgency.setPaymentTerm(paymentTerms);
		session.save(defaultCSTAgency);

		TAXItem tdsItem1 = new TAXItem(company);
		tdsItem1.setName("Exempt Purchases");
		tdsItem1.setActive(true);
		tdsItem1.setDescription("Exempt Purchases");
		tdsItem1.setTaxRate(0.0);
		tdsItem1.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem1.setDefault(true);
		tdsItem1.setPercentage(true);
		session.save(tdsItem1);
		TAXItem tdsItem2 = new TAXItem(company);
		tdsItem2.setName("Professional");
		tdsItem2.setActive(true);
		tdsItem2.setDescription("Professional");
		tdsItem2.setTaxRate(10);
		tdsItem2.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem2.setDefault(true);
		tdsItem2.setPercentage(true);
		session.save(tdsItem2);

		TAXItem tdsItem3 = new TAXItem(company);
		tdsItem3.setName("Contractors");
		tdsItem3.setActive(true);
		tdsItem3.setDescription("Contractors");
		tdsItem3.setTaxRate(2);
		tdsItem3.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem3.setDefault(true);
		tdsItem3.setPercentage(true);
		session.save(tdsItem3);

		TAXItem tdsItem4 = new TAXItem(company);
		tdsItem4.setName("Sub Contractors");
		tdsItem4.setActive(true);
		tdsItem4.setDescription("Sub Contractors");
		tdsItem4.setTaxRate(1);
		tdsItem4.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem4.setDefault(true);
		tdsItem4.setPercentage(true);
		session.save(tdsItem4);

		TAXCode none = new TAXCode(company);
		none.setName("None");
		none.setDescription("None");
		none.setTaxable(false);
		none.setActive(true);
		session.save(none);

		// For Assam State
		if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Assam")) {

			String names[] = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"Imports", "Input VAT @ 1%", "Input VAT @ 12.5%",
					"Input VAT @ 4%", "Input VAT @ 13.5%", "Input VAT @ 2%",
					"Input VAT @ 5%", "Inter-State Purchases",
					"Interstate Sale On Indemnity bond", "Inter-State Sales",
					"Inter State Sales Against Form -E1",
					"Inter State Sales Against Form -E2",
					"Inter-State Sales -Exempted",
					"Inter-State Sales-Tax Free", "Other Exempt Sales",
					"Output VAT @1%", "Output VAT @1% ( Schedule IV Items)",
					"Output VAT @12% ( Schedule IV Items)",
					"Output VAT @12.5%",
					"Output VAT @12.5% ( Schedule IV Items)",
					"Output VAT @13.5%",
					"Output VAT @13.5% ( Schedule IV Items)",
					"Output VAT @ 15.5% ( Schedule IV Items)",
					"Output VAT @16.5% ( Schedule IV Items)", "Output VAT @2%",
					"Output VAT @2% ( Schedule IV Items)",
					"Output VAT @20% ( Schedule IV Items)",
					"Output VAT @22% ( Schedule IV Items)",
					"Output VAT @24% ( Schedule IV Items)",
					"Output VAT @25.75% ( Schedule IV Items)",
					"Output VAT @27% ( Schedule IV Items)",
					"Output VAT @27.5% ( Schedule IV Items)", "Output VAT @4%",
					"Output VAT @4% ( Schedule IV Items)", "Output VAT @5%",
					"Output VAT @5% ( Schedule IV Items)",
					"Output VAT @6% ( Schedule IV Items)",
					"Output VAT @9% ( Schedule IV Items)",
					"Purchases @1% ( Schedule IV Items)",
					"Purchases @12% ( Schedule IV Items)",
					"Purchases @12.5% ( Schedule IV Items)",
					"Purchases @13.5% ( Schedule IV Items)",
					"Purchases @15.5% ( Schedule IV Items)",
					"Purchases @16.5% ( Schedule IV Items)",
					"Purchases @2% ( Schedule IV Items)",
					"Purchases @20% ( Schedule IV Items)",
					"Purchases @22% ( Schedule IV Items)",
					"Purchases @24% ( Schedule IV Items)",
					"Purchases @25.75% ( Schedule IV Items)",
					"Purchases @27% ( Schedule IV Items)",
					"Purchases @27.5% ( Schedule IV Items)",
					"Purchases @4% ( Schedule IV Items)",
					"Purchases @5% ( Schedule IV Items)",
					"Purchases @6% ( Schedule IV Items)",
					"Purchases @9% ( Schedule IV Items)",
					"Purchases -Capital Goods @12.5%",
					"Purchases -Capital Goods @13.5%",
					"Purchases -Capital Goods @4%",
					"Purchases -Capital Goods @5%", "Purchases -Exempt",
					"Purchases -From Unregistered dealers", "Purchases-Others",
					"Purchase Tax @1%", "Purchase Tax @12%",
					"Purchase Tax @12.5%", "Purchase Tax @13.5%",
					"Purchase Tax @15.5%", "Purchase Tax @16.5%",
					"Purchase Tax @2%", "Purchase Tax @20%",
					"Purchase Tax @22%", "Purchase Tax @24%",
					"Purchase Tax @25.75%", "Purchase Tax @27%",
					"Purchase Tax @27.5%", "Purchase Tax @4%",
					"Purchase Tax @5%", "Purchase Tax @6%", "Purchase Tax @9%",
					"Sales (Against Form -H Deemed Export)", "Sales-Exempt",
					"Sales to Diplomatic Missions & U.N. Etc.",
					"Sales-Zero Rated ( Inter State)",
					"Works Contract -Labour /Other Charges" };

			boolean isSales[] = { true, true, true, false, false, false, false,
					false, false, false, false, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, true, true, true, true,
					false };
			double rate[] = { 0, 0, 0, 0, 1, 12.5, 4, 13.5, 2, 5, 0, 0, 0, 0,
					0, 0, 0, 0, 1, 1, 12, 12.5, 12.5, 13.5, 13.5, 15.5, 16.5,
					2, 2, 20, 22, 24, 25.75, 27, 27.5, 4, 4, 5, 5, 6, 9, 1, 12,
					12.5, 13.5, 15.5, 16.5, 2, 20, 22, 24, 25.75, 27, 27.5, 4,
					5, 6, 9, 12.5, 13.5, 4, 5, 0, 0, 0, 1, 12, 12.5, 13.5,
					15.5, 16.5, 2, 20, 22, 24, 25.75, 27, 27.5, 4, 5, 6, 9, 0,
					0, 0, 0, 0 };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rate[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rate[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Bihar")) {
			// for Bihar state
			String names[] = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Inward ( Within State )",
					"Consignment /Branch Transfer Outward",
					"Consignment /Branch Transfer Outward ( Within State)",
					"Exports", "Imports",
					"Input Tax Credits On Purchase from URD's @1%",
					"Input Tax Credits On Purchase from URD's @12.5%",
					"Input Tax Credits On Purchase from URD's @4%",
					"Input VAT @1%", "Input VAT @12.5%", "Input VAT @4%",
					"Inter-State Purchases",
					"Inter-State Purchases ( Schedule IV Goods )",
					"Inter-State Sales", "Inter-State Sales-Exempted",
					"Output VAT @1%", "Output VAT @1% to URD's ",
					"Output VAT @12.5%", "Output VAT @12.5% to URD's",
					"Output VAT @20% on ATF",
					"Output VAT @20% on Country Liquor",
					"Output VAT @20% on Diesel", "Output VAT @20% on IMFL/IFL",
					"Output VAT @20% On Natural Gas",
					"Output VAT @20% on Petrol", "Output VAT @24% on Petrol",
					"Output VAT @27% on Motor Spirit",
					"Output VAT @27% on Petrol", "Output VAT @29% on ATF",
					"Output VAT @34% on Country Liquor", "Output VAT @4%",
					"Output VAT @4% to URD's", "Output VAT @41% on IMFL/IFL",
					"Output VAT @50% on Country Liquor",
					"Output VAT @50% on IMFL/IFL",
					"Purchase From URD's -Taxable Goods @1%",
					"Purchase From URD's -Taxable Goods @12.5%",
					"Purchase From URD's -Taxable Goods @4%",
					"Purchases -Capital Goods @12.5%",
					"Purchases -Capital Goods @4%", "Purchases-Exempt",
					"Purchases-Exempt ( Inter State)",
					"Purchases-Exempt ( Schedule IV Goods)",
					"Purchases-From Unregistered Dealers",
					"Purchases-MRP Based", "Purchases-Others",
					"Purchase Tax @1%", "Purchase Tax @12.5%",
					"Purchase Tax @4%",
					"Sale in the course of Inter-State Trade Or Commerce",
					"Sales-Exempt", "Sales-Exempt ( Schedule IV Goods)",
					"Sales MRP Based",
					"Sales to Diplomatic Missions & U.N . Etc.",
					"Sales -to Local Agents",
					"Sales to other oil company -ATF",
					"Sales to other oil company-Diesel",
					"Sales to other oil company-Motor Spirit",
					"Sales to other oil company-Natural Gas",
					"Sales to URD's -MRP Based", "Tax Paid Sales- ATF",
					"Tax Paid Sales- Diesel", "Tax Paid Sales- Motor Spirit",
					"Tax Paid Sales-Natural Gas",
					"Works Contract -Labour/Other Charges" };
			double[] rates = { 0, 0, 0, 0, 0, 0, 1, 12.5, 4, 1, 12.5, 4, 0, 0,
					0, 0, 1, 1, 12.5, 12.5, 20, 20, 20, 20, 20, 20, 24, 27, 27,
					29, 34, 4, 4, 41, 50, 50, 1, 12.5, 4, 12.5, 4, 0, 0, 0, 0,
					0, 0, 1, 12.5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0 };

			boolean isSales[] = { true, true, true, true, true, false, false,
					false, false, false, false, false, false, false, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, false };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Inward ( Within State )")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward ( Within State)")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Chattisgarh")) {
			// for Chattisgarh state

			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"Imports", "Input VAT @1%", "Input VAT @12.5%",
					"Input VAT @14%", "Input VAT @2%", "Input VAT @4%",
					"Input VAT @5%", "Inter-State Purchases",
					"Inter-State Sales", "Output VAT @1%", "Output VAT @12.5%",
					"Output VAT @14%", "Output VAT @2%", "Output VAT @25%",
					"Output VAT @4%", "Output VAT @5%",
					"Purchases-Capital Goods@12.5%",
					"Purchases-Capital Goods@14%",
					"Purchases-Capital Goods @4%",
					"Purchases-Capital Goods@5%", "Purchases-Exempt",
					"Purchases- From Unregistered Dealers", "Purchases-Others",
					"Sales-Exempt", "Sales on which Tax Paid by the principal",
					"Sales to a Dealer under SEZ/STP/EHTP",
					"Sales-to Local Agents", "Sales-U/S 2(x)" };
			double[] rates = { 0, 0, 0, 0, 1, 12.5, 14, 2, 4, 5, 0, 0, 1, 12.5,
					14, 2, 25, 4, 5, 12.5, 14, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0 };
			boolean[] isSales = { true, true, true, false, false, false, false,
					false, false, false, false, true, true, true, true, true,
					true, true, true, false, false, false, false, false, false,
					false, true, true, true, true, true };
			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}
		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Karnataka")) {
			// for Karnataka state
			
			
			
			
			
		}
		else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Jharkand")) {
			// for Jharkand state
			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Inward ( Within State )",
					"Consignment /Branch Transfer Outward",
					"Consignment /Branch Transfer Outward ( Within State)",
					"Exports", "Imports", "Input VAT @1%", "Input VAT @12%",
					"Input VAT @18%", "Input VAT @35%", "Input VAT @20%",
					"Input VAT @4%", "Input VAT @4% - Others",
					"Inter-State Purchases", "Inter-State Sales",
					"Labour, Service, Job work & Other Charges",
					"Output VAT @1%", "Output VAT @12.5%", "Output VAT @18%",
					"Output VAT @18% On Diesel",
					"Output VAT @18% to Another Oil Company- Diesel",
					"Output VAT @20%", "Output VAT @20% on ATF",
					"Output VAT @20% on Diesel", "Output VAT @20% on Petrol",
					"Output VAT @20% - Other Goods",
					"Output VAT @20% to Another Oil Company- Diesel",
					"Output VAT @20% to Another Oil Company- Petrol",
					"Output VAT @20% to Another Oil Company- ATF",
					"Output VAT @35%", "Output VAT @35% on Country Liquor",
					"Output VAT @35% on IMFL/IFL", "Output VAT @4%",
					"Output VAT @50% on IMFL/IFL",
					"Output VAT -Works Contract @12.5% ",
					"Output VAT -Works Contract @4% ",
					"Purchase From URD's -Taxable Goods @1%",
					"Purchase From URD's -Taxable Goods @12.5%",
					"Purchase From URD's -Taxable Goods @20%",
					"Purchase From URD's -Taxable Goods @35%",
					"Purchase From URD's -Taxable Goods @4%",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @4%", "Purchases-Exempt",
					"Purchases-Exempt ( Entry Tax Paid)",
					"Purchases-FROM Exempt Units",
					"Purchases-From Unregistered Dealers", "Purchases-Others",
					"Purchase Tax @1%", "Purchase Tax @12.5%",
					"Purchase Tax @20%", "Purchase Tax @35%",
					"Purchase Tax @4%", "Sales-As an Exempted Unit",
					"Sales-Exempt", "Sales-to Local Agents",
					"Taxable Purchase -ATF @20%",
					"Taxable Purchase -Country Liquor @35%",
					"Taxable Purchase -Diesel @18%",
					"Taxable Purchase -Diesel @20%",
					"Taxable Purchase -IMFL @35%",
					"Taxable Purchase -IMFL @50%",
					"Taxable Purchase -Other Goods @20%",
					"Taxable Purchase -Petrol @20%",
					"Tax Paid Purchase -ATF@20%",
					"Tax Paid Purchase -Country Liquor @35%",
					"Tax Paid Purchase -Diesel @18%",
					"Tax Paid Purchase -Diesel @20%",
					"Tax Paid Purchase -IMFL @35%",
					"Tax Paid Purchase -IMFL @50%",
					"Tax Paid Purchase -Other Goods @20%",
					"Tax Paid Purchase -Petrol @20%",
					"Tax Paid Sales -ATF@20%",
					"Tax Paid Sales -Country Liquor @35%",
					"Tax Paid Sales -Diesel @18%",
					"Tax Paid Sales -Diesel @20%", "Tax Paid Sales -IMFL @35%",
					"Tax Paid Sales -IMFL @50%",
					"Tax Paid Sales -Other Goods @20%",
					"Tax Paid Sales -Petrol @20%" };
			double[] rates = { 0, 0, 0, 0, 0, 0, 1, 12, 18, 35, 20, 4, 4, 0, 0,
					0, 1, 12.5, 18, 18, 18, 20, 20, 20, 20, 20, 20, 20, 20, 35,
					35, 35, 4, 50, 12.5, 4, 1, 12.5, 20, 35, 4, 12.5, 4, 0, 0,
					0, 0, 0, 1, 12.5, 20, 35, 4, 0, 0, 0, 20, 35, 18, 20, 35,
					50, 20, 20, 20, 35, 18, 20, 35, 50, 20, 20, 20, 35, 18, 20,
					35, 50, 20, 20 };
			boolean[] isSales = { true, true, true, true, true, false, false,
					false, false, false, false, false, false, false, true,
					false, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, true, true, true, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, true, true, true, true,
					true, true, true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Inward ( Within State )")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward ( Within State)")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Jammu and Kashmir")) {
			// for Jammu and Kashmir state
			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"Imports", "Input VAT @1%", "Input VAT @12.5%",
					"Input VAT @13.5%", "Input VAT @4%", "Input VAT @5%",
					"Inter-State Purchases", "Inter-State Sales",
					"Output VAT @1%", "Output VAT @12.5%", "Output VAT @13.5%",
					"Output VAT @4%", "Output VAT @5%",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @13.5%",
					"Purchases-Capital Goods @4%",
					"Purchases-Capital Goods @5%", "Purchases-Exempt",
					"Purchases From Registered Dealers -No Tax Paid",
					"Purchases From TOT/Casual Dealer",
					"Purchases-From Unregistered Dealers", "Purchases-Others",
					"Purchases Tax @1%", "Purchases Tax @12.5%",
					"Purchases Tax @13.5%", "Purchases Tax @4%",
					"Purchases Tax @5%", "Sales-Exempt", "Sales-Zero Rated" };

			double[] rates = { 0, 0, 0, 0, 1, 12.5, 13.5, 4, 5, 0, 0, 1, 12.5,
					13.5, 4, 5, 12.5, 13.5, 4, 5, 0, 0, 0, 0, 0, 1, 12.5, 13.5,
					4, 5, 0, 0 };

			boolean[] isSales = { true, true, true, false, false, false, false,
					false, false, false, true, true, true, true, true, true,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, true, true };
			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}
		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Chandigarh")) {
			// for Chandigarh state
			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"Imports", "Input VAT @ 1%", "Input VAT @ 12.5%",
					"Input VAT @ 20%", "Input VAT @ 22%", "Input VAT @ 30%",
					"Input VAT @ 4%", "Input VAT @ 8.8%",
					"Inter-State Purchases", "Inter-State Sales",
					"Notional ITC", "Output VAT 1%",
					"Output VAT 1% on Works Contract", "Output VAT 12.5%",
					"Output VAT 12.5% on Works Contract", "Output VAT 20%",
					"Output VAT 20% on works Contract", "Output VAT 22%",
					"Output VAT 30%", "Output VAT 4%",
					"Output VAT 4% On works Contract", "Output VAT 8.8%",
					"Output VAT- Works Contract @1%",
					"Output VAT- Works Contract @12.5%",
					"Output VAT- Works Contract @20%",
					"Output VAT- Works Contract @4%",
					"Purchase from URD's – Taxable Goods at 1%",
					"Purchase from URD's – Taxable Goods at 12.5%",
					"Purchase from URD's – Taxable Goods at 20%",
					"Purchase from URD's – Taxable Goods at 22%",
					"Purchase from URD's – Taxable Goods at 30%",
					"Purchase from URD's – Taxable Goods at 4%",
					"Purchase from URD's – Taxable Goods at 8.8%",
					"Purchases – Capital Goods at 12.5%",
					"Purchases – Capital Goods at 4%",
					"Purchases – Capital Goods at 4% ( Notional)",
					"Purchases- Exempt", "Purchases - from Exempt Units",
					"Purchases – From Unregistered Dealers",
					"Purchases -Others", "Purchases -Schedule H Items",
					"Purchase Tax at 1%", "Purchase Tax at 12.5%",
					"Purchase Tax at 20%", "Purchase Tax at 22%",
					"Purchase Tax at 30%", "Purchase Tax at 4%",
					"Purchase Tax at 8.8%", "Sales-As an Exempted unit",
					"Sales-Exempt", "Sales to Diplomatic Mission U.N etc..",
					"Sales-Zero Rated" };
			double rates[] = { 0, 0, 0, 0, 1, 12.5, 20, 22, 30, 4, 8.8, 0, 0,
					0, 1, 1, 12.5, 12.5, 20, 20, 22, 30, 4, 4, 8.8, 1, 12.5,
					20, 4, 1, 12.5, 20, 22, 30, 4, 8.8, 12.5, 4, 4, 0, 0, 0, 0,
					0, 1, 12.5, 20, 22, 30, 4, 8.8, 0, 0, 0, 0 };
			boolean isSales[] = { true, true, true, false, false, false, false,
					false, false, false, false, false, true, true, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, true, true, true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i].equalsIgnoreCase("Notional ITC")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}
		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Gujarat")) {
			// for Gujarat state
			String[] names = { "Additional Purchase Tax @1%",
					"Additional Purchase Tax @2.5%",
					"Additional Tax on Purchase of Capital Goods @1%",
					"Additional Tax on Purchase of Capital Goods @2.5%",
					"Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Inward ( Mfd Goods)",
					"Consignment /Branch Transfer Inward ( Within State )",
					"Consignment /Branch Transfer Outward",
					"Consignment /Branch Transfer Outward ( Mfd Goods)",
					"Consignment /Branch Transfer Outward ( Within State )",
					"Exports", "Imports", "Input Additional Tax @1%",
					"Input Additional Tax @2.5%",
					"Input Additional Tax on URD Purchases @1%",
					"Input Additional Tax on URD Purchases @2.5%",
					"Input Tax Credit on Purchases From URD's @1%",
					"Input Tax Credit on Purchases From URD's @12.5%",
					"Input Tax Credit on Purchases From URD's @15%",
					"Input Tax Credit on Purchases From URD's @16%",
					"Input Tax Credit on Purchases From URD's @20%",
					"Input Tax Credit on Purchases From URD's @25%",
					"Input Tax Credit on Purchases From URD's @4%",
					"Input Tax Credit on Purchases From URD's @60%",
					"Input VAT @1%", "Input VAT @12.5%", "Input VAT @13%",
					"Input VAT @15%", "Input VAT @16%", "Input VAT @17.5%",
					"Input VAT @20%", "Input VAT @21%", "Input VAT @23%",
					"Input VAT @24%", "Input VAT @25%", "Input VAT @26%",
					"Input VAT @30%", "Input VAT @38%", "Input VAT @4%",
					"Input VAT @60%", "Inter-State Purchases",
					"Inter-State Sales", "Inter-State Sales-Exempted",
					"Labour , Service , Job Work & other charges",
					"Non-Creditable Purchases", "Other Exempt Purchases",
					"Other Exempt Sales", "Output Additional Tax @1%",
					"Output Additional Tax @2.5%", "Output VAT @1%",
					"Output VAT @12.5%", "Output VAT@13%", "Output VAT @15%",
					"Output VAT @16%", "Output VAT @17.5%", "Output VAT @20%",
					"Output VAT @21%", "Output VAT @23%", "Output VAT @24%",
					"Output VAT @25%", "Output VAT @26%", "Output VAT @30%",
					"Output VAT @38%", "Output VAT @4%", "Output VAT @60%",
					"Purchase From URD's -Exempted",
					"Purchase From URD's -Taxable Goods @ 1%",
					"Purchase From URD's -Taxable Goods @ 12.5%",
					"Purchase From URD's -Taxable Goods @ 15%",
					"Purchase From URD's -Taxable Goods @ 16%",
					"Purchase From URD's -Taxable Goods @ 20%",
					"Purchase From URD's -Taxable Goods @ 25%",
					"Purchase From URD's -Taxable Goods @ 4%",
					"Purchase From URD's -Taxable Goods @ 60%",
					"Purchases ( Against Form-H Deemed Export)",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @4%", "Purchases-Exempt",
					"Purchases-From Unregistered Dealers",
					"Purchases in the course of Import into India",
					"Purchases -Others", "Purchases-Zero Rated",
					"Purchases Tax @1%", "Purchases Tax @12.5%",
					"Purchases Tax @15.%", "Purchases Tax @16%",
					"Purchases Tax @20%", "Purchases Tax @25%",
					"Purchases Tax @4%", "Purchases Tax @60%",
					"Sales ( Against Form-H Deemed Export)", "Sales-Exempt",
					"Sales in the course of Import into India",
					"sales-Zero Rated" };

			double rates[] = { 1, 2.5, 1, 2.5, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2.5,
					1, 2.5, 1, 12.5, 15, 16, 20, 25, 4, 60, 1, 12.5, 13, 15,
					16, 17.5, 20, 21, 23, 24, 25, 26, 30, 38, 4, 60, 0, 0, 0,
					0, 0, 0, 0, 1, 2.5, 1, 12.5, 13, 15, 16, 17.5, 20, 21, 23,
					24, 25, 26, 30, 38, 4, 60, 0, 1, 12.5, 15, 16, 20, 25, 4,
					60, 0, 12.5, 4, 0, 0, 0, 0, 0, 1, 12.5, 15, 16, 20, 25, 4,
					60, 0, 0, 0, 0 };
			boolean isSales[] = { false, false, false, false, true, true, true,
					true, true, true, true, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, true, true, false, false, false, true, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, true, true, true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Inward ( Within State )")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Inward ( Mfd Goods)")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward ( Mfd Goods)")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward ( Within State )")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Goa")) {
			// for Goa state

			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"Imports", "Input VAT @ 1%",
					"Input VAT @ 10% ( All types of IMFL & Country Liquor )",
					"Input VAT @ 12.5%", "Input VAT @ 15%", "Input VAT @ 20%",
					"Input VAT @ 20% ( Other than HSD & Petro Products )",
					"Input VAT @ 21%", "Input VAT @ 22%",
					"Input VAT @ 22% ( All Types of IMFL & Foreign Liquor)",
					"Input VAT @ 4%", "Input VAT @ 4% -( Industrial Input)",
					"Input VAT @ 5%", "Input VAT @ 5% -( Industrial Input)",
					"Input VAT @8% ( All Types of CFL Bulbs & Tubes)",
					"Inter -State Purchases", "Inter-State Sales",
					"Output VAT @1%",
					"Output VAT @ 10% ( All types of IMFL & Country Liquor )",
					"Output VAT @12.5%", "Output VAT @15%", "Output VAT @20%",
					"Output VAT @ 20% ( Other than HSD & Petro Products )",
					"Output VAT @21%", "Output VAT @22%",
					"Output VAT @ 22% ( All Types of IMFL & Foreign Liquor)",
					"Output VAT @4%", "Output VAT @4% ( Capital Goods)",
					"Output VAT @4% ( Industrial Input)", "Output VAT @5%",
					"Output VAT @5% ( Capital Goods)",
					"Output VAT @5% ( Industrial Input)",
					"Output VAT @8% ( All Types of CFL Bulbs & Tubes)",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @4%",
					"Purchases-Capital Goods @5%", "Purchases-exempt",
					"Purchases-From Unregistered Dealers", "Purchases-Others",
					"Sales-Exempt", "Sales to Diplomatic Missions & U.N. Etc",
					"Sales -to Local Agents", "Sales-Zero Rated" };

			double rates[] = { 0, 0, 0, 0, 1, 10, 12.5, 15, 20, 20, 21, 22, 22,
					4, 4, 5, 5, 8, 0, 0, 1, 10, 12.5, 15, 20, 20, 21, 22, 22,
					4, 4, 4, 5, 5, 5, 8, 12.5, 4, 5, 0, 0, 0, 0, 0, 0, 0 };

			boolean isSales[] = { true, true, true, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, false, false, false, false, false, false, true,
					true, true, true, };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Himachal Pradesh")) {
			// for Himachal Pradesh state
			String[] names = {
					"Consignment/Branch Transfer – In the course of Export",
					"Consignment/Branch Transfer Inward",
					"Consignment/Branch Transfer Inward ( Within state)",
					"Consignment/Branch Transfer Outward",
					"Consignment/Branch Transfer -To Local Agents ( Regd)",
					"Exports", "Imported Into the state", "Imports",
					"Input VAT @1%", "Input VAT @12.5%", "Input VAT @4%",
					"Input VAT @5%", "Inter-State Purchases",
					"Inter-State Sales", "Output VAT @1%", "Output VAT @12.5%",
					"Output VAT @14%", "Output VAT @20%", "Output VAT @25%",
					"Output VAT @4%", "Output VAT @5%",
					"Purchase in the course of Export out of India",
					"Purchase in the course of Inter-State Trade",
					"Purchase outside the state ( for sale outside)",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @4%",
					"Purchases-Capital Goods @5%", "Purchases-Exempt",
					"Purchases- From Unregistered Dealers", "Purchases-Others",
					"Sale in the course of Export out of India",
					"Sale in the course of Inter-state Trade or Commerce",
					"Sales-Exempt", "Sales in the course of Import into India",
					"Sales Outside the state (of Purc Outside State)",
					"Sales to Diplomatic Missions & U.N. Etc" };
			double rates[] = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 12.5, 4, 5, 0, 0, 1,
					12.5, 14, 20, 25, 4, 5, 0, 0, 0, 12.5, 4, 5, 0, 0, 0, 0, 0,
					0, 0, 0, 0 };

			boolean[] isSales = { true, true, true, true, true, true, false,
					false, false, false, false, false, false, true, true, true,
					true, true, true, true, true, false, false, false, false,
					false, false, false, false, false, true, true, true, true,
					true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment/Branch Transfer – In the course of Export")
							|| names[i]
									.equalsIgnoreCase("Consignment/Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment/Branch Transfer Inward ( Within state)")
							|| names[i]
									.equalsIgnoreCase("Consignment/Branch Transfer Outward")
							|| names[i]
									.equalsIgnoreCase("Consignment/Branch Transfer -To Local Agents ( Regd)")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Haryana")) {
			// for Haryana state

			String[] names = { "Imported Into State", "Input Surcharge @5%",
					"Output Surcharge @5%", "Purchase @1 % Tax Rate",
					"Purchase @10 % Tax Rate", "Purchase @12 % Tax Rate",
					"Purchase @12.5 % Tax Rate", "Purchase @20 % Tax Rate",
					"Purchase @20.5 % Tax Rate", "Purchase @4 % Tax Rate",
					"Purchase @5% Tax Rate", "Purchase @8.8 % Tax Rate",
					"Purchased in the course of Export out of India",
					"Purchased in the course of Import into India",
					"Purchased in the course of Inter-State-Trade",
					"Purchased Outside the state ( for sale outside)",
					"Purchase of Goods at Lower Rate of Tax-4%",
					"Purchase of Goods at Lower Rate of Tax-5%",
					"Purchase of Goods From Lumpsum Dealers",
					"Purchase of Goods From Unregistered Dealers",
					"Purchases-Capital Goods Consumed in Mining",
					"Purchases -Capital Goods used for Mfg Exempt Goods",
					"Purchases -Capital Goods used in Power Generation",
					"Purchases -Capital Goods used in Telecom Network",
					"Purchases-Other Goods-Disposed Otherwise Than sale",
					"Purchases-Other Goods-Exported Out Of State",
					"Purchases -Other Goods- for Telecome/Mining/Power",
					"Purchases-Other Goods-Left in Stock",
					"Purchases -Other Goods used for Mfg Exempt Goods",
					"Purchases-Other Goods- Used in Pkg -Disp O/W by sale",
					"Purchases-Other Goods-Used in Pkg of Export Goods",
					"Purchases-Paddy-for Export out of India",
					"Purchases-Petroleum Based fuels, Natural Gas etc..",
					"Purchases-Rice -For Export out of India",
					"Received for sale From dealers Regd Under VAT",
					"Sale in the course of Export out of India-5(1)",
					"Sale in the course of Export out of India-5(3)",
					"Sale in the course of Import into India",
					"Sale in the course of Inter-State Trade",
					"Sale of Goods at Lower Rate of Tax -4%",
					"Sale of Goods at Lower Rate of Tax -5%",
					"Sale of Goods to UNICEF and WHO",
					"Sale of Goods to Unregistered Dealers @1% Rate",
					"Sale of Goods to Unregistered Dealers @10% Rate",
					"Sale of Goods to Unregistered Dealers @12% Rate",
					"Sale of Goods to Unregistered Dealers @12.5% Rate",
					"Sale of Goods to Unregistered Dealers @20% Rate",
					"Sale of Goods to Unregistered Dealers @25% Rate",
					"Sale of Goods to Unregistered Dealers @4% Lower Rate",
					"Sale of Goods to Unregistered Dealers @4% Rate",
					"Sale of Goods to Unregistered Dealers @5% Lower Rate",
					"Sale of Goods to Unregistered Dealers @5% Rate",
					"Sale of Goods to Unregistered Dealers @8.8% Rate",
					"Sale outside the state ( of Purc Outside State)",
					"Sale @1% Tax Rate", "Sale @10% Tax Rate",
					"Sale @12% Tax Rate", "Sale @12.5% Tax Rate",
					"Sale @20% Tax Rate", "Sale @25% Tax Rate",
					"Sale @4% Tax Rate", "Sale @5% Tax Rate",
					"Sale @8.8% Tax Rate", "Sales-Exempt",
					"Value of Goods Disposed off otherwise Than by sale",
					"Value of Goods Exported Out of State ( Consgn.Trfs)",
					"Value of Goods Sent for sale to Local Agents" };

			double[] rates = { 0, 5, 5, 1, 10, 12, 12.5, 20, 20.5, 4, 5, 8.8,
					0, 0, 0, 0, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 4, 5, 0, 1, 10, 12, 12.5, 20, 25, 4,
					4, 5, 5, 8.8, 0, 1, 10, 12, 12.5, 20, 25, 4, 5, 8.8, 0, 0,
					0, 0 };

			boolean[] isSales = { true, false, true, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i].equalsIgnoreCase("Imported Into State")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Delhi")) {
			// for Delhi state
			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "Exports",
					"High Sea Sales", "Imports", "Input VAT @1%",
					"Input VAT @12.5%", "Input VAT @4%", "Input VAT @20%",
					"Input VAT @5%", "Input VAT -Works Contract @12.5%",
					"Input VAT -Works Contract @5%",
					"Input VAT -Works Contract @4%", "Inter-State Purchases",
					"Inter-State Purchases Against Form -E1",
					"Inter-State Purchases Against Form -E2",
					"Inter-State Purchases-Exempted", "Inter-State Sales",
					"Inter-State Sales Against Form -E1",
					"Inter-State Sales Against Form -E2",
					"Interstate Sales at low rate",
					"Inter-State Sales-Exempted",
					"Labour, Service, Job Work & Other charges",
					"Output VAT 1%", "Output VAT 12.5% on Works Contract",
					"Output VAT 12.5%", "Output VAT 20%",
					"Output VAT 4% on Works Contract", "Output VAT 4%",
					"Output VAT 5%", "Output VAT -Works Contract @12.5%",
					"Output VAT -Works Contract @4%",
					"Output VAT -Works Contract @5%",
					"Purchases-Capital Goods @12.5%",
					"Purchases-Capital Goods @4%",
					"Purchases-Capital Goods @5%", "Purchases-exempt",
					"Purchases-From Unregistered Dealers", "Purchases-Others",
					"Sales ( Against Form -H Deemed Export)", "Sales-Exempt",
					"Sales to Diplomatic Missions & U.N. Etc",
					"Sales Zero Rated ( Inter state)",
					"Works Contract -Interstate" };
			double rates[] = { 0, 0, 0, 0, 0, 1, 12.5, 4, 20, 5, 12.5, 5, 4, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 12.5, 12.5, 20, 4, 4, 5,
					12.5, 4, 5, 12.5, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0 };
			boolean isSales[] = { true, true, true, true, false, false, false,
					false, false, false, false, false, false, false, false,
					false, false, true, true, true, true, true, false, false,
					false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, true,
					true, true, true, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")
							|| names[i]
									.equalsIgnoreCase("Works Contract -Interstate")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Arunachal Pradesh")) {
			// for Arunachal Pradesh state
			String[] names = { "Consignment /Branch Transfer Inward",
					"Consignment /Branch Transfer Outward", "CST 1%",
					"CST 12.5%", "CST 14.5%", "CST 2% Against Form C",
					"CST 4%", "Exports", "Imports", "Input VAT @ 1%",
					"Input VAT @ 12.5%", "Input VAT @ 4%", "Input VAT @ 14.5%",
					"Inter-State Purchases", "Interstate Purchases @1%",
					"Interstate Purchases @12.5%", "Interstate Purchases @20%",
					"Interstate Purchases @4%",
					"Interstate Purchases @2% Against Form c",
					"Inter-State Sales", "Output VAT @1%",
					"Output VAT @1% on Works Contract", "Output VAT @12.5%",
					"Output VAT @20%", "Output VAT @4%",
					"Purchases -Capital goods @12.5%",
					"Purchases -Capital goods @4%", "Purchases -Exempt",
					"Purchases – FROM Unregistered Dealers",
					"Purchases-Others", "Sales-Exempt" };

			double rates[] = { 0, 0, 1, 12.5, 14.5, 2, 4, 0, 0, 1, 12.5, 4,
					14.5, 0, 1, 12.5, 20, 4, 2, 0, 1, 1, 12.5, 20, 4, 12.5, 4,
					0, 0, 0, 0 };
			boolean isSales[] = { true, true, true, true, true, true, true,
					true, false, false, false, false, false, false, false,
					false, false, false, false, true, true, true, true, true,
					true, false, false, false, false, false, true };

			for (int i = 0; i < names.length; i++) {
				TAXItem item = new TAXItem(company);
				item.setName(names[i]);
				item.setActive(true);
				item.setDescription(names[i]);
				item.setTaxRate(rates[i]);
				item.setTaxAgency(defaultCSTAgency);
				item.setVatReturnBox(null);
				item.setDefault(true);
				item.setPercentage(true);
				session.save(item);

				if (rates[i] >= 1) {
					TAXCode code = new TAXCode(company);
					code.setName(names[i]);
					code.setDescription(names[i]);
					code.setTaxable(true);
					code.setActive(true);
					if (isSales[i]) {
						code.setTAXItemGrpForSales(item);
					} else {
						code.setTAXItemGrpForPurchases(item);
					}
					if (names[i]
							.equalsIgnoreCase("Consignment /Branch Transfer Inward")
							|| names[i]
									.equalsIgnoreCase("Consignment /Branch Transfer Outward")) {
						code.setTAXItemGrpForSales(item);
						code.setTAXItemGrpForPurchases(item);
					}
					code.setDefault(true);
					session.save(code);
				}
			}

		} else if (company.getRegisteredAddress().getStateOrProvinence()
				.equalsIgnoreCase("Andhra Pradesh")) {
			// for Andhra Pradesh state
			TAXItem inwardItem = new TAXItem(company);
			inwardItem.setName("Branch Transfer Inward");
			inwardItem.setActive(true);
			inwardItem.setDescription("Branch Transfer Inward");
			inwardItem.setTaxRate(0.0);
			inwardItem.setTaxAgency(defaultCSTAgency);
			inwardItem.setVatReturnBox(null);
			inwardItem.setDefault(true);
			inwardItem.setPercentage(true);
			session.save(inwardItem);

			TAXItem outwardItem = new TAXItem(company);
			outwardItem.setName("Branch Transfer Outward");
			outwardItem.setActive(true);
			outwardItem.setDescription("Branch Transfer outward");
			outwardItem.setTaxRate(0.0);
			outwardItem.setTaxAgency(defaultCSTAgency);
			outwardItem.setVatReturnBox(null);
			outwardItem.setDefault(true);
			outwardItem.setPercentage(true);
			session.save(outwardItem);

			TAXItem cst1Item = new TAXItem(company);
			cst1Item.setName("CST 1%");
			cst1Item.setActive(true);
			cst1Item.setDescription("Central Service Tax 1%");
			cst1Item.setTaxRate(1.0);
			cst1Item.setTaxAgency(defaultCSTAgency);
			cst1Item.setVatReturnBox(null);
			cst1Item.setDefault(true);
			cst1Item.setPercentage(true);
			session.save(cst1Item);

			TAXItem cst12Item = new TAXItem(company);
			cst12Item.setName("CST 12.5%");
			cst12Item.setActive(true);
			cst12Item.setDescription("Central Service Tax 12.5%");
			cst12Item.setTaxRate(12.5);
			cst12Item.setTaxAgency(defaultCSTAgency);
			cst12Item.setVatReturnBox(null);
			cst12Item.setDefault(true);
			cst12Item.setPercentage(true);
			session.save(cst12Item);

			TAXItem cst14Item = new TAXItem(company);
			cst14Item.setName("CST 14.5%");
			cst14Item.setActive(true);
			cst14Item.setDescription("Central Service Tax 14.5%");
			cst14Item.setTaxRate(14.5);
			cst14Item.setTaxAgency(defaultCSTAgency);
			cst14Item.setVatReturnBox(null);
			cst14Item.setDefault(true);
			cst14Item.setPercentage(true);
			session.save(cst14Item);

			TAXItem cst2AgainstFormCItem = new TAXItem(company);
			cst2AgainstFormCItem.setName("CST 2% Against Form C");
			cst2AgainstFormCItem.setActive(true);
			cst2AgainstFormCItem.setDescription("CST 2% Against Form C");
			cst2AgainstFormCItem.setTaxRate(2);
			cst2AgainstFormCItem.setTaxAgency(defaultCSTAgency);
			cst2AgainstFormCItem.setVatReturnBox(null);
			cst2AgainstFormCItem.setDefault(true);
			cst2AgainstFormCItem.setPercentage(true);
			session.save(cst2AgainstFormCItem);

			TAXItem cst4Item = new TAXItem(company);
			cst4Item.setName("CST 4%");
			cst4Item.setActive(true);
			cst4Item.setDescription("CST 4% ");
			cst4Item.setTaxRate(4);
			cst4Item.setTaxAgency(defaultCSTAgency);
			cst4Item.setVatReturnBox(null);
			cst4Item.setDefault(true);
			cst4Item.setPercentage(true);
			session.save(cst4Item);

			TAXItem exportsItem = new TAXItem(company);
			exportsItem.setName("Exports");
			exportsItem.setActive(true);
			exportsItem.setDescription("Exports");
			exportsItem.setTaxRate(0);
			exportsItem.setTaxAgency(defaultCSTAgency);
			exportsItem.setVatReturnBox(null);
			exportsItem.setDefault(true);
			exportsItem.setPercentage(true);
			session.save(exportsItem);

			TAXItem importsItem = new TAXItem(company);
			importsItem.setName("Imports");
			importsItem.setActive(true);
			importsItem.setDescription("Imports");
			importsItem.setTaxRate(0);
			importsItem.setTaxAgency(defaultCSTAgency);
			importsItem.setVatReturnBox(null);
			importsItem.setDefault(true);
			importsItem.setPercentage(true);
			session.save(importsItem);

			TAXItem inputVat1Item = new TAXItem(company);
			inputVat1Item.setName("Input Vat 1%");
			inputVat1Item.setActive(true);
			inputVat1Item.setDescription("Input Vat 1%");
			inputVat1Item.setTaxRate(1);
			inputVat1Item.setTaxAgency(defaultCSTAgency);
			inputVat1Item.setVatReturnBox(null);
			inputVat1Item.setDefault(true);
			inputVat1Item.setPercentage(true);
			session.save(inputVat1Item);

			TAXItem inputVat12Item = new TAXItem(company);
			inputVat12Item.setName("Input Vat 12.5%");
			inputVat12Item.setActive(true);
			inputVat12Item.setDescription("Input Vat 12.5%");
			inputVat12Item.setTaxRate(12.5);
			inputVat12Item.setTaxAgency(defaultCSTAgency);
			inputVat12Item.setVatReturnBox(null);
			inputVat12Item.setDefault(true);
			inputVat12Item.setPercentage(true);
			session.save(inputVat12Item);

			TAXItem inputVat4Item = new TAXItem(company);
			inputVat4Item.setName("Input Vat 4%");
			inputVat4Item.setActive(true);
			inputVat4Item.setDescription("Input Vat 4%");
			inputVat4Item.setTaxRate(4);
			inputVat4Item.setTaxAgency(defaultCSTAgency);
			inputVat4Item.setVatReturnBox(null);
			inputVat4Item.setDefault(true);
			inputVat4Item.setPercentage(true);
			session.save(inputVat4Item);

			TAXItem inputVat14Item = new TAXItem(company);
			inputVat14Item.setName("Input Vat 14.5%");
			inputVat14Item.setActive(true);
			inputVat14Item.setDescription("Input Vat 14.5%");
			inputVat14Item.setTaxRate(14.5);
			inputVat14Item.setTaxAgency(defaultCSTAgency);
			inputVat14Item.setVatReturnBox(null);
			inputVat14Item.setDefault(true);
			inputVat14Item.setPercentage(true);
			session.save(inputVat14Item);

			TAXItem ispItem = new TAXItem(company);
			ispItem.setName("Inter State Purchases");
			ispItem.setActive(true);
			ispItem.setDescription("Inter State Purchases");
			ispItem.setTaxRate(0);
			ispItem.setTaxAgency(defaultCSTAgency);
			ispItem.setVatReturnBox(null);
			ispItem.setDefault(true);
			ispItem.setPercentage(true);
			session.save(ispItem);

			TAXItem isp1Item = new TAXItem(company);
			isp1Item.setName("Inter State Purchases 1%");
			isp1Item.setActive(true);
			isp1Item.setDescription("Inter State Purchases 1%");
			isp1Item.setTaxRate(1);
			isp1Item.setTaxAgency(defaultCSTAgency);
			isp1Item.setVatReturnBox(null);
			isp1Item.setDefault(true);
			isp1Item.setPercentage(true);
			session.save(isp1Item);

			TAXItem isp12Item = new TAXItem(company);
			isp12Item.setName("Inter State Purchases 12.5%");
			isp12Item.setActive(true);
			isp12Item.setDescription("Inter State Purchases 12.5%");
			isp12Item.setTaxRate(12.5);
			isp12Item.setTaxAgency(defaultCSTAgency);
			isp12Item.setVatReturnBox(null);
			isp12Item.setDefault(true);
			isp12Item.setPercentage(true);
			session.save(isp12Item);

			TAXItem isp14Item = new TAXItem(company);
			isp14Item.setName("Inter State Purchases 14.5%");
			isp14Item.setActive(true);
			isp14Item.setDescription("Inter State Purchases 14.5%");
			isp14Item.setTaxRate(14.5);
			isp14Item.setTaxAgency(defaultCSTAgency);
			isp14Item.setVatReturnBox(null);
			isp14Item.setDefault(true);
			isp14Item.setPercentage(true);
			session.save(isp14Item);

			TAXItem isp4Item = new TAXItem(company);
			isp4Item.setName("Inter State Purchases 4%");
			isp4Item.setActive(true);
			isp4Item.setDescription("Inter State Purchases 4%");
			isp4Item.setTaxRate(4);
			isp4Item.setTaxAgency(defaultCSTAgency);
			isp4Item.setVatReturnBox(null);
			isp4Item.setDefault(true);
			isp4Item.setPercentage(true);
			session.save(isp4Item);

			TAXItem isp2Item = new TAXItem(company);
			isp2Item.setName("Inter State Purchases 2% against Form C");
			isp2Item.setActive(true);
			isp2Item.setDescription("Inter State Purchases 2% against Form C");
			isp2Item.setTaxRate(2);
			isp2Item.setTaxAgency(defaultCSTAgency);
			isp2Item.setVatReturnBox(null);
			isp2Item.setDefault(true);
			isp2Item.setPercentage(true);
			session.save(isp2Item);

			TAXItem ispForm1Item = new TAXItem(company);
			ispForm1Item.setName("Inter State Purchases Against Form-E1");
			ispForm1Item.setActive(true);
			ispForm1Item
					.setDescription("Inter State Purchases Against Form-E1");
			ispForm1Item.setTaxRate(0);
			ispForm1Item.setTaxAgency(defaultCSTAgency);
			ispForm1Item.setVatReturnBox(null);
			ispForm1Item.setDefault(true);
			ispForm1Item.setPercentage(true);
			session.save(ispForm1Item);

			TAXItem ispForm2Item = new TAXItem(company);
			ispForm2Item.setName("Inter State Purchases Against Form-E2");
			ispForm2Item.setActive(true);
			ispForm2Item
					.setDescription("Inter State Purchases Against Form-E2");
			ispForm2Item.setTaxRate(0);
			ispForm2Item.setTaxAgency(defaultCSTAgency);
			ispForm2Item.setVatReturnBox(null);
			ispForm2Item.setDefault(true);
			ispForm2Item.setPercentage(true);
			session.save(ispForm2Item);

			TAXItem issItem = new TAXItem(company);
			issItem.setName("Inter State Sales");
			issItem.setActive(true);
			issItem.setDescription("Inter State Sales");
			issItem.setTaxRate(0);
			issItem.setTaxAgency(defaultCSTAgency);
			issItem.setVatReturnBox(null);
			issItem.setDefault(true);
			issItem.setPercentage(true);
			session.save(issItem);

			TAXItem issForm1Item = new TAXItem(company);
			issForm1Item.setName("Inter State Sales Against Form-E1");
			issForm1Item.setActive(true);
			issForm1Item.setDescription("Inter State Sales Against Form-E1");
			issForm1Item.setTaxRate(0);
			issForm1Item.setTaxAgency(defaultCSTAgency);
			issForm1Item.setVatReturnBox(null);
			issForm1Item.setDefault(true);
			issForm1Item.setPercentage(true);
			session.save(issForm1Item);

			TAXItem issForm2Item = new TAXItem(company);
			issForm2Item.setName("Inter State Sales Against Form-E2");
			issForm2Item.setActive(true);
			issForm2Item.setDescription("Inter State Sales Against Form-E2");
			issForm2Item.setTaxRate(0);
			issForm2Item.setTaxAgency(defaultCSTAgency);
			issForm2Item.setVatReturnBox(null);
			issForm2Item.setDefault(true);
			issForm2Item.setPercentage(true);
			session.save(issForm2Item);

			TAXItem issExempted = new TAXItem(company);
			issExempted.setName("Inter State Sales Exempted");
			issExempted.setActive(true);
			issExempted.setDescription("Inter State Sales Exempted");
			issExempted.setTaxRate(0);
			issExempted.setTaxAgency(defaultCSTAgency);
			issExempted.setVatReturnBox(null);
			issExempted.setDefault(true);
			issExempted.setPercentage(true);
			session.save(issExempted);

			TAXItem vat1Item = new TAXItem(company);
			vat1Item.setName("Output Vat 1%");
			vat1Item.setActive(true);
			vat1Item.setDescription("Output Vat 1%");
			vat1Item.setTaxRate(1);
			vat1Item.setTaxAgency(defaultCSTAgency);
			vat1Item.setVatReturnBox(null);
			vat1Item.setDefault(true);
			vat1Item.setPercentage(true);
			session.save(vat1Item);

			TAXItem vat1onWorksContractItem = new TAXItem(company);
			vat1onWorksContractItem.setName("Output Vat 1% on Works Contract");
			vat1onWorksContractItem.setActive(true);
			vat1onWorksContractItem
					.setDescription("Output Vat 1% on Works Contract");
			vat1onWorksContractItem.setTaxRate(1);
			vat1onWorksContractItem.setTaxAgency(defaultCSTAgency);
			vat1onWorksContractItem.setVatReturnBox(null);
			vat1onWorksContractItem.setDefault(true);
			vat1onWorksContractItem.setPercentage(true);
			session.save(vat1onWorksContractItem);

			TAXItem vat12Item = new TAXItem(company);
			vat12Item.setName("Output Vat 12.5%");
			vat12Item.setActive(true);
			vat12Item.setDescription("Output Vat 12.5%");
			vat12Item.setTaxRate(12.5);
			vat12Item.setTaxAgency(defaultCSTAgency);
			vat12Item.setVatReturnBox(null);
			vat12Item.setDefault(true);
			vat12Item.setPercentage(true);
			session.save(vat12Item);

			TAXItem vat14Item = new TAXItem(company);
			vat14Item.setName("Output Vat 14.5%");
			vat14Item.setActive(true);
			vat14Item.setDescription("Output Vat 14.5%");
			vat14Item.setTaxRate(14.5);
			vat14Item.setTaxAgency(defaultCSTAgency);
			vat14Item.setVatReturnBox(null);
			vat14Item.setDefault(true);
			vat14Item.setPercentage(true);
			session.save(vat14Item);

			TAXItem vat16Item = new TAXItem(company);
			vat16Item.setName("Output Vat 16% Special Rate");
			vat16Item.setActive(true);
			vat16Item.setDescription("Output Vat 16% Special Rate");
			vat16Item.setTaxRate(16);
			vat16Item.setTaxAgency(defaultCSTAgency);
			vat16Item.setVatReturnBox(null);
			vat16Item.setDefault(true);
			vat16Item.setPercentage(true);
			session.save(vat16Item);

			TAXItem vat21Item = new TAXItem(company);
			vat21Item.setName("Output Vat 21.33% Special Rate");
			vat21Item.setActive(true);
			vat21Item.setDescription("Output Vat 21.33% Special Rate");
			vat21Item.setTaxRate(21.33);
			vat21Item.setTaxAgency(defaultCSTAgency);
			vat21Item.setVatReturnBox(null);
			vat21Item.setDefault(true);
			vat21Item.setPercentage(true);
			session.save(vat21Item);

			TAXItem vat22Item = new TAXItem(company);
			vat22Item.setName("Output Vat 22.25% Special Rate");
			vat22Item.setActive(true);
			vat22Item.setDescription("Output Vat 22.25% Special Rate");
			vat22Item.setTaxRate(22.25);
			vat22Item.setTaxAgency(defaultCSTAgency);
			vat22Item.setVatReturnBox(null);
			vat22Item.setDefault(true);
			vat22Item.setPercentage(true);
			session.save(vat22Item);

			TAXItem vat25Item = new TAXItem(company);
			vat25Item.setName("Output Vat 25% Special Category Goods");
			vat25Item.setActive(true);
			vat25Item.setDescription("Output Vat 25% Special Category Goods");
			vat25Item.setTaxRate(25);
			vat25Item.setTaxAgency(defaultCSTAgency);
			vat25Item.setVatReturnBox(null);
			vat25Item.setDefault(true);
			vat25Item.setPercentage(true);
			session.save(vat25Item);

			TAXItem vat32Item = new TAXItem(company);
			vat32Item.setName("Output Vat 32.55% Special Rate");
			vat32Item.setActive(true);
			vat32Item.setDescription("Output Vat 32.55% Special Rate");
			vat32Item.setTaxRate(32.55);
			vat32Item.setTaxAgency(defaultCSTAgency);
			vat32Item.setVatReturnBox(null);
			vat32Item.setDefault(true);
			vat32Item.setPercentage(true);
			session.save(vat32Item);

			TAXItem vat33Item = new TAXItem(company);
			vat33Item.setName("Output Vat 33% Special Rate");
			vat33Item.setActive(true);
			vat33Item.setDescription("Output Vat 33% Special Rate");
			vat33Item.setTaxRate(33);
			vat33Item.setTaxAgency(defaultCSTAgency);
			vat33Item.setVatReturnBox(null);
			vat33Item.setDefault(true);
			vat33Item.setPercentage(true);
			session.save(vat33Item);

			TAXItem vat34Item = new TAXItem(company);
			vat34Item.setName("Output Vat 34% Special Rate");
			vat34Item.setActive(true);
			vat34Item.setDescription("Output Vat 34% Special Rate");
			vat34Item.setTaxRate(34);
			vat34Item.setTaxAgency(defaultCSTAgency);
			vat34Item.setVatReturnBox(null);
			vat34Item.setDefault(true);
			vat34Item.setPercentage(true);
			session.save(vat34Item);

			TAXItem vat4Item = new TAXItem(company);
			vat4Item.setName("Output Vat 4%");
			vat4Item.setActive(true);
			vat4Item.setDescription("Output Vat 4%");
			vat4Item.setTaxRate(4);
			vat4Item.setTaxAgency(defaultCSTAgency);
			vat4Item.setVatReturnBox(null);
			vat4Item.setDefault(true);
			vat4Item.setPercentage(true);
			session.save(vat4Item);

			TAXItem vat70Item = new TAXItem(company);
			vat70Item.setName("Output Vat 70% Special Rate");
			vat70Item.setActive(true);
			vat70Item.setDescription("Output Vat 70% Special Rate");
			vat70Item.setTaxRate(70);
			vat70Item.setTaxAgency(defaultCSTAgency);
			vat70Item.setVatReturnBox(null);
			vat70Item.setDefault(true);
			vat70Item.setPercentage(true);
			session.save(vat70Item);

			TAXItem purchases16Item = new TAXItem(company);
			purchases16Item.setName("Purchases 16% Special Rate");
			purchases16Item.setActive(true);
			purchases16Item.setDescription("Purchases 16% Special Rate");
			purchases16Item.setTaxRate(16);
			purchases16Item.setTaxAgency(defaultCSTAgency);
			purchases16Item.setVatReturnBox(null);
			purchases16Item.setDefault(true);
			purchases16Item.setPercentage(true);
			session.save(purchases16Item);

			TAXItem purchases21Item = new TAXItem(company);
			purchases21Item.setName("Purchases Vat 21.33% Special Rate");
			purchases21Item.setActive(true);
			purchases21Item.setDescription("Purchases Vat 21.33% Special Rate");
			purchases21Item.setTaxRate(21.33);
			purchases21Item.setTaxAgency(defaultCSTAgency);
			purchases21Item.setVatReturnBox(null);
			purchases21Item.setDefault(true);
			purchases21Item.setPercentage(true);
			session.save(purchases21Item);

			TAXItem purchases32Item = new TAXItem(company);
			purchases32Item.setName("Purchases Vat 32.55% Special Rate");
			purchases32Item.setActive(true);
			purchases32Item.setDescription("Purchases Vat 32.55% Special Rate");
			purchases32Item.setTaxRate(32.55);
			purchases32Item.setTaxAgency(defaultCSTAgency);
			purchases32Item.setVatReturnBox(null);
			purchases32Item.setDefault(true);
			purchases32Item.setPercentage(true);
			session.save(purchases32Item);

			TAXItem purchases34Item = new TAXItem(company);
			purchases34Item.setName("Purchases Vat 34% Special Rate");
			purchases34Item.setActive(true);
			purchases34Item.setDescription("Purchases Vat 34% Special Rate");
			purchases34Item.setTaxRate(34);
			purchases34Item.setTaxAgency(defaultCSTAgency);
			purchases34Item.setVatReturnBox(null);
			purchases34Item.setDefault(true);
			purchases34Item.setPercentage(true);
			session.save(purchases34Item);

			TAXItem purchases70Item = new TAXItem(company);
			purchases70Item.setName("Purchases Vat 70% Special Rate");
			purchases70Item.setActive(true);
			purchases70Item.setDescription("Purchases Vat 70% Special Rate");
			purchases70Item.setTaxRate(70);
			purchases70Item.setTaxAgency(defaultCSTAgency);
			purchases70Item.setVatReturnBox(null);
			purchases70Item.setDefault(true);
			purchases70Item.setPercentage(true);
			session.save(purchases70Item);

			TAXItem p_hDeemedExportItem = new TAXItem(company);
			p_hDeemedExportItem
					.setName("Purchases Against Form H-Deemed Export");
			p_hDeemedExportItem.setActive(true);
			p_hDeemedExportItem
					.setDescription("Purchases Against Form H-Deemed Export");
			p_hDeemedExportItem.setTaxRate(0);
			p_hDeemedExportItem.setTaxAgency(defaultCSTAgency);
			p_hDeemedExportItem.setVatReturnBox(null);
			p_hDeemedExportItem.setDefault(true);
			p_hDeemedExportItem.setPercentage(true);
			session.save(p_hDeemedExportItem);

			TAXItem p_form1ZeroRatedItem = new TAXItem(company);
			p_form1ZeroRatedItem.setName("Purchases Against Form 1 Zero Rated");
			p_form1ZeroRatedItem.setActive(true);
			p_form1ZeroRatedItem
					.setDescription("Purchases Against Form 1 Zero Rated");
			p_form1ZeroRatedItem.setTaxRate(0);
			p_form1ZeroRatedItem.setTaxAgency(defaultCSTAgency);
			p_form1ZeroRatedItem.setVatReturnBox(null);
			p_form1ZeroRatedItem.setDefault(true);
			p_form1ZeroRatedItem.setPercentage(true);
			session.save(p_form1ZeroRatedItem);

			TAXItem capitalGoods12 = new TAXItem(company);
			capitalGoods12.setName("Purchases Capital Goods 12.5%");
			capitalGoods12.setActive(true);
			capitalGoods12.setDescription("Purchases Capital Goods 12.5%");
			capitalGoods12.setTaxRate(12.5);
			capitalGoods12.setTaxAgency(defaultCSTAgency);
			capitalGoods12.setVatReturnBox(null);
			capitalGoods12.setDefault(true);
			capitalGoods12.setPercentage(true);
			session.save(capitalGoods12);

			TAXItem capitalGoods14 = new TAXItem(company);
			capitalGoods14.setName("Purchases Capital Goods 14.5%");
			capitalGoods14.setActive(true);
			capitalGoods14.setDescription("Purchases Capital Goods 14.5%");
			capitalGoods14.setTaxRate(14.5);
			capitalGoods14.setTaxAgency(defaultCSTAgency);
			capitalGoods14.setVatReturnBox(null);
			capitalGoods14.setDefault(true);
			capitalGoods14.setPercentage(true);
			session.save(capitalGoods14);

			TAXItem capitalGoods4 = new TAXItem(company);
			capitalGoods4.setName("Purchases Capital Goods 4%");
			capitalGoods4.setActive(true);
			capitalGoods4.setDescription("Purchases Capital Goods 4%");
			capitalGoods4.setTaxRate(4);
			capitalGoods4.setTaxAgency(defaultCSTAgency);
			capitalGoods4.setVatReturnBox(null);
			capitalGoods4.setDefault(true);
			capitalGoods4.setPercentage(true);
			session.save(capitalGoods4);

			TAXItem purchasesExempt = new TAXItem(company);
			purchasesExempt.setName("Purchases Exempt");
			purchasesExempt.setActive(true);
			purchasesExempt.setDescription("Purchases Exempt");
			purchasesExempt.setTaxRate(0);
			purchasesExempt.setTaxAgency(defaultCSTAgency);
			purchasesExempt.setVatReturnBox(null);
			purchasesExempt.setDefault(true);
			purchasesExempt.setPercentage(true);
			session.save(purchasesExempt);

			TAXItem purchasesTotCasualDealer = new TAXItem(company);
			purchasesTotCasualDealer
					.setName("Purchases form  TOT/Casual Dealer");
			purchasesTotCasualDealer.setActive(true);
			purchasesTotCasualDealer
					.setDescription("Purchases form  TOT/Casual Dealer");
			purchasesTotCasualDealer.setTaxRate(0);
			purchasesTotCasualDealer.setTaxAgency(defaultCSTAgency);
			purchasesTotCasualDealer.setVatReturnBox(null);
			purchasesTotCasualDealer.setDefault(true);
			purchasesTotCasualDealer.setPercentage(true);
			session.save(purchasesTotCasualDealer);

			TAXItem purchasesUnregisteredDealer = new TAXItem(company);
			purchasesUnregisteredDealer
					.setName("Purchases form  Unregistered Dealer");
			purchasesUnregisteredDealer.setActive(true);
			purchasesUnregisteredDealer
					.setDescription("Purchases form  Unregistered Dealer");
			purchasesUnregisteredDealer.setTaxRate(0);
			purchasesUnregisteredDealer.setTaxAgency(defaultCSTAgency);
			purchasesUnregisteredDealer.setVatReturnBox(null);
			purchasesUnregisteredDealer.setDefault(true);
			purchasesUnregisteredDealer.setPercentage(true);
			session.save(purchasesUnregisteredDealer);

			TAXItem purchasesOthers = new TAXItem(company);
			purchasesOthers.setName("Purchases Others");
			purchasesOthers.setActive(true);
			purchasesOthers.setDescription("Purchases Others");
			purchasesOthers.setTaxRate(0);
			purchasesOthers.setTaxAgency(defaultCSTAgency);
			purchasesOthers.setVatReturnBox(null);
			purchasesOthers.setDefault(true);
			purchasesOthers.setPercentage(true);
			session.save(purchasesOthers);

			TAXItem s_hDeemedExportItem = new TAXItem(company);
			s_hDeemedExportItem.setName("Sales Against Form  H-Deemed Export");
			s_hDeemedExportItem.setActive(true);
			s_hDeemedExportItem
					.setDescription("Sales Against Form H-Deemed Export");
			s_hDeemedExportItem.setTaxRate(0);
			s_hDeemedExportItem.setTaxAgency(defaultCSTAgency);
			s_hDeemedExportItem.setVatReturnBox(null);
			s_hDeemedExportItem.setDefault(true);
			s_hDeemedExportItem.setPercentage(true);
			session.save(s_hDeemedExportItem);

			TAXItem salesExempt = new TAXItem(company);
			salesExempt.setName("Sales Exempt");
			salesExempt.setActive(true);
			salesExempt.setDescription("Sales Exempt");
			salesExempt.setTaxRate(0);
			salesExempt.setTaxAgency(defaultCSTAgency);
			salesExempt.setVatReturnBox(null);
			salesExempt.setDefault(true);
			salesExempt.setPercentage(true);
			session.save(salesExempt);

			TAXItem salesZeroRated = new TAXItem(company);
			salesZeroRated.setName("Sales Zero Rated (Inter State)");
			salesZeroRated.setActive(true);
			salesZeroRated.setDescription("Sales Zero Rated (Inter State)");
			salesZeroRated.setTaxRate(0);
			salesZeroRated.setTaxAgency(defaultCSTAgency);
			salesZeroRated.setVatReturnBox(null);
			salesZeroRated.setDefault(true);
			salesZeroRated.setPercentage(true);
			session.save(salesZeroRated);

			// for Andhra Pradesh Tax codes
			TAXCode cst1Code = new TAXCode(company);
			cst1Code.setName("CST 1%");
			cst1Code.setDescription("CST 1%");
			cst1Code.setTaxable(true);
			cst1Code.setActive(true);
			// cst1Code.setTAXItemGrpForPurchases(cst1Item);
			cst1Code.setTAXItemGrpForSales(cst1Item);
			cst1Code.setDefault(true);
			session.save(cst1Code);

			TAXCode cst12Code = new TAXCode(company);
			cst12Code.setName("CST 12.5%");
			cst12Code.setDescription("CST 12.5%");
			cst12Code.setTaxable(true);
			cst12Code.setActive(true);
			// cst12Code.setTAXItemGrpForPurchases(cst12Item);
			cst12Code.setTAXItemGrpForSales(cst12Item);
			cst12Code.setDefault(true);
			session.save(cst12Code);

			TAXCode cst14Code = new TAXCode(company);
			cst14Code.setName("CST 14.5%");
			cst14Code.setDescription("CST 14.5%");
			cst14Code.setTaxable(true);
			cst14Code.setActive(true);
			// cst14Code.setTAXItemGrpForPurchases(cst14Item);
			cst14Code.setTAXItemGrpForSales(cst14Item);
			cst14Code.setDefault(true);
			session.save(cst14Code);

			TAXCode cst2AgainstFormCCode = new TAXCode(company);
			cst2AgainstFormCCode.setName("CST 2% Against Form C");
			cst2AgainstFormCCode.setDescription("CST 2% Against Form C");
			cst2AgainstFormCCode.setTaxable(true);
			cst2AgainstFormCCode.setActive(true);
			// cst2AgainstFormCCode.setTAXItemGrpForPurchases(cst2AgainstFormCItem);
			cst2AgainstFormCCode.setTAXItemGrpForSales(cst2AgainstFormCItem);
			cst2AgainstFormCCode.setDefault(true);
			session.save(cst2AgainstFormCCode);

			TAXCode cst4Code = new TAXCode(company);
			cst4Code.setName("CST 4%");
			cst4Code.setDescription("CST 4%");
			cst4Code.setTaxable(true);
			cst4Code.setActive(true);
			// cst4Code.setTAXItemGrpForPurchases(cst4Item);
			cst4Code.setTAXItemGrpForSales(cst4Item);
			cst4Code.setDefault(true);
			session.save(cst4Code);

			TAXCode inputVat1Code = new TAXCode(company);
			inputVat1Code.setName("Input Vat 1%");
			inputVat1Code.setDescription("Input Vat 1%");
			inputVat1Code.setTaxable(true);
			inputVat1Code.setActive(true);
			// inputVat1Code.setTAXItemGrpForPurchases(inputVat1Item);
			inputVat1Code.setTAXItemGrpForSales(inputVat1Item);
			inputVat1Code.setDefault(true);
			session.save(inputVat1Code);

			TAXCode inputVat12Code = new TAXCode(company);
			inputVat12Code.setName("Input Vat 12.5%");
			inputVat12Code.setDescription("Input Vat 12.5%");
			inputVat12Code.setTaxable(true);
			inputVat12Code.setActive(true);
			// inputVat12Code.setTAXItemGrpForPurchases(inputVat12Item);
			inputVat12Code.setTAXItemGrpForSales(inputVat12Item);
			inputVat12Code.setDefault(true);
			session.save(inputVat12Code);

			TAXCode inputVat4Code = new TAXCode(company);
			inputVat4Code.setName("Input Vat 4%");
			inputVat4Code.setDescription("Input Vat 4%");
			inputVat4Code.setTaxable(true);
			inputVat4Code.setActive(true);
			// inputVat4Code.setTAXItemGrpForPurchases(inputVat4Item);
			inputVat4Code.setTAXItemGrpForSales(inputVat4Item);
			inputVat4Code.setDefault(true);
			session.save(inputVat4Code);

			TAXCode inputVat14Code = new TAXCode(company);
			inputVat14Code.setName("Input Vat 14.5%");
			inputVat14Code.setDescription("Input Vat 14.5%");
			inputVat14Code.setTaxable(true);
			inputVat14Code.setActive(true);
			// inputVat14Code.setTAXItemGrpForPurchases(inputVat14Item);
			inputVat14Code.setTAXItemGrpForSales(inputVat14Item);
			inputVat14Code.setDefault(true);
			session.save(inputVat14Code);

			TAXCode isp1Code = new TAXCode(company);
			isp1Code.setName("Inter State Purchases 1%");
			isp1Code.setDescription("Inter State Purchases 1%");
			isp1Code.setTaxable(true);
			isp1Code.setActive(true);
			isp1Code.setTAXItemGrpForPurchases(isp1Item);
			// isp1Code.setTAXItemGrpForSales(isp1Item);
			isp1Code.setDefault(true);
			session.save(isp1Code);

			TAXCode isp12Code = new TAXCode(company);
			isp12Code.setName("Inter State Purchases 12.5%");
			isp12Code.setDescription("Inter State Purchases 12.5%");
			isp12Code.setTaxable(true);
			isp12Code.setActive(true);
			isp12Code.setTAXItemGrpForPurchases(isp12Item);
			// isp12Code.setTAXItemGrpForSales(isp12Item);
			isp12Code.setDefault(true);
			session.save(isp12Code);

			TAXCode isp14Code = new TAXCode(company);
			isp14Code.setName("Inter State Purchases 14.5%");
			isp14Code.setDescription("Inter State Purchases 14.5%");
			isp14Code.setTaxable(true);
			isp14Code.setActive(true);
			isp14Code.setTAXItemGrpForPurchases(isp14Item);
			// isp14Code.setTAXItemGrpForSales(isp14Item);
			isp14Code.setDefault(true);
			session.save(isp14Code);

			TAXCode isp4Code = new TAXCode(company);
			isp4Code.setName("Inter State Purchases 4%");
			isp4Code.setDescription("Inter State Purchases 4%");
			isp4Code.setTaxable(true);
			isp4Code.setActive(true);
			isp4Code.setTAXItemGrpForPurchases(isp14Item);
			// isp4Code.setTAXItemGrpForSales(isp14Item);
			isp4Code.setDefault(true);
			session.save(isp4Code);

			TAXCode isp2Code = new TAXCode(company);
			isp2Code.setName("Inter State Purchases 2% Against Form C");
			isp2Code.setDescription("Inter State Purchases 2% Against Form C");
			isp2Code.setTaxable(true);
			isp2Code.setActive(true);
			isp2Code.setTAXItemGrpForPurchases(isp2Item);
			// isp2Code.setTAXItemGrpForSales(isp2Item);
			isp2Code.setDefault(true);
			session.save(isp2Code);

			TAXCode vat1Code = new TAXCode(company);
			vat1Code.setName("Output Vat 1%");
			vat1Code.setDescription("Output Vat 1%");
			vat1Code.setTaxable(true);
			vat1Code.setActive(true);
			// vat1Code.setTAXItemGrpForPurchases(vat1Item);
			vat1Code.setTAXItemGrpForSales(vat1Item);
			vat1Code.setDefault(true);
			session.save(vat1Code);

			TAXCode vat1onWorksContractCode = new TAXCode(company);
			vat1onWorksContractCode.setName("Output Vat 1% on Works Contract");
			vat1onWorksContractCode
					.setDescription("Output Vat 1% on Works Contract");
			vat1onWorksContractCode.setTaxable(true);
			vat1onWorksContractCode.setActive(true);
			// vat1onWorksContractCode.setTAXItemGrpForPurchases(vat1onWorksContractItem);
			vat1onWorksContractCode
					.setTAXItemGrpForSales(vat1onWorksContractItem);
			vat1onWorksContractCode.setDefault(true);
			session.save(vat1onWorksContractCode);

			TAXCode vat12Code = new TAXCode(company);
			vat12Code.setName("Output Vat 12.5%");
			vat12Code.setDescription("Output Vat 12.5%");
			vat12Code.setTaxable(true);
			vat12Code.setActive(true);
			// vat12Code.setTAXItemGrpForPurchases(vat12Item);
			vat12Code.setTAXItemGrpForSales(vat12Item);
			vat12Code.setDefault(true);
			session.save(vat12Code);

			TAXCode vat14Code = new TAXCode(company);
			vat14Code.setName("Output Vat 14.5%");
			vat14Code.setDescription("Output Vat 14.5%");
			vat14Code.setTaxable(true);
			vat14Code.setActive(true);
			// vat14Code.setTAXItemGrpForPurchases(vat14Item);
			vat14Code.setTAXItemGrpForSales(vat14Item);
			vat14Code.setDefault(true);
			session.save(vat14Code);

			TAXCode vat16Code = new TAXCode(company);
			vat16Code.setName("Output Vat 16% On Special Rate");
			vat16Code.setDescription("Output Vat 16% On Special Rate");
			vat16Code.setTaxable(true);
			vat16Code.setActive(true);
			// vat16Code.setTAXItemGrpForPurchases(vat16Item);
			vat16Code.setTAXItemGrpForSales(vat16Item);
			vat16Code.setDefault(true);
			session.save(vat16Code);

			TAXCode vat21Code = new TAXCode(company);
			vat21Code.setName("Output Vat 21.33% On Special Rate");
			vat21Code.setDescription("Output Vat 21.33% On Special Rate");
			vat21Code.setTaxable(true);
			vat21Code.setActive(true);
			// vat21Code.setTAXItemGrpForPurchases(vat21Item);
			vat21Code.setTAXItemGrpForSales(vat21Item);
			vat21Code.setDefault(true);
			session.save(vat21Code);

			TAXCode vat22Code = new TAXCode(company);
			vat22Code.setName("Output Vat 22.25% On Special Rate");
			vat22Code.setDescription("Output Vat 22.25% On Special Rate");
			vat22Code.setTaxable(true);
			vat22Code.setActive(true);
			// vat22Code.setTAXItemGrpForPurchases(vat22Item);
			vat22Code.setTAXItemGrpForSales(vat22Item);
			vat22Code.setDefault(true);
			session.save(vat22Code);

			TAXCode vat25Code = new TAXCode(company);
			vat25Code.setName("Output Vat 25% Special Category Goods");
			vat25Code.setDescription("Output Vat 25% Special Category Goods");
			vat25Code.setTaxable(true);
			vat25Code.setActive(true);
			// vat25Code.setTAXItemGrpForPurchases(vat25Item);
			vat25Code.setTAXItemGrpForSales(vat25Item);
			vat25Code.setDefault(true);
			session.save(vat22Code);

			TAXCode vat32Code = new TAXCode(company);
			vat32Code.setName("Output Vat 32.55% Special Rate");
			vat32Code.setDescription("Output Vat 32.55% Special Rate");
			vat32Code.setTaxable(true);
			vat32Code.setActive(true);
			// vat32Code.setTAXItemGrpForPurchases(vat32Item);
			vat32Code.setTAXItemGrpForSales(vat32Item);
			vat32Code.setDefault(true);
			session.save(vat32Code);

			TAXCode vat33Code = new TAXCode(company);
			vat33Code.setName("Output Vat 33% Special Rate");
			vat33Code.setDescription("Output Vat 33% Special Rate");
			vat33Code.setTaxable(true);
			vat33Code.setActive(true);
			// vat33Code.setTAXItemGrpForPurchases(vat33Item);
			vat33Code.setTAXItemGrpForSales(vat33Item);
			vat33Code.setDefault(true);
			session.save(vat33Code);

			TAXCode vat34Code = new TAXCode(company);
			vat34Code.setName("Output Vat 34% Special Rate");
			vat34Code.setDescription("Output Vat 34% Special Rate");
			vat34Code.setTaxable(true);
			vat34Code.setActive(true);
			// vat34Code.setTAXItemGrpForPurchases(vat34Item);
			vat34Code.setTAXItemGrpForSales(vat34Item);
			vat34Code.setDefault(true);
			session.save(vat34Code);

			TAXCode vat4Code = new TAXCode(company);
			vat4Code.setName("Output Vat 4% ");
			vat4Code.setDescription("Output Vat 4% ");
			vat4Code.setTaxable(true);
			vat4Code.setActive(true);
			// vat4Code.setTAXItemGrpForPurchases(vat4Item);
			vat4Code.setTAXItemGrpForSales(vat4Item);
			vat4Code.setDefault(true);
			session.save(vat4Code);

			TAXCode vat70Code = new TAXCode(company);
			vat70Code.setName("Output Vat 70% Special Rate");
			vat70Code.setDescription("Output Vat 70% Special Rate");
			vat70Code.setTaxable(true);
			vat70Code.setActive(true);
			// vat70Code.setTAXItemGrpForPurchases(vat70Item);
			vat70Code.setTAXItemGrpForSales(vat70Item);
			vat70Code.setDefault(true);
			session.save(vat70Code);

			TAXCode purchases16Code = new TAXCode(company);
			purchases16Code.setName("Purchases 16% Special Rate");
			purchases16Code.setDescription("Purchases 16% Special Rate");
			purchases16Code.setTaxable(true);
			purchases16Code.setActive(true);
			purchases16Code.setTAXItemGrpForPurchases(purchases16Item);
			// purchases16Code.setTAXItemGrpForSales(purchases16Item);
			purchases16Code.setDefault(true);
			session.save(purchases16Code);

			TAXCode purchases21Code = new TAXCode(company);
			purchases21Code.setName("Purchases 21.33% Special Rate");
			purchases21Code.setDescription("Purchases 21.33% Special Rate");
			purchases21Code.setTaxable(true);
			purchases21Code.setActive(true);
			purchases21Code.setTAXItemGrpForPurchases(purchases21Item);
			// purchases21Code.setTAXItemGrpForSales(purchases21Item);
			purchases21Code.setDefault(true);
			session.save(purchases21Code);

			TAXCode purchases32Code = new TAXCode(company);
			purchases32Code.setName("Purchases 32.55% Special Rate");
			purchases32Code.setDescription("Purchases 32.55% Special Rate");
			purchases32Code.setTaxable(true);
			purchases32Code.setActive(true);
			purchases32Code.setTAXItemGrpForPurchases(purchases32Item);
			// purchases32Code.setTAXItemGrpForSales(purchases32Item);
			purchases32Code.setDefault(true);
			session.save(purchases32Code);

			TAXCode purchases34Code = new TAXCode(company);
			purchases34Code.setName("Purchases 34% Special Rate");
			purchases34Code.setDescription("Purchases 34% Special Rate");
			purchases34Code.setTaxable(true);
			purchases34Code.setActive(true);
			purchases34Code.setTAXItemGrpForPurchases(purchases34Item);
			// purchases34Code.setTAXItemGrpForSales(purchases34Item);
			purchases34Code.setDefault(true);
			session.save(purchases34Code);

			TAXCode purchases70Code = new TAXCode(company);
			purchases70Code.setName("Purchases 70% Special Rate");
			purchases70Code.setDescription("Purchases 70% Special Rate");
			purchases70Code.setTaxable(true);
			purchases70Code.setActive(true);
			purchases70Code.setTAXItemGrpForPurchases(purchases70Item);
			// purchases70Code.setTAXItemGrpForSales(purchases70Item);
			purchases70Code.setDefault(true);
			session.save(purchases70Code);

			TAXCode capitalGoods12Code = new TAXCode(company);
			capitalGoods12Code.setName("Purchases Capital Goods 12.5%");
			capitalGoods12Code.setDescription("Purchases Capital Goods 12.5%");
			capitalGoods12Code.setTaxable(true);
			capitalGoods12Code.setActive(true);
			capitalGoods12Code.setTAXItemGrpForPurchases(capitalGoods12);
			// capitalGoods12Code.setTAXItemGrpForSales(capitalGoods12);
			capitalGoods12Code.setDefault(true);
			session.save(capitalGoods12Code);

			TAXCode capitalGoods14Code = new TAXCode(company);
			capitalGoods14Code.setName("Purchases Capital Goods 14.5%");
			capitalGoods14Code.setDescription("Purchases Capital Goods 14.5%");
			capitalGoods14Code.setTaxable(true);
			capitalGoods14Code.setActive(true);
			capitalGoods14Code.setTAXItemGrpForPurchases(capitalGoods14);
			// capitalGoods14Code.setTAXItemGrpForSales(capitalGoods14);
			capitalGoods14Code.setDefault(true);
			session.save(capitalGoods14Code);

			TAXCode capitalGoods4Code = new TAXCode(company);
			capitalGoods4Code.setName("Purchases Capital Goods 4%");
			capitalGoods4Code.setDescription("Purchases Capital Goods 4%");
			capitalGoods4Code.setTaxable(true);
			capitalGoods4Code.setActive(true);
			capitalGoods4Code.setTAXItemGrpForPurchases(capitalGoods4);
			// capitalGoods4Code.setTAXItemGrpForSales(capitalGoods4);
			capitalGoods4Code.setDefault(true);
			session.save(capitalGoods4Code);

		}

		// TAXCode tdsCode1 = new TAXCode();
		//
		// tdsCode1.setName("E");
		// tdsCode1.setDescription("Exempt");
		// tdsCode1.setTaxable(true);
		// tdsCode1.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem4);
		// tdsCode1.setTAXItemGrpForSales(tdsItem4);
		// tdsCode1.setDefault(true);
		// session.save(tdsCode1);
		// TAXCode tdsCode2 = new TAXCode();
		// tdsCode2.setName("P");
		// tdsCode2.setDescription("Professional");
		// tdsCode2.setTaxable(true);
		// tdsCode2.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem2);
		// tdsCode1.setTAXItemGrpForSales(tdsItem2);
		// tdsCode2.setDefault(true);
		// session.save(tdsCode2);
		// TAXCode tdsCode3 = new TAXCode();
		// tdsCode3.setName("C");
		// tdsCode3.setDescription("Contractor");
		// tdsCode3.setTaxable(true);
		// tdsCode3.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem3);
		// tdsCode1.setTAXItemGrpForSales(tdsItem3);
		// tdsCode3.setDefault(true);
		// session.save(tdsCode3);
		// TAXCode tdsCode4 = new TAXCode();
		// tdsCode4.setName("SC");
		// tdsCode4.setDescription("Sub Contractor");
		// tdsCode4.setTaxable(true);
		// tdsCode4.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem4);
		// tdsCode1.setTAXItemGrpForSales(tdsItem4);
		// tdsCode4.setDefault(true);
		// session.save(tdsCode4);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}
}
