package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class PortletPage extends FlowPanel implements DragHandler {

	public static final String DASHBOARD = "dashboard";

	private String name;
	public ClientPortletPageConfiguration config;
	private PortletColumn[] columns;
	private PickupDragController dragController;
	public boolean haveToRefresh = true;

	public PortletPage(String pageName) {
		this.name = pageName;
		refreshPage();
		this.getElement().setId("PortletPage");
	}

	public void refreshPage() {
		this.addStyleName("portletPage");
		config = Accounter.getCompany().getPortletPageConfiguration(name);
		setup();
		refreshWidgets();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setup() {
		StyledPanel panel = new StyledPanel("portletPagePanel");
		this.add(panel);
		dragController = new PickupDragController(this, false);
		dragController.setBehaviorMultipleSelection(false);
		dragController.addDragHandler(this);
		// create columns
		columns = new PortletColumn[config.getColumnsCount()];
		for (int x = 0; x < config.getColumnsCount(); x++) {
			columns[x] = new PortletColumn(x);
			panel.add(columns[x]);
			columns[x].getElement().getParentElement()
					.addClassName("portlet_column_parent");
			dragController.registerDropController(columns[x]
					.getDropController());
		}
		// create the portlets in them
		for (ClientPortletConfiguration pc : config.getPortletConfigurations()) {
			addPortletToPage(pc);
		}

	}

	private void addPortletToPage(ClientPortletConfiguration pc) {
		Portlet portlet = createPortlet(pc);
		if (portlet != null) {
			portlet.setPortletPage(this);
			columns[pc.column].addPortlet(portlet);
			dragController.makeDraggable(portlet, portlet.getHeader());
		}
	}

	public PortletPageConfigureDialog createSettingsDialog() {
		updateConfiguration();
		PortletPageConfigureDialog configureDialog = new PortletPageConfigureDialog(
				Global.get().messages().configurePortlets(), config, this);
		return configureDialog;
	}

	private Portlet createPortlet(ClientPortletConfiguration pc) {
		return PortletFactory.get().createPortlet(pc, this);
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		updateConfiguration();
		haveToRefresh = false;
		updatePortletPage();
	}

	public void updatePortletPage() {
		if (config.getPageName() == null) {
			config.setPageName(name);
		}
		Accounter.createHomeService().savePortletPageConfig(config,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean arg0) {
						Accounter.getCompany().setPortletConfiguration(config);
						if (arg0 && haveToRefresh) {
							clear();
							refreshPage();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						System.err.println(arg0.toString());
					}
				});
	}

	public void updateConfiguration() {
		ArrayList<ClientPortletConfiguration> configs = new ArrayList<ClientPortletConfiguration>();
		for (PortletColumn column : columns) {
			for (Portlet portlet : column.getPortlets()) {
				configs.add(portlet.getConfiguration());
			}
		}
		config.setPortletsConfiguration(configs);
		config.setPageName(name);
	}

	@Override
	public void onDragStart(DragStartEvent event) {

	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {

	}

	public DragController getDragController() {
		return dragController;
	}

	public void refreshWidgets() {
		if (columns != null)
			for (PortletColumn column : this.columns) {
				column.refreshWidgets();
			}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		haveToRefresh = false;
		updatePortletPage();
	}

}
