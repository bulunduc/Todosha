package com.bulunduc.todosha;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bulunduc.todosha.notifications.AlarmReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class TaskLab {
    private static final String TAG = "TaskLab";
    private static final String FILENAME = "tasks.json";

    private ArrayList<Task> tasks;
    private TodoshaJSONSerializer todoshaJSONSerializer;

    private static TaskLab taskLab;
    private Context appContext;

    private TaskLab(Context appContext){
        this.appContext = appContext;
        todoshaJSONSerializer = new TodoshaJSONSerializer(appContext, FILENAME);
        try {
            tasks = todoshaJSONSerializer.loadTasks();
        } catch (Exception e){
            tasks = new ArrayList<Task>();
            Log.e(TAG, "Error loading tasks: " + e);
        }
    }

    public static TaskLab get(Context context) {
        if (taskLab == null){
            taskLab = new TaskLab(context.getApplicationContext());
        }
        return taskLab;
    }

    public ArrayList<Task> getTasks() {
        sortTasks();
        return tasks;
    }

    public Task getTask(UUID id){
        for (Task task : tasks) {
            if (task.getId().equals(id))
                return task;

        }
        return null;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteEmptyTasks(){
        ArrayList<Task> emptyTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.isTaskNew()) emptyTasks.add(task);
        }
        tasks.removeAll(emptyTasks);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);

    }

    public boolean saveTasks(){
        deleteEmptyTasks();
        try{
            todoshaJSONSerializer.saveTasks(tasks);
            Log.d(TAG, "tasks saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "error saving tasks:" + e);
            return false;
        }
    }

    public void addAlarmForNextTask(){
        ArrayList<Task> nearestTasks = getNearestTasks();
        if (nearestTasks.size() > 0) {
            Bundle args = new Bundle();
            args.putSerializable(AlarmReceiver.REMINDER_DATE, nearestTasks.get(0).getAlarmDate().getTime());
            if (nearestTasks.size() > 1) {
                args.putSerializable(AlarmReceiver.REMINDER_TITLE, appContext.getString(R.string.several_task_notification));
            } else
                args.putSerializable(AlarmReceiver.REMINDER_TITLE, nearestTasks.get(0).getTitle());
            AlarmReceiver.restartNotify(appContext, args);
        }
    }

    @NonNull
    protected ArrayList<Task> getNearestTasks() {
        ArrayList<Task> nearestTasks = new ArrayList<Task>();
        Task nearestTask = new Task();
        for (Task task : tasks) {
            if (task.getIsAlarmOn() && task.getAlarmDate().getTime() > System.currentTimeMillis()){
                if (nearestTasks.size() == 0) nearestTask = task;
                switch (task.getAlarmDate().compareTo(nearestTask.getAlarmDate())){
                    case -1:
                        nearestTasks.clear();
                        nearestTasks.add(task);
                        break;
                    case 0:
                        nearestTasks.add(task);
                        break;
                    case 1:
                        break;
                }
            }
        }
        return nearestTasks;
    }
    
    protected ArrayList<Task> getLatestTasks() {
        ArrayList<Task> latestTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.getIsAlarmOn() && task.getAlarmDate().getTime() < System.currentTimeMillis()){
                latestTasks.add(task);
            }
        }
        return latestTasks;
    }
    
    protected void sortTasks(){
        ArrayList<Task> noAlarmDateTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.getIsAlarmOn()){
                noAlarmDateTasks.add(task);
            }
        }
        tasks.removeAll(noAlarmDateTasks);
        Collections.sort(tasks, Task.SORT_BY_DATE);
        Collections.sort(noAlarmDateTasks, Task.SORT_BY_TITLE);
        tasks.addAll(noAlarmDateTasks);
    }


}
