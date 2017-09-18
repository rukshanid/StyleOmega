package com.example.rukshani.styleomega;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
/**
 * Created by Rukshani on 10/08/2017.
 */
public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();

    }

    public String getusername() {
        String username = prefs.getString("username","");
        return username;
    }

    public void removeUsername(String username) {
        prefs.edit().remove("username").commit();
        prefs.edit().clear();
    }
}
