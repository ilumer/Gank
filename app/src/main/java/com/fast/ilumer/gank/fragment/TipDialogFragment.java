package com.fast.ilumer.gank.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by root on 11/26/16.
 */

public class TipDialogFragment  extends DialogFragment{
    private static final String EXTRA_TITLE = "DialogFragment_TITLE";
    private String content;
    public static TipDialogFragment newInstance(String title){
        TipDialogFragment fragment = new TipDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString(EXTRA_TITLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(content);
        // Create the AlertDialog object and return i
        return builder.create();
    }
}
