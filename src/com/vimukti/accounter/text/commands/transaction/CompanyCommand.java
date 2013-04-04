package com.vimukti.accounter.text.commands.transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.text.commands.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.CoreUtils;

/**
 * ComanyCommand Class has mainly two functionalities. First it Gets the Company
 * Name from user through email. Secondly it checks the company name is
 * available or not, If Company is not available it prepares a new Company with
 * given Name and it Creates a Default Accounts also.
 * 
 * @author Nagarjuna.j
 * 
 */
public class CompanyCommand extends CreateOrUpdateCommand {

	private String companyName;
	private String countryName;
	private String fiscalYearFirstMonth;
	private int monthInNumber;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Company Name
		companyName = data.nextString("");
		if (companyName == null && companyName.isEmpty()) {
			respnse.addError("Company Name Empty or Null !, Please Enter Company Name");
			return false;
		}

		// countryName
		countryName = data.nextString("");
		if (countryName == null && countryName.isEmpty()
				&& CoreUtils.getCountriesAsList().contains(countryName)) {
			respnse.addError("Country Name Empty or Null !, Please Enter Country Name");
			return false;
		}
		// fiscalYearFirstMonth
		fiscalYearFirstMonth = data.nextString("");
		// Month Number
		monthInNumber = CommandUtils.getMonthByName(fiscalYearFirstMonth);
		if (fiscalYearFirstMonth == null && fiscalYearFirstMonth.isEmpty()
				&& monthInNumber == 0) {
			respnse.addError("Please Enter Fiscal Year First Month");
			return false;
		}

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {

		// getting Company based on Company Name
		// Creating New Company
		if (isCompanyNameExists(companyName)) {
			respnse.addError("Company Name Already Existed");
			return;
		}
		Company company = new Company();

		// Creating Preferences and sets Company Name and Fiscal Year First
		// Month
		CompanyPreferences preferences = new CompanyPreferences();
		preferences.setTradingName(companyName);
		preferences.setFiscalYearFirstMonth(monthInNumber);
		// Setting Preference to Company
		company.setPreferences(preferences);

		// Creating Address
		Address address = new Address();
		address.setCountryOrRegion(countryName);

		// Setting Address to Preferences
		preferences.setTradingAddress(address);

		Session session = HibernateUtil.getCurrentSession();

		session.saveOrUpdate(company);
		// Creating Income Account and Setting Their Values
		Account incomeaAccount = createAccount(Account.TYPE_INCOME, "Income",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company);

		// Creating Expense Account and Setting Their Values
		Account expenseAccount = createAccount(Account.TYPE_EXPENSE, "Expense",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company);

		// Creating Debtors Account and Setting Their Values
		Account debtorAccount = createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
				"Debtors", Account.CASH_FLOW_CATEGORY_OPERATING, true, company);

		// Creating Creditors Account and Setting Their Values
		Account creditorAccount = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "Creditors",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company);

		// Creating Opening Balances Account and Setting Their Values
		Account openingBalancesAccount = createAccount(Account.TYPE_EQUITY,
				"Opening Balances", Account.CASH_FLOW_CATEGORY_OPERATING, true,
				company);

		// Preparing Accounts List
		Set<Account> accounts = new HashSet<Account>();
		accounts.add(incomeaAccount);
		accounts.add(expenseAccount);
		accounts.add(creditorAccount);
		accounts.add(debtorAccount);
		accounts.add(openingBalancesAccount);

		// Setting Accounts to Company
		company.setAccounts(accounts);
		company.setOpeningBalancesAccount(openingBalancesAccount);

		// Saving the Company
		saveOrUpdate(company);

	}

	/**
	 * Creates Account
	 * 
	 * @param type
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * @return
	 */
	private Account createAccount(int type, String name, int cashFlowCategory,
			boolean isDefault, Company company) {

		final HashMap<Integer, Integer> accountNoMap = new HashMap<Integer, Integer>();
		Session session = HibernateUtil.getCurrentSession();
		int subBaseType = Utility.getAccountSubBaseType(type);
		Integer nextAccoutNo = accountNoMap.get(subBaseType);
		Account account = new Account(type, String.valueOf(nextAccoutNo), name,
				cashFlowCategory, isDefault);
		account.setCompany(company);
		session.saveOrUpdate(account);
		accountNoMap.put(subBaseType, nextAccoutNo + 1);
		return account;
	}

	/**
	 * Checking the company name Unique Validation
	 * 
	 * @param companyName
	 * @return
	 * @throws AccounterException
	 */
	public boolean isCompanyNameExists(String companyName)
			throws AccounterException {
		if (companyName == null) {
			return true;
		}
		companyName = companyName.trim().toLowerCase();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Number clientId = (Number) hibernateSession
				.getNamedQuery("getClientByCompany")
				.setParameter("clientEmail", getUserEmailID())
				.setParameter("companyName", companyName).uniqueResult();
		return clientId != null;
	}

}
