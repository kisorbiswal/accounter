package com.vimukti.accounter.company.initialize;

import org.hibernate.Session;

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
				.setEntity("company", company).setString("name", "Net Monthly")
				.uniqueResult();

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

		//for Andhra Pradesh state
		if(company.getRegisteredAddress().getStateOrProvinence().equalsIgnoreCase("Andhra Pradesh"))
		{
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
			isp2Item.setTaxRate(0);
			isp2Item.setTaxAgency(defaultCSTAgency);
			isp2Item.setVatReturnBox(null);
			isp2Item.setDefault(true);
			isp2Item.setPercentage(true);
			session.save(isp2Item);
			
			TAXItem ispForm1Item = new TAXItem(company);
			ispForm1Item.setName("Inter State Purchases Against Form-E1");
			ispForm1Item.setActive(true);
			ispForm1Item.setDescription("Inter State Purchases Against Form-E1");
			ispForm1Item.setTaxRate(0);
			ispForm1Item.setTaxAgency(defaultCSTAgency);
			ispForm1Item.setVatReturnBox(null);
			ispForm1Item.setDefault(true);
			ispForm1Item.setPercentage(true);
			session.save(ispForm1Item);
			
			TAXItem ispForm2Item = new TAXItem(company);
			ispForm2Item.setName("Inter State Purchases Against Form-E2");
			ispForm2Item.setActive(true);
			ispForm2Item.setDescription("Inter State Purchases Against Form-E2");
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
			vat1onWorksContractItem.setDescription("Output Vat 1% on Works Contract");
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
			vat25Item.setName("Output Vat 25% Special Rate");
			vat25Item.setActive(true);
			vat25Item.setDescription("Output Vat 25% Special Rate");
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
			p_hDeemedExportItem.setName("Purchases Against Form H-Deemed Export");
			p_hDeemedExportItem.setActive(true);
			p_hDeemedExportItem.setDescription("Purchases Against Form H-Deemed Export");
			p_hDeemedExportItem.setTaxRate(0);
			p_hDeemedExportItem.setTaxAgency(defaultCSTAgency);
			p_hDeemedExportItem.setVatReturnBox(null);
			p_hDeemedExportItem.setDefault(true);
			p_hDeemedExportItem.setPercentage(true);
			session.save(p_hDeemedExportItem);
			
			TAXItem p_form1ZeroRatedItem = new TAXItem(company);
			p_form1ZeroRatedItem.setName("Purchases Against Form 1 Zero Rated");
			p_form1ZeroRatedItem.setActive(true);
			p_form1ZeroRatedItem.setDescription("Purchases Against Form 1 Zero Rated");
			p_form1ZeroRatedItem.setTaxRate(0);
			p_form1ZeroRatedItem.setTaxAgency(defaultCSTAgency);
			p_form1ZeroRatedItem.setVatReturnBox(null);
			p_form1ZeroRatedItem.setDefault(true);
			p_form1ZeroRatedItem.setPercentage(true);
			session.save(p_form1ZeroRatedItem);
			
			TAXItem capitalGoods12= new TAXItem(company);
			capitalGoods12.setName("Purchases Capital Goods 12.5%");
			capitalGoods12.setActive(true);
			capitalGoods12.setDescription("Purchases Capital Goods 12.5%");
			capitalGoods12.setTaxRate(12.5);
			capitalGoods12.setTaxAgency(defaultCSTAgency);
			capitalGoods12.setVatReturnBox(null);
			capitalGoods12.setDefault(true);
			capitalGoods12.setPercentage(true);
			session.save(capitalGoods12);
			
			TAXItem capitalGoods14= new TAXItem(company);
			capitalGoods14.setName("Purchases Capital Goods 14.5%");
			capitalGoods14.setActive(true);
			capitalGoods14.setDescription("Purchases Capital Goods 14.5%");
			capitalGoods14.setTaxRate(14.5);
			capitalGoods14.setTaxAgency(defaultCSTAgency);
			capitalGoods14.setVatReturnBox(null);
			capitalGoods14.setDefault(true);
			capitalGoods14.setPercentage(true);
			session.save(capitalGoods14);
			
			TAXItem capitalGoods4= new TAXItem(company);
			capitalGoods4.setName("Purchases Capital Goods 4%");
			capitalGoods4.setActive(true);
			capitalGoods4.setDescription("Purchases Capital Goods 4%");
			capitalGoods4.setTaxRate(4);
			capitalGoods4.setTaxAgency(defaultCSTAgency);
			capitalGoods4.setVatReturnBox(null);
			capitalGoods4.setDefault(true);
			capitalGoods4.setPercentage(true);
			session.save(capitalGoods4);
			
			TAXItem purchasesExempt= new TAXItem(company);
			purchasesExempt.setName("Purchases Exempt");
			purchasesExempt.setActive(true);
			purchasesExempt.setDescription("Purchases Exempt");
			purchasesExempt.setTaxRate(0);
			purchasesExempt.setTaxAgency(defaultCSTAgency);
			purchasesExempt.setVatReturnBox(null);
			purchasesExempt.setDefault(true);
			purchasesExempt.setPercentage(true);
			session.save(purchasesExempt);
			
			TAXItem purchasesTotCasualDealer= new TAXItem(company);
			purchasesTotCasualDealer.setName("Purchases form  TOT/Casual Dealer");
			purchasesTotCasualDealer.setActive(true);
			purchasesTotCasualDealer.setDescription("Purchases form  TOT/Casual Dealer");
			purchasesTotCasualDealer.setTaxRate(0);
			purchasesTotCasualDealer.setTaxAgency(defaultCSTAgency);
			purchasesTotCasualDealer.setVatReturnBox(null);
			purchasesTotCasualDealer.setDefault(true);
			purchasesTotCasualDealer.setPercentage(true);
			session.save(purchasesTotCasualDealer);
			
			TAXItem purchasesUnregisteredDealer= new TAXItem(company);
			purchasesUnregisteredDealer.setName("Purchases form  Unregistered Dealer");
			purchasesUnregisteredDealer.setActive(true);
			purchasesUnregisteredDealer.setDescription("Purchases form  Unregistered Dealer");
			purchasesUnregisteredDealer.setTaxRate(0);
			purchasesUnregisteredDealer.setTaxAgency(defaultCSTAgency);
			purchasesUnregisteredDealer.setVatReturnBox(null);
			purchasesUnregisteredDealer.setDefault(true);
			purchasesUnregisteredDealer.setPercentage(true);
			session.save(purchasesUnregisteredDealer);
			
			TAXItem purchasesOthers= new TAXItem(company);
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
			s_hDeemedExportItem.setDescription("Sales Against Form H-Deemed Export");
			s_hDeemedExportItem.setTaxRate(0);
			s_hDeemedExportItem.setTaxAgency(defaultCSTAgency);
			s_hDeemedExportItem.setVatReturnBox(null);
			s_hDeemedExportItem.setDefault(true);
			s_hDeemedExportItem.setPercentage(true);
			session.save(s_hDeemedExportItem);
			
			TAXItem salesExempt= new TAXItem(company);
			salesExempt.setName("Sales Exempt");
			salesExempt.setActive(true);
			salesExempt.setDescription("Sales Exempt");
			salesExempt.setTaxRate(0);
			salesExempt.setTaxAgency(defaultCSTAgency);
			salesExempt.setVatReturnBox(null);
			salesExempt.setDefault(true);
			salesExempt.setPercentage(true);
			session.save(salesExempt);
			
			TAXItem salesZeroRated= new TAXItem(company);
			salesZeroRated.setName("Sales Zero Rated (Inter State)");
			salesZeroRated.setActive(true);
			salesZeroRated.setDescription("Sales Zero Rated (Inter State)");
			salesZeroRated.setTaxRate(0);
			salesZeroRated.setTaxAgency(defaultCSTAgency);
			salesZeroRated.setVatReturnBox(null);
			salesZeroRated.setDefault(true);
			salesZeroRated.setPercentage(true);
			session.save(salesZeroRated);
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
