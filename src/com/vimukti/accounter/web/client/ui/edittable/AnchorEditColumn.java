package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;

public abstract class AnchorEditColumn<T> extends EditColumn<T> {

	@Override
	public int getWidth() {
		return -1;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		Anchor anchor = (Anchor) widget;
		String value = getValue(context.getRow());
		anchor.setText(value);
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		Anchor anchor = new Anchor();
		configure(anchor);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isEnable() && !context.isDesable()) {
					AnchorEditColumn.this.onClick(context.getRow());
				}
			}
		});
		return anchor;
	}

	protected boolean isEnable() {
		return true;
	}

	protected abstract void onClick(T row);

	protected abstract String getValue(T row);

	protected void configure(Anchor anchor) {
		anchor.addStyleName("anchor");
	}

}
