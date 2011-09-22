package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.Calendar;

public class GraphChart {

	ArrayList<Double> graph_Values = new ArrayList<Double>();
	ArrayList<Object> x_Axis_Labels = new ArrayList<Object>();

	public static final int BANK_ACCOUNT_CHART_TYPE = 1;
	public static final int ACCOUNTS_RECEIVABLE_CHART_TYPE = 2;
	public static final int ACCOUNTS_PAYABLE_CHART_TYPE = 3;
	public static final int EXPENSE_CHART_TYPE = 4;

	int chartType = 0;
	int MAX_REVENUE = 0;
	int MIN_REVENUE = 0;
	int WIDTH = 0;
	int HEIGHT = 0;

	public void initializeGraphValues() {
		if (this.graph_Values == null)
			graph_Values = new ArrayList<Double>();
		for (int i = 0; i <= 30; i++)
			graph_Values.add(i, 0.0);

		// graph_Values.add(1, 500.08);
		// graph_Values.add(2, 1000.37);
		// graph_Values.add(3, 400.);
		// graph_Values.add(4, 800.);
		// graph_Values.add(5, 400.);
		// graph_Values.add(6, 200.00);
		// graph_Values.add(7, 300.00);
		// graph_Values.add(8, 100.00);
		// graph_Values.add(9, 500.);
		// graph_Values.add(10, 800.);
		// graph_Values.add(11, 400.);
		// graph_Values.add(12, 100.00);
		// graph_Values.add(13, 500.);
		// graph_Values.add(14, 800.);
		// graph_Values.add(15, 400.);
		// graph_Values.add(16, 200.00);
		// graph_Values.add(17, 300.00);
		// graph_Values.add(18, 100.00);
		// graph_Values.add(19, 500.);
		// graph_Values.add(20, 800.);
		// graph_Values.add(21, 300.00);
		// graph_Values.add(22, 100.00);
		// graph_Values.add(23, 500.);
		// graph_Values.add(24, 800.);
		// graph_Values.add(25, 400.);
		// graph_Values.add(26, 200.00);
		// graph_Values.add(27, 300.00);
		// graph_Values.add(28, 100.00);
		// graph_Values.add(29, 500.);
		// graph_Values.add(30, 800.);

	}

