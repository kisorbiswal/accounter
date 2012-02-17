package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ImportView extends AbstractBaseView<Importer<?>> {

	private SimplePanel importerMatchingPanel;
	private List<Importer<?>> importerLines = new ArrayList<Importer<?>>();
	private Map<String, Field<?>> fields = new HashMap<String, Field<?>>();
	private VerticalPanel mapPreviewPanel;
	private FlexTable previewTable, mapTable;
	private Map<String, String> map;
	private Button nextButton, prevButton;

	@Override
	public void init() {
		super.init();
		input();
		createControls();
	}

	private void createControls() {
		FlexTable mainTable = new FlexTable();
		// left-part...
		this.importerMatchingPanel = new SimplePanel();
		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("import-button-panel");
		importerMatchingPanel.addStyleName("import-mapping-panel");
		prevButton = new Button(messages.previous());
		nextButton = new Button(messages.next());
		prevButton.addStyleName("prev_button");
		nextButton.addStyleName("next_button");
		nextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createStatementMatchingBody();
			}
		});
		prevButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createStatementMatchingBody();
			}
		});

		buttonPanel.add(prevButton);
		buttonPanel.add(nextButton);

		// right-part....
		mapPreviewPanel = new VerticalPanel();
		mapPreviewPanel.addStyleName("import-preview-panel");
		previewTable = new FlexTable();
		Label previewHeader = new Label(messages.mappingPreview());
		previewHeader.addStyleName("preview-header");
		previewTable.setCellSpacing(10);
		mapPreviewPanel.add(previewHeader);
		mapPreviewPanel.add(previewTable);
		createStatementMatchingBody();

		// adding fields to main table
		mainTable.setWidget(1, 0, buttonPanel);
		mainTable.setWidget(2, 0, importerMatchingPanel);
		mainTable.setWidget(2, 1, this.mapPreviewPanel);

		add(mainTable);
		buttonPanel.getElement().getParentElement()
				.setAttribute("width", "500px");
		mainTable.setSize("100%", "100%");

	}

	private void createStatementMatchingBody() {
		importerMatchingPanel.clear();
		mapTable = new FlexTable();
		mapTable.setCellSpacing(6);
		mapTable.addStyleName("import-match-content");
		map = getCurrentLine();
		int rowIndex = 0;

		Label csvHeader = new Label(messages.nameInCSV());
		Label valueHeader = new Label(messages.fieldValue());
		Label accounterNameHeader = new Label(messages.accounterField());

		csvHeader.addStyleName("table-header");
		valueHeader.addStyleName("table-header");
		accounterNameHeader.addStyleName("table-header");

		mapTable.setWidget(0, 0, csvHeader);
		mapTable.setWidget(0, 1, valueHeader);
		mapTable.setWidget(0, 2, accounterNameHeader);

		for (final Entry<String, String> entry : map.entrySet()) {
			Label columnName = new Label(entry.getKey());
			Label columnValue = new Label(entry.getValue());
			final ListBox importerFields = new ListBox();
			importerFields.addItem(messages.unassigned());
			int i = 0;
			for (String f : fields.keySet()) {
				importerFields.addItem(f);
				Field<?> field = fields.get(f);
				if (field.getColumnName() != null
						&& field.getColumnName().equals(entry.getKey())) {
					importerFields.setSelectedIndex(i);
					if (!field.validate(entry.getValue())) {
						Accounter.showError(messages.pleaseMapProperly(
								field.getName(), entry.getValue()));
					}
				}
				i++;
			}
			initMatchedPanel();
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

			mapTable.setWidget(rowIndex + 1, 0, columnName);
			mapTable.setWidget(rowIndex + 1, 1, columnValue);
			mapTable.setWidget(rowIndex + 1, 2, importerFields);
			rowIndex++;
		}
		importerMatchingPanel.add(mapTable);

	}

	private void input() {
		fields.put("number", new Field<String>(""));
		fields.put("name", new Field<String>(""));
		fields.put("balance", new Field<String>(""));
		fields.put("amount", new Field<String>(""));
		fields.put("cost", new Field<String>(""));
		fields.put("item", new Field<String>(""));
		fields.put("s_item", new Field<String>(""));
	}

	private void initMatchedPanel() {
		if (!(map.isEmpty())) {
			int j = 0;
			for (String fieldName : fields.keySet()) {
				previewTable.setText(j, 0, fieldName);
				refreshMatchPanel(j, fields.get(fieldName));
				j++;
			}
		}
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
		refreshMatchPanel(currentRowIndex, tempField);
	}

	protected void refreshMatchPanel(int rowIndex, Field<?> tempField) {
		String value;
		if (tempField.getValue() != null
				&& ((String) tempField.getValue()).length() != 0) {
			value = (String) tempField.getValue();
		} else {
			value = messages.unassigned();
		}
		Label valueLabel=new Label(value);
		previewTable.setWidget(rowIndex, 1, valueLabel);
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
