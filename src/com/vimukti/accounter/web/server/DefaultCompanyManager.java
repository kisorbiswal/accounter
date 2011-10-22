package com.vimukti.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.core.ClientCompany;

public class DefaultCompanyManager {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Default company manager");
		XStream xStream = new XStream(new DomDriver());

		xStream.alias("DefaultCompany", ClientCompany.class);

		File file = getFile();

		Object object = xStream.fromXML(new FileInputStream(file));

	}

	private static File getFile() {
		return new File(ServerConfiguration.getDefaultCompanyDir()
				+ "/unitedkingdom.xml");
	}

}
