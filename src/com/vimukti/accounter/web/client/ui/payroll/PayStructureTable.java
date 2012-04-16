package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
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
				return messages.effectiveFrom();
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
					ClientPayHead payHead = row.getPayHead();
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
			this.addColumn(new PayHeadColumn() {
				@Override
				protected void setValue(ClientPayStructureItem row,
						ClientPayHead newValue) {
					row.setPayHead(newValue);
					update(row);
				}
			});
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
				ClientPayHead payHead = row.getPayHead();
				// if (payHead != null) {
				// return "" + payHead.getCalculationPeriod();
				// }
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
				ClientPayHead payHead = row.getPayHead();
				if (payHead != null) {
					return ClientPayHead.getPayHeadType(payHead.getType());
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
				ClientPayHead payHead = row.getPayHead();
				if (payHead != null) {
					return ClientPayHead.getCalculationType(payHead
							.getCalculationType());
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
				ClientPayHead payHead = row.getPayHead();
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

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientPayStructureItem row : getRows()) {
			if (row.getRate() == 0) {
				result.addError(row, "Rate should not be zero");
				return result;
			}
		}
		return result;
	}

	public List<ClientPayStructureItem> getRows() {
		List<ClientPayStructureItem> rows = new ArrayList<ClientPayStructureItem>();

		for (ClientPayStructureItem row : getAllRows()) {
			if (!row.isEmpty()) {
				rows.add(row);
			}
		}
		return rows;
	}

}
