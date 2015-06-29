package com.vimukti.accounter.migration;

import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.ComputionPayHead;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class PicklistUtilMigrator {
	static String depreciationForIdentity(int type) {
		switch (type) {
		case 1:
			return "All";
		case 2:
			return "Single";
		}
		return null;
	}

	static String depreciationMethodIdentity(int type) {
		switch (type) {
		case 1:
			return "StraightLine";
		case 2:
			return "ReducingBalance";
		}
		return null;
	}

	static String depreciationStatusIdentity(int type) {
		switch (type) {
		case 1:
			return "Approve";
		case 2:
			return "Rollback";
		}
		return null;
	}

	static String getAccountTypeIdentity(int type) {
		switch (type) {
		case 1:
			return "Cash";
		case 2:
			return "Bank";
		case 3:
			return "AccountReceivable";
		case 4:
			return "InventoryAsset";
		case 5:
			return "OtherCurrentAsset";
		case 6:
			return "FixedAsset";
		case 7:
			return "OtherAsset";
		case 8:
			return "AccountPayable";
		case 9:
			return "OtherCurrentLiability";
		case 10:
			return "CreditCard";
		case 11:
			return "OtherCurrentLiability";
		case 12:
			return "LongTermLiability";
		case 13:
			return "Equity";
		case 14:
			return "Income";
		case 15:
			return "CostOfGoodsSold";
		case 16:
			return "Expense";
		case 17:
			return "OtherIncome";
		case 18:
			return "OtherExpense";
		case 19:
			return "OtherCurrentLiability";
		case 20:
			return "OtherAsset";
		case 21:
			return "Paypal";
		}
		return null;
	}

	static String getFixedAssetStatusIdentifier(int i) {
		switch (i) {
		case 1:
			return "Pending";
		case 2:
			return "Register";
		case 3:
			return "Dispose";
		}
		return null;
	}

	static String getItemTypeIdentifier(int i) {
		switch (i) {
		case Item.TYPE_SERVICE:
			return "ServiceItem";
		case Item.TYPE_NON_INVENTORY_PART:
			return "NonInventory";
		case Item.TYPE_INVENTORY_PART:
			return "InventoryPart";
		case Item.TYPE_INVENTORY_ASSEMBLY:
			return "InventoryAssembly";
		}
		return null;
	}

	static String getPaymentMethodIdentifier(String name) {
		switch (name) {
		case "Credit Card":
			return "CreditCard";
		case "Direct Debit":
			return "DirectDebit";
		case "Master card":
			return "Mastercard";
		case "Online Banking":
			return "OnlineBanking";
		case "Standing Order":
			return "StandingOrder";
		case "Switch/Maestro":
			return "SwitchMaestro";
		case "Check":
			return "Cheque";
		}
		return name;
	}

	static String getMinistryDeptNameIdentity(String name) {
		switch (name) {
		case "Atomic Energy":
			return "AtomicEnergy";
		case "Chemicals and Petrochemicals":
			return "ChemicalsandPetrochemicals";
		case "Civil Aviation and Tourism":
			return "CivilAviationandTourism";
		case "Consumer Affairs, Food and Public Distribution":
			return "ConsumerAffairsFoodandPublicDistribution";
		case "Commerce and Textiles":
			return "CommerceandTextiles";
		case "Environment and Forests and Ministry of Earth Science":
			return "EnvironmentandForestsandMinistryofEarthScience";
		case "External Affairs and Overseas Indian Affairs":
			return "ExternalAffairsandOverseasIndianAffairs";
		case "Central Board of Direct Taxes":
			return "CentralBoardofDirectTaxes";
		case "Central Board of Excise and Customs":
			return "CentralBoardofExciseandCustoms";
		case "Contoller of Aid Accounts and Audit":
			return "ContollerofAidAccountsandAudit";
		case "Central Pension Accounting Office":
			return "CentralPensionAccountingOffice";
		case "Food Processing Industries":
			return "FoodProcessingIndustries";
		case "Health and Family Welfare":
			return "HealthandFamilyWelfare";
		case "Home Affairs and Development of North Eastern Region":
			return "HomeAffairsandDevelopmentofNorthEasternRegion";
		case "Human Resource Development":
			return "HumanResourceDevelopment";
		case "Information and Broadcasting":
			return "InformationandBroadcasting";
		case "Telecommunication and Information Technology":
			return "TelecommunicationandInformationTechnology";
		case "Law and Justice and Company Affairs":
			return "LawandJusticeandCompanyAffairs";
		case "Personnel, Public Grievances and Pesions":
			return "PersonnelPublicGrievancesandPesions";
		case "Petroleum and Natural Gas":
			return "PetroleumandNaturalGas";
		case "Plannning, Statistics and Programme Implementation":
			return "PlannningStatisticsandProgrammeImplementation";
		case "New and Renewable Energy":
			return "NewandRenewableEnergy";
		case "Rural Development and Panchayati Raj":
			return "RuralDevelopmentAndPanchayatiRaj";
		case "Science And Technology":
			return "ScienceAndTechnology";
		case "Social Justice and Empowerment":
			return "SocialJusticeandEmpowerment";
		case "Tribal Affairs":
			return "TribalAffairs";
		case "D/o Commerce (Supply Division)":
			return "DOFoCommerceSupplyDivision";
		case "Shipping and Road Transport and Highways":
			return "ShippingandRoadTransportandHighways";
		case "Urban Development, Urban Employment and Poverty Alleviation":
			return "UrbanDevelopmentUrbanEmploymentandPovertyAlleviation";
		case "Water Resources":
			return "WaterResources";
		case "President's Secretariat":
			return "PresidentsSecretariat";
		case "Lok Sabha Secretariat":
			return "LokSabhaSecretariat";
		case "Rajya Sabha secretariat":
			return "RajyaSabhasecretariat";
		case "Election Commission":
			return "ElectionCommission";
		case "Ministry of Defence (Controller General of Defence Accounts)":
			return "MinistryofDefence";
		case "Ministry of Railways":
			return "MinistryofRailways";
		case "Department of Posts":
			return "DepartmentofPosts";
		case "Department of Telecommunications":
			return "DepartmentofTelecommunications";
		case "Andaman and Nicobar Islands Administration":
			return "AndamanandNicobarIslandsAdministration";
		case "Chandigarh Administration":
			return "ChandigarhAdministration";
		case "Dadra and Nagar Haveli":
			return "DadraandNagarHaveli";
		case "Goa, Daman and Diu":
			return "GoaDamanandDiu";
		case "Pondicherry Administration":
			return "PondicherryAdministration";
		case "Pay and Accounts Officers (Audit)":
			return "PayandAccountsOfficers";
		case "Ministry of Non-conventional energy sources":
			return "MinistryofNonconventionalenergysources";
		case "Government Of NCT of Delhi":
			return "GovernmentOfNCTofDelhi";
		}
		return name;

	}

	static String getTransactionTypeIdentifier(int displayName) {
		String identifier = "";

		switch (displayName) {
		case Transaction.TYPE_CASH_SALES:
			identifier = "CashSale";
			break;
		case Transaction.TYPE_CASH_PURCHASE:
			identifier = "CashPurchase";
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			identifier = "PurchaseExpense";
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			identifier = "CreditMemo";
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			identifier = "CustomerRefund";
			break;
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			identifier = "CustomerPrepayment";
			break;
		case Transaction.TYPE_ENTER_BILL:
			identifier = "EnterBill";
			break;
		case Transaction.TYPE_ESTIMATE:
			identifier = "SalesQuotation";
			break;
		case Transaction.TYPE_INVOICE:
			identifier = "Invoice";
			break;
		case Transaction.TYPE_MAKE_DEPOSIT:
			identifier = "MakeDeposit";
			break;
		case Transaction.TYPE_PAY_BILL:
			identifier = "PayBill";
			break;
		case Transaction.TYPE_VENDOR_PAYMENT:
			identifier = "VendorPrepayment";
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			identifier = "ReceivePayment";
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			identifier = "TransferFund";
			break;
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			identifier = "DebitNote";
			break;
		case Transaction.TYPE_WRITE_CHECK:
			identifier = "WriteCheck";
			break;
		case Transaction.TYPE_JOURNAL_ENTRY:
			identifier = "JournalEntry";
			break;
		case Transaction.TYPE_PAY_TAX:
			identifier = "PayTax";
			break;
		case Transaction.TYPE_TAX_RETURN:
			identifier = "FileTax";
			break;
		case Transaction.TYPE_RECEIVE_TAX:
			identifier = "TaxRefund";
			break;
		case Transaction.TYPE_ADJUST_VAT_RETURN:
			identifier = "TAXAdjustment";
			break;
		case Transaction.TYPE_PURCHASE_ORDER:
			identifier = "PurchaseOrder";
			break;
		case Transaction.TYPE_CASH_EXPENSE:
			identifier = "PurchaseExpense";
			break;
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
			identifier = "PurchaseExpense";
			break;
		case Transaction.TYPE_STOCK_ADJUSTMENT:
			identifier = "StockAdujustment";
			break;
		case Transaction.TYPE_TDS_CHALLAN:
			identifier = "Tdschallan";
			break;
		case Transaction.TYPE_PAY_RUN:
			identifier = "PayRun";
			break;
		case Transaction.TYPE_PAY_EMPLOYEE:
			identifier = "PayEmployee";
			break;
		}
		return identifier;
	}

	public static String getProjectStatusIdentity(String status) {

		switch (status) {
		case "None":
			return null;
		case "Pending":
			return "Pending";
		case "Awarded":
			return "Awarded";
		case "In Progress":
			return "InProgress";
		case "Closed":
			return "Closed";
		case "Not Awarded":
			return "NotAwarded";
		}
		return "";
	}

	public static String getDeductorTypeIndentity(String deductorType) {
		switch (deductorType) {
		case "Central Government":
			return "CentralGovernment";
		case "State Government":
			return "StateGovernment";
		case "Statutory body (Central Govt.)":
			return "CentralGovtStatutoryBody";
		case "Statutory body (State Govt.)":
			return "StateGovtStatutoryBody";
		case "Autonomous body (Central Govt.)":
			return "CentralGovtAutonomousBody";
		case "Autonomous body (State Govt.)":
			return "StateGovtAutonomousBody";
		case "Local Authority (Central Govt.)":
			return "CentralGovtLocalAuthority";
		case "Local Authority (State Govt.)":
			return "StateGovtLocalAuthority";
		case "Company":
			return "Company";
		case "Association of Person (AOP)":
			return "AssociationofPerson";
		case "Association of Person (Trust)":
			return "AssociationofPersonTrust";
		case "Artificial Juridical Person":
			return "ArtificialJuridicalPerson";
		case "Body of Individuals":
			return "BodyofIndividuals";
		case "Individual/HUF":
			return "IndividualHUF";
		case "Firm":
			return "Firm";
		case "Branch / Division of Company":
			return "BranchDivisionofCompany";
		}
		return null;
	}

	static String getCalculationPeriod(int calculationPeriod) {
		switch (calculationPeriod) {
		case ClientPayHead.CALCULATION_PERIOD_DAYS:
			return "Days";

		case ClientPayHead.CALCULATION_PERIOD_MONTHS:
			return "Months";

		case ClientPayHead.CALCULATION_PERIOD_WEEKS:
			return "Weeks";

		default:
			return null;
		}
	}

	static String getPerdayCalculationBasis(int perDayCalculationBasis) {
		switch (perDayCalculationBasis) {
		case ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD:
			return "AsPerCalendarPeriod";

		case ClientAttendancePayHead.PER_DAY_CALCULATION_30_DAYS:
			return "Days30";

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED_CALANDAR:
			return "UserDefinedCalendar";

		default:
			return null;
		}
	}

	static String getComputationType(int computationType) {
		AccounterMessages messages = Accounter.getMessages();
		switch (computationType) {
		case ComputionPayHead.COMPUTATE_ON_SUBTOTAL:
			return "OnSubTotal";

		case ComputionPayHead.COMPUTATE_ON_DEDUCTION_TOTAL:
			return "OnDeductionTotal";

		case ComputionPayHead.COMPUTATE_ON_EARNING_TOTAL:
			return "OnEarningTotal";

		case ComputionPayHead.COMPUTATE_ON_SPECIFIED_FORMULA:
			return "OnSpecifiedFormula";

		default:
			break;
		}
		return null;
	}

	static String getAttendanceType(int attendanceType) {
		AccounterMessages messages = Global.get().messages();
		switch (attendanceType) {
		case AttendancePayHead.ATTENDANCE_ON_PAYHEAD:
			return messages.otherPayhead();

		case AttendancePayHead.ATTENDANCE_ON_EARNING_TOTAL:
			return messages.onEarningTotal();

		case AttendancePayHead.ATTENDANCE_ON_SUBTOTAL:
			return messages.onSubTotal();

		case AttendancePayHead.ATTENDANCE_ON_RATE:
			return messages.rate();

		default:
			break;
		}
		return null;
	}

	public static String getPaymentStatusIdentifier(int status) {
		switch (status) {
		case 0:
			return "Not Issued";
		case 1:
			return "Issued";
		}
		return "";
	}

	public static String getQuotationTypeIdentifier(int estimateType) {
		switch (estimateType) {
		case 1:
			return "Quotation";
		case 4:
			return "BillableExpenses";
		}
		return "";
	}

	static String getCalculationType(int type) {
		switch (type) {
		case ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE:
			return "OnAttendance";

		case ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE:
			return "AsComputedValue";

		case ClientPayHead.CALCULATION_TYPE_FLAT_RATE:
			return "FlatRate";

		case ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION:
			return "OnProduction";

		case ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED:
			return "UserDefined";

		default:
			return null;
		}
	}

	static String getComputationSlabType(int type) {
		switch (type) {
		case 1:
			return "Percentage";

		case 2:
			return "Value";
		}
		return null;
	}

	static String getPayHeadFormulaFunctionType(int type) {
		switch (type) {
		case 1:
			return "AddPayHead";
		case 2:
			return "SubtractPayHead";
		case 3:
			return "DivideAttendance";
		case 4:
			return "MultiplyAttendance";
		}
		return null;
	}
}
