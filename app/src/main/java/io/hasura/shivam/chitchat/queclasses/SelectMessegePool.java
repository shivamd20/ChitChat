package io.hasura.shivam.chitchat.queclasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 18/7/17.
 */

//TODO Do Not use it anywhere
public class SelectMessegePool {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "messege_pool";

        @SerializedName("columns")
        String[] columns = {"*"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        
        @SerializedName("to")
        Integer userId;
    }

    public SelectMessegePool(Integer userId){
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }
}
