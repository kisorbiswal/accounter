package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
import com.googlecode.gchart.client.GChart;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.Calendar;

public class GraphChart extends GChart {

	ArrayList<String> x_Axis_Labels;
	ArrayList<Double> graph_Values;

	public static final int BANK_ACCOUNT_CHART_TYPE = 1;
	public static final int ACCOUNTS_RECEIVABLE_CHART_TYPE = 2;
	public static final int ACCOUNTS_PAYABLE_CHART_TYPE = 3;

	int chartType = 0;
	int MAX_REVENUE = 0;
	int WIDTH = 0;
	int HEIGHT = 0;

	public GraphChart() {

		this(1, 1000, 400, 150, new ArrayList<Double>());

		// x_Axis_Labels = new ArrayList<String>();
		// graph_Values = new ArrayList<Double>();

	}

	public GraphChart(int chartType) {
		this(chartType, 1000, 400, 150, new ArrayList<Double>());
	}

	public GraphChart(int chartType, int MAX_REVENUE, int WIDTH, int HEIGHT,
			ArrayList<Double> graph_Values) {

		x_Axis_Labels = new ArrayList<String>();
		this.graph_Values = new ArrayList<Double>();

		this.chartType = chartType;
		this.MAX_REVENUE = MAX_REVENUE + 100;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.graph_Values = graph_Values;

		if (this.graph_Values == null || this.graph_Values.isEmpty())
			intializeGraphValues();

		setChartSize(this.WIDTH, this.HEIGHT);
		createGraph(chartType);
		createLabels(chartType);
	}

	public void intializeGraphValues() {

		graph_Values = new ArrayList<Double>();

		graph_Values.add(0, 200.10);
		graph_Values.add(1, 500.08);
		graph_Values.add(2, 1000.37);
		graph_Values.add(3, 400.);
		graph_Values.add(4, 800.);
		graph_Values.add(5, 400.);
		graph_Values.add(6, 200.00);
		graph_Values.add(7, 300.00);
		graph_Values.add(8, 100.00);
		graph_Values.add(9, 500.);
		graph_Values.add(10, 800.);
		graph_Values.add(11, 400.);
		graph_Values.add(12, 100.00);
		graph_Values.add(13, 500.);
		graph_Values.add(14, 800.);
		graph_Values.add(15, 400.);
		graph_Values.add(16, 200.00);
		graph_Values.add(17, 300.00);
		graph_Values.add(18, 100.00);
		graph_Values.add(19, 500.);
		graph_Values.add(20, 800.);
		graph_Values.add(21, 300.00);
		graph_Values.add(22, 100.00);
		graph_Values.add(23, 500.);
		graph_Values.add(24, 800.);
		graph_Values.add(25, 400.);
		graph_Values.add(26, 200.00);
		graph_Values.add(27, 300.00);
		graph_Values.add(28, 100.00);
		graph_Values.add(29, 500.);
		graph_Values.add(30, 800.);
	}

	public void createGraph(int chartType) {

		addCurve();
		getCurve().getSymbol().setBackgroundColor("Turquoise");

		/*
		 * Creates the Graph to Type of Accounts Receivable Account
		 */
		if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {

			getCurve().getSymbol().setSymbolType(SymbolType.VBAR_SOUTHWEST);
			getCurve().getSymbol().setModelWidth(1.0);
			getCurve().getSymbol().setBorderColor("black");
			getCurve().getSymbol().setBorderWidth(1);

			for (int i = 0; i < 6; i++) {
				getCurve().addPoint((i + 1) * 2, graph_Values.get(i));
				getCurve().getSymbol().setHovertextTemplate(
						GChart.formatAsHovertext("${y}"));
			}

		} else {

			getCurve().getSymbol().setSymbolType(SymbolType.LINE);
			getCurve().getSymbol().setFillThickness(2);
			getCurve().getSymbol().setFillSpacing(1);
			getCurve().getSymbol().setBorderColor("Turquoise");

			/*
			 * Creates the Graph to Type of Banking Accounts
			 */
			if (chartType == BANK_ACCOUNT_CHART_TYPE) {
				for (int i = 0; i < 4; i++) {
					getCurve().addPoint(i * 2, graph_Values.get(i));
					getCurve().getSymbol().setHovertextTemplate(
							GChart.formatAsHovertext("${y}"));
				}
			}

			/*
			 * Creates the Graph to Type of Accounts Payable Account
			 */

			if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {
				for (int i = 0; i < 30; i++)
					getCurve().addPoint(i * 2, graph_Values.get(i));
				getCurve().getSymbol().setHovertextTemplate(
						GChart.formatAsHovertext("${y}"));
			}
		}
	}

	private void createLabels(int chartType) {

		ClientFinanceDate date = new ClientFinanceDate();

		// Graph for Bank Accounts
		if (chartType == BANK_ACCOUNT_CHART_TYPE)
			createLablesToBankingAccounts(date);

		// Graph for Accounts Receivable
		if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE)
			createLablesToAccountsReceivable(date);

