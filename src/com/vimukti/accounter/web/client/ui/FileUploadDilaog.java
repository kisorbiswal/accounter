package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

@SuppressWarnings("deprecation")
public class FileUploadDilaog extends CustomDialog {
	private static final String ATTACH_ANOTHER = "Add another file";
	private final String parentID;

	private FormPanel uploadForm;
	// private boolean canRelplaceFile;
	private final ValueCallBack<ClientBrandingTheme> callback;
	private HorizontalPanel loadingLayout;
	private VerticalPanel mainLayout;
	private final boolean shouldUploadMulti;
	private VerticalPanel panel;
	private final ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();
	private int count = 1;
	private VerticalPanel uploadFormLayout;
	private final String[] fileTypes;
	private HorizontalPanel buttonHlay;
	private boolean closeAfterUploaded;
	private static ClientBrandingTheme brandingTheme;

	public FileUploadDilaog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback,
			boolean shouldUploadMulti, String[] fileTypes,
			ClientBrandingTheme theme) {
		super(false, true);
		setHTML(title);
		this.parentID = parentID;
		this.callback = callback;
		this.shouldUploadMulti = shouldUploadMulti;
		this.fileTypes = fileTypes;
		this.brandingTheme = theme;
		closeAfterUploaded = true;
		doCreateContents();
		center();
	}

	public FileUploadDilaog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback,
			boolean shouldUploadMulti, String[] fileTypes,
			boolean closeAfterUploaded) {
		this(title, parentID, callback, shouldUploadMulti, fileTypes,
				brandingTheme);
		this.closeAfterUploaded = closeAfterUploaded;
	}

	protected void doCreateContents() {

		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		final String fileID = createID();

		// Because we're going to add a FileUpload widget, we'll need to set the

		// form to use the POST method, and multipart MIME encoding.
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel vpaPanel = new VerticalPanel();

		panel = new VerticalPanel();
		/* make space small */
		panel.setSpacing(2);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// Create a FileUpload widget.
		final FileUpload upload = new FileUpload();
		/* Default height of upload text box 26 */
		upload.setHeight("26");
		upload.getElement().setAttribute("size", "50");
		upload.setName(fileID);
		uploadItems.add(upload);
		panel.add(upload);
		vpaPanel.add(panel);

		buttonHlay = new HorizontalPanel();
		buttonHlay.setSpacing(2);
		buttonHlay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		if (shouldUploadMulti) {
			Anchor label = new Anchor(ATTACH_ANOTHER);
			label.addStyleName("NoWrapping");
			label.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					if (count == 5) {
						return;
					}
					panel.add(getFileItem());
					count++;
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(uploadFormLayout.getOffsetHeight());
					uploadFormLayout.setHeight(stringBuilder.toString());

				}
			});
			buttonHlay.add(label);
			buttonHlay.setCellHorizontalAlignment(label,
					HasHorizontalAlignment.ALIGN_LEFT);
		}

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button("Add");
		uploadSubmitButton.setWidth("50px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button("Close");
		closeButton.setWidth("50px");

		buttonHlay.add(uploadSubmitButton);
		buttonHlay.add(closeButton);
		uploadSubmitButton.getElement().getParentElement().setClassName(
				"ibutton");
		ThemesUtil.addDivToButton(uploadSubmitButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		closeButton.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(closeButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");

		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		buttonHlay.setCellWidth(uploadSubmitButton, "100%");

		buttonHlay.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonHlay.setCellHorizontalAlignment(uploadSubmitButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// if(!shouldUploadMulti){
		// horizontalPanel.setCellWidth(uploadSubmitButton, "80%");
		// }

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
			public void onSubmit(FormSubmitEvent event) {
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				String value = event.getResults();
				brandingTheme.setFileName(value);
				if (value == null) {
					getFileInfo();
				} else {
					if (value.trim().length() != 0) {
						processUploadAttachments(brandingTheme, callback);
					} else {
						loadingLayout.setVisible(false);
					}

				}
			}

		});
		uploadFormLayout = new VerticalPanel();
		// if (shouldUploadMulti)
		// // uploadFormLayout.setSize("100%", "150");
		// else
		// uploadFormLayout.setSize("78%", "100%");
		uploadFormLayout.add(uploadForm);
		mainLayout = new VerticalPanel();
		mainLayout.add(uploadFormLayout);
		// mainLayout.add(message);
		// createLoadingLayout();
		// setWidth("350px");
		// setSize("500", "200");
		add(mainLayout);
		show();
	}

	protected void getFileInfo() {
		getFileInfo(parentID, new AsyncCallback<ClientBrandingTheme>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ClientBrandingTheme result) {
				processUploadAttachments(result, callback);
			}
		});

	}

	public HorizontalPanel getFileItem() {
		final HorizontalPanel panel = new HorizontalPanel();
		// panel.setHeight("30");
		panel.setStyleName("fileuploaddialog-fileitem");

		final FileUpload upload = new FileUpload();
		String fileID = createID();
		upload.setName(fileID);
		upload.getElement().setAttribute("size", "50");

		HTML label = new HTML(
				"<font color='blue'><u style='cursor:pointer;'><nobr>Remove<nobr></u></font>");
		label.setWidth("60");
		label.setHeight("25");
		label.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				panel.removeFromParent();
				uploadItems.remove(upload);
				count--;
				uploadFormLayout.setHeight(new StringBuilder(uploadFormLayout
						.getOffsetHeight() - 30).toString());
			}
		});

		uploadItems.add(upload);
		panel.add(upload);
		panel.add(label);

		return panel;

	}

	private boolean checkFileType(String name, String[] types) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : types) {
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

	protected void processOnUpload() {
		if (!validateFileItems())
			return;

		if (fileTypes != null) {
			for (FileUpload fileUpload : uploadItems) {
				if (!checkFileType(fileUpload.getFilename(), fileTypes)) {
					return;
				}
			}

		}
		// loadingLayout.setVisible(true);
		uploadForm.submit();

	}

	private boolean validateFileItems() {
		// String ids="";
		boolean fileSelected = false;
		for (FileUpload upload : uploadItems) {
			String filename = upload.getFilename();

			if (filename != null && filename.length() > 0) {
				fileSelected = true;
			}
			// for (FileUpload fileUpload : fileids) {
			// if (filename.equals(fileUpload.getFilename())) {
			// SC.say(messages.filesDuplicationError());
			// return false
			// }
			// }
			/*
			 * if(ids.length()==0) ids=upload.getName(); else
			 * ids=ids+","+upload.getName();
			 */
		}

		if (fileSelected) {
			uploadForm.setAction("/do/uploadfile");
			return fileSelected;
		}

		return fileSelected;

	}

	public void close() {
		this.removeFromParent();
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

	public void getFileInfo(String parentID,
			final AsyncCallback<ClientBrandingTheme> callback) {
		post("/do/uploadfile?parentId=" + parentID,
				new AsyncCallback<ClientBrandingTheme>() {

					public void onSuccess(ClientBrandingTheme value) {
						callback.onSuccess(value);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}
				}, parentID);
	}

	// public static JsArray<IAttachment> getUploadedAttachments() {
	// return uploadedAttachments;
	// }

	// private void createLoadingLayout() {
	//
	// Image loadingImage = new Image(images.loaderImage());
	// Label loadingLabel = new Label(CollaberCoreUtils
	// .getStringWithSpaces(messages.pleaseWait()));
	// loadingLayout = new HorizontalPanel();
	// loadingLayout.add(loadingImage);
	// loadingLayout.add(loadingLabel);
	// loadingLayout
	// .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	// mainLayout.add(loadingLayout);
	// loadingLayout.setVisible(false);
	//
	// }

	public static <E, T> Object post(String url,
			final AsyncCallback<ClientBrandingTheme> callback, String parentId) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Accept", "text/html");
		// Create a callback object to handle the result
		RequestCallback requestCallback = new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				exception.printStackTrace();
			}

			@Override
			public void onResponseReceived(Request request, Response response) {
				// ClientBrandingTheme attachment =
				// parseStringAttachment(response
				// .getText());
				callback.onSuccess(brandingTheme);
			}
		};
		try {
			Request request = builder.sendRequest("", requestCallback);
			return request;
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static ClientBrandingTheme parseStringAttachment(String text) {
	// String[] attachments = text.split(":");
	// // ClientBrandingTheme[] brandingThemes = new
	// ClientBrandingTheme[attachments.length];
	// for (int i = 0; i < attachments.length; i++) {
	// String[] values = getValues(attachments[i]);
	// ClientBrandingTheme attachment = new ClientBrandingTheme();
	// brandingTheme = attachment;
	// }
	// return brandingTheme;
	// }

	// private static String[] getValues(String string) {
	// String[] temp1 = string.split(",");
	// if(temp1.length>3){
	// for(int i = temp1.length-2; i > 1; i--){
	// temp1[i-1]=temp1[i-1]+temp1[i];
	// temp1[i] = temp1 [i+1];
	// }
	// String[] temp2 = new String[3];
	// temp2[0] = temp1[0];
	// temp2[1] = temp1[1];
	// temp2[2] = temp1[2];
	// return temp2;
	// }
	// return temp1;
	// }

}
