package com.vimukti.accounter.servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.MISCInformationTemplate;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class MISCInfoServlet extends BaseServlet {

	public ITemplate template;
	public Converter converter;
	private static final long serialVersionUID = 1L;
	private String outPutString;
	private String fileName;

	public void generateData() throws Exception, IOException,
			AccounterException {

		try {
			converter = new Converter(PD4Constants.LETTER);
			MISCInformationTemplate info = new MISCInformationTemplate();
			outPutString = info.generateFile();
			fileName = info.getFileName();
		} finally {

		}

	}

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response) {

		ServletOutputStream sos = null;
		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}

			generateData();

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			java.io.InputStream inputString = new ByteArrayInputStream(
					outPutString.getBytes());
			InputStreamReader miscCreator = new InputStreamReader(inputString);
			converter.generatePdfDocuments(fileName, sos, miscCreator);

			System.err.println("Converter obj created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		generatePDF(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
