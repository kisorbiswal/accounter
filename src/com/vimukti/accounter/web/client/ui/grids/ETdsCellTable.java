package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientETDSFilling;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.BankDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class ETdsCellTable extends EditTable<ClientETDSFilling> {

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

	@Override
	protected void initColumns() {

		TextEditColumn<ClientETDSFilling> serialNoColumn = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Integer.toString(object.getSerialNo());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> bankBsrCodeColumn = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getBankBSRCode();

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> dateTaxDepositedColumn = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DateUtills.getDateAsString(object.getDateTaxDeposited());
			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> chalanSerialCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Long.toString(object.getChalanSerialNumber());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> sectionCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getSectionForPayment();

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> tdsTotalCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getTotalTDSfordeductees());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> deducteePanCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getPanOfDeductee();

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
				row.setPanOfDeductee(value);
			}

			@Override
			protected String getColumnName() {
				return "PAN of Deductee";
			}

		};

		TextEditColumn<ClientETDSFilling> deducteeNameCOl = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Accounter.getCompany().getVendor(object.getDeducteeID())
						.getName();

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> paymentDateCOl = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DateUtills.getDateAsString(object.getDateOFpayment());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> amountPaidCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object.getAmountPaid());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> TDSCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object.getTds());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> surchargeCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object.getSurcharge());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> educationCessCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object.getEducationCess());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> totalTaxDeductedCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDEducted());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> totalTaxDepositedCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDeposited());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> dateDeductionCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DateUtills.getDateAsString(object.getDateofDeduction());

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		ComboColumn<ClientETDSFilling, ClientBank> deducteeCodeCol = new ComboColumn<ClientETDSFilling, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFilling object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return "Deductee Code(01-Company/02-other than Company)";
			}

			@Override
			protected void setValue(ClientETDSFilling row, ClientBank newValue) {
				row.setCompanyCode(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFilling row) {
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

		ComboColumn<ClientETDSFilling, ClientBank> remarkCol = new ComboColumn<ClientETDSFilling, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFilling object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return "Remark(Reason for non-Deduction/lower deduction/higher deduction/threshold)";
			}

			@Override
			protected void setValue(ClientETDSFilling row, ClientBank newValue) {
				row.setRemark(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFilling row) {
				return remarkCodeTable;
			}

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		// SelectionCell deducteeCodeCol = new SelectionCell(null);

		TextEditColumn<ClientETDSFilling> rateCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return DataUtils.getAmountAsStrings(object.getTaxRate());
			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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

		TextEditColumn<ClientETDSFilling> bookEntryCol = new TextEditColumn<ClientETDSFilling>() {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getBookEntry();

			}

			@Override
			protected void setValue(ClientETDSFilling row, String value) {
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
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
