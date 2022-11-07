package com.example.myapplication;

import android.util.Log;

import org.json.JSONObject;

public class Answer {
    private final int id;
    private final String answer;
    private final int taskId;
    //    private final int userId;
    private boolean approved;
    private boolean checked;

    public Answer(int id, String answer, int taskId, /*int userId,*/ boolean approved, boolean checked) {
        this.id = id;
        this.answer = answer;
        this.taskId = taskId;
//        this.userId = userId;
        this.approved = approved;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public int getTaskId() {
        return taskId;
    }

//    public int getUserId() {
//        return userId;
//    }

    static public Answer fromJson(JSONObject json) {
        try {
            return new Answer(
                    json.getInt("id"),
                    json.getString("response"),
                    json.getInt("taskId"),
                    // json.getInt("userId"),
                    json.getBoolean("approved"),
                    json.getBoolean("checked"));
        } catch (Exception e) {
            Log.e("Answer", "Parsing error: " + e.getMessage());
            return null;
        }
    }
}
