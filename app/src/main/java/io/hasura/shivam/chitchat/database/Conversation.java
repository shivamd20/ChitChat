package io.hasura.shivam.chitchat.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Date;

/**
 * Created by shivam on 3/7/17.
 */

@Table(name = "conversation")
public class Conversation extends Model {

    @Column(name = "time_date")
    private Date date;

    @Column(name = "message", notNull = true)
    private String message;

    @Column(name = "isMe", notNull = true)
    private boolean isMe;

    @Column(name = "isDraw", notNull = true)
    private boolean isDraw;

    @Column(name = "isDelivered", notNull = true)
    private boolean isDelivered;

    @Column(name = "person", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Person with;

    @Column(name = "isSent",notNull = true)
    private boolean isSent;

    public Conversation(){
        super();
    }

    public Conversation( boolean isMe,boolean isDelivered,boolean isDraw, Person person,String message,Date date,boolean isSent){
        super();
        this.message= message;
        this.with = person;
        this.isDelivered=isDelivered;
        this.isDraw=isDraw;
        this.isMe=isMe;
        this.date=date;
        this.isSent=isSent;
    }
}