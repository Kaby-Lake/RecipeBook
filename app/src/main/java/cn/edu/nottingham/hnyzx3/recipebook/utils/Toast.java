package cn.edu.nottingham.hnyzx3.recipebook.utils;

import android.content.Context;

public class Toast {
    public static void showToastShort(Context context, String text) {
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String text) {
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG).show();
    }
}
