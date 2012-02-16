package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAdvertisement;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.help.HelpDialog;
import com.vimukti.accounter.web.client.help.HelpPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DashBoardView;
import com.vimukti.accounter.web.client.ui.HistoryToken;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.StatementImportViewAction;
import com.vimukti.accounter.web.client.ui.company.TransactionsCenterView;
import com.vimukti.accounter.web.client.ui.core.HistoryList.HistoryItem;
import com.vimukti.accounter.web.client.ui.customers.CustomerCenterView;
import com.vimukti.accounter.web.client.ui.customers.VendorCenterView;

/**
 * 
 * 
 */

public class ViewManager extends HorizontalPanel {

	protected static AccounterMessages messages = Global.get().messages();

	private static final int VIEW_MANAGER_BODY_WIDTH = 1000;

	private static final int REQUIRED_SPACE = 350;

	/**
	 * This reference var. holds currently opened view. it is not only
	 * AbstractBaseView, it is may be AbstractReportView also
	 */
	public AbstractView<?> existingView;

	public final Map<String, Object> viewDataHistory = new HashMap<String, Object>();

	private final MainFinanceWindow mainWindow;

	private final HistoryList views = new HistoryList();

	private final ToolBar toolBar;

	private ActivityManager manager;

	private ImageButton previousButton;

	private ImageButton nextButton;

	private ImageButton printButton;

	private ImageButton exportButton;

	private ImageButton editButton;

	private ImageButton closeButton;

	private ImageButton configButton;

	private ImageButton addCustomerButton;

	private ImageButton addVendorButton;

	private ImageButton searchButton;

	private Label viewTitleLabel;

	ImageButton addNewButton;

	ButtonGroup group1;
	ButtonGroup group2;
	ButtonGroup group3;

	private final SimplePanel viewHolder;

	ButtonGroup group4;
	ButtonGroup group5;
	ButtonGroup group6;
	ButtonGroup group7;
	ButtonGroup group8;

