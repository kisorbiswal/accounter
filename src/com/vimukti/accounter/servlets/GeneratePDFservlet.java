package com.vimukti.accounter.servlets;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.CreditNoteTemplete;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoiceTemplete;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.server.FinanceTool;

public class GeneratePDFservlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	class TemplateAndConcerter {
		ITemplate template;
		Converter converter;
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
			String footerImg = ("FinanceDir" + File.separator + "footer-print-img.jpg");

			String style = ("FinanceDir" + File.separator + "FinancePrint.css");

			TemplateAndConcerter result = getTempleteObjByRequest(request,
					footerImg, style);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ result.template.getFileName().replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			System.err.println("Converter obj created");
			result.converter.generatePDF(result.template, sos);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private TemplateAndConcerter getTempleteObjByRequest(
			HttpServletRequest request, String footerImg, String style) {
		Session session = null;
		try {

			String companyName = getCompanyName(request);
			session = HibernateUtil.openSession(companyName);

			FinanceTool financetool = getFinanceTool(request);
			TemplateBuilder.setCmpName(companyName);

			TemplateAndConcerter result = new TemplateAndConcerter();

			String objectId = request.getParameter("objectId");
			if (objectId != null) {
				int type = Integer.parseInt(request.getParameter("type"));

				if (type == Transaction.TYPE_INVOICE) {
					Invoice invoice = (Invoice) financetool
							.getServerObjectForid(AccounterCoreType.INVOICE,
									Long.parseLong(objectId));
					String brandingThemeId = request
							.getParameter("brandingThemeId");
					BrandingTheme brandingTheme = (BrandingTheme) financetool
							.getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));

					result.converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));
					result.template = new InvoiceTemplete(invoice,
							brandingTheme, footerImg, style);
				}
				// for Credit Note
				if (type == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
					result.converter = new Converter();
					CustomerCreditMemo memo = (CustomerCreditMemo) financetool
							.getServerObjectForid(
									AccounterCoreType.CUSTOMERCREDITMEMO,
									Long.parseLong(objectId));
					result.template = new CreditNoteTemplete(memo, footerImg,
							style);
				}

			}
			// for Reports
			else {
				result.converter = new Converter();
				result.template = getReportTemplate(request, financetool,
						footerImg, style);
			}

			return result;
		} finally {
			session.close();
		}

	}

	private ITemplate getReportTemplate(HttpServletRequest request,
			FinanceTool financeTool, String footerImg, String style) {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		ReportsGenerator generator;
		if (status == null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, status);
		}

		String gridTemplate = generator.generate(financeTool,
				ReportsGenerator.GENERATIONTYPEPDF);

		ReportTemplate template = new ReportTemplate(reportType, new String[] {
				gridTemplate, footerImg, style, dateRangeHtml });
		/*  */

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
		generatePDF(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
