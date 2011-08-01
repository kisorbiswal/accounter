/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.customers.InvoiceView;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;

/**
 * @author Murali.A
 * 
 */
public class InvoicePrintLayout extends VerticalPanel {

	private ClientInvoice invoice;
	private InvoiceView invoiceView;
	private HorizontalPanel vatPanel;
	private Double vatTotal;
	private double totalAmount;
	private DialogBox dialog;
	private Double taxTotal;

	public InvoicePrintLayout(ClientInvoice invoice) {
		this.invoice = invoice;
	}

	public void setView(InvoiceView invoiceView) {
		this.invoiceView = invoiceView;
	}

	public void createTemplate() {
		VerticalPanel mainVPanel = new VerticalPanel();
		PrintTemplateUtils util = new PrintTemplateUtils();

		List<String> removeHeaderBackground = new ArrayList<String>();

		String cmpAdd = "<br/><br/><br/><br/><br/>";
		ClientAddress registeredAddress = getCompany().getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd = "<br><font size=\"2\">&nbsp;"
					+ registeredAddress.getAddress1() + ",<br/>&nbsp;"
					+ registeredAddress.getStreet() + ",<br/>&nbsp;"
					+ registeredAddress.getCity() + ",<br/>&nbsp;"
					+ registeredAddress.getStateOrProvinence() + ",<br/>&nbsp;"
					+ registeredAddress.getZipOrPostalCode() + ",<br/>&nbsp;"
					+ registeredAddress.getCountryOrRegion() + ".</font></br>";
		}

		HTML compLab = new HTML();
		compLab.setHTML("<p style=\"margin-bottom:12px;\"><font color=\"black\" size=\"5\"><strong> "
				+ getCompany().getName().replace(
						getCompany().getName().charAt(0) + "",
						(getCompany().getName().charAt(0) + "").toUpperCase())
				+ "</strong></font>" + cmpAdd + "</p>");

		Map<String, String> dateNumForm = getMap(new String[] {
				Accounter.constants().invoiceDate(),
				UIUtils.dateFormat(new ClientFinanceDate(invoice
						.getTransactionDate())),
				Accounter.messages().invoiceNumber(), invoice.getNumber() + "",
				Accounter.constants().orderNumber(), invoice.getOrderNum(),
				Accounter.constants().customerNumber(),
				getCompany().getCustomer(invoice.getCustomer()).getNumber() });

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setSize("300px", "100%");
		FlexTable table = util.getWidget1(4, 2, dateNumForm, true,
				removeHeaderBackground);
		table.setSize("100%", "100%");
		datepanel.add(table);

