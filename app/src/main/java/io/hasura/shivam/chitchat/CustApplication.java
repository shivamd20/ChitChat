package io.hasura.shivam.chitchat;

import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;



import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraInitException;
import io.hasura.shivam.chitchat.services.GetNewMessages;
import io.hasura.shivam.chitchat.services.SendMesseges;
import io.hasura.shivam.chitchat.services.SyncContacts;

public class CustApplication extends com.activeandroid.app.Application {

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!

//
//    DaoSession daoSession;

    boolean Encrypted=true;


    //TODO  protected AbstractDaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();

        Intent syncintent=new Intent(this.getApplicationContext(), SyncContacts.class);



        Intent sendmsgintent=new Intent(this.getApplicationContext(), SendMesseges.class);

        Intent getNewMsg=new Intent(this.getApplicationContext(), GetNewMessages.class);



        try {

            Hasura.setProjectConfig(new ProjectConfig.Builder()
                    //.setProjectName("projectName") // or it can be .setCustomBaseDomain("somthing.com")
                    .setCustomBaseDomain("shivam.hasura.me")
                    .enableOverHttp() // if not included, then https by default
                    .setDefaultRole("user") // if not included then "user" role is used by default
                 //   .setApiVersion(2) //if not included v1 is used by default
                    .build())
                    .enableLogs() // not included by default
                    .initialise(this);

        } catch (HasuraInitException hie) {
            Toast.makeText(this, hie.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("initialzation error",hie.toString());
        }

      //  startService(fetchmsgintent);
        startService(syncintent);
        startService(sendmsgintent);
        startService(getNewMsg);
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Encrypted ? "chitchat-db-encrypted" : "chitchat-db");
//        Database db = Encrypted ? helper.getEncryptedWritableDb("chit-chat") : helper.getWritableDb();
//        daoSession = new DaoMaster(db).newSession();
    }
//
//    public DaoSession getDaoSession() {
//        return daoSession;
//    }


        // Required initialization logic here!


    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
