package com.vimukti.accounter.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.vimukti.accounter.android.ui.UIDesigner;

public class AccounterActivity extends Activity {
	private UIDesigner designer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		LinearLayout resultView = new LinearLayout(this);
		resultView.setPadding(2, 2, 2, 2);
		resultView.setOrientation(LinearLayout.VERTICAL);
		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
		scrollView.addView(resultView);
		createConnection(resultView, "www.accounterlive.com");
	}

	protected void createConnection(LinearLayout resultView, String host) {

		designer = new UIDesigner(AccounterActivity.this, resultView);
		designer.connecting();
		AccounterConnection connection = new AccounterConnection(designer,
				this, host);
		designer.setConnection(connection);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setContentView(R.layout.main);
		super.onConfigurationChanged(newConfig);
		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
		LinearLayout layout = designer.getLayout();
		((ScrollView) layout.getParent()).removeView(layout);
		scrollView.addView(layout);

		designer.reDraw();
	}

	@Override
	public void onBackPressed() {
		if (designer.hasBack()) {
			designer.send("Back");
		} else {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("About");
		menu.add("Close");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("About")) {
			designer.send("About");
		} else if (item.getTitle().equals("Close")) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}