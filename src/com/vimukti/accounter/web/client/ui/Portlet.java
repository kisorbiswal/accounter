package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.portlet.PortletPage;
import com.vimukti.accounter.web.client.ui.widgets.WorkbenchPanel;

/**
 * 
 * @author Gajendra Choudhary
 * 
 * 
 */

public abstract class Portlet extends WorkbenchPanel {
	protected static AccounterMessages messages = Accounter.messages();

	public static final int TYPE_I_OWE = 1;
	public static final int TYPE_OWE_TO_ME = 2;

	private ClientCompanyPreferences preferences = Global.get().preferences();
	private HTML title = new HTML();
	private String name;
	protected Label all;
	private ScrollPanel vPanel;
	private int previousIndex;
	public HTML refresh;
	private int row;
	private int column;
	public VerticalPanel body;
	private ClientPortletConfiguration configuration;
	private PortletPage portletPage;

	protected boolean isInitializing;

	public Portlet(ClientPortletConfiguration configuration, String title,
			String gotoString) {
		this(title, gotoString);
		setName(title);
		this.configuration = configuration;
	}

	public Portlet(String title, String gotoString) {
		super(title, gotoString);
		vPanel = new ScrollPanel();
		body = new VerticalPanel();
		body.setStyleName("portlet-body");
		this.setSize("100%", "100%");
		addStyleName("portlet");
		vPanel.add(body);
		vPanel.setWidth("100%");
		super.add(vPanel);

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public void setTitle(String title) {
		super.setTitle(title);
		this.title.setHTML(title);
		this.title.setStyleName("portletLabel");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void add(Widget w) {
		this.vPanel.add(w);
	}

	public void fitToSize(int width, int height) {
		width = Math.max(width, 300);
		height = Math.max(height, 100);
		this.setWidth(width + "px");
		this.setHeight(height + "px");
		// setGridWidth(width - 10, height - 40);
	}

	public void createBody() {

	}

	@Override
	public void titleClicked() {
		super.titleClicked();
		goToClicked();
	}

	public void goToClicked() {

	}

	public void refreshWidget() {
		isInitializing = true;
	}

	protected void completeInitialization() {
		isInitializing = false;
	}

	public void refreshClicked() {

	}

	public void helpClicked() {

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setPreviousIndex(int widgetIndex) {
		this.previousIndex = widgetIndex;

	}

	public int getPreviousIndex() {
		return this.previousIndex;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public String getDecimalCharacter() {
		return getPreferences().getDecimalCharacter();
	}

	public String amountAsString(Double amount) {
		return DataUtils.getAmountAsString(amount);
	}

	public String getPrimaryCurrencySymbol() {
		return getPreferences().getPrimaryCurrency().getSymbol();
	}

	public ClientPortletConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected boolean canClose() {
		return true;
	}

	@Override
	protected boolean canConfigure() {
		return false;
	}

	@Override
	protected void onClose() {
		this.removeFromParent();
		portletPage.config.getPortletConfigurations().remove(configuration);
		portletPage.haveToRefresh = false;
		portletPage.updatePortletPage();
	}

	@Override
	protected void onConfigure() {

	}

	public PortletPage getPortletPage() {
		return portletPage;
	}

	public void setPortletPage(PortletPage portletPage) {
		this.portletPage = portletPage;
	}

	public boolean isInitializing() {
		return isInitializing;
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		vPanel.setHeight(height);
	}
}
