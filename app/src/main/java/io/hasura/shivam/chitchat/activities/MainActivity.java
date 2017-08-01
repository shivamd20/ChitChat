package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.SyncStatusListener;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Conversation;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.recent.screens.RecentRVAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecentRVAdapter recentRVAdapter;

    public  boolean stopSync=false;

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

        Log.e(TAG,Hasura.getClient().getUser().isLoggedIn()+"");// this returns false even when user is logged in



        mRecyclerView=(RecyclerView)findViewById(R.id.main_recyler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(myToolbar);

         loadRecentsFromDatabase=new LoadRecentsFromDatabase();
        loadRecentsFromDatabase.execute();
    }

    LoadRecentsFromDatabase loadRecentsFromDatabase;

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

            List<Conversation> conversationList;

            Log.e(TAG,"inside async"+Hasura.getClient().getUser().isLoggedIn()+"");


            if(!Hasura.getClient().getUser().isLoggedIn())
            {
                Log.e(TAG,"inside async"+Hasura.getClient().getUser().isLoggedIn()+"");

                return true;
            }


            while(!stopSync) {
                String str = new Select("max(time_date)").from(Conversation.class)
                        .groupBy("with").where("isDraw=0").toSql();

                conversationList = new Select()
                        .from(Conversation.class)
                        .where("time_date in (" + str + ")").orderBy("time_date DESC").execute();

                publishProgress(conversationList);


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        @Override
        protected void onCancelled() {
            //
        }

    }

    String TAG="MAINACTIVITY";
}


