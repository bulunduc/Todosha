package com.bulunduc.todosha;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bulunduc.android_swipe_to_dismiss_undo.SwipeToDismissTouchListener;
import com.bulunduc.android_swipe_to_dismiss_undo.adapter.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskListFragment extends ListFragment {
    private ArrayList<Task> tasks;
    private ArrayList<Task> searchTasks;
    ViewGroup textViewGroup;

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
                                TaskLab.get(getActivity()).saveTasks();
                                ((TaskAdapter)getListAdapter()).notifyDataSetChanged();

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

    private static class ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView alarmDateTextView;
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(ArrayList<Task> tasks) {
            super(getActivity(), 0, tasks);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Task task = getItem(position);
            View view = convertView;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.list_item_task, null);
                viewHolder.titleTextView = (TextView) view.findViewById(R.id.task_list_item_titleTextView);
                viewHolder.descriptionTextView = (TextView) view.findViewById(R.id.task_list_item_descriptionTextView);
                viewHolder.alarmDateTextView = (TextView) view.findViewById(R.id.task_list_item_alarmDateTextView);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.titleTextView.setText(task.getTitle());
            viewHolder.descriptionTextView.setText(task.getDescription());
            if (task.getIsAlarmOn()) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                viewHolder.alarmDateTextView.setText(dt.format(task.getAlarmDate()));
            } else {
                viewHolder.alarmDateTextView.setText(R.string.task_alarm_off);
            }

            if (TaskLab.get(getActivity()).getLatestTasks().contains(task))
            {
                view.setBackgroundColor(getResources().getColor(R.color.colorLatest));
            } else if (TaskLab.get(getActivity()).getNearestTasks().contains(task))
            {
                view.setBackgroundColor(getResources().getColor(R.color.colorNearest));
            } else view.setBackgroundColor(getResources().getColor(R.color.colorListView));

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                ArrayList<Task> filterTasks = new ArrayList<>();// this is your adapter that will be filtered
                for (Task task : tasks) {
                    if (task.getTitle().toLowerCase().startsWith(newText.toLowerCase())) {
                        filterTasks.add(task);
                    }
                }
                TaskAdapter taskArrayAdapter = new TaskAdapter(filterTasks);
                setListAdapter(taskArrayAdapter);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);


        searchManager.setOnCancelListener(new SearchManager.OnCancelListener() {
            @Override
            public void onCancel() {
                tasks = TaskLab.get(getActivity()).getTasks();
                TaskAdapter taskArrayAdapter = new TaskAdapter(tasks);
                setListAdapter(taskArrayAdapter);
            }
        });


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
