package com.progzesp22.scoutout.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer extends Entity{
    protected final long id;
    protected final String answer;
    protected final long taskId;
    protected final long userId;
    protected boolean approved;
    protected boolean checked;

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
        // temporary fix for null userId and response in json
        long userId;
        String response;

        try {
            userId = json.getInt("userId");
        } catch (JSONException e) {
            userId = -1;
        }

        try {
            response = json.getString("response");
        } catch (JSONException e) {
            response = "";
        }

        return new Answer(
                json.getInt("id"),
                response,
                json.getInt("taskId"),
                userId,
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
