package com.data_management.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * Created by Ahndroid-3 on 21-11-2017.
 */

public class Utils {

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static int checkamount(double amount, double balance) {

        if (balance < 50) {
            return 0;
        } else if (balance < amount) {
            return 1;
        } else {
            return 2;
        }
    }
    public static double getToatalAmount(int qty,double amount){
        return qty*amount;

    }

    public static long generateNumber() {
        return (long) (Math.random() * 100000 + 3333300000L);
    }

    public static float dpToPx(float dp) {
        return  (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
