package com.vimukti.accounter.web.client.imports;

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
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;

public class ImportView extends AbstractBaseView {

	/**
	 * Contains Map<Column,AllValues>
	 */
	private Map<String, List<String>> importData = new HashMap<String, List<String>>();
	private int importType;
	/**
	 * All Fields of the CurrentImporter
	 */
	private Map<String, ImportField> fields = new HashMap<String, ImportField>();

	private FlexTable mappingTable, previewTable, mainTable;
	// contains accounter names and fields..
	private Map<String, ImportField> listBoxMap;

	private int currentLine = 0;
	private String fileID;
	private int currentRow = 0;
	private double noOfRecords;
	Button backButton;
	Button nextButton;

	public ImportView(int importType, String fileID,
			List<ImportField> importerFields, Map<String, List<String>> data,
			double recordCount) {
		this.importType = importType;
		this.importData = data;
		this.fileID = fileID;
		this.noOfRecords = recordCount;
		for (ImportField field : importerFields) {
			fields.put(field.getDesplayName(), field);
		}
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mappingTable = new FlexTable();
		mainTable = new FlexTable();
		previewTable = new FlexTable();
		mappingTable.addStyleName("import-match-content");

		// importerMatchingPanel.addStyleName("import-mapping-panel");
		FlowPanel buttonPanel = new FlowPanel();

		Label header = new Label();

		backButton = new Button(messages.back());
		nextButton = new Button(messages.next());
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentLine--;
				getNextOrPreRow();
				initPreviewGUI();
				showOrHideButtons();
			}
		});

		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentLine++;
				getNextOrPreRow();
				initPreviewGUI();
				showOrHideButtons();
			}
		});
		backButton.addStyleName("prev_button");
		nextButton.addStyleName("next_button");
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.addStyleName("import-button-panel");

		FlowPanel previewPanel = new FlowPanel();
		Label previewHeader = new Label(messages.mappingPreview());
		previewHeader.addStyleName("preview-header");
		previewPanel.add(previewHeader);
		previewPanel.add(previewTable);

		// Save & Import buttons
		FlowPanel buttonPanel2 = new FlowPanel();
		Button saveButton = new Button(messages.save());
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveAndUpdateView();
			}
		});
		Button cancelButton = new Button(messages.cancel());
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		buttonPanel2.add(saveButton);
		buttonPanel2.add(cancelButton);
		buttonPanel2.addStyleName("import-button-panel");

		mainTable.setWidget(1, 0, header);
		mainTable.setWidget(1, 0, buttonPanel);
		mainTable.setWidget(2, 0, mappingTable);
		mainTable.setWidget(2, 1, previewPanel);
		mainTable.setWidget(3, 1, buttonPanel2);
		add(mainTable);
	//	mainTable.setSize("100%", "100%");
		previewPanel.addStyleName("import-preview-panel");
