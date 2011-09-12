/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.company.options.AbstractPreferenceOption;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PreferencePage extends ScrollPanel {

	private String title;

	private List<AbstractPreferenceOption> options = new ArrayList<AbstractPreferenceOption>();

	private VerticalPanel optionsPane = new VerticalPanel();

	/**
	 * Creates new Instance
	 */
	public PreferencePage(String title) {
		this.title = title;
		this.setHeight("250px");
		this.add(this.optionsPane);
	}

	public void addPreferenceOption(AbstractPreferenceOption option) {
		optionsPane.add(option);
		this.options.add(option);
	}

	public void removePreferenceOption(AbstractPreferenceOption option) {
		optionsPane.remove(option);
		this.options.remove(option);
	}

	public void onSave() {
		for (AbstractPreferenceOption option : options) {
			option.onSave();
		}
	}

	public void onLoad() {
		for (AbstractPreferenceOption option : options) {
			option.onLoad();
		}
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 */
	public List<AbstractPreferenceOption> getOptions() {
		return this.options;
	}

	/**
	 * @param option
	 */
	public void show(AbstractPreferenceOption option) {
		ensureVisible(option);
	}

}
