package io.hasura.shivam.chitchat;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.Toast;


import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraInitException;

public class CustApplication extends Application {

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!

  //TODO  protected AbstractDaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();


        try {
            Hasura.setProjectConfig(new ProjectConfig.Builder()
                    //.setProjectName("projectName") // or it can be .setCustomBaseDomain("somthing.com")
                    .setCustomBaseDomain("shivam.hasura.me")
                    .enableOverHttp() // if not included, then https by default
                    .setDefaultRole("customDefaultRole") // if not included then "user" role is used by default
                    .setApiVersion(2) //if not included v1 is used by default
                    .build())
                    .enableLogs() // not included by default
                    .initialise(this);

        }
        catch (HasuraInitException hie)
        {
            Toast.makeText(this,hie.getMessage(),Toast.LENGTH_SHORT).show();
        }



        // Required initialization logic here!
    }

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
