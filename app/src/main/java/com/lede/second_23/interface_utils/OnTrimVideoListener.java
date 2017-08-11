package com.lede.second_23.interface_utils;

import android.net.Uri;

public interface OnTrimVideoListener {

    void onStartTrim();

    void onFinishTrim(final Uri uri);

    void onCancel();
}
