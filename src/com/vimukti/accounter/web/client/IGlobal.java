package com.vimukti.accounter.web.client;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public interface IGlobal {

	public AccounterMessages messages();

	public ClientCompanyPreferences preferences();

	public String Customer();

	public String customer();

	public String Vendor();

	public String vendor();

	public String Location();

	public String toCurrencyFormat(double amount, String currencyCode);

	public String customers();

	public String vendors();

	public String Customers();

	public String Vendors();

	public DateTimeFormatInfo createDateTimeFormatInfo();

}
