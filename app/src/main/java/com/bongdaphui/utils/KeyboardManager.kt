package com.bongdaphui.utils

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

object KeyboardManager {

    fun hideSoftKeyboard(activity: AppCompatActivity) {

        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        if (activity.currentFocus != null) {

            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    /* public static void showSoftKeyboardWithFocus(AppCompatActivity activity, EditText editText) {
        editText.post(() -> {
            editText.requestFocus();
            InputMethodManager imgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imgr != null) {
                imgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }*/
}
