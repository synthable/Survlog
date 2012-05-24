package com.grasscove.survlog.ui.phone;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.ui.UIUtils;

public class HostActivity extends FragmentActivity implements View.OnClickListener {

	private long mHostId;
	private boolean isEditing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Remove the title if this isn't a tablet device **/
        if(UIUtils.isTablet) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

		String action;
		if(getIntent().getAction() != null) {
			 action = getIntent().getAction();
		} else {
			action = Intent.ACTION_INSERT;
		}

		final Uri uri = getIntent().getData();
		
		if(action.equals(Intent.ACTION_INSERT)) {
			setContentView(R.layout.fragment_new_host);
		} else if(action.equals(Intent.ACTION_EDIT)) {
			setContentView(R.layout.fragment_new_host);

			mHostId = Long.parseLong(uri.getPathSegments().get(1));
			Cursor c = managedQuery(Hosts.buildHostUri(mHostId), null, null, null, null);
			c.moveToFirst();
			Host host = new Host(c);
			c.close();

			final EditText title = (EditText) findViewById(R.id.new_host_title_input);
			final EditText hostInput = (EditText) findViewById(R.id.new_host_host_input);
			final EditText username = (EditText) findViewById(R.id.new_host_username_input);
			//final EditText password = (EditText) findViewById(R.id.new_log_title_input);
			final EditText port = (EditText) findViewById(R.id.new_host_port_input);

			title.setText(host.getTitle());
			hostInput.setText(host.getHost());
			username.setText(host.getUsername());
			port.setText(Integer.toString(host.getPort()));

			isEditing = true;
		} else {
			setContentView(R.layout.fragment_new_host);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.save_button:
				final EditText title = (EditText) findViewById(R.id.new_host_title_input);
				final EditText h = (EditText) findViewById(R.id.new_host_host_input);
				final EditText username = (EditText) findViewById(R.id.new_host_username_input);
				final EditText password = (EditText) findViewById(R.id.new_host_password_input);
				final EditText port = (EditText) findViewById(R.id.new_host_port_input);

				final Host host = new Host();
				host.setTitle(title.getText().toString());
				host.setHost(h.getText().toString());
				host.setUsername(username.getText().toString());
				if(password.getText().toString().length() != 0) {
					host.setPassword(password.getText().toString());
				}
				host.setPort(Integer.valueOf(port.getText().toString()));

				if(isEditing) {
					getContentResolver().update(Hosts.buildHostUri(mHostId), host.getValues(), null, null);
				} else {
					getContentResolver().insert(Hosts.URI, host.getValues());
				}
				break;
			case R.id.cancel_button:
				break;
			default:
				break;
		}

		finish();
	}
}