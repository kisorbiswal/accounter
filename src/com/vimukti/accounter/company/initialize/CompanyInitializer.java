package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;


public abstract class CompanyInitializer {

	/**
	 * company preferences
	 */
	CompanyPreferences preferences = new CompanyPreferences();
	/**
	 * this is used to get the company type
	 */
	public abstract void init();
	public abstract Company getCompany();
	
	
	public void initializeDefaultAccounts(){
		
		Account parentaccount = new Account();
		
		Account expense  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.TYPE_EXPENSE, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		
		Account income  = new Account(Account.TYPE_INCOME, null, AccounterConstants.TYPE_INCOME, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		
		Account liability  = new Account(Account.TYPE_LIABILITY, null, AccounterConstants.TYPE_INCOME, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		
		Account assets  = new Account(Account.TYPE_ASSET, null, AccounterConstants.TYPE_CURRENT_ASSET, parentaccount, "", this.preferences.getPreventPostingBeforeDate());
		
		Account advertising_and_marketing = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CONSULTING_AND_ACCOUNTING, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Consulting_and_Accounting = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_ADEVRTISING_AND_MARKETING, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Deprication = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DEPRICATION, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Meals_and_Entertainment = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MEALS_AND_ENTERTAINMENT, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account General_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_GENERAL_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Insurance = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INSURANCE, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Interest_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INTEREST_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Legal_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_LEGAL_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Motor_Veichle_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MOTOR_VEICHLE, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Fuel_Gas  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHEL_FUEL, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Veichle_lease = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHLE_LEASE, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Repair_Maintainance  = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_VEICHLE_REPAIR_MAINTAINANCE, Motor_Veichle_Expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Wages_Salaries = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_WAGES_SALARIES, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Superannuation = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_SUPERANNUATION, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Subscription = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_SUBSCRIPTION, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Back_Revaluation = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BANKREVALUATION, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Unrealized_Currency_Gains = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_UNREALISED_CURRENCY_GAIN, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Realized_Currency_Gains = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_REALISED_CURRENCY_GAIN, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Income_Tax_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INCOME_TAX_EXPENSE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Bad_Debit = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BAD_DEBIT, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Bank_Fee_Charges = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_BANK_FEE_CHARGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Credit_card_charges = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CREDIT_CARD_CHARGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Travel_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Travel_International = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL_INTERNATIONAL, Travel_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Travel_National = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TRAVEL_NATIONAL, Travel_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Automobile_Expense = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_AUTOMOBILE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Postage = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_POSTAGE, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Other_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_OTHER, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Explain_Gain_Loss = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_GAIN_LOSS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Miscellaneous = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MISCELLANEOUS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Penalties_Settelments = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PENALTIES_SETELLMENTS, Other_Expenses, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Utilities = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_UTILITIES, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Tax_Lisences = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_TAX_LISENSES, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account cost_of_goods_sold = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_COST_OF_GOODS_SOLDs, expense, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_A = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_A, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_B = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_B, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_C = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_C, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_D = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_D, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_E = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_E, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account product_purchase_F = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_PRODUCT_PURCHASE_F, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Carriage = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_CARRIAGE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account discounts_taken = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DISCOUNTS, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account import_duty = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_IMPORT_DUTY, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account stock_value_changed = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_STOCK_VALUE_CHAGE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account finished_good_value = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_FINISHED_GOOD_VALUE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account work_in_progress_value = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_WORK_IN_PROGRESS_VALUE, cost_of_goods_sold, "", this.preferences.getPreventPostingBeforeDate());
		
		Account Other_Direct_Costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_OTHER_DIRECT_COST, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account Direct_Labour = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_LABOUR, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Direct_Employee_NI = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_EMPLOYEE_NI, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account direct_Employee_Related_costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_EMPLOYEE_RELATED_COSTS, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Direct_Expenses = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Direct_Travel = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_TRAVEL, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Direct_Consumable = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_DIRECT_CONSUMABLE, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Merchant_Account_Fees = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_MERCHANY_ACCOUNT_FEES, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account Cosmission_Paid = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_COMISSION_PAID, Other_Direct_Costs, "", this.preferences.getPreventPostingBeforeDate());

		Account indirect_costs = new Account(Account.TYPE_EXPENSE, null, AccounterConstants.EXPENSE_INDIRECT_COSTS, expense, "", this.preferences.getPreventPostingBeforeDate());

		Account indirect_labour = new Account(Account.TYPE_EXPENSE, null, "Indirect Labour", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account indirect_employee_NI = new Account(Account.TYPE_EXPENSE, null, "Indirect Employee NI", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account directors_remuneration = new Account(Account.TYPE_EXPENSE, null, "Directors Remuneration", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account casual_labour = new Account(Account.TYPE_EXPENSE, null, "Casual Labour", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account employeer_pension = new Account(Account.TYPE_EXPENSE, null, "Employeer Pension Contribution", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account SSP_reclaimed = new Account(Account.TYPE_EXPENSE, null, "SSP Reclaimed", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account SMP_reclaimed = new Account(Account.TYPE_EXPENSE, null, "SMP Reclaimed", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account Employee_Benefits = new Account(Account.TYPE_EXPENSE, null, "Employee Benefits", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account medical_insurance = new Account(Account.TYPE_EXPENSE, null, "Medical Insurance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account recruitment = new Account(Account.TYPE_EXPENSE, null, "Recruitment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account training = new Account(Account.TYPE_EXPENSE, null, "Training", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account rent = new Account(Account.TYPE_EXPENSE, null, "Rent", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account general_rates = new Account(Account.TYPE_EXPENSE, null, "General Rates", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account water_rates = new Account(Account.TYPE_EXPENSE, null, "Water Rates", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account electricity = new Account(Account.TYPE_EXPENSE, null, "Electricity", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account gas = new Account(Account.TYPE_EXPENSE, null, "Gas", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account oil = new Account(Account.TYPE_EXPENSE, null, "Oil", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account office_cleaning = new Account(Account.TYPE_EXPENSE, null, "Office Cleaning", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account office_machine_maintainance = new Account(Account.TYPE_EXPENSE, null, "Office Machine Maintainance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account repair_renewals = new Account(Account.TYPE_EXPENSE, null, "Repair and Renewals", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account office_consumable = new Account(Account.TYPE_EXPENSE, null, "Office Consumables", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account books = new Account(Account.TYPE_EXPENSE, null, "Books etc", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account internet = new Account(Account.TYPE_EXPENSE, null, "Internet", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account postage = new Account(Account.TYPE_EXPENSE, null, "Postage", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account printing = new Account(Account.TYPE_EXPENSE, null, "Printing", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account stationary = new Account(Account.TYPE_EXPENSE, null, "Stationary", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account subscription = new Account(Account.TYPE_EXPENSE, null, "Subscription ", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account telephone = new Account(Account.TYPE_EXPENSE, null, "Telephone", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account conference_seminar = new Account(Account.TYPE_EXPENSE, null, "Conference and Seminar", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account charity_donations = new Account(Account.TYPE_EXPENSE, null, "Charity Donations", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account insurance_business = new Account(Account.TYPE_EXPENSE, null, "Insurance Bussiness", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account advertising_marketting = new Account(Account.TYPE_EXPENSE, null, "Advertising Bussiness", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account local_entertainment = new Account(Account.TYPE_EXPENSE, null, "Local Entertainment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account oversee_entertainment = new Account(Account.TYPE_EXPENSE, null, "Oversee Entertainment", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account indirect_local_travel = new Account(Account.TYPE_EXPENSE, null, "Indirect Local Travel", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account indirect_oversee_travel = new Account(Account.TYPE_EXPENSE, null, "Indirect Oversee Travel", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account subsistence = new Account(Account.TYPE_EXPENSE, null, "Subsistence", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account veichle_expense = new Account(Account.TYPE_EXPENSE, null, "Veichle Expenses", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account veichle_insurance = new Account(Account.TYPE_EXPENSE, null, "Veichle Insurance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account veichle_repair = new Account(Account.TYPE_EXPENSE, null, "Veichel Repair & Servicing", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account professional_fees = new Account(Account.TYPE_EXPENSE, null, "Professional Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account accountancy_fees = new Account(Account.TYPE_EXPENSE, null, "Accountancy Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account consultancy_fees = new Account(Account.TYPE_EXPENSE, null, "Consultancy Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account legal_fees = new Account(Account.TYPE_EXPENSE, null, "Legal Fees", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account bank_paid_interest = new Account(Account.TYPE_EXPENSE, null, "Bank Paid Interest", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account bank_charges = new Account(Account.TYPE_EXPENSE, null, "Bank Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account credit_charges = new Account(Account.TYPE_EXPENSE, null, "Credit Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account lease_payments = new Account(Account.TYPE_EXPENSE, null, "Lease Payments", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account Loan_interest_paid = new Account(Account.TYPE_EXPENSE, null, "Loan Interest Paid", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account currency_charges = new Account(Account.TYPE_EXPENSE, null, "Currency Charges", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account exchange_rate_variance = new Account(Account.TYPE_EXPENSE, null, "Exchange Rate Variance", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account bad_debut_provision = new Account(Account.TYPE_EXPENSE, null, "Bad Debut Provision", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account bad_debut_write_off = new Account(Account.TYPE_EXPENSE, null, "Bad Debut write off", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account deprication = new Account(Account.TYPE_EXPENSE, null, "Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account office_equipment_deprication = new Account(Account.TYPE_EXPENSE, null, "Office Equipment Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account it_equipment_deprication = new Account(Account.TYPE_EXPENSE, null, "IT Equipment Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account furniture_fixture_deprication = new Account(Account.TYPE_EXPENSE, null, "Furniture Fixture Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account plant_machinary_deprication = new Account(Account.TYPE_EXPENSE, null, "Plant/Machinary Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account veichel_deprication = new Account(Account.TYPE_EXPENSE, null, "Veichel Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account freehold_building_deprication = new Account(Account.TYPE_EXPENSE, null, "Freehold Building Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		Account lease_property_improvement_deprication = new Account(Account.TYPE_EXPENSE, null, "Lease Property Improvement Deprication", indirect_costs, "", this.preferences.getPreventPostingBeforeDate());
		

	}

}
