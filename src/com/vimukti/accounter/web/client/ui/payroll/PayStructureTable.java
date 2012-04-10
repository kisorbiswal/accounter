package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class PayStructureTable extends EditTable<ClientPayStructureItem> {

	private boolean isPayRun;

	public PayStructureTable(boolean isPayRun) {
		super();
		this.isPayRun = isPayRun;
		if (!isPayRun) {
			addEmptyRecords();
		}
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	protected void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			addEmptyRowAtLast();
		}
	}

	@Override
	public void addEmptyRowAtLast() {
		ClientPayStructureItem item = new ClientPayStructureItem();
		add(item);
	}

	@Override
	protected void initColumns() {
		this.addColumn(new DateColumn<ClientPayStructureItem>() {

			@Override
			protected ClientFinanceDate getValue(ClientPayStructureItem row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setValue(ClientPayStructureItem row,
					ClientFinanceDate value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return "Effective from";
			}

			@Override
			protected boolean isEnable() {
				return !isPayRun;
			}
		});

		if (isPayRun) {
			this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

				@Override
				protected String getValue(ClientPayStructureItem row) {
					ClientPayHead payHead = Accounter.getCompany().getPayHead(
							row.getPayHead());
					if (payHead != null) {
						return payHead.getName();
					}
					return "";
				}

				@Override
				protected void setValue(ClientPayStructureItem row, String value) {
					// TODO Auto-generated method stub
				}

				@Override
				protected String getColumnName() {
					return messages.payhead();
				}

				@Override
				protected boolean isEnable() {
					return false;
				}
			});

		} else {
			this.addColumn(new PayHeadColumn());
		}

		this.addColumn(new AmountColumn<ClientPayStructureItem>(null, false) {

			@Override
			protected Double getAmount(ClientPayStructureItem row) {
				return row.getRate();
			}

			@Override
			protected void setAmount(ClientPayStructureItem row, Double value) {
				row.setRate(value);
			}

			@Override
			protected String getColumnName() {
				return messages.rate();
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = Accounter.getCompany().getPayHead(
						row.getPayHead());
//				if (payHead != null) {
//					return "" + payHead.getCalculationPeriod();
//				}
				return "";
			}

			@Override
			protected void setValue(ClientPayStructureItem row, String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.calculationPeriod();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = Accounter.getCompany().getPayHead(
						row.getPayHead());
				if (payHead != null) {
					return "" + payHead.getType();
				}
				return "";
			}

			@Override
			protected void setValue(ClientPayStructureItem row, String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.payHeadType();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = Accounter.getCompany().getPayHead(
						row.getPayHead());
				if (payHead != null) {
					return "" + payHead.getCalculationType();
				}
				return "";
			}

			@Override
			protected void setValue(ClientPayStructureItem row, String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.calculationType();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = Accounter.getCompany().getPayHead(
						row.getPayHead());
				return "";
			}

			@Override
			protected void setValue(ClientPayStructureItem row, String value) {
				// TODO Auto-generated method stub
			}

			@Override
			protected String getColumnName() {
				return messages.computedOn();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		if (!isPayRun) {
			this.addColumn(new DeleteColumn<ClientPayStructureItem>());
		}
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
