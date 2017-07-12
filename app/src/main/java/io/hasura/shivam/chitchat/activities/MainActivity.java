package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.hasura.sdk.Hasura;
import io.hasura.shivam.chitchat.R;

public class MainActivity extends AppCompatActivity {

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
    }
}
