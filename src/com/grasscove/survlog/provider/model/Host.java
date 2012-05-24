package com.grasscove.survlog.provider.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.grasscove.survlog.provider.SurvlogContract.Hosts;

public class Host extends BaseModel {

	private long id;
	private String title;
	private String host;
	private String username;
	private String password;
	private int port;

	public Host() {
	}

	public Host(Cursor c) {
		/** move the cursor to the first position if available **/
		if(c.getPosition() == 0 && c.getCount() != 0) {
			//c.moveToFirst();
		}

		int index = c.getColumnIndex(Hosts.Columns._ID);
		this.id = !c.isNull(index) ? c.getLong(index) : null;

		index = c.getColumnIndex(Hosts.Columns.TITLE);
		this.title = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Hosts.Columns.HOST);
		this.host = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Hosts.Columns.USER);
		this.username = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Hosts.Columns.PASSWORD);
		this.password = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Hosts.Columns.PORT);
		this.port = !c.isNull(index) ? c.getInt(index) : null;
	}

	public ContentValues getValues() {
		ContentValues cv = new ContentValues();
		cv.put(Hosts.Columns.TITLE, this.title);
		cv.put(Hosts.Columns.HOST, this.host);
		cv.put(Hosts.Columns.USER, this.username);
		cv.put(Hosts.Columns.PASSWORD, this.password);
		cv.put(Hosts.Columns.PORT, this.port);
		return cv;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}
