package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.HistoryToken;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
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

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				historyChanged(event.getValue());
			}
		});
		// handleBackSpaceEvent();
		this.toolBar=new ToolBar();
		this.add(toolBar);
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
		ParentCanvas<?> view = getViewFromHistory(token.getToken());
		if (view != null) {
			showView(view, token.getToken(), null);
		} else {
			this.mainWindow.historyChanged(value);
		}
	}

	private void showView(final ParentCanvas<?> newview, final String token,
			Object input) {
		if (this.existingView != null) {
			// We already have some view visible
			if (this.existingView instanceof IEditableView) {
				IEditableView editView = (IEditableView) existingView;
				if (editView.isDirty()) {
					tryToClose(editView, new Command() {

						@Override
						public void execute() {
							// Called if the prev view can be closed
							showNewView(newview, token, null);
						}
					});
				} else {
					// We can just remove it and put new one
					this.existingView.removeFromParent();
					showNewView(newview, token, null);
				}
			} else {
				// We can just remove it and put new one
				this.existingView.removeFromParent();
				showNewView(newview, token, null);
			}
		} else {
			showNewView(newview, token, null);
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

	private void showNewView(ParentCanvas newview, String token, Object input) {
		newview.init();
		if (input != null) {
			newview.setData(input);
		}
		newview.initData();

		this.views.add(new HistoryItem(token, newview));

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
	private ParentCanvas<?> getViewFromHistory(String token) {
		return views.getView(token);
	}

	/**
	 * Called when we want to remove current view and put previous view back
	 */
	public void closeCurrentView() {
		if (this.existingView == null) {
			return;
		}
		this.existingView.removeFromParent();
		HistoryItem item = this.views.get(-1);
		this.add(item.view);
		this.views.add(item);
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

	public void showView(ParentCanvas<?> report, Object data,
			Boolean isDependent, Action action) {
		showView(report, action.getHistoryToken(), data);
	}
}
