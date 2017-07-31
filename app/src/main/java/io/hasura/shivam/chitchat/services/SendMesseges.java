package io.hasura.shivam.chitchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.shivam.chitchat.database.Conversation;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendMesseges extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "io.hasura.shivam.chitchat.action.FOO";
    private static final String ACTION_BAZ = "io.hasura.shivam.chitchat.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "io.hasura.shivam.chitchat.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "io.hasura.shivam.chitchat.extra.PARAM2";

    String TAG="SEND MESSEGES";
    private boolean responseArrivedSend;
    final private long waitFor=1000;

    GetNewMessages.InsertResponse insertResponse;

    public SendMesseges() {
        super("SendMesseges");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SendMesseges.class);
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
        Intent intent = new Intent(context, SendMesseges.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    void onsendResult()
    {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        try {
            Log.e(TAG,"Handle intent caled");
            while (true) {


                // sendMesseges.start();

                sendMesseges();


                while(!(responseArrivedSend)) {

                    Thread.currentThread().sleep(waitFor);

                    Log.e(TAG,"waiting");
                }

                responseArrivedSend=false;
            }
        }

        catch (/*Interrupted*/Exception ie)
        {
            Log.e(TAG,ie.toString());
        }

        return ;
    }

    public  void sendMesseges()
    {

        // ActiveAndroid.beginTransaction();
        final List<Conversation> conversations=new Select().from(Conversation.class).where("(isSent=0)").and("isMe=1").execute();

        // ActiveAndroid.endTransaction();

        if(conversations.size()<1) {


            responseArrivedSend=true;

            return;
        }

        try{
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "insert");

            JSONObject args =new JSONObject();

            args.put("table","messege_pool");

            JSONArray jsonArray=new JSONArray();



            for (Conversation conversation:conversations) {
                JSONObject each=new JSONObject();

                each.put("from", Hasura.getClient().getUser().getId());
                each.put("to",conversation.with.user_id);
                each.put("msg",conversation.message);
                each.put("timestamp",conversation.date);
                each.put("isDraw",conversation.isDraw);
                each.put("isDelivered",conversation.isDelivered);

                jsonArray.put(each);
            }




            args.put("objects",jsonArray);

            JSONArray returning=new JSONArray();

            returning.put("msg_id");

            args.put("returning",returning);

            jsonObject.put("args",args);

            Log.i(TAG,jsonObject.toString());

            Hasura.getClient().useDataService()
                    .setRequestBody(jsonObject)
                    .expectResponseType(GetNewMessages.InsertResponse.class)
                    .enqueue(new Callback<GetNewMessages.InsertResponse, HasuraException>() {
                        @Override
                        public void onSuccess(GetNewMessages.InsertResponse insertResponse) {

                            SendMesseges.this.insertResponse=insertResponse;

                            responseArrivedSend=true;
                            for(int i=0;i<insertResponse.affected_rows;i++)
                            {
                                Conversation con=  conversations.get(i);

                                con.msg_id=insertResponse.returning.get(i).msg_id;
                                con.isSent=true;
                                con.save();
                            }

                            Log.e(TAG,"Messges sent");
                        }

                        @Override
                        public void onFailure(HasuraException e) {

                            responseArrivedSend=true;

                            Log.e(TAG,e.toString());

                        }
                    });
        }
        catch (JSONException je)
        {
            Log.e(TAG,je.toString());
        }
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
}