		HTML lab1 = new HTML();
		lab1.setHTML(Accounter.messages().labelHTML().asString() + datepanel);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setSize("100%", "100%");
		labeldateNoLayout.add(compLab);
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.setCellHorizontalAlignment(compLab,
				HasHorizontalAlignment.ALIGN_LEFT);
		labeldateNoLayout.setCellHorizontalAlignment(lab1,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel adressHPanel = new HorizontalPanel();
		adressHPanel.setSize("100%", "100%");
		adressHPanel.setSpacing(10);
		adressHPanel.getElement().setAttribute("cellpaddding", "1");

		ClientCustomer customer = getCompany().getCustomer(
				invoice.getCustomer());
		String billAdrs = "<br/><br/><br/><br/><br/>";
		String shpAdrs1 = "<br/><br/><br/><br/><br/>";
		// Set<ClientAddress> bill = customer.getAddress();
		// for (ClientAddress add : bill) {
		// if (add.getType() == ClientAddress.TYPE_BILL_TO) {
		ClientAddress bill = invoice.getBillingAddress();
		if (bill != null) {
			billAdrs = "<p align=\"left\"><font size=\"3\">"
					+ bill.getAddress1() + ",<br/>" + bill.getStreet()
					+ ",<br/>" + bill.getCity() + ",<br/>"
					+ bill.getStateOrProvinence() + ",<br/>"
					+ bill.getZipOrPostalCode() + ",<br/>"
					+ bill.getCountryOrRegion() + ".</font></p>";
		}

		// Set<ClientAddress> add = customer.getAddress();
		// for (ClientAddress shpAdres : add) {
		// if (shpAdres.getType() == ClientAddress.TYPE_SHIP_TO) {
		ClientAddress shpAdres = invoice.getShippingAdress();
		if (shpAdres != null) {
			shpAdrs1 = "<p align=\"left\"><font size=\"3\">"
					+ shpAdres.getAddress1() + ",<br/>" + shpAdres.getStreet()
					+ ",<br/>" + shpAdres.getCity() + ",<br/>"
					+ shpAdres.getStateOrProvinence() + ",<br/>"
					+ shpAdres.getZipOrPostalCode() + ",<br/>"
					+ shpAdres.getCountryOrRegion() + ".</font></p>";
		}

		Map<String, String> billAdrsMap = getMap("Bill To", billAdrs);
		Map<String, String> shpAdrsMap = getMap("Ship To", shpAdrs1);

		FlexTable billToTable = util.getWidget(2, 1, billAdrsMap, true);
		billToTable.setSize("280px", "100%");
		adressHPanel.add(billToTable);
		FlexTable shipToTable = util.getWidget(2, 1, shpAdrsMap, true);
		shipToTable.setSize("280px", "100%");
		adressHPanel.add(shipToTable);
		adressHPanel.setCellHorizontalAlignment(billToTable,
				HasHorizontalAlignment.ALIGN_LEFT);
		adressHPanel.setCellHorizontalAlignment(shipToTable,
				HasHorizontalAlignment.ALIGN_RIGHT);

		ClientSalesPerson salesPerson = getCompany().getSalesPerson(
				invoice.getSalesPerson());
		ClientPaymentTerms paymtnTerm = getCompany().getPaymentTerms(
				invoice.getPaymentTerm());
		String paymentTermName = paymtnTerm != null ? paymtnTerm.getName() : "";
		String salesPersname = salesPerson != null ? salesPerson.getName() : "";
		ClientShippingMethod shipMtd = getCompany().getShippingMethod(
				invoice.getShippingMethod());
		String shipMtdName = shipMtd != null ? shipMtd.getName() : "";

		ClientShippingTerms shipingterm = getCompany().getShippingTerms(
				invoice.getShippingTerm());
		String shipterm = shipingterm != null ? shipingterm.getName() : "";

		Map<String, String> detailsMap = getMap(Accounter.constants()
				.salesPerson(), salesPersname + "<br/>", Accounter.constants()
				.shippingMethod(), shipMtdName + "<br/>", Accounter.constants()
				.shippingTerm(), shipterm + "<br/>");

		HorizontalPanel detailsPanel = new HorizontalPanel();
		detailsPanel.setSize("100%", "100%");
		FlexTable detailsTable = util.getWidget(2, 3, detailsMap, true);
		detailsPanel.add(detailsTable);
		detailsTable.setSize("100%", "100%");

		Map<List<String>, Map<Integer, List<String>>> gridData = getGridDataMap();
		VerticalPanel gridPanel = new VerticalPanel();
		FlexTable grid = util.getGridWidget(gridData, true);
		grid.setSize("100%", "100%");
		gridPanel.add(grid);
		gridPanel.setSize("100%", "100%");

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatPanel = new HorizontalPanel();

			vatPanel.setSize("100%", "100%");
			List<String> footer1Headers = new ArrayList<String>();
			footer1Headers.add("VAT%");
			footer1Headers.add(Accounter.constants().VATAmount());
			FlexTable tr = util.getFooterWidget(invoiceView
					.getGridForPrinting().getVATDetailsMapForPrinting(),
					footer1Headers);
			tr.getElement().setAttribute("style",
					"border-left:1px ridge ;border-bottom:1px ridge;");
			tr.setSize("100%", "100%");
			// vatPanel.add(tr);
		}

		HTML message = new HTML();
		message.setHTML("" + invoice.getMemo() + "");
		vatPanel.add(message);
		vatPanel.setCellWidth(message, "73.2%");
		vatPanel.setCellHorizontalAlignment(message,
				HasHorizontalAlignment.ALIGN_LEFT);

		double lineTotal = invoiceView.getGridForPrinting().getTotal();

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotal = invoiceView.getGridForPrinting().getVatTotal();
			totalAmount = lineTotal + vatTotal;

