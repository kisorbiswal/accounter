package com.vimukti.accounter.servlets;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditNotePDFTemplete;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.Misc1099PDFTemplate;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class GeneratePDFservlet extends BaseServlet {
	public ITemplate template;
	public Converter converter;

	private StringBuilder outPutString;
	private String fileName;
	private int transactionType;
	private PrintTemplete printTemplete;

	public final static String CLASSIC = "Classic";
	public final static String PROFESSIONAL = "Professional";
	public final static String PLAIN = "Plain";
	public final static String MODERN = "Modern";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		ServletOutputStream sos = null;
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			getTempleteObjByRequest(request, footerImg, style, companyName);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:
				String output = outPutString.toString().replaceAll(
						"</html><html>", "");

				java.io.InputStream inputStream = new ByteArrayInputStream(
						output.getBytes());
				InputStreamReader reader = new InputStreamReader(inputStream);
				converter.generatePdfDocuments(printTemplete, sos, reader);
				break;
			// for credit note
			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
				String creditOutput = outPutString.toString().replaceAll(
						"</html>", "");
				creditOutput = creditOutput.toString().replaceAll("<html>", "");

				creditOutput = "<html>" + creditOutput + "</html>";

				java.io.InputStream inputStr = new ByteArrayInputStream(
						creditOutput.toString().getBytes());
				InputStreamReader creditReader = new InputStreamReader(inputStr);
				converter.generatePdfDocuments(printTemplete, sos, creditReader);
				break;
			case Transaction.TYPE_MISC_FORM:
				java.io.InputStream inputString = new ByteArrayInputStream(
						outPutString.toString().getBytes());
				InputStreamReader miscCreator = new InputStreamReader(
						inputString);
				converter.generatePdfDocuments(fileName, sos, miscCreator);
				break;
			default:
				// for generating pdf document for reports
				converter.generatePdfReports(template, sos);
				break;
			}

			System.err.println("Pdf created");

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
			String footerImg, String style, String companyName)
			throws Exception, IOException, AccounterException {
		String companyID = getCookie(request, COMPANY_COOKIE);
		Session session = HibernateUtil.openSession();
		fileName = "";
		outPutString = new StringBuilder();
		transactionType = 0;
		try {
			FinanceTool financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);

			Company company = financetool.getCompany(Long.valueOf(companyID));

			CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
					.getClientCompanyPreferences(company));

			String objectId = request.getParameter("objectId");

			String multipleId = request.getParameter("multipleIds");
			String[] ids = null;
			if (multipleId != null) {
				ids = multipleId.split(",");
			}
			String brandingThemeId = request.getParameter("brandingThemeId");

			// this is used to print multiple pdf documents at a time
			if (multipleId != null) {
				transactionType = Integer
						.parseInt(request.getParameter("type"));
				BrandingTheme brandingTheme = (BrandingTheme) financetool
						.getManager().getServerObjectForid(
								AccounterCoreType.BRANDINGTHEME,
								Long.parseLong(brandingThemeId));
				converter = new Converter(
						getPageSizeType(brandingTheme.getPageSizeType()));

				for (int i = 0; i < ids.length; i++) {

					if (transactionType == Transaction.TYPE_INVOICE) {
						Invoice invoice = (Invoice) financetool.getManager()
								.getServerObjectForid(
										AccounterCoreType.INVOICE,
										Long.parseLong(ids[i]));

						// template = new InvoiceTemplete(invoice,
						// brandingTheme, footerImg, style);

						printTemplete = getInvoiceReportTemplete(invoice,
								brandingTheme, company, companyID);

						fileName = printTemplete.getFileName();

						outPutString = outPutString.append(printTemplete
								.getPdfData());

					}
					if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						CustomerCreditMemo memo = (CustomerCreditMemo) financetool
								.getManager().getServerObjectForid(
										AccounterCoreType.CUSTOMERCREDITMEMO,
										Long.parseLong(ids[i]));

						printTemplete = getCreditReportTemplete(memo,
								brandingTheme, company, companyID);

						fileName = printTemplete.getFileName();

						outPutString = outPutString.append(printTemplete
								.getPdfData());

					}

				}

			} else if (objectId != null) {
				transactionType = Integer
						.parseInt(request.getParameter("type"));
				BrandingTheme brandingTheme = (BrandingTheme) financetool
						.getManager().getServerObjectForid(
								AccounterCoreType.BRANDINGTHEME,
								Long.parseLong(brandingThemeId));

				// for printing individual pdf documents
				if (transactionType == Transaction.TYPE_INVOICE) {

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

					Invoice invoice = (Invoice) financetool.getManager()
							.getServerObjectForid(AccounterCoreType.INVOICE,
									Long.parseLong(objectId));

					printTemplete = getInvoiceReportTemplete(invoice,
							brandingTheme, company, companyID);

					fileName = printTemplete.getFileName();

					outPutString = outPutString.append(printTemplete
							.getPdfData());

				} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
					// for Credit Note

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

					CustomerCreditMemo memo = (CustomerCreditMemo) financetool
							.getManager().getServerObjectForid(
									AccounterCoreType.CUSTOMERCREDITMEMO,
									Long.parseLong(objectId));

					printTemplete = getCreditReportTemplete(memo,
							brandingTheme, company, companyID);

					fileName = printTemplete.getFileName();

					outPutString = outPutString.append(printTemplete
							.getPdfData());

				}// for MISC form
				else if (transactionType == Transaction.TYPE_MISC_FORM) {
					converter = new Converter(PD4Constants.LETTER);

					long vendorID = Long.parseLong(request
							.getParameter("vendorID"));

					int horizontalValue = Integer.parseInt(request
							.getParameter("horizontalValue"));

					int verticalValue = Integer.parseInt(request
							.getParameter("verticalValue"));

					Vendor memo = (Vendor) financetool.getManager()
							.getServerObjectForid(AccounterCoreType.VENDOR,
									vendorID);

					Misc1099PDFTemplate miscHtmlTemplete = new Misc1099PDFTemplate(
							memo, horizontalValue, verticalValue);
					fileName = miscHtmlTemplete.getFileName();
					outPutString = outPutString.append(miscHtmlTemplete
							.generatePDF());
				}
			}
			// for Reports
			else {
				transactionType = 0;
				converter = new Converter();
				template = getReportTemplate(company, request, financetool,
						footerImg, style);
				fileName = template.getFileName();
			}

		} finally {
			session.close();
		}

	}

	private ITemplate getReportTemplate(Company company,
			HttpServletRequest request, FinanceTool financeTool,
			String footerImg, String style) throws IOException {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		ReportsGenerator generator;

		if (status == null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF,
					company.getAccountingType(), company);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, status,
					company.getAccountingType(), company);
		}

		String gridTemplate = generator.generate(financeTool,
				ReportsGenerator.GENERATIONTYPEPDF);

		ReportTemplate template = new ReportTemplate(company, reportType,
				new String[] { gridTemplate, footerImg, style, dateRangeHtml });
		return template;
	}

	private Dimension getPageSizeType(int pageSizeType) {
		switch (pageSizeType) {
		case 2:
			return PD4Constants.LETTER;
		default:
			return PD4Constants.A4;
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

	private PrintTemplete getInvoiceReportTemplete(Invoice invoice,
			BrandingTheme theme, Company company, String companyID) {

		String invStyle = theme.getInvoiceTempleteName();

		if (invStyle.contains(CLASSIC)) {
			printTemplete = new InvoicePDFTemplete(invoice, theme, company,
					companyID, "ClassicInvoice");

		} else if (invStyle.contains(PLAIN)) {
			printTemplete = new InvoicePDFTemplete(invoice, theme, company,
					companyID, "PlainInvoice");
		} else if (invStyle.contains(PROFESSIONAL)) {
			printTemplete = new InvoicePDFTemplete(invoice, theme, company,
					companyID, "ProfessionalInvoice");

		} else if (invStyle.contains(MODERN)) {
			printTemplete = new InvoicePDFTemplete(invoice, theme, company,
					companyID, "ModernInvoice");
		}
		return printTemplete;
	}

	private PrintTemplete getCreditReportTemplete(CustomerCreditMemo memo,
			BrandingTheme theme, Company company, String companyID) {

		String invStyle = theme.getCreditNoteTempleteName();

		if (invStyle.contains(CLASSIC)) {
			printTemplete = new CreditNotePDFTemplete(memo, theme, company,
					companyID,"ClassicCredit");

		} else if (invStyle.contains(PLAIN)) {
			printTemplete = new CreditNotePDFTemplete(memo, theme,
					company, companyID,"PlainCredit");
		} else if (invStyle.contains(PROFESSIONAL)) {
			printTemplete = new CreditNotePDFTemplete(memo, theme, company,
					companyID,"ProfessionalCredit");
		} else if (invStyle.contains(MODERN)) {
			printTemplete = new CreditNotePDFTemplete(memo, theme, company,
					companyID,"ModernCredit");
		}
		return printTemplete;
	}

}
