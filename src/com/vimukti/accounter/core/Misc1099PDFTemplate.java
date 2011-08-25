package com.vimukti.accounter.core;

import java.io.IOException;

import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;

/**
 * @author photoshop3
 * 
 */
public class Misc1099PDFTemplate {

	private static final String templateFileName = "1099MISCTemplate.html";

	public Misc1099PDFTemplate() {
	}

	public String generatePDF() throws TemplateSyntaxException, IOException {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(templateFileName);

			String payersAddress = "Hyderabad";
			String rents = "20000";
			String royalties = "10000";
			String other_income = "";
			String fedral_incomeTax = "";
			String payer_fedral_identification_number = "";
			String recepent_identification_number = "";
			String fishing_boats_procedds = "";
			String medical_health_payments = "";
			String recepent_name = "";
			String nonemployee_compensation = "";
			String substitute_payment = "";
			String street_adress = "";
			String crop_insurance_Proceeds = "";
			String city_zip = "";
			String account_number = "";
			String golden_parachute_payments = "";
			String gross_paidto_atorney = "";
			String section_409A_deferals = "";
			String section_409A_income = "";
			String state_tax = "";
			String payers_state_no = "";
			String state_income = "";

			t.setVariable("payersAddress", payersAddress);
			t.setVariable("rents", rents);
			t.setVariable("royalties", royalties);
			t.setVariable("other_income", other_income);
			t.setVariable("fedral_incomeTax", fedral_incomeTax);
			t.setVariable("payer_fedral_identification_number",
					payer_fedral_identification_number);
			t.setVariable("recepent_identification_number",
					recepent_identification_number);
			t.setVariable("fishing_boats_procedds", fishing_boats_procedds);
			t.setVariable("medical_health_payments", medical_health_payments);
			t.setVariable("recepent_name", recepent_name);
			t.setVariable("nonemployee_compensation", nonemployee_compensation);
			t.setVariable("substitute_payment", substitute_payment);
			t.setVariable("street_adress", street_adress);
			t.setVariable("crop_insurance_Proceeds", crop_insurance_Proceeds);
			t.setVariable("city_zip", city_zip);
			t.setVariable("account_number", account_number);
			t.setVariable("golden_parachute_payments",
					golden_parachute_payments);
			t.setVariable("gross_paidto_atorney", gross_paidto_atorney);
			t.setVariable("section_409A_deferals", section_409A_deferals);
			t.setVariable("section_409A_income", section_409A_income);

			t.addBlock("theme");

			System.out.println("string......" + t.getFileString());
			outPutString = t.getFileString();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return outPutString;
	}

	public String getFileName() {
		return "MISC1099FORM";
	}

}
