package io.hasura.shivam.chitchat.database;

import android.provider.BaseColumns;

/**
 * Created by shivam on 15/6/17.
 */

public   interface DBContract {

   public static interface Contacts extends BaseColumns
    {

        String TABLE_NAME="contacts";
        //String
        public static final String NAME ="name";

        //Long
        public static final String MOB ="mob";

        //BLOB
        public static final String PROFILE_PIC ="prof_pic";
    }

    public static interface Chats extends BaseColumns
    {

        String TABLE_NAME="chats";
        //LONG
        public static final String WITH ="with";

        public static final String ISME ="isme";
        public static final String DATE_TIME ="datetime";
        public static final String MESSEGE ="message";
        public static final String ISDELIVERED ="isDelivered";
    }

    public static interface Draws extends BaseColumns
    {
        String TABLE_NAME="draws";
        public static final String WITH ="with";
        public static final String ISME ="isme";
        public static final String DATE_TIME ="datetime";
        public static final String TODRAW ="todraw";
        public static final String ISDELIVERED ="isDelivered";
    }

}
