package com.vimukti.accounter.migration;

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
		case 1:
			return "ServiceItem";
		case 2:
			return "NonInventory";
		case 3:
			return "InventoryPart";
		case 4:
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
}
