package io.hasura.shivam.chitchat.queclasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 5/6/17.
 */


public class PersonDetails {
    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;
    //byte[] picture;

    @SerializedName("user_id")
    long user_id;

    @SerializedName("profie_pic")
    String profie_pic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getProfile_pic() {
        return profie_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profie_pic = profile_pic;
    }

    public PersonDetails(){

    }
}
