package com.bulunduc.todosha;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_IS_ALARM_ON = "isAlarmOn";
    private static final String JSON_ALARM_DATE = "alarmDate";

    private UUID id;
    private String title;
    private String description;
    private Boolean isAlarmOn;
    private Date alarmDate;

    public Task() {
        this.id = UUID.randomUUID();
        this.title = "";
        this.description = "";
        this.isAlarmOn = false;
        this.alarmDate = new Date();
    }

    public Task(JSONObject jsonObject) throws JSONException {
        id = UUID.fromString(jsonObject.getString(JSON_ID));
        title = jsonObject.getString(JSON_TITLE);
        description = jsonObject.getString(JSON_DESCRIPTION);
        isAlarmOn = jsonObject.getBoolean(JSON_IS_ALARM_ON);
        alarmDate = new Date(jsonObject.getLong(JSON_ALARM_DATE));
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, id.toString());
        jsonObject.put(JSON_TITLE, title.toString());
        jsonObject.put(JSON_DESCRIPTION, description.toString());
        jsonObject.put(JSON_IS_ALARM_ON, isAlarmOn);
        jsonObject.put(JSON_ALARM_DATE, alarmDate.getTime());
        return jsonObject;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAlarmOn() {
        return isAlarmOn;
    }

    public void setIsAlarmOn(Boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date date) {
        this.alarmDate = date;
    }

    public boolean isTaskNew(){
        if (this.title.equals("")) return true;
        return false;
    }
    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(id, task.id) &&
                    Objects.equals(title, task.title) &&
                    Objects.equals(description, task.description) &&
                    Objects.equals(isAlarmOn, task.isAlarmOn) &&
                    Objects.equals(alarmDate, task.alarmDate);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(id, title, description, isAlarmOn, alarmDate);
        }
        return 0;
    }
}
