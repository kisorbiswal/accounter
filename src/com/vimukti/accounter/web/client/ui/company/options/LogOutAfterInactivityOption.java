/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class LogOutAfterInactivityOption extends AbstractPreferenceOption {

	private static LogOutAfterInactivityOptionUiBinder uiBinder = GWT
			.create(LogOutAfterInactivityOptionUiBinder.class);
	@UiField
	ListBox signoutAfterInactivity;

	interface LogOutAfterInactivityOptionUiBinder extends
			UiBinder<Widget, LogOutAfterInactivityOption> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public LogOutAfterInactivityOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		signoutAfterInactivity.setTitle(constants.signoutafterInactivity());
		signoutAfterInactivity.addItem("1 Hour");
		signoutAfterInactivity.addItem("2 Hour");
		signoutAfterInactivity.addItem("3 Hour");
		signoutAfterInactivity.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	public LogOutAfterInactivityOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
