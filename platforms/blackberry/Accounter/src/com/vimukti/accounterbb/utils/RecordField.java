package com.vimukti.accounterbb.utils;

import net.rim.device.api.ui.container.VerticalFieldManager;

import com.vimukti.accounterbb.result.Record;

public class RecordField extends VerticalFieldManager{
	private Record record;

	public RecordField(Record record) {
		this.record = record;
		displayCells();
	}

	private void displayCells() {

			RecordCellsField cellsField = new RecordCellsField(record);
		
		this.add(cellsField);
	}



}
