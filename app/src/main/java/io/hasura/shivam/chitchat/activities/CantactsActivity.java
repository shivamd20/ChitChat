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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.contacts.ContactsRVAdapter;
import io.hasura.shivam.chitchat.database.Person;

import static io.hasura.shivam.chitchat.services.SyncContacts.getContactName;

public class CantactsActivity extends AppCompatActivity {

    String TAG="CONTACTSACTIVITY";
    ContactsRVAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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

            for (Person p:persons)
            {
                Log.i("Person in DB",p.mobile);
            }

            Log.i("Person in DB",persons.size()+"");

            mAdapter=new ContactsRVAdapter(persons);

            mAdapter.setmOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int itempPos=mRecyclerView.getChildLayoutPosition(v);
                    Person per=mAdapter.getmDataset().get(itempPos);

                    Intent intent=new Intent(CantactsActivity.this,ChatActivity.class);

                    Log.i(TAG,"with main"+per.getId());

                    intent.putExtra("with",per.getId());

                    if(per.getId()!=null)
                        startActivity(intent);
                    else{
                        Toast.makeText(CantactsActivity.this,"You can not talk with strangers" +
                                "first save his phone number and refresh your contact list",Toast.LENGTH_SHORT).show();
                    }

                    CantactsActivity.this.finish();
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected List<Person> doInBackground(Void... params) {

            List<Person> list = new Select().from(Person.class).where("mobile!=?", Hasura.getClient().getUser().getMobile()).execute();

            // List<Person> list= new Select("*").from(Person.class).orderBy("name ASC").execute();
            for(Person p:list)
            {
                p.name=getContactName(CantactsActivity.this,p.mobile);
                if(p.name==null)
                {
                    p.name=p.mobile;
                }
            }
            return list;
        }

    }

}

