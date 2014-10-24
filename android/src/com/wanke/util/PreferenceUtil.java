package com.wanke.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.wanke.WankeTVApplication;

public class PreferenceUtil {

    private final static String PREFERENCE_NAME = "config";

    public final static String KEY_USERNAME = "username";
    public final static String KEY_PASSWORD = "password";
    public final static String KEY_UID = "uid";
    public final static String KEY_AVATAR = "avatar";

    public static void saveAccountInfo(
            String username, String password, String uid, String avatar) {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = sp.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_AVATAR, avatar);
        editor.commit();
    }

    public static String getUsername() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getString(KEY_USERNAME, null);
    }

    public static String getPassword() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getString(KEY_PASSWORD, null);
    }

    public static String getAvatar() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getString(KEY_AVATAR, null);
    }

    public static String getUid() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getString(KEY_UID, null);
    }

    public static void registerPreferencesListener(
            OnSharedPreferenceChangeListener listener) {
        if (listener != null) {
            SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                    .getSharedPreferences(PREFERENCE_NAME, 0);
            sp.registerOnSharedPreferenceChangeListener(listener);
        }
    }

    public static void unregisterPreferencesListener(
            OnSharedPreferenceChangeListener listener) {
        if (listener != null) {
            SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                    .getSharedPreferences(PREFERENCE_NAME, 0);
            sp.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

    public static void clearAccount() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = sp.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_AVATAR);
        editor.remove(KEY_UID);
        editor.commit();
    }

    private final static String KEY_DECODE_BY_HARDWARE = "decode_by_hardware";

    public static boolean getDecodeByHardware() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getBoolean(KEY_DECODE_BY_HARDWARE, false);
    }

    public static void setDecodeByHardware(boolean use) {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = sp.edit();
        editor.putBoolean(KEY_DECODE_BY_HARDWARE, use);
        editor.commit();
    }

    private final static String KEY_AUTO_PLAY_IN_23G = "auto_play_in_23g";

    public static boolean getAutoPlayIn23G() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getBoolean(KEY_AUTO_PLAY_IN_23G, false);
    }

    public static void setAutoPlayIn23G(boolean use) {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = sp.edit();
        editor.putBoolean(KEY_AUTO_PLAY_IN_23G, use);
        editor.commit();
    }

    private final static String KEY_DANMAKU_SETTING_ALPHA = "danmaku_setting_alpha";

    public static int getDanmakuAlpha() {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        return sp.getInt(KEY_DANMAKU_SETTING_ALPHA, 0xFF);
    }

    public static void setDanmakuAlpha(int alpha) {
        SharedPreferences sp = WankeTVApplication.getCurrentApplication()
                .getSharedPreferences(PREFERENCE_NAME, 0);
        Editor editor = sp.edit();
        editor.putInt(KEY_DANMAKU_SETTING_ALPHA, alpha);
        editor.commit();
    }
}
