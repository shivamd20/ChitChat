package io.hasura.shivam.chitchat.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.PersonDetails;
import io.hasura.shivam.chitchat.queclasses.SelectQueryPerson;

public class ApiTestingActivity extends AppCompatActivity {

    HasuraUser user=Hasura.getClient().getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        user.setUsername("admin");
        user.setPassword("HeYmos53");


      //  user.setAuthToken("bnsmjm84z7e62njxtf8qyjbjsv9k382f");
       // user.setRoles(new String[]{"admin"});

        user.login(new AuthResponseListener() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(ApiTestingActivity.this,""+s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(HasuraException e) {
                Toast.makeText(ApiTestingActivity.this,""+e.toString(),Toast.LENGTH_LONG).show();
            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



                try {
              //  new JSONObject(new SelectQueryPerson(100)).toString();

                    JSONObject jsonObject=new JSONObject("  {\"type\":\"select\"," +
                            " \"args\":{" +
                            "\"table\":\"person\", \"columns\":[\"*\"]" +
                            "}" +
                            "}");

                    Log.e("json",jsonObject.toString());



                    Hasura.getClient().asRole("admin").useDataService()
                            .setRequestBody(jsonObject)
                            .expectResponseType(PersonDetails.class)
                            .enqueue(new Callback<PersonDetails, HasuraException>() {
                                @Override
                                public void onSuccess(PersonDetails message) {
                                    Toast.makeText(ApiTestingActivity.this.getApplicationContext(), "row affected" + message.getMobile(), Toast.LENGTH_LONG).show();
                                    Person person = new Person(user.getMobile() + ":me", null);

                                    person.save();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(ApiTestingActivity.this, e.getCode() + "error " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("select query",e.getCode()+" <-code   " +e.toString());
                                }
                            });
                }catch (Exception je)
                {
                    Log.e("json ",je.toString());
                }

            }
        });


    }

}
