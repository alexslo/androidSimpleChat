package com.camlab.simplechatalexslo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MsgDataBase  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "msg_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "contact_table";
    public static final String MSQ_VALUE = "msqvalue";
    public static final String _ID = "_id";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MSQ_VALUE + " VARCHAR(255));";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public MsgDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
