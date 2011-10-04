package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.CSVReportTemplate;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class ExportReportServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String style;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void exportReport(HttpServletRequest request,
			HttpServletResponse response) {

		ServletOutputStream sos = null;
		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}

			ITemplate template = getTempleteObjByRequest(request);
			response.setContentType("application/csv");
			response.setHeader("Content-disposition", "attachment; filename="
					+ template.getFileName().trim().replace(" ", "") + ".csv");
			sos = response.getOutputStream();

			String templateBody = template.getBody();
			if (templateBody == null) {
				templateBody = "No records to show";
			}

			String header = template.getHeader() + "\n" + templateBody;
			sos.write(header.getBytes());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sos != null)
					sos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private ITemplate getTempleteObjByRequest(HttpServletRequest request)
			throws IOException, AccounterException {
		String companyID = getCookie(request, COMPANY_COOKIE);
		String companyName = getCompanyName(request);
		Session session = HibernateUtil.openSession();
		if (companyName == null)
			return null;
		try {

			FinanceTool financetool = new FinanceTool();
			Company company = financetool.getCompany(Long.valueOf(companyID));
			int companyType = company.getAccountingType();

			TemplateBuilder.setCmpName(companyName);

			ClientCompanyPreferences clientCompanyPreferences = financetool
					.getCompanyManager().getClientCompanyPreferences(company);
			CompanyPreferenceThreadLocal.set(clientCompanyPreferences);

			ITemplate template = null;
			template = getReportTemplate(request, financetool, companyType);

			return template;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	private ITemplate getReportTemplate(HttpServletRequest request,
			FinanceTool financeTool, int companyType) throws IOException {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		String vendor = request.getParameter("vendorId");
		long vendorId = 0;
		int boxNo = 0;
		if (vendor != null) {
			vendorId = Long.parseLong(vendor);
		}
		String box = request.getParameter("boxNo");
		if (box != null) {
			boxNo = Integer.parseInt(box);
		}
		String companyID = getCookie(request, COMPANY_COOKIE);

		Company company = financeTool.getCompany(Long.valueOf(companyID));
		ReportsGenerator generator = null;

		if (vendorId != 0) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPECSV,
					vendorId, boxNo, companyType, company);

		} else if (status != null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPECSV, status,
					companyType, company);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPECSV,
					companyType, company);
		}

		String gridTemplate = generator.generate(financeTool,
				ReportsGenerator.GENERATIONTYPECSV);

		CSVReportTemplate template = new CSVReportTemplate(company, reportType,
				new String[] { gridTemplate, null, style, dateRangeHtml });
		/*  */

		return template;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;
		exportReport(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
