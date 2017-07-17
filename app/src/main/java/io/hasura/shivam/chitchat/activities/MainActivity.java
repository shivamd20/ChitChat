package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Conversation;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


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

      if  ( Hasura.getClient().getUser().getAuthToken()==null)
        {
            Intent intent=new Intent(this,LoginActivity.class);
            finish();
            startActivity(intent);

        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(myToolbar);
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

    class LoadRecentsFromDatabase extends AsyncTask<Void,Void,List<Conversation>>{

        @Override
        protected void onPostExecute(List<Conversation> conversations) {
            super.onPostExecute(conversations);
        }

        @Override
        protected List<Conversation> doInBackground(Void... params) {
            return null;
        }
    }
}


