package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

@SuppressWarnings({ "deprecation" })
public class UploadTemplateFileDialog extends BaseDialog<ClientBrandingTheme> {
	private static final String INVOICE = "INVOICE";
	private static final String QUOTE = "QUOTE";
	private static final String CREDITNOTE = "CREDITNOTE";
	private static final String CASHSALE = "CASHSALE";
	private ValueCallBack<ClientBrandingTheme> callback;
	private FormPanel uploadForm;
	private VerticalPanel mainLayout;
	private VerticalPanel panel;
	private VerticalPanel uploadFormLayout;
	private HorizontalPanel buttonHlay;
	private ClientBrandingTheme brandingTheme;
	private boolean closeAfterUploaded = true;
	private String title;
	private HTML detailsHtml1, detailsHtml2, detailsHtml3, detailsHtml4,
			detailsHtml5, detailsHtml6;

	// private TextItem invoiceBox, creditNoteBox, quoteBox;
	private FileUpload invoiceBtn, creditNoteBtn, quoteBtn, cashSaleBtn;
	private String[] fileTypes;
	private ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();

	public UploadTemplateFileDialog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback,
			ClientBrandingTheme theme) {
		// super(false, true);
		this.title = title;
		setText(title);
		this.callback = callback;
		this.brandingTheme = theme;
		this.fileTypes = new String[] { "odt", "docx" };
		doCreateContents();
		center();
	}

	protected void doCreateContents() {
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel vpaPanel = new VerticalPanel();

		panel = new VerticalPanel();
		/* make space small */
		panel.setSpacing(2);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// Create a FileUpload widget.

		// for uploading the custom template files
		detailsHtml1 = new HTML(messages.uploadOneOrMoreTemplates());
		detailsHtml2 = new HTML(messages.eachFileCanBeNoLongerThan());
		detailsHtml3 = new HTML(messages.invoice());
		detailsHtml3.addStyleName("bold_HTML");
		detailsHtml4 = new HTML(messages.creditNote());
		detailsHtml4.addStyleName("bold_HTML");
		detailsHtml5 = new HTML(messages.quote());
		detailsHtml5.addStyleName("bold_HTML");
		detailsHtml6 = new HTML(messages.cashSale());
		detailsHtml6.addStyleName("bold_HTML");

		invoiceBtn = new FileUpload();
		// final String fileID_1 = createID();
		invoiceBtn.setName(INVOICE);

		creditNoteBtn = new FileUpload();
		// final String fileID_2 = createID();
		creditNoteBtn.setName(CREDITNOTE);

		quoteBtn = new FileUpload();
		// final String fileID_3 = createID();
		quoteBtn.setName(QUOTE);

		cashSaleBtn = new FileUpload();
		// final String fileID_3 = createID();
		cashSaleBtn.setName(CASHSALE);

		uploadItems.add(invoiceBtn);
		uploadItems.add(creditNoteBtn);
		uploadItems.add(quoteBtn);
		uploadItems.add(cashSaleBtn);

		panel.add(detailsHtml1);
		panel.setSpacing(5);
		panel.add(detailsHtml2);
		panel.setSpacing(5);
		panel.add(detailsHtml3);
		panel.setSpacing(5);

		VerticalPanel invPanel = new VerticalPanel();
		invPanel.add(invoiceBtn);
		panel.add(invPanel);

		panel.setSpacing(5);
		panel.add(detailsHtml4);
		panel.setSpacing(5);

		VerticalPanel creditPanel = new VerticalPanel();
		creditPanel.add(creditNoteBtn);
		panel.add(creditPanel);

		panel.setSpacing(5);
		panel.add(detailsHtml5);
		panel.setSpacing(5);

		VerticalPanel quotePanel = new VerticalPanel();
		quotePanel.add(quoteBtn);
		panel.add(quotePanel);

		panel.setSpacing(5);
		panel.add(detailsHtml6);
		panel.setSpacing(5);
		
		VerticalPanel cashPanel = new VerticalPanel();
		cashPanel.add(cashSaleBtn);
		panel.add(cashPanel);

		vpaPanel.add(panel);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.save());
		uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		closeButton.setWidth("80px");
		buttonHlay = new HorizontalPanel();
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
		uploadForm.addFormHandler(new FormHandler() {

			@Override
			public void onSubmit(FormSubmitEvent event) {
			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				// processUploadAttachments(brandingTheme, callback);

				String value = event.getResults();

				if (brandingTheme == null) {
					brandingTheme = new ClientBrandingTheme();
				}
				processResult(brandingTheme, value);
			}
		});

		uploadFormLayout = new VerticalPanel();
		uploadFormLayout.add(uploadForm);
		mainLayout = new VerticalPanel();
		mainLayout.add(uploadFormLayout);
		add(mainLayout);
		show();
	}

	/**
	 * this method is used to process the final result
	 * 
	 * @param brandingTheme
	 * @param value
	 *            (a.docx;b.docx;c.docx)or(a.odt;b.odt;c.odt)
	 */
	private void processResult(ClientBrandingTheme brandingTheme, String value) {

		if (value.trim().length() != 0) {
			String[] split = value.split(";");
			if (split[0].trim().length() > 0)
				brandingTheme.setInvoiceTempleteName(split[0]);
			if (split[1].trim().length() > 0)
				brandingTheme.setCreditNoteTempleteName(split[1]);
			if (split[2].trim().length() > 0)
				brandingTheme.setQuoteTemplateName(split[2]);
			if (split[3].trim().length() > 0)
				brandingTheme.setCashSaleTemplateName(split[3]);

			processUploadAttachments(brandingTheme, callback);
		}

	}

	protected void processOnUpload() {
		String file_1 = uploadItems.get(0).getFilename() == null ? ""
				: uploadItems.get(0).getFilename();
		String file_2 = uploadItems.get(1).getFilename() == null ? ""
				: uploadItems.get(1).getFilename();
		String file_3 = uploadItems.get(2).getFilename() == null ? ""
				: uploadItems.get(2).getFilename();
		String file_4 = uploadItems.get(3).getFilename() == null ? ""
				: uploadItems.get(3).getFilename();
		if (file_1.equals("") && file_2.equals("") && file_3.equals("")
				&& file_4.equals("")) {
			Accounter.showInformation(messages.noFileSelected());
			return;
		}
		for (FileUpload upload : uploadItems) {
			String filename = upload.getFilename();

			if (filename != null && filename.length() > 0) {
				if (!(filename.endsWith("odt") || filename.endsWith("docx"))) {

					Accounter.showInformation(messages
							.selectFileOfTypeOdtOrDocx());
					return;
				}
			}
		}

		uploadForm.setAction("/do/uploadtemplatefile?themeId="
				+ brandingTheme.getID());
		uploadForm.submit();
	}

	private boolean checkFileType(String name) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : fileTypes) {
			if (type.equalsIgnoreCase(fileType))
				return true;
		}
		return false;
	}

	private void processUploadAttachments(ClientBrandingTheme result,
			final ValueCallBack<ClientBrandingTheme> callback2) {

		if (callback2 != null) {
			callback2.execute(result);
		}
		if (closeAfterUploaded) {
			close();
		}

	}

	public void close() {
		this.removeFromParent();
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
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
