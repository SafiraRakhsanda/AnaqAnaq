package com.example.anaqanaq;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "introslider";

    private static final String ITS_FIRST_TIME_LAUNCH = "ItsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setFirstTimeLaunch(boolean itsFirstTime) {
        editor.putBoolean(ITS_FIRST_TIME_LAUNCH, itsFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(ITS_FIRST_TIME_LAUNCH, true);
    }

}
