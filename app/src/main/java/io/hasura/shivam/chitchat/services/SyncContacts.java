package io.hasura.shivam.chitchat.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.shivam.chitchat.activities.ApiTestingActivity;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.PersonDetails;
import io.hasura.shivam.chitchat.queclasses.SelectQueryPerson;

public class SyncContacts extends Service {

    boolean doInstantSync=true;
    int waitFor =5000;

     SyncContacts() {
    }
    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {

        mContext=this.getApplicationContext();
    }

    Context mContext;
    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        while (true) {

            try {

                Thread.sleep(waitFor);
            }
            catch (InterruptedException ie)
            {

            }

            ArrayList<String> alContacts;

            ContentResolver cr = mContext.getContentResolver(); //Activity/Application android.content.Context
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
               alContacts = new ArrayList<String>();
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {

                            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            contactNumber= contactNumber.replace("-","");
                            contactNumber= contactNumber.replace(" ","");
                            if(contactNumber.length()>10) {

                             //   contactNumber.replace("-","");
                                try {


                                    contactNumber = contactNumber.substring(contactNumber.length() - 10, contactNumber.length());

                                    Long.parseLong(contactNumber);

                                  //  Log.i("number", contactNumber);

                                    alContacts.add(contactNumber);
                                }
                                catch (NumberFormatException ne)
                                {
                                    Log.e("number form",ne.toString());
                                }
                            }

                            break;
                        }
                        pCur.close();
                    }


                } while (cursor.moveToNext());

                try {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("type", "select");

                    JSONObject args =new JSONObject();

                    args.put("table","person");

                    JSONArray jsonArray=new JSONArray();

                    jsonArray.put("*");


                    args.put("columns",jsonArray);

                    JSONObject where=new JSONObject();

                    JSONArray or =new JSONArray();



                    for(String X:alContacts) {

                        JSONObject orjson=new JSONObject();
                        orjson.put("mobile", X);
                        or.put(orjson);
                    }



                    where.put("$or",or);

                    args.put("where",where);

                    jsonObject.put("args",args);


                    Log.i("json",jsonObject.toString());

                    Hasura.getClient().asRole("user").useDataService()
                            .setRequestBody(jsonObject)
                            .expectResponseTypeArrayOf(PersonDetails.class)
                            .enqueue(new Callback<List<PersonDetails>, HasuraException>() {
                                @Override
                                public void onSuccess(List<PersonDetails> message) {
                                    //TODO add all to local database
                                    Log.e("sync resu", message.size() + "");

                                    for(PersonDetails p:message)
                                    {
                                        Person person=new Person();
                                        person.profile_pic= Base64.decode(p.getProfile_pic(),Base64.URL_SAFE);
                                        person.mobile=p.getMobile()+"";
                                        person.save();
                                    }
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Log.e("select query", e.getCode() + " <-code   " + e.toString());
                                }
                            });
                }
                catch (JSONException je)
                {
                    Log.e("json error",je.toString());
                }

                if(doInstantSync) {
                 //   doInstantSync=false;
                    break;
                }
            }

        }

        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }



    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }

}
