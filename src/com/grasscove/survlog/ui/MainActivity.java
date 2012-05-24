package com.grasscove.survlog.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;

import com.grasscove.survlog.R;
import com.grasscove.survlog.R.id;
import com.grasscove.survlog.R.layout;
import com.grasscove.survlog.SurvlogApp;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.ui.fragment.HostsFragment;
import com.grasscove.survlog.ui.fragment.LogsFragment;
import com.grasscove.survlog.ui.fragment.HostsFragment.HostsFragmentCallback;
import com.grasscove.survlog.ui.phone.HostActivity;
import com.grasscove.survlog.ui.phone.LogActivity;
import com.grasscove.survlog.ui.tablet.HostDialogFragment;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class MainActivity extends FragmentActivity implements View.OnClickListener, HostsFragmentCallback {

    private EditText mHostView;
    private EditText mUserView;
    private EditText mPasswordView;

    private Session mSession;
    private Connection mConn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Remove the title if this isn't a tablet device **/
        if(!UIUtils.isTablet) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setContentView(R.layout.home);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		final HostsFragment f = new HostsFragment();
		ft.replace(R.id.hosts_fragment, f);
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    ft.commit();
    }

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.title_add_log_button:
				if(UIUtils.isTablet) {
					/*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			        ft.addToBackStack(null);

			        HostDialogFragment f = HostDialogFragment.newInstance();
			        f.show(ft, "dialog");*/
				} else {
					startActivity(new Intent(
						Intent.ACTION_INSERT,
						null,
						this,
						LogActivity.class
					));
				}
				break;
			case R.id.title_add_host_button:
			case R.id.add_new_host_button:
				if(UIUtils.isTablet) {
					final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			        ft.addToBackStack(null);
			        final HostDialogFragment f = HostDialogFragment.newInstance();
			        f.show(ft, "dialog");
				} else {
					startActivity(new Intent(
						Intent.ACTION_INSERT,
						null,
						this,
						HostActivity.class
					));
				}
				break;
		}
	}

	@Override
	public void onHostClick(long id) {
		if(UIUtils.isTablet) {
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        ft.addToBackStack(null);
	        final LogsFragment f = LogsFragment.newInstance(id);
	        ft.replace(R.id.hosts_fragment, f);
		} else {
			startActivity(new Intent(Intent.ACTION_PICK,
				Logs.buildHostLogsUri(id),
				this,
				LogActivity.class
			));
		}
	}
}