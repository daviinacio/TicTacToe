package com.daviapps.tictactoe.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class ErrorDialog {
    public static void show(Context context, Class c, Exception ex){
        ErrorDialog.show(context, c, ex, null);
    }

    public static void show(Context context, Class c, Exception ex, final String annotation){
        ErrorDialog.show(context,
                String.format("%s [%s]", c.getName(), ex.getClass().getName()),
                String.format(annotation != null ? "%s" : "%s\nannotation: %s", ex.getMessage(), annotation != null ? annotation : "")
        );
    }

    public static void show(Context context, int title, int message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void show(Context context, String title, int message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void show(Context context, int title, String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void show(Context context, String title, String message){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
    }
}
