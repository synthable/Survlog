package com.grasscove.survlog.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.LogContents;
import com.grasscove.survlog.provider.SurvlogContract.Logs;

public class SurvlogProvider extends ContentProvider {

	private SurvlogDbHelper dbHelper;

	public static final int HOSTS = 100;
	public static final int HOST = 101;
	public static final int HOST_LOGS = 102;
	public static final int HOST_LOG = 103;

	public static final int LOGS = 200;
	public static final int LOG = 201;
	public static final int LOG_CONTENT = 202;

    public static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "hosts", HOSTS);
        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "hosts/#", HOST);
        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "hosts/#/logs", HOST_LOGS);
        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "hosts/#/logs/#", HOST_LOG);

        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "logs", LOGS);
        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "logs/#", LOG);
        sUriMatcher.addURI(SurvlogContract.AUTHORITY, "logs/#/content", LOG_CONTENT);
    }

	@Override
	public boolean onCreate() {
		dbHelper = new SurvlogDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		try {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            long rowId = 0;
            Uri notifyUri = null;

            switch(sUriMatcher.match(uri)) {
	            case HOSTS:
	            	notifyUri = Hosts.URI;
                    rowId = db.insertOrThrow(Hosts.TABLE, null, values);
	            	break;
	            case LOGS:
	            	notifyUri = Logs.URI;
                    rowId = db.insertOrThrow(Logs.TABLE, null, values);
	            	break;
	            case LOG_CONTENT:
	            	notifyUri = LogContents.URI;
                    rowId = db.insertOrThrow(LogContents.TABLE, null, values);
	            	break;
	            default:
                    throw new IllegalArgumentException("Unknown insert URI: " + uri);
            }
Log.v("uri", uri.toString());
            if (rowId > 0) {
                final Uri insertUri = ContentUris.withAppendedId(notifyUri, rowId);
                getContext().getContentResolver().notifyChange(uri, null); //insertUri, null);
                return insertUri;
            }

		} catch (SQLiteConstraintException e) {
            throw e;
        }
        return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteDatabase mDb = dbHelper.getReadableDatabase();
	    final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

Log.v("uri", uri.toString());
	    
	    switch (sUriMatcher.match(uri)) {
	        case HOSTS:
	            qb.setTables(Hosts.TABLE);
	            break;
	        case HOST:
	            qb.setTables(Hosts.TABLE);
	            selection = Hosts.TABLE +"."+ Hosts.Columns._ID +"=?";
	            selectionArgs = new String[] {uri.getLastPathSegment()};
	            break;
	        case HOST_LOGS:
	            qb.setTables(Hosts.TABLE +","+ Logs.TABLE);
	            projection = new String[] {
	            	Logs.TABLE +"."+ Logs.Columns._ID,
	            	Logs.TABLE +"."+ Logs.Columns.HOST,
	            	Logs.TABLE +"."+ Logs.Columns.PATH,
	            	Logs.TABLE +"."+ Logs.Columns.TITLE,
	            	Logs.TABLE +"."+ Logs.Columns.LINES
	            };
	            selection = 
	            	Hosts.TABLE +"."+ Hosts.Columns._ID +"="+ Logs.TABLE +"."+ Logs.Columns.HOST +" AND "+
	            	Hosts.TABLE +"."+ Hosts.Columns._ID +"=?";
                selectionArgs = new String[] {uri.getPathSegments().get(1)};
	            break;
	        case LOGS:
	            qb.setTables(Logs.TABLE);
	            break;
	        case LOG:
	            qb.setTables(Logs.TABLE);
	            selection = Logs.TABLE +"."+ Logs.Columns._ID +"=?";
	            selectionArgs = new String[] {uri.getLastPathSegment()};
	            break;
	        case LOG_CONTENT:
	            qb.setTables(LogContents.TABLE);
	            selection = LogContents.Columns.LOG +"=?";
	            selectionArgs = new String[] {uri.getPathSegments().get(1)};
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown query URI: " + uri);
	    }

	    final Cursor c = qb.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
	    c.setNotificationUri(getContext().getContentResolver(), uri);
	    return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table = null;
        Uri notify;
        switch(sUriMatcher.match(uri)) {
            case LOG:
                table = Logs.TABLE;
                selection = Logs.Columns._ID +"=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                notify = Logs.buildHostLogsUri(values.getAsLong(Logs.Columns.HOST));
                break;
            case HOST:
                table = Hosts.TABLE;
                selection = Hosts.Columns._ID +"=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                notify = uri;
                break;
            default:
                throw new IllegalArgumentException("Unknown update URL: " + uri);
        }

Log.v("uri", uri.toString());
        
        getContext().getContentResolver().notifyChange(uri, null); //notify, null);
        return db.update(table, values, selection, selectionArgs);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		Uri notify;
		switch(sUriMatcher.match(uri)) {
			case LOG:
				selection = Logs.Columns._ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                count = db.delete(Logs.TABLE, selection, selectionArgs);
                notify = Logs.buildLogUri(
                	//Long.valueOf(uri.getPathSegments().get(1))
                	Long.valueOf(uri.getLastPathSegment())
                );
				break;
			case HOST:
				selection = Hosts.Columns._ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                count = db.delete(Hosts.TABLE, selection, selectionArgs);
                notify = Hosts.buildHostUri(
                	Long.valueOf(uri.getLastPathSegment())
                );
				break;
			case LOG_CONTENT:
				selection = LogContents.Columns.LOG + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                count = db.delete(LogContents.TABLE, selection, selectionArgs);
                notify = Hosts.buildHostUri(
                	Long.valueOf(uri.getLastPathSegment())
                );
	            break;
			default:
                throw new IllegalArgumentException("Unknown delete URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(notify, null);
        return count;
	}
}