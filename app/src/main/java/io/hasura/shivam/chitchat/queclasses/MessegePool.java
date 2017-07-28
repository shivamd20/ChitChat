package io.hasura.shivam.chitchat.queclasses;


import java.util.Date;

/**
 * Created by shivam on 19/7/17.
 */

public class MessegePool {

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    long msg_id;
    long from;
    long to;
    String msg;
    Date timestamp;
    boolean isDraw;
    boolean isDelivered;

}
