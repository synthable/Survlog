package com.grasscove.survlog.ui.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.grasscove.survlog.ConnectionManager;
import com.grasscove.survlog.R;
import com.grasscove.survlog.SurvlogService;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.ui.UIUtils;
import com.grasscove.survlog.ui.fragment.LiveLogFragment;
import com.grasscove.survlog.ui.fragment.LogsFragment;

public class LogActivity extends FragmentActivity implements View.OnClickListener, ConnectionManager.LogReader {

	private Host mHost;
	private Log mLog;
	private long mHostId;
	private long mLogId;
	private boolean isEditing = false;
	private String mAction;

	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        	ConnectionManager.getInstance().BOUND = true;
        	ConnectionManager.getInstance().setService(
        		((SurvlogService.LocalBinder)service).getService()
        	);
        	onServiceBound();
        }
        public void onServiceDisconnected(ComponentName className) {
        	ConnectionManager.getInstance().BOUND = false;
        	ConnectionManager.getInstance().setService(null);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Remove the title if this isn't a tablet device **/
        if(!UIUtils.isTablet) {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        mAction = getIntent().getAction() != null ? getIntent().getAction() : Intent.ACTION_PICK;
		final Uri uri = getIntent().getData();

		mHostId = Long.parseLong(uri.getPathSegments().get(1));

		if(mAction.equals(Intent.ACTION_INSERT)) {
			setContentView(R.layout.fragment_new_log);
		} else if(mAction.equals(Intent.ACTION_EDIT)) {
			setContentView(R.layout.fragment_new_log);

			mLogId = Long.parseLong(uri.getPathSegments().get(1));
			Cursor c = managedQuery(Logs.buildLogUri(mLogId), null, null, null, null);
			c.moveToFirst();
			Log log = new Log(c);
			c.close();

			mHostId = log.getHost();

			final EditText title = (EditText) findViewById(R.id.new_log_title_input);
			final EditText path = (EditText) findViewById(R.id.new_log_path_input);
			final EditText lines = (EditText) findViewById(R.id.new_log_start_input);

			title.setText(log.getTitle());
			path.setText(log.getPath());
			lines.setText(String.valueOf(log.getLines()));

			isEditing = true;
		} else if(mAction.equals(Intent.ACTION_PICK)) {
			setContentView(R.layout.logs);

			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    		final LogsFragment f = LogsFragment.newInstance(mHostId);
    		ft.replace(R.id.logs_fragment, f);
		    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    ft.commit();
		} else if(mAction.equals(Intent.ACTION_VIEW)) {
			setContentView(R.layout.logs);

			/*long id = Long.parseLong(uri.getPathSegments().get(1));
			Cursor c = managedQuery(Hosts.buildHostUri(id), null, null, null, null);
			c.moveToFirst();
			mHost = new Host(c);
			c.close();*/

			Long id = Long.parseLong(uri.getLastPathSegment());
			Cursor c = managedQuery(Logs.buildLogUri(id), null, null, null, null);
			c.moveToFirst();
			mLog = new Log(c);
			c.close();

			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    		final LiveLogFragment f = LiveLogFragment.newInstance();
    		Bundle args = new Bundle();
    		args.putLong(Logs.Columns._ID, Long.parseLong(uri.getLastPathSegment()));
    		args.putLong(Hosts.Columns._ID, mLog.getHost());
    		f.setArguments(args);
    		ft.replace(R.id.logs_fragment, f);
		    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    ft.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(mAction.equals(Intent.ACTION_VIEW)) {
			bindService(new Intent(this, SurvlogService.class), mConnection, Context.BIND_AUTO_CREATE);
		} else if(mAction.equals(Intent.ACTION_PICK)) {
			
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(mAction.equals(Intent.ACTION_VIEW) && ConnectionManager.getInstance().BOUND) {
			unbindService(mConnection);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.title_add_log_button:
				startActivity(new Intent(
					Intent.ACTION_INSERT,
					Hosts.buildHostUri(mHostId),
					this,
					LogActivity.class
				));
				return;
			case R.id.save_button:
				final EditText title = (EditText) findViewById(R.id.new_log_title_input);
				final EditText path = (EditText) findViewById(R.id.new_log_path_input);
				final EditText start = (EditText) findViewById(R.id.new_log_start_input);

				final Log log = new Log();
				log.setTitle(title.getText().toString());
				log.setHost(mHostId);
				log.setPath(path.getText().toString());
				log.setLines(start.getText().toString());

				if(isEditing) {
					getContentResolver().update(Logs.buildLogUri(mLogId), log.getValues(), null, null);
				} else {
					getContentResolver().insert(Logs.URI, log.getValues());
				}
				break;
			case R.id.cancel_button:
			default:
				break;
		}
		finish();
	}

	public void onServiceBound() {
		ConnectionManager.getInstance().connect(mHost, mLog);
	}

	@Override
	public void onUpdate(String line) {
		final LiveLogFragment f = (LiveLogFragment) getSupportFragmentManager().findFragmentById(R.id.logs_fragment);
		f.appendListData(line);
	}
}