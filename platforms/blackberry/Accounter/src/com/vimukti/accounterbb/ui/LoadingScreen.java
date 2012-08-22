//package com.vimukti.accounterbb.ui;
//
//import net.rim.device.api.system.EncodedImage;
//import net.rim.device.api.system.GIFEncodedImage;
//import net.rim.device.api.ui.Screen;
//import net.rim.device.api.ui.UiApplication;
//import net.rim.device.api.ui.component.LabelField;
//import net.rim.device.api.ui.container.FullScreen;
//import net.rim.device.api.ui.container.PopupScreen;
//import net.rim.device.api.ui.container.VerticalFieldManager;
//
//import com.vimukti.accounterbb.core.AccounterConnection;
//import com.vimukti.accounterbb.core.ConnectionListener;
//import com.vimukti.accounterbb.result.Result;
//import com.vimukti.accounterbb.utils.AnimatedGIFField;
//
//public class LoadingScreen extends PopupScreen {
//	public ConnectionListener connectionListener;
//	private AccounterConnection accounterConnection;
//	LabelField labelField = new LabelField("Connecting to server...");
//	VerticalFieldManager manager = new VerticalFieldManager(USE_ALL_WIDTH);
//
//	public LoadingScreen() {
//
//		super(new VerticalFieldManager(USE_ALL_WIDTH));
//
//		EncodedImage encodedImage = GIFEncodedImage
//				.getEncodedImageResource("images/animatedGIF.gif");
//		GIFEncodedImage image = (GIFEncodedImage) encodedImage;
//
//		AnimatedGIFField animatedGIF = new AnimatedGIFField(image,
//				FIELD_HCENTER);
//		animatedGIF.setPadding(25, 25, 25, 25);
//
//		manager.add(animatedGIF);
//		manager.add(labelField);
//		add(manager);
//
//	}
//
//	protected void onDisplay() {
//
//		getConnectListener();
//		super.onDisplay();
//	}
//
//	public boolean onClose() {
//		accounterConnection = null;
//		System.exit(1);
//		close();
//		return true;
//	}
//
//	private void getConnectListener() {
//
//		if (connectionListener == null) {
//			connectionListener = new ConnectionListener() {
//
//				public void messageSent(String commandMessage) {
//				}
//
//				public void messageReceived(final Result result) {
//
//					UiApplication.getUiApplication().invokeAndWait(
//							new Runnable() {
//
//								public void run() {
//									Screen activeScreen = UiApplication
//											.getUiApplication()
//											.getActiveScreen();
//									if (activeScreen != null) {
//										if (activeScreen.getClass() == LoadingScreen.class) {
//											UiApplication.getUiApplication()
//													.popScreen(activeScreen);
//										}
//
//										if (activeScreen.getClass() == ChatScreen.class) {
//											UiApplication.getUiApplication()
//													.popScreen(activeScreen);
//										}
//										if (activeScreen.getClass() == FullScreen.class) {
//											UiApplication.getUiApplication()
//													.popScreen(activeScreen);
//										}
//										if (activeScreen.getClass() == DisplayPopUPScreen.class) {
//											UiApplication.getUiApplication()
//													.popScreen(activeScreen);
//										}
//									}
//									UiApplication.getUiApplication()
//											.invokeAndWait(new Runnable() {
//
//												public void run() {
//													UiApplication
//															.getUiApplication()
//															.pushScreen(
//																	new ChatScreen(
//																			result,
//																			accounterConnection));
//
//												}
//											});
//
//								}
//							});
//				}
//
//				public void errorMessage(final String errorMessage) {
//					UiApplication.getUiApplication().invokeAndWait(
//							new Runnable() {
//
//								public void run() {
//									labelField.setText(errorMessage);
//
//								}
//							});
//
//				}
//
//				public void connectionEstablished(String successMessage) {
//				}
//
//				public void connectionClosed(String closeMessage) {
//				}
//			};
//		}
//
//		accounterConnection = new AccounterConnection(connectionListener);
//		accounterConnection.start();
//
//	}
//
// }
