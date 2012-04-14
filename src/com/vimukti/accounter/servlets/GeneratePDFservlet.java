package com.vimukti.accounter.servlets;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.CCExpensePdfGeneration;
import com.vimukti.accounter.core.CashExpensePdfGeneration;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashPurchasePdfGeneration;
import com.vimukti.accounter.core.CashSalePdfGeneration;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditNotePDFTemplete;
import com.vimukti.accounter.core.CreditNotePdfGeneration;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerPaymentPdfGeneration;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.EnterBillPdfGeneration;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.InvoicePdfGeneration;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.JournelEntryPdfGeneration;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayBillPdfGeneration;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.PurchaseOrderPdfGeneration;
import com.vimukti.accounter.core.QuotePdfGeneration;
import com.vimukti.accounter.core.QuotePdfTemplate;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceivePaymentPdfGeneration;
import com.vimukti.accounter.core.RefundPdfGeneration;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.SalesOrderPdfGeneration;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorCreditPdfGeneration;
import com.vimukti.accounter.core.VendorPayment;
import com.vimukti.accounter.core.VendorPaymentPdfGeneration;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.core.WriteCheckPdfGeneration;
import com.vimukti.accounter.core.reports.generators.IReportGenerator;
import com.vimukti.accounter.core.vat.IndianVATTemplate;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.countries.India;
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

	// @Override
	// protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
	// throws ServletException, IOException {
	// super.service(arg0, arg1);
	// }

	/**
	 * to generate pdf using HTML files
	 * 
	 * @param request
	 * @param response
	 * @param companyName
	 */
	public void generateHtmlPDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		ITemplate template = null;
		Converter converter = null;
		BrandingTheme brandingTheme = null;
		Invoice invoice = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		boolean pageBreak = false;
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);

			StringBuilder outPutString = new StringBuilder();
			transactionType = 0;
			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(companyName);

				Company company = financetool.getCompany(companyID);

				CompanyPreferenceThreadLocal.set(financetool
						.getCompanyManager().getClientCompanyPreferences(
								company));

				String brandingThemeId = request
						.getParameter("brandingThemeId");
				if (brandingThemeId != null) {
					brandingTheme = (BrandingTheme) financetool.getManager()
							.getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));
				}
				String objectId = request.getParameter("objectId");
				if (request.getParameter("multipleIds") != null) {
					objectId = request.getParameter("multipleIds");
				}

				String[] ids = null;
				if (objectId != null) {
					ids = objectId.split(",");
					if (ids.length > 1) {
						pageBreak = true;

					}
				}

				// this is used to print multiple pdf documents at a time
				if (objectId != null) {

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

						if (transactionType == Transaction.TYPE_ESTIMATE) {
							Estimate estimate = (Estimate) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.ESTIMATE,
											Long.parseLong(ids[i]));

							printTemplete = getQuoteReportTemplete(estimate,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}

						if (pageBreak) {
							outPutString.append(PAGE_BREAK);
						}
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

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// session.close();
			}
			FontFactory.setFontImp(new FontFactoryImpEx());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:
			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			case Transaction.TYPE_CASH_SALES:
			case Transaction.TYPE_ESTIMATE:

				String output = outPutString.toString().replaceAll("</html>",
						"");
				output = output.toString().replaceAll("<html>", "");

				output = "<html>" + output + "</html>";
				java.io.InputStream inputStream = new ByteArrayInputStream(
						output.getBytes("UTF-8"));

				InputStreamReader reader = new InputStreamReader(inputStream,
						Charset.forName("UTF-8"));
				converter.generatePdfDocuments(printTemplete, sos, reader);

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

	/**
	 * to generate custom pdf template using odt & docx
	 * 
	 * @param request
	 * @param response
	 * @param companyName
	 */
	public void generateCustom2PDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		HashMap map = null;
		ITemplate template = null;
		Converter converter = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		boolean isMultipleId = false;
		List<String> fileNames = new ArrayList<String>();
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			transactionType = 0;
			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(companyName);

				Company company = financetool.getCompany(companyID);

				CompanyPreferenceThreadLocal.set(financetool
						.getCompanyManager().getClientCompanyPreferences(
								company));

				String objectId = request.getParameter("objectId");
				if (request.getParameter("multipleIds") != null) {
					isMultipleId = true;
					objectId = request.getParameter("multipleIds");
				}

				String[] ids = null;
				if (objectId != null) {
					ids = objectId.split(",");
				}
				String brandingThemeId = request
						.getParameter("brandingThemeId");
				BrandingTheme brandingTheme = null;
				if (brandingThemeId != null) {
					brandingTheme = (BrandingTheme) financetool.getManager()
							.getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

				}

				// this is used to print multiple pdf documents at a time
				if (objectId != null) {
					transactionType = Integer.parseInt(request
							.getParameter("type"));

					for (int i = 0; i < ids.length; i++) {

						if (transactionType == Transaction.TYPE_INVOICE) {
							Invoice invoice = (Invoice) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.INVOICE,
											Long.parseLong(ids[i]));

							fileName = "Invoice_" + invoice.getNumber();

							map = Odt2PdfGeneration(invoice, company,
									brandingTheme, isMultipleId, fileNames);

						}
						if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
							CustomerCreditMemo memo = (CustomerCreditMemo) financetool
									.getManager()
									.getServerObjectForid(
											AccounterCoreType.CUSTOMERCREDITMEMO,
											Long.parseLong(ids[i]));
							fileName = "CreditNote" + memo.getNumber();
							map = Odt2PdfGeneration(memo, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_ESTIMATE) {
							Estimate estimate = (Estimate) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.ESTIMATE,
											Long.parseLong(ids[i]));

							if (estimate.getEstimateType() == Estimate.SALES_ORDER) {
								fileName = "SalesOrder_" + estimate.getNumber();
								map = Odt2PdfGeneration(estimate, company,
										brandingTheme, isMultipleId, fileNames);

							} else if (estimate.getEstimateType() == Estimate.QUOTES) {
								fileName = "Quote" + estimate.getNumber();
								map = Odt2PdfGeneration(estimate, company,
										brandingTheme, isMultipleId, fileNames);
							}
						}
						if (transactionType == Transaction.TYPE_CASH_SALES) {
							CashSales cashSales = (CashSales) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.CASHSALES,
											Long.parseLong(ids[i]));
							fileName = "Cash Sale" + cashSales.getNumber();
							map = Odt2PdfGeneration(cashSales, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_RECEIVE_PAYMENT) {
							ReceivePayment receivepayment = (ReceivePayment) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.RECEIVEPAYMENT,
											Long.parseLong(ids[i]));
							fileName = "Receive Payment"
									+ receivepayment.getNumber();
							map = Odt2PdfGeneration(receivepayment, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_PURCHASE_ORDER) {
							PurchaseOrder order = (PurchaseOrder) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.PURCHASEORDER,
											Long.parseLong(ids[i]));

							fileName = "PurchaseOrder_" + order.getNumber();

							map = Odt2PdfGeneration(order, company,
									brandingTheme, isMultipleId, fileNames);

						}
						if (transactionType == Transaction.TYPE_JOURNAL_ENTRY) {
							JournalEntry journalEntry = (JournalEntry) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.JOURNALENTRY,
											Long.parseLong(ids[i]));
							fileName = "JournalEntry_"
									+ journalEntry.getNumber();
							map = Odt2PdfGeneration(journalEntry, company,
									brandingTheme, isMultipleId, fileNames);

						}
						if (transactionType == Transaction.TYPE_CUSTOMER_REFUNDS) {
							CustomerRefund customerRefund = (CustomerRefund) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.CUSTOMERREFUND,
											Long.parseLong(ids[i]));
							fileName = "Refund_" + customerRefund.getNumber();
							map = Odt2PdfGeneration(customerRefund, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_CASH_PURCHASE) {
							CashPurchase purchase = (CashPurchase) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.CASHPURCHASE,
											Long.parseLong(ids[i]));
							fileName = "CashPurcahse_" + purchase.getNumber();
							map = Odt2PdfGeneration(purchase, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_CUSTOMER_PRE_PAYMENT) {
							CustomerPrePayment prePayment = (CustomerPrePayment) financetool
									.getManager()
									.getServerObjectForid(
											AccounterCoreType.CUSTOMERPREPAYMENT,
											Long.parseLong(ids[i]));
							fileName = "Prepayment_" + prePayment.getNumber();
							map = Odt2PdfGeneration(prePayment, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_CREDIT_CARD_EXPENSE) {
							CreditCardCharge charge = (CreditCardCharge) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.CREDITCARDCHARGE,
											Long.parseLong(ids[i]));
							fileName = "CCExpense_" + charge.getNumber();
							map = Odt2PdfGeneration(charge, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_CASH_EXPENSE) {
							CashPurchase purchase = (CashPurchase) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.CASHPURCHASE,
											Long.parseLong(ids[i]));
							fileName = "CashExpense_" + purchase.getNumber();
							map = Odt2PdfGeneration(purchase, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
							VendorCreditMemo vendorCreditMemo = (VendorCreditMemo) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.VENDORCREDITMEMO,
											Long.parseLong(ids[i]));
							fileName = "CreditMemo_"
									+ vendorCreditMemo.getNumber();
							map = Odt2PdfGeneration(vendorCreditMemo, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_WRITE_CHECK) {
							WriteCheck writeCheck = (WriteCheck) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.WRITECHECK,
											Long.parseLong(ids[i]));
							fileName = "WriteCheck_" + writeCheck.getNumber();
							map = Odt2PdfGeneration(writeCheck, company,
									brandingTheme, isMultipleId, fileNames);
						}

						if (transactionType == Transaction.TYPE_VENDOR_PAYMENT) {
							VendorPayment vendorPayment = (VendorPayment) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.VENDORPAYMENT,
											Long.parseLong(ids[i]));
							fileName = "Payment" + vendorPayment.getNumber();
							map = Odt2PdfGeneration(vendorPayment, company,
									brandingTheme, isMultipleId, fileNames);
						}

						if (transactionType == Transaction.TYPE_ENTER_BILL) {
							EnterBill enterBill = (EnterBill) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.ENTERBILL,
											Long.parseLong(ids[i]));
							fileName = "EnterBill" + enterBill.getNumber();
							map = Odt2PdfGeneration(enterBill, company,
									brandingTheme, isMultipleId, fileNames);
						}

						if (transactionType == Transaction.TYPE_PAY_BILL) {
							PayBill payBill = (PayBill) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.PAYBILL,
											Long.parseLong(ids[i]));
							fileName = "PayBill_" + payBill.getNumber();
							map = Odt2PdfGeneration(payBill, company,
									brandingTheme, isMultipleId, fileNames);
						}

					}

				}

				// for Reports
				else {
					transactionType = 0;
					converter = new Converter();
					int reportType = Integer.parseInt(request
							.getParameter("reportType"));
					if (reportType == 174) {
						reportType = 165;
					}
					if (reportType == 174
							&& company.getCountryPreferences() instanceof India) {
						String status = request.getParameter("status");
						long startDate = Long.parseLong(request
								.getParameter("startDate"));
						long endDate = Long.parseLong(request
								.getParameter("endDate"));

						IndianVATTemplate indianVATTemplate = new IndianVATTemplate(
								company, Long.parseLong(status), startDate,
								endDate);

						response.setContentType("application/pdf");
						response.setHeader("Content-disposition",
								"attachment; filename="
										+ indianVATTemplate.getFileName()
												.replace(" ", "") + ".pdf");
						sos = response.getOutputStream();

						indianVATTemplate.writeResponse(sos);
						return;
					} else {
						template = getReportTemplate(company, request,
								financetool, footerImg, style);
					}
					fileName = template.getFileName();
				}

				response.setContentType("application/pdf");
				response.setHeader("Content-disposition",
						"attachment; filename=" + fileName.replace(" ", "")
								+ ".pdf");
				sos = response.getOutputStream();

				switch (transactionType) {
				// for invoice or CreditNote
				case Transaction.TYPE_INVOICE:
				case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
				case Transaction.TYPE_ESTIMATE:
				case Transaction.TYPE_CASH_SALES:
				case Transaction.TYPE_RECEIVE_PAYMENT:
				case Transaction.TYPE_PURCHASE_ORDER:
				case Transaction.TYPE_JOURNAL_ENTRY:
				case Transaction.TYPE_CUSTOMER_REFUNDS:
				case Transaction.TYPE_CASH_PURCHASE:
				case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
				case Transaction.TYPE_CREDIT_CARD_EXPENSE:
				case Transaction.TYPE_CASH_EXPENSE:
				case Transaction.TYPE_VENDOR_CREDIT_MEMO:
				case Transaction.TYPE_WRITE_CHECK:
				case Transaction.TYPE_VENDOR_PAYMENT:
				case Transaction.TYPE_ENTER_BILL:
				case Transaction.TYPE_PAY_BILL:

					if (isMultipleId) {// for merging multiple custom pdf
										// documents
						mergePDFDocuments(fileNames, sos, true);
					} else {
						IContext context = (IContext) map.get("context");
						IXDocReport report = (IXDocReport) map.get("report");

						Options options = Options.getTo(ConverterTypeTo.PDF)
								.via(ConverterTypeVia.ITEXT);
						report.convert(context, options, sos);
					}
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
			}
		} finally {
			try {
				if (sos != null) {
					sos.close();
				}
			} catch (Exception e) {
			}
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
					navigatedName, IReportGenerator.GENERATION_TYPE_PDF,
					company, dateRangeHtml);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, IReportGenerator.GENERATION_TYPE_PDF,
					status, company, dateRangeHtml);
		}

		String gridTemplate = generator.generate(financeTool,
				IReportGenerator.GENERATION_TYPE_PDF);
		String reportName = generator.getReportNameByType(reportType);
		ReportTemplate template = new ReportTemplate(company, reportType,
				new String[] { gridTemplate, footerImg, style, dateRangeHtml },
				reportName);
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
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();
			String companyName = getCompanyName(request);
			BrandingTheme brandingTheme = null;
			if (companyName == null)
				return;

			String brandingThemeId = request.getParameter("brandingThemeId");

			String typeStr = request.getParameter("type");
			int type = 0;
			if (typeStr != null) {
				type = Integer.valueOf(typeStr);
			}

			FinanceTool financetool = new FinanceTool();
			if (brandingThemeId != null) {
				// If branding theme is valid, then check for isCustomFile.If
				// isCustomFile is true , then call generateODT2PDF and if
				// false,
				// then call generateHtmlPDF
				brandingTheme = (BrandingTheme) financetool.getManager()
						.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
								Long.parseLong(brandingThemeId));

				if (brandingTheme.isCustomFile()) {
					// for genearting reports using XdocReport
					generateCustom2PDF(request, response, companyName);
				} else {
					if (type == Transaction.TYPE_CASH_SALES
							|| type == Transaction.TYPE_PURCHASE_ORDER
							|| type == Transaction.TYPE_JOURNAL_ENTRY) {
						generateCustom2PDF(request, response, companyName);
					} else {
						generateHtmlPDF(request, response, companyName);
					}
				}

			} else {
				// for all kinds of other reports
				generateCustom2PDF(request, response, companyName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private PrintTemplete getInvoiceReportTemplete(Invoice invoice,
			BrandingTheme theme, Company company) {

		try {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
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

	private PrintTemplete getQuoteReportTemplete(Estimate estimate,
			BrandingTheme theme, Company company) {

		String invStyle = theme.getQuoteTemplateName();

		if (invStyle.contains(CLASSIC)) {
			return new QuotePdfTemplate(estimate, theme, company,
					"ClassicQuote");
		} else if (invStyle.contains(PLAIN)) {
			return new QuotePdfTemplate(estimate, theme, company, "PlainQuote");
		} else if (invStyle.contains(PROFESSIONAL)) {
			return new QuotePdfTemplate(estimate, theme, company,
					"ProfessionalQuote");
		} else if (invStyle.contains(MODERN)) {
			return new QuotePdfTemplate(estimate, theme, company, "ModernQuote");
		}
		return null;
	}

	private HashMap Odt2PdfGeneration(Transaction transaction, Company company,
			BrandingTheme brandingTheme, boolean multipleIds,
			List<String> fileNames) {

		try {

			CreditNotePdfGeneration creditPdfGeneration = null;
			InvoicePdfGeneration invoicePdfGeneration = null;
			QuotePdfGeneration quotePdfGeneration = null;
			CashSalePdfGeneration cashSalePdfGeneration = null;
			ReceivePaymentPdfGeneration receivePaymentPdfGeneration = null;
			RefundPdfGeneration refundPdfGeneration = null;
			PurchaseOrderPdfGeneration purchaseOrderPdfGeneration = null;
			SalesOrderPdfGeneration salesOrderPdfGeneration = null;
			JournelEntryPdfGeneration journelEntryPdfGeneration = null;
			CashPurchasePdfGeneration purchasePdfGeneration = null;
			CustomerPaymentPdfGeneration customerPaymentPdfGeneration = null;
			CCExpensePdfGeneration ccExpensePdfGeneration = null;
			CashExpensePdfGeneration cashExpensePdfGeneration = null;
			VendorCreditPdfGeneration vendorCreditPdfGeneration = null;
			WriteCheckPdfGeneration writeCheckPdfGeneration = null;
			VendorPaymentPdfGeneration vendorPaymentPdfGeneration = null;
			EnterBillPdfGeneration enterBillPdfGeneration = null;
			PayBillPdfGeneration payBillPdfGeneration = null;

			String templeteName = null;
			String fileName = null;

			if (transaction instanceof Invoice) {
				// for Invoice
				if (brandingTheme.getInvoiceTempleteName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "InvoiceDocx.docx";
				} else {

					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getInvoiceTempleteName();
				}
				fileName = "Invoice_" + transaction.getNumber();
				invoicePdfGeneration = new InvoicePdfGeneration(
						(Invoice) transaction, company, brandingTheme);
			}

			if (transaction instanceof CustomerCreditMemo) {
				// For CreditNote
				if (brandingTheme.getCreditNoteTempleteName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "CreditDocx.docx";
				} else {
					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getCreditNoteTempleteName();
				}
				fileName = "CreditNote_" + transaction.getNumber();
				creditPdfGeneration = new CreditNotePdfGeneration(
						(CustomerCreditMemo) transaction, company,
						brandingTheme);
			}

			if (transaction instanceof ReceivePayment) {
				templeteName = "templetes" + File.separator
						+ "ReceivePaymentOdt.odt";
				fileName = "ReceivePayment_" + transaction.getNumber();
				receivePaymentPdfGeneration = new ReceivePaymentPdfGeneration(
						(ReceivePayment) transaction, company);
			}

			if (transaction instanceof CustomerRefund) {
				templeteName = "templetes" + File.separator + "RefundOdt.odt";
				fileName = "Refund_" + transaction.getNumber();
				refundPdfGeneration = new RefundPdfGeneration(
						(CustomerRefund) transaction, company);
			}

			if (transaction instanceof CashPurchase) {
				if (transaction.getType() == Transaction.TYPE_CASH_PURCHASE) {
					templeteName = "templetes" + File.separator
							+ "CashPurchaseOdt.odt";
					fileName = "CashPurchase_" + transaction.getNumber();
					purchasePdfGeneration = new CashPurchasePdfGeneration(
							(CashPurchase) transaction, company);
				} else {
					templeteName = "templetes" + File.separator
							+ "CashExpenseOdt.odt";
					fileName = "CashExpense_" + transaction.getNumber();
					cashExpensePdfGeneration = new CashExpensePdfGeneration(
							(CashPurchase) transaction, company);
				}
			}

			if (transaction instanceof CustomerPrePayment) {
				templeteName = "templetes" + File.separator
						+ "CustomerPaymentOdt.odt";
				fileName = "Prepayment_" + transaction.getNumber();
				customerPaymentPdfGeneration = new CustomerPaymentPdfGeneration(
						(CustomerPrePayment) transaction, company);
			}

			if (transaction instanceof CreditCardCharge) {
				templeteName = "templetes" + File.separator
						+ "CCExpenseOdt.odt";
				fileName = "CCExpense_" + transaction.getNumber();
				ccExpensePdfGeneration = new CCExpensePdfGeneration(
						(CreditCardCharge) transaction, company);
			}

			if (transaction instanceof VendorCreditMemo) {
				templeteName = "templetes" + File.separator
						+ "VendorCreditOdt.odt";
				fileName = "CreditMemo_" + transaction.getNumber();
				vendorCreditPdfGeneration = new VendorCreditPdfGeneration(
						(VendorCreditMemo) transaction, company);

			}

			if (transaction instanceof WriteCheck) {
				templeteName = "templetes" + File.separator
						+ "WriteCheckOdt.odt";
				fileName = "WriteCheck_" + transaction.getNumber();
				writeCheckPdfGeneration = new WriteCheckPdfGeneration(
						(WriteCheck) transaction, company);

			}

			if (transaction instanceof VendorPayment) {
				templeteName = "templetes" + File.separator
						+ "VendorPaymentOdt.odt";
				fileName = "Payment_" + transaction.getNumber();
				vendorPaymentPdfGeneration = new VendorPaymentPdfGeneration(
						(VendorPayment) transaction, company);

			}

			if (transaction instanceof EnterBill) {
				templeteName = "templetes" + File.separator
						+ "EnterBillOdt.odt";
				fileName = "Bill_" + transaction.getNumber();
				enterBillPdfGeneration = new EnterBillPdfGeneration(
						(EnterBill) transaction, company);

			}

			if (transaction instanceof PayBill) {
				templeteName = "templetes" + File.separator + "PaybillOdt.odt";
				fileName = "PayBill_" + transaction.getNumber();
				payBillPdfGeneration = new PayBillPdfGeneration(
						(PayBill) transaction, company);

			}

			if (transaction instanceof CashSales) {
				if (brandingTheme.getCashSaleTemplateName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "CashSaleOdt.odt";
				} else {
					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getCashSaleTemplateName();
				}

				fileName = "CashSale_" + transaction.getNumber();
				cashSalePdfGeneration = new CashSalePdfGeneration(
						(CashSales) transaction, company, brandingTheme);
			}
			if (transaction instanceof Estimate) {

				Estimate est = (Estimate) transaction;

				if (est.getEstimateType() == Estimate.QUOTES) {
					// For Quote
					if (brandingTheme.getQuoteTemplateName().contains(
							"Classic Template")) {
						templeteName = "templetes" + File.separator
								+ "QuoteDocx.docx";
					} else {
						templeteName = ServerConfiguration.getAttachmentsDir()
								+ "/" + company.getId() + "/" + "templateFiles"
								+ "/" + brandingTheme.getID() + "/"
								+ brandingTheme.getQuoteTemplateName();
					}
					fileName = "Quote_" + transaction.getNumber();
					quotePdfGeneration = new QuotePdfGeneration(
							(Estimate) transaction, company, brandingTheme);
				} else if (est.getEstimateType() == Estimate.SALES_ORDER) {
					// for sales Order
					if (brandingTheme.getSalesOrderTemplateName().contains(
							"Classic Template")) {
						templeteName = "templetes" + File.separator
								+ "SalesOrder.docx";
					} else {

						templeteName = ServerConfiguration.getAttachmentsDir()
								+ "/" + company.getId() + "/" + "templateFiles"
								+ "/" + brandingTheme.getID() + "/"
								+ brandingTheme.getSalesOrderTemplateName();
					}
					fileName = "SalesOrder_" + transaction.getNumber();
					salesOrderPdfGeneration = new SalesOrderPdfGeneration(
							(Estimate) transaction, company, brandingTheme);
				}
			}

			if (transaction instanceof PurchaseOrder) {
				// for Purchase Order
				if (brandingTheme.getPurchaseOrderTemplateName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "PurchaseOrder.docx";
				} else {

					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getPurchaseOrderTemplateName();
				}
				fileName = "PurchaseOrder_" + transaction.getNumber();
				purchaseOrderPdfGeneration = new PurchaseOrderPdfGeneration(
						(PurchaseOrder) transaction, company, brandingTheme);
			}

			if (transaction instanceof JournalEntry) {
				// for Purchase Order
				if (brandingTheme.getPurchaseOrderTemplateName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "JournelEntryDocx.docx";
				} else {

					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getPurchaseOrderTemplateName();
				}
				fileName = "JournalEntry_" + transaction.getNumber();
				journelEntryPdfGeneration = new JournelEntryPdfGeneration(
						(JournalEntry) transaction, company, brandingTheme);
			}

			InputStream in = new BufferedInputStream(new FileInputStream(
					templeteName));

			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);
			IContext context = report.createContext();

			if (transaction instanceof CustomerCreditMemo) {
				context = creditPdfGeneration.assignValues(context, report);
			} else if (transaction instanceof Invoice) {
				context = invoicePdfGeneration.assignValues(context, report);
			} else if (transaction instanceof Estimate) {
				if (((Estimate) transaction).getEstimateType() == Estimate.QUOTES) {
					context = quotePdfGeneration.assignValues(context, report);
				} else {
					context = salesOrderPdfGeneration.assignValues(context,
							report);
				}
			} else if (transaction instanceof CashSales) {
				context = cashSalePdfGeneration.assignValues(context, report);
			} else if (transaction instanceof ReceivePayment) {
				context = receivePaymentPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof PurchaseOrder) {
				context = purchaseOrderPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof JournalEntry) {
				context = journelEntryPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof CustomerRefund) {
				context = refundPdfGeneration.assignValues(context, report);
			} else if (transaction instanceof CashPurchase) {
				if (transaction.getType() == Transaction.TYPE_CASH_PURCHASE) {
					context = purchasePdfGeneration.assignValues(context,
							report);
				} else {
					context = cashExpensePdfGeneration.assignValues(context,
							report);
				}
			} else if (transaction instanceof CustomerPrePayment) {
				context = customerPaymentPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof CreditCardCharge) {
				context = ccExpensePdfGeneration.assignValues(context, report);
			} else if (transaction instanceof VendorCreditMemo) {
				context = vendorCreditPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof WriteCheck) {
				context = writeCheckPdfGeneration.assignValues(context, report);
			} else if (transaction instanceof VendorPayment) {
				context = vendorPaymentPdfGeneration.assignValues(context,
						report);
			} else if (transaction instanceof EnterBill) {
				context = enterBillPdfGeneration.assignValues(context, report);
			} else if (transaction instanceof PayBill) {
				context = payBillPdfGeneration.assignValues(context, report);
			}

			FontFactory.setFontImp(new FontFactoryImpEx());
			if (multipleIds) {
				Options options = Options.getTo(ConverterTypeTo.PDF).via(
						ConverterTypeVia.ITEXT);

				File file = File.createTempFile(fileName.replace(" ", ""),
						".pdf");
				java.io.FileOutputStream fos = new java.io.FileOutputStream(
						file);
				report.convert(context, options, fos);
				fileNames.add(file.getAbsolutePath());

			}

			HashMap objects = new HashMap();
			objects.put("context", context);
			objects.put("report", report);
			return objects;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * for merging custom PDF Documents in to Single Pdf Document
	 * 
	 * @param fileNames
	 * @param outPutFileName
	 * @param paginate
	 */
	private void mergePDFDocuments(List<String> fileNames,
			OutputStream outputStream, boolean paginate) {
		Document document = new Document();
		try {

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			// Loop through the PDF files and add to the output.
			for (String fileName : fileNames) {
				PdfReader pdfReader = new PdfReader(fileName);
				int pageOfCurrentReaderPDF = 0;
				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					PdfImportedPage page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
								+ pageOfCurrentReaderPDF + " "
								+ Global.get().messages().of() + " "
								+ pdfReader.getNumberOfPages(), 520, 5, 0);
						cb.endText();
					}
				}
			}
			outputStream.flush();
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	public static class FontFactoryImpEx extends FontFactoryImp {

		private static Properties properties;
		private static String fontsPath = "./config/fonts/";

		FontFactoryImpEx() {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(fontsPath
						+ "pd4fonts.properties"));
			} catch (Exception e) {

			}
		}

		@Override
		public Font getFont(String fontname, String encoding, boolean embedded,
				float size, int style, Color color) {
			try {
				String fileName = properties.getProperty(fontname);
				BaseFont bf = BaseFont.createFont(fontsPath + fileName,
						BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				bf.setSubset(true);
				return new Font(bf, size, style, color);
			} catch (Exception e) {
				Font font = super.getFont(fontname, encoding, true, size,
						style, color);
				return font;
			}

		}
	}
}
