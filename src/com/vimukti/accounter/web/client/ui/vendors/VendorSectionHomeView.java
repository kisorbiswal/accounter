package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.PortalLayout;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.grids.VendorListGrid;

public class VendorSectionHomeView extends BaseHomeView {

	ArrayList<String> addablePortletList = new ArrayList<String>();

	private VendorSectionHomeView vendorSectionView = null;
	private PortalLayout portalLayout;

	private String vendorSectionPreference;
	private String[] widgetOnSectionPage;

	private Portlet[] portlet;

	private WidgetCreator creator;
	private String[] secondColumn;

	private String[] firstColumn;
	private VendorListGrid listGrid;

	public VendorSectionHomeView() {
		vendorSectionView = this;
		// vendorSectionPreference = FinanceApplication.getUser()
		// .getUserPreferences().getVendorSectionViewPreferences();
	}

	@Override
	public void init() {
		super.init();
		getLeftLayout().add(createControl());
		setSize("100%", "100%");

	}

	private VerticalPanel createControl() {
		creator = new WidgetCreator();

		HorizontalPanel addWidgetLinkLayout = new HorizontalPanel();
		// addWidgetLinkLayout.setHeight(20);

		LinkItem addWidgetLink = new LinkItem();
		addWidgetLink.setLinkTitle(Accounter.constants().addWidget());
		addWidgetLink.setShowTitle(false);
		// addWidgetLink.setAlign(Alignment.RIGHT);

		addWidgetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// new AddWidgetDialog(vendorSectionView);
			}

		});

		DynamicForm form = new DynamicForm();
		form.setItems(addWidgetLink);

		addWidgetLinkLayout.add(form);

		portalLayout = new PortalLayout(this, 2);
		// portalLayout.setWidth100();
		// portalLayout.setHeight100();

		String[] portletArray = { "" };// vendorSectionPreference.split(",");
		if (portletArray.length > 2) {
			widgetOnSectionPage = portletArray;
			portlet = new Portlet[portletArray.length];
			for (int i = 0; i < portletArray.length; i++) {

				final int index = i;
				if (portletArray[i].equals("")) {

				} else {
					// portlet[i] = creator.getWidgetByName(portletArray[i]);
					// portlet[i].addCloseHandler(new CloseHandler() {
					// @Override
					// public void onClose(CloseEvent event) {
					// addablePortletList.add(portlet[index].getName());
					// portlet[index].hide();
					//
					// }
					//
					// });
					// portalLayout.addPortlet(portlet[i]);
				}
			}

		} else {
			firstColumn = portletArray[0].split("~");
			if (portletArray.length > 1) {
				secondColumn = portletArray[1].split("~");
				widgetOnSectionPage = new String[firstColumn.length
						+ secondColumn.length];
			} else {
				widgetOnSectionPage = new String[firstColumn.length];
			}

			int widgetIndex = 0;
			for (int i = 0; i < firstColumn.length; i++) {
				widgetOnSectionPage[widgetIndex] = firstColumn[i];
				widgetIndex++;
			}
			if (portletArray.length > 1) {
				for (int i = 0; i < secondColumn.length; i++) {

					widgetOnSectionPage[widgetIndex] = secondColumn[i];
					widgetIndex++;
				}
			}
			if (portletArray.length > 1) {
				portlet = new Portlet[firstColumn.length + secondColumn.length];
			} else {
				portlet = new Portlet[firstColumn.length];
			}
			for (int i = 0; i < firstColumn.length; i++) {

				final int index = i;
				if (firstColumn[i].equals("")) {

				} else {
					// portlet[i] = creator.getWidgetByName(firstColumn[i]);
					// portlet[i].addCloseHandler(new CloseHandler() {
					//
					// @Override
					// public void onClose(CloseEvent event) {
					// addablePortletList.add(portlet[index].getName());
					// portlet[index].hide();
					//
					// }
					//
					// });
					// portalLayout.addPortlet(portlet[i], 0);
				}
			}

			if (portletArray.length > 1) {
				for (int i = 0; i < secondColumn.length; i++) {

					final int index = i + firstColumn.length;
					if (secondColumn[i].equals("")) {

					} else {
						// portlet[index] = creator
						// .getWidgetByName(secondColumn[i]);
						// portlet[index].addCloseHandler(new CloseHandler() {
						//
						// @Override
						// public void onClose(CloseEvent event) {
						// addablePortletList
						// .add(portlet[index].getName());
						// portlet[index].hide();
						//
						// }
						//
						// });
						// portalLayout.addPortlet(portlet[index], 1);
					}
				}
			}
		}

		getAddableWidgets(widgetOnSectionPage);
		listGrid = new VendorListGrid(false);
		listGrid.init();
		// listGrid.setHeight("400px");
		Accounter.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_VENDOR,
				new AccounterAsyncCallback<ArrayList<PayeeList>>() {

					@Override
					public void onResultSuccess(ArrayList<PayeeList> result) {
						listGrid.clear();
						listGrid.addRecords(result);
					}

					@Override
					public void onException(AccounterException caught) {
					}
				});

		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("100%", "100%");
		// leftLayout.add(addWidgetLinkLayout);
		leftLayout.add(portalLayout);
		leftLayout.setSpacing(10);
		leftLayout.add(listGrid);
		return leftLayout;

	}

	public void getAddableWidgets(String[] widgetOnSectionPage) {
		String[] totalWidget = {
				Accounter.messages().newVendor(Global.get().Vendor()),
				Accounter.constants().itemPurchase(),
				Accounter.constants().billPaid(),
				Accounter.constants().cashPurchase() };

		boolean isAvailable = false;

		for (int i = 0; i < totalWidget.length; i++) {
			for (int k = 0; k < widgetOnSectionPage.length; k++) {
				if (totalWidget[i].equals(widgetOnSectionPage[k])) {
					isAvailable = true;
					break;
				} else {
					isAvailable = false;
				}

			}
			if (!isAvailable) {
				addablePortletList.add(totalWidget[i]);
				isAvailable = false;
			}
		}

	}

	public PortalLayout getPortalLayout() {
		return portalLayout;
	}

	public void setPortalLayout(PortalLayout portalLayout) {
		this.portalLayout = portalLayout;
	}

	public ArrayList<String> getAddablePortletList() {
		return addablePortletList;
	}

	public void setAddablePortletList(ArrayList<String> addablePortletList) {
		this.addablePortletList = addablePortletList;
	}

	@Override
	public void fitToSize(int height, int width) {
//		if (UIUtils.isMSIEBrowser())
//			this.listGrid.setHeight((height - 140) + "px");
//		else
			this.listGrid.setHeight((height - 130) + "px");

	}

	public void setPrevoiusOutput(Object preObject) {
		Accounter.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_VENDOR,
				new AccounterAsyncCallback<ArrayList<PayeeList>>() {

					@Override
					public void onResultSuccess(ArrayList<PayeeList> result) {
						listGrid.clear();
						listGrid.addRecords(result);
					}

					@Override
					public void onException(AccounterException caught) {
					}
				});
	}
}
