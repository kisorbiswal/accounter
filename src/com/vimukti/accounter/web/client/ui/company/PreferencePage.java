/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.ui.company.options.AbstractPreferenceOption;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PreferencePage extends ScrollPanel {

	private String title;

	private List<AbstractPreferenceOption> options = new ArrayList<AbstractPreferenceOption>();

	/**
	 * Creates new Instance
	 */
	public PreferencePage(String title) {
		this.title = title;
	}

	void addPreferenceOption(AbstractPreferenceOption option) {
		this.add(option);
		this.options.add(option);
	}

	void removePreferenceOption(AbstractPreferenceOption option) {
		this.remove(option);
		this.options.remove(option);
	}

	void onSave() {
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
