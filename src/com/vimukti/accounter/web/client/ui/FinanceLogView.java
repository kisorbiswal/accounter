/**
 * @author Murali.A
 *This class displays the log records.
 *Each time 20 records only will be displayed in the grid
 */
package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceLogger;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.FinanceLogginGrid;

/**
 * @author Murali.A
 * 
 */
public class FinanceLogView extends AbstractBaseView<ClientFinanceLogger> {

	public ScrollPanel messageTxtPnl;
	private FinanceLogginGrid grid;
	protected long lastRecordID;
	protected long firstRecordID;
	private DateItem dateItm;
	protected boolean isDateChanged;
	protected boolean isNext = true;
	protected boolean isPrevious;
	protected boolean isDateChanged2;
	private Anchor prvsHyprLink;
	private Anchor nextHyprLnk;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		grid = new FinanceLogginGrid(false);
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("280px");
		grid.setView(this);
		Label label = new Label(Accounter.constants().detailedLog());
		label.addStyleName("bold");
		messageTxtPnl = new ScrollPanel();
		messageTxtPnl.addStyleName("logview-border");

		prvsHyprLink = new Anchor() {
			@Override
			public void setEnabled(boolean enabled) {
				isPrevious = enabled;
				if (!enabled)
					this.getElement().getStyle().setColor("gray");
				else {
					this.getElement().getStyle().clearColor();
				}
				super.setEnabled(enabled);
			}
		};
		prvsHyprLink.setHTML(Accounter.messages().previousHTML());
		prvsHyprLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isPrevious)
					return;
				nextHyprLnk.setEnabled(true);
				// isNext = false;
				// isPrevious = true;
				if (isDateChanged2) {
					getLogsForDate(UIUtils.dateToString(dateItm
							.getEnteredDate()));
				} else {
					// fillGridWithPreviousRecords();
				}
			}
		});

		nextHyprLnk = new Anchor() {
			@Override
			public void setEnabled(boolean enabled) {
				isNext = enabled;
				if (!enabled)
					this.getElement().getStyle().setColor("gray");
				else {
					this.getElement().getStyle().clearColor();
				}
				super.setEnabled(enabled);
			}
		};
		nextHyprLnk.setHTML(Accounter.messages().nextHTML());
		nextHyprLnk.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isNext)
					return;
				prvsHyprLink.setEnabled(true);
				// isNext = true;
				// isPrevious = false;
				if (isDateChanged2) {
					getLogsForDate(UIUtils.dateToString(dateItm
							.getEnteredDate()));
				} else {
					// fillGridWithNextRecords();
				}
			}
		});

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setSpacing(15);
		buttonLayout.add(prvsHyprLink);
		buttonLayout.add(nextHyprLnk);

		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.setWidth("100%");

		dateItm = new DateItem();
		dateItm.setTitle(Accounter.constants().getLogUpto());
		DynamicForm dateForm = new DynamicForm();
		dateForm.setFields(dateItm);
		datePanel.add(dateForm);
		datePanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		Button getLogByDateBtn = new Button();
		getLogByDateBtn.setText(Accounter.constants().get());
		getLogByDateBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				prvsHyprLink.setEnabled(true);
				nextHyprLnk.setEnabled(true);
				isDateChanged = true;
				isNext = true;
				getLogsForDate(UIUtils.dateToString(dateItm.getEnteredDate()));
				isDateChanged2 = true;
			}
		});
		datePanel.add(getLogByDateBtn);
		datePanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		ScrollPanel gridPanel = new ScrollPanel();
		gridPanel.add(grid);

		mainPanel.add(datePanel);
		mainPanel.add(gridPanel);
		mainPanel.add(buttonLayout);
		mainPanel.setCellHorizontalAlignment(buttonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.addStyleName("margin-b");
		mainPanel.add(label);
		mainPanel.add(messageTxtPnl);

		setSize("100%", "100%");
		add(mainPanel);
	}

	protected void getLogsForDate(String dateToString) {
		// long id = isDateChanged ? -1 : (isNext ? lastRecordID :
		// firstRecordID);
		// rpcUtilService.getLog();
	}

	@Override
	public void initData() {
		// fillGrid();
		super.initData();
	}

	// private void fillGrid() {
	// rpcUtilService.getLog();
	// }

	// public void fillGridWithNextRecords() {
	// rpcUtilService.getLog();
	// }

	// public void fillGridWithPreviousRecords() {
	// rpcUtilService.getLog();
	// }

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().showLog();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
