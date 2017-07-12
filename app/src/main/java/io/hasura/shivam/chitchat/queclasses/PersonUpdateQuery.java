package io.hasura.shivam.chitchat.queclasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 19/6/17.
 */

public class PersonUpdateQuery {
    @SerializedName("type")
    String type = "update";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "person";

        @SerializedName("$set")
        $Set set;

        @SerializedName("where")
        Where where;
    }

    class $Set{
        @SerializedName("name")
        String name;

        @SerializedName("mobile")
        String mobile;
        //byte[] picture;

        @SerializedName("profile_pic")
        String profile_pic;
    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public PersonUpdateQuery(PersonDetails personDetails){
        args = new Args();
        args.set = new $Set();
        args.set.name = personDetails.getName();
       // args.set.status = personDetails.getStatus();
        args.where = new Where();
      //  args.where.userId = personDetails.getId();
    }
}
