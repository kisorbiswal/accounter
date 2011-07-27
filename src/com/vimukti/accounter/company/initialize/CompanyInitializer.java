package com.vimukti.accounter.company.initialize;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.utils.HibernateUtil;


public abstract class CompanyInitializer {

	Session session; ;
	
	Account parentaccount;
	Account expense;
	
	Account income;
	
	Account liability;
	
	Account assets;
	/**
	 * company preferences
	 */
	CompanyPreferences preferences = new CompanyPreferences();
	/**
	 * this is used to get the company type
	 */
	public  void init(){
		 session = HibernateUtil.getCurrentSession();
			
		 parentaccount = new Account();
		session.save(parentaccount);
		
		 expense  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.TYPE_EXPENSE, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		session.save(expense);
		
		 income  = new Account(Account.TYPE_INCOME, null, AccounterConstants.TYPE_INCOME, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income);
		
		 liability  = new Account(Account.TYPE_LIABILITY, null, AccounterConstants.TYPE_INCOME, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability);
		
		 assets  = new Account(Account.TYPE_ASSET, null, AccounterConstants.TYPE_CURRENT_ASSET, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets);
		
		initializeDefaultExpenseAccounts();
		initializeDefaultAssetsAccounts();
		initializeDefaultIncomeAccounts();
		initializeDefaultlLiabilitiesAccounts();
	}
	public abstract Company getCompany();
	
	
	public void initializeDefaultExpenseAccounts(){
		
		Account advertising_and_marketing = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CONSULTING_AND_ACCOUNTING, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(advertising_and_marketing);
				
		Account Consulting_and_Accounting = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_ADEVRTISING_AND_MARKETING, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Consulting_and_Accounting);
		
