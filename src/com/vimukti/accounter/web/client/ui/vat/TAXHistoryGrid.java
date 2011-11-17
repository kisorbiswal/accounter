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

	public TAXHistoryGrid(boolean isMultiselectionEnable) {
		super(isMultiselectionEnable);
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;

	}

	@Override
	protected String[] getColumns() {
		return new String[] { companyConstants.taxAgency(),
				companyConstants.periodStartDate(),
				companyConstants.periodEndDate(),
				companyConstants.vatFileDate(), companyConstants.taxDue(),
				companyConstants.totalPaymentMade(), companyConstants.report() };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onClick(ClientTAXReturn obj, int row, int index) {
		if (index == 5) {

			ICountryPreferences countryPreferences = Accounter.getCompany()
					.getCountryPreferences();
			if (countryPreferences instanceof UnitedKingdom
					&& countryPreferences.isVatAvailable()) {
				List<VATDetail> vatDetails = new ArrayList<VATDetail>();
				vatDetails = getVATDetailsByBoxes(obj);
				VATDetail vatDetail = new VATDetail();
				vatDetail.setStartDate(new ClientFinanceDate(obj
						.getPeriodStartDate()));
				vatDetail.setEndDate(new ClientFinanceDate(obj
						.getPeriodEndDate()));
				ActionFactory.getVATExceptionDetailsReportAction().run(
						vatDetails, true);
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
				taxItemExceptionDetailReportAction.run(details, true);
			}
		}

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
			return DataUtils.amountAsStringWithCurrency((obj.getBalance()),
					currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					(obj.getTotal() - obj.getBalance()), currency);
		case 6:
			return companyConstants.exceptionDetails();
		default:
			break;
		}
		return null;
	}

	@Override
	protected boolean isEditable(ClientTAXReturn obj, int row, int index) {

		return false;
	}

	public void setTaxHistoryView(TaxHistoryView taxHistoryView) {
		this.taxHistoryView = taxHistoryView;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDoubleClick(ClientTAXReturn obj, int row, int col) {
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
			ActionFactory.getTaxItemDetailReportAction().run(details, true);

		}
	}

	private List<TAXItemDetail> getExceptionDetailData(
			List<ClientTAXReturnEntry> taxEntries, long taxReturnStartDate) {

		List<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		for (ClientTAXReturnEntry c : taxEntries) {
			if (c.getTransactionDate() >= taxReturnStartDate) {
				continue;
			}
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
}