//		previewPanel.getElement().getParentElement()
//				.setAttribute("height", "100%");
//		buttonPanel.getElement().getParentElement()
//				.setAttribute("width", "500px");
		initMappingGUI();
		initPreviewGUI();
		showOrHideButtons();
	}

	protected void close() {
		this.removeFromParent();
	}

	private void getNextOrPreRow() {
		Map<String, String> nextLine = getCurrentLine();
		int row = 1;
		if (!nextLine.isEmpty()) {
			for (Entry<String, String> entry : nextLine.entrySet()) {
				Label label = (Label) mappingTable.getWidget(row, 1);
				label.setText(entry.getValue());
				row++;
			}
		} else {
			currentLine = 0;
		}
	}

	private void initMappingGUI() {

		listBoxMap = new HashMap<String, ImportField>();
		Label csvHeader = new Label(messages.nameInCSV());
		Label valueHeader = new Label(messages.fieldValue());
		Label accounterNameHeader = new Label(messages.accounterField());

		csvHeader.addStyleName("table-header");
		valueHeader.addStyleName("table-header");
		accounterNameHeader.addStyleName("table-header");

		mappingTable.setWidget(0, 0, csvHeader);
		mappingTable.setWidget(0, 1, valueHeader);
		mappingTable.setWidget(0, 2, accounterNameHeader);
		int objectIndex = 0;
		Map<String, String> map = getCurrentLine();
		for (final Entry<String, String> entry : map.entrySet()) {
			Label columnName = new Label(entry.getKey());
			Label columnValue = new Label(entry.getValue());
			final ListBox importerFields = new ListBox();
			importerFields.addItem(messages.unassigned());
			for (String f : fields.keySet()) {
				importerFields.addItem(f);
			}
			importerFields.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = importerFields.getSelectedIndex();
					String selectedVal = importerFields
							.getItemText(selectedIndex);

					ImportField tempField = fields.get(selectedVal);
					tempField.setColumnName(entry.getKey());
					validateImportSelection(selectedVal, selectedIndex,
							tempField, importerFields, entry.getValue());
					initPreviewGUI();
				}
			});
			mappingTable.setWidget(objectIndex + 1, 0, columnName);
			mappingTable.setWidget(objectIndex + 1, 1, columnValue);
			mappingTable.setWidget(objectIndex + 1, 2, importerFields);
			objectIndex++;
		}
	}

	protected void refreshListBoxes(String selectedVal, ImportField tempField,
			int selectedIndex, ListBox selectedBox) {
		for (int i = 1; i < fields.values().size() - 1; i++) {
			ListBox listBox = (ListBox) mappingTable.getWidget(i, 2);
			if (listBox != selectedBox
					&& listBox.getSelectedIndex() == selectedIndex) {
				listBox.setSelectedIndex(0);
			}
		}
		initPreviewGUI();
		selectedBox.setSelectedIndex(selectedIndex);
	}

	protected void validateImportSelection(final String fieldName,
			final int selectedIndex, final ImportField field,
			final ListBox importerFields, final String columnValue) {
		if (listBoxMap.containsKey(fieldName)) {
			Accounter.showWarning(messages.aleadyMapped(fieldName),
					AccounterType.WARNING, new ErrorDialogHandler() {

						@SuppressWarnings("unchecked")
						@Override
						public boolean onYesClick() {
							if (validateFieldValue(fieldName, field,
									columnValue)) {
								refreshListBoxes(fieldName,
										(ImportField) field, selectedIndex,
										importerFields);
							} else {
								initPreviewGUI();
								importerFields.setSelectedIndex(0);
							}
							return true;
						}

						@Override
						public boolean onNoClick() {
							importerFields.setSelectedIndex(0);
							return true;
						}

						@Override
						public boolean onCancelClick() {
							return false;
						}
					});
		} else {
			if (!validateFieldValue(fieldName, field, columnValue)) {
				importerFields.setSelectedIndex(0);
			}
		}
	}

	protected boolean validateFieldValue(String fieldName, ImportField field,
			String columnValue) {
		if (field.validate(columnValue)) {
			refreshMapByValue(fieldName, field);
			listBoxMap.put(fieldName, field);
			ImportField localField = fields.get(fieldName);
			localField.setColumnName(field.getColumnName());
			return true;
		} else {
			Accounter.showError(messages.matchedWithWrongProprty(fieldName,
					field.getValueAsString()));
			return false;
		}
	}

	private void refreshMapByValue(String fieldName, ImportField field) {
		for (Entry<String, ImportField> entry : listBoxMap.entrySet()) {
			if (field != null && field.getColumnName() != null && entry != null
					&& entry.getValue() != null) {
				if (field.getColumnName().equals(
						entry.getValue().getColumnName())) {
					listBoxMap.remove(entry.getKey());
					return;
				}
			}

		}

	}

	private void initPreviewGUI() {
		int row = 0;
		for (ImportField field : fields.values()) {
			Label fieldNameLabel = new Label(field.getDesplayName());
			Label valLabel = new Label(
					(field.getValueAsString() != null) ? field
							.getValueAsString() : messages.unassigned());
			previewTable.setWidget(row, 0, fieldNameLabel);
			previewTable.setWidget(row, 1, valLabel);
			row++;
		}

	}

	private Map<String, String> getCurrentLine() {
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, List<String>> entrySet : importData.entrySet()) {
			String key = entrySet.getKey();
			List<String> value = entrySet.getValue();
			map.put(key, value.size() > currentLine ? value.get(currentLine)
					: "");
		}
		return map;
	}

	@Override
	public void saveAndUpdateView() {
		Map<String, String> importMap = updateImport();
		Accounter.createHomeService().importData(this.fileID, importType,
				importMap, new AccounterAsyncCallback<Boolean>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(exception.getMessage());
					}

					@Override
					public void onResultSuccess(Boolean result) {
						Accounter.showMessage(" Import Completed");
					}
				});
		super.saveAndUpdateView();
	}

	private Map<String, String> updateImport() {
		Map<String, String> map = new HashMap<String, String>();
		for (ImportField field : fields.values()) {
			map.put(field.getName(), field.getColumnName());
		}
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

	private void showOrHideButtons() {
		if (noOfRecords == 1) {
			this.nextButton.setVisible(false);
			this.backButton.setVisible(false);
		} else if (noOfRecords > 1 && currentLine == 0) {
			this.nextButton.setVisible(true);
			this.backButton.setVisible(false);
		} else if (currentLine > 0 && noOfRecords > 1 && noOfRecords - 1 == 0) {
			this.nextButton.setVisible(false);
			this.backButton.setVisible(true);
		} else {
			this.nextButton.setVisible(true);
			this.backButton.setVisible(true);
		}
	}
}
