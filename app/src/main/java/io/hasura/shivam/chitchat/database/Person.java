package io.hasura.shivam.chitchat.database;

/**
 * Created by shivam on 3/7/17.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;



@Table(name = "Person")
public class Person extends Model {

    @Column(name = "mobile", unique = true,notNull = true)
    public String mobile;


    @Column(name = "profile_pic")
    public byte[] profile_pic;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Person(){
        super();
    }

    public Person(String mobile,byte[] profile_pic)
    {
        super();

        this.mobile=mobile;

        this.profile_pic=profile_pic;

    }

}