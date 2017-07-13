package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.hasura.sdk.Hasura;
import io.hasura.shivam.chitchat.R;

public class MainActivity extends AppCompatActivity {


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
}
