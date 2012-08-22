package com.vimukti.accounterbb.ui;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.vimukti.accounterbb.result.Record;
import com.vimukti.accounterbb.result.ResultList;
import com.vimukti.accounterbb.utils.GuiUtills;
import com.vimukti.accounterbb.utils.RecordCellsField;

public class ResultListField extends VerticalFieldManager {

	private ResultList result;
	private RecordSelectionListener listener;

	public ResultListField(ResultList result, RecordSelectionListener listener) {
		this.result = result;
		this.listener = listener;
		create();
	}

	private void create() {
		VerticalFieldManager groupManager = GuiUtills.getMainLayout();
		Vector records = result.getRecords();

		for (int j = 0; j < records.size(); j++) {

			final Record record = (Record) records.elementAt(j);

			RecordCellsField recordField = new RecordCellsField(record);
			recordField.setListener(listener);
			groupManager.add(recordField);

			recordField.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					recordSelected(record);
				}
			});

		}

		this.add(groupManager);
	}

	protected void recordSelected(Record record) {
		listener.recordSelected(result, record);

	}

	public void setSelectionListener(RecordSelectionListener listener) {
		this.listener = listener;
	}

//	protected boolean navigationClick(int status, int time) {
//		fieldChangeNotify(0);
//		return true;
//	}
}
