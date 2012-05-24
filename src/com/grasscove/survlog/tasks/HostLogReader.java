package com.grasscove.survlog.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.grasscove.survlog.ConnectionManager;
import com.grasscove.survlog.provider.model.Host;
import com.grasscove.survlog.provider.model.Log;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class HostLogReader extends AsyncTask<Object, Object, Object> {

	private Session session;
	private Connection conn;
	private String path;
	private ArrayList<String> lines = new ArrayList<String>();

	public HostLogReader(Connection c) {
		conn = c;
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Object... args) {
		try {
			path = (String) args[0];

			conn.connect();

			//if (!conn.authenticateWithPassword(host.getUsername(), host.getPassword())) {
			if (!conn.authenticateWithPassword("jason", "j4s0nr0cks")) {
				throw new IOException("Authentication failed.");
			}

			session = conn.openSession();
			session.execCommand("tail -f "+ path);
			InputStream stdout = new StreamGobbler(session.getStdout());

			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			String line;
			while ((line = br.readLine()) != null) {
				
				//ConnectionManager.getInstance().onUpdate(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		if(session != null) session.close();
		if(conn != null) conn.close();
	}
}