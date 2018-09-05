package com.bulunduc.todosha.pickers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.bulunduc.todosha.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends PickerFragment {
    private DatePicker datePicker;
    public static DatePickerFragment newInstance(Date date){
        Bundle args = getArgs(date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View initLayout(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        return view;
    }

    public Date getDate() {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return new GregorianCalendar(year, month, day, hour, minutes).getTime();
    }

}
