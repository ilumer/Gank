package com.fast.ilumer.gank.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.fast.ilumer.gank.model.GankDay;

/**
 * Created by ilumer on 2/8/17.
 * 选择不同日期的干货
 */

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{
    private static final String EXTRA_DATE =  "DatePickerDialogFragment.EXTRA_DATE";
    private onDataSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        GankDay day = getArguments().getParcelable(EXTRA_DATE);
        return new DatePickerDialog(getActivity(),this,day.getYear(),day.getMonth(),day.getDay());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //do on  BUTTON_POSITIVE
        listener.setDateSet(year,month,dayOfMonth);
    }

    public static DatePickerDialogFragment Instance(GankDay gankDay){
        Bundle arg = new Bundle();
        arg.putParcelable(EXTRA_DATE,gankDay);
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (onDataSetListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement onDataSetListener");
        }
    }

    public interface onDataSetListener{
        void setDateSet(int year,int month,int dayOfMonth);
    }

}
