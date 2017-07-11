package io.hasura.shivam.chitchat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.InsertQueryPerson;
import io.hasura.shivam.chitchat.queclasses.PersonDetails;
import io.hasura.shivam.chitchat.queclasses.ResponseMessage;
import io.hasura.shivam.chitchat.queclasses.SelectQueryPerson;

public class otpVarificationActivity extends AppCompatActivity {


    Button submitotp;
    EditText otpTxt;
    HasuraUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);

         user= Hasura.getClient().getUser();




        Toast.makeText(this,user.getMobile(),Toast.LENGTH_LONG).show();

       submitotp=(Button)findViewById(R.id.varify_otp_button);
        otpTxt=(EditText) findViewById(R.id.otp);



        if(!getIntent().getBooleanExtra(LoginActivity.ISNEW,false))
        {
            user.resendOTP(new OtpStatusListener() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(otpVarificationActivity.this,s,Toast.LENGTH_SHORT).show();

                }
                @Override
                public void onFailure(HasuraException e) {
                    Toast.makeText(otpVarificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        submitotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otpTxt.getText().length()!=6)
                {
                    otpTxt.setError("enter valid otp");

                    return;
                }


                user.otpLogin(otpTxt.getText().toString(), new AuthResponseListener() {
                    @Override
                    public void onSuccess(String s) {

                        Toast.makeText(otpVarificationActivity.this, s, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(otpVarificationActivity.this, MainActivity.class);

                        startActivity(intent);

                        android.app.AlertDialog progressDialog = new ProgressDialog.Builder(otpVarificationActivity.this)
                                .setTitle("Just a moment... We are setting few things")
                                .setMessage("wont take long").create();


                        PersonDetails personDetails=new PersonDetails();

                        personDetails.setMobile(user.getMobile());
                        personDetails.setUser_id(user.getId());





                        Gson gson=new Gson();

                       Log.e( "json",gson.toJson(new SelectQueryPerson(100)).toString());

                      //  finish();

                        progressDialog.dismiss();


                    }

                    @Override
                    public void onFailure(HasuraException e) {

                        otpTxt.setError(e.getMessage());

                    }
                });
            }
        });







    }

}
