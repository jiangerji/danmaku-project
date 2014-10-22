package com.wanke.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class RegexUtil {

    private static final String EmailRegEx =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    public static boolean isValidEmail(String email) {
        boolean result = false;

        if (!TextUtils.isEmpty(email)) {
            Matcher matcherObj = Pattern.compile(EmailRegEx).matcher(email);

            if (matcherObj.matches()) {
                result = true;
            }
        }

        return result;
    }
}
