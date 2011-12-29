package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsView;

public class TdsChalanTransactionItemsTable extends
		EditTable<ClientTDSTransactionItem> {

	private ICurrencyProvider currencyProvider;
	private final TDSChalanDetailsView tdsChalanDetails;
	protected boolean isSelected;

	public TdsChalanTransactionItemsTable(
			TDSChalanDetailsView tdsChalanDetailsView) {
		tdsChalanDetails = tdsChalanDetailsView;
	}

	@Override
	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTDSTransactionItem row) {
				isSelected = value;
				if (value) {
					row.setBoxSelected(true);
					tdsChalanDetails.setSurchargeValuesToField(
							row.getSurchargeAmount(), true);
					tdsChalanDetails.setEduCessValuesToField(row.getEduCess(),
							true);
					tdsChalanDetails.setTaxAmountValuesToField(
							row.getTaxAmount(), true);
				} else {
					row.setBoxSelected(false);
					tdsChalanDetails.setSurchargeValuesToField(
							row.getSurchargeAmount(), false);
					tdsChalanDetails.setEduCessValuesToField(row.getEduCess(),
							false);
					tdsChalanDetails.setTaxAmountValuesToField(
							row.getTaxAmount(), false);
				}

			}
		});

		TextEditColumn<ClientTDSTransactionItem> vendorNameColumn = new TextEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected String getValue(ClientTDSTransactionItem row) {
				return Accounter.getCompany().getVendor(row.getVendorID())
						.getName();
			}

			@Override
			protected void setValue(ClientTDSTransactionItem row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected String getColumnName() {
				return "Deductee Name";
			}
		};
		this.addColumn(vendorNameColumn);

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return "Amount paid/credit";
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				// TODO Auto-generated method stub
				return row.getTotalAmount();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				// TODO Auto-generated method stub

			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return "TDS Amount";
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getTaxAmount();

			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				// TODO Auto-generated method stub

			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return true;
			}

			@Override
			protected String getColumnName() {
				return "Surcharge Amount";
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getSurchargeAmount();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				row.setSurchargeAmount(value);

				row.setTdsTotal(row.getTaxAmount() + value + row.getEduCess());
				getTable().update(row);
				if (isSelected) {
					tdsChalanDetails.setSurchargeValuesToField(value, true);
				}

			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return true;
			}

			@Override
			protected String getColumnName() {
				return "Education Cess";
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getEduCess();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				row.setEduCess(value);
				row.setTdsTotal(row.getTaxAmount() + value
						+ row.getSurchargeAmount());
				getTable().update(row);
				if (isSelected) {
					tdsChalanDetails.setEduCessValuesToField(value, true);
				}
			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return "Total Tax";
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getTdsTotal();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				// TODO Auto-generated method stub

			}

		});
		TextEditColumn<ClientTDSTransactionItem> dateColumn = new TextEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected String getValue(ClientTDSTransactionItem row) {
				ClientFinanceDate date = new ClientFinanceDate(
						row.getTransactionDate());
				return date.toString();
			}

			@Override
			protected void setValue(ClientTDSTransactionItem row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected String getColumnName() {
				return "Date of payment";
			}
		};
		this.addColumn(dateColumn);

	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
