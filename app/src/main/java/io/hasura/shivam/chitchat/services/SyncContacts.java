package io.hasura.shivam.chitchat.services;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
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

public class SyncContacts extends IntentService {

    boolean doInstantSync=false;
    int waitFor =5000;


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public SyncContacts()
    {
        super("sync Contacts");
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

        super.onCreate();
        mContext=this.getApplicationContext();
    }

    Context mContext;
    /** The service is starting, due to a call to startService() */


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Log.e("Sync Started","Handle intent caled");
            while (true) {
                ArrayList<String[]> alContacts;

                ContentResolver cr = mContext.getContentResolver(); //Activity/Application android.content.Context
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cursor.moveToFirst()) {
                    alContacts = new ArrayList<String[]>();
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {

                                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String displayName= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                                contactNumber= contactNumber.replace("-","");
                                contactNumber= contactNumber.replace(" ","");
                                if(contactNumber.length()>10) {

                                    //   contactNumber.replace("-","");
                                    try {


                                        contactNumber = contactNumber.substring(contactNumber.length() - 10, contactNumber.length());

                                        Long.parseLong(contactNumber);

                                        //  Log.i("number", contactNumber);

                                        alContacts.add(new String[]{contactNumber,});
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

//                        Thread.sleep(waitFor);

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



                        for(String []X:alContacts) {

                            JSONObject orjson=new JSONObject();
                            orjson.put("mobile", X[0]);
                            or.put(orjson);
                 ///           Thread.sleep(waitFor);
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

                                        ActiveAndroid.beginTransaction();
                                        for(PersonDetails p:message)
                                        {
                                            //Thread.sleep(waitFor);
                                            Person person=  new Select()
                                                    .from(Person.class)
                                                    .where("mobile = ?", p.getMobile()+"")
                                                    .orderBy("RANDOM()")
                                                    .executeSingle();

                                            try {
                                                if(person.mobile==null) {
                                                    person = new Person();
                                                    if (p.getProfile_pic() != null)
                                                        person.profile_pic = Base64.decode(p.getProfile_pic(), Base64.URL_SAFE);
                                                    person.mobile = p.getMobile() + "";


                                                    if (person.save() == -1) {
                                                        Log.e("not saved", person.mobile);
                                                    }
                                                }
                                                else
                                                {
                                                    if (p.getProfile_pic() != null)
                                                        person.profile_pic = Base64.decode(p.getProfile_pic(), Base64.URL_SAFE);
                                                    if (person.save() == -1) {
                                                        Log.i("not saved", person.mobile);
                                                    }
                                                }
                                            }
                                            catch (Exception sq)
                                            {
                                                Log.e("not saved", p.getMobile()+"  duplicate"+sq.toString());
                                            }
                                        }
                                        ActiveAndroid.endTransaction();

                                        Log.e("Synced","Contact Synced");
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

                Thread.sleep(waitFor);
            }
        }
        catch (/*Interrupted*/Exception ie)
        {
            Log.e("intrupt",ie.toString());
        }

        return ;
    }




}
