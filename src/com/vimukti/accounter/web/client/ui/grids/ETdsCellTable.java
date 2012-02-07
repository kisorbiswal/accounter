package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientETDSFillingItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.BankDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class ETdsCellTable extends EditTable<ClientETDSFillingItem> {

	private List<ClientBank> getDeducteeCodes() {

		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName("1");
		ClientBank b = new ClientBank();
		b.setName("2");
		list.add(a);
		list.add(b);
		return list;
	}

	private List<ClientBank> getRemarkValues() {
		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName("A");
		ClientBank b = new ClientBank();
		b.setName("B");
		list.add(a);
		list.add(b);

		return list;
	}

	private List<ClientBank> getGrossingUpIndicators() {
		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName("Yes");
		ClientBank b = new ClientBank();
		b.setName("No");
		list.add(a);
		list.add(b);

		return list;
	}

	@Override
	protected void initColumns() {

		TextEditColumn<ClientETDSFillingItem> serialNoColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Integer.toString(object.getSerialNo());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Sr. No.";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> bankBsrCodeColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getBankBSRCode();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Bank BSR Code";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> dateTaxDepositedColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateTaxDeposited());
			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Date Tax deposited";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> chalanSerialCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Long.toString(object.getChalanSerialNumber());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Chalan Serial NO.";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> sectionCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getSectionForPayment();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Section for payment";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> tdsTotalCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Double.toString(object.getTotalTDSfordeductees());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Total TDS to be allocated among all deductees";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> deducteePanCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getPanOfDeductee();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				row.setPanOfDeductee(value);
			}

			@Override
			protected String getColumnName() {
				return "PAN of Deductee";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

		};

		TextEditColumn<ClientETDSFillingItem> deducteeNameCOl = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Accounter.getCompany().getPayee(object.getDeducteeID())
						.getName();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Deductee Name";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> paymentDateCOl = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateOFpayment());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Date of Payment/Credit";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> amountPaidCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getAmountPaid());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Amount Paid/Credited";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> TDSCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getTds());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "TDS";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> surchargeCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getSurcharge());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Surcharge";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> educationCessCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getEducationCess());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Education Cess";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> totalTaxDeductedCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDEducted());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Total Tax Deducted";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> totalTaxDepositedCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDeposited());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Total Tax Deposited";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> dateDeductionCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateofDeduction());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Date of Deduction";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		final BankDropDownTable deducteeCodeTable = new BankDropDownTable(
				getDeducteeCodes());

		ComboColumn<ClientETDSFillingItem, ClientBank> deducteeCodeCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return "Deductee Code(01-Company/02-other than Company)";
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setCompanyCode(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return deducteeCodeTable;
			}

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		final BankDropDownTable remarkCodeTable = new BankDropDownTable(
				getRemarkValues());

		ComboColumn<ClientETDSFillingItem, ClientBank> remarkCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return "Remark(Reason for non-Deduction/lower deduction/higher deduction/threshold)";
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setRemark(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return remarkCodeTable;
			}

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		// SelectionCell deducteeCodeCol = new SelectionCell(null);

		TextEditColumn<ClientETDSFillingItem> rateCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getTaxRate());
			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Rate at which Tax Deducted";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		TextEditColumn<ClientETDSFillingItem> bookEntryCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getBookEntry();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Paid by book entry or otherwise";
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		final BankDropDownTable grossingUpTable = new BankDropDownTable(
				getGrossingUpIndicators());

		ComboColumn<ClientETDSFillingItem, ClientBank> grossingUpCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return "Grossing up Indicator";
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setGrossingUpIndicator(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return grossingUpTable;
			}

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		this.addColumn(serialNoColumn);
		this.addColumn(bankBsrCodeColumn);
		this.addColumn(dateTaxDepositedColumn);
		this.addColumn(chalanSerialCol);
		this.addColumn(sectionCol);
		this.addColumn(tdsTotalCol);
		this.addColumn(deducteePanCol);
		this.addColumn(deducteeNameCOl);
		this.addColumn(paymentDateCOl);
		this.addColumn(amountPaidCol);
		this.addColumn(TDSCol);
		this.addColumn(surchargeCol);
		this.addColumn(educationCessCol);
		this.addColumn(totalTaxDeductedCol);
		this.addColumn(totalTaxDepositedCol);
		this.addColumn(dateDeductionCol);
		this.addColumn(deducteeCodeCol);
		this.addColumn(remarkCol);
		this.addColumn(rateCol);
		this.addColumn(bookEntryCol);
		if (is27Q()) {
			this.addColumn(grossingUpCol);
		}
	}

	protected abstract boolean is27Q();

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
