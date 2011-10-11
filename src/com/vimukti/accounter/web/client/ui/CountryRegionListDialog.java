package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class CountryRegionListDialog extends DialogBox {
	private DialogGrid grid;
	private Button addGroupButt, editGroupButt, remGroupButt;
	private Button helpButt, closeButt;
	private String[] typeRecords = { Accounter.constants().us(),
			Accounter.constants().india() };

	public CountryRegionListDialog() {
		setModal(true);
		createControls();

	}

	private void createControls() {
		Label lab1 = new Label(Accounter.constants().countryListLabel());

		Label lab2 = new Label(Accounter.constants().addCountryLabel());
		lab2.setHeight("1px");
		lab2.setWidth("100%");

		grid = new DialogGrid(false);
		grid.setSize("100%", "100%");
		grid.addColumns(new String[] { Accounter.constants().country() });
		createListGridRecords(typeRecords);

		addGroupButt = new Button(Accounter.constants().add());
		addGroupButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showAddEditGroupDialog();
			}
		});
		editGroupButt = new Button(Accounter.constants().edit());
		remGroupButt = new Button(Accounter.constants().remove());

		helpButt = new Button(Accounter.constants().help());
		closeButt = new Button(Accounter.constants().close());

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
		buttVLay.add(addGroupButt);
		buttVLay.add(editGroupButt);
		buttVLay.add(remGroupButt);

		HorizontalPanel groupEditHLay = new HorizontalPanel();
		groupEditHLay.add(grid);
		groupEditHLay.add(buttVLay);

		closeButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

		VerticalPanel MVLay = new VerticalPanel();
		MVLay.setSize("100%", "100%");
		MVLay.add(lab1);
		MVLay.add(lab2);
		MVLay.add(groupEditHLay);
		MVLay.add(buttHLay);

		add(MVLay);
		setWidth("450px");
		show();
	}

	private void createListGridRecords(String[] records) {
		for (Object obj : records) {
			grid.addData((IsSerializable) obj);
		}
	}

	private void showAddEditGroupDialog() {
		DialogBox dlg = new DialogBox();
		dlg.setTitle(Accounter.constants().country());
		dlg.setModal(true);
		DynamicForm form = new DynamicForm();
		TextItem nameText = new TextItem(Accounter.constants().countryName());
		nameText.setRequired(true);
		form.setFields(nameText);

		TextItem a3Text = new TextItem(Accounter.constants().a3Code());
		a3Text.setColSpan(1);
		TextItem a2Text = new TextItem(Accounter.constants().a2Code());
		a2Text.setColSpan(1);
		TextItem isoText = new TextItem(Accounter.constants().isoCode());
		isoText.setColSpan(3);

		DynamicForm codeForm = new DynamicForm();
		codeForm.setIsGroup(true);
		codeForm.setGroupTitle(Accounter.constants().countryCode());
		codeForm.setWidth("100%");
		codeForm.setNumCols(4);
		codeForm.setFields(a3Text, a2Text, isoText);

		Button helpButt = new Button(Accounter.constants().help());
		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.add(helpButt);
		helpHLay.setWidth("50%");
		Button okButt = new Button(Accounter.constants().ok());
		Button canButt = new Button(Accounter.constants().cancel());

		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		canButt.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		dlg.add(mainVLay);
		dlg.setSize("450px", "300px");
		dlg.show();
	}

}
