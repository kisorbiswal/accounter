package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;

public class AccounterClientConstants implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SYMBOL_PLUS = "+";
	public static final String SYMBOL_MINUS = "-";

	public static final String MEMO_OPENING_BALANCE = "Opening Balance";
	// Constants for Account Names

	public static final String ACCOUNTS_RECEIVABLE = "Accounts Receivable";
	public static final String ACCOUNTS_PAYABLE = "Accounts Payable";
	public static final String OPENING_BALANCE = "Opening Balances";
	public static final String UN_DEPOSITED_FUNDS = "Un Deposited Funds";
	public static final String PENDING_ITEM_RECEIPTS = "Pending Item Receipts";
	public static final String SALES_TAX_PAYABLE = "Sales Tax Payable";
	public static final String EMPLOYEE_PAYROLL_LIABILITIES = "Employee Payroll Liabilities";
	public static final String RETAINED_EARNINGS = "Retained Earnings";
	public static final String CASH_DISCOUNT_GIVEN = "Cash Discount Given";
	public static final String WRITE_OFF = "Write off";
	public static final String CASH_DISCOUNT_TAKEN = "Cash Discount taken";
	public static final String BANK_CHARGE = "Bank Charge";

	// Constants for Account types
	public static final String TYPE_INCOME = "Income";
	public static final String TYPE_OTHER_INCOME = "Other Income";
	public static final String TYPE_EXPENSE = "Expense";
	public static final String TYPE_OTHER_EXPENSE = "Other Expense";
	public static final String TYPE_COST_OF_GOODS_SOLD = "Cost of Goods Sold";
	public static final String TYPE_CASH = "Cash";
	public static final String TYPE_BANK = "Bank";
	public static final String TYPE_OTHER_CURRENT_ASSET = "Other Current Asset";
	public static final String TYPE_INVENTORY_ASSET = "Inventory Asset";
	public static final String TYPE_OTHER_ASSET = "Other Asset";
	public static final String TYPE_FIXED_ASSET = "Fixed Asset";
	public static final String TYPE_CREDIT_CARD = "Credit Card";
	public static final String TYPE_PAYROLL_LIABILITY = "Payroll Liability";
	public static final String TYPE_OTHER_CURRENT_LIABILITY = "Current Liability";
	public static final String TYPE_LONG_TERM_LIABILITY = "Long Term Liability";
	public static final String TYPE_EQUITY = "Equity";

	public static final String TYPE_ACCOUNT_RECEIVABLE = "Accounts Receivable";
	public static final String TYPE_ACCOUNT_PAYABLE = "Accounts Payable";

	// Constants for Bank Account Types
	public static final String BANK_ACCCOUNT_TYPE_NONE = "None";
	public static final String BANK_ACCCOUNT_TYPE_CHECKING = "Checking";
	public static final String BANK_ACCCOUNT_TYPE_SAVING = "Saving";
	public static final String BANK_ACCCOUNT_TYPE_MONEY_MARKET = "Money Market";

	// Cash Flow Categories.
	public static final String CASH_FLOW_CATEGORY_FINANCING = "Financing";
	public static final String CASH_FLOW_CATEGORY_INVESTING = "Investing";
	public static final String CASH_FLOW_CATEGORY_OPERATING = "Operating";

	// Payment Method names.
	public static final String PAYMENT_METHOD_CASH = "Cash";
	public static final String PAYMENT_METHOD_CHECK = "Check";
	public static final String PAYMENT_METHOD_CHECK_FOR_UK = "Cheque";
	public static final String PAYMENT_METHOD_CREDITCARD = "Credit Card";

	// Constants for Credits and Payments

	public static final String CUSTOMER_CREDIT_MEMO = "Customer Credit";
	public static final String VENDOR_PAYMENT = Global.get().messages()
			.supplierPayment(Global.get().Vendor());
	public static final String CUSTOMER_PAYMENT = "Customer Payment";

	public static final String CUSTOMER_PREPAYMENT = "Customer PrePayment";
	public static final String VENDOR_CREDIT_MEMO = Global.get().messages()
			.supplierCredit(Global.get().Vendor());

	// constants for Transaction Names

	public static final String TYPE_CASH_SALES = "Cash Sale";
	public static final String TYPE_CASH_PURCHASE = "Cash Purchase";
	public static final String TYPE_CREDIT_CARD_CHARGE = "Credit Card Charge";
	public static final String TYPE_CUSTOMER_CREDIT_MEMO = "Customer Credit";
	public static final String TYPE_CUSTOMER_PREPAYMENT = "Customer PrePayment";

	public static final String TYPE_CUSTOMER_REFUNDS = "Customer Refund";
	public static final String TYPE_ENTER_BILL = Global.get().messages()
			.supplierBill(Global.get().Vendor());
	public static final String TYPE_ESTIMATE = "Quote";
	public static final String TYPE_INVOICE = "Invoice";
	public static final String TYPE_ISSUE_PAYMENT = "Issue Payment";
	public static final String TYPE_MAKE_DEPOSIT = "Deposit/Transfer Funds";
	public static final String TYPE_PAY_BILL = Global.get().messages()
			.supplierPayment(Global.get().Vendor());
	public static final String TYPE_RECEIVE_PAYMENT = "Customer Payment";
	public static final String TYPE_TRANSFER_FUND = "Transfer Fund";
	public static final String TYPE_VENDOR_CREDIT_MEMO = Global.get()
			.messages().supplierCredit(Global.get().Vendor());
	public static final String TYPE_WRITE_CHECK = "Write Check";
	public static final String TYPE_JOURNAL_ENTRY = "Journal Entry";
	public static final String TYPE_PAY_SALES_TAX = "Pay Sales Tax";
	public static final String TYPE_SALES_ORDER = "Sales Order";
	public static final String TYPE_PURCHASE_ORDER = "Purchase Order";
	public static final String TYPE_ITEM_RECEIPT = "Item Receipt";
	public static final String TYPE_RECEIVE_VAT = "Receive VAT ";
	public static final String TYPE_VAT_RETURN = "VAT Return ";

	// CONSTANTS FOR TRANSACTION STATUS
	public static final String STATUS_NOT_PAID = "Not Paid";
	public static final String STATUS_PARTIALLY_PAID = "Partially Paid";
	public static final String STATUS_PAID = "Paid";
	public static final String STATUS_OPEN = "Open";
	public static final String STATUS_ACCEPTED = "Accepted";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_UN_APPLIED = "Un Applied";
	public static final String STATUS_PARTIALLY_APPLIED = "Partially Applied";
	public static final String STATUS_APPLIED = "Applied";
	public static final String STATUS_NOT_ISSUED = "Not Issued";
	public static final String STATUS_ISSUED = "Issued";
	public static final String STATUS_NEW = "New";
	public static final String STATUS_POSTED = "Posted";
	public static final String STATUS_NOT_INVOICED = "Not Invoiced";
	public static final String STATUS_INVOICED = "Invoiced";
	public static final String STATUS_PARTIALLY_INVOICED = "Partially Invoiced";
	public static final String STATUS_NOT_RECEIVED = "Not Received";
	public static final String STATUS_RECEIVED = "Received";
	public static final String STATUS_PARTIALLY_RECEIVED = "Partially Received";

	public static final String SALES_INCOME_TYPE_A = "Sales/Income Type A";
	public static final String SALES_INCOME_TYPE_B = "Sales/Income Type B";
	public static final String SALES_INCOME_TYPE_C = "Sales/Income Type C";
	public static final String SALES_INCOME_TYPE_D = "Sales/Income Type D";
	public static final String SALES_INCOME_TYPE_E = "Sales/Income Type E";
	public static final String MISCELLANEOUS_INCOME = "Miscellaneous Income";
	public static final String DISTRIBUTION_AND_CARRIAGE = "Distribution & Carriage";
	public static final String DISCOUNTS = "Discounts";
	public static final String COMMISSION_RECIEVED = "Commissions Received";
	public static final String CREDIT_CHARGES_LATEPAYMENT = "Credit Charges (Late Payment)";
	public static final String INSURANCE_CLAIMS = "Insurance Claims";
	public static final String INTEREST_INCOME = "Interest Income";
	public static final String RENT_INCOME = "Rent Income";
	public static final String ROYALTIES_RECIEVED = "Royalties Received";
	public static final String PROFIT_OR_LOSS_ON_SALES_ASSETS = "Profit/Loss on Sales of Assets";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A = "Products/Materials Purchased Type A";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B = "Products/Materials Purchased Type B";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C = "Products/Materials Purchased Type C";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D = "Products/Materials Purchased Type D";
	public static final String PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E = "Products/Materials Purchased Type E";
	public static final String OPENING_STOCK = "Opening Stock";
	public static final String CLOSING_STOCK = "Closing Stock";
	public static final String FINISHED_GOODS = "Finished Goods";
	public static final String WORK_IN_PROGRESS = "Work in Progress";
	public static final String CARRIAGE = "Carriage";
	public static final String DISCOUNTS_TAKEN = "Discounts Taken";
	public static final String IMPORT_DUTY = "Import Duty";

	public static final String DIRECT_LABOUR = "Direct Labour";
	public static final String DIRECT_EMPLOYERS_NI = "Direct Employers NI";
	public static final String OTHER_DIRECT_EMPLOYEE_RELATED_COSTS = "Other Direct Employee Related Costs";
	public static final String DIRECT_EXPENSES = "Direct Expenses";
	public static final String DIRECT_TRAVEL = "Direct Travel";
	public static final String DIRECT_CONSUMABLES = "Direct Consumables";
	public static final String MERCHANT_ACCOUNT_FEES = "Merchant Account Fees";
	public static final String COMMISSIONS_PAID = "Commissions Paid";

	public static final String INDIRECT_LABOUR = "Indirect Labour";
	public static final String INDIRECT_EMPLOYERS_NI = "Indirect Employers NI";
	public static final String DIRECTORS_REMUNERATION = "Directors Remuneration";
	public static final String CASUAL_LABOUR = "Casual Labour";
	public static final String EMPLOYERS_PANSION_CONTRIBUTIONS = "Employers Pension Contributions";
	public static final String SSP_RECLAIMED = "SSP Reclaimed";
	public static final String SMP_RECLAIMED = "SMP Reclaimed";
	public static final String EMPLOYEE_BENIFITS = "Employee Benefits";
	public static final String MEDICAL_INSURANCE = "Medical Insurance";
	public static final String RECRUITMENT = "Recruitment";
	public static final String TRAINING = "Training";

	public static final String RENT = "Rent";
	public static final String GENERAL_RATES = "General Rates";
	public static final String WATER_RATES = "Water Rates";
	public static final String ELECTRICITY = "Electricity";
	public static final String GAS = "Gas";
	public static final String OIL = "Oil";
	public static final String OFFICE_CLEANING = "Office Cleaning";
	public static final String OFFICE_MACHINE_MAINTENANCE = "Office Machine Maintenance";
	public static final String REPAIR_RENEWALS = "Repairs & Renewals";

	public static final String OFFICE_CONSUMABLES = "Office Consumables";
	public static final String BOOKS_ETC = "Books etc";
	public static final String INTERNET = "Internet";
	public static final String POSTAGE = "Postage";
	public static final String PRINTING = "Printing";
	public static final String STATIONERY = "Stationery";
	public static final String SUBSCRIPTIONS = "Subscriptions";
	public static final String TELEPHONE = "Telephone";
	public static final String CONFERENCES_AND_SEMINARS = "Conferences & Seminars";
	public static final String CHARITY_DONATIONS = "Charity Donations";

	public static final String INSURANCES_BUSINESS = "Insurances - Business";

	public static final String ADVERTISING_AND_MARKETING = "Advertising & Marketing";
	public static final String LOCAL_ENTERTAINMENT = "Local Entertainment";
	public static final String OVERSEAS_ENTERTAINMENT = "Overseas Entertainment";
	public static final String INDIRECT_LOCAL_TRAVEL = "Indirect Local Travel";
	public static final String INDIRECT_OVERSEAS_TRAVEL = "Indirect Overseas Travel";
	public static final String SUBSISTENCE = "Subsistence";

	public static final String VECHILE_EXPENSES = "Vehicle Expenses";
	public static final String VECHILE_INSURANCE = "Vehicle Insurance";
	public static final String VECHILE_REPAIRS_AND_SERVICING = "Vehicle Repairs & Servicing";

	public static final String PROFESSIONAL_FEES = "Professional Fees";
	public static final String ACCOUNTANCY_FEES = "Accountancy Fees";
	public static final String CONSULTANY_FEES = "Consultancy Fees";
	public static final String LEGAL_FEES = "Legal Fees";

	public static final String BANK_INTEREST_PAID = "Banks Interest Paid";
	public static final String BANK_CHARGES = "Bank Charges";
	public static final String CREDIT_CHARGES = "Credit Charges";
	public static final String LEASE_PAYMENTS = "Lease Payments";
	public static final String LOAN_INTEREST_PAID = "Loan Interest Paid";
	public static final String CURRENCY_CHARGES = "Currency Charges";
	public static final String EXCHANGE_RATE_VARIANCE = "Exchange Rate Variance";
	public static final String BAD_DEBT_PROVISION = "Bad Debt Provision";
	public static final String BAD_DEBT_WRITE_OFF = "Bad Debt Write Off";

	public static final String DEPRECIATION = "Depreciation";
	public static final String OFFICE_EQUIPMENT_DEPRECIATION = "Office Equipment Depreciation";
	public static final String IT_EQUIPMENT_DEPRECIATION = "IT Equipment Depreciation";
	public static final String FURNITURE_AND_FIXTURES_DEPRECIARION = "Furniture& Fixtures Depreciation";
	public static final String PLANT_OR_MACHINERY_DEPRECIATION = "Plant/Machinery Depreciation";
	public static final String VECHILE_DEPRECIATION = "Vehicle Depreciation";
	public static final String FREEHOLD_BUILDING_DEPRECIATION = "Freehold Building Depreciation";
	public static final String LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION = "Leasehold Property Improvements Depreciation";

	public static final String DEBTORS_ACCOUNTS_RECEIVABLE = "Debtors (Accounts-Receivable)";
	public static final String BANK_CURRENT_ACCOUNT = "Bank Current Account";
	public static final String BANK_DEPOSIT_ACCOUNT = "Bank Deposit Account";
	public static final String PETTY_CASH = "Petty Cash";
	public static final String PRE_PAYMENTS = "Pre-Payments";
	public static final String ADVANCES_TO_EMPLOYEES = "Advances to Employees";
	public static final String STOCK = "Stock";

	public static final String CREDITORS_ACCOUNTS_PAYBLE = "Creditors  (Accounts-Payable)";
	public static final String CREDIT_CARDS = "Credit Cards";
	public static final String PAYEE_EMPLOYEMENT_TAX = "PAYEE (Employment Tax)";
	public static final String NATIONAL_INSURANCE_TAX = "National Insurance tax";
	public static final String SALES_TAX_VAT = "Sales Tax (VAT)";
	public static final String CORPORATION_RAX = "Corporation Tax";
	public static final String LOANS = "Loans";
	public static final String MORTGAGES = "Mortgages";
	public static final String ACCRUALS = "Accruals";
	public static final String DIRECTORS_CURRENT_ACCOUNT = "Directors Current Account";
	public static final String NET_SALARIES = "Net Salaries";
	public static final String PENSIONS = "Pensions";
	public static final String UNPAID_EXPENSE_CLAIMS = "Unpaid Expense Claims";

	public static final String FREEHOLD_BUILDINGS = "Freehold Buildings";
	public static final String ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION = "Accumulated Freehold Building Depreciation";
	public static final String LEASEHOLD_PROPERTY_IMPROVEMENTS = "Leasehold Property Improvements";
	public static final String ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION = "Accumulated Leasehold Property Improvements Depreciation";
	public static final String OFFICE_EQUIPMENT = "Office Equipment";
	public static final String ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION = "Accumulated Office Equipment Depreciation";
	public static final String IT_EQUIPMENT = "IT Equipment";
	public static final String ACCUMULATED_IT_EQUIPMENT_DEPRECIATION = "Accumulated IT Equipment Depreciation";
	public static final String FURNITURE_AND_FIXTURES = "Furniture & Fixtures";
	public static final String ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION = "Accumulated Furniture & Fixtures Depreciation";
	public static final String PLANT_AND_MACHINERY = "Plant & Machinery";
	public static final String ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION = "Accumulated Plant & Machinery Depreciation";
	public static final String VECHICLES = "Vehicles";
	public static final String ACCUMULATED_VEHICLES_DEPRECIATION = "Accumulated Vehicles Depreciation";
	public static final String INTANGIBLES = "Intangibles";

	public static final String LONG_TERM_LOANS = "Long Term Loans";
	public static final String HIRE_PURCHASE_CREDITORS = "Hire Purchase Creditors";
	public static final String DEFERRED_TAX = "Deferred Tax";

	public static final String BANK_REVALUATIONS = "Bank Revaluations";
	public static final String HISTORICAL_ADJUSTMENT = "Historical Adjustment";
	public static final String REALISED_CURRENCY_GAINS = "Realised Currency Gains";
	public static final String UNREALISED_CURRENCY_GAINS = "Unrealised Currency Gains";
	public static final String ROUNDING = "Rounding";
	public static final String VAT_ON_IMPORTS = "VAT on Imports";
	public static final String SUSPENSE = "Suspense";

	public static final String ORDINARY_SHARES = "Ordinary Shares";
	public static final String RESERVES_RETAINED_EARNINGS = "Reserves (Retained Earnings)";
	public static final String P_AND_L_BOUGHT_FORWARD_OR_YTD = "P&L Bought Forward/YTD";
	public static final String DIVIDENDS = "Dividends";

	public static final String LESS_ACCUMULATED_DEPRECIATION_ON = "Less Accumulated Depreciation on ";

	public static final String JOURNAL_ENTRY_FOR_DEPRECIATION = "Journal Entry for Running Depreciation";
	public static final String CREDIT_CARD_COMPANIES = "Credit Card Companies";

	public static final String TYPE_CASH_EXPENSE = "Cash Expense";
	public static final String TYPE_EMPLOYEE_EXPENSE = "Employee Expense";
	public static final String TYPE_CREDIT_CARD_EXPENSE = "Credit Card Expense";
	public static final String TYPE_PAY_VAT = "Pay VAT";

	// UK default VAT Agency
	public static final String DEFAULT_VAT_AGENCY_NAME = "HM Revenue & Customs VAT";

	public static final String Box1_Description = "Total amount of VAT charged on sales and other supplies";
	public static final String Box2_Description = "Total amount of VAT charged on acquisitions from other EC member states";
	public static final String TYPE_VENDOR_PAYMENT = Global.get().messages()
			.supplierPrePayment(Global.get().Vendor());
}
