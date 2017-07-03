package io.hasura.shivam.chitchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.shivam.chitchat.R;

public class OtpVarificationActivity extends AppCompatActivity {


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
                    Toast.makeText(OtpVarificationActivity.this,s,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(HasuraException e) {
                    Toast.makeText(OtpVarificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(OtpVarificationActivity.this,s,Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(OtpVarificationActivity.this,MainActivity.class);

                        startActivity(intent);

                        finish();


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
