package com.bulunduc.todosha;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
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

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public boolean saveTasks(){
        try{
            todoshaJSONSerializer.saveTasks(tasks);
            Log.d(TAG, "tasks saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "error saving tasks:" + e);
            return false;
        }
    }
}
