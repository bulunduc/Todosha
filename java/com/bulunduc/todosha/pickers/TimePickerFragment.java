package com.bulunduc.todosha.pickers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.bulunduc.todosha.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends PickerFragment {
    private TimePicker timePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = getArgs(date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View initLayout() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        timePicker = (TimePicker) view.findViewById(R.id.dialog_time_timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        return view;
    }

    @Override
    public Date getDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();
        return new GregorianCalendar(year, month, day, hour, minutes, 0).getTime();
    }
}
