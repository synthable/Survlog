package com.grasscove.survlog.ui.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.LogContents;
import com.grasscove.survlog.provider.SurvlogContract.Logs;
import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.ui.phone.LogActivity;

public class LiveLogFragment extends BaseListFragment implements View.OnClickListener {

	private Log mLog;
	private Host mHost;
	private Dialog mLoginDialog;

	public static final LiveLogFragment newInstance() {
		return new LiveLogFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = new SimpleCursorAdapter(
			getActivity(),
			android.R.layout.simple_list_item_1,
			null,
			new String[] { LogContents.Columns.LINE },
			new int[] { android.R.id.text1 },
			0
		);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Long id = getArguments().getLong(Logs.Columns._ID);

		Bundle a = new Bundle();
		a.putLong(Logs.Columns._ID, id);
		getLoaderManager().initLoader(LOG, a, this);

		Cursor c = getActivity().managedQuery(Logs.buildLogUri(id), null, null, null, null);
		c.moveToFirst();
		mLog = new Log(c);
		c.close();

		c = getActivity().managedQuery(Hosts.buildHostUri(id), null, null, null, null);
		c.moveToFirst();
		mHost = new Host(c);
		c.close();

		if(mHost.getPassword() == null || mHost.getPassword().length() == 0) {
			/*
			FragmentTransaction ft = getFragmentManager().beginTransaction();
		    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		    if (prev != null) {
		        ft.remove(prev);
		    }
		    ft.addToBackStack(null);
		    */

		    // Create and show the dialog.
		    //DialogFragment newFragment = MyDialogFragment.newInstance(1);
		    //newFragment.show(ft, "dialog");

		    mLoginDialog = new Dialog(getActivity());
		    mLoginDialog.setContentView(R.layout.fragment_login);
		    ((Button)mLoginDialog.findViewById(R.id.save_button)).setText(android.R.string.ok);
		    mLoginDialog.show();
		} else {
			
		}

		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
	}

	public void appendListData(String data) {
		((ArrayAdapter) mAdapter).add(data);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.save_button:
				CheckBox box = (CheckBox)mLoginDialog.findViewById(R.id.host_login_save_password_input);
				if(box.isChecked()) {
					EditText pass = (EditText)mLoginDialog.findViewById(R.id.host_login_save_password_input);
					mHost.setPassword(pass.getText().toString());
					//final Uri uri = Hosts.buildHostUri(mHost.);
					//getActivity().getContentResolver().update(Hosts.URI, mHost.getValues(), null, null);
				}
				
				break;
			case R.id.cancel_button:
				break;
			default:
				break;
		}
	}
	
	public static class MyDialogFragment extends DialogFragment {
	    int mNum;

	    static MyDialogFragment newInstance(int num) {
	        MyDialogFragment f = new MyDialogFragment();

	        f.setCancelable(true);

	        // Supply num input as an argument.
	        Bundle args = new Bundle();
	        args.putInt("num", num);
	        f.setArguments(args);

	        return f;
	    }

	    static MyDialogFragment newInstance(String username, String password) {
	        MyDialogFragment f = new MyDialogFragment();

	        Bundle args = new Bundle();
	        args.putString(Hosts.Columns.USER, username);
	        args.putString(Hosts.Columns.PASSWORD, password);
	        f.setArguments(args);

	        f.setCancelable(true);

	        return f;
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mNum = getArguments().getInt("num");

	        // Pick a style based on the num.
//	        int style = DialogFragment.STYLE_NORMAL, theme = 0;
//	        switch ((mNum-1)%6) {
//	            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
//	            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
//	            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
//	            case 4: style = DialogFragment.STYLE_NORMAL; break;
//	            case 5: style = DialogFragment.STYLE_NORMAL; break;
//	            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
//	            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
//	            case 8: style = DialogFragment.STYLE_NORMAL; break;
//	        }
//	        switch ((mNum-1)%6) {
//	            case 4: theme = android.R.style.Theme_Holo; break;
//	            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
//	            case 6: theme = android.R.style.Theme_Holo_Light; break;
//	            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
//	            case 8: theme = android.R.style.Theme_Holo_Light; break;
//	        }
//	        setStyle(style, theme);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.fragment_login, container, false);
	        /*View tv = v.findViewById(R.id.text);
	        ((TextView)tv).setText("Dialog #" + mNum + ": using style "+ getNameForNum(mNum));

	        // Watch for button clicks.
	        Button button = (Button)v.findViewById(R.id.show);
	        button.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // When button is clicked, call up to owning activity.
	                ((FragmentDialog)getActivity()).showDialog();
	            }
	        });*/

	        return v;
	    }
	}
}