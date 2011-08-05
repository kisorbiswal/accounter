package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPayVAT;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DashBoardView;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.settings.UsersView;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimView;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaims;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;

/**
 * 

 * 
 */

public class ViewManager extends DockPanel {

	private int index;

	private ArrayList<History> historyList;

	/**
	 * @return the historyList
	 */
	private ArrayList<History> getHistoryList() {
		return historyList;
	}

	private Label previousButton;

	private Label nextButton;

	private Label statusLabel;

	private Label closeButton;
	private Image print1Button;

	private Image edit1Button;
	// private Image editButton;
	private Label editButton;

	private VerticalPanel rightCanvas;

	/**
	 * This reference var. holds currently opened view. it is not only
	 * AbstractBaseView, it is may be AbstractReportView also
	 */
	private ParentCanvas currentCanvas;

	public final static int CMD_Sucess = 1;
	public final static int CMD_SAVEFAILED = 2;
	public final static int CMD_UPDATEFAILED = 3;
	public final static int CMD_DELETEFAILED = 3;

	private boolean isPreviousFocus, isNextFocus;

	private int height;

	private int width;
	public final static int TOP_MENUBAR = 75;
	private final int BORDER = 15;

	private boolean isShowWarningDialog;

	private ParentCanvas presentView;

	private Object presentDate;

	private boolean presentViewDependency;

	private Action presentAction;

	private Image exportButton;

	@SuppressWarnings("serial")
	public ViewManager(MainFinanceWindow financeWindow) {
		index = -1;
		historyList = new ArrayList<History>() {
			@Override
			public History remove(int index) {
				return super.remove(index);
			}

			@Override
			public boolean remove(Object o) {
				return super.remove(o);
			}

		};
		createControl(financeWindow);
		com.google.gwt.user.client.History
				.addValueChangeHandler(new ValueChangeHandler<String>() {

					public void onValueChange(ValueChangeEvent<String> event) {
						navigateTo(event.getValue());
					}
				});
	}

	protected void navigateTo(String value) {

	}

