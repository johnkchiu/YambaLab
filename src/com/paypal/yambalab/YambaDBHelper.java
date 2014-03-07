package com.paypal.yambalab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YambaDBHelper extends SQLiteOpenHelper {
	private static final String DB_FILE = "yamba.db";
	private static final int VERSION = 1;

	public YambaDBHelper(Context context) {
		super(context, DB_FILE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE " + YambaContract.Timeline.TABLE + " (" +
                 YambaContract.Timeline.Column.ID + " INTEGER PRIMARY KEY," +
                 YambaContract.Timeline.Column.TIMESTAMP + " INTEGER NOT NULL," +
                 YambaContract.Timeline.Column.USER + " TEXT NOT NULL," +
                 YambaContract.Timeline.Column.STATUS + " TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no upgrade SQL
	}

}
