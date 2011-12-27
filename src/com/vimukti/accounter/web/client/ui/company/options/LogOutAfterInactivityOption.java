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
import com.vimukti.accounter.web.client.ui.Accounter;

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

	public void createControls() {
		signoutAfterInactivity.setTitle(messages.signoutafterInactivity());
		signoutAfterInactivity.addItem(messages.HourNumber(1));
		signoutAfterInactivity.addItem(messages.HourNumber(2));
		signoutAfterInactivity.addItem(messages.HourNumber(3));
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
		return Accounter.messages().LogOutAfterInactivity();
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
