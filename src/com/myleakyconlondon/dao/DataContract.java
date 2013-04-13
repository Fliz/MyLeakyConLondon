package com.myleakyconlondon.dao;

import android.provider.BaseColumns;

/**
 * User: Elizabeth Hamlet
 */
public abstract class DataContract implements BaseColumns {

    private DataContract() {}

    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String EVENT_ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String START_DATE = "startTime";
        public static final String END_DATE = "endTime";
        public static final String LOCATION = "location";
        public static final String TYPE = "type";
        public static final String IS_BACKUP_EVENT= "isBackUpEvent";
        public static final String BACKUP_EVENT_ID = "backUpEventId";

    }
}
