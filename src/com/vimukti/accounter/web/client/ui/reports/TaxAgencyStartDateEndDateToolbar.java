package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class TaxAgencyStartDateEndDateToolbar extends ReportToolbar {

	public DateItem fromItem;
	public DateItem toItem;

	TAXAgencyCombo taxAgencyCombo;
	ClientTAXAgency selectedAgency;
	private Button updateButton;

	public TaxAgencyStartDateEndDateToolbar() {
		createControls();
	}

	private void createControls() {

		String[] reportBasisArray = { Accounter.constants().cash(),
				Accounter.constants().accrual() };

		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants()
				.selectTaxAgency(), false);
		taxAgencyCombo.setHelpInformation(true);
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {
							selectedAgency = selectItem;
							setStartDate(fromItem.getDate());
							setEndDate(toItem.getDate());
							reportRequest();
						}

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setTitle(Accounter.constants().from());
		fromItem.setEnteredDate(new ClientFinanceDate());

		toItem = new DateItem();
		toItem.setHelpInformation(true);

		toItem.setEnteredDate(new ClientFinanceDate());
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		toItem.setTitle(Accounter.constants().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);

			}
		});
		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());

				reportRequest();

				setSelectedDateRange(Accounter.constants().custom());
			}

		});

		Button printButton = new Button(Accounter.constants().print());

		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		addItems(taxAgencyCombo, fromItem, toItem);
		add(updateButton);
		List<ClientTAXAgency> vatAgencies = Accounter.getCompany()
				.getActiveTAXAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(
					AccounterClientConstants.DEFAULT_VAT_AGENCY_NAME))
				taxAgencyCombo.addItemThenfireEvent(vatAgency);
		}
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);

	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setEnteredDate(startDate);
		toItem.setEnteredDate(endDate);
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		// TODO Auto-generated method stub

	}

	private void reportRequest() {
		if (this.reportview != null)
			this.reportview.makeReportRequest(this.selectedAgency.getID(),
					startDate, endDate);

	}

}
