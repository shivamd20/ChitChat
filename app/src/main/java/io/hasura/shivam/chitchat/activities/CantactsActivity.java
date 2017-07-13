package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.query.Select;

import java.util.List;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.contacts.ContactsRVAdapter;
import io.hasura.shivam.chitchat.database.DBContract;
import io.hasura.shivam.chitchat.database.Person;

public class CantactsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ContactsRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView=(RecyclerView) findViewById(R.id.contacts_recyler_view);

        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        GetContactsFromDataBase getContactsFromDataBase=new GetContactsFromDataBase();

        getContactsFromDataBase.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }



    class GetContactsFromDataBase extends AsyncTask<Void,Void,List<Person>>{

        @Override
        protected void onPostExecute(List<Person> persons) {
            super.onPostExecute(persons);

            mAdapter=new ContactsRVAdapter(persons);



        }

        @Override
        protected List<Person> doInBackground(Void... params) {

            return  new Select().from(Person.class).execute();


        }
    }

}

