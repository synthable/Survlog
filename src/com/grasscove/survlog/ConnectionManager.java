package com.grasscove.survlog;

import java.io.IOException;
import java.util.ArrayList;

import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.ui.phone.LogActivity;
import com.trilead.ssh2.Connection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ConnectionManager {

	public interface LogReader {
		public void onUpdate(String line);
		/*public void onBind();
		public void onUnbind();*/
	}

	protected static ConnectionManager instance;

	public boolean BOUND = false;
	public static final int CONNECTION_LIMIT = 1;

	protected Context context;
	public SurvlogService service;

	public static ConnectionManager getInstance() {
		if(instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	public ConnectionManager() {
	}

	public void onUpdate(String line) {
		((LogActivity) context).onUpdate(line);
	}

	public void connect(Host host, Log log) {
		if(BOUND) {
			try {
				service.connectAndOpenLog(host, log);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public SurvlogService getService() {
		return service;
	}

	public void setService(SurvlogService service) {
		this.service = service;
	}
}