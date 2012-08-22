package com.vimukti.accounterbb.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.VirtualKeyboard;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.vimukti.accounterbb.core.AccounterConnection;
import com.vimukti.accounterbb.core.AddressListener;
import com.vimukti.accounterbb.core.ConnectionListener;
import com.vimukti.accounterbb.result.Command;
import com.vimukti.accounterbb.result.CommandList;
import com.vimukti.accounterbb.result.Cookie;
import com.vimukti.accounterbb.result.DataStore;
import com.vimukti.accounterbb.result.InputType;
import com.vimukti.accounterbb.result.Record;
import com.vimukti.accounterbb.result.Result;
import com.vimukti.accounterbb.result.ResultList;
import com.vimukti.accounterbb.utils.AnimatedGIFField;
import com.vimukti.accounterbb.utils.GridFieldManager;

public class ResultScreen extends MainScreen implements ConnectionListener,
		RecordSelectionListener, CommandSelectionListener,
		InputSelectionListener, AddressListener {

	protected static final String BACK = "back";
	protected static final String CANCEL = "cancel";
	private static final String ACCOUNTER = "Accounter";

	private AccounterConnection connection;

	private ButtonField cancelField;
	private ButtonField backField;
	private LabelField titleField;

	private AnimatedGIFField animatedGIF;

	private GridFieldManager titleManager;
	private HorizontalFieldManager hManager;

	private ButtonField reconnectButton;

	private LabelField statusLabelField;

	private int MAX_CONNECTION_ATTEMPTS = 20;
	private int interval = 0;
	private int attempts = 0;

	private VerticalFieldManager manager = new VerticalFieldManager(
			VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
	private boolean askUserForDomain = false;

	private String domain;

	public ResultScreen() {
		add(manager);
		instansiateLoadingImage();
		createTitle();

		statusLabelField = new LabelField("Connecting to server");
		startConnection();
		addMenuItem(aboutMenuItem);

	}

	private void startConnection() {

		if (askUserForDomain) {
			final AddressPopup addressPopup = new AddressPopup(this);
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					UiApplication.getUiApplication().pushModalScreen(
							addressPopup);
				}
			});

		} else {
			titleManager.deleteAll();
			displayTitleManagarWhileConnectingToServerAtAppLanchTime();
			connection = new AccounterConnection(this);
			manager.add(statusLabelField);
		}

	}

	private void checkAndConnectIfNeeded() {
		if (connection == null && domain != null) {
			// This situation will come if we have to ask user for domain
			connection = new AccounterConnection(this, domain);
			manager.add(new LabelField("Connecting to server"));
		}
	}

	protected void onDisplay() {
		super.onDisplay();
	}

	private void createTitle() {
		titleManager = new GridFieldManager(3, USE_ALL_WIDTH);
		hManager = new HorizontalFieldManager(FIELD_VCENTER);

		backField = new ButtonField("Back", ButtonField.CONSUME_CLICK
				| FIELD_LEFT) {
			public boolean isFocusable() {
				return isEditable();
			}
		};
		backField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				connection.sendMessage(BACK);
			}
		});
		titleManager.add(backField);

		// for loading image
		hManager.add(animatedGIF);

		titleField = new LabelField(ACCOUNTER, FIELD_VCENTER);
		titleField.setText(ACCOUNTER);
		hManager.add(titleField);

		titleManager.add(hManager);

		cancelField = new ButtonField("Cancel", ButtonField.CONSUME_CLICK
				| FIELD_RIGHT) {
			public boolean isFocusable() {
				return isEditable();
			}
		};
		cancelField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				connection.sendMessage(CANCEL);
			}
		});

		titleManager.add(cancelField);
		// backField.setEditable(false);
		// cancelField.setEditable(false);
		setTitle(titleManager);
	}

	private void displayResult(Result result) {

		

		String cookie = result.getCookie();
		if (cookie != null) {
			Cookie cookieObj = new Cookie();
			cookieObj.setCookie(cookie);
			DataStore.getInstance().saveData(cookieObj);
		}

		manager.deleteAll();
		titleManager.deleteAll();
		hManager.deleteAll();

		if (result.isShowBack()) {
			backField.setEditable(result.isShowBack());
			titleManager.add(backField);
		} else {
			// backField.setEditable(false);
			backField.setEditable(false);
			titleManager.add(new LabelField());

		}

		if (result.getTitle() != null) {
			titleField = new LabelField(result.getTitle(), FIELD_VCENTER);
			hManager.add(titleField);
		} else {
			titleField = new LabelField(ACCOUNTER, FIELD_VCENTER);
			hManager.add(titleField);
		}
		// for image position
		titleManager.add(hManager);

		if (!result.isHideCancel()) {
			cancelField.setEditable(!result.isHideCancel());
			titleManager.add(cancelField);
		} else {
			cancelField.setEditable(false);
			titleManager.add(new LabelField());

		}

		Vector resultParts = result.getResultParts();

		for (int i = 0; i < resultParts.size(); i++) {
			Object obj = resultParts.elementAt(i);
			if (obj instanceof String) {
				manager.add(new StringLabelField((String) obj));
			} else if (obj instanceof ResultList) {
				ResultListField resultListField = new ResultListField(
						(ResultList) obj, this);
				// resultListField.setSelectionListener(this);
				manager.add(resultListField);
			} else if (obj instanceof CommandList) {
				CommandListField commandListField = new CommandListField(
						(CommandList) obj);
				commandListField.setSelectionListener(this);
				manager.add(commandListField);
			} else if (obj instanceof InputType) {
				InputField inputField = new InputField((InputType) obj, this);
				// inputField.setSelectionListener(this);
				// inputField.setFocus();
				manager.add(inputField);
			}
		}

		invalidate();
	}

	public void connectionClosed(boolean fromRemote) {
//		if (fromRemote) {
			// Check if we are reconnecting very fast

			if (attempts > MAX_CONNECTION_ATTEMPTS) {
				attempts = 0;
				interval = 0;
				displayReconnectButton();
				// invalidate();

				return;
			}
			attempts++;
			if (interval == 0) {
				connection.connect();
				interval = 1;
			} else {
				startTimer(interval);
			}
			interval = interval + 1;

//		} else {
			displayReconnectButton();
//			// invalidate();
//
//		}
	}

	private void startTimer(int interval) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				connection.connect();

			}
		}, interval);

	}

	public void connectionEstablished() {

		interval = 0;
		attempts = 0;

		Locale locale = Locale.getDefault();
		String language = locale.getLanguage();

		this.connection.sendMessage(getSavedCookie() + " " + language);
	}

	private String getSavedCookie() {
		String cookie = DataStore.getInstance().getData().getCookie();
		return cookie;
	}

	public void messageReceived(Result result) {
		displayResult(result);
	}

	public void messageSent(String msg) {
		VirtualKeyboard virtualKeyboard = this.getVirtualKeyboard();
		if(virtualKeyboard != null){
		virtualKeyboard.setVisibility(VirtualKeyboard.HIDE);
		}
	}

	public void recordSelected(ResultList list, Record record) {
		displayImageWhileSendingToServer();
		this.connection.sendMessage(record.getCode());
	}

	public void commandSelected(CommandList list, Command command) {
		displayImageWhileSendingToServer();
		this.connection.sendMessage(command.getCode());
	}

	public void setAddress(String domain) {
		this.domain = domain;
		checkAndConnectIfNeeded();
	}

	public void inputSelected(String inputValue) {
		displayImageWhileSendingToServer();
		this.connection.sendMessage(inputValue);
	}

	public void close() {
		// if (backField.isEditable()) {
		// connection.sendMessage(BACK);
		// } else {
		connection = null;
		System.exit(1);
		close();
		// }
	}

	public boolean onClose() {
		connection = null;
		System.exit(1);
		close();
		return true;
	}

	MenuItem aboutMenuItem = new MenuItem("About", 10, 10) {
		public void run() {
			displayImageWhileSendingToServer();
			connection.sendMessage("about");

		}
	};

	// protected boolean keyDown(int keycode, int time) {
	//
	// if (Keypad.key(keycode) == Keypad.KEY_ESCAPE) {
	// if (backField.isEditable()) {
	// connection.sendMessage(BACK);
	// } else {
	// connection = null;
	// System.exit(1);
	// close();
	// }
	// }
	// return true;
	//
	// };

	// protected boolean keyChar(char c, int status, int time) {
	// if (c == Characters.ESCAPE) {
	// if (backField.isEditable()) {
	// connection.sendMessage(BACK);
	// return true;
	// } else {
	// connection = null;
	// System.exit(1);
	// close();
	// }
	// }else if(c == Characters.ENTER){
	// return true;
	// }
	// return true;
	// }
	
	protected boolean keyChar(char character, int status, int time) {
		if (Characters.ESCAPE == character) {
			if(backField.isEditable())
			{
				connection.sendMessage(BACK);
			}else{
				System.exit(1);
				close();
			}
		    return true;
		}
		return super.keyChar(character, status, time);
		}
	/**
	 * for displaying the Reconnect button, when connection is failed for First
	 * time and when MAX_CONNECTION_ATTEMPTS exceeds
	 */

	public void displayReconnectButton() {

		manager.deleteAll();
		statusLabelField = new LabelField("Connection failed");

		reconnectButton = new ButtonField("Reconnect",
				ButtonField.CONSUME_CLICK | ButtonField.FOCUSABLE) {

			public int getPreferredHeight() {
				return 25;
			}

			protected void paint(Graphics graphics) {

				graphics.drawText("Reconnect", 5, 5);
			}

			public int getPreferredWidth() {
				return Display.getWidth() - 42;
			}

			protected void layout(int width, int height) {
				super.layout(Display.getWidth() - 42, 25);
			}

		};

		reconnectButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				manager.deleteAll();
				statusLabelField = new LabelField("Connecting to server");
				manager.add(statusLabelField);
				connection.connect();

			}
		});
		manager.add(statusLabelField);
		manager.add(reconnectButton);
	}

	protected void sizeChanged(int w, int h) {
		
		
		if(w> h){
			net.rim.device.api.ui.Ui.getUiEngineInstance()
			.setAcceptableDirections(Display.DIRECTION_LANDSCAPE);
		}else{
			net.rim.device.api.ui.Ui.getUiEngineInstance()
			.setAcceptableDirections(Display.DIRECTION_PORTRAIT);
		}
	}

	/**
	 * this method is used to display only 'Accounter' in TitleManagar
	 */
	private void displayTitleManagarWhileConnectingToServerAtAppLanchTime() {
		titleManager.add(new LabelField());
		titleManager.add(hManager);
		titleManager.add(new LabelField());
	}

	private void instansiateLoadingImage() {
		EncodedImage encodedImage = GIFEncodedImage
				.getEncodedImageResource("images/loadingImage.gif");
		GIFEncodedImage image = (GIFEncodedImage) encodedImage;

		animatedGIF = new AnimatedGIFField(image, FIELD_HCENTER);
		animatedGIF.setPadding(2, 2, 2, 2);

	}

	private void displayImageWhileSendingToServer() {

		hManager.deleteAll();
		hManager.add(animatedGIF);
		hManager.add(titleField);

	}
}
