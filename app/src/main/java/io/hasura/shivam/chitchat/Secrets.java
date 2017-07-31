package io.hasura.shivam.chitchat;

import android.util.Log;

/**
 * Created by shivam on 1/8/17.
 */

public class Secrets {

    private static final String TAG = "SECRETS";

    public  static String getPassword(String str)
    {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<str.length();i++)
        {
            sb.append((char)(str.charAt(i)*7-i));
        }
        Log.e(TAG,sb.toString());
        return  sb.toString();
    }

    public static String getUserName(String str)
    {
        return  str+"@chitchat";
    }

}
