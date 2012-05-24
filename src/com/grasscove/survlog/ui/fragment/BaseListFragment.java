package com.grasscove.survlog.ui.fragment;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.ui.UIUtils;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

public class BaseListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static final int HOSTS = 0;
	public static final int LOGS = 1;
	public static final int HOST_LOGS = 2;
	public static final int LOG = 3;

	protected ImageButton mRefreshButton = null;
    protected ProgressBar mRefreshProgress = null;

	protected ListAdapter mAdapter;

	public BaseListFragment() {
		final Bundle args = new Bundle();
		args.putInt("type", HOSTS);
		setArguments(args);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setAdapter(mAdapter);

		if(!UIUtils.isTablet) {
			//getListView().setEmptyView(getActivity().findViewById(R.id.add_new_host_button));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		setListShown(false);
		switch(id) {
			default:
			case HOSTS:
				return new CursorLoader(getActivity(), Hosts.URI, null, null, null, null);
			case HOST_LOGS:
				long hostId = args.getLong(Logs.Columns.HOST);
				Uri uri = Logs.buildHostLogsUri(hostId);
				return new CursorLoader(getActivity(), uri, null, null, null, null);
			case LOGS:
				return new CursorLoader(getActivity(), Logs.URI, null, null, null, null);
			case LOG:
				return new CursorLoader(getActivity(),
					Logs.buildLogContentUri(args.getLong(Logs.Columns._ID)),
					null, null, null, null
				);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> l, Cursor c) {
		((SimpleCursorAdapter) mAdapter).swapCursor(c);
		setListShown(true);

		if(mRefreshButton != null && mRefreshButton.getVisibility() == View.GONE) {
			mRefreshButton.setVisibility(View.VISIBLE);
		}
		if(mRefreshProgress != null && mRefreshProgress.getVisibility() == View.VISIBLE) {
			mRefreshProgress.setVisibility(View.GONE);
		}		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> l) {
		((SimpleCursorAdapter) mAdapter).swapCursor(null);
	}

	/** Update the Titlebar's refresh icon based on the status of the API call **/
    /*protected void updateRefreshingStatus(boolean status) {
        if(mRefreshButton == null) {
            mRefreshButton = (ImageButton) getActivity().findViewById(R.id.title_refresh_button);
        }
        if(mRefreshButton != null) mRefreshButton.setVisibility(status ? View.GONE : View.VISIBLE);

        if(mRefreshProgress == null) {
            mRefreshProgress = (ProgressBar) getActivity().findViewById(R.id.title_refresh_progress);
        }
        if(mRefreshProgress != null)  mRefreshProgress.setVisibility(status ? View.VISIBLE : View.GONE);
    }*/
}