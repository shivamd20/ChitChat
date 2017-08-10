package io.hasura.shivam.chitchat.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Conversation;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.recent.screens.RecentRVAdapter;
import io.hasura.shivam.chitchat.services.AccountGeneral;
import io.hasura.shivam.chitchat.services.SyncAdapter;
import io.hasura.shivam.chitchat.services.SyncContacts;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CONTACT = 500;
    public boolean stopSync = false;
    LoadRecentsFromDatabase loadRecentsFromDatabase;
    String TAG = "MAINACTIVITY";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecentRVAdapter recentRVAdapter;

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // getContact();
            }
        } else {
            // getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    askForContactPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();

        menuInflater.inflate(R.menu.menu_contacts_act,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForContactPermission();

        AccountGeneral.createSyncAccount(this);

        SyncAdapter.performSync();


        //// Intent sendmsgintent = new Intent(this.getApplicationContext(), SendMesseges.class);
       // Intent getNewMsg = new Intent(this.getApplicationContext(), GetNewMessages.class);


        SharedPreferences sp = this.getSharedPreferences("HasuraUserStore", MODE_PRIVATE);

        Log.e(TAG, sp.getString("AuthTokenKey", "null"));

        Log.e(TAG, Hasura.getClient().getUser().isLoggedIn() + "");// this returns false even when user is logged in

        mRecyclerView=(RecyclerView)findViewById(R.id.main_recyler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(myToolbar);

         loadRecentsFromDatabase=new LoadRecentsFromDatabase();
        loadRecentsFromDatabase.execute();

        if (sp.getString("AuthTokenKey", "null").compareTo("null") == 0) {
            Log.e(TAG, "NOT WORKING" + Hasura.getClient().getUser().isLoggedIn() + "");

            Intent intent = new Intent(this, LoginActivity.class);

            stopSync = true;

            startActivity(intent);

            finish();
        } else {
            Intent syncintent = new Intent(this.getApplicationContext(), SyncContacts.class);
            startService(syncintent);
           // startService(sendmsgintent);
           // startService(getNewMsg);

        }
    }

    @Override
    protected void onStart() {


        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.new_chat:

                Intent intent=new Intent(this,CantactsActivity.class);

                startActivity(intent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopSync=true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopSync=true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        stopSync=false;

        loadRecentsFromDatabase=new LoadRecentsFromDatabase();

        loadRecentsFromDatabase.execute();
    }

    class LoadRecentsFromDatabase extends AsyncTask<Void,List<Conversation>,Boolean>{


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean)
            {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                    stopSync=true;
            }
        }

        @Override
        protected void onProgressUpdate(List<Conversation>... values) {
            super.onProgressUpdate(values);

            List<Conversation> conversations=values[0];

            Log.i("database messege",""+conversations.size());

            for(Conversation X:conversations)
            {
                Log.i("database messege",""+X.message);
            }
            if(recentRVAdapter==null)
                recentRVAdapter=new RecentRVAdapter(conversations);
            else
                recentRVAdapter.swap(conversations);
            mRecyclerView.setAdapter(recentRVAdapter);

            recentRVAdapter.setmOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loadRecentsFromDatabase.cancel(true);
                    stopSync=true;

                    int itempPos=mRecyclerView.getChildLayoutPosition(v);
                    Conversation cor=recentRVAdapter.getmDataset().get(itempPos);

                    Intent intent=new Intent(MainActivity.this,ChatActivity.class);

                    Log.i(TAG,"with main"+cor.with.getId());

                    intent.putExtra("with",cor.with.getId());

                    if(cor.with.getId()!=null)
                        startActivity(intent);
                    else{
                        Toast.makeText(MainActivity.this,"You can not talk with strangers" +
                                "first save his phone number and refresh your contact list",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                List<Conversation> conversationList;

                Log.e(TAG, "inside async" + Hasura.getClient().getUser().isLoggedIn() + "");

                long me = new Select().from(Person.class).where("mobile=?", Hasura.getClient().getUser().getMobile()).executeSingle().getId();

                while (!stopSync) {
                    String str = new Select("max(time_date)").from(Conversation.class)
                            .groupBy("with").where("isDraw=0").and("with!=" + me).toSql();

                    conversationList = new Select().distinct()
                            .from(Conversation.class)
                            .where("time_date in (" + str + ")").orderBy("time_date DESC").execute();

                    publishProgress(conversationList);


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            return false;
        }

        @Override
        protected void onCancelled() {
            //
        }

    }
}


