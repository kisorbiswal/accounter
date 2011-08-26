package com.vimukti.accounter.core;

import java.io.File;
import java.io.IOException;

import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;

public class MISCInformationTemplate {

	private static final String templateFileName = "templetes" + File.separator
			+ "MISCInformation.html";

	public MISCInformationTemplate() {
	}

	public String generateFile() throws TemplateSyntaxException, IOException {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(templateFileName);

			int i;
			for (i = 0; i < 3; i++) {
				t.addBlock("addRows");
			}

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
