package com.vimukti.accounter.mobile.utils;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;

public class CommandUtils {

	public static Vendor getVendorByName(String vendorName) {
		// TODO Auto-generated method stub
		return null;
	}

	public static IAccounterCore getClientObjectById(long id,
			AccounterCoreType taxitem) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ClientItemGroup getItemGroupByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ClientFinanceDate getCurrentFiscalYearStartDate(
			ClientCompanyPreferences preferences) {
		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);
		return startDate;
	}

	public static ClientFinanceDate getCurrentFiscalYearEndDate(
			ClientCompanyPreferences preferences) {

		ClientFinanceDate startDate = new ClientFinanceDate();
		startDate.setMonth(preferences.getFiscalYearFirstMonth() + 1);
		startDate.setDay(1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());
		return endDate;
	}

	public static ClientAccount getAccountByName(Company company, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ClientAccount getAccountByNumber(Company company,
			long numberFromString) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<String> getPaymentMethods() {
		List<String> paymentMethods = new ArrayList<String>();
		paymentMethods.add(Accounter.constants().cash());
		paymentMethods.add(Accounter.constants().check());
		paymentMethods.add(Accounter.constants().creditCard());
		return paymentMethods;
	}

	public static Object getServerObjectById(long account,
			AccounterCoreType account2) {
		// TODO Auto-generated method stub
		return null;
	}
}
