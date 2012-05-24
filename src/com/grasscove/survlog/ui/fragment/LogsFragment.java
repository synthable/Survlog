package com.grasscove.survlog.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.ui.phone.LogActivity;

public class LogsFragment extends BaseListFragment {

	private ImageButton mDeleteButton;
	
	private long mHostId;
	
	

	public static final LogsFragment newInstance(long host) {
		LogsFragment f = new LogsFragment();
		Bundle a = new Bundle();
		a.putLong(Logs.Columns.HOST, host);
		f.setArguments(a);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHostId = getArguments().getLong(Logs.Columns.HOST);

		mAdapter = new SimpleCursorAdapter(
			getActivity(),
			android.R.layout.simple_list_item_2,
			null,
			new String[] { Logs.Columns.TITLE, Logs.Columns.PATH },
			new int[] { android.R.id.text1, android.R.id.text2 },
		    0
		);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mDeleteButton = (ImageButton) getActivity().findViewById(R.id.title_delete_log_button);
		
		registerForContextMenu(getListView());

		getListView().setBackgroundColor(Color.TRANSPARENT);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		Bundle a = new Bundle();
		a.putLong(Logs.Columns.HOST, mHostId);
		getLoaderManager().initLoader(HOST_LOGS, a, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Uri uri = Logs.buildLogUri(id);
		Cursor c = getActivity().managedQuery(uri, null, null, null, null);
		c.moveToFirst();
		Log log = new Log(c);
		c.close();

		startActivity(new Intent(
			Intent.ACTION_VIEW,
			Logs.buildHostLogUri(log.getHost(), id),
			getActivity(),
			LogActivity.class
		));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.log_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//mDeleteButton.setVisibility(View.GONE);

		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Uri uri = Logs.buildLogUri(info.id);
		switch(item.getItemId()) {
			default:
			case R.id.menu_edit:
				startActivity(new Intent(
					Intent.ACTION_EDIT,
					uri,
					getActivity(),
					LogActivity.class
				));
				break;
			case R.id.menu_delete:
				getActivity().getContentResolver().delete(uri, null, null);
				break;
		}

		return super.onContextItemSelected(item);
	}
}