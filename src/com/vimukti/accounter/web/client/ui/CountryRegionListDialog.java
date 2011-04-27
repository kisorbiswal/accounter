package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
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
	private String[] typeRecords = {
			FinanceApplication.getFinanceUIConstants().us(),
			FinanceApplication.getFinanceUIConstants().india() };

	public CountryRegionListDialog() {
		setModal(true);
		// setShowModalMask(true);
		// setModalMaskOpacity(10);
		// setShowEdges(false);
		// setHeaderStyle("widget");
		// setBackgroundColor("white");
		// setBorder("4px solid #595959");
		createControls();

	}

	private void createControls() {
		setTitle(FinanceApplication.getFinanceUIConstants()
				.manageCountryRegionList());

		Label lab1 = new Label(FinanceApplication.getFinanceUIConstants()
				.countryListLabel());
		// lab1.setAutoFit(true);
		// lab1.setWrap(false);

		Label lab2 = new Label(FinanceApplication.getFinanceUIConstants()
				.addCountryLabel());
		// lab2.setAutoFit(true);lab2.setBackgroundColor("green");
		lab2.setHeight("1");
		// lab2.setOverflow(Overflow.VISIBLE);
		// lab2.setWrap(false);
		lab2.setWidth("100%");

		grid = new DialogGrid(false);
		// typeGrid.setAutoFitData(Autofit.BOTH);
		// typeGrid.setOverflow(Overflow.SCROLL);
		grid.setSize("100%", "100%");
		// grid.setShowAllRecords(true);
		grid.addColumns(new String[] { FinanceApplication
				.getFinanceUIConstants().country() });
		// ListGridField nameField = new ListGridField("name",
		// "<center><b>Country/Region</b></center>");
		// businessTypeField.setAlign(Alignment.CENTER);
		// grid.setFields(nameField);
		// grid.setCanResizeFields(true);
		createListGridRecords(typeRecords);

		addGroupButt = new Button(FinanceApplication.getFinanceUIConstants()
				.add());
		addGroupButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showAddEditGroupDialog();
			}
		});
		editGroupButt = new Button(FinanceApplication.getFinanceUIConstants()
				.edit());
		remGroupButt = new Button(FinanceApplication.getFinanceUIConstants()
				.remove());

		helpButt = new Button(FinanceApplication.getFinanceUIConstants().help());
		closeButt = new Button(FinanceApplication.getFinanceUIConstants()
				.close());

		HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setAlign(Alignment.LEFT);
		helpHLay.add(helpButt);
		if (helpButt.isEnabled()) {
			helpButt.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(helpButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		HorizontalPanel closeHLay = new HorizontalPanel();
		// closeHLay.setAlign(Alignment.RIGHT);
		closeHLay.add(closeButt);
		if (closeButt.isEnabled()) {
			closeButt.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(closeButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSize("100%", "10%");
		buttHLay.add(helpHLay);
		buttHLay.add(closeHLay);

		VerticalPanel buttVLay = new VerticalPanel();
		// buttVLay.setMembersMargin(5);
		buttVLay.add(addGroupButt);
		buttVLay.add(editGroupButt);
		buttVLay.add(remGroupButt);
		if (addGroupButt.isEnabled()) {
			addGroupButt.getElement().getParentElement()
					.setClassName("ibutton");
			ThemesUtil.addDivToButton(addGroupButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (editGroupButt.isEnabled()) {
			editGroupButt.getElement().getParentElement().setClassName(
					"ibutton");
			ThemesUtil.addDivToButton(editGroupButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (remGroupButt.isEnabled()) {
			remGroupButt.getElement().getParentElement()
					.setClassName("ibutton");
			ThemesUtil.addDivToButton(remGroupButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}

		HorizontalPanel groupEditHLay = new HorizontalPanel();
		// groupEditHLay.setSize("100%", "*");
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
		setSize("450", "300");
		show();
	}

	private void createListGridRecords(String[] records) {
		for (Object obj : records) {
			grid.addData((IsSerializable) obj);
		}
	}

	private void showAddEditGroupDialog() {
		DialogBox dlg = new DialogBox();
		dlg.setTitle(FinanceApplication.getFinanceUIConstants().country());
		dlg.setModal(true);
		DynamicForm form = new DynamicForm();
		// form.setSize("100%", "*");
		TextItem nameText = new TextItem(FinanceApplication
				.getFinanceUIConstants().countryName());
		// nameText.setWrapTitle(false);
		nameText.setRequired(true);
		form.setFields(nameText);

		TextItem a3Text = new TextItem(FinanceApplication
				.getFinanceUIConstants().a3Code());
		a3Text.setColSpan(1);
		TextItem a2Text = new TextItem(FinanceApplication
				.getFinanceUIConstants().a2Code());
		a2Text.setColSpan(1);
		TextItem isoText = new TextItem(FinanceApplication
				.getFinanceUIConstants().ISOCode());
		isoText.setColSpan(3);

		DynamicForm codeForm = new DynamicForm();
		codeForm.setIsGroup(true);
		codeForm.setGroupTitle(FinanceApplication.getFinanceUIConstants()
				.countryCode());
		// codeForm.setWrapItemTitles(false);
		codeForm.setWidth("100%");
		codeForm.setNumCols(4);
		codeForm.setFields(a3Text, a2Text, isoText);

		Button helpButt = new Button(FinanceApplication.getFinanceUIConstants()
				.help());
		// helpButt.setAutoFit(true);
		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.add(helpButt);
		helpHLay.setWidth("50%");
		Button okButt = new Button(FinanceApplication.getFinanceUIConstants()
				.ok());
		// okButt.setAutoFit(true);
		Button canButt = new Button(FinanceApplication.getFinanceUIConstants()
				.cancel());
		// canButt.setAutoFit(true);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setAlign(Alignment.RIGHT);
		// buttHLay.setSize("100%", "*");
		// buttHLay.setMembersMargin(5);
		// buttHLay.setLeft(20);
		buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		canButt.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(50);
		mainVLay.setSize("100%", "100%");
		// mainVLay.setMembers(form, codeForm, buttHLay);
		dlg.add(mainVLay);
		dlg.setSize("450", "300");
		dlg.show();
	}
}