	/**
	 * Creating GUI Controls
	 * 
	 * @param financeWindow
	 */
	private void createControl(final MainFinanceWindow financeWindow) {

		HorizontalPanel statusLayout = new HorizontalPanel();
		statusLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		statusLayout.setWidth("100%");
		statusLayout.setStyleName("widget");

		HorizontalPanel hlay = new HorizontalPanel();

		// hlay.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		HorizontalPanel buttonLayout1 = new HorizontalPanel();
		buttonLayout1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		previousButton = new Label();
		if (UIUtils.isMSIEBrowser())
			previousButton.setSize("20", "15");
		else
			previousButton.setSize("15", "15");
		previousButton.setTitle(Accounter.constants().previous());
		previousButton.setStyleName("previousButton");
		previousButton
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getPreviousView(null);
			}

		});
		buttonLayout1.add(previousButton);

		nextButton = new Label();
		if (UIUtils.isMSIEBrowser())
			nextButton.setSize("20", "15");
		else
			nextButton.setSize("15", "15");
		nextButton.setTitle(Accounter.constants().next());
		nextButton.setStyleName("nextButton");
		nextButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getNextView();
			}

		});

		buttonLayout1.add(nextButton);
		hlay.add(buttonLayout1);
		statusLabel = new Label();
		statusLabel.addStyleName("nav-content");
		statusLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		if (!UIUtils.isMSIEBrowser())
			statusLabel.setWidth("100%");

		hlay.add(statusLabel);
		statusLayout.add(hlay);

		closeButton = new Label();
		closeButton.setTitle(Accounter.constants().close());
		if (UIUtils.isMSIEBrowser())
			closeButton.setSize("20", "14");
		else
			closeButton.setSize("14", "14");
		closeButton.setStyleName("discardButton");
		closeButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				closeCurrentView();

			}

		});

		if (!isPreviousFocus && isNextFocus) {
			previousButton.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					previousButton.setStyleName("previousButton_mouseOver");
					isPreviousFocus = true;
					isNextFocus = false;
				}
			});

			nextButton.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					nextButton.setStyleName("nextButton");
					isNextFocus = false;
					isPreviousFocus = true;
				}
			});

		} else if (isPreviousFocus && !isNextFocus) {
			previousButton.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					previousButton.setStyleName("previousButton");
					isPreviousFocus = false;
					isNextFocus = true;
				}
			});

			nextButton.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					nextButton.setStyleName("nextButton_mouseOver");
					isNextFocus = true;
					isPreviousFocus = false;
				}
			});
		}

		print1Button = new Image(Accounter.getFinanceImages().Print1Icon());
		print1Button.setStyleName("print_button_icon");
		print1Button.setTitle(Accounter.constants().print());
		print1Button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (currentCanvas != null)
					currentCanvas.print();

			}
		});

		exportButton = new Image(Accounter.getFinanceImages().exportIcon());
		exportButton.setStyleName("export_icon");
		exportButton.setTitle("Export to CSV ");
		exportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (currentCanvas != null)
					currentCanvas.exportToCsv();
			}
		});

		// printButton = new Image(FinanceApplication.getFinanceImages()
		// .printicon());
		// printButton.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// if (canvas != null)
		// canvas.printPreview();
		//
		// }
		// });

		edit1Button = new Image(Accounter.getFinanceImages().PageEditIcon());
		edit1Button.setStyleName("edit_button_icon");
		edit1Button.setTitle(Accounter.constants().edit());
		edit1Button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (canvas != null)
					canvas.onEdit();

			}
		});
		// editButton=new
		// Image(FinanceApplication.getFinanceImages().editicon());

		editButton = new Label();
		editButton.setSize("20", "20");
		editButton.setStyleName("editButton");

		editButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setCellHorizontalAlignment(closeButton, ALIGN_RIGHT);
		// buttonLayout.setCellHorizontalAlignment(print1Button, ALIGN_RIGHT);
		buttonLayout.setCellHorizontalAlignment(print1Button,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// buttonLayout.setCellHorizontalAlignment(printButton,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setCellHorizontalAlignment(edit1Button,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setCellHorizontalAlignment(editButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// buttonLayout.add(editButton);
		buttonLayout.add(edit1Button);
		buttonLayout.add(exportButton);
		buttonLayout.add(print1Button);
		buttonLayout.add(closeButton);

		statusLayout.add(buttonLayout);

		rightCanvas = new VerticalPanel() {

			VerticalPanel tempPanel;

			@Override
			public void add(Widget w) {
				if (w instanceof BaseView) {
					// remove(tempPanel);
					financeWindow.onLoad();
				}
				super.add(w);
			}

			// @Override
			// public boolean remove(Widget w) {
			// super.remove(w);
			// if (w instanceof BaseView) {
			// if (tempPanel == null)
			// tempPanel = new VerticalPanel();
			// tempPanel.setHeight(rightCanvas.getOffsetHeight() + "px");
			// tempPanel.setWidth("100%");
			// add(tempPanel);
			// }
			// return true;
			// }
		};
		rightCanvas.setStyleName("financeBackground");
		rightCanvas.setWidth("100%");

		add(statusLayout, DockPanel.NORTH);
		add(rightCanvas, DockPanel.CENTER);

		AccounterDOM.addStyleToparent(statusLayout.getElement(),
				"statusLayoutParent");
		AccounterDOM.addStyleToparent(statusLabel.getElement(),
				"statusLabelParent");
		AccounterDOM.setAttribute(
				(Element) ((Element) ((Element) ((Element) closeButton
						.getElement().getParentElement()).getParentElement())
						.getParentElement()).getParentElement(), "align",
				"right");
		// "closeBtnParent");

		// setSize("100%", "100%");
		setWidth("100%");

	}

	public void closeCurrentView() {
		// History history = getHistoryForView(currentCanvas);
		// final ParentCanvas view = history.getView();

		// If View is A transaction or non Transaction view then only
		// checking user want to save data or not,
		// if yes validating object & saving it other wise getting last
		// view from history & showing it
		if (currentCanvas instanceof BaseView
				&& !(currentCanvas instanceof ExpenseClaimView)
				&& ((AbstractBaseView<?>) currentCanvas).isViewModfied()) {
			showWarning(currentCanvas);
		} else {
			getPreviousView(null);
		}
	}

	public ParentCanvas<?> getParentCanvas() {
		return this.currentCanvas;
	}

	/**
	 * Adding Given History To History List
	 * 
	 * @param history
	 */
	private void addToHistoryList(History history) {

		// SC.logWarn("ViewManager Adding to History List" + history.getView());

		if (history == null)
			return;

		historyList.add(history);

		index++;

		// SC.logWarn("ViewManager Added to History " + history.getView());

	}

	/**
	 * 
	 * @param action
	 */

	public void showView(Action action) throws Exception {

		// SC.logWarn("ViewManager Show View Called for Action" + action);

		History history = getHistoryForAction(action);
		ParentCanvas canvas = history.getView();
		if (canvas == null) {
			runAction(action, null, history.isDependent());
		} else {
			showView(canvas, null, history.isDependent(), action);
		}

	}

	private void runAction(Action action, Object input, boolean isDependent) {
		action.run(input, isDependent);
		// resetTopRightButtons(action, input);
	}

	private void resetTopRightButtons(Action action, Object input) {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			boolean enableEdit = input != null
					&& !action.getCatagory().equals(
							Accounter.constants().report())
					&& (input instanceof ClientTransaction)
					&& !(input instanceof ClientPayVAT)
					&& !(input instanceof ClientReceiveVAT);
			edit1Button.setVisible(enableEdit);
		} else {
			boolean enableEdit = input != null
					&& !action.getCatagory().equals(
							Accounter.constants().report())
					&& (input instanceof ClientTransaction)
					&& !(input instanceof ClientPaySalesTax);
			edit1Button.setVisible(enableEdit);
		}

		boolean enablePrint = (input != null && (input instanceof ClientInvoice || input instanceof ClientCustomerCreditMemo))
				|| action.getCatagory().equals(Accounter.constants().report());

		print1Button.setVisible(enablePrint);
		exportButton.setVisible(action.getCatagory().equals(
				Accounter.constants().report()));

		boolean enableClose = !(action instanceof CompanyHomeAction)
				&& !(action instanceof PurchaseOrderListAction)
				&& !(action instanceof SalesOrderListAction);

		closeButton.setVisible(enableClose);
	}

	/**
	 * To Show the view in ViewManager
	 * 
	 * @param view
	 * @param input
	 * @param action
	 * @param dependent
	 */

	public void showView(ParentCanvas view, Object input, boolean dependent,
			Action action) throws Exception {

		// These references are useful, when there is a warning dialog asking to
		// save the current view.
		presentView = view;
		presentDate = input;
		presentViewDependency = dependent;
		presentAction = action;
		if (!dependent) {
			if (currentCanvas != null && currentCanvas instanceof BaseView
					&& !currentCanvas.isSaveAndNew
					&& !(currentCanvas instanceof ExpenseClaimView)
					&& ((AbstractBaseView<?>) currentCanvas).isViewModfied()) {
				isShowWarningDialog = true;
				showWarningDialog(view, input, dependent, action);

				return;
			} else {
				isShowWarningDialog = false;
			}
		} else if (currentCanvas instanceof AbstractBaseView<?>) {
			((AbstractBaseView<?>) currentCanvas).setViewModfied(false);
		}

		showPresentView(view, input, dependent, action);

	}

	public void showPresentView(ParentCanvas view, Object input,
			boolean dependent, Action action) throws Exception {
		// Checking for any duplication of Company Home Page. due to should save
		// in history This are just stacking up. so need to remove.
		MainFinanceWindow.shouldExecuteRun = true;
		if (currentCanvas != null && getNextHistory() != null) {
			removeAllSubsequentHistory();
		}

		// check whether view is dependent.
		if (!dependent) {
			// no, so close all non savable previous views
			closeAllNonSavableViews();
		} else {
			// view.showSaveAndNewButton = true;
			saveAndCloseCurrentView();
		}

		// if (action.getActionSource() instanceof CustomCombo) {
		// view.showSaveAndNewButton = false;
		// } else {
		// view.showSaveAndNewButton = true;
		// }

		if (!actionExistsInHistory(action)) {
			History history = null;
			if (input instanceof IAccounterCore) {
				history = new History(view, (IAccounterCore) input, action,
						dependent);
			} else {
				history = new History(view, null, action, dependent);
			}
			addToHistoryList(history);
		}
		if (input != null) {

			view.setData(input);
		}

		view.setAction(action);
		// Set the navigation to GWT History object
		// com.google.gwt.user.client.History.newItem(view.getViewName());
		setCurrentView(view);

		canvas = view;
		currentCanvas = view;
		fitToSize(this.height, this.width);

		statusLabel.setText(action.catagory
				+ " > "
				+ (input == null ? action.getText() : action.getText().replace(
						Accounter.constants().new1(),
						Accounter.constants().viewEdit())));
		refreshStatusBar();
	}

	private void showWarningDialog(final ParentCanvas view, final Object input,
			final boolean dependent, final Action action) {
		Accounter.showWarning(AccounterWarningType.saveOrClose,
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onNoClick() {
						try {
							showPresentView(view, input, dependent, action);
							isShowWarningDialog = false;
						} catch (Exception e) {

							e.printStackTrace();
						}
						return true;
					}

					@Override
					public boolean onYesClick() throws Exception {

						((AbstractBaseView) currentCanvas).errorOccured = false;
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);
						// AccounterExecute execute = new AccounterExecute(
						// (AbstractBaseView) currentCanvas,
						// ((AbstractBaseView) currentCanvas)
						// .getSaveAndCloseButton());
						// execute.run();
						// Accounter.setTimer(execute);
						((AbstractBaseView) currentCanvas).validate();
						// try {
						// showPresentView(view, input, dependent, action);
						// } catch (Exception e) {
						//
						// e.printStackTrace();
						// }
						return true;
					}

				});
	}

	public void closeView(Action action, Object viewOutPutData) {

		// Here this Method will be Called when, we are done with the View, and
		// View, no longer needs a History
		MainFinanceWindow.shouldExecuteRun = true;
		if (isShowWarningDialog) {
			try {
				isShowWarningDialog = false;
				showPresentView(presentView, presentDate,
						presentViewDependency, presentAction);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		removeCurrentView();
		FormItem item = null;
		if (viewOutPutData != null) {
			removeHistory(action);
		}

		History history = getPreviousHistory();

		if (history == null)
			return;

		HistoryTokenUtils.setPresentToken(history.getAction(),
				history.getData());
		if (history.getView() instanceof UsersView
				|| history.getView() instanceof ExpenseClaims) {
			currentCanvas = history.getView();
			history.getAction().run(null, true);
		} else if (!(history.getView().isAListView())
				&& !(history.getView() instanceof AbstractReportView)) {
			currentCanvas = history.getView();
			if (history.getAction().getActionSource() != null) {
				item = history.getAction().getActionSource();
			}
			if (historyList.size() > 1)
				history.getAction().setActionSource(action.getActionSource());
			currentCanvas.setAction(history.getAction());

			if (item != null)
				history.getAction().setActionSource(item);
			if (currentCanvas instanceof DashBoardView)
				((DashBoardView) currentCanvas).refreshWidgetData(null);

			currentCanvas.setWidth("100%");
			rightCanvas.add(currentCanvas);
			statusLabel.setText(history.getAction().catagory
					+ " > "
					+ (currentCanvas.isEditMode() ? history
							.getAction()
							.getText()
							.replace(Accounter.constants().new1(),
									Accounter.constants().viewEdit()) : history
							.getAction().getText()));
			fitToSize(this.height, this.width);
			statusLabel.setText(history.getAction().catagory + " > "
					+ history.getAction().getText());

			refreshStatusBar();
			// currentCanvas.show();
		} else {
			// For Solving List View's Stall Data problem in List we need to
			// redraw it again rather than using Same view in the history.
			if (history.getView() instanceof BaseView<?>)
				history.getAction().run(null, true);
			else
				history.getAction().run(null, false);
		}

		// SC.logWarn("ViewManager Close View Exited, With Action"
		// + String.valueOf(action.getText()));

	}

	/**
	 * Hiding Given View
	 * 
	 * @param view
	 */

	private void hideView(ParentCanvas view) {

		// SC.logWarn("ViewManager hide View Called." + view);

		if (view != null) {
			// view.hide();
		}
		view = null;

	}

	/**
	 * Save & Close Operation to be performed when view is Dependent. It set
	 * view input back to history before Hiding view so that same values can be
	 * set back to view when again opening from history. saveAndHideCurrentView
	 * 
	 * @param history
	 */

	private void saveAndCloseCurrentView() {

		History history = getHistoryForView(currentCanvas);

		if (history == null)
			return;

		historyList.remove(history);
		historyList.add(history);
		history.updateHistory(currentCanvas.getData());
		history.setView(currentCanvas);

		if (historyList.size() != 0) {
			removeCurrentView();
		}
	}

	/**
	 * Provides Previous History object
	 * 
	 * @return History
	 */
	private History getPreviousHistory() {

		// SC.logWarn("ViewManager getPreviousHistory Called...With HistoryList "
		// + historyList.size() + "index@" + index);
		History history = new History();

		try {
			if (historyList != null) {
				if (index <= 0) {
					history = null;
				} else {
					if (index > historyList.size()) {
						index = historyList.size() > 1 ? historyList.size() - 1
								: 1;
					}
					history = historyList.get(index - 1);
					index--;
				}
			}

		} catch (Exception e) {
			// SC
			// .logWarn("ViewManager Error While Calling getPreviousHistory Called...");
			history = null;
			e.printStackTrace();
		}
		return history;
	}

	public History getTempHistory() {

		// SC.logWarn("ViewManager getPreviousHistory Called...With HistoryList "
		// + historyList.size() + "index@" + index);
		History history = new History();

		try {
			if (historyList != null) {
				if (index <= 0) {
					history = null;
				} else {

					if (index > historyList.size()) {
						history = historyList
								.get(historyList.size() > 1 ? historyList
										.size() - 2 : 0);
						// index = historyList.size() -1;
					} else
						history = historyList.get(index - 1);
					// index--;
				}
			}

		} catch (Exception e) {
			// SC
			// .logWarn("ViewManager Error While Calling getPreviousHistory Called...");
			history = null;
			e.printStackTrace();
		}
		return history;
	}

	/**
	 * Provides next History object
	 * 
	 * @return History
	 */
	private History getNextHistory() {

		// SC.logWarn("ViewManager GetNext History"
		// + getViewManagerIndexAndListSize());

		History history = new History();

		try {
			if (historyList != null) {
				if (index >= historyList.size() - 1) {
					history = null;
				} else {
					history = historyList.get(index + 1);
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return history;
	}

	/**
	 * Removing History object from History List depending on History object
	 * provided
	 * 
	 * @param history
	 */
	public void removeHistory(Action action) {

		for (History history : historyList) {
			if (history.getAction() == action) {
				historyList.remove(history);
				// index--;
				return;
			}
		}
	}

	/**
	 * Delete all Nonsavable Objects from History if Current View is Non
	 * Dependable
	 */

	private void closeAllNonSavableViews() {

		// SC
		// .logWarn("Closing Non Savable Views...Called closeAllNonSavableViews()");

		if (index == -1) {
			return;
		}
		List<History> temp = new ArrayList<History>();

		for (History history : historyList) {
			ParentCanvas canvas = history.getView();
			if (!canvas.shouldSaveInHistory()) {
				// temp.add(history);
				// index--;
			}
		}
		historyList.removeAll(temp);

		// index = 0;

		removeCurrentView();
	}

	/**
	 * Instantiating View Manager
	 * 
	 * @return
	 */
	public static ViewManager getInstance() {

		return MainFinanceWindow.getViewManager();

	}

	/**
	 * Instantiating View Manager for Sales and Purchases
	 * 
	 * @return
	 */
	public static ViewManager getInstance(boolean isSalesOrPurchases) {

		return MainFinanceWindow.getViewManager();

	}

	/**
	 * Setting View For Current Display
	 * 
	 * @param canvas
	 */

	private void setCurrentView(final ParentCanvas<?> canvas) throws Exception {

		if (currentCanvas != null) {
			return;
		}

		canvas.init(this);
		canvas.initData();
		currentCanvas = canvas;
		rightCanvas.add(currentCanvas);

	}

	/**
	 * Removing view from Current Display
	 */
	private void removeCurrentView() {

		// SC.logWarn("ViewManager Removing Current View");
		if (currentCanvas != null) {
			rightCanvas.remove(currentCanvas);
			currentCanvas = null;
		}
	}

	/**
	 * Getting History for particular action from History List
	 * 
	 * @param action
	 * @return
	 */
	private History getHistoryForAction(Action action) {

		for (History history : historyList) {
			if (action.equals(history.getAction())) {
				return history;
			}
		}
		// SC.logWarn("ViewManager Returning History Null");
		return null;
	}

	private History getHistoryForView(ParentCanvas view) {

		for (History history : historyList) {
			if (view.getAction().equals(history.getView().getAction())) {
				return history;
			}
		}
		return null;
	}

	/**
	 * Checks Whether history object exists in History List for Given Action
	 * 
	 * @param action
	 * @return
	 */
	private boolean actionExistsInHistory(Action action) {
		for (int i = 0; i < historyList.size(); i++) {
			if (historyList.get(i).getAction().equals(action)) {
				return true;
			}
		}

		return false;
	}

	private void getPreviousView(IAccounterCore viewOutPutData) {

		// SC.logWarn("Get getPreviousView(data) Called....");

		if (index > 0) {
			saveAndCloseCurrentView();
			History previousHistory = getPreviousHistory();

			HistoryTokenUtils.setPresentToken(previousHistory.getAction(),
					viewOutPutData);
			displayView(previousHistory, viewOutPutData);
			MainFinanceWindow.shouldExecuteRun = true;
		}
	}

	private void getNextView() {
		if (index < historyList.size() - 1) {
			saveAndCloseCurrentView();
			History nextHistory = getNextHistory();
			HistoryTokenUtils.setPresentToken(nextHistory.getAction(), null);
			displayView(nextHistory, null);
			MainFinanceWindow.shouldExecuteRun = true;
		}
	}

	private void displayView(History history, Object viewOutPutData) {

		if (history == null)
			return;

		ParentCanvas view = history.getView();
		view.setData(history.getData());
		view.isEdit = false;

		// Setting CallBack Data if Not null
		if (viewOutPutData != null) {
			view.setPrevoiusOutput(viewOutPutData);
		}

		if (view != null) {
			// view.initData();
			if (view instanceof AbstractReportView) {
				((AbstractReportView<?>) view).showRecords();
			}
			if (view instanceof DashBoardView) {
				((DashBoardView) view).refreshWidgetData(null);
			}
			rightCanvas.add(view);
			currentCanvas = view;
		}
		fitToSize(this.height, this.width);
		statusLabel.setText(history.getAction().catagory
				+ " > "
				+ (currentCanvas.isEditMode() ? history
						.getAction()
						.getText()
						.replace(Accounter.constants().new1(),
								Accounter.constants().viewEdit()) : history
						.getAction().getText()));
		refreshStatusBar();
	}

	private void refreshStatusBar() {

		// SC.logWarn("Refresh Status Bar Called @index" + index);

		if (index <= 0) {
			// previousButton.setShowDisabled(true);
			// previousButton.setBackgroundImage("/images/icons/arrow2.png");
			// previousButton.setStyleName("defaultCursor");
		} else {
			// previousButton.setShowDisabled(false);
			// previousButton.setBackgroundImage("/images/icons/arrow2.1.png");
			// previousButton.setStyleName("handCursor");
		}

		if (index >= historyList.size() - 1) {
			// nextButton.setShowDisabledIcon(true);
			// nextButton.setBackgroundImage("/images/icons/arrow1.png");
			// nextButton.setStyleName("defaultCursor");
		} else {
			// nextButton.setShowDisabledIcon(false);
			// nextButton.setBackgroundImage("/images/icons/arrow1.1.png");
			// nextButton.setStyleName("handCursor");
		}

	}

	// private String getViewManagerIndexAndListSize() {
	//
	// return "HistoryList size" + historyList.size() + " index@" + index;
	//
	// }

	private void removeAllSubsequentHistory() {
		ArrayList<History> tempHistoryList = new ArrayList<History>();
		index = -1;
		for (History history : historyList) {
			tempHistoryList.add(history);
			index++;
			if (currentCanvas.getAction().getText()
					.equals(history.getAction().getText())) {
				break;
			}
		}

		historyList.clear();
		historyList = tempHistoryList;
	}

	private void showWarning(final ParentCanvas view) {
		Accounter.showWarning(AccounterWarningType.saveOrClose,
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						getPreviousView(null);
						return true;
					}

					@Override
					public boolean onYesClick() throws Exception {

						((AbstractBaseView) view).errorOccured = false;
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);
						// AccounterExecute execute = new AccounterExecute(
						// (AbstractBaseView) view,
						// ((AbstractBaseView) view)
						// .getSaveAndCloseButton());
						// execute.run();
						// Accounter.setTimer(execute);
						((AbstractBaseView) view).validate();
						return true;
					}

				});
	}

	public static void updateComboDataInViews(SelectItemType selectItemType,
			IAccounterCore accounterCoreObject) {

		if (MainFinanceWindow.getViewManager() == null)
			return;

		for (History history : MainFinanceWindow.getViewManager()
				.getHistoryList()) {
			ParentCanvas canvas = history.getView();
			updateComboDataInViews(selectItemType, accounterCoreObject, canvas);
		}
	}

	public static void updateComboDataInViews(SelectItemType selectItemType,
			IAccounterCore accounterCoreObject, ParentCanvas canvas) {

		if (canvas != null && canvas instanceof AbstractBaseView) {

			List<CustomCombo> formItems = ((AbstractBaseView) canvas)
					.getComboList(selectItemType);

			if (formItems == null)
				return;

			for (CustomCombo customCombo : formItems) {

				if (!(accounterCoreObject instanceof AccounterCommand))
					customCombo.addComboItem(accounterCoreObject);
				else
					customCombo.removeComboItem(accounterCoreObject);

			}
		}

	}

	public static <T extends IAccounterCore> void updateDataInDashBoard(T t) {

		if (t == null)
			return;

		switch (t.getObjectType()) {

		case ACCOUNT:

			break;

		default:
			break;
		}

	}

	private <T extends IAccounterCore, P extends IAccounterCore> void saveOrUpdate(
			final P core, final IAccounterWidget widget, boolean save) {
		processDialog = UIUtils.getLoadingMessageDialog(Accounter.constants()
				.processingRequest());

		processDialog.center();

		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				if (processDialog != null) {
					processDialog.removeFromParent();
				}
				widget.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onSuccess(Long result) {
				if (processDialog != null) {
					processDialog.removeFromParent();
				}
				core.setID(result);
				Accounter.getCompany().processUpdateOrCreateObject(core);
				widget.saveSuccess(core);
			}

		};
		if (save) {
			Accounter.createCRUDService().create(((IAccounterCore) core),
					transactionCallBack);
		} else {

		}

	}

	public <T extends IAccounterCore, P extends IAccounterCore> void createObject(
			final P core, final IAccounterWidget widget) {
		saveOrUpdate(core, widget, true);
	}

	public <T extends IAccounterCore, P extends IAccounterCore> void alterObject(
			final P core, final IAccounterWidget widget) {

		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				if (caught instanceof AccounterException) {
					AccounterException exception = (AccounterException) caught;
					widget.saveFailed(exception);
					exception.printStackTrace();
				}
				// TODO handle other kind of errors
			}

			public void onSuccess(Long result) {
				super.onSuccess(result);
				core.setID(result);
				Accounter.getCompany().processUpdateOrCreateObject(core);
				widget.saveSuccess(core);
			}

		};
		// widget.setID(core.getID());
		// when you edit transaction, previous transactionitems and related
		// objects has to delete, below code is to clear lists that transaction
		// item has, those no longer need after editing transactions
		if (core instanceof ClientTransaction)
			cleanTransactionitems((ClientTransaction) core);

		Accounter.createCRUDService().update(((IAccounterCore) core),
				transactionCallBack);
	}

	/**
	 * @return
	 */
	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	// private boolean isprocessingRequestAdd(final IAccounterWidget widget) {
	// if (widget instanceof ExpenseClaimView) {
	// if (((ExpenseClaimView) widget).isProcessingAdded) {
	// return true;
	// } else {
	// ((ExpenseClaimView) widget).isProcessingAdded = true;
	// return false;
	// }
	// }
	// if (widget instanceof AwaitingAuthorisationView) {
	// if (((AwaitingAuthorisationView) widget).isProcessingAdded) {
	// return true;
	// } else {
	// ((AwaitingAuthorisationView) widget).isProcessingAdded = true;
	// return false;
	// }
	// }
	// return false;
	// }

	public <T extends IAccounterCore, P extends IAccounterCore> void voidTransaction(
			final AccounterCoreType type, final long transactionID,
			final IAccounterWidget widget) {

		// currentrequestedWidget = widget;
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				if (!GWT.isScript()) {
					AccounterException exception = (AccounterException) caught;
					// exception.setID(currentrequestedWidget.getID());
					getCompany().processCommand(exception);
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					if (!GWT.isScript()) {
						AccounterCommand cmd = new AccounterCommand();
						cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
						cmd.setData(null);
						cmd.setID(transactionID);
						cmd.setObjectType(type);
						getCompany().processUpdateOrCreateObject(cmd);
					}
				}

			}
		};
		// widget.setID(transactionID);
		Accounter.createCRUDService().voidTransaction(type, transactionID,
				callback);
	}

	private void cleanTransactionitems(ClientTransaction coreObject) {

		if (coreObject.getTransactionItems() != null) {
			for (ClientTransactionItem item : coreObject.getTransactionItems()) {
				item.itemBackUpList.clear();
				item.taxRateCalculationEntriesList.clear();
			}
		}
	}

	public void updateCompany(final ClientCompany clientCompany,
			final IAccounterWidget widget) {

		processDialog = UIUtils.getLoadingMessageDialog(Accounter.constants()
				.processingRequest());

		processDialog.center();
		// currentrequestedWidget = widget;

		AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {

				if (caught instanceof AccounterException) {
					AccounterException exception = (AccounterException) caught;
					// exception.setID(currentrequestedWidget.getID());
					// getCompany().processCommand(exception);
					operationFailed(exception);
				}
			}

			public void onSuccess(Long result) {

				// if (!GWT.isScript()) {
				if (result != null) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
					cmd.setData(clientCompany);
					cmd.setID(result);
					cmd.setObjectType(clientCompany.getObjectType());
					getCompany().processCommand(cmd);
				}
				// }
			}

		};
		// widget.setID(clientCompany.getID());
		Accounter.createCRUDService().updateCompany(clientCompany,
				transactionCallBack);

	}

	public void updateCompanyPreferences(
			final ClientCompanyPreferences preferences,
			final IAccounterWidget widget) {
		processDialog = UIUtils.getLoadingMessageDialog(Accounter.constants()
				.processingRequest());

		processDialog.center();
		// currentrequestedWidget = widget;

		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException caught) {

				if (caught instanceof AccounterException) {
					AccounterException exception = (AccounterException) caught;
					// exception.setID(currentrequestedWidget.getID());
					// getCompany().processCommand(exception);
					operationFailed(exception);
				}
			}

			public void onSuccess(Boolean result) {

				if (!GWT.isScript()) {
					if (result != null) {
						AccounterCommand cmd = new AccounterCommand();
						cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
						cmd.setData(preferences);
						cmd.setID(getCompany().getID());
						cmd.setObjectType(preferences.getObjectType());
						getCompany().processCommand(cmd);
					} else {
						onFailure(null);
					}
				}
			}

		};
		// widget.setID(getCompany().getID());
		Accounter.createCRUDService().updateCompanyPreferences(preferences,
				transactionCallBack);

	}

	/**
	 * Deletes the Object
	 * 
	 * @param core
	 * @param coreType
	 * @param widget
	 */
	public <A extends IAccounterCore> void deleteObject(final A core,
			AccounterCoreType coreType, final IAccounterWidget widget) {
		// currentrequestedWidget = widget;

		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException caught) {
				AccounterException exception = (AccounterException) caught;
				// exception.setID(currentrequestedWidget.getID());
				// getCompany().processCommand(exception);
				operationFailed(exception);
			}

			public void onSuccess(Boolean result) {
				super.onSuccess(result);
				getCompany().processDeleteObject(core);
			}

		};
		Accounter.createCRUDService().delete(coreType, core.getID(),
				transactionCallBack);
	}

	/**
	 * Set the width and height to the given values.
	 * 
	 * @param height
	 * @param width
	 */
	public void fitToSize(int height, int width) {

		this.height = 500;
		this.width = width;

		if (currentCanvas != null) {
			currentCanvas.fitToSize(height, width);
		}
	}
}
