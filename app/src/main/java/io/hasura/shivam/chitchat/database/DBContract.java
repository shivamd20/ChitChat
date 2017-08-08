package io.hasura.shivam.chitchat.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shivam on 15/6/17.
 */

public   interface DBContract {


    String CONTENT_AUTHORITY = "io.hasura.chitchat";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    String PATH_CONVERSATION = "chats";

    interface Person extends BaseColumns {
        String TABLE_NAME = "Person";
        String mobile = "mobile";

        String name = "name";
        String user_id = "user_id";
        String profile_pic = "profile_pic";
    }

    interface Chats extends BaseColumns
    {


        String msg_id = "msg_id";

        String isRead = "isRead";


        String date = "time_date";

        String message = "messege";

        String isMe = "isMe";

        String isDraw = "isDraw";

        String isDelivered = "isDelivered";

        String with = "with";

        String isSent = "isSent";
    }
}
