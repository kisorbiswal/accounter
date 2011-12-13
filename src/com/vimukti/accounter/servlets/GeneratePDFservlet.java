package com.vimukti.accounter.servlets;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

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
import com.vimukti.accounter.core.InvoicePdfGeneration;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class GeneratePDFservlet extends BaseServlet {

	public final static String CLASSIC = "Classic";
	public final static String PROFESSIONAL = "Professional";
	public final static String PLAIN = "Plain";
	public final static String MODERN = "Modern";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PAGE_BREAK = "<pd4ml:page.break>";

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		HashMap map = null;
		ITemplate template = null;
		Converter converter;
		Invoice invoice = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			Session session = HibernateUtil.getCurrentSession();
			StringBuilder outPutString = new StringBuilder();
			transactionType = 0;
			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(companyName);

				Company company = financetool.getCompany(companyID);

				CompanyPreferenceThreadLocal.set(financetool
						.getCompanyManager().getClientCompanyPreferences(
								company));

				String objectId = request.getParameter("objectId");

				String multipleId = request.getParameter("multipleIds");
				String[] ids = null;
				if (multipleId != null) {
					ids = multipleId.split(",");
				}

				// this is used to print multiple pdf documents at a time
				if (multipleId != null) {
					String brandingThemeId = request
							.getParameter("brandingThemeId");
					BrandingTheme brandingTheme = (BrandingTheme) financetool
							.getManager().getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));
					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

					transactionType = Integer.parseInt(request
							.getParameter("type"));

					for (int i = 0; i < ids.length; i++) {

						if (transactionType == Transaction.TYPE_INVOICE) {
							invoice = (Invoice) financetool.getManager()
									.getServerObjectForid(
											AccounterCoreType.INVOICE,
											Long.parseLong(ids[i]));

							printTemplete = getInvoiceReportTemplete(invoice,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}
						if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
							CustomerCreditMemo memo = (CustomerCreditMemo) financetool
									.getManager()
									.getServerObjectForid(
											AccounterCoreType.CUSTOMERCREDITMEMO,
											Long.parseLong(ids[i]));

							printTemplete = getCreditReportTemplete(memo,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}

						outPutString.append(PAGE_BREAK);

					}

				} else if (objectId != null) {

					String brandingThemeId = request
							.getParameter("brandingThemeId");
					BrandingTheme brandingTheme = (BrandingTheme) financetool
							.getManager().getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));
					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

					transactionType = Integer.parseInt(request
							.getParameter("type"));

					// for printing individual pdf documents
					if (transactionType == Transaction.TYPE_INVOICE) {

						invoice = (Invoice) financetool.getManager()
								.getServerObjectForid(
										AccounterCoreType.INVOICE,
										Long.parseLong(objectId));

						printTemplete = getInvoiceReportTemplete(invoice,
								brandingTheme, company);

						fileName = printTemplete.getFileName();

						outPutString = outPutString.append(printTemplete
								.getPdfData());

					} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						// for Credit Note

						CustomerCreditMemo memo = (CustomerCreditMemo) financetool
								.getManager().getServerObjectForid(
										AccounterCoreType.CUSTOMERCREDITMEMO,
										Long.parseLong(objectId));

						printTemplete = getCreditReportTemplete(memo,
								brandingTheme, company);

						fileName = printTemplete.getFileName();

						outPutString = outPutString.append(printTemplete
								.getPdfData());

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

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:

				String output = outPutString.toString().replaceAll("</html>",
						"");
				output = output.toString().replaceAll("<html>", "");

				output = "<html>" + output + "</html>";
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
				converter
						.generatePdfDocuments(printTemplete, sos, creditReader);
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

	public void generateODT2PDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		HashMap map = null;
		ITemplate template = null;
		Converter converter;
		Invoice invoice = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			StringBuilder outPutString = new StringBuilder();
			transactionType = 0;
			FinanceTool financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);

			Company company = financetool.getCompany(companyID);

			CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
					.getClientCompanyPreferences(company));

			String objectId = request.getParameter("objectId");

			String multipleId = request.getParameter("multipleIds");
			String[] ids = null;
			if (multipleId != null) {
				ids = multipleId.split(",");
			}
			String brandingThemeId = request.getParameter("brandingThemeId");
			BrandingTheme brandingTheme = (BrandingTheme) financetool
					.getManager().getServerObjectForid(
							AccounterCoreType.BRANDINGTHEME,
							Long.parseLong(brandingThemeId));
			converter = new Converter(
					getPageSizeType(brandingTheme.getPageSizeType()));
			// this is used to print multiple pdf documents at a time
			if (multipleId != null) {
				transactionType = Integer
						.parseInt(request.getParameter("type"));

				for (int i = 0; i < ids.length; i++) {

					if (transactionType == Transaction.TYPE_INVOICE) {
						invoice = (Invoice) financetool.getManager()
								.getServerObjectForid(
										AccounterCoreType.INVOICE,
										Long.parseLong(ids[i]));

						fileName = "Invoice_" + invoice.getNumber();

						map = Odt2PdfGeneration(invoice, company, brandingTheme);

					}
					if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						CustomerCreditMemo memo = (CustomerCreditMemo) financetool
								.getManager().getServerObjectForid(
										AccounterCoreType.CUSTOMERCREDITMEMO,
										Long.parseLong(ids[i]));

						printTemplete = getCreditReportTemplete(memo,
								brandingTheme, company);

						fileName = printTemplete.getFileName();

						outPutString = outPutString.append(printTemplete
								.getPdfData());

					}

					outPutString.append(PAGE_BREAK);

				}

			} else if (objectId != null) {

				transactionType = Integer
						.parseInt(request.getParameter("type"));

				// for printing individual pdf documents
				if (transactionType == Transaction.TYPE_INVOICE) {

					invoice = (Invoice) financetool.getManager()
							.getServerObjectForid(AccounterCoreType.INVOICE,
									Long.parseLong(objectId));
					fileName = "Invoice_" + invoice.getNumber();
					map = Odt2PdfGeneration(invoice, company, brandingTheme);

				} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
					// for Credit Note

					CustomerCreditMemo memo = (CustomerCreditMemo) financetool
							.getManager().getServerObjectForid(
									AccounterCoreType.CUSTOMERCREDITMEMO,
									Long.parseLong(objectId));

					printTemplete = getCreditReportTemplete(memo,
							brandingTheme, company);

					fileName = printTemplete.getFileName();

					outPutString = outPutString.append(printTemplete
							.getPdfData());

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

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:

				IContext context = (IContext) map.get("context");
				IXDocReport report = (IXDocReport) map.get("report");

				Options options = Options.getTo(ConverterTypeTo.PDF).via(
						ConverterTypeVia.ITEXT);
				report.convert(context, options, sos);

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
				converter
						.generatePdfDocuments(printTemplete, sos, creditReader);
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
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, company);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, status,
					company);
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
		// for generating reports using html templates
		generatePDF(request, response, companyName);

		// TODO for genearting reports using XdocReport
		// generateODT2PDF(request, response, companyName);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private PrintTemplete getInvoiceReportTemplete(Invoice invoice,
			BrandingTheme theme, Company company) {

		String invStyle = theme.getInvoiceTempleteName();

		if (invStyle.contains(CLASSIC)) {
			return new InvoicePDFTemplete(invoice, theme, company,
					"ClassicInvoice");

		} else if (invStyle.contains(PLAIN)) {
			return new InvoicePDFTemplete(invoice, theme, company,
					"PlainInvoice");
		} else if (invStyle.contains(PROFESSIONAL)) {
			return new InvoicePDFTemplete(invoice, theme, company,
					"ProfessionalInvoice");

		} else if (invStyle.contains(MODERN)) {
			return new InvoicePDFTemplete(invoice, theme, company,
					"ModernInvoice");
		}
		return null;
	}

	private PrintTemplete getCreditReportTemplete(CustomerCreditMemo memo,
			BrandingTheme theme, Company company) {

		String invStyle = theme.getCreditNoteTempleteName();

		if (invStyle.contains(CLASSIC)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ClassicCredit");

		} else if (invStyle.contains(PLAIN)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"PlainCredit");
		} else if (invStyle.contains(PROFESSIONAL)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ProfessionalCredit");
		} else if (invStyle.contains(MODERN)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ModernCredit");
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap Odt2PdfGeneration(Invoice invoice, Company company,
			BrandingTheme brandingTheme) {
		try {
			String templeteName = "";
			String invStyle = brandingTheme.getInvoiceTempleteName();
			InvoicePdfGeneration pdfGeneration = null;
			if (invStyle.contains(CLASSIC)) {
				templeteName = "templetes" + File.separator
						+ "classicinvoiceodt" + ".odt";

			} else if (invStyle.contains(PLAIN)) {
				templeteName = "templetes" + File.separator + "plainInvoiceodt"
						+ ".odt";

			} else if (invStyle.contains(PROFESSIONAL)) {
				templeteName = "templetes" + File.separator
						+ "professionalinvoiceodt" + ".odt";

			} else if (invStyle.contains(MODERN)) {
				templeteName = "templetes" + File.separator
						+ "modernInvoiceodt" + ".odt";

			}
			pdfGeneration = new InvoicePdfGeneration(invoice, company,
					brandingTheme);
			InputStream in = new BufferedInputStream(new FileInputStream(
					templeteName));
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);

			IContext context = report.createContext();

			context = pdfGeneration.assignValues(context, report);

			HashMap objects = new HashMap();
			objects.put("context", context);
			objects.put("report", report);

			return objects;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
