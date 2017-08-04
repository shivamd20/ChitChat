package io.hasura.shivam.chitchat.frags;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.android.graphics.CanvasView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.activities.ChatActivity;
import io.hasura.shivam.chitchat.database.Conversation;
import io.hasura.shivam.chitchat.database.Person;
import io.hasura.shivam.chitchat.frags.mChat.ChatAdapter;


public class Chats extends Fragment implements OnClickListener {

    public static ChatAdapter chatAdapter;
    public long me,with;

    boolean isCancel=false;
    ListView msgListView;
    CanvasView canvasView;
    GetMesseges getMesseges;
    private EditText msg_edittext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);

//        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
//                "Chats");
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        msgListView.setStackFromBottom(true);


        drawBackground();

        with= ((ChatActivity )this.getActivity()).with;
        me=((ChatActivity)this.getActivity()).me;



        Log.e("with",(with)+"");


        List<Conversation> list=new ArrayList<>();

        chatAdapter=new ChatAdapter(this.getActivity(),list);

        msgListView.setAdapter(chatAdapter);

         getMesseges=new GetMesseges();
        getMesseges.execute();

        drawBackground();

        return view;
    }


    @TargetApi(16)
    void drawBackground(){

        if (android.os.Build.VERSION.SDK_INT >= 16){
            // Do something for lollipop and above versions

            if(canvasView!=null) {
                Canvas canvas=new Canvas();

                msgListView.setBackgroundResource(R.drawable.welcome);

                msgListView.setBackground(new BitmapDrawable(getResources(),canvasView.getBitmap()));
            }
        } else{
            // do something for phones running an SDK before lollipop
        }

    }


    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {

            Conversation conversation=new Conversation();

            conversation.isMe=true;
            conversation.date= Calendar.getInstance().getTime();

           Person with=new Select().from(Person.class).where("id="+this.with).executeSingle();

            conversation.with=with;
            conversation.message=message;
            conversation.isSent=false;
            conversation.isDraw=false;
            conversation.isDelivered=false;
            conversation.isMe=true;
            conversation.save();
            chatAdapter.add(conversation);
            chatAdapter.notifyDataSetChanged();
            msg_edittext.setText("");

           // msgListView.scrollTo();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);

        }



    }

    @Override
    public void onPause() {
        super.onPause();

        isCancel=true;

         getMesseges.cancel(true);

    }

    @Override
    public void onResume() {
        super.onResume();

        isCancel=false;

        getMesseges=new GetMesseges();

        getMesseges.execute();
    }

     class GetMesseges extends AsyncTask<Void,List<Conversation>,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i("preExecute",with+"");

            List<Conversation> list=new  Select().from(Conversation.class).execute();

            for(Conversation x:list)
            {
                Log.i("conversation",x.message+"  "+x.with+"  "+x.msg_id+"  "+x.date+"  "+x.isDelivered+"  isme "+x.isMe+" issent  "+x.isSent);
            }
        }

        @Override
        protected void onProgressUpdate(List<Conversation> ...values) {
            super.onProgressUpdate(values);

            chatAdapter.clear();
            
            int i=-1;
            for (Conversation x:values[0]) {
                i++;
                chatAdapter.add(x);
                Log.i("each msg",x.with.user_id+"");
            }
                    chatAdapter.notifyDataSetChanged();
        }



        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancel) {

                List<Conversation> conversations = new Select().from(Conversation.class).where("with="+with).and("isDraw=0")
                        .execute();

                publishProgress(conversations);

                try {
                    Thread.sleep(30);

                }catch (InterruptedException ie)
                {
                    Log.e("CHat Activity",""+ie);
                }
            }
            return null;
        }
    }

}

