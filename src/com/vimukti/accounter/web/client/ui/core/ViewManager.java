package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAdvertisement;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.Features;
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
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.TransactionMeterPanel;
import com.vimukti.accounter.web.client.ui.company.TransactionsCenterView;
import com.vimukti.accounter.web.client.ui.core.HistoryList.HistoryItem;
import com.vimukti.accounter.web.client.ui.customers.CustomerCenterView;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.VendorCenterView;
import com.vimukti.accounter.web.client.ui.search.SearchInputAction;
import com.vimukti.accounter.web.client.ui.settings.InventoryCentreView;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

/**
 * 
 * 
 */

public class ViewManager extends FlowPanel {

	protected static AccounterMessages messages = Global.get().messages();

	private static final int VIEW_MANAGER_BODY_WIDTH = 1000;

	private static final int REQUIRED_SPACE = 350;

	/**
	 * This reference var. holds currently opened view. it is not only
	 * AbstractBaseView, it is may be AbstractReportView also
	 */
	public AbstractView<?> existingView;

	public final Map<String, Object> viewDataHistory = new HashMap<String, Object>();

	private MainFinanceWindow mainWindow;

	private final HistoryList views = new HistoryList();

	private ToolBar toolBar;

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

	private Map<String, String> keyValues = new HashMap<String, String>();
	ImageButton addNewButton;

	ButtonGroup group1;
	ButtonGroup group2;
	ButtonGroup group3;

	private SimplePanel viewHolder;

	ButtonGroup group4;
	ButtonGroup group5;
	ButtonGroup group6;
	ButtonGroup group7;
	ButtonGroup group8;

	public ViewManager() {

	}