		// Graph for Accounts Payable
		if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE)
			createLablesToAccountsPayable(date);

		getXAxis().setTickLength(4);
		getXAxis().setTickLabelFontSize(10);
		getXAxis().setAxisMin(0);
		getXAxis().setHasGridlines(true);

		getYAxis().setAxisMin(0);
		getYAxis().setAxisMax(MAX_REVENUE);
		getYAxis().setTickLabelFormat("#,###");
		getYAxis().setHasGridlines(true);

	}

	private void createLablesToBankingAccounts(ClientFinanceDate date) {

		Calendar cal = Calendar.getInstance();
		int labelDateVal = cal.getTime().getDate();
		int labelMonthVal = cal.getTime().getMonth();

		for (int i = 3; i >= 0; i--) {

			/*
			 * creating x-axis labels. Ex: Jan-01, Jan-03, Jan-05, ... Here
			 * x-axis label are combination of date and month. We using date
			 * values at each two intervals like date 1, 3, 5..
			 */
			labelDateVal = ((date.getDate()) - (i * 2));

			if (labelDateVal > 0)
				labelMonthVal = date.getMonth();

			if (labelDateVal < 0 && labelMonthVal == date.getMonth()) {
				labelMonthVal = labelMonthVal - 1;
				cal.set(Calendar.MONDAY, labelMonthVal);
			}

			if (labelMonthVal != date.getMonth())
				labelDateVal = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
						+ labelDateVal;

			x_Axis_Labels.add(3 - i, getMonthAsString(labelMonthVal) + " "
					+ labelDateVal);

			// adding x-axis labels to x-axis at each point. Ex: points like 0,
			// 2, 4, ...
			getXAxis().addTick((3 - i) * 2, x_Axis_Labels.get(3 - i));
		}
	}

	private void createLablesToAccountsReceivable(ClientFinanceDate date) {

		// For Account Receivable we have to show 6 points in graph
		for (int i = 6; i > 0; i--) {

			// creating x-axis labels. Ex: June, May, Apr, ...
			x_Axis_Labels.add(6 - i, getMonthAsString(date.getMonth() - i + 1));

			// adding x-axis labels to x-axis at each point. Ex: points like 2,
			// 4, 6, ...
			getXAxis().addTick(((6 - i + 1) * 2), x_Axis_Labels.get(6 - i));
		}
	}

	private void createLablesToAccountsPayable(ClientFinanceDate date) {

		int labelDateVal = date.getDate();
		int labelMonthVal = date.getMonth();

		for (int i = 0; i < 30; i++) {

			if ((labelMonthVal == 0 || labelMonthVal == 2 || labelMonthVal == 4
					|| labelMonthVal == 6 || labelMonthVal == 7
					|| labelMonthVal == 9 || labelMonthVal == 11)
					&& labelDateVal > 31) {

				labelMonthVal++;
				if (labelDateVal == 32)
					labelDateVal = 1;
				else
					labelDateVal = labelDateVal
							- (date.getDate() + ((i - 1) * 2));

			} else if ((labelMonthVal == 3 || labelMonthVal == 5
					|| labelMonthVal == 8 || labelMonthVal == 10)
					&& labelDateVal > 30) {

				labelMonthVal++;
				if (labelDateVal == 31)
					labelDateVal = 1;
				else
					labelDateVal = labelDateVal
							- (date.getDate() + ((i - 1) * 2));

			} else if (labelMonthVal == 1 && date.getYear() % 4 == 0
					&& labelDateVal > 29) {

				labelMonthVal++;
				if (labelDateVal == 30)
					labelDateVal = 1;
				else
					labelDateVal = labelDateVal
							- (date.getDate() + ((i - 1) * 2));

			} else if (labelMonthVal == 1 && date.getYear() % 4 != 0
					&& labelDateVal > 28) {

				labelMonthVal++;
				if (labelDateVal == 29)
					labelDateVal = 1;
				else
					labelDateVal = labelDateVal
							- (date.getDate() + ((i - 1) * 2));
			}

			// creating x-axis labels. Ex: 1-Jan, 3-Jan, 5-Jan, ...
			x_Axis_Labels.add(i, (labelDateVal) + " "
					+ getMonthAsString(labelMonthVal));

			// adding x-axis labels to x-axis at each point. Ex: points like 0,
			// 2, 4, ...
			getXAxis().addTick(i * 2, x_Axis_Labels.get(i));

			labelDateVal = labelDateVal + 2;
		}
	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
		case -5:
			return "Aug";
		case 8:
		case -4:
			return "Sept";
		case 9:
		case -3:
			return "Oct";
		case 10:
		case -2:
			return "Nov";
		case 11:
		case -1:
			return "Dec";

		}
		return "";

	}

}
