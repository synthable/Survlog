package com.grasscove.survlog.provider;

import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.provider.SurvlogContract.LogContents;
import com.grasscove.survlog.provider.SurvlogContract.Logs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SurvlogDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "survlog.db";
    public static final int DATABASE_VERSION = 4;

    public SurvlogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when no database exists in disk and the helper class needs to create a new one.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Hosts.CREATE);
        db.execSQL(Logs.CREATE);
        db.execSQL(LogContents.CREATE);
    }

    /**
     * Called when there is a database version mismatch meaning that the version of the database on
     * disk needs to be upgraded to the current version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Upgrade the existing database to conform to the new version. Multiple previous versions
         * can be handled by comparing _oldVersion and _newVersion values. The simplest case is to
         * drop the old table and create a new one.
         */
        db.execSQL("DROP TABLE IF EXISTS " + Hosts.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Logs.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LogContents.TABLE);

        // Create a new one.
        onCreate(db);
    }
}