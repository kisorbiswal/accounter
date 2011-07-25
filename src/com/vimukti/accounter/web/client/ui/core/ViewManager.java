package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.InvalidOperationException;
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
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.DashBoard;
import com.vimukti.accounter.web.client.ui.FinanceDashboard;
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
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationView;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimView;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaims;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;

/**
 * 
 * @modified by Raj Vimal
 * 
 */
@SuppressWarnings("deprecation")
public class ViewManager extends DockPanel {

	private int index;

	private static ViewManager viewManagerInstance;

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

	@SuppressWarnings("unchecked")
	/* This reference var. holds currently opened view */
	private ParentCanvas currentCanvas;

	/* This reference var. holds currently opened dialog */
	private BaseDialog<?> currentDialog;

	public final static int CMD_Sucess = 1;
	public final static int CMD_SAVEFAILED = 2;
	public final static int CMD_UPDATEFAILED = 3;
	public final static int CMD_DELETEFAILED = 3;

	private boolean isPreviousFocus, isNextFocus;
	/**
	 * This variable hold reference of Dialog or View which sent Crud service
	 * request to server .
	 */
	private IAccounterWidget currentrequestedWidget;

	private PopupPanel dialog;

	private ScrollPanel scrollPanel;

	private int height;

	private int width;
	public final static int TOP_MENUBAR = 75;
	private final int BORDER = 15;

	@SuppressWarnings("unchecked")
	private ParentCanvas canvas;

	private boolean isShowWarningDialog;

	@SuppressWarnings("unchecked")
	private ParentCanvas presentView;

	private Object presentDate;

	private boolean presentViewDependency;

	private Action presentAction;

	private Image exportButton;

	public HTML errordata;
	public VerticalPanel commentPanel;

