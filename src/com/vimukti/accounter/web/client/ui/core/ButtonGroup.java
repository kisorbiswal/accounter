package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ButtonGroup extends HorizontalPanel {

	public static int BACK_NEXT_BUTTON_GROUP = 1;
	public static int PRINT_BUTTON_GROUP = 2;
	public static int EDIT_BUTTON_GROUP = 3;
	public static int ADD_BUTTON_GROUP = 4;
	public static int CLOSE_BUTTON_GROUP = 5;
	public static int TITLE_BUTTON_GROUP = 6;

	private Image nextbutton, previousButton, closeButton, printButton,
			editButton, exportCsvButton;
	private AccounterButton addButton;
	private ViewManager manager;

	public ButtonGroup(int buttonGroup) {

		createControls(buttonGroup);

	}

	public void createControls(int buttonGroupType) {
		if (buttonGroupType == BACK_NEXT_BUTTON_GROUP) {
			add(addFrontBackButtons());
		} else if (buttonGroupType == PRINT_BUTTON_GROUP) {
			add(addPrintButtons());
		} else if (buttonGroupType == EDIT_BUTTON_GROUP) {
			add(addEditButton());
		} else if (buttonGroupType == CLOSE_BUTTON_GROUP) {
			add(addCloseButton());
		} else if (buttonGroupType == ADD_BUTTON_GROUP) {
			add(addAddButton());
		}
	}

	public Widget addAddButton() {
		HorizontalPanel panel = new HorizontalPanel();
		addButton = new AccounterButton("Add Button");
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onAddButtonClick();
			}
		});
		panel.add(addButton);
		addButton.enabledButton();
		return panel;
	}

	public Widget addCloseButton() {
		HorizontalPanel closePanel = new HorizontalPanel();
		closeButton = new Image("/images/dialog-close.png");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onCloseButtonClick();
			}
		});
		closePanel.add(closeButton);
		return closeButton;
	}

	public Widget addEditButton() {
		HorizontalPanel editPanel = new HorizontalPanel();
		editButton = new Image("/images/Page_edit1.png");
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onEditButtonClick();
			}
		});
		editPanel.add(editButton);
		return editButton;
	}

	public Widget addPrintButtons() {
		HorizontalPanel printPanel = new HorizontalPanel();
		exportCsvButton = new Image("/images/export-icon.png");
		exportCsvButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onExportCsvClick();
			}
		});
		printButton = new Image("/images/Print1.png");
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPrintButtonClick();
			}
		});
		printPanel.add(exportCsvButton);
		printPanel.add(printButton);
		return printPanel;
	}

	public Widget addFrontBackButtons() {
		HorizontalPanel previousNextPanel = new HorizontalPanel();
		previousButton = new Image("/images/icons/arrow2.png");
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPreviousButtonClick();
			}
		});
		nextbutton = new Image("/images/icons/arrow1.png");
		nextbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onNextButtonClick();
			}
		});
		previousNextPanel.add(previousButton);
		previousNextPanel.add(nextbutton);
		return previousNextPanel;
	}

	protected void onNextButtonClick() {

	}

	protected void onPreviousButtonClick() {

	}

	protected void onCloseButtonClick() {
		
	}

	protected void onAddButtonClick() {

	}

	protected void onPrintButtonClick() {

	}

	protected void onExportCsvClick() {

	}

	protected void onEditButtonClick() {

	}

}
