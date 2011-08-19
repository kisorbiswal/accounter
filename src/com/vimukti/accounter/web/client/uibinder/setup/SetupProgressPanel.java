/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Administrator
 *
 */
public class SetupProgressPanel extends AbstractSetupPage {

	private static SetupProgressPanelUiBinder uiBinder = GWT
			.create(SetupProgressPanelUiBinder.class);
	@UiField VerticalPanel viewPanel;
	@UiField Label startUp;
	@UiField Label companyInformation;
	@UiField Label industryType;
	@UiField Label organization;
	@UiField Label refferingName;
	@UiField Label trackEmployee;
	@UiField Label sell;
	@UiField Label estimates;
	@UiField Label currency;
	@UiField Label billTracking;
	@UiField Label fiscalYear;
	@UiField Label requiredAccounts;
	@UiField Image setupImage;
	@UiField Image companyInfoImage;
	@UiField Image industryTypeImage;
	@UiField Image organizationImage;
	@UiField Image referringImage;
	@UiField Image trackImage;
	@UiField Image sellImage;
	@UiField Image estimateImage;
	@UiField Image currencyImage;
	@UiField Image billTrackingImage;
	@UiField Image fiscalYearImage;
	@UiField Image requiredImage;

	interface SetupProgressPanelUiBinder extends
			UiBinder<Widget, SetupProgressPanel> {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public SetupProgressPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected VerticalPanel getViewBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub
		
	}

	
}
