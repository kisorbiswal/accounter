package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class ItemTaxCodeDialog extends DialogBox {
	public ItemTaxCodeDialog() {
		createControls();
	}

	private void createControls() {
		setTitle(FinanceApplication.getFinanceUIConstants().manageItemTaxCode());

		Label lab1 = new Label(FinanceApplication.getFinanceUIConstants()
				.itemTaxCode());
		// lab1.setWrap(false);
		// lab1.setAutoFit(true);

		Label lab2 = new Label(FinanceApplication.getFinanceUIConstants()
				.toAddAnItemTax());
		// lab2.setHeight("1");
		// lab2.setOverflow(Overflow.VISIBLE);
		// lab2.setWrap(false);
		lab2.setWidth("100%");

		DialogGrid grid = new DialogGrid(false);
		// typeGrid.setAutoFitData(Autofit.BOTH);
		// typeGrid.setOverflow(Overflow.SCROLL);
		grid.setSize("100%", "100%");
		// grid.setShowAllRecords(true);

		// ListGridField nameField = new ListGridField("name",
		// "<center><b>Name</b></center>");
		// businessTypeField.setAlign(Alignment.CENTER);
		// grid.setFields(nameField);
		// grid.setCanResizeFields(true);
		// grid.setData(createListGridRecords(typeRecords));

		Button addButt = new Button("Add...");
		addButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showAddEditDialog();
			}
		});
		Button editButt = new Button(FinanceApplication.getFinanceUIConstants()
				.edit());
		Button remButt = new Button(FinanceApplication.getFinanceUIConstants()
				.remove());

		Button helpButt = new Button(FinanceApplication.getFinanceUIConstants()
				.help());
		Button closeButt = new Button(FinanceApplication
				.getFinanceUIConstants().close());

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setAlign(Alignment.LEFT);
		helpHLay.add(helpButt);
		helpButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(helpButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		HorizontalPanel closeHLay = new HorizontalPanel();
		// closeHLay.setAlign(Alignment.RIGHT);
		closeHLay.add(closeButt);
		closeButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(closeButt, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSize("100%", "10%");
		buttHLay.add(helpHLay);
		buttHLay.add(closeHLay);

		VerticalPanel buttVLay = new VerticalPanel();
		buttVLay.add(addButt);
		buttVLay.add(editButt);
		buttVLay.add(remButt);

		addButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(addButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		editButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(editButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		remButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(remButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");

		HorizontalPanel groupEditHLay = new HorizontalPanel();
		// groupEditHLay.setSize("100%", "*");
		groupEditHLay.add(grid);
		groupEditHLay.add(buttVLay);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(30);
		mainVLay.add(lab1);
		mainVLay.add(lab2);
		mainVLay.add(groupEditHLay);
		mainVLay.add(buttHLay);
		// setOverflow(Overflow.VISIBLE);
		add(mainVLay);

		setSize("600", "300");
		show();
	}

	private void showAddEditDialog() {
		TextItem taxText = new TextItem(FinanceApplication
				.getFinanceUIConstants().itemTax());
		taxText.setColSpan(3);
		RadioGroupItem taxableRadio = new RadioGroupItem();

		taxableRadio.setColSpan(1);
		taxableRadio.setShowTitle(false);
		taxableRadio.setValueMap(FinanceApplication.getFinanceUIConstants()
				.taxable(), FinanceApplication.getFinanceUIConstants()
				.nonTaxable());
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setFields(taxText, taxableRadio);

		Button helpButt = new Button(FinanceApplication.getFinanceUIConstants()
				.help());
		// helpButt.setAutoFit(true);
		Button okButt = new Button(FinanceApplication.getFinanceUIConstants()
				.ok());// okButt.setAutoFit(true);
		Button canButt = new Button(FinanceApplication.getFinanceUIConstants()
				.cancel());// canButt.setAutoFit(true);
		HorizontalPanel helpHLay = new HorizontalPanel();

		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(helpButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setWidth("100%");
		// buttHLay.setMembersMargin(5);
		// buttHLay.setAlign(Alignment.RIGHT);
		buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(okButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		canButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(canButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(30);
		mainVLay.add(form);
		mainVLay.add(buttHLay);
		DialogBox dlg = new DialogBox();
		// dlg.setOverflow(Overflow.VISIBLE);
		dlg.setTitle(FinanceApplication.getFinanceUIConstants().itemTax());
		dlg.add(mainVLay);
		dlg.setSize("320", "150");
		dlg.show();
	}
}
