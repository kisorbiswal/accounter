package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryToken;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.HistoryList.HistoryItem;

/**
 * 
 * 
 */

public class ViewManager extends VerticalPanel {

	/**
	 * This reference var. holds currently opened view. it is not only
	 * AbstractBaseView, it is may be AbstractReportView also
	 */
	private ParentCanvas<?> existingView;

	private MainFinanceWindow mainWindow;

	private HistoryList views = new HistoryList();

	private ToolBar toolBar;

	public ViewManager(MainFinanceWindow financeWindow) {
		this.mainWindow = financeWindow;
		addStyleName("view_manager");
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				historyChanged(event.getValue());
			}
		});
		// handleBackSpaceEvent();
		this.toolBar = new ToolBar();
		this.add(toolBar);
		initilizeToolBar();
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
			showView(item.view, item.action, true);
		} else {
			this.mainWindow.historyChanged(value);
		}
	}

	private void showView(final ParentCanvas<?> newview, final Action action,
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
		Accounter.showWarning(AccounterWarningType.saveOrClose,
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

	private void showNewView(ParentCanvas newview, Action action) {
		Object input = action.getInput();
		String token = action.getHistoryToken();

		if (newview.getManager() == null) {
			newview.init();
			if (input != null) {
				newview.setData(input);
			}
			newview.initData();
			newview.setManager(this);
		}

		this.views.add(new HistoryItem(newview, action));

		if (input instanceof IAccounterCore) {
			token = HistoryTokenUtils.getTokenWithID(token,
					(IAccounterCore) input);
		}
		History.newItem(token, false);

		if (existingView != null) {
			existingView.removeFromParent();
		}
		existingView = newview;
		add(newview);
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

	/**
	 * Called when we want to remove current view and put previous view back
	 */
	public void closeCurrentView() {
		if (this.existingView == null) {
			return;
		}
		// If this is the last view, then do not close
		if (this.views.list.size() == 1) {
			return;
		}
		this.existingView.removeFromParent();
		HistoryItem item = this.views.previous();
		if (item.view == null) {
			item.action.run();
		} else {
			this.existingView = item.view;
			this.add(item.view);
			this.views.add(item);
			History.newItem(item.action.getHistoryToken(), false);
		}
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

	public void showView(ParentCanvas<?> view, Object data,
			Boolean isDependent, Action action) {
		if (!isDependent) {
			this.views.clear();
		}
		view.setAction(action);
		showView(view, action, !isDependent);
	}

	void initilizeToolBar() {

		ButtonGroup group1 = new ButtonGroup();
		ButtonGroup group2 = new ButtonGroup();
		ButtonGroup group3 = new ButtonGroup();

		ImageButton previousButton = new ImageButton(Accounter
				.getFinanceImages().previousIcon());
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.back();
			}
		});
		ImageButton nextbutton = new ImageButton(Accounter.getFinanceImages()
				.nextIcon());
		nextbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.forward();
			}
		});
		ImageButton printButton = new ImageButton(
				Accounter.constants().print(), Accounter.getFinanceImages()
						.Print1Icon());
		ImageButton editButton = new ImageButton(Accounter.constants().edit(),
				Accounter.getFinanceImages().editIcon());
		ImageButton closeButton = new ImageButton(
				Accounter.constants().close(), Accounter.getFinanceImages()
						.dialougueCloseicon());
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				closeCurrentView();
			}
		});
		group1.add(nextbutton);
		group1.add(previousButton);

		group2.add(editButton);
		group2.add(printButton);

		group3.add(closeButton);

		toolBar.add(HasHorizontalAlignment.ALIGN_LEFT, group1);
		toolBar.add(HasHorizontalAlignment.ALIGN_RIGHT, group2);
		toolBar.add(HasHorizontalAlignment.ALIGN_RIGHT, group3);
		toolBar.addStyleName("group-toolbar");

	}
}
