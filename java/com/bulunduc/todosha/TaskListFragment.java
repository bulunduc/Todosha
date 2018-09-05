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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bulunduc.android_swipe_to_dismiss_undo.SwipeToDismissTouchListener;
import com.bulunduc.android_swipe_to_dismiss_undo.adapter.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskListFragment extends ListFragment {
    private ArrayList<Task> tasks;

    private static final String TAG = "TaskListFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tasks = TaskLab.get(getActivity()).getTasks();
        TaskAdapter taskArrayAdapter = new TaskAdapter(tasks);
        setListAdapter(taskArrayAdapter);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_list_tasks, null);
        ((ViewGroup)getListView().getParent()).addView(emptyView);
        getListView().setEmptyView(emptyView);

        ListView listView = getListView();
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(ListViewAdapter recyclerView, int position) {

                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                TaskLab.get(getActivity()).deleteTask(tasks.get(position));
                            }
                        });
// Dismiss the item automatically after 3 seconds
        touchListener.setDismissDelay(3000);

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Task task = ((TaskAdapter) getListAdapter()).getItem(position);
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
                    startActivity(intent);
                }
            }
        });
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
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
