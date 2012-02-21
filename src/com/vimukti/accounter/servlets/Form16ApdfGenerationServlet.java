package com.vimukti.accounter.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.lowagie.text.FontFactory;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Form16ApdfTemplate;
import com.vimukti.accounter.core.Form16ApdfTemplate.BookEntry;
import com.vimukti.accounter.core.Form16ApdfTemplate.FormDetails;
import com.vimukti.accounter.core.Form16ApdfTemplate.PaymentSummary;
import com.vimukti.accounter.core.Form16ApdfTemplate.SummaryOfTax;
import com.vimukti.accounter.core.Form16ApdfTemplate.Verification;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TDSChalanDetail;
import com.vimukti.accounter.core.TDSCoveringLetterTemplate;
import com.vimukti.accounter.core.TDSDeductorMasters;
import com.vimukti.accounter.core.TDSResponsiblePerson;
import com.vimukti.accounter.core.TDSTransactionItem;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.servlets.GeneratePDFservlet.FontFactoryImpEx;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class Form16ApdfGenerationServlet extends BaseServlet {

	/**
	 * this file is required to generate the form 16A pdf
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Long companyID;
	private FinanceTool financetool;
	private Company company;
	private long vendorID;
	private int type;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String companyName = getCompanyName(req);
		if (companyName == null)
			return;
		try {
			generatePDF(req, resp, companyName);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	private void generatePDF(HttpServletRequest req, HttpServletResponse resp,
			String companyName) throws AccounterException {
		try {
			ServletOutputStream sos = null;
			String vendorIDString = req.getParameter("vendorID");
			String dateRangeString = req.getParameter("datesRange");
			String[] split = dateRangeString.split("-");
			String fromDate = split[0];
			String toDate = split[1];
			String place = req.getParameter("place");
			String date = req.getParameter("printDate");
			String typeString = req.getParameter("type");

			type = Integer.parseInt(typeString);

			vendorID = Long.parseLong(vendorIDString);
			companyID = (Long) req.getSession().getAttribute(COMPANY_ID);
			String acknowledgementNo = req.getParameter("tdsCertificateNumber");
			financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);
			company = financetool.getCompany(companyID);
			CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
					.getClientCompanyPreferences(company));

			Session currentSession = HibernateUtil.getCurrentSession();
			Vendor vendor = (Vendor) currentSession.get(Vendor.class, vendorID);
			IXDocReport report = null;
			IContext context = null;
			TDSDeductorMasters tdsDeductorMasterDetails = company
					.getTdsDeductor();
			TDSResponsiblePerson tdsResposiblePerson = company
					.getTdsResposiblePerson();
			if (tdsDeductorMasterDetails == null || vendor == null
					|| tdsResposiblePerson == null) {
				return;
			}
			if (type == 0) {
				fileName = "Form16A";
				String templeteName = "templetes" + File.separator
						+ "Form16a.docx";
				ClientFinanceDate cEndDate = new ClientFinanceDate(toDate);
				ClientFinanceDate cStartDate = new ClientFinanceDate(fromDate);
				FinanceDate startDate = new FinanceDate(cStartDate);
				FinanceDate endDate = new FinanceDate(cEndDate);
				ArrayList<TDSChalanDetail> chalanList = financetool
						.getChalanList(startDate, endDate, acknowledgementNo,
								company.getID());

				updateChallanForVendor(chalanList, vendor);

				ClientTDSDeductorMasters deductor = financetool
						.getTDSDeductorMasterDetails(company.getId());

				Form16ApdfTemplate form16APdfGeneration = new Form16ApdfTemplate();

				form16APdfGeneration.setFormdetails(getFormDetails(deductor,
						vendor, dateRangeString));

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}

				form16APdfGeneration
						.setSummaryOfPayments(getSummaryOfPayments(chalanList));

				Map<Integer, ArrayList<TDSChalanDetail>> quartersList = new HashMap<Integer, ArrayList<TDSChalanDetail>>();
				for (int i = 0; i < 4; i++) {
					FinanceDate[] quarterDates = getQuarterDates(i);
					ArrayList<TDSChalanDetail> quarterList = new ArrayList<TDSChalanDetail>();
					if (quarterDates != null) {
						quarterList = financetool.getChalanList(
								quarterDates[0], quarterDates[1],
								acknowledgementNo, company.getID());
						updateChallanForVendor(quarterList, vendor);
					}
					quartersList.put(i, quarterList);
				}
				form16APdfGeneration
						.setSummaryOfTax(getSummaryOfTax(quartersList));
				form16APdfGeneration.setBookentries(getBookEntries(
						form16APdfGeneration, chalanList));
				form16APdfGeneration.setChalaLists(getChallansEntries(
						form16APdfGeneration, chalanList));
				form16APdfGeneration.setVerification(getVerificationDetails(
						tdsResposiblePerson, place, date, chalanList));
				context = report.createContext();
				context = form16APdfGeneration.assignValues(context, report);

			} else {
				fileName = "CoveringLetter" + vendor.getName();
				String templeteName = null;
				templeteName = "templetes" + File.separator
						+ "CoveringLetter.docx";
				TDSCoveringLetterTemplate coveringLetterTemp = new TDSCoveringLetterTemplate();
				coveringLetterTemp.setValues(vendor, company,
						tdsResposiblePerson);

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}
				context = report.createContext();
				context = coveringLetterTemp.assignValues(context, report);
			}
			FontFactory.setFontImp(new FontFactoryImpEx());
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ITEXT);

			resp.setContentType("application/pdf");
			resp.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");

			sos = resp.getOutputStream();

			report.convert(context, options, sos);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println("Pdf created");
	}

	private Verification getVerificationDetails(
			TDSResponsiblePerson tdsResposiblePerson, String place,
			String date, ArrayList<TDSChalanDetail> chalanList) {
		Verification verification = new Form16ApdfTemplate().new Verification();
		verification.setDate(date);
		if (place == null || place.isEmpty()) {
			place = tdsResposiblePerson.getCity();
		}
		verification.setPlace(place);
		verification.setDesignation(tdsResposiblePerson.getDesignation());
		verification.setFullName(tdsResposiblePerson.getName());
		verification.setSignature("");
		verification.setName(tdsResposiblePerson.getName());
		double total = 0;
		for (TDSChalanDetail challan : chalanList) {
			total += challan.getTotal();
		}
		verification.setRupees(String.valueOf(total));
		verification
				.setAmountinwords(com.vimukti.accounter.web.client.core.Utility
						.convert(total));

		return verification;
	}

	private List<BookEntry> getChallansEntries(
			Form16ApdfTemplate form16ATemplate,
			ArrayList<TDSChalanDetail> chalanList) {
		List<BookEntry> challanEntries = new ArrayList<BookEntry>();
		double total = 0.0;
		int srNo = 1;
		for (TDSChalanDetail chalan : chalanList) {
			BookEntry bookEntry = new Form16ApdfTemplate().new BookEntry();
			bookEntry.setSrNo(String.valueOf(srNo));
			bookEntry.setBsrCode(chalan.getBankBsrCode());
			bookEntry.setDateTaxDeposited(new FinanceDate(chalan
					.getDateTaxPaid()).toString());
			bookEntry.setSerialNumber(String.valueOf(chalan
					.getChalanSerialNumber()));
			bookEntry.setTaxDeposited(String.valueOf(chalan.getTotal()));
			total += chalan.getTotal();
			challanEntries.add(bookEntry);
			srNo++;
		}
		if (challanEntries.isEmpty()) {
			BookEntry bookEntry = new Form16ApdfTemplate().new BookEntry();
			bookEntry.setSrNo(String.valueOf(srNo));
			bookEntry.setBsrCode("No information available");
			bookEntry.setDateTaxDeposited("");
			bookEntry.setSerialNumber("");
			bookEntry.setTaxDeposited(String.valueOf(0.00));
			challanEntries.add(bookEntry);
		}
		form16ATemplate
				.setTotalTaxDeductedThroughChallan(String.valueOf(total));
		return challanEntries;
	}

	private List<BookEntry> getBookEntries(Form16ApdfTemplate form16ATemplate,
			ArrayList<TDSChalanDetail> chalanList) {
		List<BookEntry> bookEntries = new ArrayList<BookEntry>();
		double total = 0.0;
		int srNo = 1;
		for (TDSChalanDetail chalan : chalanList) {
			if (chalan.isBookEntry()) {
				BookEntry bookEntry = new Form16ApdfTemplate().new BookEntry();
				bookEntry.setSrNo(String.valueOf(srNo));
				bookEntry.setBsrCode(chalan.getBankBsrCode());
				bookEntry.setDateTaxDeposited(new FinanceDate(chalan
						.getDateTaxPaid()).toString());
				bookEntry.setSerialNumber(String.valueOf(chalan
						.getChalanSerialNumber()));
				bookEntry.setTaxDeposited(String.valueOf(chalan.getTotal()));
				total += chalan.getTotal();
				bookEntries.add(bookEntry);
				srNo++;
			}
		}
		if (bookEntries.isEmpty()) {
			BookEntry bookEntry = new Form16ApdfTemplate().new BookEntry();
			bookEntry.setSrNo(String.valueOf(srNo));
			bookEntry.setBsrCode("No information available");
			bookEntry.setDateTaxDeposited("");
			bookEntry.setSerialNumber("");
			bookEntry.setTaxDeposited(String.valueOf(0.00));
			bookEntries.add(bookEntry);
		}
		form16ATemplate.setTotalTaxDeductedThroughBookEntry(String
				.valueOf(total));
		return bookEntries;
	}

	private void updateChallanForVendor(ArrayList<TDSChalanDetail> chalanList,
			Vendor vendor) {
		List<TDSChalanDetail> challans = new ArrayList<TDSChalanDetail>();
		for (TDSChalanDetail challan : chalanList) {
			boolean haveVendor = false;
			List<TDSTransactionItem> items = new ArrayList<TDSTransactionItem>();
			for (TDSTransactionItem item : challan.getTdsTransactionItems()) {
				if (item.getVendor() == null
						|| !item.getVendor().equals(vendor)) {
					items.add(item);
					challan.setIncomeTaxAmount(challan.getIncomeTaxAmount()
							- item.getTdsAmount());
					challan.setEducationCessAmount(challan
							.getEducationCessAmount() - item.getEduCess());
					challan.setSurchangePaidAmount(challan
							.getSurchangePaidAmount()
							- item.getSurchargeAmount());
					challan.setTotal(challan.getTotal() - item.getTotalTax());
				} else {
					haveVendor = true;
				}
			}
			challan.getTdsTransactionItems().removeAll(items);
			if (challan.getTotal() == 0 && !haveVendor) {
				challans.add(challan);
			}
		}
		chalanList.removeAll(challans);
	}

	private List<SummaryOfTax> getSummaryOfTax(
			Map<Integer, ArrayList<TDSChalanDetail>> quartersList) {
		List<SummaryOfTax> summaryOfTaxes = new ArrayList<SummaryOfTax>();
		for (int i = 0; i < quartersList.size(); i++) {
			SummaryOfTax summaryOfTax = new Form16ApdfTemplate().new SummaryOfTax();
			summaryOfTax.setQuarter("Quarter " + String.valueOf(i + 1));
			if (!quartersList.get(i).isEmpty()) {
				long deposited = 0;
				for (TDSChalanDetail chalan : quartersList.get(i)) {
					deposited += chalan.getTotal();
					summaryOfTax.setReceiptNumber(chalan
							.getEtdsfillingAcknowledgementNo());
				}
				summaryOfTax.setDeposited(String.valueOf(deposited));
				summaryOfTax.setAmount(String.valueOf(deposited));
			} else {
				summaryOfTax.setAmount("");
				summaryOfTax.setDeposited("");
				summaryOfTax.setReceiptNumber("");
			}
			summaryOfTaxes.add(summaryOfTax);
		}
		return summaryOfTaxes;
	}

	private List<PaymentSummary> getSummaryOfPayments(
			ArrayList<TDSChalanDetail> chalanList) {
		List<PaymentSummary> summaryOfPayments = new ArrayList<PaymentSummary>();
		for (TDSChalanDetail challan : chalanList) {
			for (TDSTransactionItem item : challan.getTdsTransactionItems()) {
				summaryOfPayments
						.add(new Form16ApdfTemplate().new PaymentSummary(
								Utility.decimalConversation(
										item.getTotalAmount(), ""), challan
										.getPaymentSection(), item
										.getTransactionDate().toString()));
			}
		}
		return summaryOfPayments;
	}

	private FormDetails getFormDetails(ClientTDSDeductorMasters deductor,
			Vendor vendor, String dateRangeString) throws AccounterException {
		FormDetails formDetails = new Form16ApdfTemplate().new FormDetails();

		formDetails.setDeductorAddress(getDeductorNameAddress(deductor));

		String vendorAddr = vendor.getName();
		if (vendor.getAddress() != null && !vendor.getAddress().isEmpty()) {
			for (Address addr : vendor.getAddress()) {
				if (addr.getType() == Address.TYPE_BILL_TO) {
					vendorAddr += "\n" + getValidAddress(addr);
					break;
				}
			}
		}
		formDetails.setDeducteeAddress(vendorAddr);

		formDetails.setDeductorPan(deductor.getPanNumber());

		formDetails.setDeductorTan(deductor.getTanNumber());

		formDetails.setDeducteePan(vendor.getTaxId());

		Address cit = null;
		cit = new ServerConvertUtil().toServerObject(cit,
				deductor.getTaxOfficeAddress(),
				HibernateUtil.getCurrentSession());

		formDetails.setCit(getValidAddress(cit));

		String[] dates = dateRangeString.split("-");

		formDetails.setFromDate(dates[0]);
		formDetails.setToDate(dates[1]);

		ClientFinanceDate date1 = new ClientFinanceDate(dates[0]);
		ClientFinanceDate date2 = new ClientFinanceDate(dates[1]);
		String assesment = date1.getYear() + " - " + (date1.getYear() + 1);

		formDetails.setAssessmentYear(assesment);
		return formDetails;
	}

	private FinanceDate[] getQuarterDates(int quarter) {
		int finYear = new ClientFinanceDate().getYear();
		String frmDt = null;
		String toDt = null;
		switch (quarter) {
		case 0:
			frmDt = "04/01/" + Integer.toString(finYear);
			toDt = "06/30/" + Integer.toString(finYear);
			break;
		case 1:
			frmDt = "07/01/" + Integer.toString(finYear);
			toDt = "09/30/" + Integer.toString(finYear);
			break;
		case 2:
			frmDt = "10/01/" + Integer.toString(finYear);
			toDt = "12/31/" + Integer.toString(finYear);
			break;
		case 3:
			frmDt = "01/01/" + Integer.toString(finYear);
			toDt = "31/03/" + Integer.toString(finYear);
			break;
		default:
			break;

		}
		FinanceDate fromDate = new FinanceDate(frmDt);
		FinanceDate toDate = new FinanceDate(toDt);
		return new FinanceDate[] { fromDate, toDate };
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private String getValidAddress(Address address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	private String getDeductorNameAddress(ClientTDSDeductorMasters deductor) {
		String toToSet;
		String buildingName;
		if (deductor.getBuildingName() != null)
			buildingName = deductor.getBuildingName();
		else
			buildingName = "";

		String cityName;
		if (deductor.getCity() != null)
			cityName = deductor.getCity();
		else
			cityName = "";

		String roadName;
		if (deductor.getRoadName() != null)
			roadName = deductor.getRoadName();
		else
			roadName = "";

		String stateName;
		if (deductor.getState() != null)
			stateName = deductor.getState();
		else
			stateName = "";

		String pinCOde = "";
		if (deductor.getPinCode() != 0)
			pinCOde = Long.toString(deductor.getPinCode());

		toToSet = deductor.getDeductorName() + "\n" + buildingName + "\n"
				+ cityName + "\n " + roadName + "\n" + stateName + "\n "
				+ pinCOde;
		return toToSet;
	}

}
