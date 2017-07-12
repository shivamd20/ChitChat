package io.hasura.shivam.chitchat.queclasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 19/6/17.
 */

public class InsertQueryPerson {
    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "person";

        @SerializedName("objects")
        List<PersonDetails> objects;
    }

    public InsertQueryPerson(PersonDetails personDetails){
        args = new Args();
        args.objects = new ArrayList<>();
        args.objects.add(personDetails);
    }
}