			Map<String, String> footer2Map = new LinkedHashMap<String, String>();
			footer2Map = getMap(
					Accounter.constants().subTotal(),
					"<p align=\"right\">"
							+ splitString(DataUtils
									.getAmountAsString(lineTotal)) + "</p>",
					Accounter.constants().VATTotal(),
					"<p align=\"right\">"
							+ splitString(DataUtils.getAmountAsString(vatTotal))
							+ "</p>",
					"TOTAL",
					"<p align=\"right\" class=\"gridHeaderBackGround\">"
							+ splitString(DataUtils
									.getAmountAsString(totalAmount)) + "</p>");
			// footer2Map.put("0",
			// "<strong>Sub Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(lineTotal));
			// footer2Map.put("1",
			// "<strong>VAT Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(vatTotal));
			// footer2Map.put("2",
			// "<strong>Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(totalAmount));
			removeHeaderBackground.add(Accounter.constants().subTotal());
			removeHeaderBackground.add(Accounter.constants().VATTotal());

			FlexTable totalsTabel = util.getWidget1(3, 2, footer2Map, true,
					removeHeaderBackground);
			totalsTabel.setCellPadding(3);
			totalsTabel.setSize("100%", "100%%");
			vatPanel.add(totalsTabel);
			vatPanel.setCellHorizontalAlignment(totalsTabel,
					HasHorizontalAlignment.ALIGN_RIGHT);

		} else {
			taxTotal = invoiceView.getSalesTax();
			totalAmount = lineTotal + taxTotal;

			Map<String, String> footer2Map = new LinkedHashMap<String, String>();
			footer2Map = getMap(
					Accounter.constants().subTotal(),
					"<p align=\"right\">"
							+ splitString(DataUtils
									.getAmountAsString(lineTotal)) + "</p>",
					Accounter.constants().totalTax(),
					"<p align=\"right\">"
							+ splitString(DataUtils.getAmountAsString(taxTotal))
							+ "</p>",
					Accounter.constants().total(),
					"<p align=\"right\"  class=\"gridHeaderBackGround\">"
							+ splitString(DataUtils
									.getAmountAsString(totalAmount)) + "</p>");
			// footer2Map.put("0",
			// "<strong>Sub Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(lineTotal));
			// footer2Map.put("1",
			// "<strong>Total Tax</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(taxTotal));
			// footer2Map
			// .put(
			// "2",
			// "<strong>Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			// + DataUtils.getAmountAsString(totalAmount));
			removeHeaderBackground.add(Accounter.constants().subTotal());
			removeHeaderBackground.add(Accounter.constants().totalTax());

			FlexTable totalsTable = util.getWidget1(3, 2, footer2Map, true,
					removeHeaderBackground);
			totalsTable.setSize("100%", "100%");
			totalsTable.setCellPadding(3);
			vatPanel.add(totalsTable);
			vatPanel.setCellHorizontalAlignment(totalsTable,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}
		vatPanel.setBorderWidth(1);
		gridPanel.add(vatPanel);

		mainVPanel.add(labeldateNoLayout);
		adressHPanel.setStyleName(Accounter.constants().shiftBottom());
		mainVPanel.add(adressHPanel);
		detailsPanel.setStyleName(Accounter.constants().shiftBottom());
		mainVPanel.add(detailsPanel);
		gridPanel.setStyleName(Accounter.constants().shiftBottom());
		mainVPanel.add(gridPanel);

		add(mainVPanel);
		setSize("100%", "100%");
	}

	/**
	 * @return
	 */
	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	private Map<List<String>, Map<Integer, List<String>>> getGridDataMap() {

		AbstractTransactionGrid<ClientTransactionItem> grid = invoiceView
				.getGridForPrinting();
		List<String> headerNames = Arrays.asList(grid
				.getColumnNamesForPrinting());

		Map<List<String>, Map<Integer, List<String>>> gridData = new LinkedHashMap<List<String>, Map<Integer, List<String>>>();
		int i = 0;
		List<String> row;
		Map<Integer, List<String>> rows = new LinkedHashMap<Integer, List<String>>();
		for (ClientTransactionItem item : grid.getRecords()) {
			row = new ArrayList<String>();
			for (int col = 0; col < headerNames.size(); col++) {
				String val = grid.getColumnValueForPrinting(item, col);
				row.add(splitString(val));
			}
			rows.put(new Integer(i), row);
			i++;
		}
		gridData.put(headerNames, rows);
		return gridData;
	}

	private Map<String, String> getMap(String... args) {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0)
				dataMap.put(args[i], "");
			else {
				dataMap.put(args[i - 1], args[i]);
			}

		}
		return dataMap;
	}

	public void print() {
		if (this == null) {
			UIUtils.say(Accounter.constants().noDataToPrint());
			return;
		} else
			it("", this.getElement().getInnerHTML());
	}

	public void it(String style, String it) {
		String style1 = "FinancePrint.css";
		String html = "<table border=\"0\" align=\"center\" width=\"100%\"><tfoot><tr><td align=\"center\"  width=\"100%\">"
				+ getFooterWidgets()
				+ "</tfoot><tbody height=\"100%\"><tr><td width=\"100%\">"
				+ it
				+ "</td></tr></tbody></table>";
		System.out.println(html);
		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("htmltoconvert")).append("=")
				.append(URL.encode(html)).append("&")
				.append(URL.encode("cssfile")).append("=")
				.append(URL.encode(style1));

		String url = GWT.getModuleBaseURL() + postData;
		// UIUtils.downloadAttachment(this.invoice.getID());
		// this.doPost("/do/finance/generatePDFServlet", postData.toString());
		// PrintUtils.it(html);
	}

	public void doPost(String url, String postData) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		try {

			Request response = builder.sendRequest(postData,
					new RequestCallback() {

						public void onError(Request request, Throwable exception) {
						}

						public void onResponseReceived(Request request,
								Response response) {
						}
					});
		} catch (RequestException e) {
			Window.alert(Accounter.constants().failedToSendTheRequest()
					+ e.getMessage());
		}
	}

	private String splitString(String val) {
		if (val != null && val.length() > 9) {
			String split[] = val.split(" ");
			val = "";
			for (int j = 0; j < split.length; j++) {
				if (split[j].length() > 9)
					val = val
							+ "  "
							+ getStringBy9char(split[j].substring(8),
									split[j].substring(0, 8));
				else
					val = val + "  " + split[j];
			}
		}

		return val;
	}

	public String getStringBy9char(String val, String valueTobesend) {
		int length = val.length();
		int l = 8;
		if (length > 9) {
			valueTobesend = valueTobesend + "  " + val.substring(0, 8);
			valueTobesend = valueTobesend + "  "
					+ getStringBy9char(val.substring(l++), valueTobesend);
		} else {
			valueTobesend = valueTobesend + "  " + val;
		}
		return valueTobesend;
	}

	public VerticalPanel getFooterWidgets() {

		VerticalPanel Vpanel = new VerticalPanel();
		PrintTemplateUtils util = new PrintTemplateUtils();

		Map<String, String> detailsMap = getMap("VAT No : "
				+ getCompany().getpreferences().getVATregistrationNumber(), ""
				+ "<br/>", Accounter.constants().sortCodeColon()
				+ getCompany().getSortCode(), "" + "<br/>", Accounter
				.constants().bankAccountNumberColon()
				+ getCompany().getBankAccountNo(), "" + "<br/>");

		FlexTable vatTable = util.getThinBorderWidget(2, 3, detailsMap, true);
		Vpanel.add(vatTable);
		vatTable.setSize("100%", "100%");

		String regAdd = "&nbsp;";
		ClientAddress tradingAddress = getCompany().getTradingAddress();
		if (tradingAddress != null) {
			regAdd = tradingAddress.getAddress1() + ",&nbsp;"
					+ tradingAddress.getStreet() + ",&nbsp;"
					+ tradingAddress.getCity() + ",&nbsp;"
					+ tradingAddress.getStateOrProvinence() + ",&nbsp;"
					+ tradingAddress.getZipOrPostalCode() + ",&nbsp;"
					+ tradingAddress.getCountryOrRegion() + ".";
		}

		HorizontalPanel Hpanel = new HorizontalPanel();
		Map<String, String> regAddMap = getMap(getCompany().getName() + ", "
				+ getCompany().getRegistrationNumber() + ", " + regAdd);

		FlexTable table = util.getThinBorderWidget(1, 0, regAddMap, true);
		table.setSize("100%", "100%");
		Hpanel.add(table);

		Hpanel.setStyleName("shiftBottom");
		Hpanel.setSize("100%", "100%");

		Vpanel.add(Hpanel);
		Vpanel.setStyleName("shiftBottom");
		Vpanel.setSize("100%", "100%");

		return Vpanel;
	}
}
