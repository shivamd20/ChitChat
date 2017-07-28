package io.hasura.shivam.chitchat.frags.mChat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Conversation;

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    List<Conversation> chatMessageList;

    public List<Conversation> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<Conversation> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public void clear()
    {
        chatMessageList.clear();
    }

    public ChatAdapter(Activity activity, List<Conversation> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation message = (Conversation) chatMessageList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chatbubble, null);

        TextView time=(TextView) vi.findViewById(R.id.timeMsgView) ;

        TextView isSent=(TextView)vi.findViewById(R.id.isSentMessege);

        time.setText(printDifference(message.date,Calendar.getInstance().getTime()));


        if(!message.isSent)
        isSent.setText("sending");
        else
        {
            isSent.setText("sent");
        }


        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        msg.setText(message.message);
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (message.isMe) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }
    public String printDifference(Date startDate, Date endDate){

        StringBuilder sb=new StringBuilder();

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays>1)
        {
            return elapsedDays+" days ago";
        }
        if(elapsedHours>1)
        {
            return  elapsedHours+" hours ago";
        }
        else if(elapsedMinutes>1)
        {
            return elapsedMinutes+" minutes ago";
        }
        else {
            return elapsedSeconds+" seconds ago";
        }

    }


    public void add(Conversation object) {
        chatMessageList.add(object);
    }
}