package com.grasscove.survlog.provider.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.grasscove.survlog.provider.SurvlogContract.Logs;

public class Log extends BaseModel {

	private String title;
	private long host;
	private String path;
	private int lines;

	public Log() {
	}

	public Log(Cursor c) {
		/** move the cursor to the first position if available **/
		if(c.getPosition() == 0 && c.getCount() != 0) {
			//c.moveToFirst();
		}

		int index = c.getColumnIndex(Logs.Columns.TITLE);
		this.title = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Logs.Columns.HOST);
		this.host = !c.isNull(index) ? c.getInt(index) : null;

		index = c.getColumnIndex(Logs.Columns.PATH);
		this.path = !c.isNull(index) ? c.getString(index) : null;

		index = c.getColumnIndex(Logs.Columns.LINES);
		this.lines = !c.isNull(index) ? c.getInt(index) : null;
	}

	public ContentValues getValues() {
		ContentValues cv = new ContentValues();
		cv.put(Logs.Columns.TITLE, this.title);
		cv.put(Logs.Columns.HOST, this.host);
		cv.put(Logs.Columns.PATH, this.path);
		cv.put(Logs.Columns.LINES, this.lines);
		return cv;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getHost() {
		return host;
	}

	public void setHost(long host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(String lines) {
		this.lines = Integer.valueOf(lines);
	}
}