/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.TemplateAccount;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccountsTemplateManager {

	public List<AccountsTemplate> loadAccounts(String language)
			throws IOException {

		XStream xStream = new XStream(new DomDriver());

		xStream.alias("industries", List.class);

		xStream.alias("industry", AccountsTemplate.class);
		xStream.useAttributeFor(AccountsTemplate.class, "type");
		xStream.useAttributeFor(AccountsTemplate.class, "name");

		xStream.alias("account", TemplateAccount.class);
		xStream.useAttributeFor(TemplateAccount.class, "type");
		xStream.useAttributeFor(TemplateAccount.class, "name");
		xStream.useAttributeFor(TemplateAccount.class, "defaultValue");
		xStream.useAttributeFor(TemplateAccount.class, "isSystemOnly");
		xStream.useAttributeFor(TemplateAccount.class, "cashFlowType");
		xStream.useAttributeFor(TemplateAccount.class, "countries");

		xStream.aliasField("default", TemplateAccount.class, "defaultValue");
		xStream.aliasField("systemOnly", TemplateAccount.class, "isSystemOnly");

		File file = getFile(language);

		Object object = xStream.fromXML(new FileInputStream(file));

		return (List<AccountsTemplate>) object;

	}

	/**
	 * @param language
	 * @return
	 */
	private File getFile(String language) {
		return new File(ServerConfiguration.getAccountsDir() + "/accounts.xml");
	}
}
