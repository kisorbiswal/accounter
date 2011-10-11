package com.vimukti.accounter.servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.MISCInformationTemplate;
import com.vimukti.accounter.core.Misc1099PDFTemplate;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class MISCInfoServlet extends BaseServlet {

	public ITemplate template;
	public Converter converter;
	private static final long serialVersionUID = 1L;
	private String fileName;
	private int formType;
	private StringBuilder outPutString;
	private String companyID;
	private Session session;
	private FinanceTool financetool;
	private Company company;
	private String objectId;
	private String brandingThemeId;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		ServletOutputStream sos = null;
		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}
			getTempleteObjByRequest(request, companyName);
			// generateData();

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			if (formType == 1) {
				java.io.InputStream inputString = new ByteArrayInputStream(
						outPutString.toString().getBytes());
				InputStreamReader miscCreator = new InputStreamReader(
						inputString);
				converter.generatePdfDocuments(fileName, sos, miscCreator);
			} else if (formType == 0) {

				java.io.InputStream inputString = new ByteArrayInputStream(
						outPutString.toString().getBytes());
				InputStreamReader miscCreator = new InputStreamReader(
						inputString);
				converter.generatePdfDocuments(fileName, sos, miscCreator);
			}

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

	private void getTempleteObjByRequest(HttpServletRequest request,
			String companyName) throws TemplateSyntaxException, IOException,
			AccounterException {

		companyID = getCookie(request, COMPANY_COOKIE);
		session = HibernateUtil.openSession();
		fileName = "";
		outPutString = new StringBuilder();

		financetool = new FinanceTool();
		TemplateBuilder.setCmpName(companyName);

		company = financetool.getCompany(Long.valueOf(companyID));

		CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
				.getClientCompanyPreferences(company));

		objectId = request.getParameter("objectId");

		String multipleId = request.getParameter("multipleIds");
		String[] ids = null;
		if (multipleId != null) {
			ids = multipleId.split(",");
		}
		brandingThemeId = request.getParameter("brandingThemeId");

		formType = Integer.parseInt(request.getParameter("type"));

		int horizontalValue = Integer.parseInt(request
				.getParameter("horizontalValue"));

		int verticalValue = Integer.parseInt(request
				.getParameter("verticalValue"));

		long vendorID = Long.parseLong(request.getParameter("vendorID"));

		if (formType == 1) {

			converter = new Converter(PD4Constants.LETTER);

			Vendor vendor1099 = (Vendor) financetool.getManager()
					.getServerObjectForid(AccounterCoreType.VENDOR, vendorID);

			Misc1099PDFTemplate miscHtmlTemplete = new Misc1099PDFTemplate(
					vendor1099, horizontalValue, verticalValue);
			fileName = miscHtmlTemplete.getFileName();
			outPutString = outPutString.append(miscHtmlTemplete.generatePDF());

		} else if (formType == 0) {

			ArrayList<Client1099Form> miscinfo = financetool.getVendorManager()
					.get1099Vendors(1, company.getID());

			converter = new Converter(PD4Constants.LETTER);
			MISCInformationTemplate info = new MISCInformationTemplate(
					miscinfo, company);
			outPutString = outPutString.append(info.generateFile());
			fileName = info.getFileName();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;
		generatePDF(request, response, companyName);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
