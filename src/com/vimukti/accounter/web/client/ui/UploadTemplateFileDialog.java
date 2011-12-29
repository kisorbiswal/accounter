package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
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
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings({ "deprecation" })
public class UploadTemplateFileDialog extends BaseDialog<ClientBrandingTheme> {

	private ValueCallBack<ClientBrandingTheme> callback;
	private FormPanel uploadForm;
	private VerticalPanel mainLayout;
	private VerticalPanel panel;
	private VerticalPanel uploadFormLayout;
	private HorizontalPanel buttonHlay;
	private ClientBrandingTheme brandingTheme;

	private String title;
	private HTML detailsHtml1, detailsHtml2, detailsHtml3, detailsHtml4,
			detailsHtml5;

	private TextItem invoiceBox, creditNoteBox, quoteBox;

	public UploadTemplateFileDialog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback,
			ClientBrandingTheme theme) {
		// super(false, true);
		this.title = title;
		setText(title);
		this.callback = callback;
		this.brandingTheme = theme;
		doCreateContents();
		center();
	}

	protected void doCreateContents() {
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");

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

		invoiceBox = new TextItem();
		invoiceBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						invoiceBox.setValue(value.getInvoiceTempleteName());
					}
				};

				FileUploadDilaog uploadDialog = new FileUploadDilaog(
						"Upload Invoice template", "parent", callback, null,
						brandingTheme, true);
			}
		});

		creditNoteBox = new TextItem();
		creditNoteBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						creditNoteBox.setValue(value
								.getCreditNoteTempleteName());
					}
				};

				FileUploadDilaog uploadDialog = new FileUploadDilaog(
						"Upload CreditNote template", "parent", callback, null,
						brandingTheme, true);
			}
		});

		quoteBox = new TextItem();
		quoteBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						quoteBox.setValue(value.getQuoteTemplateName());
					}
				};

				FileUploadDilaog uploadDialog = new FileUploadDilaog(
						"Upload Quote template", "parent", callback, null,
						brandingTheme, true);
			}
		});
		panel.add(detailsHtml1);
		panel.setSpacing(5);
		panel.add(detailsHtml2);
		panel.setSpacing(5);
		panel.add(detailsHtml3);
		panel.setSpacing(5);
		DynamicForm invoiceForm = new DynamicForm();
		invoiceForm.setFields(invoiceBox);
		panel.add(invoiceForm);
		panel.setSpacing(5);
		panel.add(detailsHtml4);
		panel.setSpacing(5);
		DynamicForm creditNoteForm = new DynamicForm();
		creditNoteForm.setFields(creditNoteBox);
		panel.add(creditNoteForm);
		panel.setSpacing(5);
		panel.add(detailsHtml5);
		panel.setSpacing(5);
		DynamicForm quoteForm = new DynamicForm();
		quoteForm.setFields(quoteBox);
		panel.add(quoteForm);

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

		uploadFormLayout = new VerticalPanel();
		uploadFormLayout.add(uploadForm);
		mainLayout = new VerticalPanel();
		mainLayout.add(uploadFormLayout);
		add(mainLayout);
		show();
	}

	private void processOnUpload() {
		// TODO Auto-generated method stub

		// brandingTheme.setInvoiceTempleteName(invoiceBox.getValue().toString());
		// brandingTheme.setCreditNoteTempleteName(creditNoteBox.getValue()
		// .toString());
		saveOrUpdate(brandingTheme);
		close();

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
		invoiceBox.setFocus();

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
	}
}
