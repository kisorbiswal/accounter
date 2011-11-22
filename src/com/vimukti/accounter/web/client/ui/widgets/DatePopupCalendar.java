package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class DatePopupCalendar extends PopupPanel {

	private Grid daysGrid;
	private DatePicker datePicker;
	private Button okButton;
	private Button cancelButton;
	private Button nextButton, prevButton;
	String months[][] = { { "Jan", "July" }, { "Feb", "Aug" },
			{ "Mar", "Sep" }, { "Apr", "Oct" }, { "May", "Nov" },
			{ "Jun", "Dec" } };
	private int year = 2011;
	private String month;

	public DatePopupCalendar(DatePicker datePicker) {

		this.addStyleName("blue" + "-date-picker");
		this.daysGrid = new Grid(6, 4);
		this.datePicker = datePicker;
		VerticalPanel panel = new VerticalPanel();
		this.add(panel);
		sinkEvents(Event.ONBLUR);
		nextButton(panel);
		drawButtons(panel);
		drawGrid(panel);
		this.addStyleName("nextmonths-years");

	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONBLUR:
			DatePopupCalendar.this.hide();
			break;
		default:
			break;

		}
		super.onBrowserEvent(event);
	}

	private void nextButton(VerticalPanel panel) {
		prevButton = new Button("-");
		prevButton.setWidth("60%");
		nextButton = new Button("+");
		nextButton.setWidth("60%");
	}

	private void drawButtons(final VerticalPanel panel) {
		okButton = new Button("Ok");
		okButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {

				datePicker.getSelectedDate().setYear(getYear() - 1900);
				datePicker.getSelectedDate().setMonth(
						new DateUtills().evaluateMonthFormat(month) - 1);
				datePicker.synchronizeFromDate();
				datePicker.showPopup();
				hide();

			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panel.remove(daysGrid);
				setYear(getYear() + 1);
				drawGrid(panel);

			}
		});
		prevButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				panel.remove(daysGrid);
				setYear(getYear() - 1);
				drawGrid(panel);
			}
		});

	}

	private void drawGrid(VerticalPanel panel) {
		daysGrid.setStyleName("blue" + "-" + "day-grid");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				daysGrid.setText(i, j, months[i][j]);
			}
		}

		daysGrid.setWidget(0, 2, prevButton);
		daysGrid.setWidget(0, 3, nextButton);

		int k = getYear();
		for (int i = 1; i < 6; i++) {
			for (int j = 2; j < 4; j++) {
				daysGrid.setText(i, j, k + "");
				k++;
			}

		}
		setYear(k);

		this.daysGrid.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = daysGrid.getCellForEvent(event);
				int rowIndex = cell.getRowIndex();
				int cellIndex = cell.getCellIndex();
				CellFormatter cfJours = daysGrid.getCellFormatter();
				if (cellIndex < 2) {
					for (int i = 0; i < 6; i++) {
						for (int j = 0; j < 2; j++) {
							cfJours.removeStyleName(i, j, "blue" + "-"
									+ "current-month-selected");

						}
					}
					month = daysGrid.getText(rowIndex, cellIndex);

				} else {
					for (int i = 1; i < 6; i++) {
						for (int j = 2; j < 4; j++) {
							cfJours.removeStyleName(i, j, "blue" + "-"
									+ "current-month-selected");

						}
					}
					String n = daysGrid.getText(rowIndex, cellIndex).trim();
					if (!n.isEmpty() && !n.equals("+") && !n.equals("-")) {
						setYear(Integer.parseInt(n));
					}

				}
				if ((rowIndex == 0 && cellIndex == 2)
						|| (rowIndex == 0 && cellIndex == 3)) {

				} else {
					cfJours.addStyleName(rowIndex, cellIndex, "blue" + "-"
							+ "current-month-selected");
				}

			}

		});
		panel.add(daysGrid);
		HorizontalPanel h = new HorizontalPanel();

		h.setSpacing(10);
		h.setWidth("100%");
		h.add(okButton);

		h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		h.add(cancelButton);
		h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		h.setCellHorizontalAlignment(okButton,
				HasHorizontalAlignment.ALIGN_LEFT);
		h.setCellHorizontalAlignment(cancelButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(h);
		h.addStyleName("prenextmonth-okcancel");

	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
