package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.imports.ImportView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class UploadCSVFileDialog extends BaseDialog {

	private FileUpload selectFileToUpload;
	private FormPanel uploadForm;

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public UploadCSVFileDialog() {
		doCreateContents();
	}

	protected void doCreateContents() {
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel vpaPanel = new VerticalPanel();

		VerticalPanel panel = new VerticalPanel();
		/* make space small */
		panel.setSpacing(2);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// Create a FileUpload widget.

		// Create list combo for type selection.
		TextItem item = new TextItem(messages.type());
		item.getLabelWidget().addStyleName("bold_HTML");

		HorizontalPanel uplHorizontalPanel = new HorizontalPanel();
		HTML detailsHtml3 = new HTML(messages.chooseLogo());
		detailsHtml3.addStyleName("bold_HTML");
		uplHorizontalPanel.add(detailsHtml3);
		uplHorizontalPanel.setSpacing(1);
		selectFileToUpload = new FileUpload();
		uplHorizontalPanel.add(selectFileToUpload);

		panel.setSpacing(5);
		DynamicForm form = new DynamicForm();
		form.setFields(item);

		panel.add(form);
		panel.setSpacing(5);

		panel.add(uplHorizontalPanel);
		vpaPanel.add(panel);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.save());
		uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		closeButton.setWidth("80px");
		HorizontalPanel buttonHlay = new HorizontalPanel();
		buttonHlay.add(uploadSubmitButton);
		buttonHlay.add(closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		buttonHlay.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonHlay.setCellHorizontalAlignment(uploadSubmitButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

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
		uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				StringBuilder result = new StringBuilder(event.getResults());

				String aa = result.toString().replaceAll("<pre>", " ");
				aa = aa.replaceAll("</pre>", " ");
				// for checking the data length
				if (aa.trim().length() > 2) {
					JSONValue jSONValue = JSONParser.parseLenient(aa.toString());
					JSONArray object = jSONValue.isArray();
					List<String[]> data = parseJsonArray(object);

					HashMap<String, Integer> map = compareUploadedFileRecords(data);
					int matched = map.get("matched");
					int notMatched = map.get("notMatched");
					if (notMatched != 0) {
						ImportView view = new ImportView();
						removeFromParent();
					} else {
						Accounter.showInformation(messages
								.statementAlreadyImported());
						removeFromParent();
						return;
					}
				} else {
					Accounter.showInformation(messages
							.unableToUploadStatementFile());
					removeFromParent();
					return;
				}

			}
		});
		mainPanel = new VerticalPanel();
		mainPanel.add(vpaPanel);
		add(mainPanel);
		show();

	}

	/**
	 * for checking the imported statement file records with the previous
	 * statementRecordList
	 * 
	 * @param importedList
	 */
	private HashMap<String, Integer> compareUploadedFileRecords(
			List<String[]> importedList) {
		// int matched = 0, notMatched = importedList.size() - 1;
		// if (previousImportStatementList != null) {
		// for (int i = 0; i < previousImportStatementList.size(); i++) {
		// Object stObj = previousImportStatementList.get(i);
		// // List<ClientStatementRecord> stList =
		// // stObj.getStatementList();
		//
		// for (int j = 0; j < importedList.size() - 1; j++) {
		//
		// String[] fileValues = importedList.get(j + 1);
		// // for (ClientStatementRecord stRecord : stList) {
		// // String[] recValues = getStatementRecordValues(stRecord);
		// // boolean isMatched = compare(recValues, fileValues);
		// // if (isMatched) {
		// // importedList.remove(j + 1);
		// // matched++;
		// // j = j - 1;
		// // break;
		// // }
		// //
		// // }
		//
		// }
		//
		// }
		// }
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// map.put("matched", matched);
		// map.put("notMatched", notMatched - matched);
		return map;
	}

	protected List<String[]> parseJsonArray(JSONArray array) {
		boolean forHeadings = true;
		List<String[]> data = new ArrayList<String[]>();
		JSONArray array1 = array.isArray();
		for (int i = 0; i < array1.size(); i++) {
			JSONValue jsonValue = array1.get(i);
			JSONObject object = jsonValue.isObject();
			Set<String> keySet = object.keySet();
			String[] headings = new String[object.size()];
			if (forHeadings) {
				int jj = 0;
				for (String key : keySet) {
					headings[jj] = key.trim();
					jj++;
				}
				data.add(headings);
			}
			forHeadings = false;
			String[] values = new String[object.size()];
			for (int j = 0; j < object.size(); j++) {
				int tmp = 0;
				for (String key : keySet) {
					String value = toString(object.get(key));
					values[tmp] = value.trim();
					tmp++;
				}
			}
			data.add(values);
		}
		return data;
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
				// FIXME
				uploadForm.setAction("/do/uploadstatementfile?id");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return fileSelected;
	}

	protected void close() {
		// TODO Auto-generated method stub

	}

}
