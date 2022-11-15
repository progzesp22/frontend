package com.example.myapplication;

import org.json.JSONObject;

import java.util.ArrayList;

public class Task {

    public static final long UNNOWN_ID = Long.MIN_VALUE;

    private final long id;
    private String name;
    private String description;
    private TaskType type;
    private final ArrayList<Task> prerequisites;
    private final long gameId;

    public Task(long id, String name, String description, long gameId, TaskType type, ArrayList<Task> prerequisites) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameId = gameId;
        this.type = type;
        this.prerequisites = prerequisites;
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

    static public Task fromJson(JSONObject json) {
        try {
            return new Task(json.getInt("id"),
                    json.getString("name"),
                    json.getString("description"),
                    json.getInt("gameId"),
                    TaskType.valueOf(json.getString("type")),
                    null); // TODO: parse prerequisites
        } catch (Exception e) {
            return null;
        }
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

    public ArrayList<Task> getPrerequisiteTasks() {
        return prerequisites;
    }

    public enum TaskType {
        TEXT,
        PHOTO,
        QR_CODE,
        NAV_POS,
        AUDIO
    }
}
