package com.vimukti.accounter.web.client.ui;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.portlet.PortletPage;

public class DashBoardView extends BaseHomeView {

	private PortletPage page;

	public DashBoardView() {

	}

	@Override
	public void init() {
		super.init();
		getLeftLayout().add(createControl());
		setSize("100%", "100%");

	}

	private Widget createControl() {
		setPage(new PortletPage(PortletPage.DASHBOARD));
		AbsolutePanel absolutePanel = new AbsolutePanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		VerticalPanel vPanel_1 = new VerticalPanel();
		VerticalPanel vPanel_2 = new VerticalPanel();

		VerticalPanel panel1 = new VerticalPanel();
		VerticalPanel panel2 = new VerticalPanel();
		VerticalPanel panel3 = new VerticalPanel();
		VerticalPanel panel4 = new VerticalPanel();
		Label label_1 = new Label("First Label");
		Label label_2 = new Label("Second Label");
		Label label_3 = new Label("Third Label");
		Label label_4 = new Label("Fourth Label");

		Label label_5 = new Label("First Label Child");
		Label label_6 = new Label("Second Label Child");
		Label label_7 = new Label("Third Label Child");
		Label label_8 = new Label("Fourth Label Child");

		SimplePanel panel = new SimplePanel();
		panel.add(panel1);

		panel1.add(label_1);
		panel1.add(label_5);

		panel2.add(label_2);
		panel2.add(label_6);

		vPanel_1.add(panel1);
		vPanel_1.add(panel2);

		panel3.add(label_3);
		panel3.add(label_7);

		panel4.add(label_4);
		panel4.add(label_8);

		vPanel_2.add(panel3);
		vPanel_2.add(panel4);

		hPanel.add(vPanel_1);
		hPanel.add(vPanel_2);

		absolutePanel.add(hPanel);

		// PickupDragController columnDragController = new PickupDragController(
		// absolutePanel, false);
		// columnDragController.setBehaviorMultipleSelection(false);

		PickupDragController widgetDragController = new PickupDragController(
				absolutePanel, false);
		widgetDragController.setBehaviorMultipleSelection(false);
		// HorizontalPanelDropController columnDropController = new
		// HorizontalPanelDropController(
		// hPanel);
		// columnDragController
		// .registerDropController(columnDropController);

		VerticalPanelDropController widgetDropController1 = new VerticalPanelDropController(
				vPanel_1);
		widgetDragController.registerDropController(widgetDropController1);

		VerticalPanelDropController widgetDropController2 = new VerticalPanelDropController(
				vPanel_2);
		widgetDragController.registerDropController(widgetDropController2);

		// widgetDragController.registerDropController(widgetDropController1);
		// widgetDragController.registerDropController(widgetDropController2);

		widgetDragController.makeDraggable(panel, label_1);
		widgetDragController.makeDraggable(panel2, label_2);
		widgetDragController.makeDraggable(panel3, label_3);
		// widgetDragController.makeDraggable(label_3);
		// widgetDragController.makeDraggable(label_4);
		widgetDragController.makeDraggable(panel4, label_4);

		return getPage();

		// create absolute panel

		// horizontal panel with 100% width and height
		// two vertical panels
		// 2 lables in each with size 100px,100px

		// return absolute panel
	}

	@Override
	protected void onAttach() {
		getPage().refreshWidgets();
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void refreshWidgetData() {
	}

	public void showGettingStarted() {
	}

	public void hideGettingStarted() {
	}

	public PortletPage getPage() {
		return page;
	}

	public void setPage(PortletPage page) {
		this.page = page;
	}

}
