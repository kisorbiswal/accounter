package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;

public abstract class CompanyInitializer {

	/**
	 * this is used to get the company type
	 */
	public abstract void init();
	public abstract Company getCompany();
	
	
	
	int type;
	
	String name;
	
	String number;
	
	String comment;
	
	FinanceDate asOf;
	
	Account parentaccount = new Account();
	
	Account expense  = new Account(type, number, name, parentaccount, comment, asOf);
	
	public void consulting_and_accounting(){
		
	}
	public void deprecation(){
		
	}
	public void meals_and_entertainment(){
		
	}
	public void general_expenses(){
		
	}
	public void insurance(){
		
	}
	public void interest_expense(){
		
	}
	public void legal_expense(){
		
	}
	
	public abstract void office_expense();
	
	public void printing_and_stationary(){
		
	}
	public void freight_and_courier(){
		
	}
	public void repair_and_maintainance(){
		
	}
	public void telephone_and_internet(){
		
	}
	public void rent(){
		
	}
	public abstract void motor_veichel_expense();
	
	public void veichel_fuel(){
		
	}
	public void gas_expense(){
		
	}
	public void veichle_lease_expense(){
		
	}
	public void veichle_repair_maintance(){
		
	}
	
	public void wages_and_salaries(){
		
	}
	public void superannuation(){
		
	}
	public void subscription(){
		
	}
	public void bank_revaluations(){
		
	}
	public void unrealised_currency_gain(){
		
	}
	public void incometax_expense(){
		
	}
	public void bad_debit(){
		
	}
	public void bank_fee_charge(){
		
	}
	public void credit_card_charge(){
		
	}
	public abstract void travel_expenses();
	
	public void travel_international(){
		
	}
	public void travel_national(){
		
	}
	public void automobile_expense(){
		
	}
	public void deprication_expense(){
		
	}
	public abstract void other_expenses();
	
	public void explain_gain_loss(){
		
	}
	
	public void miscellaneous(){
		
	}
	
	public void penalties_settelment(){
		
	}
	
	public void utilities(){
		
	}
	public void taxes_and_lisences(){
		
	}
	
	
	public  abstract void Cost_of_good_sold();

	public void product_material_purchased_A(){
		
	}
	public void product_material_purchased_B(){
		
	}
	public void product_material_purchased_C(){
	
	}
	public void product_material_purchased_D(){
	
	}
	public void product_material_purchased_E(){
	
	}
	public void product_material_purchased_F(){
	
	}
	public void carriage(){
		
	}
	public void discount_taken(){
		
	}
	public void import_Duty(){
		
	}
	public void stock_value_change(){
		
	}
	public void finished_goods_value_change(){
		
	}
	public void work_in_progress_value_change(){
		
	}
	
	
	public abstract void other_direct_cost();
	
	public void direct_labour(){
		
	}
	public void direct_employers_NI(){
		
	}
	public void other_direct_employee_related_costs(){
		
	}
	public void direct_expense(){
		
	}
	public void direct_travel(){
		
	}
	public void direct_consumables(){
		
	}
	public void merchant_account_fees(){
		
	}
	public void commissions_paid(){
		
	}



}
