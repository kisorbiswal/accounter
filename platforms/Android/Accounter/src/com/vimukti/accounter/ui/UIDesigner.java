package com.vimukti.accounter.ui;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout.LayoutParams;

import com.vimukti.accounter.AccounterConnection;
import com.vimukti.accounter.ConnectionListener;
import com.vimukti.accounter.R;
import com.vimukti.accounter.result.Cell;
import com.vimukti.accounter.result.Command;
import com.vimukti.accounter.result.CommandList;
import com.vimukti.accounter.result.InputType;
import com.vimukti.accounter.result.Record;
import com.vimukti.accounter.result.Result;
import com.vimukti.accounter.result.ResultList;

public class UIDesigner implements ConnectionListener {
	private static int MAX_CONNECTION_ATTEMPTS = 20;

	private Activity activity;
	private LinearLayout layout;
	private AccounterConnection connection;
	private EditText editText;
	private boolean hasBack;
	private int state;
	private Result result;

	public UIDesigner(Activity activity, LinearLayout layout) {
		this.activity = activity;
		this.layout = layout;
		TextView title = (TextView) UIDesigner.this.activity
				.findViewById(R.id.textView1);
		title.setTextColor(Color.BLACK);

		Button cancel = (Button) UIDesigner.this.activity
				.findViewById(R.id.button2);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send("cancel");
			}
		});

		Button back = (Button) UIDesigner.this.activity
				.findViewById(R.id.button1);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send("back");
			}
		});
	}

	private boolean isSending;

	public void send(String string) {
		if (string.length() == 0 && isSending) {
			return;
		}
		isSending = true;
		hasBack = false;
		ImageView imageView = (ImageView) activity
				.findViewById(R.id.imageView1);
		imageView.setVisibility(View.VISIBLE);
		if (editText != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
		connection.sendMessage(string);
	}

	private void addRetryButton() {
		state = FAIL;
		Button button = new Button(activity);
		button.setText("Reconnect");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connection.connect();
			}
		});
		layout.addView(button);
	}

	private void addResult(Result fromJson) {
		state = RESULT;
		result = fromJson;
		String cookie = fromJson.getCookie();
		if (cookie != null) {
			storeCookie(cookie);
		}
		clearView();
		editText = null;
		TextView title = (TextView) activity.findViewById(R.id.textView1);
		if (fromJson.getTitle() != null) {
			title.setText(fromJson.getTitle());
		}

		Button cancel = (Button) activity.findViewById(R.id.button2);
		if (fromJson.isHideCancel()) {
			cancel.setVisibility(4);
		} else {
			cancel.setVisibility(0);
		}

		Button back = (Button) activity.findViewById(R.id.button1);
		hasBack = fromJson.isShowBack();
		if (fromJson.isShowBack()) {
			back.setVisibility(0);
		} else {
			back.setVisibility(4);
		}

		List<Object> resultParts = fromJson.getResultParts();
		for (Object object : resultParts) {
			if (object instanceof String) {
				addString((String) object);
			} else if (object instanceof ResultList) {
				addResultList((ResultList) object);
			} else if (object instanceof CommandList) {
				addCommandList((CommandList) object);
			} else if (object instanceof InputType) {
				addInput((InputType) object);
			}
		}

		ImageView imageView = (ImageView) activity
				.findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);
	}

	private void addInput(InputType object) {
		int type = object.getType();
		if (type == InputType.INPUT_TYPE_NONE) {
			return;
		}
		if (type == InputType.INPUT_TYPE_DATE) {
			Date date;
			String value = object.getValue();
			if (value.length() == 0) {
				date = new Date();
			} else {
				date = new Date(Integer.parseInt(value.substring(0, 4)) - 1900,
						Integer.parseInt(value.substring(4, 6)) - 1, Integer
								.parseInt(value.substring(6, 8)));
			}
			final DatePicker datePicker = new DatePicker(activity);
			datePicker.init(1900 + date.getYear(), date.getMonth(), date
					.getDate(), new OnDateChangedListener() {

				@Override
				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {

				}
			});

			Button button = new Button(activity);
			button.setText("Set Date");
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int year = datePicker.getYear();
					int month = datePicker.getMonth() + 1;
					int day = datePicker.getDayOfMonth();
					long time = (year * 10000) + (month * 100) + day;
					send(String.valueOf(time));
				}
			});
			layout.addView(datePicker);
			layout.addView(button);
			return;
		}
		editText = new EditText(activity);
		editText.setText(object.getValue());
		editText.setSelectAllOnFocus(true);
		editText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					send(editText.getText().toString());
					editText.setText("");
					return true;
				}
				return false;
			}
		});
		layout.addView(editText);
		if (type == InputType.INPUT_TYPE_NUMBER) {
			editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		} else if (type == InputType.INPUT_TYPE_AMOUNT) {
			editText
					.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
							| android.text.InputType.TYPE_CLASS_NUMBER);
		} else if (type == InputType.INPUT_TYPE_EMAIL) {
			editText
					.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else if (type == InputType.INPUT_TYPE_PASSWORD) {
			editText.setInputType(editText.getInputType()
					| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| EditorInfo.TYPE_TEXT_VARIATION_FILTER);
			editText.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		} else if (type == InputType.INPUT_TYPE_PHONE) {
			editText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
		} else if (type == InputType.INPUT_TYPE_STRING) {
			editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
		} else if (type == InputType.INPUT_TYPE_URL) {
			editText
					.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);
		}
		editText.requestFocus();
		if (type != InputType.INPUT_TYPE_PASSWORD) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, 0);
		}
	}

	private void addResultList(ResultList object) {

		View spacer = new View(activity);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 10));
		spacer.setBackgroundColor(Color.WHITE);
		layout.addView(spacer);
		String title = object.getTitle();
		if (title != null) {
			View lable = getLable(title + ":");
			lable.setPadding(5, 0, 0, 0);
			layout.addView(lable);
		}
		List<Record> records = object.getRecords();
		TableLayout table = new TableLayout(activity);
		int layWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		layWidth -= 35;
		for (final Record record : records) {
			LinearLayout tableRow = new LinearLayout(activity);
			tableRow.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout innerRow = new LinearLayout(activity);
			innerRow.setOrientation(LinearLayout.VERTICAL);

			List<Cell> cells = record.getCells();

			for (Cell cell : cells) {
				LinearLayout cellLayout = new LinearLayout(activity);
				cellLayout.setOrientation(LinearLayout.HORIZONTAL);
				int tw = layWidth / 2;
				int vw = layWidth / 2;
				if (cell.getTitle().length() == 0) {
					vw = layWidth;
				}
				if (cell.getValue().length() == 0) {
					tw = layWidth;
				}
				if (cell.getTitle().length() != 0) {
					TextView textView = createCell(cell.getTitle());
					textView.setWidth(tw);
					cellLayout.addView(textView);
				}
				if (cell.getValue().length() != 0) {
					TextView textView2 = createCell(cell.getValue());
					if (cell.getTitle().length() != 0) {
						textView2.setGravity(Gravity.RIGHT);
					}
					textView2.setWidth(vw);
					cellLayout.addView(textView2);
				}
				cellLayout.setGravity(Gravity.CENTER_VERTICAL);
				innerRow.addView(cellLayout);
			}

			tableRow.addView(innerRow);

			ImageView view = creatRowImage();
			tableRow.addView(view);

			tableRow.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

			tableRow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String code = record.getCode();
					send(code);
				}
			});
			tableRow.setMinimumHeight(60);
			tableRow.setBackgroundResource(R.drawable.row);
			tableRow.setGravity(Gravity.CENTER_VERTICAL);
			tableRow.setClickable(true);
			tableRow.setFocusable(true);

			table.addView(tableRow);
			table.addView(createSpacer());
		}
		table.setClickable(true);
		table.removeViewAt(table.getChildCount() - 1);
		table.setBackgroundResource(R.drawable.table);
		layout.addView(table);
	}

	private ImageView creatRowImage() {
		ImageView view = new ImageView(activity);
		view.setImageResource(R.drawable.right);
		return view;
	}

	private TextView createCell(String txt) {
		TextView text = new TextView(activity);
		text.setText(txt);
		text.setTextColor(Color.BLACK);
		text.setPadding(2, 0, 4, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);
		return text;
	}

	private View createSpacer() {
		View spacer = new View(activity);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
		// spacer.setBackgroundResource(R.drawable.space);
		spacer.setBackgroundColor(Color.parseColor("#B2C8F8"));

		return spacer;
	}

	private void addCommandList(final CommandList object) {

		View spacer = new View(activity);
		spacer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 10));
		spacer.setBackgroundColor(Color.WHITE);
		layout.addView(spacer);

		for (final Command command : object.getCommandNames()) {
			Button button = new Button(activity);
			button.setText(command.getName());
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					send(command.getCode());
				}
			});
			// button.setBackgroundResource(R.drawable.button);
			button.setTextColor(Color.BLACK);
			// button.setTextSize(BUTTON_TEXT_SIZE);
			// button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
			// BUTTON_TEXT_SIZE * 5));
			layout.addView(button);
		}

	}

	private void addString(String string) {
		layout.addView(getLable(string));
	}

	private View getLable(String string) {
		TextView view = new TextView(activity);
		view.setText(string);
		view.setPadding(5, 1, 5, 10);
		view.setTextColor(Color.BLACK);
		return view;
	}

	private void storeCookie(String cookie) {
		SharedPreferences myPrefs = activity.getSharedPreferences("settings",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor e = myPrefs.edit();
		e.putString("cookie", cookie);
		e.commit();
	}

	public void setConnection(AccounterConnection connection) {
		this.connection = connection;
	}

	public boolean hasBack() {
		return hasBack;
	}

	public LinearLayout getLayout() {
		return layout;
	}

	private void clearView() {
		isSending = false;
		layout.removeAllViews();
		Button cancel = (Button) activity.findViewById(R.id.button2);
		cancel.setVisibility(4);

		Button back = (Button) activity.findViewById(R.id.button1);
		back.setVisibility(4);
	}

	private String getCookie() {
		SharedPreferences myPrefs = activity.getSharedPreferences("settings",
				Activity.MODE_PRIVATE);
		String string = myPrefs.getString("cookie", "No Cookie");
		String displayLanguage = Locale.getDefault().getLanguage();
		String device = "Android " + Build.MODEL;
		return string + " " + displayLanguage + "," + device;
	}

	private int interval = 0;
	private int attempts = 0;

	private static final int RESULT = 0;

	private static final int FAIL = 1;

	@Override
	public void connectionClosed(boolean fromRemote) {
		ImageView imageView = (ImageView) activity
				.findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);

		clearView();
		layout.addView(getLable("Connection failed."));

		if (attempts > MAX_CONNECTION_ATTEMPTS) {
			attempts = 0;
			interval = 0;
			addRetryButton();
			return;
		}
		attempts++;
		if (interval == 0) {
			connecting();
			connection.connect();
			interval = 1;
		} else {
			startTimer(interval);
		}
		interval = interval + 1;
	}

	private void startTimer(int interval) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						connecting();
					}
				});
				connection.connect();
			}
		}, interval * 1000);
	}

	@Override
	public void connectionEstablished() {
		connection.sendMessage(getCookie());
	}

	@Override
	public void messageReceived(Result result) {
		ImageView imageView = (ImageView) activity
				.findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);

		addResult(result);
	}

	@Override
	public void messageSent(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connecting() {
		ImageView imageView = (ImageView) activity
				.findViewById(R.id.imageView1);
		imageView.setVisibility(View.VISIBLE);

		clearView();
		TextView createCell = createCell("Connecting to server...");
		layout.addView(createCell);
	}

	public void reDraw() {
		switch (state) {
		case RESULT:
			if (result != null) {
				messageReceived(result);
			}
			break;
		case FAIL:
			addRetryButton();
			break;
		default:
			break;
		}
		if (isSending) {
			ImageView imageView = (ImageView) activity
					.findViewById(R.id.imageView1);
			imageView.setVisibility(View.VISIBLE);
		}
	}
}
