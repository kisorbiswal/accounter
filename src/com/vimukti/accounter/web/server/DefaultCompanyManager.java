package com.vimukti.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.DemoCompany;

public class DefaultCompanyManager {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		XStream xStream = new XStream(new DomDriver());

		xStream = createAlias(xStream);

		File file = getFile();

		Object object = xStream.fromXML(new FileInputStream(file));

	}

	private static XStream createAlias(XStream xStream) {

		xStream.alias("ClientCompany", DemoCompany.class);

		xStream.alias("customers", List.class);
		xStream.alias("Customer", ClientCustomer.class);

		xStream.alias("ClientAddress", ClientAddress.class);
		xStream.alias("ClientContact", ClientContact.class);

		xStream.alias("vendors", List.class);
		xStream.alias("Vendor", ClientVendor.class);

		xStream.alias("accounts", List.class);
		xStream.alias("BankAccount", ClientBankAccount.class);

		xStream.alias("items", List.class);
		xStream.alias("ClientItem", ClientItem.class);

		xStream.alias("quotes", List.class);
		xStream.alias("Quote", ClientEstimate.class);

		xStream.alias("transactionItems", List.class);
		xStream.alias("ClientTransactionItem", ClientTransactionItem.class);

		return xStream;

	}

	private static File getFile() {
		return new File(ServerConfiguration.getDefaultCompanyDir()
				+ "/unitedkingdom.xml");
	}

}
