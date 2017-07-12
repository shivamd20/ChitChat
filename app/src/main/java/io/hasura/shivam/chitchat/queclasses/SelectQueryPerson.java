package io.hasura.shivam.chitchat.queclasses;

import com.google.gson.annotations.SerializedName;


public class SelectQueryPerson {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "person";

        @SerializedName("columns")
        String[] columns = {"name","user_id","profie_pic","mobile"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public SelectQueryPerson(Integer userId){
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }
}
