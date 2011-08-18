/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupStartPage extends Composite implements HasText {

	private static SetupStartPageUiBinder uiBinder = GWT
			.create(SetupStartPageUiBinder.class);

	interface SetupStartPageUiBinder extends UiBinder<Widget, SetupStartPage> {
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
	public SetupStartPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SetupStartPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

		// Can access @UiField after calling createAndBindUi
	}

	public void setText(String text) {
	}

	/**
	 * Gets invoked when the default constructor is called and a string is
	 * provided in the ui.xml file.
	 */
	public String getText() {
		return null;
	}
creatControls(){
	s
}
}
