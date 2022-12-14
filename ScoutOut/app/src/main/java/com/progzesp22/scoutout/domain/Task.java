package com.progzesp22.scoutout.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Task extends Entity{


    protected final long id;
    protected String name;
    protected String description;
    protected TaskType type;
    protected final List<Task> prerequisites;
    protected final List<Answer> answers;
    protected final long gameId;

    public Task(long id, String name, String description, long gameId, TaskType type, List<Task> prerequisites) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameId = gameId;
        this.type = type;
        this.prerequisites = prerequisites;
        this.answers = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    static public Task fromJson(JSONObject json) throws JSONException {
        return new Task(json.getInt("id"),
                json.getString("name"),
                json.getString("description"),
                json.getInt("gameId"),
                TaskType.valueOf(json.getString("type")),
                new ArrayList<>()); // TODO: parse prerequisites
    }

    public long getGameId() {
        return gameId;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public List<Task> getPrerequisiteTasks() {
        return prerequisites;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isFinished() {
        for (Answer answer : answers) {
            if (answer.isApproved()) {
                return true;
            }
        }

        return false;
    }

    public enum TaskType {
        TEXT,
        PHOTO,
        QR_CODE,
        NAV_POS,
        AUDIO
    }


}
