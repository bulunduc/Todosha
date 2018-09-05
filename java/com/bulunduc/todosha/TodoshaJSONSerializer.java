package com.bulunduc.todosha;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class TodoshaJSONSerializer {
    private Context context;
    private String fileName;

    public TodoshaJSONSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveTasks(ArrayList<Task> tasks) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks) {
            jsonArray.put(task.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(jsonArray.toString());
        }finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Task> loadTasks() throws IOException, JSONException{
        ArrayList<Task> tasks = new ArrayList<Task>();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                tasks.add(new Task(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){}
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return tasks;
    }
}