		Account Deprication = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DEPRICATION, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Deprication);
				
		Account Meals_and_Entertainment = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MEALS_AND_ENTERTAINMENT, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Meals_and_Entertainment);
				
		Account General_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_GENERAL_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(General_Expense);
				
		Account Insurance = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INSURANCE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Insurance);
				
		Account Interest_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INTEREST_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Interest_Expense);
			
		Account Legal_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_LEGAL_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Legal_Expense);
				
		Account Motor_Veichle_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MOTOR_VEICHLE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Motor_Veichle_Expense);
				
		Account Fuel_Gas  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHEL_FUEL, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Fuel_Gas);
				
		Account Veichle_lease = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHLE_LEASE, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Veichle_lease);
				
		Account Repair_Maintainance  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHLE_REPAIR_MAINTAINANCE, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Repair_Maintainance);
				
		Account Wages_Salaries = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_WAGES_SALARIES, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Wages_Salaries);
				
		Account Superannuation = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_SUPERANNUATION, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Superannuation);
				
		Account Subscription = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_SUBSCRIPTION, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Subscription);
				
		Account Back_Revaluation = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BANKREVALUATION, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Back_Revaluation);
				
		Account Unrealized_Currency_Gains = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_UNREALISED_CURRENCY_GAIN, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Unrealized_Currency_Gains);
				
		Account Realized_Currency_Gains = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_REALISED_CURRENCY_GAIN, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Realized_Currency_Gains);
				
		Account Income_Tax_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INCOME_TAX_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Income_Tax_Expenses);
				
		Account Bad_Debit = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BAD_DEBIT, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Bad_Debit);
				
		Account Bank_Fee_Charges = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BANK_FEE_CHARGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Bank_Fee_Charges);
				
		Account Credit_card_charges = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CREDIT_CARD_CHARGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Credit_card_charges);
				
		Account Travel_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Travel_Expenses);
				
		Account Travel_International = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL_INTERNATIONAL, Travel_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Travel_International);
				
		Account Travel_National = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL_NATIONAL, Travel_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Travel_National);
				
		Account Automobile_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_AUTOMOBILE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Automobile_Expense);
				
		Account Postage = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_POSTAGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Postage);
				
		Account Other_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_OTHER, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Other_Expenses);
				
		Account Explain_Gain_Loss = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_GAIN_LOSS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Explain_Gain_Loss);
				
		Account Miscellaneous = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MISCELLANEOUS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Miscellaneous);
				
		Account Penalties_Settelments = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PENALTIES_SETELLMENTS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Penalties_Settelments);
				
		Account Utilities = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_UTILITIES, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Utilities);
				
		Account Tax_Lisences = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TAX_LISENSES, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Tax_Lisences);
				
		Account cost_of_goods_sold = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_COST_OF_GOODS_SOLDs, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(cost_of_goods_sold);
				
		Account product_purchase_A = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_A, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_A);
				
		Account product_purchase_B = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_B, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_B);
				
		Account product_purchase_C = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_C, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_C);
				
		Account product_purchase_D = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_D, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_D);
				
		Account product_purchase_E = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_E, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_E);
				
		Account product_purchase_F = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_F, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(product_purchase_F);
				
		Account Carriage = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CARRIAGE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Carriage);
				
		Account discounts_taken = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DISCOUNTS, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(discounts_taken);
				
		Account import_duty = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_IMPORT_DUTY, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(import_duty);
				
		Account stock_value_changed = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_STOCK_VALUE_CHAGE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(stock_value_changed);
				
		Account finished_good_value = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_FINISHED_GOOD_VALUE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(finished_good_value);
				
		Account work_in_progress_value = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_WORK_IN_PROGRESS_VALUE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		session.save(work_in_progress_value);
				
		Account Other_Direct_Costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_OTHER_DIRECT_COST, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Other_Direct_Costs);
				
		Account Direct_Labour = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_LABOUR, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Direct_Labour);
				
		Account Direct_Employee_NI = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_EMPLOYEE_NI, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Direct_Employee_NI);
				
		Account direct_Employee_Related_costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_EMPLOYEE_RELATED_COSTS, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(direct_Employee_Related_costs);
				
		Account Direct_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Direct_Expenses);
				
		Account Direct_Travel = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_TRAVEL, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Direct_Travel);
				
		Account Direct_Consumable = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_CONSUMABLE, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Direct_Consumable);
				
		Account Merchant_Account_Fees = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MERCHANY_ACCOUNT_FEES, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Merchant_Account_Fees);
				
		Account Cosmission_Paid = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_COMISSION_PAID, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Cosmission_Paid);
				
		Account indirect_costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INDIRECT_COSTS, expense, "", this.preferences.getPreventPostingBeforeDate());
		session.save(indirect_costs);
				
		Account indirect_labour = new Account(Account.TYPE_EXPENSE, null, "Indirect Labour", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(indirect_labour);
		
		Account indirect_employee_NI = new Account(Account.TYPE_EXPENSE, null, "Indirect Employee NI", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(indirect_employee_NI);
		
		Account directors_remuneration = new Account(Account.TYPE_EXPENSE, null, "Directors Remuneration", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(directors_remuneration);
		
		Account casual_labour = new Account(Account.TYPE_EXPENSE, null, "Casual Labour", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(casual_labour);
		
		Account employeer_pension = new Account(Account.TYPE_EXPENSE, null, "Employeer Pension Contribution", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(employeer_pension);
		
		Account SSP_reclaimed = new Account(Account.TYPE_EXPENSE, null, "SSP Reclaimed", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(SSP_reclaimed);
		
		Account SMP_reclaimed = new Account(Account.TYPE_EXPENSE, null, "SMP Reclaimed", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(SMP_reclaimed);
		
		Account Employee_Benefits = new Account(Account.TYPE_EXPENSE, null, "Employee Benefits", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Employee_Benefits);
		
		Account medical_insurance = new Account(Account.TYPE_EXPENSE, null, "Medical Insurance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(medical_insurance);		
		
		Account recruitment = new Account(Account.TYPE_EXPENSE, null, "Recruitment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(recruitment);
		
		Account training = new Account(Account.TYPE_EXPENSE, null, "Training", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(training);
		
		Account rent = new Account(Account.TYPE_EXPENSE, null, "Rent", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(rent);
		
		Account general_rates = new Account(Account.TYPE_EXPENSE, null, "General Rates", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(general_rates);
		
		Account water_rates = new Account(Account.TYPE_EXPENSE, null, "Water Rates", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(water_rates);
		
		Account electricity = new Account(Account.TYPE_EXPENSE, null, "Electricity", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(electricity);
		
		Account gas = new Account(Account.TYPE_EXPENSE, null, "Gas", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(gas);
		
		Account oil = new Account(Account.TYPE_EXPENSE, null, "Oil", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(oil);
		
		Account office_cleaning = new Account(Account.TYPE_EXPENSE, null, "Office Cleaning", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(office_cleaning);
		
		Account office_machine_maintainance = new Account(Account.TYPE_EXPENSE, null, "Office Machine Maintainance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(office_machine_maintainance);
		
		Account repair_renewals = new Account(Account.TYPE_EXPENSE, null, "Repair and Renewals", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(repair_renewals);
		
		Account office_consumable = new Account(Account.TYPE_EXPENSE, null, "Office Consumables", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(office_consumable);
		
		Account books = new Account(Account.TYPE_EXPENSE, null, "Books etc", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(books);
		
		Account internet = new Account(Account.TYPE_EXPENSE, null, "Internet", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(internet);
		
		Account postage = new Account(Account.TYPE_EXPENSE, null, "Postage", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(postage);
		
		Account printing = new Account(Account.TYPE_EXPENSE, null, "Printing", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(printing);
		
		Account stationary = new Account(Account.TYPE_EXPENSE, null, "Stationary", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(stationary);
		
		Account subscription = new Account(Account.TYPE_EXPENSE, null, "Subscription ", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(subscription);
		
		Account telephone = new Account(Account.TYPE_EXPENSE, null, "Telephone", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(telephone);
		
		Account conference_seminar = new Account(Account.TYPE_EXPENSE, null, "Conference and Seminar", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(conference_seminar);
		
		Account charity_donations = new Account(Account.TYPE_EXPENSE, null, "Charity Donations", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(charity_donations);
		
		Account insurance_business = new Account(Account.TYPE_EXPENSE, null, "Insurance Bussiness", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(insurance_business);
		
		Account advertising_marketting = new Account(Account.TYPE_EXPENSE, null, "Advertising Bussiness", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(advertising_marketting);
		
		Account local_entertainment = new Account(Account.TYPE_EXPENSE, null, "Local Entertainment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(local_entertainment);
		
		Account oversee_entertainment = new Account(Account.TYPE_EXPENSE, null, "Oversee Entertainment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(oversee_entertainment);
		
		Account indirect_local_travel = new Account(Account.TYPE_EXPENSE, null, "Indirect Local Travel", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(indirect_local_travel);
		
		Account indirect_oversee_travel = new Account(Account.TYPE_EXPENSE, null, "Indirect Oversee Travel", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(indirect_oversee_travel);
		
		Account subsistence = new Account(Account.TYPE_EXPENSE, null, "Subsistence", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(subsistence);
		
		Account veichle_expense = new Account(Account.TYPE_EXPENSE, null, "Veichle Expenses", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(veichle_expense);
		
		Account veichle_insurance = new Account(Account.TYPE_EXPENSE, null, "Veichle Insurance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(veichle_insurance);
		
		Account veichle_repair = new Account(Account.TYPE_EXPENSE, null, "Veichel Repair & Servicing", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(veichle_repair);
		
		Account professional_fees = new Account(Account.TYPE_EXPENSE, null, "Professional Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(professional_fees);
		
		Account accountancy_fees = new Account(Account.TYPE_EXPENSE, null, "Accountancy Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(accountancy_fees);
		
		Account consultancy_fees = new Account(Account.TYPE_EXPENSE, null, "Consultancy Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(consultancy_fees);
		
		Account legal_fees = new Account(Account.TYPE_EXPENSE, null, "Legal Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(legal_fees);
		
		Account bank_paid_interest = new Account(Account.TYPE_EXPENSE, null, "Bank Paid Interest", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(bank_paid_interest);
		
		Account bank_charges = new Account(Account.TYPE_EXPENSE, null, "Bank Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(bank_charges);
		
		Account credit_charges = new Account(Account.TYPE_EXPENSE, null, "Credit Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(credit_charges);
		
		Account lease_payments = new Account(Account.TYPE_EXPENSE, null, "Lease Payments", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(lease_payments);
		
		Account Loan_interest_paid = new Account(Account.TYPE_EXPENSE, null, "Loan Interest Paid", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(Loan_interest_paid);
		
		Account currency_charges = new Account(Account.TYPE_EXPENSE, null, "Currency Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(currency_charges);
		
		Account exchange_rate_variance = new Account(Account.TYPE_EXPENSE, null, "Exchange Rate Variance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(exchange_rate_variance);
		
		Account bad_debut_provision = new Account(Account.TYPE_EXPENSE, null, "Bad Debut Provision", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(bad_debut_provision);
		
		Account bad_debut_write_off = new Account(Account.TYPE_EXPENSE, null, "Bad Debut write off", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(bad_debut_write_off);
		
		Account deprication = new Account(Account.TYPE_EXPENSE, null, "Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(deprication);
		
		Account office_equipment_deprication = new Account(Account.TYPE_EXPENSE, null, "Office Equipment Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(office_equipment_deprication);
		
		Account it_equipment_deprication = new Account(Account.TYPE_EXPENSE, null, "IT Equipment Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(it_equipment_deprication);
		
		Account furniture_fixture_deprication = new Account(Account.TYPE_EXPENSE, null, "Furniture Fixture Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(furniture_fixture_deprication);
		
		Account plant_machinary_deprication = new Account(Account.TYPE_EXPENSE, null, "Plant/Machinary Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(plant_machinary_deprication);
		
		Account veichel_deprication = new Account(Account.TYPE_EXPENSE, null, "Veichel Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(veichel_deprication);
		
		Account freehold_building_deprication = new Account(Account.TYPE_EXPENSE, null, "Freehold Building Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(freehold_building_deprication);
		
		Account lease_property_improvement_deprication = new Account(Account.TYPE_EXPENSE, null, "Lease Property Improvement Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		session.save(lease_property_improvement_deprication);

	}

	
	
	public void initializeDefaultIncomeAccounts(){
		
		Account income_sales = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales);
		
		Account income_sales_of_product_name = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES_OF_PRODUCT_INCOME, income_sales, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales_of_product_name);
		
		Account income_others = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_OTHER, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_others);
		
		Account income_general = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_GENERAL, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_general);
				
		Account income_interest = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_INTEREST, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_interest);
		
		Account income_late_fee = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_LATE_FEE, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_late_fee);
		
		Account income_shipping = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SHIPPING, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_shipping);
		
		Account income_refund_allowance = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_REFUNDS_ALLOWANCE, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_refund_allowance);
		
		Account income_fees_billed = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_FEES_BILLED, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_fees_billed);
		
		Account income_services = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SERVICES, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_services);
		
		Account income_sales_type_B = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES_TYPE_B, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales_type_B);
		
		Account income_sales_type_C = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES_TYPE_C, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales_type_C);
		
		Account income_sales_type_D = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES_TYPE_D, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales_type_D);
		
		Account income_sales_type_E = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_SALES_TYPE_E, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_sales_type_E);
		
		Account income_miscellaneous = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_MISCELLANEOUS, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_miscellaneous);
		
		Account income_distribution_carriages = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_DISTRIBUTION_CARRIAGES, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_distribution_carriages);
		
		Account income_commission_received = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_COMMISION_RECEIVED, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_commission_received);
		
		Account income_credit_charges = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_CREDIT_CHARGES, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_credit_charges);
		
		Account income_insurance_claims = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_INSURANCE_CLAIMS, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_insurance_claims);
		
		Account income_rent = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_RENT, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_rent);
		
		Account income_royalites_received = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_ROYALTIES_RECEIVED, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_royalites_received);
		
		Account income_profit_loss_in_sales_of_assets = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.INCOME_PROFIT_LOSS_IN_SALE_OF_ASSETS, income, "", this.preferences.getPreventPostingBeforeDate());
		session.save(income_profit_loss_in_sales_of_assets);
	}
	
	public	void initializeDefaultAssetsAccounts(){
				
		Account assets_current = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_CURRENT, assets, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_current);
		
		Account assets_fixed = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_FIXED, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_fixed);
		
		Account assets_other_current = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_OTHER_CURRENT, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_other_current);
		
		Account assets_cash = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_CASH, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_cash);
		
		Account assets_bank = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_BANK, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_bank);
		
		Account assets_accounts_receivables = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_ACCOUNTS_RECEIVABLES, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_accounts_receivables);
		
		Account assets_prepayments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_PREPAYMENTS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_prepayments);
		
		Account assets_debtors = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_DEBTORS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_debtors);
		
		Account assets_deposites = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_DEPOSITS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_deposites);
		
		Account assets_bank_current_account = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_BANK_CURRENT_ACCOUNT, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_bank_current_account);
		
		Account assets_bank_deposite_accounts = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_BANK_DEPOSITE_ACCOUNT, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_bank_deposite_accounts);
		
		Account assets_undeposited_funds = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_UNDEPOSTITED_FUNDS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_undeposited_funds);
		
		Account assets_current_petty_cash = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_CURRENT_PETTY_CASH, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_current_petty_cash);
		
		//Account assets_prepayments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_PRE_PAYMENTS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		
		
		Account assets_advance_of_employees = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_ADVANCES_OF_EMPLOYEES, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_advance_of_employees);
		
		Account assets_stockvalues = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_STOCKVALUES, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_stockvalues);
		
		Account assets_office_equipments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_OFFICE_EQUIPMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_office_equipments);
		
		Account assets_lad_office_equipments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_LAD_OFFICE_EQUIPMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_lad_office_equipments);
		
		Account assets_computer_equipments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_COMPUTER_EQUIPMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_computer_equipments);
		
		Account assets_lad_computer_equipments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_LAD_COMPUTER_EQUIPMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_lad_computer_equipments);
		
		Account assets_freehold_buildings = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_FREEHOLD_BUILDINGS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_freehold_buildings);
		
		Account assets_af_building_depriciation = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_AF_BUILDING_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_af_building_depriciation);
		
		Account assets_leasehold_property_improvements = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_LEASEHOLD_PROPERTY_IMPROVEMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_leasehold_property_improvements);
		
		Account assets_al_property_improvements_depriciation = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_AL_PROPERTY_IMPROVEMENT_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_al_property_improvements_depriciation);
		
		Account assets_it_equipments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_IT_EQUIPMENTS, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_it_equipments);
		
		Account assets_a_office_equipments_depriciation = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_A_OFFICE_EQUIPMENTS_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_a_office_equipments_depriciation);
		
		Account assets_a_it_equipments_depriciation = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_A_IT_EQUIPMENTS_DEPRECIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_a_it_equipments_depriciation);
		
		Account assets_furniture_fixtures = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_FURNITURES_FIXTURES, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_furniture_fixtures);
		
		Account assets_a_furniture_fixture_depriciation = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_A_FURNITURE_FIXTURES_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_a_furniture_fixture_depriciation);
		
		Account assets_plants_machinary = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_PLANTS_mACHINARY, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_plants_machinary);
		
		Account assets_a_plants_machinary_depriciations = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_A_PLANT_MACHINARY_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_a_plants_machinary_depriciations);
		
		Account assets_vechiles = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_VEHICLES, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_vechiles);
		
		Account assets_a_veichles_depriciations = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_A_VEICHLES_DEPRICIATION, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_a_veichles_depriciations);
		
		Account assets_intangibles = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_INTANGIBLES, assets_fixed, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_intangibles);
		
		
		
		Account assets_advance_tax = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_ADVANCE_TAX, assets_other_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_advance_tax);
		
		Account assets_inventory = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_INVENTORY, assets_other_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_inventory);
		
		Account assets_prepaid_expenses = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_PREPAID_EXPENSES, assets_other_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_prepaid_expenses);
		
		//Account assets_undeposited_funds = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_UNDEPOSITED_FUNDS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		
		
		Account assets_service_tax_incurred_on_expenses = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_SERVICE_TAX_INCURED_ON_EXPENSES, assets_other_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_service_tax_incurred_on_expenses);
		
		//Account assets_undeposited_funds = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_UNDEPOSITED_FUNDS, assets_current, "", this.preferences.getPreventPostingBeforeDate());
		
		
		Account assets_cash_petty_cash = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.ASSETS_CASH_PETTY_CASH, assets_cash, "", this.preferences.getPreventPostingBeforeDate());
		session.save(assets_cash_petty_cash);
		
	}
	
	public void initializeDefaultlLiabilitiesAccounts(){


		Account equity = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity);
		
		Account equity_retained_earnings = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_RETAINED_EARINGS, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_retained_earnings);
		
		Account equity_owner_share = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_OWNER_SHARE, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_owner_share);
		
		Account equity_opening_balance_offset = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_OPENING_BALANCE_OFFSET, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_opening_balance_offset);
		
		Account equity_gain_loss_exchange = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_GAIN_LOSS_EXCHANGE, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_gain_loss_exchange);
		
		Account equity_ordinary_shares = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_ORDINARY_SHARES, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_ordinary_shares);
		
		Account equity_opening_balance = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_OPENING_BALANCE, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_opening_balance);
		
		Account equity_reserves = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_RESERVES, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_reserves);
		
		Account equity_ytd = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_YTD, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_ytd);
		
		Account equity_dividend = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.EQUITY_DIVIDEND, equity, "", this.preferences.getPreventPostingBeforeDate());
		session.save(equity_dividend);
		
		Account liability_non_current = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_NON_CURRENT, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_non_current);
		
		Account liability_loans = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_LOAN, liability_non_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_loans);
		
		Account liability_secured_loans = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SECURED_LOAN, liability_loans, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_secured_loans);
		
		Account liability_unsecured_loans = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_UNSECURED_LOAN, liability_loans, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_unsecured_loans);
				
		Account liability_client_credits = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CLIENT_CREDITS, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_client_credits);
		
		Account liability_long_term = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_LONG_TERM, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_long_term);
		
		Account liability_long_term_loans = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_LONG_TERM_LOANS, liability_long_term, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_long_term_loans);
		
		Account liability_hire_purchase_creditors = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_HIRE_PURCHASE_CREDITORS, liability_long_term, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_hire_purchase_creditors);
		
		Account liability_deferred_tax = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_DEFERRED_TAX, liability_long_term, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_deferred_tax);
		
		Account liability_other_balance_sheet_category = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_OTHER_BALANCE_SHEET_CATEGORY, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_other_balance_sheet_category);
		
		Account liability_bank_revaluations = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_BANK_REVALUATIONS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_bank_revaluations);
		
		Account liability_historical_adjustments = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_HISTORICAL_ADJUSTMENTS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_historical_adjustments);
		
		Account liability_realised_currency_gains = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_REALISED_CURRENCY_GAINS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_realised_currency_gains);
		
		Account liability_unrealised_currency_gains = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_UNREALISED_CURRENCY_GAINS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_unrealised_currency_gains);
		
		Account liability_roundings = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_ROUNDINGS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_roundings);
		
		Account liability_vat_on_import = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_VAT_ON_IMPORTS, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_vat_on_import);
		
		Account liability_suspenses = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SUSPENSES, liability_other_balance_sheet_category, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_suspenses);

		Account liability_current = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CURRENT, liability, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_current);
		
		Account liability_taxpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_TAXPAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_taxpayable);
		
		Account liability_vatpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_VATPAYABLE, liability_taxpayable, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_vatpayable);
		
		Account liability_cstpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CSTPAYABLE, liability_taxpayable, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_cstpayable);
		
		Account liability_employee_taxpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_EMPLOYEE_TAXPAYABLE, liability_taxpayable, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_employee_taxpayable);
		
		Account liability_other_current = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_OTHER_CURRENT_LIABILITIES, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_other_current);
		
		Account liability_incometax_payable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_INCOME_TAX_PAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_incometax_payable);
		
		Account liability_salestax = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SALES_TAX, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_salestax);
		
		Account liability_wagespayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_WAGES_PAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_wagespayable);
		
		Account liability_accountpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_ACCOUNTS_PAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_accountpayable);
		
		Account liability_supperannation_payable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SUPPERANNATION_PAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_supperannation_payable);
		
		Account liability_unpaid_expenses_claims = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_UNPAID_EXPENSES_CLAIMS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_unpaid_expenses_claims);
		
		Account liability_tracking_transfer = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_TRACKING_TRANSFERS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_tracking_transfer);
		
		Account liability_owner_drawing = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_OWNER_DRAWING, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_owner_drawing);
		
		Account liability_owner_fund_introduced = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_OWNER_FUND_INTRODUCED, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_owner_fund_introduced);
		
		Account liability_credit_cards = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CREDIT_CARD, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_credit_cards);
		
		Account liability_service_taxpayable = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SERVICE_TAXPAYABLE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_service_taxpayable);
		
		Account liability_creditors = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CREDITORS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_creditors);
		
		Account liability_payee = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_PAYEE, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_payee);
		
		Account liability_nat = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_NAT, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_nat);
		
		Account liability_salestax_unfiled = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SALESTAX_UNFILED, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_salestax_unfiled);
		
		Account liability_salestax_filed = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_SALESTAX_FILLED, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_salestax_filed);
		
		Account liability_corporation_tax = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_CORPORATIONTAX, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_corporation_tax);
		
		Account liability_loans1 = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_LOANS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_loans1);
		
		Account liability_mortgages = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_MORTGAGES, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_mortgages);
		
		Account liability_accurals = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_ACCURALS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_accurals);
		
		Account liability_directors_current_accounts = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_DIRECTORS_CURRENT_ACCOUNT, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_directors_current_accounts);
		
		Account liability_net_salaries = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_NET_SALARIES, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_net_salaries);
		
		Account liability_pesions = new Account(Account.TYPE_EXPENSE, null,  AccounterConstants.LIABLITY_PENSIONS, liability_current, "", this.preferences.getPreventPostingBeforeDate());
		session.save(liability_pesions);
		

	
	
	}
	
}
