package io.hasura.shivam.chitchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.hasura.shivam.chitchat.database.DBContract.*;

/**
 * Created by shivam on 15/6/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + Contacts.TABLE_NAME + " (" +
                    Contacts._ID + " INTEGER PRIMARY KEY," +
                    Contacts.MOB + " NUMBER," +
                    Contacts.NAME + " TEXT" +
                    Contacts.PROFILE_PIC + " BLOB" +
                    ")";

    private static final String SQL_CREATE_CHATS_TABLE =
            "CREATE TABLE " + Chats.TABLE_NAME + " (" +
                    Chats._ID + " INTEGER PRIMARY KEY," +
                    Chats.DATE_TIME + " NUMBER," +
                    Chats.ISME + " INTEGER," +
                    Chats.MESSEGE + " TEXT," +
                    Chats.ISDELIVERED + " NUMBER," +
                    Chats.WITH + " TEXT" +
                    ")";

    private static final String SQL_CREATE_DRAW_TABLE =
            "CREATE TABLE " + Draws.TABLE_NAME + " (" +
                    Draws._ID + " INTEGER PRIMARY KEY," +
                    Draws.DATE_TIME + " NUMBER," +
                    Draws.ISME + " INTEGER," +
                    Draws.TODRAW + " TEXT," +
                    Draws.ISDELIVERED + " NUMBER," +
                    Draws.WITH + " TEXT" +
                    ")";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Chats.TABLE_NAME;




    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chitchat.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHATS_TABLE);

        db.execSQL(SQL_CREATE_CONTACTS_TABLE);

        db.execSQL(SQL_CREATE_DRAW_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
