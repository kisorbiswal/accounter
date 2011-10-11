package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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

		Label lab1 = new Label(Accounter.constants().itemTaxCode());
		// lab1.setWrap(false);
		// lab1.setAutoFit(true);

		Label lab2 = new Label(Accounter.constants().toAddAnItemTax());
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

		Button addButt = new Button(Accounter.constants().add3dots());
		addButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showAddEditDialog();
			}
		});
		Button editButt = new Button(Accounter.constants().edit());
		Button remButt = new Button(Accounter.constants().remove());

		Button helpButt = new Button(Accounter.constants().help());
		Button closeButt = new Button(Accounter.constants().close());

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setAlign(Alignment.LEFT);
		helpHLay.add(helpButt);
		HorizontalPanel closeHLay = new HorizontalPanel();
		// closeHLay.setAlign(Alignment.RIGHT);
		closeHLay.add(closeButt);
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSize("100%", "10%");
		buttHLay.add(helpHLay);
		buttHLay.add(closeHLay);

		VerticalPanel buttVLay = new VerticalPanel();
		buttVLay.add(addButt);
		buttVLay.add(editButt);
		buttVLay.add(remButt);
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

		setWidth("600px");
		show();
	}

	private void showAddEditDialog() {
		TextItem taxText = new TextItem(Accounter.constants().itemTax());
		taxText.setColSpan(3);
		RadioGroupItem taxableRadio = new RadioGroupItem();

		taxableRadio.setColSpan(1);
		taxableRadio.setShowTitle(false);
		taxableRadio.setValueMap(Accounter.constants().taxable(), Accounter
				.constants().nonTaxable());
		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setFields(taxText, taxableRadio);

		Button helpButt = new Button(Accounter.constants().help());
		// helpButt.setAutoFit(true);
		Button okButt = new Button(Accounter.constants().ok());// okButt.setAutoFit(true);
		Button canButt = new Button(Accounter.constants().cancel());// canButt.setAutoFit(true);
		HorizontalPanel helpHLay = new HorizontalPanel();

		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setWidth("100%");
		// buttHLay.setMembersMargin(5);
		// buttHLay.setAlign(Alignment.RIGHT);
		buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(30);
		mainVLay.add(form);
		mainVLay.add(buttHLay);
		DialogBox dlg = new DialogBox();
		// dlg.setOverflow(Overflow.VISIBLE);
		dlg.setTitle(Accounter.constants().itemTax());
		dlg.add(mainVLay);
		dlg.setSize("320px", "150px");
		dlg.show();
	}
	// setTitle(FinanceApplication.constants().manageItemTaxCode());
}
