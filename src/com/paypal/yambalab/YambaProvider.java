package com.paypal.yambalab;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;

public class YambaProvider extends ContentProvider {
	private static final int TIMELINE_ITEM_TYPE = 1;
	private static final int TIMELINE_DIR_TYPE = 2;

	private static final UriMatcher MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	private static final Map<String, String> PROJECTION_MAP = new HashMap<String, String>();

	static {
		MATCHER.addURI(YambaContract.AUTHORITY, YambaContract.Timeline.TABLE, TIMELINE_DIR_TYPE);
		MATCHER.addURI(YambaContract.AUTHORITY, YambaContract.Timeline.TABLE + "/#", TIMELINE_ITEM_TYPE);

		PROJECTION_MAP.put(YambaContract.Timeline.Column.ID, YambaContract.Timeline.Column.ID);
		PROJECTION_MAP.put(YambaContract.Timeline.Column.USER, YambaContract.Timeline.Column.USER);
		PROJECTION_MAP.put(YambaContract.Timeline.Column.TIMESTAMP, YambaContract.Timeline.Column.TIMESTAMP);
		PROJECTION_MAP.put(YambaContract.Timeline.Column.STATUS, YambaContract.Timeline.Column.STATUS);
		PROJECTION_MAP.put(YambaContract.Timeline.Column.MAX_TIMESTAMP, "max (" + YambaContract.Timeline.Column.TIMESTAMP + ")");
	}

	private YambaDBHelper mDbHelper;

	@Override
	public boolean onCreate() {
		mDbHelper = new YambaDBHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case TIMELINE_DIR_TYPE:
			return YambaContract.Timeline.DIR_TYPE;
		case TIMELINE_ITEM_TYPE:
			return YambaContract.Timeline.ITEM_TYPE;
		default:
			throw new UnsupportedOperationException("Not yet implemented");
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		long id = -1;

		switch (MATCHER.match(uri)) {
		case TIMELINE_ITEM_TYPE:
			id = ContentUris.parseId(uri);
		case TIMELINE_DIR_TYPE:
			break;
		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(YambaContract.Timeline.TABLE);
		qb.setProjectionMap(PROJECTION_MAP);

		if (id != -1) {
			qb.appendWhere(YambaContract.Timeline.Column.ID + " = " + id);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			qb.setStrict(true);
		}

		Cursor c = qb.query(mDbHelper.getReadableDatabase(), projection,
				selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		switch (MATCHER.match(uri)) {
		case TIMELINE_DIR_TYPE:
			break;
		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}

		int count = 0;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		try {
			db.beginTransaction();

			for (ContentValues cv : values) {
				db.insert(YambaContract.Timeline.TABLE, null, cv);
				count++;
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