	private void getAdvertisePanel(final StyledPanel rightPanel) {
		Accounter.createHomeService().getAdvertisements(
				new AsyncCallback<ArrayList<ClientAdvertisement>>() {

					@Override
					public void onSuccess(
							ArrayList<ClientAdvertisement> advertisements) {

						if ((advertisements != null)
								&& !(advertisements.isEmpty())) {
							for (ClientAdvertisement clientAdvertisement : advertisements) {
								String url = clientAdvertisement.getUrl();
								url = getReplacedURL(url);
								Frame frame = new Frame(url);
								frame.setSize(clientAdvertisement.getWidth()
										+ "px", clientAdvertisement.getHeight()
										+ "px");
								rightPanel.add(frame);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
	}

	protected String getReplacedURL(String url) {
		Set<Entry<String, String>> entrySet = keyValues.entrySet();
		for (Entry<String, String> entry : entrySet) {
			url = url.replaceAll("\\{" + entry.getKey() + "\\}",
					entry.getValue());
		}
		return url;
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

	private ButtonGroup group10;

	private String getUrl() {
		return "http://help.accounterlive.com/" + url;
	}

	private void initializeActivityManager() {
		this.manager = new ActivityManager(new AccounterActivityMapper(),
				Accounter.getEventBus());
		manager.setDisplay(viewHolder);
	}

	protected boolean defaultPresumtion(String eventTarget) {
		return eventTarget.contains("HTMLInputElement")
				|| eventTarget.contains("HTMLSelectElement")
				|| eventTarget.contains("HTMLTextAreaElement");
	}

	protected void historyChanged(String value) {

		if (value != null && views.current() != null
				&& value.equals(views.current().token)) {
			return;
		}

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
		addRequiredButtons();

		if (existingView instanceof IEditableView
				&& ((IEditableView) existingView).canEdit()) {
			group4.add(editButton);
		} else {
			group4.remove(editButton);
		}

		if ((existingView instanceof BaseListView)
				|| (existingView instanceof TransactionsCenterView)
				|| (existingView instanceof InventoryCentreView)) {
			String labelString;
			if (existingView instanceof TransactionsCenterView) {
				TransactionsCenterView centerView = (TransactionsCenterView) existingView;
				labelString = centerView.baseListView.getAddNewLabelString();
			} else if (existingView instanceof InventoryCentreView) {
				labelString = ((InventoryCentreView) existingView)
						.getAddNewLabelString();
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

		if (existingView instanceof CustomerCenterView
				&& Accounter.getUser().isCanDoUserManagement()) {
			addCustomerButton.setText(messages.addNew(Global.get().Customer()));
			group7.add(addCustomerButton);
		} else {
			removeAddCustomerButton();
		}

		if (existingView instanceof VendorCenterView
				&& Accounter.getUser().isCanDoUserManagement()) {
			addVendorButton.setText(messages.addNew(Global.get().Vendor()));
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
	@SuppressWarnings("unchecked")
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
				item.action.isCalledFromHistory = true;
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
		group1.getElement().setId("group1");

		group2 = new ButtonGroup();
		group2.getElement().setId("group2");

		group3 = new ButtonGroup();
		group3.getElement().setId("group3");

		group4 = new ButtonGroup();
		group4.getElement().setId("group4");

		group5 = new ButtonGroup();
		group5.getElement().setId("group5");

		group6 = new ButtonGroup();
		group6.getElement().setId("group6");

		group7 = new ButtonGroup();
		group7.getElement().setId("group7");

		group8 = new ButtonGroup();
		group8.getElement().setId("group8");

		group9 = new ButtonGroup();
		group9.getElement().setId("group9");

		group10 = new ButtonGroup();
		group10.getElement().setId("group10");

		viewTitleLabel = new Label(messages.dashBoard());
		viewTitleLabel.addStyleName("viewTitle");

		previousButton = new ImageButton(Accounter.getFinanceImages()
				.previousIcon());
		previousButton.getElement().setId("previousButton");
		previousButton.setTitle(messages.clickThisToOpen(messages.previous()));
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.back();
			}
		});
		nextButton = new ImageButton(Accounter.getFinanceImages().nextIcon());
		nextButton.getElement().setId("nextButton");
		nextButton.setTitle(messages.clickThisToOpen(messages.next()));
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.forward();
			}
		});
		printButton = new ImageButton(messages.print(), Accounter
				.getFinanceImages().Print1Icon());
		printButton.getElement().setId("printButton");
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.print();

			}
		});

		exportButton = new ImageButton(messages.exportToCSV(), Accounter
				.getFinanceImages().exportIcon());
		exportButton.getElement().setId("exportButton");
		exportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				existingView.exportToCsv();

			}
		});

		editButton = new ImageButton(messages.edit(), Accounter
				.getFinanceImages().editIcon());
		editButton.getElement().setId("editButton");
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
		configButton.getElement().setId("configButton");
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
		addCustomerButton.getElement().setId("addCustomerButton");
		addCustomerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewCustomerAction().run();
			}
		});

		addVendorButton = new ImageButton(
				messages.addNew(Global.get().Vendor()), Accounter
						.getFinanceImages().portletPageSettings());
		addVendorButton.addStyleName("settingsButton");
		addVendorButton.getElement().setId("addVendorButton");
		addVendorButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewVendorAction().run();
			}
		});

		closeButton = new ImageButton(Accounter.getFinanceImages()
				.closeButton());
		closeButton.setTitle(messages.clickThisTo(messages.close(),
				messages.view()));
		closeButton.getElement().setId("closeButton");
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
		searchButton.getElement().setId("searchButton");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// ActionFactory.getSearchInputAction().run();
				String historyToken = new SearchInputAction().getHistoryToken();
				History.newItem(historyToken, false);
				Accounter.getMainFinanceWindow().historyChanged(historyToken);
			}
		});

		addNewButton = new ImageButton("", Accounter.getFinanceImages()
				.createAction());
		addNewButton.getElement().setId("addNewButton");
		addNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BaseListView baseListView;
				Action action;
				if (existingView instanceof TransactionsCenterView) {
					TransactionsCenterView centerView = (TransactionsCenterView) existingView;
					baseListView = centerView.baseListView;
					action = baseListView.getAddNewAction();
				} else if (existingView instanceof InventoryCentreView) {
					action = ((InventoryCentreView) existingView)
							.getAddNewAction();
				} else {
					baseListView = (BaseListView) existingView;
					action = baseListView.getAddNewAction();
				}
				if (action != null) {
					action.run(null, false);
				}
			}

		});

		previousButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		nextButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");

		group1.add(previousButton);
		group1.add(nextButton);
		group1.add(viewTitleLabel);
		addRequiredButtons();
		group4.add(editButton);
		group9.add(addNewButton);
		group2.add(exportButton);
		group2.add(printButton);
		group3.add(closeButton);
		group5.add(configButton);
		group6.add(searchButton);
		group7.add(addCustomerButton);
		group8.add(addVendorButton);

		exportButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		printButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");

		// StyledPanel horizontalPanel = new StyledPanel();
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

		// toolBar.add(group1);
		// toolBar.add(group5);
		// toolBar.add(group2);
		// toolBar.add(group9);
		// toolBar.add(group4);
		// toolBar.add(group7);
		// toolBar.add(group8);
		// toolBar.add(group6);
		// toolBar.add(group3);

		toolBar.add(group1);
		StyledPanel buttonsPanel = new StyledPanel("buttonsPanel");

		buttonsPanel.add(group5);
		buttonsPanel.add(group2);
		buttonsPanel.add(group9);
		buttonsPanel.add(group4);
		if (Accounter.hasPermission(Features.TRANSACTION_NAVIGATION)) {
			buttonsPanel.add(group10);
		}
		buttonsPanel.add(group7);
		buttonsPanel.add(group8);
		buttonsPanel.add(group6);
		buttonsPanel.add(group3);

		toolBar.add(buttonsPanel);

		toolBar.addStyleName("group-toolbar");
	}

	protected void addRequiredButtons() {
		group10.clear();
		if (existingView instanceof AbstractTransactionBaseView) {
			AbstractTransactionBaseView view = (AbstractTransactionBaseView) existingView;
			view.addButtons(group10);
		}
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
				// this.setCellWidth(helpPanel, "50%");
			} else {
				this.add(prevhelpPanel);
			}
			// this.setCellWidth(helpPanel, "50%");
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
				// this.setCellWidth(helpPanel, "50%");
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
				// this.setCellWidth(helpPanel, "50%");
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

	public void createView(MainFinanceWindow financeWindow) {
		keyValues.put("ispaid", getCompany().isPaid() ? "Yes" : "No");
		// for bookkeeping value
		keyValues.put("bookKeeping", getCompany().isBookKeeping() ? "Yes"
				: "No");
		this.mainWindow = financeWindow;
		StyledPanel mainPanel = new StyledPanel("mainPanel");

		StyledPanel rightPanel = createRightPanel();
		StyledPanel leftPanel = new StyledPanel("leftPanel");
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

		if (rightPanel != null) {
			mainPanel.add(rightPanel);
		}
		this.addStyleName("main_manager");
		this.add(mainPanel);
		initilizeToolBar();
		initializeActivityManager();
	}

	protected StyledPanel createRightPanel() {
		StyledPanel panel = new StyledPanel("rightPanel");
		if (!Accounter.hasPermission(Features.TRANSACTIONS)) {
			TransactionMeterPanel meterPanel = new TransactionMeterPanel();
			panel.add(meterPanel);
		}
		getAdvertisePanel(panel);
		panel.addStyleName("frame_manager");
		return panel;
	}

}
