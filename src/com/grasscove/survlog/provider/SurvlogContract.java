package com.grasscove.survlog.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class SurvlogContract {

	public static final String AUTHORITY = "com.grasscove.survlog.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY);

    public static final class Hosts {

        public static final Uri URI = Uri.parse(CONTENT_URI +"/hosts");

        public static final String TABLE = "hosts";

        public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.grasscove.survlog.host";
        public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.grasscove.survlog.host";

        public static final class Columns implements BaseColumns {
            public static final String TITLE = "title";
            public static final String HOST = "host";
            public static final String USER = "user";
            public static final String PASSWORD = "password";
            public static final String PORT = "port";
        }

        public static final String DEFAULT_SORT_ORDER = Columns.TITLE +" ASC";

        public static final String CREATE =
            "CREATE TABLE " + TABLE + "("
                + Columns._ID + " integer primary key autoincrement,"
                + Columns.TITLE + " text default '',"
                + Columns.HOST + " text default '',"
                + Columns.USER + " text default '',"
                + Columns.PASSWORD + " text default '',"
                + Columns.PORT + " integer default 22"
            + ");";

        public static final Uri buildHostUri(long id) {
        	return Uri.withAppendedPath(Hosts.URI, String.valueOf(id));
        }
    }

    public static final class Logs {

        public static final Uri URI = Uri.parse(CONTENT_URI +"/logs");

        public static final String TABLE = "logs";

        public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.grasscove.survlog.log";
        public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.grasscove.survlog.log";

        public static final class Columns implements BaseColumns {
            public static final String TITLE = "title";
            public static final String HOST = "host";
            public static final String PATH = "path";
            public static final String LINES = "lines";
        }

        public static final String DEFAULT_SORT_ORDER = Columns.TITLE +" ASC";

        public static final String CREATE =
            "CREATE TABLE " + TABLE + "("
                + Columns._ID + " integer primary key autoincrement,"
                + Columns.TITLE + " text default '',"
                + Columns.HOST + " integer,"
                + Columns.PATH + " text default '',"
                + Columns.LINES + " integer default 50"
            + ");";

        public static final Uri buildHostLogsUri(long id) {
        	return Uri.withAppendedPath(Hosts.buildHostUri(id), "logs");
        }

        public static final Uri buildHostLogUri(long host, long log) {
        	return Uri.withAppendedPath(Hosts.URI, String.valueOf(host))
        		.buildUpon()
        		.appendPath("logs")
        		.appendPath(String.valueOf(log))
        		.build();
        }

        public static final Uri buildLogUri(long id) {
        	return Uri.withAppendedPath(Logs.URI, String.valueOf(id));
        }

        public static final Uri buildLogContentUri(long id) {
        	return Uri.withAppendedPath(Logs.buildLogUri(id), "content");
        }
    }

    public static final class LogContents {

        public static final Uri URI = Uri.parse(CONTENT_URI +"/logcontents");

        public static final String TABLE = "log_contents";

        public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.grasscove.survlog.log_contens";
        public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.grasscove.survlog.log_contens";

        public static final class Columns implements BaseColumns {
            public static final String LOG = "log";
            public static final String LINE = "line";
        }

        public static final String DEFAULT_SORT_ORDER = Columns._ID +" ASC";

        public static final String CREATE =
            "CREATE TABLE " + TABLE + "("
                + Columns._ID + " integer primary key autoincrement,"
                + Columns.LOG + " integer,"
                + Columns.LINE + " text default ''"
            + ");";
    }
}