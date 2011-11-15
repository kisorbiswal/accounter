package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class CustomTranslationPager extends HorizontalPanel {
	public static final int DEFAULT_START = 0;
	public static int DEFAULT_RANGE = 5;
	private ImageButton previousImage, nextImage;
	private int range, start, last;
	private TranslationView view;
	private List<?> data;
	private Label rangeText;

	public CustomTranslationPager(TranslationView view) {
		this.view = view;
		createControls();
	}

	public CustomTranslationPager(int start, int range, TranslationView view) {
		this.view = view;
		this.start = start;
		this.range = range;
		createControls();
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	private void createControls() {
		previousImage = new ImageButton(Accounter.getFinanceImages()
				.leftArrow());
		nextImage = new ImageButton(Accounter.getFinanceImages().rightArrow());
		rangeText = new Label();
		updateRange();
		rangeText.addStyleName("pager-range");
		previousImage.setVisible(false);
		previousImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPrevious();
			}
		});
		nextImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onNext();
			}
		});
		this.add(previousImage);
		this.add(rangeText);
		this.add(nextImage);
	}

	protected void onPrevious() {
		if (start >= range) {
			if (range != DEFAULT_RANGE) {
				range = DEFAULT_RANGE;
			}
			view.setHaveRecords(true);
			start = start - range;
			view.updateListData();
		}
	}

	private void buttonsVisability() {
		if (start >= range) {
			previousImage.setVisible(true);
		} else {
			previousImage.setVisible(false);
		}
		if (range != DEFAULT_RANGE || (!view.hasMoreRecords)) {
			nextImage.setVisible(false);
		} else {
			nextImage.setVisible(true);
		}
	}

	private void onNext() {
		view.setHaveRecords(true);
		start = start + range;
		view.updateListData();
	}

	public void updateRange() {
		last = start + range;
		rangeText.setText(Accounter.messages().range(start, last));
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public List<?> getData() {
		return data;
	}

	public void updateData(ArrayList<ClientMessage> result) {
		buttonsVisability();
		setData(result);
		setRange(result.size());
		updateRange();
		setVisible(true);
	}
}
