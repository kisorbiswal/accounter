package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientETDSFilling;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ETdsCellTable extends CellTable<ClientETDSFilling> {

	ListDataProvider<ClientETDSFilling> listDataProvider = new ListDataProvider<ClientETDSFilling>();

	public ETdsCellTable() {
		createControls();
		this.addStyleName("budget-table");
	}

	private void createControls() {

		setPageSize(50);
		listDataProvider.addDataDisplay(this);
		// List<ClientETDSFilling> list = listDataProvider.getList();
		// ClientETDSFilling empty = new ClientETDSFilling();
		// for (int i = 0; i < 5; i++) {
		// list.add(empty);
		// }

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
	}

	private void initTableColumns() {

		Column<ClientETDSFilling, String> serialNoColumn = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Integer.toString(object.getSerialNo());

			}
		};
		serialNoColumn
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> bankBsrCodeColumn = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Long.toString(object.getBankBSRCode());

			}
		};
		bankBsrCodeColumn
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> dateTaxDepositedColumn = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return null;

			}
		};
		dateTaxDepositedColumn
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> chalanSerialCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Long.toString(object.getChalanSerialNumber());

			}
		};
		chalanSerialCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> sectionCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getSectionForPayment();

			}
		};
		sectionCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> tdsTotalCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getTotalTDSfordeductees());

			}
		};
		tdsTotalCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> deducteePanCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Accounter.getCompany().getVendor(object.getDeducteeID())
						.getPanNumber();

			}
		};
		deducteePanCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> deducteeNameCOl = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Accounter.getCompany().getVendor(object.getDeducteeID())
						.getName();

			}
		};
		deducteeNameCOl
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> paymentDateCOl = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Long.toString(object.getDateOFpayment());

			}
		};
		paymentDateCOl
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> amountPaidCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getAmountPaid());

			}
		};
		amountPaidCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> TDSCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getTds());

			}
		};
		TDSCol.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

			@Override
			public void update(int index, ClientETDSFilling object, String value) {
				// TODO Auto-generated method stub

			}

		});

		Column<ClientETDSFilling, String> surchargeCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getSurcharge());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> educationCessCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getEducationCess());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> totalTaxDeductedCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getTotalTaxDEducted());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> totalTaxDepositedCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Double.toString(object.getTotalTaxDeposited());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> dateDeductionCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Long.toString(object.getDateofDeduction());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> deducteeCodeCol = new Column<ClientETDSFilling, String>(
				new SelectionCell(getListValues())) {

			@Override
			public String getValue(ClientETDSFilling object) {
				// TODO Auto-generated method stub
				return null;
			}

		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		// SelectionCell deducteeCodeCol = new SelectionCell(null);

		Column<ClientETDSFilling, String> rateCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return Integer.toString(object.getTaxRate());

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientETDSFilling, String> bookEntryCol = new Column<ClientETDSFilling, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientETDSFilling object) {
				return object.getBookEntry();

			}
		};
		surchargeCol
				.setFieldUpdater(new FieldUpdater<ClientETDSFilling, String>() {

					@Override
					public void update(int index, ClientETDSFilling object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		this.setColumnWidth(serialNoColumn, "100px");
		this.setColumnWidth(bankBsrCodeColumn, "100px");
		this.setColumnWidth(dateTaxDepositedColumn, "100px");
		this.setColumnWidth(chalanSerialCol, "100px");
		this.setColumnWidth(sectionCol, "100px");
		this.setColumnWidth(tdsTotalCol, "150px");
		this.setColumnWidth(deducteePanCol, "100px");
		this.setColumnWidth(deducteeNameCOl, "100px");
		this.setColumnWidth(paymentDateCOl, "100px");
		this.setColumnWidth(amountPaidCol, "100px");
		this.setColumnWidth(TDSCol, "100px");
		this.setColumnWidth(surchargeCol, "100px");
		this.setColumnWidth(educationCessCol, "100px");
		this.setColumnWidth(totalTaxDeductedCol, "100px");
		this.setColumnWidth(totalTaxDepositedCol, "100px");
		this.setColumnWidth(dateDeductionCol, "100px");
		this.setColumnWidth(deducteeCodeCol, "150px");
		this.setColumnWidth(rateCol, "100px");
		this.setColumnWidth(bookEntryCol, "100px");

		this.addColumn(serialNoColumn, "Sr. No.");
		this.addColumn(bankBsrCodeColumn, "Bank BSR Code");
		this.addColumn(dateTaxDepositedColumn, "Date Tax deposited");
		this.addColumn(chalanSerialCol, "Chalan Serial NO.");
		this.addColumn(sectionCol, "Section for payment");
		this.addColumn(tdsTotalCol,
				"Total TDS to be allocated among all deductees");
		this.addColumn(deducteePanCol, "PAN of Deductee");
		this.addColumn(deducteeNameCOl, "Deductee Name");
		this.addColumn(paymentDateCOl, "Date of Payment/Credit");
		this.addColumn(amountPaidCol, "Amount Paid/Credited");
		this.addColumn(TDSCol, "TDS");
		this.addColumn(surchargeCol, "Surcharge");
		this.addColumn(educationCessCol, "Education Cess");
		this.addColumn(totalTaxDeductedCol, "Total Tax Deducted");
		this.addColumn(totalTaxDepositedCol, "Total Tax Deposited");
		this.addColumn(dateDeductionCol, "Date of Deduction");
		this.addColumn(deducteeCodeCol,
				"Deductee Code(01-Company/02-other than Company)");
		this.addColumn(rateCol, "Rate at which Tax Deducted");
		this.addColumn(bookEntryCol, "Paid by book entry or otherwise");

	}

	private List<String> getListValues() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("01");
		list.add("02");

		return list;
	}

	public void sortRowData(int columnIndex, boolean isAscending) {
		redraw();
	}

	/*
	 * private void openLinkAction(final ClientETDSFilling object) {
	 * HashMap<String, String> map = new HashMap<String, String>(); String
	 * budgetTitle = "Add Budget for " + object.getAccountsName();
	 * AddBudgetAmountDialogue assignAccountsTo1099Dialog = new
	 * AddBudgetAmountDialogue( budgetTitle, "", map, object);
	 * assignAccountsTo1099Dialog .setCallback(new
	 * ActionCallback<HashMap<String, String>>() {
	 * 
	 * @Override public void actionResult(HashMap<String, String> result) {
	 * refreshView(result, object);
	 * 
	 * } }); assignAccountsTo1099Dialog.show(); }
	 */

	public List<ClientETDSFilling> getDataList() {
		List<ClientETDSFilling> list = listDataProvider.getList();
		return list;
	}

	public void setDataProvidedValue(ArrayList<ClientETDSFilling> eTDSList) {

		List<ClientETDSFilling> list = listDataProvider.getList();
		// list.removeAll(list);

		for (ClientETDSFilling clientETDSFilling : eTDSList) {
			list.add(clientETDSFilling);
		}
	}

}
