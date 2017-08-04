package io.hasura.shivam.chitchat.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.PersonDetails;

public class SyncContacts extends IntentService {

    boolean doInstantSync=false;
    int waitFor =1000;

    boolean responseArrived=false;

    String TAG="SYNC CONTACTS";

    List<PersonDetails> message;
    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;
    /**
     * interface for clients that bind
     */
    IBinder mBinder;
    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;
    Context mContext;

    public SyncContacts()
    {
        super("sync Contacts");
    }

    public static String getContactName(Context context, String number) {

        String name = null;

        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            } else {
            }
            cursor.close();
        }
        return name;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {

        super.onCreate();
        mContext=this.getApplicationContext();
    }

    /** The service is starting, due to a call to startService() */

    void onSyncResult(){

        Log.e(TAG, message.size() + "");

        //    ActiveAndroid.beginTransaction();
        for(PersonDetails p:message)
        {
            //Thread.sleep(waitFor);
//                                            Person person=  new Select()
//                                                    .from(Person.class)
//                                                    .where("mobile = ?", p.getMobile()+"")
//                                                    .executeSingle();

            try {
                                           /*     if(person==null) {
                                                    person = new Person();
                                                    if (p.getProfile_pic() != null)
                                                        person.profile_pic = Base64.decode(p.getProfile_pic(), Base64.URL_SAFE);
                                                    person.mobile = p.getMobile() + "";


                                                    long row;
                                                    if ((row=person.save()) == -1) {
                                                        Log.e(TAG, "not duplicate"+person.mobile);
                                                    }
                                                    else
                                                    {
                                                        Log.i(TAG,row+" saved  mob"+person.mobile);
                                                    }
                                                }
                                                else
                                                {
                                                    Log.i(TAG,"is not null");

                                                    if (p.getProfile_pic() != null)
                                                        person.profile_pic = Base64.decode(p.getProfile_pic(), Base64.URL_SAFE);
                                                    if (person.save() == -1) {
                                                        Log.i(TAG, person.mobile);
                                                    }
                                                }*/


                Person per;

                per=new Select().from(Person.class).where("mobile="+p.getMobile()).executeSingle();

                if(per==null)
                {
                    per=new Person();
                }

                per.mobile=p.getMobile()+"";

                per.name=getContactName(getApplicationContext(),per.mobile);

                per.user_id=p.getUser_id();

                // per.profile_pic=p.getProfile_pic();

                per.save();
            }
            catch (SQLiteConstraintException sq)
            {
                Log.e(TAG, p.getMobile()+"  duplicate"+sq.toString());
            }
        }
        //   ActiveAndroid.endTransaction();

    //    Log.e(TAG,"Contact Synced"+"    "+message.size());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Log.e(TAG,"Handle intent caled");
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
                                if(contactNumber.length()>=10) {

                                    try {
                                        contactNumber = contactNumber.substring(contactNumber.length() - 10, contactNumber.length());
                                        Long.parseLong(contactNumber);
                                        //  Log.i("number", contactNumber);
                                        alContacts.add(new String[]{contactNumber,});
                                    }
                                    catch (NumberFormatException ne)
                                    {
                                        Log.e(TAG,ne.toString());
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


                        Log.i(TAG,jsonObject.toString());

                        Hasura.getClient().asRole("user").useDataService()
                                .setRequestBody(jsonObject)
                                .expectResponseTypeArrayOf(PersonDetails.class)
                                .enqueue(new Callback<List<PersonDetails>, HasuraException>() {
                                    @Override
                                    public void onSuccess(List<PersonDetails> message) {

                                        SyncContacts.this.message=message;

                                        responseArrived=true;
                                        //TODO add all to local database
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                        responseArrived=true;
                                        Log.e(TAG, e.getCode() + " <-code   " + e.toString());
                                    }
                                });
                    }
                    catch (JSONException je)
                    {
                        Log.e(TAG,je.toString());
                    }

                    while(!responseArrived) {

                        Thread.currentThread().sleep(waitFor);
                    }

                    onSyncResult();

                    responseArrived=false;

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
            Log.e(TAG,ie.toString());
        }

        return ;
    }




}
