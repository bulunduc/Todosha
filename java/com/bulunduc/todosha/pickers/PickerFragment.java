package com.bulunduc.todosha.pickers;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.bulunduc.todosha.R;

import java.util.Calendar;
import java.util.Date;

public abstract class PickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.bulunduc.todosha.date";
    public static final String ARG_DATE = "date";

    public Calendar calendar;

    public abstract View initLayout();
    public abstract Date getDate();


    public static Bundle getArgs(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        return args;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        calendar = Calendar.getInstance();
        calendar.setTime(date);


        View view = initLayout();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = getDate();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }
    public void sendResult(int resultCode, Date date){
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
