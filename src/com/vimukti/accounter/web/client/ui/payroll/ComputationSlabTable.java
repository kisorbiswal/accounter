package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientComputationSlab;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.StringListColumn;

public class ComputationSlabTable extends EditTable<ClientComputationSlab> {

	private List<String> slabTypes = new ArrayList<String>();

	public ComputationSlabTable() {
		super();
		slabTypes.add("Percentage");
		slabTypes.add("Value");
		addEmptyRowAtLast();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new DateColumn<ClientComputationSlab>() {

			@Override
			protected ClientFinanceDate getValue(ClientComputationSlab row) {
				return row.getEffectiveFrom();
			}

			@Override
			protected void setValue(ClientComputationSlab row,
					ClientFinanceDate value) {
				row.setEffectiveFrom(value);
			}

			@Override
			protected String getColumnName() {
				return messages.effectiveFrom();
			}

		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getFromAmount();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setFromAmount(value);
			}

			@Override
			protected String getColumnName() {
				return messages.fromAmount();
			}
		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getToAmount();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setToAmount(value);
			}

			@Override
			protected String getColumnName() {
				return messages.amountUpto();
			}
		});

		this.addColumn(new StringListColumn<ClientComputationSlab>() {

			@Override
			protected String getValue(ClientComputationSlab row) {
				return slabTypes.get(row.getSlabType());
			}

			@Override
			protected List<String> getData() {
				return slabTypes;
			}

			@Override
			protected void setValue(ClientComputationSlab row, String newValue) {
				row.setSlabType(slabTypes.indexOf(newValue));
			}

			@Override
			protected String getColumnName() {
				return messages.slabType();
			}
		});

		this.addColumn(new AmountColumn<ClientComputationSlab>(null, false) {

			@Override
			protected Double getAmount(ClientComputationSlab row) {
				return row.getValue();
			}

			@Override
			protected void setAmount(ClientComputationSlab row, Double value) {
				row.setValue(value);
			}

			@Override
			protected String getColumnName() {
				return messages.valueBasis();
			}
		});

		this.addColumn(new DeleteColumn<ClientComputationSlab>());
	}

	@Override
	public void addEmptyRowAtLast() {
		ClientComputationSlab item = new ClientComputationSlab();
		add(item);
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
