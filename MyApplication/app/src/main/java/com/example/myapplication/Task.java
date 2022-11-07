package com.example.myapplication;

import org.json.JSONObject;

public class Task {
    private final int id;
    private final String name;
    private final String description;

    public Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    static public Task fromJson(JSONObject json){
        try {
            return new Task(json.getInt("id"), json.getString("name"), json.getString("description"));
        } catch (Exception e){
            return null;
        }
    }
}
