package com.grasscove.survlog.ui.tablet;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.model.Host;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class HostDialogFragment extends DialogFragment implements View.OnClickListener {

	public static HostDialogFragment newInstance() {
        return new HostDialogFragment();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setContentView(R.layout.host_dialog);
		getDialog().setTitle("Add new host");

		final float scale = getResources().getDisplayMetrics().density;
		final int width = (int) (400 * scale + 0.5f);

		getDialog().getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);

		final Button save = (Button)getDialog().findViewById(R.id.host_dialog_save_button);
		save.setOnClickListener(this);

		final Button cancel = (Button)getDialog().findViewById(R.id.host_dialog_cancel_button);
		cancel.setOnClickListener(this);

		return null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.host_dialog_save_button:
				final EditText title = (EditText) getDialog().findViewById(R.id.host_dialog_title_edit);
				final EditText h = (EditText) getDialog().findViewById(R.id.host_dialog_host_edit);
				final EditText username = (EditText) getDialog().findViewById(R.id.host_dialog_username_edit);
				final EditText password = (EditText) getDialog().findViewById(R.id.host_dialog_password_edit);
				final EditText port = (EditText) getDialog().findViewById(R.id.host_dialog_port_edit);

				final Host host = new Host();
				host.setTitle(title.getText().toString());
				host.setHost(h.getText().toString());
				host.setUsername(username.getText().toString());
				host.setPassword(password.getText().toString());
				host.setPort(Integer.valueOf(port.getText().toString()));

				getActivity().getContentResolver().insert(Hosts.URI, host.getValues());
				break;
			case R.id.host_dialog_cancel_button:
				break;
		}

		dismiss();
	}
}