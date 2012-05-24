package com.grasscove.survlog;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.grasscove.survlog.tasks.HostLogReader;
import com.trilead.ssh2.Connection;

public class SurvlogService extends Service {

	private ArrayList<Connection> mConnections = new ArrayList<Connection>();

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
		public SurvlogService getService() {
            return SurvlogService.this;
        }
    }

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void connectAndOpenLog(Host host, Log log) throws IOException {
		if(mConnections.size() < ConnectionManager.CONNECTION_LIMIT) {
			//mConnections.add(conn);
			//new HostLogReader(conn).execute(host, log);
		}
	}

	private class LogConnection {
		private Connection connection;
		private Host host;
		private Log log;
		private HostLogReader reader;

		public void LogConnection(Host host, Log log) {
			this.host = host;
			this.log = log;

			connection = new Connection(host.getHost(), host.getPort());
			reader = (HostLogReader) new HostLogReader(connection).execute(host, log);
		}
	}
}