	public LineChart createBankingChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(BANK_ACCOUNT_CHART_TYPE);
		LineChart chart = new LineChart(createTable(BANK_ACCOUNT_CHART_TYPE),
				createOptionsToBankingChart());
		return chart;
	}

	public ColumnChart createAccountReceivableChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNTS_RECEIVABLE_CHART_TYPE);
		ColumnChart chart = new ColumnChart(
				createTable(ACCOUNTS_RECEIVABLE_CHART_TYPE),
				createOptionsToAccountReceivableChart());
		return chart;
	}

	public LineChart createAccountPayableChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNTS_PAYABLE_CHART_TYPE);
		LineChart chart = new LineChart(
				createTable(ACCOUNTS_PAYABLE_CHART_TYPE),
				createOptionsToAccountPayableChart());
		return chart;
	}

	private Options createOptionsToAccountReceivableChart() {

		Options options = Options.create();
		options.setWidth(444);
		options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");
		// options.set3D(true);

		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private Options createOptionsToBankingChart() {
		Options options = Options.create();
		options.setWidth(430);
		options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");
		// options.setSmoothLine(true);
		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);
		return options;
	}

	private Options createOptionsToAccountPayableChart() {

		Options options = Options.create();

		// options.setDisplayAnnotations(true);
		// options.setDisplayLegendValues(false);
		// options.setDisplayZoomButtons(false);
		// options.setDisplayExactValues(true);
		// options.setAllowHtml(true);
		// options.setWindowMode(WindowMode.OPAQUE);
		options.setWidth(425);
		options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");

		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private AbstractDataTable createTable(int chartType) {
		DataTable data = DataTable.create();

		if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {
			data.addColumn(ColumnType.STRING, Accounter.constants().date());
			data.addColumn(ColumnType.NUMBER);

		} else {
			data.addColumn(ColumnType.STRING, Accounter.constants().date());
			data.addColumn(ColumnType.NUMBER, Accounter.constants().revenue());
		}

		if (chartType == BANK_ACCOUNT_CHART_TYPE) {
			data.addRows(4);
			data = addGraphPoints(chartType, data, 4);
		}

		if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {
			data.addRows(6);
			data = addGraphPoints(chartType, data, 6);
		}

		if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {
			data.addRows(30);
			data = addGraphPoints(chartType, data, 30);
		}

		return data;
	}

	private DataTable addGraphPoints(int chartType, DataTable data, int size) {
		for (int i = 0; i < size; i++) {

			// if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE)
			// data.setValue(i, 0, (String) x_Axis_Labels.get(i));
			// else
			data.setValue(i, 0, (String) x_Axis_Labels.get(i));

			data.setValue(i, 1, graph_Values.get(i));
		}
		return data;
	}

	private void createLabels(int chartType) {

		ClientFinanceDate date = new ClientFinanceDate();

		if (chartType == BANK_ACCOUNT_CHART_TYPE) {

			Calendar cal = Calendar.getInstance(); // GregorianCalendar();
			cal.setTime(date.getDateAsObject());
			int labelDateVal = cal.getTime().getDate();
			int labelMonthVal = cal.getTime().getMonth();

			for (int i = 3; i >= 0; i--) {

				/*
				 * creating x-axis labels. Ex: Jan-01, Jan-03, Jan-05, ... Here
				 * x-axis label are combination of date and month. We using date
				 * values at each two intervals like date 1, 3, 5..
				 */
				labelDateVal = ((date.getDay()) - (i));

				if (labelDateVal > 0)
					labelMonthVal = date.getMonth();

				if (labelDateVal < 0 && labelMonthVal == date.getMonth()) {
					labelMonthVal = labelMonthVal - 1;
					cal.set(Calendar.MONDAY, labelMonthVal);
				}

				if (labelMonthVal != date.getMonth())
					labelDateVal = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
							+ labelDateVal;

				x_Axis_Labels.add(3 - i, getMonthAsString(labelMonthVal-1) + " "
						+ labelDateVal);
			}
		}

		// Graph for Accounts Receivable
		if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {

			// For Account Receivable we have to show 6 points in graph
			for (int i = 6; i > 0; i--) {

				// creating x-axis labels. Ex: June, May, Apr, ...
				x_Axis_Labels.add(6 - i, getMonthAsString(date.getMonth() - i
						+ 1));
			}
		}

		// Graph for Accounts Payable
		if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {

			int labelDateVal = date.getDay();
			int labelMonthVal = date.getMonth();

			for (int i = 0; i < 30; i++) {

				if ((labelMonthVal == 0 || labelMonthVal == 2
						|| labelMonthVal == 4 || labelMonthVal == 6
						|| labelMonthVal == 7 || labelMonthVal == 9 || labelMonthVal == 11)
						&& labelDateVal > 31) {

					labelMonthVal++;
					if (labelDateVal == 32)
						labelDateVal = 1;
					else
						labelDateVal = labelDateVal
								- (date.getDay() + ((i - 1) * 2));

				} else if ((labelMonthVal == 3 || labelMonthVal == 5
						|| labelMonthVal == 8 || labelMonthVal == 10)
						&& labelDateVal > 30) {

					labelMonthVal++;
					if (labelDateVal == 31)
						labelDateVal = 1;
					else
						labelDateVal = labelDateVal
								- (date.getDay() + ((i - 1) * 2));

				} else if (labelMonthVal == 1 && date.getYear() % 4 == 0
						&& labelDateVal > 29) {

					labelMonthVal++;
					if (labelDateVal == 30)
						labelDateVal = 1;
					else
						labelDateVal = labelDateVal
								- (date.getDay() + ((i - 1) * 2));

				} else if (labelMonthVal == 1 && date.getYear() % 4 != 0
						&& labelDateVal > 28) {

					labelMonthVal++;
					if (labelDateVal == 29)
						labelDateVal = 1;
					else
						labelDateVal = labelDateVal
								- (date.getDay() + ((i - 1) * 2));
				}

				// creating x-axis labels. Ex: 1-Jan, 3-Jan, 5-Jan, ...
				x_Axis_Labels.add(i, getMonthAsString(labelMonthVal-1) + " "
						+ labelDateVal);

				labelDateVal = labelDateVal + 1;
			}
		}

	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 0:
			return Accounter.constants().jan();
		case 1:
			return Accounter.constants().feb();
		case 2:
			return Accounter.constants().mar();
		case 3:
			return Accounter.constants().apr();
		case 4:
			return Accounter.constants().may();
		case 5:
			return Accounter.constants().jun();
		case 6:
			return Accounter.constants().jul();
		case 7:
		case -5:
			return Accounter.constants().aug();
		case 8:
		case -4:
			return Accounter.constants().sept();
		case 9:
		case -3:
			return Accounter.constants().oct();
		case 10:
		case -2:
			return Accounter.constants().nov();
		case 11:
		case -1:
			return Accounter.constants().dec();

		}
		return "";

	}

}
