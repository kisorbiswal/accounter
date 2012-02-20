package com.vimukti.accounter.web.client.imports;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.server.imports.Importer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ImportView extends AbstractBaseView<Importer<?>> {
	private VerticalPanel importerMatchingPanel;
	private Map<String, Field<?>> fields = new HashMap<String, Field<?>>();
	private VerticalPanel refreshMatchedPanel;
	private FlexTable matchTable;
	private Map<String, String> map;

	/**
	 * Contains Map<Column,AllValues>
	 */
	private Map<String, List<String>> importData = new HashMap<String, List<String>>();
	private int importType;

	public ImportView(int importType, Map<String, List<String>> data) {
		this.importType = importType;
		this.importData = data;
	}

	@Override
	public void init() {
		super.init();
		input();
		createControls();
	}

	private void input() {
		fields.put("number", new Field<String>("HHHH", ""));
		fields.put("name", new Field<String>("GGGG", ""));
		fields.put("balance", new Field<String>("IIII", ""));
		fields.put("amount", new Field<String>("JJJJ", ""));
		fields.put("cost", new Field<String>("KKKK", ""));
		fields.put("item", new Field<String>("LLLL", ""));
		fields.put("s_item", new Field<String>("MMMM", ""));
	}

	private void createControls() {
		HorizontalPanel hPanel = new HorizontalPanel();
		this.importerMatchingPanel = new VerticalPanel();
		refreshMatchedPanel = new VerticalPanel();
		matchTable = new FlexTable();
		createStatementMatchingBody();
		hPanel.add(importerMatchingPanel);
		hPanel.add(this.refreshMatchedPanel);
		refreshMatchedPanel.add(matchTable);
		add(hPanel);
	}

	private void createStatementMatchingBody() {
		FlexTable table = new FlexTable();
		map = getCurrentLine();
		int rowIndex = 0;
		for (final Entry<String, String> entry : map.entrySet()) {
			Label columnName = new Label(entry.getKey());
			Label columnValue = new Label(entry.getValue());
			final ListBox importerFields = new ListBox();
			importerFields.addItem(messages.unassigned());
			for (Field<?> f : fields.values()) {
				importerFields.addItem(f.getName());
			}
			final int currentRowIndex = rowIndex;
			importerFields.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					String selectedValue = importerFields
							.getValue(importerFields.getSelectedIndex());
					String value = entry.getValue();
					Field<?> tempField = fields.get(selectedValue);

					if (validateFieldValue(selectedValue) && tempField != null) {
						if (tempField.validate(value)) {
							tempField.setColumnName(entry.getKey());
							tempField.setName(selectedValue);
							importFieldSelected(currentRowIndex, tempField);
						} else {
							Accounter.showError(messages.pleaseMapProperly(
									selectedValue, value));
						}
					} else {
						importerFields.setSelectedIndex(0);
					}
				}
			});

			table.setWidget(rowIndex, 0, columnName);
			table.setWidget(rowIndex, 1, columnValue);
			table.setWidget(rowIndex, 2, importerFields);
			rowIndex++;
		}
		importerMatchingPanel.add(table);

	}

	protected boolean validateFieldValue(String selectedValue) {
		Collection<Field<?>> values = fields.values();
		for (Field<?> field : values) {
			if (field.getName().equals(selectedValue)) {
				Accounter.showError(messages.propertyMatchedAlready());
				return false;
			}
		}
		return true;
	}

	protected void importFieldSelected(int currentRowIndex, Field<?> tempField) {
		refreshMatchPanel(currentRowIndex, tempField.getName());
	}

	protected void refreshMatchPanel(int rowIndex, String fieldName) {
		Label namelabel = new Label(fieldName);
		Label vallabel = new Label(fieldName);
		matchTable.setWidget(rowIndex, 0, namelabel);
		matchTable.setWidget(rowIndex, 1, vallabel);
	}

	private Map<String, String> getCurrentLine() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("invoice_number", "1");
		map.put("invoice_name", "INV_1");
		map.put("invoice_balance", "551");
		map.put("invoice_amount", "4441");
		map.put("invoice_cost", "441");
		map.put("invoice_item", "Item_11");
		map.put("invoice_s_item", "S_Item_1");
		return map;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
