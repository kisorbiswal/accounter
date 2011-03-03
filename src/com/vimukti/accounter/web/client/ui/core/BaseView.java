package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.vat.FileVATView;

public abstract class BaseView<T> extends AbstractBaseView<T> {

	protected ScrollPanel canvas;
	protected HorizontalPanel buttonLayout;

	private String height;
	protected boolean isFixedAssetView;
	protected int accountType;

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

		canvas = new ScrollPanel();
		canvas.setWidth("100%");
		add(canvas);

		buttonLayout = new HorizontalPanel();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setSpacing(10);
		buttonLayout.setHeight("30px");
		
			saveAndCloseButton = new CustomButton(
					CustomButtonType.SAVE_AND_CLOSE, this);
		buttonLayout
				.setCellHorizontalAlignment(saveAndCloseButton, ALIGN_RIGHT);
		if (this != null && this instanceof FileVATView){
			saveAndCloseButton.setText("File VAT Return");
		
			saveAndCloseButton.setWidth("200px");
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
			if (!isFixedAssetView && !(this != null && this instanceof FileVATView))
				buttonLayout.add(saveAndNewButton);
		}

		buttonLayout.add(cancelButton);

		setCellHorizontalAlignment(buttonLayout, ALIGN_RIGHT);
		add(buttonLayout);
		buttonLayout.getElement().getParentElement()
				.setClassName("bottom-view");
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
//		if (height != null && canvas != null) {
//			double h = Double.parseDouble(height.replaceAll("px", ""));
//			this.canvas.setHeight(h - 10 + "px");
//		}
	}

	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
//		canvas.setHeight(height - 125 + "px");
		canvas.setWidth(width - 15 + "px");
	}
}
