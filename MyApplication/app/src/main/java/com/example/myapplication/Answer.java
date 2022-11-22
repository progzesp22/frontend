package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
    private final long id;
    private final String answer;
    private final long taskId;
    private final long userId;
    private boolean approved;
    private boolean checked;

    public Answer(long id, String answer, long taskId, long userId, boolean approved, boolean checked) {
        this.id = id;
        this.answer = answer;
        this.taskId = taskId;
        this.userId = userId;
        this.approved = approved;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getUserId() {
        return userId;
    }

    static public Answer fromJson(JSONObject json) throws JSONException {
        return new Answer(
                json.getInt("id"),
                json.getString("response"),
                json.getInt("taskId"),
                json.getInt("userId"),
                json.getBoolean("approved"),
                json.getBoolean("checked"));
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
