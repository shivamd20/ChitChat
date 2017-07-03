package io.hasura.shivam.chitchat.frags;

        import java.util.ArrayList;
        import java.util.Random;

        import android.annotation.TargetApi;
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListView;

        import com.android.graphics.CanvasView;

        import io.hasura.shivam.chitchat.R;
        import io.hasura.shivam.chitchat.frags.mChat.ChatAdapter;
        import io.hasura.shivam.chitchat.frags.mChat.ChatMessage;
        import io.hasura.shivam.chitchat.frags.mChat.CommonMethods;


public class Chats extends Fragment implements OnClickListener {

    private EditText msg_edittext;
    private String user1 = "khushi", user2 = "khushi1";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    CanvasView canvasView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        random = new Random();
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
//                "Chats");
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);

        drawBackground();

        return view;
    }


    @TargetApi(16)
    void drawBackground(){

        if (android.os.Build.VERSION.SDK_INT >= 16){
            // Do something for lollipop and above versions

            if(canvasView!=null) {
                Canvas canvas=new Canvas();
                msgListView.setBackground(new BitmapDrawable(getResources(),canvasView.getBitmap()));
            }
        } else{
            // do something for phones running an SDK before lollipop
        }

    }


    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, user2,
                    message, "" + random.nextInt(1000), false);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);

        }
    }

}

