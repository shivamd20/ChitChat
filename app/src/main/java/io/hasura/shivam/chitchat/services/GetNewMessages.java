package io.hasura.shivam.chitchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.shivam.chitchat.database.Conversation;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.MessegePool;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetNewMessages extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "io.hasura.shivam.chitchat.services.action.FOO";
    private static final String ACTION_BAZ = "io.hasura.shivam.chitchat.services.action.BAZ";

    String TAG = "GETNEWMESSEGES";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "io.hasura.shivam.chitchat.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "io.hasura.shivam.chitchat.services.extra.PARAM2";
    private boolean responseArrivedFetch = false;
    private long waitFor = 50;

    List<MessegePool> message;

    public GetNewMessages() {
        super("GetNewMessages");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetNewMessages.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetNewMessages.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    void onFetchResult() {
        //TODO add all to local database
        Log.e(TAG, message.size() + "");

        //    ActiveAndroid.beginTransaction();
        for (MessegePool p : message) {
            try {

                Conversation conversation;

                conversation = new Select().from(Conversation.class).where("msg_id=" + p.getMsg_id()).executeSingle();

                if (conversation == null)
                    conversation = new Conversation();

                conversation.isDelivered = p.isDelivered();

                Person person = new Select().from(Person.class).where("user_id=" + p.getFrom()).executeSingle();

                if (person == null)
                    continue;

                //if(person!=null)
                conversation.with = person;
                conversation.isMe = false;
                conversation.message = p.getMsg();
                conversation.date = p.getTimestamp();
                conversation.msg_id = p.getMsg_id();

                //  p.getTimestamp();
                conversation.isDraw = p.isDraw();
                conversation.save();
            } catch (SQLiteConstraintException sq) {
                Log.e(TAG, p.getMsg() + "  duplicate" + sq.toString());
            }
        }
        //   ActiveAndroid.endTransaction();

        Log.e(TAG, "Messege Fetched");
        // sendMesseges();
    }

    void sendDeliveryReport()
    {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "update");

            JSONObject args = new JSONObject();

            args.put("table", "messege_pool");

            JSONObject set=new JSONObject();

            set.put("isDelivered",true);

          args.put("$set",set);

            JSONObject where = new JSONObject();

                        JSONArray or =new JSONArray();

            for(MessegePool m:message) {
                JSONObject orjson=new JSONObject();
                orjson.put("msg_id", m.getMsg_id());
                or.put(orjson);
            }



            where.put("$or",or);

            args.put("where", where);

            jsonObject.put("args", args);


            //  Log.i(TAG,jsonObject.toString());

            Hasura.getClient().asRole("user").useDataService()
                    .setRequestBody(jsonObject)
                    .expectResponseType(InsertResponse.class)
                    .enqueue(new Callback<InsertResponse, HasuraException>() {
                        @Override
                        public void onSuccess(InsertResponse message) {
                            responseArrivedFetch = true;




                            //   sendMesseges();


                        }

                        @Override
                        public void onFailure(HasuraException e) {


                            responseArrivedFetch = true;
                            Log.e(TAG, e.getCode() + " <-code   " + e.toString());
                        }
                    });
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
        }
    }


    void fetchMesseges() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "select");

            JSONObject args = new JSONObject();

            args.put("table", "messege_pool");

            JSONArray jsonArray = new JSONArray();

            jsonArray.put("*");


            args.put("columns", jsonArray);

            JSONObject where = new JSONObject();

//                        JSONArray or =new JSONArray();
//
//
//                            JSONObject orjson=new JSONObject();
//                              orjson.put();
//                            or.put(orjson);


            where.put("to", Hasura.getClient().getUser().getId());

            where.put("isDelivered","false");

            args.put("where", where);

            jsonObject.put("args", args);


            //  Log.i(TAG,jsonObject.toString());

            Hasura.getClient().asRole("user").useDataService()
                    .setRequestBody(jsonObject)
                    .expectResponseTypeArrayOf(MessegePool.class)
                    .enqueue(new Callback<List<MessegePool>, HasuraException>() {
                        @Override
                        public void onSuccess(List<MessegePool> message) {

                            GetNewMessages.this.message = message;


                            responseArrivedFetch = true;

                            //   sendMesseges();

                            sendDeliveryReport();


                        }

                        @Override
                        public void onFailure(HasuraException e) {


                            responseArrivedFetch = true;
                            Log.e(TAG, e.getCode() + " <-code   " + e.toString());
                        }
                    });
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
        }

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Log.e(TAG

                    , "Handle intent caled");
            while (true) {


                fetchMesseges();
                //   sendMesseges.start();


                while (!(responseArrivedFetch)) {

                    Thread.currentThread().sleep(waitFor);

                    Log.e(TAG, "waiting");
                }

                onFetchResult();

                responseArrivedFetch = false;

            }
        } catch (/*Interrupted*/Exception ie) {
            Log.e(TAG, ie.toString());
        }

        return;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public static class InsertResponse{

        @SerializedName("affected_rows")
        int affected_rows;

        @SerializedName("returning")
        List<Msg_Id> returning;

        class Msg_Id
        {
            @SerializedName("msg_id")
            long msg_id;
        }
    }
}
