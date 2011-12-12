package com.vimukti.accounter.web.client.ui.translation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class Pager extends HorizontalPanel {
	public static final int DEFAULT_START = 0;
	public static int DEFAULT_RANGE = 0;

	private HorizontalPanel pagerPanel;
	private ImageButton previousButton, nextButton;
	private Label rangeLabel;
	private int totalResultCount, presentResultCount, startRange, endRange,
			range;
	private AbstractPagerView<?> pagerView;
	private boolean isNext;
	private boolean isBack;

	public Pager(int range, AbstractPagerView<?> pagerView) {
		createControls();
		DEFAULT_RANGE = range;
		refreshPager();
		this.pagerView = pagerView;
	}

	public void refreshPager() {
		this.setRange(DEFAULT_RANGE);
		this.setStartRange(DEFAULT_START);
		endRange = getStartRange() + getRange();
		this.updateRange();
	}

	public void updateRange() {
		rangeLabel.setText(Accounter.messages()
				.range(getStartRange(), endRange));
	}

	private void createControls() {
		pagerPanel = new HorizontalPanel();
		previousButton = new ImageButton(Accounter.getFinanceImages()
				.leftArrow());
		nextButton = new ImageButton(Accounter.getFinanceImages().rightArrow());
		rangeLabel = new Label();
		rangeLabel.addStyleName("pager-range");
		previousButton.setVisible(false);
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPrevious();
			}
		});
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onNext();
			}
		});
		pagerPanel.add(previousButton);
		pagerPanel.add(rangeLabel);
		pagerPanel.add(nextButton);
		this.add(pagerPanel);
		this.setWidth("100%");
		this.setCellHorizontalAlignment(pagerPanel, HasAlignment.ALIGN_CENTER);
	}

	protected void onNext() {
		isNext = true;
		isBack = false;
		setStartRange(getStartRange() + getRange());
		pagerView.updateListData();
	}

	protected void updateRangeData() {
		if (isNext) {
			nextRange();
		}
		if (isBack) {
			presentResultCount = pagerView.getDataSize();
			setStartRange(endRange - presentResultCount);
		}
		if (!isNext && !isBack) {
			nextRange();
		}
		buttonVisability();
	}

	private void nextRange() {
		presentResultCount = pagerView.getDataSize();
		endRange = getStartRange() + presentResultCount;
	}

	protected void initData() {
		onNext();
	}

	private void buttonVisability() {
		if (startRange >= range) {
			previousButton.setVisible(true);
		} else {
			previousButton.setVisible(false);
		}
		if ((totalResultCount != 0) && (totalResultCount > endRange)) {
			pagerView.hasMoreRecords = true;
		} else {
			pagerView.hasMoreRecords = false;
		}
		if (range != DEFAULT_RANGE || (!pagerView.hasMoreRecords)) {
			nextButton.setVisible(false);
			// range = DEFAULT_RANGE;
		} else {
			nextButton.setVisible(true);
		}
		updateRange();
	}

	protected void onPrevious() {
		isNext = false;
		isBack = true;
		endRange = getStartRange();
		startRange = startRange - range;
		pagerView.updateListData();

	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	public int getPresentResultCount() {
		return presentResultCount;
	}

	public void setPresentResultCount(int presentResultCount) {
		this.presentResultCount = presentResultCount;
	}

	public int getStartRange() {
		return startRange;
	}

	public void setStartRange(int startRange) {
		this.startRange = startRange;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

}