	@SuppressWarnings("serial")
	private ViewManager() {
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
		createControl();
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
	 */
	private void createControl() {

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
		previousButton.setTitle(Accounter.getCustomersMessages().previous());
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
		nextButton.setTitle(Accounter.getCustomersMessages().next());
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
		closeButton.setTitle(Accounter.getCustomersMessages().close());
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

		print1Button = new Image("/images/Print1.png");
		print1Button.setStyleName("print_button_icon");
		print1Button.setTitle(Accounter.getCustomersMessages().print());
		print1Button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (currentCanvas != null)
					currentCanvas.print();

			}
		});

		exportButton = new Image("/images/export-icon.png");
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

		edit1Button = new Image("/images/Page_edit1.png");
		edit1Button.setStyleName("edit_button_icon");
		edit1Button.setTitle(Accounter.getCustomersMessages().edit());
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
			@SuppressWarnings("unused")
			VerticalPanel tempPanel;

			@SuppressWarnings("unchecked")
			@Override
			public void add(Widget w) {
				if (w instanceof BaseView) {
					// remove(tempPanel);
					MainFinanceWindow.getInstance().onLoad();
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
		commentPanel = new VerticalPanel();
		commentPanel.setWidth("97%");
		commentPanel.setVisible(false);
		commentPanel.addStyleName("commentPanel");
		errordata = new HTML();
		errordata.addStyleName("error-data");
		commentPanel.add(errordata);

		rightCanvas.add(commentPanel);

		scrollPanel = new ScrollPanel() {
			@SuppressWarnings("unchecked")
			@Override
			public void add(Widget w) {
				ParentCanvas canvas = (ParentCanvas) w;
				resetTopRightButtons(canvas.getAction(), canvas.getData());
				super.add(w);
			}
		};

		scrollPanel.setStyleName("scroll-panel");

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

	@SuppressWarnings("unchecked")
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
			restoreErrorBox();
			getPreviousView(null);
		}
	}

	@SuppressWarnings("unchecked")
	public BaseView getContentPanel() {
		return this.currentCanvas instanceof BaseView ? (BaseView) this.currentCanvas
				: null;
	}

	@SuppressWarnings("unchecked")
	public ParentCanvas getParentCanvas() {
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
	@SuppressWarnings("unchecked")
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
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			boolean enableEdit = input != null
					&& !action.getCatagory().equals(
							Accounter.getReportsMessages().report())
					&& (input instanceof ClientTransaction)
					&& !(input instanceof ClientPayVAT)
					&& !(input instanceof ClientReceiveVAT);
			edit1Button.setVisible(enableEdit);
		} else {
			boolean enableEdit = input != null
					&& !action.getCatagory().equals(
							Accounter.getReportsMessages().report())
					&& (input instanceof ClientTransaction)
					&& !(input instanceof ClientPaySalesTax);
			edit1Button.setVisible(enableEdit);
		}

		boolean enablePrint = (input != null && (input instanceof ClientInvoice || input instanceof ClientCustomerCreditMemo))
				|| action.getCatagory().equals(
						Accounter.getReportsMessages().report());

		print1Button.setVisible(enablePrint);
		exportButton.setVisible(action.getCatagory().equals(
				Accounter.getReportsMessages().report()));

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
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	public void showPresentView(ParentCanvas view, Object input,
			boolean dependent, Action action) throws Exception {
		// Checking for any duplication of Company Home Page. due to should save
		// in history This are just stacking up. so need to remove.
		restoreErrorBox();
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
			History history = new History(view, input, action, dependent);
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
						Accounter.getCustomersMessages().New(),
						Accounter.getCustomersMessages().viewEdit())));
		refreshStatusBar();
	}

	@SuppressWarnings("unchecked")
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {

						((AbstractBaseView) currentCanvas).errorOccured = false;
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);
						restoreErrorBox();
						AccounterExecute execute = new AccounterExecute(
								(AbstractBaseView) currentCanvas,
								((AbstractBaseView) currentCanvas)
										.getSaveAndCloseButton());
						execute.run();
						Accounter.setTimer(execute);
						// try {
						// showPresentView(view, input, dependent, action);
						// } catch (Exception e) {
						// // TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
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

			if (viewOutPutData != null) {
				currentCanvas.setPrevoiusOutput(viewOutPutData);
			}
			if (item != null)
				history.getAction().setActionSource(item);
			if (currentCanvas instanceof DashBoard)
				((DashBoard) currentCanvas).refreshWidgetData(null);

			currentCanvas.setWidth("100%");
			scrollPanel.add(currentCanvas);
			rightCanvas.add(scrollPanel);
			statusLabel
					.setText(history.getAction().catagory
							+ " > "
							+ (currentCanvas.isEditMode() ? history
									.getAction()
									.getText()
									.replace(
											Accounter.getCustomersMessages()
													.New(),
											Accounter.getCustomersMessages()
													.viewEdit()) : history
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
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unused")
	private void saveAndHideCurrentView() {
		// check history is null
		// if not null, then get input from view, and update the history
		// then hide view

		// SC.logWarn("ViewManager Called Save and Hide Current View");

		History history = getHistoryForView(getCurrentView());

		if (history == null)
			return;

		history.updateHistory(getCurrentView().getData());

		if (historyList.size() != 0) {
			history.setView(getCurrentView());
			hideView(getCurrentView());
			removeCurrentView();
		}

		// SC.logWarn("ViewManager Save and Hide Current View");

	}

	@SuppressWarnings("unchecked")
	private void saveAndCloseCurrentView() {

		// SC.logWarn("ViewManager Called saveAndCloseCurrentView()");

		History history = getHistoryForView(getCurrentView());

		if (history == null)
			return;

		historyList.remove(history);
		historyList.add(history);
		history.updateHistory(getCurrentView().getData());
		history.setView(getCurrentView());

		if (historyList.size() != 0) {
			removeCurrentView();
			@SuppressWarnings("unused")
			ParentCanvas view = getCurrentView();
			// if (view != null) {
			// view.destroy();
			// }
			view = null;
		}

		// SC.logWarn("ViewManager Ended saveAndCloseCurrentView()");

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
					if (Accounter.isSales() || Accounter.isPurchases()) {
						if (historyList.size() == 1) {
							return historyList.get(0);
						}
					}
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
					if (Accounter.isSales() || Accounter.isPurchases()) {
						if (historyList.size() == 1) {
							return historyList.get(0);
						}
					}

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
	@SuppressWarnings("unchecked")
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

		if (viewManagerInstance == null) {
			viewManagerInstance = new ViewManager();
		}

		return viewManagerInstance;
	}

	/**
	 * Instantiating View Manager for Sales and Purchases
	 * 
	 * @return
	 */
	public static ViewManager getInstance(boolean isSalesOrPurchases) {

		return viewManagerInstance = new ViewManager();

	}

	/**
	 * Setting View For Current Display
	 * 
	 * @param canvas
	 */
	@SuppressWarnings("unchecked")
	private void setCurrentView(final ParentCanvas canvas) throws Exception {

		if (currentCanvas == null) {
			if (!(canvas.isInitialized())) {

				canvas.init();
				canvas.initData();
				if (canvas instanceof BaseHomeView
						|| canvas instanceof BaseListView
						|| canvas instanceof AbstractReportView) {
					if (canvas.isTransactionView() && canvas.isEditMode()) {
						canvas.disableUserEntry();
					}
					currentCanvas = canvas;
					scrollPanel.clear();
					scrollPanel.add(canvas);
					rightCanvas.add(scrollPanel);

				} else {
					Timer timer = new Timer() {

						@Override
						public void run() {

							if (canvas.isEditMode()
									&& canvas.isTransactionView()) {
								canvas.disableUserEntry();
								if (canvas.getData() != null) {
									ClientTransaction transaction = (ClientTransaction) canvas
											.getData();
									if (!transaction.canEdit) {
										edit1Button.setVisible(false);
									}
								}
							}
							// loadingDialog.removeFromParent();
							currentCanvas = canvas;
							scrollPanel.clear();
							scrollPanel.add(canvas);
							rightCanvas.add(scrollPanel);
							currentCanvas.setFocus();

						}

					};
					timer.schedule(300);
				}

			}
		}

		// resetTopRightButtons(canvas.getAction(), canvas.getData());

	}

	/**
	 * Removing view from Current Display
	 */
	private void removeCurrentView() {

		// SC.logWarn("ViewManager Removing Current View");
		if (currentCanvas != null) {
			scrollPanel.remove(currentCanvas);
			currentCanvas = null;
		}

		if (scrollPanel != null) {
			rightCanvas.remove(scrollPanel);
		}
		restoreErrorBox();

	}

	/**
	 * Getter For CurrentView
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ParentCanvas getCurrentView() {
		return currentCanvas;
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

	@SuppressWarnings("unchecked")
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

	private void getPreviousView(Object viewOutPutData) {

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

	@SuppressWarnings("unchecked")
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
			if (view instanceof DashBoard) {
				((DashBoard) view).refreshWidgetData(null);
			}
			scrollPanel.add(view);
			rightCanvas.add(scrollPanel);
			currentCanvas = view;
		}
		fitToSize(this.height, this.width);
		statusLabel.setText(history.getAction().catagory
				+ " > "
				+ (currentCanvas.isEditMode() ? history
						.getAction()
						.getText()
						.replace(Accounter.getCustomersMessages().New(),
								Accounter.getCustomersMessages().viewEdit())
						: history.getAction().getText()));
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

	@SuppressWarnings("unchecked")
	private void showWarning(final ParentCanvas view) {
		Accounter.showWarning(AccounterWarningType.saveOrClose,
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() throws InvalidEntryException {
						return true;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {
						restoreErrorBox();
						getPreviousView(null);
						return true;
					}

					@Override
					public boolean onYesClick() throws InvalidEntryException {

						((AbstractBaseView) view).errorOccured = false;
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);
						restoreErrorBox();
						AccounterExecute execute = new AccounterExecute(
								(AbstractBaseView) view,
								((AbstractBaseView) view)
										.getSaveAndCloseButton());
						execute.run();
						Accounter.setTimer(execute);
						return true;
					}

				});
	}

	@SuppressWarnings("unchecked")
	public static void updateComboDataInViews(SelectItemType selectItemType,
			IAccounterCore accounterCoreObject) {

		if (viewManagerInstance == null)
			return;

		for (History history : viewManagerInstance.getHistoryList()) {
			ParentCanvas canvas = history.getView();
			updateComboDataInViews(selectItemType, accounterCoreObject, canvas);
		}
	}

	@SuppressWarnings("unchecked")
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

	public void operationFailed(InvalidOperationException exception) {

		if (dialog != null)
			dialog.removeFromParent();
		String id = exception.getID();
		if (currentrequestedWidget == null || id == null) {
			return;
		}
		if (!currentrequestedWidget.getID().equals(exception.getID()))
			return;

		switch (exception.getStatus()) {
		case InvalidOperationException.DELETE_FAILED:
			currentrequestedWidget.deleteFailed(exception);
			break;
		case InvalidOperationException.UPDATE_FAILED:
		case InvalidOperationException.CREATE_FAILED:
			currentrequestedWidget.saveFailed(exception);
		default:
			break;
		}

		currentrequestedWidget = null;
		Accounter.stopExecution();
	}

	public void operationSuccessFull(AccounterCommand cmd) {
		try {
			if (dialog != null)
				dialog.removeFromParent();

			String objectID = cmd.getID();

			if (currentCanvas instanceof BaseListView<?>
					&& currentDialog == null && cmd.getData() != null) {
				currentCanvas.processupdateView(cmd.getData(), cmd.command);
			} else if (currentDialog instanceof GroupDialog<?>) {
				if (currentDialog != null) {
					currentDialog.processupdateView(cmd.getData(), cmd.command);
				}
			}

			if (currentrequestedWidget == null || objectID == null) {
				return;
			}

			if (!objectID.equals(currentrequestedWidget.getID()))
				return;

			currentrequestedWidget.saveSuccess(cmd.getData() == null ? cmd
					: cmd.getData());

			Accounter.stopExecution();
			currentrequestedWidget = null;
			// if (currentCanvas != null) {

			// }

		} catch (Exception e) {
			if (!GWT.isScript())
				Accounter
						.showInformation(e instanceof JavaScriptException ? ((JavaScriptException) e)
								.getDescription() : e.getMessage());
			// else
			e.printStackTrace();
		}
	}

	/**
	 * called when deletion is Success
	 * 
	 * @param accounterCoreObject
	 */
	public void deleteSuccess(IAccounterCore accounterCoreObject) {
		try {
			if (dialog != null) {
				dialog.removeFromParent();
			}
			if (currentCanvas instanceof BaseListView<?>
					&& currentDialog == null) {
				currentCanvas.processupdateView(accounterCoreObject,
						AccounterCommand.DELETION_SUCCESS);
			} else if (currentDialog instanceof GroupDialog<?>) {

				if (currentDialog != null)
					currentDialog.processupdateView(accounterCoreObject,
							AccounterCommand.DELETION_SUCCESS);

			}
			if (currentrequestedWidget != null
					&& currentrequestedWidget.getID().equals(
							accounterCoreObject.getID())) {

				currentrequestedWidget.deleteSuccess(true);
				currentrequestedWidget = null;
			}
		} catch (Exception e) {
			Accounter.showInformation(((JavaScriptException) e)
					.getDescription());
		}
	}

	public <T extends IAccounterCore, P extends IAccounterCore> void createObject(
			final P core, final IAccounterWidget widget) {

		dialog = UIUtils.getLoadingMessageDialog(Accounter
				.getCustomersMessages().processingRequest());

		dialog.center();

		currentrequestedWidget = widget;

		final AsyncCallback<String> transactionCallBack = new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {

				if (!GWT.isScript()) {
					InvalidOperationException exception = new InvalidOperationException();
					exception
							.setStatus(InvalidOperationException.CREATE_FAILED);
					exception.setID(currentrequestedWidget.getID());
					getCompany().processCommand(exception);
					exception.printStackTrace();
					System.out.println(exception.getMessage());
				}
			}

			public void onSuccess(String result) {

				if (!GWT.isScript()) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.CREATION_SUCCESS);
					cmd.setData(core);
					cmd.setID(core.getID());
					cmd.setObjectType(core.getObjectType());
					getCompany().processCommand(cmd);
				}
			}

		};
		Accounter.createGETService().getID(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				core.setID(result);
				widget.setID(result);
				Accounter.createCRUDService().create(((IAccounterCore) core),
						transactionCallBack);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError("Could Not Initialize the id.....");
				}
				dialog.removeFromParent();
				currentrequestedWidget = null;
			}
		});

	}

	public <T extends IAccounterCore, P extends IAccounterCore> void alterObject(
			final P core, final IAccounterWidget widget) {

		if (!((widget instanceof ExpenseClaimView) || (widget instanceof AwaitingAuthorisationView))) {
			dialog = UIUtils.getLoadingMessageDialog(Accounter
					.getCustomersMessages().processingRequest());

			dialog.center();
		} else {
			if (!isprocessingRequestAdd(widget)) {
				dialog = UIUtils.getLoadingMessageDialog(Accounter
						.getCustomersMessages().processingRequest());

				dialog.center();
			}
		}
		currentrequestedWidget = widget;
		AsyncCallback<Boolean> transactionCallBack = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					if (!GWT.isScript()) {
						InvalidOperationException exception = new InvalidOperationException();
						exception
								.setStatus(InvalidOperationException.UPDATE_FAILED);
						exception.setID(currentrequestedWidget.getID());
						getCompany().processCommand(exception);
					}
				}
			}

			public void onSuccess(Boolean result) {

				if (!GWT.isScript()) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
					cmd.setData(core);
					cmd.setID(widget.getID());
					cmd.setObjectType(core.getObjectType());
					getCompany().processCommand(cmd);
				}
			}
		};
		widget.setID(core.getID());
		// when you edit transaction, previous transactionitems and related
		// objects has to delete, below code is to clear lists that transaction
		// item has, those no longer need after editing transactions
		if (core instanceof ClientTransaction)
			cleanTransactionitems((ClientTransaction) core);

		Accounter.createCRUDService().update(((IAccounterCore) core),
				transactionCallBack);
	}

	private boolean isprocessingRequestAdd(final IAccounterWidget widget) {
		if (widget instanceof ExpenseClaimView) {
			if (((ExpenseClaimView) widget).isProcessingAdded) {
				return true;
			} else {
				((ExpenseClaimView) widget).isProcessingAdded = true;
				return false;
			}
		}
		if (widget instanceof AwaitingAuthorisationView) {
			if (((AwaitingAuthorisationView) widget).isProcessingAdded) {
				return true;
			} else {
				((AwaitingAuthorisationView) widget).isProcessingAdded = true;
				return false;
			}
		}
		return false;
	}

	public <T extends IAccounterCore, P extends IAccounterCore> void voidTransaction(
			final AccounterCoreType type, String transactionID,
			final IAccounterWidget widget) {

		currentrequestedWidget = widget;
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (!GWT.isScript()) {
					InvalidOperationException exception = new InvalidOperationException();
					exception
							.setStatus(InvalidOperationException.UPDATE_FAILED);
					exception.setID(currentrequestedWidget.getID());
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
						cmd.setID(widget.getID());
						cmd.setObjectType(type);
						getCompany().processCommand(cmd);
					}
				}

			}
		};
		widget.setID(transactionID);
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

		dialog = UIUtils.getLoadingMessageDialog(Accounter
				.getCustomersMessages().processingRequest());

		dialog.center();
		currentrequestedWidget = widget;

		AsyncCallback<Boolean> transactionCallBack = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {

				if (!GWT.isScript()) {
					InvalidOperationException exception = new InvalidOperationException();
					exception
							.setStatus(InvalidOperationException.DELETE_FAILED);
					exception.setID(currentrequestedWidget.getID());
					getCompany().processCommand(exception);
				}
			}

			public void onSuccess(Boolean result) {

				if (!GWT.isScript()) {
					if (result != null && result) {
						AccounterCommand cmd = new AccounterCommand();
						cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
						cmd.setData(clientCompany);
						cmd.setID(widget.getID());
						cmd.setObjectType(clientCompany.getObjectType());
						getCompany().processCommand(cmd);
					} else {
						onFailure(null);
					}
				}
			}

		};
		widget.setID(clientCompany.getID());
		Accounter.createCRUDService().updateCompany(clientCompany,
				transactionCallBack);

	}

	public void updateCompanyPreferences(
			final ClientCompanyPreferences preferences,
			final IAccounterWidget widget) {
		dialog = UIUtils.getLoadingMessageDialog(Accounter
				.getCustomersMessages().processingRequest());

		dialog.center();
		currentrequestedWidget = widget;

		AsyncCallback<Boolean> transactionCallBack = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {

				if (!GWT.isScript()) {
					InvalidOperationException exception = new InvalidOperationException();
					exception
							.setStatus(InvalidOperationException.DELETE_FAILED);
					exception.setID(currentrequestedWidget.getID());
					getCompany().processCommand(exception);
				}
			}

			public void onSuccess(Boolean result) {

				if (!GWT.isScript()) {
					if (result != null && result) {
						AccounterCommand cmd = new AccounterCommand();
						cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
						cmd.setData(preferences);
						cmd.setID(getCompany().id + "pre");
						cmd.setObjectType(preferences.getObjectType());
						getCompany().processCommand(cmd);
					} else {
						onFailure(null);
					}
				}
			}

		};
		widget.setID(getCompany().id + "pre");
		Accounter.createCRUDService().updateCompanyPreferences(preferences,
				transactionCallBack);

	}

	public <A extends IAccounterCore> void deleteObject(final A core,
			AccounterCoreType coreType, final IAccounterWidget widget) {

		dialog = UIUtils.getLoadingMessageDialog(Accounter
				.getCustomersMessages().processingRequest());

		dialog.center();
		currentrequestedWidget = widget;

		AsyncCallback<Boolean> transactionCallBack = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					if (!GWT.isScript()) {
						InvalidOperationException exception = new InvalidOperationException();
						exception
								.setStatus(InvalidOperationException.DELETE_FAILED);
						exception.setID(currentrequestedWidget.getID());
						getCompany().processCommand(exception);
					}
				}
			}

			public void onSuccess(Boolean result) {

				// if (!GWT.isScript()) {
				// if (result != null && result) {
				// AccounterCommand cmd = new AccounterCommand();
				// cmd.setCommand(AccounterCommand.DELETION_SUCCESS);
				// cmd.setData(core);
				// cmd.setID(widget.getID());
				// cmd.setObjectType(core.getObjectType());
				// FinanceApplication.getCompany().processCommand(cmd);
				// } else {
				// onFailure(null);
				// }
				// }
			}

		};
		widget.setID(core.getID());
		Accounter.createCRUDService().delete(coreType, core.getID(),
				transactionCallBack);
	}

	public void updateHomePageLists(IAccounterCore accounterCoreObject) {
		History history = this.historyList.get(0);
		if (history.getView() instanceof FinanceDashboard) {
			FinanceDashboard dashboard = (FinanceDashboard) history.getView();
			dashboard.refreshGrids(accounterCoreObject);
		}
	}

	public void updateDashBoardData(IAccounterCore accounterCoreObject) {
		History history = this.historyList.get(0);
		if (history.getView() instanceof DashBoard) {
			DashBoard dashboard = (DashBoard) history.getView();
			dashboard.refreshWidgetData(accounterCoreObject);
		}
	}

	@SuppressWarnings("unchecked")
	public void fitToSize(int height, int width) {

		this.height = 500;
		this.width = width;
		System.err.println("View Manager" + height);
		if (height - TOP_MENUBAR > 0) {
			// this.scrollPanel.setHeight(height - TOP_MENUBAR + 8 + "px");
			// this.rightCanvas.setHeight(height - TOP_MENUBAR + "px");
		}

		if (width - BORDER > 0) {
			// this.scrollPanel.setWidth(width + BORDER + "px");
			// this.setWidth(width + BORDER + "px");
			// this.rightCanvas.setWidth(width - BORDER + "px");
		}

		if (currentCanvas != null && currentCanvas instanceof BaseView) {

			// if (height - TOP_MENUBAR - 40 > 0)
			// ((BaseView) currentCanvas).setHeightForCanvas(height
			// - TOP_MENUBAR - 40 + "");
			if (!(currentCanvas instanceof UsersView))
				((BaseView) currentCanvas).getButtonPanel().setHeight("30px");
		}

		if (currentCanvas != null)
			currentCanvas.fitToSize(height, width);

	}

	/*
	 * This method used to set the Currently opened Dialog
	 */
	public void setCurrentDialog(BaseDialog<?> currentDialog) {
		this.currentDialog = currentDialog;
	}

	public BaseDialog<?> getCurrentDialog() {
		return this.currentDialog;
	}

	public static void makeAllStaticInstancesNull() {
		viewManagerInstance = null;
	}

	public void showError(String message) {
		errordata.setHTML("<li> " + message + ".");
		commentPanel.setVisible(true);
		AbstractBaseView.errorOccured = true;
	}

	public void appendError(String message) {
		errordata.setHTML(errordata.getHTML() + "<li> " + message + ".");
		commentPanel.setVisible(true);
		AbstractBaseView.errorOccured = true;
	}

	public void restoreErrorBox() {
		errordata.setHTML("");
		commentPanel.setVisible(false);
	}

	public void showErrorInCurrectDialog(String message) {
		if (BaseDialog.errordata != null && BaseDialog.commentPanel != null) {
			BaseDialog.errordata.setHTML("<li> " + message + ".");
			BaseDialog.commentPanel.setVisible(true);
		}
	}

}
