package com.vimukti.accounter.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.vimukti.accounter.web.client.core.Client1099Form;

public class MISCInformationTemplate {

	ArrayList<Client1099Form> miscVendors;
	Company myCompany;

	private static final String templateFileName = "templetes" + File.separator
			+ "MISCInformation.html";

	public MISCInformationTemplate(ArrayList<Client1099Form> miscinfo,
			Company company) {
		miscVendors = miscinfo;
		myCompany = company;
	}

	public String generateFile() throws TemplateSyntaxException, IOException {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(templateFileName);

			t.setVariable("companyName", myCompany.getFullName());
			t.addBlock("changeCompanyName");

			t.setVariable("companyAddress1",
					myCompany.getTradingAddress().street);
			t.addBlock("changeCompanyAddress1");

			t.setVariable("companyAddress2", myCompany.getTradingAddress().city);
			t.addBlock("changeCompanyAddress2");

			t.setVariable("companyAddress3",
					myCompany.getTradingAddress().countryOrRegion);
			t.addBlock("changeCompanyAddress3");

			t.setVariable("companyAddress4",
					myCompany.getTradingAddress().zipOrPostalCode);
			t.addBlock("changeCompanyAddress4");

			t.setVariable("companyId", Long.toString(myCompany.getID()));
			t.addBlock("changeCompanyId");

			int i;
			for (i = 0; i < miscVendors.size(); i++) {

				t.setVariable("vendorName", miscVendors.get(i).getVendor()
						.getName());
				t.addBlock("changeVendorName");

				t.setVariable("VendorcompanyAddress1", miscVendors.get(i)
						.getVendor().getAddress().toString());
				t.addBlock("changeVendorCompanyAddress1");

				t.setVariable("VendorCompanyId",
						Long.toString(miscVendors.get(i).getVendor().getID()));
				t.addBlock("changeVendorCompanyId");

				t.addBlock("addRows");
			}

			t.setVariable("noOfForms", miscVendors.size());
			t.addBlock("changenoOfForms");

			t.addBlock("theme");

			System.out.println("string......" + t.getFileString());
			outPutString = t.getFileString();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return outPutString;
	}

	public String getFileName() {
		return "1099-InformationSheet";
	}

}
