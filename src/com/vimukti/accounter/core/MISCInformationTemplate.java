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
	Double totalPayment;

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

				t.setVariable("box1Value",
						Double.toString(miscVendors.get(i).getBox(1)));
				t.addBlock("changebox1Value");

				t.setVariable("box2Value",
						Double.toString(miscVendors.get(i).getBox(2)));
				t.addBlock("changebox2Value");

				t.setVariable("box3Value",
						Double.toString(miscVendors.get(i).getBox(3)));
				t.addBlock("changebox3Value");

				t.setVariable("box4Value",
						Double.toString(miscVendors.get(i).getBox(4)));
				t.addBlock("changebox4Value");

				t.setVariable("box5Value",
						Double.toString(miscVendors.get(i).getBox(5)));
				t.addBlock("changebox5Value");

				t.setVariable("box6Value",
						Double.toString(miscVendors.get(i).getBox(6)));
				t.addBlock("changebox6Value");

				t.setVariable("box7Value",
						Double.toString(miscVendors.get(i).getBox(7)));
				t.addBlock("changebox7Value");

				t.setVariable("box8Value",
						Double.toString(miscVendors.get(i).getBox(8)));
				t.addBlock("changebox8Value");

				t.setVariable("box9Value",
						Double.toString(miscVendors.get(i).getBox(9)));
				t.addBlock("changebox9Value");

				t.setVariable("box10Value",
						Double.toString(miscVendors.get(i).getBox(10)));
				t.addBlock("changebox10Value");

				t.addBlock("addRows");

				totalPayment = totalPayment
						+ miscVendors.get(i).getTotal1099Payments();
			}

			t.setVariable("noOfForms", miscVendors.size());
			t.addBlock("changenoOfForms");

			t.setVariable("totalPaymment", Double.toString(totalPayment));
			t.addBlock("changetotalPaymment");

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
