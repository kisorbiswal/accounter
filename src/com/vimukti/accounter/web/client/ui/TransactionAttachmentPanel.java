package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientAttachment;

public abstract class TransactionAttachmentPanel extends SimplePanel {
	private static final String ATTACHMENT_URL = "/do/uploadattachment";

	VerticalPanel attachmentTable;
	private boolean isFileSelected;
	private String[] fileTypes = { "*" };
	private List<ClientAttachment> attachments = new ArrayList<ClientAttachment>();
	FormPanel uploadForm;

	public TransactionAttachmentPanel() {
		createControls();
		this.addStyleName("attachment_panel");
		setSize("100%", "100%");
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();

		attachmentTable = new VerticalPanel();
		mainPanel.add(attachmentTable);
		mainPanel.setWidth("100%");

		uploadForm = new FormPanel();
		uploadForm.setVisible(!isInViewMode());
		uploadForm.setAction(ATTACHMENT_URL);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		HorizontalPanel hPanel = new HorizontalPanel();
		final FileUpload uploadFile = new FileUpload();
		uploadFile.setEnabled(!isInViewMode());
		final Anchor browseFileAnchor = new Anchor(Accounter.messages()
				.uploadAttachment());
		browseFileAnchor.setEnabled(!isInViewMode());
		uploadFile.setVisible(false);
		uploadFile.setName(createID());
		uploadFile.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isFileSelected = true;
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
				String text = browseFileAnchor.getText();
				String results = event.getResults();
				String[] split = results.split("#");
				if (split.length != 2) {
					return;// Failed
				}
				ClientAttachment attachment = new ClientAttachment();
				attachment.setCreatedBy(Accounter.getUser().getID());
				attachment.setAttachmentId(split[0]);
				attachment.setName(text);
				attachment.setSize(Long.parseLong(split[1]));
				browseFileAnchor.setText(Accounter.messages()
						.uploadAttachment());
				attachmentTable.add(getAttachmentField(attachment));
				attachments.add(attachment);
				saveAttachment(attachment);
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
				if (isFileSelected) {
					isFileSelected = false;
					uploadForm.submit();
				} else {
					// TODO Ask user to select a file.
				}
			}
		});
		hPanel.add(uploadButton);

		mainPanel.add(uploadForm);

		this.add(mainPanel);
	}

	protected HorizontalPanel getAttachmentField(
			final ClientAttachment attachment) {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.addStyleName("attachment_hpanel");
		Anchor anchor = new Anchor(attachment.getName());
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UIUtils.downloadTransactionAttachment(
						attachment.getAttachmentId(), attachment.getName());
			}
		});

		Label attachmentSizeLabel = new Label(" (" + attachment.getSize()
				+ " )");

		Label creatorLabel = new Label(Accounter.getCompany()
				.getUserById(attachment.getCreatedBy()).getName());

		ImageButton button = new ImageButton(Accounter.getFinanceImages()
				.delete());
		button.setVisible(!isInViewMode());
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deeleteAttachment(attachment);
			}
		});
		hPanel.add(anchor);
		hPanel.add(attachmentSizeLabel);
		hPanel.add(creatorLabel);
		hPanel.add(button);
		return hPanel;
	}

	protected void deeleteAttachment(ClientAttachment attachment) {
		this.attachments.remove(attachment);
		addAttachments(this.attachments);
	}

	public abstract boolean isInViewMode();

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

	protected abstract void saveAttachment(ClientAttachment attachment);

	public void setAttachments(List<ClientAttachment> attachments) {
		this.attachments = attachments;
		for (ClientAttachment clientAttachment : attachments) {
			attachmentTable.add(getAttachmentField(clientAttachment));
		}
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

	public List<ClientAttachment> getAttachments() {
		return attachments;
	}

	public void addAttachments(List<ClientAttachment> attachments) {
		this.attachments = attachments;
		attachmentTable.clear();
		for (ClientAttachment clientAttachment : attachments) {
			attachmentTable.add(getAttachmentField(clientAttachment));
		}
	}

	public void setEnable(boolean isEnable) {
		uploadForm.setVisible(isEnable);
		for (int i = 0; i < attachmentTable.getWidgetCount(); i++) {
			Widget widget = attachmentTable.getWidget(i);
			if (widget instanceof HorizontalPanel) {
				HorizontalPanel panel = (HorizontalPanel) widget;
				ImageButton childWidget = (ImageButton) panel.getWidget(3);
				childWidget.setVisible(isEnable);
			}
		}
	}

}
