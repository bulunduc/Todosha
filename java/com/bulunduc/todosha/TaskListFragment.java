package com.bulunduc.todosha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskListFragment extends ListFragment {
    private ArrayList<Task> tasks;

    private static final String TAG = "TaskListFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(Html.fromHtml("<font color=\"black\">"+ getString(R.string.tasks_title) +"</font>"));
        tasks = TaskLab.get(getActivity()).getTasks();
        TaskAdapter taskArrayAdapter = new TaskAdapter(tasks);
        setListAdapter(taskArrayAdapter);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_list_tasks, null);
        ((ViewGroup)getListView().getParent()).addView(emptyView);
        FloatingActionButton addNewTaskButton = (FloatingActionButton) emptyView.findViewById(R.id.add_task);
        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                TaskLab.get(getActivity()).addTask(task);
                Intent intent = new Intent(getActivity(), TaskPagerActivity.class);
                intent.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
                startActivityForResult(intent, 0);
            }
        });

        getListView().setEmptyView(emptyView);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task task = ((TaskAdapter) getListAdapter()).getItem(position);
        Intent intent = new Intent(getActivity(), TaskPagerActivity.class);
        intent.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
        startActivity(intent);
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(ArrayList<Task> tasks) {
            super(getActivity(), 0, tasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, null);
            }
            Task task = getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.task_list_item_titleTextView);
            titleTextView.setText(task.getTitle());

            TextView alarmDateTextView = (TextView) convertView.findViewById(R.id.task_list_item_alarmDateTextView);

            if (task.getIsAlarmOn()) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                alarmDateTextView.setText(dt.format(task.getAlarmDate()));
            }
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter)getListAdapter()).notifyDataSetChanged();
        //toDo it will be eat time, need to change architecture with creating new task
        ArrayList<Task> emptyTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.isTaskNew()) emptyTasks.add(task);
        }
        tasks.removeAll(emptyTasks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
