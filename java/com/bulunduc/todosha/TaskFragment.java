package com.bulunduc.todosha;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bulunduc.todosha.pickers.DatePickerFragment;
import com.bulunduc.todosha.pickers.PickerFragment;
import com.bulunduc.todosha.pickers.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TaskFragment extends Fragment implements View.OnClickListener{
    public static final String EXTRA_TASK_ID = "com.bulunduc.todosha.task_id";
    public static final String DIALOG_DATE = "date";
    public static final int REQUEST_DATE = 0;
    private Task task;
    private EditText titleField;
    private EditText descriptionField;
    private Switch alarmOnSwitch;
    private Button dateButton;
    private Button timeButton;
    private Button addUpdateTask;
    private Date tempDate = new Date();

    public static TaskFragment newInstance(UUID taskId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TASK_ID, taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(args);
        return taskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(EXTRA_TASK_ID);
        task = TaskLab.get(getActivity()).getTask(taskId);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        initLayoutItems(view);
        titleField.setText(task.getTitle());

        descriptionField.setText(task.getDescription());

        alarmOnSwitch.setChecked(task.getIsAlarmOn());
        alarmOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDateTimeButtons(isChecked);
            }
        });

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        updateDateTimeButtons(alarmOnSwitch.isChecked());

        if (task.isTaskNew()) {
            addUpdateTask.setText(R.string.add_task);
        }
        else {
            addUpdateTask.setText(R.string.update_task);
        }
        addUpdateTask.setOnClickListener(this);
        return view;
    }

    private void initLayoutItems(View view){
        titleField = (EditText) view.findViewById(R.id.task_title);
        descriptionField = (EditText) view.findViewById(R.id.task_description);
        alarmOnSwitch = (Switch) view.findViewById(R.id.task_alarm_set);
        dateButton = (Button) view.findViewById(R.id.task_date);
        timeButton = (Button) view.findViewById(R.id.task_time);
        addUpdateTask = (Button) view.findViewById(R.id.add_update_task);
    }


    private void updateDateTimeButtons(boolean isAlarmOn) {
        setVisibilityDateButtons(isAlarmOn);
        if (isAlarmOn) updateDateButtonsText(task.getAlarmDate());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AppCompatActivity.RESULT_OK) return;
        tempDate = (Date) data.getSerializableExtra(PickerFragment.EXTRA_DATE);
        updateDateButtonsText(tempDate);
    }

    private void updateDateButtonsText(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateButton.setText(dateFormat.format(date));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeButton.setText(timeFormat.format(date));
    }

    private void setVisibilityDateButtons(boolean visible){
        if (visible){
            dateButton.setVisibility(View.VISIBLE);
            timeButton.setVisibility(View.VISIBLE);
        }else {
            dateButton.setVisibility(View.GONE);
            timeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskLab.get(getActivity()).saveTasks();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        switch (v.getId()){
            case R.id.task_date:
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(tempDate);
                dateDialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);
                dateDialog.show(fragmentManager, DIALOG_DATE);
                break;
            case R.id.task_time:
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(tempDate);
                timeDialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);
                timeDialog.show(fragmentManager, DIALOG_DATE);
                break;
            case R.id.add_update_task:
                if (titleField.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.title_is_empty), Toast.LENGTH_LONG).show();
                    break;
                }
                if (alarmOnSwitch.isChecked() && tempDate.getTime() < System.currentTimeMillis()){
                    Toast.makeText(getActivity(), getResources().getText(R.string.date_is_old), Toast.LENGTH_LONG).show();
                    break;
                }
                task.setTitle(titleField.getText().toString());
                task.setDescription(descriptionField.getText().toString());
                task.setIsAlarmOn(alarmOnSwitch.isChecked());
                task.setAlarmDate(tempDate);
                Intent intent = new Intent(getActivity(), TaskListActivity.class);
                startActivity(intent);
                break;
        }

    }
}

