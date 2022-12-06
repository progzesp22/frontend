package com.progzesp22.scoutout.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Game extends Entity{

    protected long id;
    protected String name;
    protected String gameMaster;
    protected GameState state;
    protected Timestamp startTime;
    protected Timestamp endTime;
    protected EndCondition endCondition;
    protected long endScore;

    public Game(long id, String name, String gameMaster, GameState state) {
        this.id = id;
        this.name = name;
        this.gameMaster = gameMaster;
        this.state = state;
    }

    public static Game fromJson(JSONObject json) throws JSONException {
        String name = "<no name>";
        String gameMaster = "<no GM>";

        if(json.has("name")){
            name = json.getString("name");
        }

        if(json.has("gameMaster")){
            gameMaster = json.getString("gameMaster");
        }

        Game game = new Game(json.getLong("id"),
                name,
                gameMaster,
                GameState.valueOf(json.getString("state")));

        if (json.has("startTime")) {
            game.setStartTime(Timestamp.valueOf(json.getString("startTime")));
        }

        if (json.has("endCondition")) {
            EndCondition endCondition = EndCondition.valueOf(json.getString("endCondition"));
            game.setEndCondition(endCondition);
            if (endCondition == EndCondition.TIME) {
                game.setEndTime(Timestamp.valueOf(json.getString("endTime")));
            } else if (endCondition == EndCondition.SCORE) {
                game.setEndScore(json.getLong("endScore"));
            }
        }

        return game;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameMaster() {
        return gameMaster;
    }

    public void setGameMaster(String gameMaster) {
        this.gameMaster = gameMaster;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public EndCondition getEndCondition() {
        return endCondition;
    }

    public void setEndCondition(EndCondition endCondition) {
        this.endCondition = endCondition;
    }

    public long getEndScore() {
        return endScore;
    }

    public void setEndScore(long endScore) {
        this.endScore = endScore;
    }

    public enum GameState {
        CREATED,
        PENDING,
        STARTED,
        FINISHED
    }

    public enum EndCondition {
        MANUAL,
        TIME,
        SCORE,
        TASKS
    }

}
