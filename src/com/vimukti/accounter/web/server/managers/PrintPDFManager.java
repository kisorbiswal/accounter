package com.vimukti.accounter.web.server.managers;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.EnterBillPdfGeneration;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.InvoicePdfGeneration;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.JournelEntryPdfGeneration;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayBillPdfGeneration;
import com.vimukti.accounter.core.PaySlipPdfGeneration;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.PurchaseOrderPdfGeneration;
import com.vimukti.accounter.core.QuotePdfGeneration;
import com.vimukti.accounter.core.QuotePdfTemplate;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceivePaymentPdfGeneration;
import com.vimukti.accounter.core.RefundPdfGeneration;
import com.vimukti.accounter.core.SalesOrderPdfGeneration;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionPDFGeneration;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorCreditPdfGeneration;
import com.vimukti.accounter.core.VendorPaymentPdfGeneration;
import com.vimukti.accounter.core.VendorPrePayment;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.core.WriteCheckPdfGeneration;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class PrintPDFManager extends Manager {

	private final static String CLASSIC = "Classic";
	private final static String PROFESSIONAL = "Professional";
	private final static String PLAIN = "Plain";
	private final static String MODERN = "Modern";

	private static final String PAGE_BREAK = "<pd4ml:page.break>";

	/**
	 * to generate pdf using HTML files
	 * 
	 * @param request
	 * @param response
	 * @param companyName
	 * @return
	 */
	private List<String> generateHtmlPDF(Company company,
			BrandingTheme brandingTheme, String objectId, int transactionType) {
		List<String> result = new ArrayList<String>();
		Converter converter = null;
		String fileName = "";
		PrintTemplete printTemplete = null;
		boolean pageBreak = false;
		try {

			StringBuilder outPutString = new StringBuilder();
			try {
				FinanceTool financetool = new FinanceTool();

				TemplateBuilder.setCmpName(company.getDisplayName());

				converter = new Converter(
						getPageSizeType(brandingTheme.getPageSizeType()));

				String[] ids = null;
				if (objectId != null) {
					ids = objectId.split(",");
					if (ids.length > 1) {
						pageBreak = true;

					}
				}

				// this is used to print multiple pdf documents at a time
				if (objectId != null) {

					for (int i = 0; i < ids.length; i++) {

						if (transactionType == Transaction.TYPE_INVOICE) {
							Invoice invoice = (Invoice) financetool
									.getManager().getServerObjectForid(
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

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// session.close();
			}
			FontFactory.setFontImp(new FontFactoryImpEx());
			String output = outPutString.toString().replaceAll("</html>", "");
			output = output.toString().replaceAll("<html>", "");

			output = "<html>" + output + "</html>";
			InputStream inputStream = new ByteArrayInputStream(
					output.getBytes("UTF-8"));

			InputStreamReader reader = new InputStreamReader(inputStream,
					Charset.forName("UTF-8"));
			File file = File.createTempFile(fileName.replace(" ", ""), ".pdf");
			result.add(file.getName());
			FileOutputStream outputStream = new FileOutputStream(file);
			switch (transactionType) {
			case Transaction.TYPE_INVOICE:
			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			case Transaction.TYPE_CASH_SALES:
			case Transaction.TYPE_ESTIMATE:
				converter.generatePdfDocuments(printTemplete, outputStream,
						reader);
				break;
			}

			System.err.println("HTML Pdf created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		result.add(fileName + ".pdf");
		return result;
	}

	/**
	 * to generate custom pdf template using odt & docx
	 * 
	 * @param request
	 * @param response
	 * @param companyName
	 * @return
	 */
	public List<String> generateCustom2PDF(Company company,
			int transactionType, String objectId, BrandingTheme brandingTheme,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<String> result = new ArrayList<String>();
		TransactionPDFGeneration pdfGenerator = null;
		OutputStream sos = null;
		boolean isMultipleId = false;
		String fileName = "";
		List<String> fileNames = new ArrayList<String>();
		try {

			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(company.getDisplayName());

				String[] ids = objectId.split(",");
				isMultipleId = ids.length > 1;

				// this is used to print multiple pdf documents at a time
				for (int i = 0; i < ids.length; i++) {
					if (transactionType == 116) {
						Employee payrun = (Employee) financetool.getManager()
								.getServerObjectForid(
										AccounterCoreType.EMPLOYEE,
										Long.parseLong(ids[i]));
						pdfGenerator = generatePaySlip(payrun, new FinanceDate(
								startDate), new FinanceDate(endDate));
					} else {
						AccounterCoreType accounterCoreType = UIUtils
								.getAccounterCoreType(transactionType);
						Transaction transaction = (Transaction) financetool
								.getManager().getServerObjectForid(
										accounterCoreType,
										Long.parseLong(ids[i]));
						pdfGenerator = odt2PdfGeneration(transaction,
								brandingTheme, new FinanceDate(startDate),
								new FinanceDate(endDate));
					}

					fileName = pdfGenerator.getFileName();
					if (isMultipleId) {
						InputStream in = new BufferedInputStream(
								new FileInputStream(
										pdfGenerator.getTemplateName()));
						IXDocReport report = XDocReportRegistry.getRegistry()
								.loadReport(in, TemplateEngineKind.Velocity);
						IContext context = report.createContext();
						context = pdfGenerator.assignValues(context, report);
						FontFactory.setFontImp(new FontFactoryImpEx());
						Options options = Options.getTo(ConverterTypeTo.PDF)
								.via(ConverterTypeVia.ITEXT);

						File file = File.createTempFile(pdfGenerator
								.getFileName().replace(" ", ""), ".pdf");
						java.io.FileOutputStream fos = new java.io.FileOutputStream(
								file);
						report.convert(context, options, fos);
						fileNames.add(file.getAbsolutePath());
					}
				}

				if (pdfGenerator != null) {
					File file = File.createTempFile(fileName.replace(" ", ""),
							".pdf");
					java.io.FileOutputStream fos = new java.io.FileOutputStream(
							file);
					result.add(file.getName());
					if (isMultipleId) {
						mergePDFDocuments(fileNames, fos, true);
					} else {
						InputStream in = new BufferedInputStream(
								new FileInputStream(
										pdfGenerator.getTemplateName()));
						IXDocReport report = XDocReportRegistry.getRegistry()
								.loadReport(in, TemplateEngineKind.Velocity);
						IContext context = report.createContext();
						context = pdfGenerator.assignValues(context, report);
						FontFactory.setFontImp(new FontFactoryImpEx());
						Options options = Options.getTo(ConverterTypeTo.PDF)
								.via(ConverterTypeVia.ITEXT);
						report.convert(context, options, fos);
					}
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
		result.add(fileName + ".pdf");
		return result;
	}

	private TransactionPDFGeneration generatePaySlip(Employee employee,
			FinanceDate startDate, FinanceDate endDate) {

		try {
			PaySlipPdfGeneration pdfGeneration = new PaySlipPdfGeneration(
					employee, startDate, endDate);
			InputStream in = new BufferedInputStream(new FileInputStream(
					pdfGeneration.getTemplateName()));

			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);
			IContext context = report.createContext();
			context = pdfGeneration.assignValues(context, report);
			FontFactory.setFontImp(new FontFactoryImpEx());
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ITEXT);

			File file = File.createTempFile(pdfGeneration.getFileName()
					.replace(" ", ""), ".pdf");
			java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
			report.convert(context, options, fos);
			return pdfGeneration;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private Dimension getPageSizeType(int pageSizeType) {
		switch (pageSizeType) {
		case 2:
			return PD4Constants.LETTER;
		default:
			return PD4Constants.A4;
		}
	}

	public List<String> generatePDFFile(Company company,
			BrandingTheme brandingTheme, int type, String objectId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		try {
			if (brandingTheme == null
					|| brandingTheme.isCustomFile()
					|| (type == Transaction.TYPE_CASH_SALES
							|| type == Transaction.TYPE_PURCHASE_ORDER || type == Transaction.TYPE_JOURNAL_ENTRY)) {
				return generateCustom2PDF(company, type, objectId,
						brandingTheme, startDate, endDate);
			} else {
				return generateHtmlPDF(company, brandingTheme, objectId, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

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

	private TransactionPDFGeneration odt2PdfGeneration(Transaction transaction,
			BrandingTheme brandingTheme, FinanceDate startDate,
			FinanceDate endDate) {

		try {
			TransactionPDFGeneration pdfGeneration = null;

			if (transaction instanceof Invoice) {
				// for Invoice
				pdfGeneration = new InvoicePdfGeneration((Invoice) transaction,
						brandingTheme);
			}

			if (transaction instanceof CustomerCreditMemo) {
				// For CreditNote
				pdfGeneration = new CreditNotePdfGeneration(
						(CustomerCreditMemo) transaction, brandingTheme);
			}

			if (transaction instanceof ReceivePayment) {
				pdfGeneration = new ReceivePaymentPdfGeneration(
						(ReceivePayment) transaction);
			}

			if (transaction instanceof CustomerRefund) {
				pdfGeneration = new RefundPdfGeneration(
						(CustomerRefund) transaction);
			}

			if (transaction instanceof CashPurchase) {
				pdfGeneration = new CashExpensePdfGeneration(
						(CashPurchase) transaction);
			}

			if (transaction instanceof CustomerPrePayment) {
				pdfGeneration = new CustomerPaymentPdfGeneration(
						(CustomerPrePayment) transaction);
			}

			if (transaction instanceof CreditCardCharge) {
				pdfGeneration = new CCExpensePdfGeneration(
						(CreditCardCharge) transaction);
			}

			if (transaction instanceof VendorCreditMemo) {
				pdfGeneration = new VendorCreditPdfGeneration(
						(VendorCreditMemo) transaction);

			}

			if (transaction instanceof WriteCheck) {
				pdfGeneration = new WriteCheckPdfGeneration(
						(WriteCheck) transaction);
			}

			if (transaction instanceof VendorPrePayment) {
				pdfGeneration = new VendorPaymentPdfGeneration(
						(VendorPrePayment) transaction);

			}

			if (transaction instanceof EnterBill) {
				pdfGeneration = new EnterBillPdfGeneration(
						(EnterBill) transaction);
			}

			if (transaction instanceof PayBill) {
				pdfGeneration = new PayBillPdfGeneration((PayBill) transaction);
			}

			if (transaction instanceof CashSales) {
				pdfGeneration = new CashSalePdfGeneration(
						(CashSales) transaction, brandingTheme);
			}
			if (transaction instanceof Estimate) {

				Estimate est = (Estimate) transaction;

				if (est.getEstimateType() == Estimate.QUOTES) {
					// For Quote
					pdfGeneration = new QuotePdfGeneration(
							(Estimate) transaction, brandingTheme);
				} else if (est.getEstimateType() == Estimate.SALES_ORDER) {
					// for sales Order
					pdfGeneration = new SalesOrderPdfGeneration(
							(Estimate) transaction, brandingTheme);
				}
			}

			if (transaction instanceof PurchaseOrder) {
				// for Purchase Order
				pdfGeneration = new PurchaseOrderPdfGeneration(
						(PurchaseOrder) transaction, brandingTheme);
			}

			if (transaction instanceof JournalEntry) {
				// for Journal Entry
				pdfGeneration = new JournelEntryPdfGeneration(
						(JournalEntry) transaction, brandingTheme);
			}

			return pdfGeneration;
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

	private static class FontFactoryImpEx extends FontFactoryImp {

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
