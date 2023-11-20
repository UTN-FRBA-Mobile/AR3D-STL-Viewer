package com.example.practica.common.helpers;

import android.app.Activity;

import com.example.practica.R;

public class MensajesArCore {

    public static String getInsufficientLightMessage(Activity activity) {
        return activity.getString(R.string.insufficient_light_message);
    }

    public static String getInsufficientLightAndroidSMessage(Activity activity) {
        return activity.getString(R.string.insufficient_light_android_s_message);
    }

    public static String getExcessiveMotion(Activity activity) {
        return activity.getString(R.string.excessive_motion);
    }

    public static String getBadStateMessage(Activity activity) {
        return activity.getString(R.string.bad_state_message);
    }

    public static String getCameraUnavailableMessage(Activity activity) {
        return activity.getString(R.string.camera_unavailable_message);
    }

    public static String getInsufficienteFeaturesMessage(Activity activity) {
        return activity.getString(R.string.insufficient_features_message);
    }
}
