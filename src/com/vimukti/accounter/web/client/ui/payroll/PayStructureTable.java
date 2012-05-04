package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class PayStructureTable extends EditTable<ClientPayStructureItem> {

	private AmountColumn<ClientPayStructureItem> rateColumn;
	private PayHeadColumn payheadColumn;

	public PayStructureTable() {
		super();
		addEmptyRecords();
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
				long effectiveFrom = row.getEffectiveFrom();
				ClientFinanceDate date = new ClientFinanceDate(effectiveFrom);
				if (effectiveFrom == 0) {
					date = new ClientFinanceDate();
					setValue(row, date);
				}
				return date;
			}

			@Override
			protected void setValue(ClientPayStructureItem row,
					ClientFinanceDate value) {
				row.setEffectiveFrom(value.getDate());
			}

			@Override
			protected String getColumnName() {
				return messages.effectiveFrom();
			}

			@Override
			public int getWidth() {
				return 100;
			}

		});

		payheadColumn = new PayHeadColumn() {
			@Override
			protected void setValue(ClientPayStructureItem row,
					ClientPayHead newValue) {

				if (newValue.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
					rateColumn.setEnable(row, false);
				} else if (newValue.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
						|| newValue.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
					ClientAttendancePayHead ph = (ClientAttendancePayHead) newValue;
					if (ph.getAttendanceType() == ClientAttendancePayHead.ATTENDANCE_ON_RATE) {
						rateColumn.setEnable(row, true);
					} else {
						rateColumn.setEnable(row, false);
					}
				} else {
					rateColumn.setEnable(row, true);
				}
				row.setPayHead(newValue.getID());
				row.setClientPayHead(newValue);
				update(row);
			}
		};
		this.addColumn(payheadColumn);

		rateColumn = new AmountColumn<ClientPayStructureItem>(null, false) {

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

			@Override
			public int getWidth() {
				return 50;
			}
		};
		this.addColumn(rateColumn);

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = payheadColumn.getPayHead(row
						.getPayHead());
				if (payHead == null) {
					return "";
				}
				int type = 0;
				if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
					ClientAttendancePayHead payhead = ((ClientAttendancePayHead) payHead);
					type = payhead.getCalculationPeriod();
				} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
					ClientComputionPayHead payhead = ((ClientComputionPayHead) payHead);
					type = payhead.getCalculationPeriod();
				} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
					ClientFlatRatePayHead payhead = ((ClientFlatRatePayHead) payHead);
					type = payhead.getCalculationPeriod();
				}
				return ClientPayHead.getCalculationPeriod(type);
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

			@Override
			public int getWidth() {
				return 120;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = payheadColumn.getPayHead(row
						.getPayHead());
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

			@Override
			public int getWidth() {
				return 180;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = payheadColumn.getPayHead(row
						.getPayHead());
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

			@Override
			public int getWidth() {
				return 150;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayStructureItem>() {

			@Override
			protected String getValue(ClientPayStructureItem row) {
				ClientPayHead payHead = payheadColumn.getPayHead(row
						.getPayHead());
				if (payHead != null
						&& payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
					ClientComputionPayHead payhead = (ClientComputionPayHead) payHead;
					if (payhead.getComputationType() != ClientComputionPayHead.COMPUTATE_ON_SPECIFIED_FORMULA) {
						return ClientComputionPayHead
								.getComputationType(payhead
										.getComputationType());
					} else {
						for (ClientComputaionFormulaFunction formula : payhead
								.getFormulaFunctions()) {
							formula.setClientPayHead(payheadColumn
									.getPayHead(formula.getPayHead()));
						}
						return UIUtils.prepareFormula(payhead
								.getFormulaFunctions());
					}
				}
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

			@Override
			public int getWidth() {
				return 130;
			}
		});

		this.addColumn(new DeleteColumn<ClientPayStructureItem>());
	}

	@Override
	protected boolean isInViewMode() {
		return false;
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

	public ValidationResult validate(ValidationResult validationResult) {
		List<ClientPayStructureItem> allRows = super.getAllRows();
		for (ClientPayStructureItem item : allRows) {
			if ((item.getPayHead() != 0
					&& isRateRequired(payheadColumn.getPayHead(item
							.getPayHead())) && item.getRate() == 0)) {
				validationResult.addError(this,
						messages.pleaseEnter(messages.rate()));
				return validationResult;
			}
		}
		for (int i = 0; i < allRows.size(); i++) {
			if (i + 1 < allRows.size()) {
				ClientPayStructureItem item1 = allRows.get(i + 1);
				if (isExists(item1.getPayHead(), i + 1)) {
					validationResult.addError(this,
							messages.duplicatePayheadsExist());
					return validationResult;
				}

			}
		}
		return validationResult;
	}

	private boolean isRateRequired(ClientPayHead payhead) {
		if (payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
			return false;
		} else if (payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
				|| payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
			ClientAttendancePayHead ph = (ClientAttendancePayHead) payhead;
			if (ph.getAttendanceType() == ClientAttendancePayHead.ATTENDANCE_ON_RATE) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private boolean isExists(long payhead, int i) {
		for (int j = 0; j < getAllRows().size(); j++) {
			if (i == j) {
				continue;
			}
			ClientPayStructureItem item = getAllRows().get(j);

			if (item.getPayHead() != 0 && item.getPayHead() == payhead) {
				return true;
			}
		}
		return false;
	}

}
