package com.paypal.yambalab;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class YambaContract {
        private YambaContract() {}

    public static final long VERSION = 1;

    public static final String AUTHORITY = "com.paypal.android.yamba";

    public static final Uri BASE_URI = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT) // content://
        .authority(AUTHORITY)
        .build();

    public static final class Timeline {
        private Timeline() { }

        public static final String TABLE = "timeline";

        private static final String MINOR_TYPE = "/vnd." + AUTHORITY;

        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE; // vnd.android.cursor.item/vnd.com.paypal.android.yamba
        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE; // vnd.android.cursor.dir/vnd.com.paypal.android.yamba

        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build(); // content://com.paypal.android.yamba/timeline

        public static final class Column {
            private Column() { }

            public static final String ID = BaseColumns._ID; // ID, id, Id, _id
            public static final String TIMESTAMP = "time";
            public static final String MAX_TIMESTAMP = "maxTime";
            public static final String USER = "user";
            public static final String STATUS = "message";
        }
    }
}