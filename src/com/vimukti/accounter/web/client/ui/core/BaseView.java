package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.vat.FileVATView;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseView;

public abstract class BaseView<T> extends AbstractBaseView<T> {

	protected VerticalPanel mainPanel;
	// public static HTML errordata;
	// public static VerticalPanel commentPanel;

	protected ScrollPanel canvas;
	protected HorizontalPanel buttonLayout, bottomShadow;

	private String height;
	protected boolean isFixedAssetView;
	protected int accountType;

	public BaseView() {
	}

	protected abstract String getViewTitle();

	public BaseView(boolean isFixedAssetView) {
		this.isFixedAssetView = isFixedAssetView;
	}

	@Override
	public void init() {
		createView();
		this.accountType = Accounter.getCompany().getAccountingType();

	}

	@Override
	public void createID() {

		this.rpcGetService.getID(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				BaseView.this.id = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter
							.showError("Could Not Initialize View.... \nID Could Not Be Initialized");
					MainFinanceWindow.getViewManager().closeView(getAction(),
							null);
				}
			}
		});

	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void createView() {

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		setWidth("100%");
		setHeight("100%");
		// commentPanel = new VerticalPanel();
		// commentPanel.setWidth("100%");
		// commentPanel.setVisible(false);
		// commentPanel.addStyleName("commentPanel");
		// errordata = new HTML();
		// errordata.addStyleName("error-data");
		// commentPanel.add(errordata);
		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		canvas = new ScrollPanel();
		canvas.setWidth("100%");
		// mainPanel.add(commentPanel);
		mainPanel.add(canvas);
		add(mainPanel);
		mainPanel.setStyleName("main-class-pannel");

		buttonLayout = new HorizontalPanel();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setSpacing(10);
		buttonLayout.setHeight("30px");
		bottomShadow = new HorizontalPanel();

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
		if (this instanceof EmployeeExpenseView) {
			if (Accounter.getUser().canApproveExpences()) {
				approveButton = new CustomButton(CustomButtonType.APPROVE, this);
				buttonLayout.add(approveButton);
			} else {
				submitForApprove = new CustomButton(CustomButtonType.SUBMIT,
						this);
				buttonLayout.add(submitForApprove);
			}

		}
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
		add(bottomShadow);
		if (this instanceof SalesTaxGroupListView)
			buttonLayout.setVisible(false);
		buttonLayout.getElement().getParentElement()
				.setClassName("bottom-view");
		bottomShadow.getElement().getParentElement().setClassName(
				"bottom-shadow");

		cancelButton.getElement().getParentElement().setClassName(
				"cancel-button");

		// inithelpPansel();
		if (saveAndNewButton.isEnabled()) {
			Element spanEle = DOM.createSpan();
			spanEle.addClassName("save-new-separator");
			DOM.appendChild(saveAndNewButton.getElement(), spanEle);

			Element spanElement = DOM.createSpan();
			spanElement.addClassName("save-new-image");
			DOM.appendChild(saveAndNewButton.getElement(), spanElement);

			if (!(this != null && this instanceof FileVATView)) {
				ThemesUtil.addDivToButton(saveAndNewButton, Accounter
						.getThemeImages().custom_button_right_blue_image(),
						"custom-button-right-image");
			}
		}
		if (saveAndCloseButton.isEnabled()) {

			Element savecloseseparator = DOM.createSpan();
			savecloseseparator.addClassName("save-close-separator");
			DOM
					.appendChild(saveAndCloseButton.getElement(),
							savecloseseparator);

			Element savecloseimage = DOM.createSpan();
			savecloseimage.addClassName("save-close-image");
			DOM.appendChild(saveAndCloseButton.getElement(), savecloseimage);

			ThemesUtil.addDivToButton(saveAndCloseButton, Accounter
					.getThemeImages().custom_button_right_blue_image(),
					"custom-button-right-image");
		}
		if (approveButton != null && approveButton.isEnabled()) {

			// approveButton.enabledButton(AccounterButton.APPROVE_BUTTON,
			// "approve-image", "ibutton1");

			Element addseparator = DOM.createSpan();
			addseparator.addClassName("save-close-separator");
			DOM.appendChild(approveButton.getElement(), addseparator);

			Element addimage = DOM.createSpan();
			addimage.addClassName("approve-image");
			DOM.appendChild(approveButton.getElement(), addimage);

			ThemesUtil.addDivToButton(approveButton, Accounter
					.getThemeImages().button_right_blue_image(),
					"custom-button-right-image");
		}

		if (submitForApprove != null && submitForApprove.isEnabled()) {

			// submitForApprove.enabledButton(AccounterButton.SUBMIT_BUTTON,
			// "submit-approve-image", "ibutton1");

			Element addseparator = DOM.createSpan();
			addseparator.addClassName("save-close-separator");
			DOM.appendChild(submitForApprove.getElement(), addseparator);

			Element addimage = DOM.createSpan();
			addimage.addClassName("submit-approve-image");
			DOM.appendChild(submitForApprove.getElement(), addimage);

			ThemesUtil.addDivToButton(submitForApprove, Accounter
					.getThemeImages().button_right_blue_image(),
					"custom-button-right-image");
		}
		if (cancelButton.isEnabled()) {
			Element closeseparator = DOM.createSpan();
			closeseparator.addClassName("close-separator");
			DOM.appendChild(cancelButton.getElement(), closeseparator);

			Element closeimage = DOM.createSpan();
			closeimage.addClassName("close-image");
			DOM.appendChild(cancelButton.getElement(), closeimage);

			ThemesUtil.addDivToButton(cancelButton, Accounter
					.getThemeImages().button_right_gray_image(),
					"custom-button-right-image");

		}

		// if (submitForApprove.isEnabled()) {
		//
		// Element closeseparator = DOM.createSpan();
		// closeseparator.addClassName("close-separator");
		// DOM.appendChild(cancelButton.getElement(), closeseparator);
		//
		// Element closeimage = DOM.createSpan();
		// closeimage.addClassName("close-image");
		// DOM.appendChild(cancelButton.getElement(), closeimage);
		//
		// ThemesUtil.addDivToButton(cancelButton, FinanceApplication
		// .getThemeImages().button_right_gray_image(),
		// "custom-button-right-image");
		//
		// }
	}

	public Panel getCanvas() {
		return canvas;
	}

	public Panel getButtonPanel() {
		return this.buttonLayout;
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
		// canvas.setWidth(width - 15 + "px");
		canvas.setWidth("auto");
	}
}
