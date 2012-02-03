package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.TAXItemDetail;
import com.vimukti.accounter.web.client.ui.reports.TAXItemExceptionDetailReport;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TAXHistoryGrid extends AbstractTransactionGrid<ClientTAXReturn> {

	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
			ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_LINK };
	private TaxHistoryView taxHistoryView;
	private ClientCurrency currency = getCompany().getPrimaryCurrency();

	public TAXHistoryGrid(TaxHistoryView taxHistoryView,
			boolean isMultiselectionEnable) {
		super(isMultiselectionEnable);
		this.taxHistoryView = taxHistoryView;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {

	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 1:
		case 2:
		case 3:
			return 120;
		case 4:
			return 80;
		case 5:
			return 130;
		case 6:
			return 120;

		}
		return -1;

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxAgency(), messages.periodStartDate(),
				messages.periodEndDate(), messages.taxFiledDate(),
				messages.taxAmount(), messages.totalPaymentMade(),
				messages.report() };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onClick(ClientTAXReturn obj, int row, int index) {
		taxHistoryView.taxReturnSelected(obj);
	}

	private List<VATDetail> getVATDetailsByBoxes(ClientTAXReturn clientVATReturn) {
		List<VATDetail> vatDetails = new ArrayList<VATDetail>();

		List<ClientBox> boxes = clientVATReturn.getBoxes();

		for (ClientBox c : boxes) {

			VATDetail vatDetail = new VATDetail();
			vatDetail.setBoxName(c.getName());
			vatDetail.setBoxNumber(c.getBoxNumber());
			vatDetail.setNetAmount(c.getAmount());
			vatDetails.add(vatDetail);
		}

		return vatDetails;

	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTAXReturn obj, int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getColumnValue(ClientTAXReturn obj, int index) {
		switch (index) {

		case 0:
			return getCompany().getTaxAgency(obj.getTaxAgency()).getName();
		case 1:
			return new ClientFinanceDate(obj.getPeriodStartDate()).toString();
		case 2:
			return new ClientFinanceDate(obj.getPeriodEndDate());
		case 3:
			return new ClientFinanceDate(obj.getTransactionDate());
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					obj.getTotalTAXAmount(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					(obj.getTotalTAXAmount() - obj.getBalance()), currency);
		case 6:
			return messages.exceptionDetails();
		default:
			break;
		}
		return null;
	}

	@Override
	protected boolean isEditable(ClientTAXReturn obj, int row, int index) {

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDoubleClick(ClientTAXReturn obj, int row, int col) {
		if (col == 6) {

			ICountryPreferences countryPreferences = Accounter.getCompany()
					.getCountryPreferences();
			if (countryPreferences instanceof UnitedKingdom
					&& countryPreferences.isVatAvailable()) {
				List<VATDetail> vatDetails = new ArrayList<VATDetail>();
				vatDetails = getVATDetailsByBoxes(obj);
				for (VATDetail detail : vatDetails) {
					detail.setStartDate(new ClientFinanceDate(obj
							.getPeriodStartDate()));
					detail.setEndDate(new ClientFinanceDate(obj
							.getPeriodEndDate()));
				}

				ActionFactory.getVATExceptionDetailsReportAction().run(
						vatDetails, obj.getID(), true);
			} else {

				List<ClientTAXReturnEntry> taxEntries = null;
				List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();
				ClientTAXReturn clientTAXReturn = (ClientTAXReturn) obj;
				taxEntries = clientTAXReturn.getTaxReturnEntries();
				details = getExceptionDetailData(taxEntries,
						clientTAXReturn.getPeriodStartDate());

				TAXItemExceptionDetailReport taxItemExceptionDetailReportAction = ActionFactory
						.getTaxItemExceptionDetailReportAction();
				taxItemExceptionDetailReportAction
						.setTaxReturn(clientTAXReturn);
				for (TAXItemDetail detail : details) {
					detail.setStartDate(new ClientFinanceDate(obj
							.getPeriodStartDate()));
					detail.setEndDate(new ClientFinanceDate(obj
							.getPeriodEndDate()));
				}
				taxItemExceptionDetailReportAction.run(details, obj.getID(),
						true);
			}
		} else {
			ICountryPreferences countryPreferences = Accounter.getCompany()
					.getCountryPreferences();
			if (countryPreferences instanceof UnitedKingdom
					&& countryPreferences.isVatAvailable()) {
				List<VATSummary> summaries = getSummaires(obj);
				ActionFactory.getVATSummaryReportAction().run(summaries, false);
			} else {
				List<ClientTAXReturnEntry> taxEntries = null;
				List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();
				taxEntries = obj.getTaxReturnEntries();
				details = getData(taxEntries);
				ActionFactory.getTaxItemDetailReportAction().run(details,
						obj.getID(), true);

			}
		}
	}

	private List<TAXItemDetail> getExceptionDetailData(
			List<ClientTAXReturnEntry> taxEntries, long taxReturnStartDate) {

		List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		for (ClientTAXReturnEntry c : taxEntries) {

			ClientFinanceDate transactionDate = new ClientFinanceDate(
					c.getTransactionDate());
			ClientFinanceDate startDate = new ClientFinanceDate(
					taxReturnStartDate);

			// if (c.getTransactionDate() >= taxReturnStartDate) {
			// continue;
			// }

			if (transactionDate.before(startDate)) {

				TAXItemDetail detail = new TAXItemDetail();
				detail.setTaxAmount(c.getTaxAmount());
				detail.setTransactionId(c.getTransaction());
				detail.setTaxItemName(getCompany().getTAXItem(c.getTaxItem())
						.getName());
				detail.setTransactionType(c.getTransactionType());
				detail.setTransactionDate(new ClientFinanceDate(c
						.getTransactionDate()));
				detail.setNetAmount(c.getNetAmount());
				detail.setTAXRate(getCompany().getTaxItem(c.getTaxItem())
						.getTaxRate());
				detail.setTotal(c.getGrassAmount());
				detail.setFiledTAXAmount(c.getFiledTAXAmount());
				details.add(detail);
			}
			// else{
			// continue;
			// }

		}
		return details;
	}

	private List<TAXItemDetail> getData(List<ClientTAXReturnEntry> taxEntries) {

		List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		for (ClientTAXReturnEntry c : taxEntries) {
			TAXItemDetail detail = new TAXItemDetail();
			detail.setTaxAmount(c.getTaxAmount());
			detail.setTransactionId(c.getTransaction());
			detail.setTaxItemName(getCompany().getTAXItem(c.getTaxItem())
					.getName());
			detail.setTransactionType(c.getTransactionType());
			detail.setTransactionDate(new ClientFinanceDate(c
					.getTransactionDate()));
			detail.setNetAmount(c.getNetAmount());
			detail.setTAXRate(getCompany().getTaxItem(c.getTaxItem())
					.getTaxRate());
			detail.setTotal(c.getGrassAmount());
			details.add(detail);
		}
		return details;
	}

	private List<VATSummary> getSummaires(ClientTAXReturn data) {

		List<VATSummary> result = new ArrayList<VATSummary>();
		for (ClientBox c : data.getBoxes()) {
			VATSummary summary = new VATSummary();
			summary.setStartDate(new ClientFinanceDate(data
					.getPeriodStartDate()));
			summary.setEndDate(new ClientFinanceDate(data.getPeriodEndDate()));
			summary.setName(c.getName());
			summary.setValue(c.getAmount());
			result.add(summary);
		}

		return result;
	}

	protected void onSelectionChanged(ClientTAXReturn obj, int row,
			boolean isChecked) {
		taxHistoryView.taxReturnSelected(obj);
	}
}
