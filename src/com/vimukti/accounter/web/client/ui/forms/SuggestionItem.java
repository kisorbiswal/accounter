package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class SuggestionItem extends FormItem {

	SuggestBox suggestBox;
	MultiWordSuggestOracle multiWordSuggestOracle;

	public SuggestionItem(MultiWordSuggestOracle multiWordSuggestOracle,
			String title) {
		this.multiWordSuggestOracle = multiWordSuggestOracle;
		this.setTitle(title);
	}

	@Override
	public Widget getMainWidget() {
		if (suggestBox == null)
			suggestBox = new SuggestBox(multiWordSuggestOracle);
		return suggestBox;
	}

	@Override
	public void setValue(Object value) {
		suggestBox.setValue(value.toString());
		super.setValue(value);
	}

	@Override
	public String getValue() {
		return suggestBox.getValue();
	}

	public void setMultiWordSuggestion(MultiWordSuggestOracle suggestOracle) {
		this.multiWordSuggestOracle = suggestOracle;
	}

	public void setDisabledForSuggBox(boolean enabled) {
		super.isDisabled = enabled;
		DOM.setElementPropertyBoolean(this.suggestBox.getElement(), "disabled",
				super.isDisabled);
	}
}
