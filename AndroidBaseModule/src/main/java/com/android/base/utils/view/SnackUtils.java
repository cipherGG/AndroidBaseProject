package com.android.base.utils.view;

import android.support.design.widget.Snackbar;
import android.view.View;

import static android.R.attr.action;

/**
 * Created by gg on 2017/4/3.
 * Snack
 */
public class SnackUtils {

    private void aaa(View view, String show, String action, View.OnClickListener listener){
        Snackbar.make(view, show, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }
}