	public ViewManager(MainFinanceWindow financeWindow) {
		this.mainWindow = financeWindow;
		this.setWidth("100%");
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setWidth("100%");
		VerticalPanel rightPanel = new VerticalPanel();
		getAdvertisePanel(rightPanel);
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.addStyleName("view_manager_body");
		// leftPanel.setWidth("100%");
		this.viewHolder = new SimplePanel();
		viewHolder.addStyleName("viewholder");

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				historyChanged(event.getValue());
			}
		});
		// handleBackSpaceEvent();
		this.toolBar = new ToolBar();
		leftPanel.add(toolBar);
		leftPanel.add(viewHolder);
		mainPanel.add(leftPanel);
		leftPanel.getElement().getParentElement().addClassName("view_manager");
		rightPanel.addStyleName("frame_manager");
		mainPanel.add(rightPanel);
		this.addStyleName("main_manager");
		this.add(mainPanel);
		initilizeToolBar();
		initializeActivityManager();
	}

	@SuppressWarnings("deprecation")
	private void getAdvertisePanel(final VerticalPanel rightPanel) {
		Accounter.createHomeService().getAdvertisements(
				new AsyncCallback<List<ClientAdvertisement>>() {

					@Override
					public void onSuccess(
							List<ClientAdvertisement> advertisements) {

						if ((advertisements != null)
								&& !(advertisements.isEmpty())) {
							final double addPanelWidth = advertisements.get(0)
									.getWidth();
							Window.addWindowResizeListener(new WindowResizeListener() {

								@Override
								public void onWindowResized(int width,
										int height) {
									if ((addPanelWidth + 960) <= Window
											.getClientWidth()) {
										rightPanel.setVisible(true);
									} else {
										rightPanel.setVisible(false);
									}
								}
							});
							if ((addPanelWidth + 960) <= Window
									.getClientWidth()) {
								for (ClientAdvertisement clientAdvertisement : advertisements) {
									Frame frame = new Frame(clientAdvertisement
											.getUrl());
									frame.setSize(
											clientAdvertisement.getWidth()
													+ "px",
											clientAdvertisement.getHeight()
													+ "px");
									rightPanel.add(frame);
								}
							}
						}
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	private HelpPanel helpPanel;

	private Widget createHelpPanel() {
		if (isHelpPanelEnabled) {
			helpPanel = new HelpPanel();
			helpPanel.setHelpUrl(this.getUrl());
			helpPanel.setIsHelpPanel(true);
			helpPanel.addStyleName("view_help_panel");
			return helpPanel;
		} else {
			return null;
		}
	}

	String url = "";

	private ButtonGroup group9;

	private String getUrl() {
		return "http://help.accounterlive.com/" + url;
	}

	private void initializeActivityManager() {
		this.manager = new ActivityManager(new AccounterActivityMapper(),
				Accounter.getEventBus());
		manager.setDisplay(viewHolder);
	}

	private void handleBackSpaceEvent() {
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			@Override
			public void onPreviewNativeEvent(final NativePreviewEvent event) {
				Event e = Event.as(event.getNativeEvent());

				if (e.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if (!defaultPresumtion(e.getEventTarget().toString())) {
						e.preventDefault();
						// viewManager.closeCurrentView();
					}
					return;
				}
			}
		});
	}

	protected boolean defaultPresumtion(String eventTarget) {
		return eventTarget.contains("HTMLInputElement")
				|| eventTarget.contains("HTMLSelectElement")
				|| eventTarget.contains("HTMLTextAreaElement");
	}

	protected void historyChanged(String value) {
		HistoryToken token = null;
		try {
			token = new HistoryToken(value);
		} catch (Exception e) {
			// Unable to parse the token, done do anything
			e.printStackTrace();
		}
		// Check if it some thing we have kept alive
		HistoryItem item = getViewFromHistory(token.getToken());
		if (item != null && item.view != null) {
			// I think we have to remove this view from history.
			// views.list.remove(item);
			showView(item.view, item.action, true);
		} else {
			this.mainWindow.historyChanged(value);
		}
	}

	private void showView(final AbstractView<?> newview, final Action action,
			boolean shouldAskToSave) {
		if (this.existingView != null) {
			// We already have some view visible
			if (this.existingView instanceof IEditableView) {
				IEditableView editView = (IEditableView) existingView;
				if (shouldAskToSave && editView.isDirty()) {
					tryToClose(editView, new Command() {

						@Override
						public void execute() {
							// Called if the prev view can be closed
							existingView.removeFromParent();
							showNewView(newview, action);
						}
					});
				} else {
					// We can just remove it and put new one
					this.existingView.removeFromParent();
					showNewView(newview, action);
				}
			} else {
				// We can just remove it and put new one
				this.existingView.removeFromParent();
				showNewView(newview, action);
			}
		} else {
			showNewView(newview, action);
		}
	}

	/**
	 * Try to close the current
	 * 
	 * @param editView
	 * @param command
	 */
	private void tryToClose(final IEditableView editView, final Command command) {
		Accounter.showWarning(messages.W_106(),
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return true;
					}

					@Override
					public boolean onNoClick() {
						remove(existingView);
						command.execute();
						return true;
					}

					@Override
					public boolean onYesClick() {
						editView.onSave(false);
						command.execute();
						return true;
					}
				});
	}

	private void showNewView(AbstractView newview, Action action) {
		Object input = action.getInput();
		String token = action.getHistoryToken();

		// Save history
		if (existingView instanceof ISavableView) {
			viewDataHistory.put(existingView.getAction().getHistoryToken(),
					((ISavableView) existingView).saveView());
		}

		if (newview.getManager() == null) {
			newview.setManager(this);
			// newview.setPreferences(Accounter.getCompany().getPreferences());
			if (input != null) {
				newview.setData(input);
			}
			newview.init();
			if (input == null && newview instanceof ISavableView) {
				Object object = viewDataHistory.get(action.getHistoryToken());
				if (object != null) {
					((ISavableView) newview).restoreView(object);
				}
			}
			newview.initData();
		}

		if (input instanceof IAccounterCore) {
			token = HistoryTokenUtils.getTokenWithID(token,
					(IAccounterCore) input);
		}
		this.views.add(new HistoryItem(newview, action, token));
		History.newItem(token, false);

		if (existingView != null) {
			existingView.removeFromParent();
		}
		existingView = newview;
		if (existingView instanceof BaseView) {
			if (((BaseView<IAccounterCore>) existingView).isInViewMode()) {
				viewTitleLabel.setText(action.getCatagory() + "  >  "
						+ action.getViewModeText());
			} else {
				viewTitleLabel.setText(action.getCatagory() + "  >  "
						+ action.getText());
			}
		} else {
			viewTitleLabel.setText(action.getCatagory() + "  >  "
					+ action.getText());
		}
		if (exportButton != null)
			exportButton.setTitle(messages.clickThisTo(messages.exportToCSV(),
					existingView.getAction().getViewName()));
		if (printButton != null)
			printButton.setTitle(messages.clickThisTo(messages.print(),
					existingView.getAction().getViewName()));
		if (editButton != null)
			editButton.setTitle(messages.clickThisTo(messages.edit(),
					existingView.getAction().getViewName()));
		viewHolder.add(newview);
		updateButtons();
	}

	public void removeEditButton() {
		group4.remove(editButton);
	}

	public void removeConfigButton() {
		group5.remove(configButton);
	}

	public void removeAddCustomerButton() {
		group7.remove(addCustomerButton);
	}

	public void removeAddVendorButton() {
		group8.remove(addVendorButton);
	}

	public void updateButtons() {
		if (existingView instanceof IEditableView
				&& ((IEditableView) existingView).canEdit()) {
			group4.add(editButton);
		} else {
			group4.remove(editButton);
		}

		if ((existingView instanceof BaseListView)
				|| (existingView instanceof TransactionsCenterView)) {
			String labelString;
			if (existingView instanceof TransactionsCenterView) {
				TransactionsCenterView centerView = (TransactionsCenterView) existingView;
				labelString = centerView.baseListView.getAddNewLabelString();
			} else {
				labelString = ((BaseListView) existingView)
						.getAddNewLabelString();
			}
			if (labelString != null && !labelString.isEmpty()) {
				addNewButton.setText(labelString);
				group9.add(addNewButton);
			} else {
				group9.remove(addNewButton);
			}
		} else {
			group9.remove(addNewButton);
		}

		if (existingView instanceof IPrintableView) {
			if (((IPrintableView) existingView).canExportToCsv()) {
				group2.add(exportButton);
			} else {
				group2.remove(exportButton);
			}
			if (((IPrintableView) existingView).canPrint()) {
				group2.add(printButton);
			} else {
				group2.remove(printButton);
			}
		} else {
			group2.remove(exportButton);
			group2.remove(printButton);

		}
		if (existingView instanceof DashBoardView) {
			group5.add(configButton);
		} else {
			removeConfigButton();
		}

		if (existingView instanceof CustomerCenterView) {
			group7.add(addCustomerButton);
		} else {
			removeAddCustomerButton();
		}

		if (existingView instanceof VendorCenterView) {
			group8.add(addVendorButton);
		} else {
			removeAddVendorButton();
		}
	}

	/**
	 * Checks in local history if that view is already open
	 * 
	 * @param token
	 * @return
	 */
	private HistoryItem getViewFromHistory(String token) {
		return views.getView(token);
	}

	public void closeCurrentView() {
		closeCurrentView(true);
	}

	/**
	 * Called when we want to remove current view and put previous view back
	 */
	public void closeCurrentView(boolean restorePreviousView) {
		if (this.existingView == null) {
			return;
		}
		// If this is the last view, then do not close
		if (this.views.list.size() == 1) {
			return;
		}
		// Take the data and set to new view
		Object data = this.existingView.getData();

		this.existingView.removeFromParent();
		HistoryItem current = this.views.current();

		HistoryItem item = this.views.previous();

		if (restorePreviousView) {
			if (item.view == null) {
				item.action.run();
			} else {
				// Save history
				if (existingView instanceof ISavableView) {
					viewDataHistory.put(existingView.getAction()
							.getHistoryToken(), ((ISavableView) existingView)
							.saveView());
				}
				existingView.removeFromParent();
				this.existingView = item.view;
				ActionCallback callback = current.action.getCallback();
				if (data != null && callback != null) {
					callback.actionResult(data);
				}

				if (item.view instanceof BaseView
						&& (((BaseView<IAccounterCore>) item.view)
								.isInViewMode())) {
					viewTitleLabel.setText(item.action.getCatagory() + "  >  "
							+ item.action.getViewModeText());
				} else {
					viewTitleLabel.setText(item.action.getCatagory() + "  >  "
							+ item.action.getText());
				}
				viewHolder.add(item.view);
				this.views.add(item);
				History.newItem(item.action.getHistoryToken(), false);
			}
		} else {
			this.views.add(item);
		}
		updateButtons();
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
	 * @return
	 */
	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	/**
	 * Set the width and height to the given values.
	 * 
	 * @param height
	 * @param width
	 */
	public void fitToSize(int height, int width) {

		if (existingView != null) {
			existingView.fitToSize(height, width);
		}
	}

	public void showView(AbstractView<?> view, Object data,
			Boolean isDependent, Action action) {
		if (!isDependent) {
			this.views.clear();
		}
		view.setAction(action);
		showHelp(action.getHelpToken());
		showView(view, action, !isDependent);
	}

	void initilizeToolBar() {

		group1 = new ButtonGroup();
		group2 = new ButtonGroup();
		group3 = new ButtonGroup();
		group4 = new ButtonGroup();
		group5 = new ButtonGroup();
		group6 = new ButtonGroup();
		group7 = new ButtonGroup();
		group8 = new ButtonGroup();
		group9 = new ButtonGroup();
		viewTitleLabel = new Label(messages.dashBoard());
		viewTitleLabel.addStyleName("viewTitle");

		previousButton = new ImageButton(Accounter.getFinanceImages()
				.previousIcon());
		previousButton.setTitle(messages.clickThisToOpen(messages.previous()));
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.back();
			}
		});
		nextButton = new ImageButton(Accounter.getFinanceImages().nextIcon());
		nextButton.setTitle(messages.clickThisToOpen(messages.next()));
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.forward();
			}
		});
		printButton = new ImageButton(messages.print(), Accounter
				.getFinanceImages().Print1Icon());

		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.print();

			}
		});

		exportButton = new ImageButton(messages.exportToCSV(), Accounter
				.getFinanceImages().exportIcon());
		exportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				existingView.exportToCsv();

			}
		});

		editButton = new ImageButton(messages.edit(), Accounter
				.getFinanceImages().editIcon());
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				viewTitleLabel.setText(existingView.getAction().getCatagory()
						+ "  >  " + existingView.getAction().getEditText());
				existingView.onEdit();
			}
		});

		configButton = new ImageButton(messages.configurePortlets(), Accounter
				.getFinanceImages().portletPageSettings());
		configButton.addStyleName("settingsButton");
		configButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				((DashBoardView) existingView).getPage().createSettingsDialog()
						.showRelativeTo(configButton);
			}
		});

		addCustomerButton = new ImageButton(messages.addNew(Global.get()
				.Customer()), Accounter.getFinanceImages()
				.portletPageSettings());
		addCustomerButton.addStyleName("settingsButton");
		addCustomerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getNewCustomerAction().run();
			}
		});

		addVendorButton = new ImageButton(
				messages.addNew(Global.get().Vendor()), Accounter
						.getFinanceImages().portletPageSettings());
		addVendorButton.addStyleName("settingsButton");
		addVendorButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getNewVendorAction().run();
			}
		});

		closeButton = new ImageButton(Accounter.getFinanceImages()
				.closeButton());
		closeButton.setTitle(messages.clickThisTo(messages.close(),
				messages.view()));
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.cancel();
			}
		});

		searchButton = new ImageButton(Accounter.getFinanceImages()
				.searchButton());
		searchButton.setTitle(messages.clickThisTo(messages.open(),
				messages.search()));
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// ActionFactory.getSearchInputAction().run();
				String historyToken = ActionFactory.getSearchInputAction()
						.getHistoryToken();
				History.newItem(historyToken, false);
				Accounter.getMainFinanceWindow().historyChanged(historyToken);
			}
		});

		addNewButton = new ImageButton("", Accounter.getFinanceImages()
				.createAction());
		addNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BaseListView baseListView;
				if (existingView instanceof TransactionsCenterView) {
					TransactionsCenterView centerView = (TransactionsCenterView) existingView;
					baseListView = centerView.baseListView;
				} else {
					baseListView = (BaseListView) existingView;
				}
				Action action = baseListView.getAddNewAction();
				if (action != null) {
					action.run(null, false);
				}
			}

		});
		ImageButton importButton = new ImageButton(messages.importFile(),
				Accounter.getFinanceImages().addIcon());
		importButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				List<String[]> list = new ArrayList<String[]>();
				list.add(new String[] { "name", "number", "opening balance",
						"items", "bank number", "sales tax number" });
				list.add(new String[] { "inv_name", "inv_number",
						"inv_opening balance", "inv_items", "inv_bank number",
						"inv_sales tax number" });
				list.add(new String[] { "cash_sale_name", "cash_sale_number",
						"cash_sale_opening balance", "cash_sale_items",
						"cash_sale_bank number", "cash_sale_sales tax number" });
				list.add(new String[] { "enter_bill_name", "enter_bill_number",
						"enter_bill_opening balance", "enter_bill_items",
						"enter_bill_bank number", "enter_bil_sales tax number" });
				list.add(new String[] { "48675", "56569", "9867", "23121",
						"32423", "324324" });
				list.add(new String[] { "7777", "2222", "11111", "99999",
						"33333", "55555" });
				StatementImportViewAction action = new StatementImportViewAction(
						list, 1);
				action.run();
			}
		});

		previousButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		nextButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		group1.add(previousButton);
		group1.add(nextButton);
		previousButton.getElement().getParentElement()
				.addClassName("prebutton");
		nextButton.getElement().getParentElement().addClassName("nextbutton");

		group1.add(viewTitleLabel);

		group4.add(editButton);

		group9.add(addNewButton);
		group2.add(exportButton);
		group2.add(printButton);
		group2.addStyleName("print_export_button");

		group3.add(closeButton);
		group5.add(configButton);

		group6.add(searchButton);
		group6.add(importButton);
		group7.add(addCustomerButton);
		group8.add(addVendorButton);

		exportButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		printButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");

		// HorizontalPanel horizontalPanel = new HorizontalPanel();
		// horizontalPanel.setWidth("100%");
		// horizontalPanel.add(group1);
		// horizontalPanel.add(group2);
		// horizontalPanel.add(group7);
		// horizontalPanel.add(group8);
		// horizontalPanel.add(group9);
		// horizontalPanel.add(group4);
		// horizontalPanel.add(group5);
		// horizontalPanel.add(group6);
		// horizontalPanel.add(group3);
		//
		// toolBar.add(horizontalPanel);

		toolBar.add(group1);
		group1.getElement().getStyle().setFloat(Float.LEFT);
		toolBar.add(group3);
		toolBar.add(group6);
		toolBar.add(group5);
		toolBar.add(group2);
		toolBar.add(group9);
		toolBar.add(group4);
		toolBar.add(group7);
		toolBar.add(group8);
		toolBar.addStyleName("group-toolbar");
	}

	public void toggleHelpPanel(boolean isHelpPanel) {
		if (!isPanelEnabled() && !isHelpPanel) {
			return;
		}
		if (!isHelpPanel) {
			HelpPanel prevhelpPanel = helpPanel;
			helpPanel.removeFromParent();
			helpDialog.removeFromParent();
			helpPanel = (HelpPanel) createHelpPanel();
			if (helpPanel != null) {
				this.add(helpPanel);
				this.setCellWidth(helpPanel, "50%");
			} else {
				this.add(prevhelpPanel);
			}
			this.setCellWidth(helpPanel, "50%");
		} else {
			helpPanel.removeFromParent();
			helpPanel.setIsHelpPanel(false);
			createHelpDialog();
		}
	}

	private boolean isPanelEnabled() {
		if (Window.getClientWidth() - VIEW_MANAGER_BODY_WIDTH > REQUIRED_SPACE) {
			return true;
		}
		return false;
	}

	private HelpDialog helpDialog;

	private boolean isHelpPanelEnabled = true;

	private void createHelpDialog() {
		helpPanel.setIsHelpPanel(false);
		if (helpDialog != null) {
			helpDialog.removeFromParent();
		}
		helpDialog = new HelpDialog(helpPanel);
		helpDialog.show();
	}

	public HelpPanel getHelpPanel() {
		return helpPanel;
	}

	public void setHelpPanel(HelpPanel helpPanel) {
		this.helpPanel = helpPanel;
	}

	public void showHelp(String helpTopic) {
		url = helpTopic;
		if (!isHelpPanelEnabled) {
			if (helpPanel != null) {
				helpPanel.removeFromParent();
				if (helpDialog != null) {
					helpDialog.removeFromParent();
				}
				helpPanel.setIsRemoved(true);
			}
			return;
		}
		if (helpTopic == null || helpTopic.isEmpty()) {
			return;
		}

		if (helpPanel == null) {
			return;
		}
		helpPanel.setHelpUrl(getUrl());
	}

	public void addRemoveHelpPanel() {
		if (!isHelpPanelEnabled) {
			Window.open("http://help.accounterlive.com", "_blank", "");
			return;
		}

		if (helpPanel == null) {
			helpPanel = (HelpPanel) createHelpPanel();
			if (isPanelEnabled()) {
				this.add(helpPanel);
				this.setCellWidth(helpPanel, "50%");
			} else {
				helpPanel.setButtonDisabled(false);
				helpPanel.setButtonPushed(true);
				createHelpDialog();
			}
			return;
		}

		if (helpDialog != null) {
			helpDialog.removeFromParent();
		}
		if (helpPanel != null) {
			helpPanel.removeFromParent();
		}
		if (helpPanel.isRemoved()) {
			if (!helpPanel.isHelpPanel()) {
				helpPanel = (HelpPanel) createHelpPanel();
			}
			if (isPanelEnabled()) {
				this.add(helpPanel);
				this.setCellWidth(helpPanel, "50%");
				helpPanel.setIsHelpPanel(true);
			} else {
				helpPanel.setButtonDisabled(false);
				helpPanel.setButtonPushed(true);
				createHelpDialog();
			}
			helpPanel.setIsRemoved(false);
			helpPanel.setHelpUrl(getUrl());
		} else {
			helpPanel.setIsRemoved(true);
		}
	}

	public void setHelpPannelEnabled(boolean isEnabled) {
		this.isHelpPanelEnabled = isEnabled;
	}

	public boolean isHelpPanelEnabled() {
		return this.isHelpPanelEnabled;
	}
}
