package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.ImportAction;
import com.vimukti.accounter.web.client.imports.ImporterType;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UploadCSVFileDialog extends BaseDialog {

	private FileUpload selectFileToUpload;
	private FormPanel uploadForm;
	HashMap<String, Integer> csvFileTypeListMap = new HashMap<String, Integer>();
	private ArrayList<String> list = new ArrayList<String>();
	private SelectCombo typeCombo;
	private Map<Integer, String> allSupportedImporters;

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {

	}

	public UploadCSVFileDialog() {
		doCreateContents();
	}

	@SuppressWarnings("deprecation")
	protected void doCreateContents() {
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		StyledPanel vpaPanel = new StyledPanel("vpaPanel");

		StyledPanel panel = new StyledPanel("panel");
		/* make space small */
		// Create a FileUpload widget.

		// Create list combo for type selection.
		typeCombo = new SelectCombo(messages.select() + messages.type());
		typeCombo.initCombo(getCSVFileList());
		typeCombo.setComboItem(Global.get().Customer());
		// typeCombo.getLabelWidget().addStyleName("bold_HTML");

		HTML detailsHtml3 = new HTML(messages.chooseLogo());
		detailsHtml3.addStyleName("bold_HTML");
		selectFileToUpload = new FileUpload();
		selectFileToUpload.setName("Import");

		DynamicForm form = new DynamicForm("form");
		form.add(typeCombo);

		panel.add(form);

		panel.add(detailsHtml3);
		// panel.add(selectFileToUpload);
		vpaPanel.add(panel);
		vpaPanel.add(selectFileToUpload);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.save());
		uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		StyledPanel buttonHlay = new StyledPanel("buttonHlay");
		buttonHlay.add(uploadSubmitButton);
		buttonHlay.add(closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);

		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		uploadSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				processOnUpload();
			}

		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}

		});
		uploadForm.setWidget(vpaPanel);
		uploadForm.addFormHandler(new FormHandler() {

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				StringBuilder result = new StringBuilder(event.getResults());

				String aa = result.toString().replaceAll("<pre>", " ");
				aa = aa.replaceAll("</pre>", " ");
				// for checking the data length
				if (aa.trim().length() > 2) {
					JSONValue jSONValue = JSONParser.parseLenient(aa.toString());
					JSONObject object = jSONValue.isObject();
					final String fileID = object.get("fileID").isString()
							.stringValue();
					final JSONNumber noOfRows = object.get("noOfRows")
							.isNumber();
					final double recordCount = noOfRows.doubleValue();

					final Map<String, List<String>> data = parseJsonArray(object);

					Accounter.createHomeService().getFieldsOf(getType(),
							new AccounterAsyncCallback<List<ImportField>>() {

								@Override
								public void onException(
										AccounterException exception) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onResultSuccess(
										List<ImportField> result) {
									ImportAction action = new ImportAction(
											result, data, getType(), fileID,
											recordCount);
									action.run();
									close();
								}
							});

				} else {
					Accounter.showInformation(messages
							.unableToUploadStatementFile());
					removeFromParent();
					return;
				}

			}

			@Override
			public void onSubmit(FormSubmitEvent event) {
				// TODO Auto-generated method stub

			}
		});
		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(uploadForm);
		add(mainPanel);
		show();

	}

	protected int getType() {
		return ImporterType.getTypeByName(typeCombo.getSelectedValue());
	}

	private List<String> getCSVFileList() {
		allSupportedImporters = ImporterType.getAllSupportedImporters();
		Collection<String> keySet = allSupportedImporters.values();
		list.addAll(keySet);
		return list;
	}

	/**
	 * Returns Map<ColumnName,AllValues>
	 * 
	 * @param object
	 * @return
	 */
	protected Map<String, List<String>> parseJsonArray(JSONObject object) {
		JSONValue first20Records = object.get("first20Records");
		JSONObject jObject = first20Records.isObject();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String columnName : jObject.keySet()) {
			JSONArray array = jObject.get(columnName).isArray();
			List<String> list = new ArrayList<String>();
			for (int x = 0; x < array.size(); x++) {
				list.add(toString(array.get(x)));
			}
			map.put(columnName, list);
		}
		return map;
	}

	private String toString(JSONValue value) {

		if (value.isString() != null) {
			return value.isString().stringValue();
		} else if (value.isNumber() != null) {
			return value.isNumber().toString();
		} else if (value.isObject() != null) {
			return value.isObject().toString();
		} else if (value.isBoolean() != null) {
			return value.isBoolean().toString();
		} else {
			return value.isString().stringValue();
		}
	}

	private void processOnUpload() {
		if (!validateFile()) {
			Accounter.showInformation(messages.noFileSelected());
			return;
		}
		uploadForm.submit();
	}

	private boolean validateFile() {
		boolean fileSelected = false;
		try {
			String filename = selectFileToUpload.getFilename();

			if (filename != null && filename.length() > 0) {
				// for checking the file extension
				if (filename.endsWith(".csv")) {
					fileSelected = true;
				}
			}
			if (fileSelected) {
				uploadForm.setAction("/do/uploadImportDataFile");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return fileSelected;
	}

	protected void close() {
		onCancel();
		this.removeFromParent();
	}

}
