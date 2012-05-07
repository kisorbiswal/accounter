/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

/**
 * @author Administrator
 * 
 */
public class CompanyTimeZoneOption extends AbstractPreferenceOption {

	SelectCombo timeZoneListBox;
	private List<String> timezones;

	interface CompanyTimeZoneOptionUiBinder extends
			UiBinder<Widget, CompanyTimeZoneOption> {
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
	public CompanyTimeZoneOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public void createControls() {
		timeZoneListBox = new SelectCombo(messages.timezone());
		timeZoneListBox.addStyleName("header");
		this.timezones = CoreUtils.getTimeZonesAsList();
		for (String tz : timezones) {
			timeZoneListBox.addItem(tz);
		}

		String defalutTzOffset = getDefaultTzOffsetStr();
		for (String tz : timezones) {
			if (tz.startsWith(defalutTzOffset)) {
				timeZoneListBox.setSelectedItem(timezones.indexOf(tz));
				break;
			}
		}
		add(timeZoneListBox);

	}

	private String getDefaultTzOffsetStr() {
		Date date = new Date();
		DateTimeFormat tzFormat = DateTimeFormat.getFormat("z");
		return tzFormat.format(date);
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public String getTitle() {
		return messages.timezone();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().getTimezone() != ""
				&& getCompanyPreferences().getTimezone() != null) {
			this.timeZoneListBox.setSelectedItem((timezones
					.indexOf(getCompanyPreferences().getTimezone())));
		}
	}

	@Override
	public void onSave() {
		if (timeZoneListBox.getSelectedIndex() != -1)
			getCompanyPreferences().setTimezone(
					timezones.get(timeZoneListBox.getSelectedIndex()));
	}
}
