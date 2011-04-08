package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.vat.FileVATView;

public abstract class BaseView<T> extends AbstractBaseView<T> {

	protected VerticalPanel mainPanel;
	public static HTML errordata;
	public static VerticalPanel commentPanel;

	protected ScrollPanel canvas;
	protected HorizontalPanel buttonLayout;

	private String height;
	protected boolean isFixedAssetView;
	protected int accountType;
	public int type;
	private HelpItem item;

	public BaseView() {

	}

	public BaseView(boolean isFixedAssetView) {
		this.isFixedAssetView = isFixedAssetView;
	}

	@Override
	public void init() {
		createView();
		this.accountType = FinanceApplication.getCompany().getAccountingType();

	}

	@Override
	public void createID() {

		this.rpcGetService.getStringID(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				BaseView.this.stringID = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				Accounter
						.showError("Could Not Initialize View.... \nID Could Not Be Initialized");
				MainFinanceWindow.getViewManager().closeView(getAction(), null);
			}
		});

	}

	private void createView() {

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		setWidth("100%");
		setHeight("100%");
		commentPanel = new VerticalPanel();
		commentPanel.setWidth("100%");
		commentPanel.setVisible(false);
		commentPanel.addStyleName("commentPanel");
		errordata = new HTML();
		errordata.addStyleName("error-data");
		commentPanel.add(errordata);
		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		canvas = new ScrollPanel();
		canvas.setWidth("100%");
		mainPanel.add(commentPanel);
		mainPanel.add(canvas);
		add(mainPanel);
		mainPanel.setStyleName("main-class-pannel");

		buttonLayout = new HorizontalPanel();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setSpacing(10);
		buttonLayout.setHeight("30px");

		saveAndCloseButton = new CustomButton(CustomButtonType.SAVE_AND_CLOSE,
				this);
		buttonLayout
				.setCellHorizontalAlignment(saveAndCloseButton, ALIGN_RIGHT);
		if (this != null && this instanceof FileVATView) {
			saveAndCloseButton.setText("File VAT Return");

			// saveAndCloseButton.setWidth("200px");
		}
		registerButton = new CustomButton(CustomButtonType.REGISTER, this);

		cancelButton = new CustomButton(CustomButtonType.CANCEL, this);
		/*
		 * if the view is related to FixedAsset,then "SaveAndNew" is replaced
		 * with "Register" button(it is similar to "saveAndClose" button but
		 * with different functionality)
		 */
		if (!isFixedAssetView)
			buttonLayout.add(saveAndCloseButton);
		else {
			// In edit mode "SaveAndClose" button is renamed as "Update"
			if (isEdit)
				saveAndCloseButton.setText("Update");
			buttonLayout.add(saveAndCloseButton);
			buttonLayout.add(registerButton);

		}
		if (!isFixedAssetView) {
			saveAndNewButton = new CustomButton(CustomButtonType.SAVE_AND_NEW,
					this);
			buttonLayout.setCellHorizontalAlignment(saveAndNewButton,
					ALIGN_RIGHT);
			if (!isFixedAssetView
					&& !(this != null && this instanceof FileVATView))
				buttonLayout.add(saveAndNewButton);
		}

		buttonLayout.add(cancelButton);

		setCellHorizontalAlignment(buttonLayout, ALIGN_RIGHT);
		add(buttonLayout);
		buttonLayout.getElement().getParentElement()
				.setClassName("bottom-view");

		cancelButton.getElement().getParentElement().setClassName(
				"cancel-button");

		inithelpPanel();

		Element spanEle = DOM.createSpan();
		spanEle.addClassName("save-new-separator");
		DOM.appendChild(saveAndNewButton.getElement(), spanEle);

		Element spanElement = DOM.createSpan();
		spanElement.addClassName("save-new-image");
		DOM.appendChild(saveAndNewButton.getElement(), spanElement);

		Element savecloseseparator = DOM.createSpan();
		savecloseseparator.addClassName("save-close-separator");
		DOM.appendChild(saveAndCloseButton.getElement(), savecloseseparator);

		Element savecloseimage = DOM.createSpan();
		savecloseimage.addClassName("save-close-image");
		DOM.appendChild(saveAndCloseButton.getElement(), savecloseimage);

		Element closeseparator = DOM.createSpan();
		closeseparator.addClassName("close-separator");
		DOM.appendChild(cancelButton.getElement(), closeseparator);

		Element closeimage = DOM.createSpan();
		closeimage.addClassName("close-image");
		DOM.appendChild(cancelButton.getElement(), closeimage);

		ThemesUtil.addDivToButton(cancelButton, FinanceApplication
				.getThemeImages().button_right_gray_image(),
				"custom-button-right-image");
		ThemesUtil.addDivToButton(saveAndCloseButton, FinanceApplication
				.getThemeImages().custom_button_right_blue_image(),
				"custom-button-right-image");
		if (!(this != null && this instanceof FileVATView)) {
			ThemesUtil.addDivToButton(saveAndNewButton, FinanceApplication
					.getThemeImages().custom_button_right_blue_image(),
					"custom-button-right-image");
		}
	}

	public Panel getCanvas() {
		return canvas;
	}

	public Panel getButtonPanel() {
		return this.buttonLayout;
	}

	public void inithelpPanel() {
		item = MainFinanceWindow.getInstance().getHelpItem();
		item.removeLinks();
		FinanceApplication.createGETService().getHelpLinks(type,
				new AsyncCallback<List<HelpLink>>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError("unable to add links");
					}

					@Override
					public void onSuccess(List<HelpLink> result) {
						item.setLinks(result);

					}
				});

	}

	@Override
	protected void onLoad() {
		if (height != null) {
			double h = Double.parseDouble(height.replaceAll("px", ""));
			this.canvas.setHeight(h - 10 + "px");
		}
		super.onLoad();
	}

	public String getHeight() {
		return height;
	}

	public void setHeightForCanvas(String height) {
		this.height = height;
		// if (height != null && canvas != null) {
		// double h = Double.parseDouble(height.replaceAll("px", ""));
		// this.canvas.setHeight(h - 10 + "px");
		// }
	}

	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
		// canvas.setHeight(height - 125 + "px");
		canvas.setWidth(width - 15 + "px");
	}
}
