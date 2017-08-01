package io.hasura.shivam.chitchat.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import org.json.JSONObject;


import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;
import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.Secrets;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.queclasses.InsertQueryPerson;
import io.hasura.shivam.chitchat.queclasses.PersonDetails;
import io.hasura.shivam.chitchat.queclasses.ResponseMessage;
import io.hasura.shivam.chitchat.services.GetNewMessages;
import io.hasura.shivam.chitchat.services.SendMesseges;
import io.hasura.shivam.chitchat.services.SyncContacts;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    public static final String ISNEW="isNew";

    private static final int APP_REQUEST_CODE = 5000;
    private static final String TAG = "LOGINACTIVITY";
    private View mProgressView;
    private View mLoginFormView;
    CardView cardView;

    OkHttpClient client = new OkHttpClient();


    HasuraUser hasuraUser;



    public void phoneLogin(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

        showProgress(true);

    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
               // showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();

                    toastMessage="uder sucees";

                    Log.e("AK Token",loginResult.getAccessToken().getToken());

                    loginResult.getAccessToken().getToken();

                    GraphApiCall graphApiCall=new GraphApiCall();

                    graphApiCall.execute(loginResult.getAccessToken().getToken());


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
              //  goToMyLoggedInActivity();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

            hasuraUser=Hasura.getClient().getUser();

        cardView=(CardView)findViewById(R.id.cardviewlogin);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

//            cardView.setVisibility(show ? View.VISIBLE : View.GONE);
//            cardView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
           // cardView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }



    public   void registerAsChitchatMember(final HasuraUser hasuraUser)
    {
        PersonDetails personDetails=new PersonDetails();

        personDetails.setMobile(Long.parseLong(hasuraUser.getMobile()));
        personDetails.setUser_id(hasuraUser.getId());

        Hasura.getClient().asRole("user").useDataService()
                .setRequestBody(new InsertQueryPerson(personDetails))
                .expectResponseType(ResponseMessage.class)
                .enqueue(new Callback<ResponseMessage, HasuraException>() {
                    @Override
                    public void onSuccess(ResponseMessage message) {
                        Log.e(TAG,message.getRowsAffected()+" row affacted Register Chitchat user");


                        Person person = new Person();

                        person.mobile=hasuraUser.getMobile();

                        person.save();


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);

                        LoginActivity.this.finish();

                        Intent syncintent=new Intent(LoginActivity.this.getApplicationContext(), SyncContacts.class);



                        Intent sendmsgintent=new Intent(LoginActivity.this.getApplicationContext(), SendMesseges.class);

                        Intent getNewMsg=new Intent(LoginActivity.this.getApplicationContext(), GetNewMessages.class);

                        startService(syncintent);
                        startService(sendmsgintent);
                        startService(getNewMsg);


                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if(e.getCode()== HasuraErrorCode.BAD_REQUEST)
                            if(e.getMessage().contains("unique constraint")){


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                Log.e("hasura error", "user already exists"+e.toString());
                                Person person = new Person();

                                person.mobile=hasuraUser.getMobile();

                                Intent syncintent=new Intent(LoginActivity.this.getApplicationContext(), SyncContacts.class);



                                Intent sendmsgintent=new Intent(LoginActivity.this.getApplicationContext(), SendMesseges.class);

                                Intent getNewMsg=new Intent(LoginActivity.this.getApplicationContext(), GetNewMessages.class);

                                startService(syncintent);
                                startService(sendmsgintent);
                                startService(getNewMsg);


                                person.save();

                                LoginActivity.this.finish();


                            }else {
                                Log.e("hasura error", e.toString());
                                showProgress(false);
                            }
                            else{
                            Log.e("hasura error", e.toString());
                            showProgress(false);
                        }

                    }
                });
    }

    class GraphApiCall extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
           // super.onPostExecute(aVoid);
            Toast.makeText(LoginActivity.this,"Mobile Number"+aVoid,Toast.LENGTH_SHORT).show();
            Log.e(TAG,aVoid);

            hasuraUser.setUsername(Secrets.getUserName(aVoid));
            hasuraUser.setMobile(aVoid);
            hasuraUser.setPassword(Secrets.getPassword(aVoid));
            hasuraUser.signUp(new SignUpResponseListener() {
                @Override
                public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {

                }

                @Override
                public void onSuccess(HasuraUser hasuraUser) {

                    Toast.makeText(LoginActivity.this,"LOGGED IN AS HASURA USER",Toast.LENGTH_LONG).show();
                    registerAsChitchatMember(hasuraUser);



                }

                @Override
                public void onFailure(HasuraException e) {
                    Log.e(TAG,"Login Failure"+e.toString());

                    if(e.getCode()==HasuraErrorCode.USER_ALREADY_EXISTS)
                    {
                        hasuraUser.login(new AuthResponseListener() {
                            @Override
                            public void onSuccess(String s) {
                                Log.e(TAG,"s"+s);

                                registerAsChitchatMember(hasuraUser);

                            }

                            @Override
                            public void onFailure(HasuraException e) {

                                showProgress(false);

                                Log.e(TAG,e.toString());

                            }
                        });
                    }
                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Log.e(TAG,"doinbackground");

                String url = "https://graph.accountkit.com/v1.2/me/?access_token=" + params[0];
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                if(response.code()==200)
                {
                    JSONObject json=new JSONObject(response.body().string());
                   return  (String)((JSONObject) json.get("phone")).get("national_number");
                }
            }catch (Exception ioe)
            {
                Log.e(TAG,ioe.toString());
                showProgress(false);
            }

            return null;
        }
    }
}