package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAttachment;
import com.vimukti.accounter.web.client.ui.edittable.AttachmentTable;

public abstract class TransactionAttachmentPanel extends SimplePanel {
	AttachmentTable attachmentTable;

	private String[] fileTypes = { "*" };

	public TransactionAttachmentPanel() {
		createControls();
		this.addStyleName("attachment_panel");
		setSize("100%", "100%");
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();

		attachmentTable = new AttachmentTable();
		attachmentTable.setDisabled(isinViewMode());
		mainPanel.add(attachmentTable);
		mainPanel.setWidth("100%");

		final FormPanel uploadForm = new FormPanel();
		uploadForm.setAction("");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		HorizontalPanel hPanel = new HorizontalPanel();

		final FileUpload uploadFile = new FileUpload();
		uploadFile.setEnabled(isinViewMode());
		final Anchor browseFileAnchor = new Anchor(Accounter.messages()
				.uploadAttachment());
		browseFileAnchor.setEnabled(isinViewMode());
		uploadFile.setVisible(false);
		uploadFile.setName(createID());
		uploadFile.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String filename = uploadFile.getFilename();
				browseFileAnchor.setText(filename);
			}
		});
		hPanel.add(uploadFile);
		uploadForm.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				if (uploadFile.getFilename() == null
						|| uploadFile.getFilename().length() <= 0) {
					Accounter.showInformation(Accounter.messages()
							.noImageisselected());
					return;
				}

				// if (!checkFileType(upload.getFilename(), fileTypes)) {
				// return;
				// }
			}
		});

		uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				browseFileAnchor.setText(Accounter.messages()
						.uploadAttachment());
				saveAttachment(event.getResults());
			}
		});

		browseFileAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clickOnInputFile(uploadFile.getElement());
			}
		});
		hPanel.add(browseFileAnchor);

		uploadForm.add(hPanel);

		Button uploadButton = new Button(Accounter.messages().upload());
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadForm.submit();
			}
		});
		uploadButton.setEnabled(!isinViewMode());
		hPanel.add(uploadButton);

		mainPanel.add(uploadForm);

		this.add(mainPanel);
	}

	public abstract boolean isinViewMode();

	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

	private boolean checkFileType(String name, String[] types) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : types) {
			if (type.equalsIgnoreCase(fileType))
				return true;

		}
		Accounter.showInformation(Accounter.messages().pleaseSelect("File"));
		return false;

	}

	protected void saveAttachment(String string) {
		// TODO Auto-generated method stub

	}

	public void setAttachments(List<ClientAttachment> attachments) {
		attachmentTable.setAllRows(attachments);
	}

	public native static String createID()/*-{
		var MES_UNIQUE_IDS = {};
		var mes_generateUniqueId = function(charset, len, isNotInDOM) {
			var i = 0;
			if (!charset) {
				charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
			}
			if (!len) {
				len = 40;
			}
			var id = '', charsetlen = charset.length, charIndex;

			// iterate on the length and get a random character for each position
			for (i = 0; len > i; i += 1) {
				charIndex = Math.random() * charsetlen;
				id += charset.charAt(charIndex);
			}

			if (MES_UNIQUE_IDS[id] || (isNotInDOM)) {
				MES_UNIQUE_IDS[id] = true; // add DOM ids to the map
				return mes_generateUniqueId(charset, len, isNotInDOM);
			}

			MES_UNIQUE_IDS[id] = true;

			return id;
		};
		return mes_generateUniqueId();
	}-*/;

}
