package com.myleakyconlondon.dao;

import android.provider.BaseColumns;

/**
 * User: Elizabeth Hamlet
 */
public abstract class DataContract implements BaseColumns {

    private DataContract() {}

    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String EVENT_ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String START_DATE = "startTime";
        public static final String END_DATE = "endTime";
        public static final String LOCATION = "location";
        public static final String TYPE = "type";
        public static final String IS_BACKUP_EVENT= "isBackUpEvent";
        public static final String BACKUP_EVENT_ID = "backUpEventId";
        public static final String DAY_ID = "dayId";
        public static final String DAY_END_ID = "dayEndId";

        public static final String[] COLUMNS = {EVENT_ID, TITLE, DESCRIPTION, START_DATE, END_DATE, LOCATION, TYPE, IS_BACKUP_EVENT, BACKUP_EVENT_ID, DAY_ID, DAY_END_ID};
    }

    public static abstract class Day implements BaseColumns {
        public static final String TABLE_NAME = "day";
        public static final String DAY_ID = "_id";
        public static final String DATE = "date";
        public static final String DAY_NUMBER = "dayNumber";

        public static final String[] COLUMNS = {DAY_ID, DATE, DAY_NUMBER};
    }
}
