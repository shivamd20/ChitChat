package io.hasura.shivam.chitchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.HandlerThread;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
public class FetchMesseges extends IntentService {

 public static   final  String TAG="FETH MESSEGES";
    int waitFor=2000;
   static boolean doInstantSync=false;
   static boolean responseArrivedFetch =false;
    static boolean responseArrivedSend=false;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "io.hasura.shivam.chitchat.services.action.FOO";
    private static final String ACTION_BAZ = "io.hasura.shivam.chitchat.services.action.BAZ";

    private static final String FETCH_MESSEGES="io.hasura.shivam.chitchat.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "io.hasura.shivam.chitchat.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "io.hasura.shivam.chitchat.services.extra.PARAM2";

    private Context mContext;


    public FetchMesseges() {
        super("FetchMesseges");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchMesseges.class);
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
        Intent intent = new Intent(context, FetchMesseges.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


   synchronized void fetchMesseges()
    {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "select");

            JSONObject args =new JSONObject();

            args.put("table","messege_pool");

            JSONArray jsonArray=new JSONArray();

            jsonArray.put("*");


            args.put("columns",jsonArray);

            JSONObject where=new JSONObject();

//                        JSONArray or =new JSONArray();
//
//
//                            JSONObject orjson=new JSONObject();
//                              orjson.put();
//                            or.put(orjson);



            where.put("to",Hasura.getClient().getUser().getId() );

            args.put("where",where);

            jsonObject.put("args",args);


          //  Log.i(TAG,jsonObject.toString());

            Hasura.getClient().asRole("user").useDataService()
                    .setRequestBody(jsonObject)
                    .expectResponseTypeArrayOf(MessegePool.class)
                    .enqueue(new Callback<List<MessegePool>, HasuraException>() {
                        @Override
                        public void onSuccess(List<MessegePool> message) {

                            responseArrivedFetch =true;

                         //   sendMesseges();

                            //TODO add all to local database
                            Log.e(TAG, message.size() + "");

                            //    ActiveAndroid.beginTransaction();
                            for(MessegePool p:message)
                            {
                                try {

                                    Conversation conversation;

                                    conversation=new Select().from(Conversation.class).where("msg_id="+p.getMsg_id()).executeSingle();

                                    if(conversation==null)
                                        conversation=new Conversation();

                                    conversation.isDelivered=p.isDelivered();

                                    Person person=new Select().from(Person.class).where("user_id="+p.getFrom()).executeSingle();

                                    if(person==null)
                                        continue;

                                    //if(person!=null)
                                    conversation.with=person;
                                    conversation.isMe=false;
                                    conversation.message=p.getMsg();
                                    conversation.date=p.getTimestamp();
                                    conversation.msg_id=p.getMsg_id();

                                    //  p.getTimestamp();
                                    conversation.isDraw=p.isDraw();
                                    conversation.save();
                                }
                                catch (SQLiteConstraintException sq)
                                {
                                    Log.e(TAG, p.getMsg()+"  duplicate"+sq.toString());
                                }
                            }
                            //   ActiveAndroid.endTransaction();

                            Log.e(TAG,"Messege Fetched");

                            for(MessegePool p:message)
                            {
                                System.out.println(p.getMsg());
                            }

                            try{
                                Thread.sleep(waitFor/2);
                            }catch (Exception e363){
                                Log.e(TAG,e363.toString());

                            }

                           // sendMesseges();
                        }

                        @Override
                        public void onFailure(HasuraException e) {

                            try{
                                Thread.sleep(waitFor/2);
                            }catch (Exception e363){
                                Log.e(TAG,e363.toString());

                            }

                            responseArrivedFetch =true;
                            Log.e(TAG, e.getCode() + " <-code   " + e.toString());
                        }
                    });
        }
        catch (JSONException je)
        {
            Log.e(TAG,je.toString());
        }

    }

    public static void sendMesseges()
    {

       // ActiveAndroid.beginTransaction();
        final List<Conversation> conversations=new Select().from(Conversation.class).where("(isSent=0)").and("isMe=1").execute();

       // ActiveAndroid.endTransaction();
        if(conversations.size()<1) {
            Log.i(TAG,"0 sendings");

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

                each.put("from",Hasura.getClient().getUser().getId());
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
                    .expectResponseType(InsertResponse.class)
                    .enqueue(new Callback<InsertResponse, HasuraException>() {
                        @Override
                        public void onSuccess(InsertResponse insertResponse) {

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

    Thread sendMesseges=new Thread(new Runnable() {
        @Override
        public void run() {
            sendMesseges();
        }
    });


    @Override
    protected void onHandleIntent(Intent intent) {

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        try {
            Log.e(TAG,"Handle intent caled");
            while (true) {
                    fetchMesseges();


               // sendMesseges.start();


                    if(doInstantSync) {
                        //   doInstantSync=false;
                        break;
                    }

                while(!(responseArrivedFetch&&responseArrivedSend)) {

                    Thread.sleep(waitFor);

                    Log.e(TAG,"waiting");
                }

                responseArrivedFetch =false;
                responseArrivedSend=false;
                List<Conversation> messegePools=  new Select()
                        .from(Conversation.class)
                        //  .where("mobile = ?", p.getMobile()+"")
                        .execute();
                }
            }

        catch (/*Interrupted*/Exception ie)
        {
            Log.e(TAG,ie.toString());
        }

        return ;
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

