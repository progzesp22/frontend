package com.progzesp22.scoutout.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Game extends Entity{

    protected long id;
    protected String name = "";
    protected String description = "";
    protected String gameMaster = "";
    protected GameState state;
    protected Date startTime;
    protected Date endTime;
    protected EndCondition endCondition = EndCondition.MANUAL;
    protected long endScore;

    static public final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());

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

        game.setDescription(json.optString("description", ""));

        try{
            if (json.has("startTime") && !json.isNull("startTime")) {
                game.setStartTime(dateFormat.parse(json.getString("startTime")));

            }

            if (json.has("endCondition") && !json.isNull("endCondition")) {
                EndCondition endCondition = EndCondition.valueOf(json.getString("endCondition"));
                game.setEndCondition(endCondition);
                if (endCondition == EndCondition.TIME) {
                    game.setEndTime(dateFormat.parse(json.getString("endTime")));
                } else if (endCondition == EndCondition.SCORE) {
                    game.setEndScore(json.getLong("endScore"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeString(){
        if (startTime == null) return "";
        return dateFormat.format(startTime);
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public String getDescription(){return description;}

    public void setDescription(String description){this.description= description;}

    public String getEndTimeString() {
        return dateFormat.format(endTime);
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

    public void updateFrom(Game other){
        this.name = other.name;
        this.description = other.description;
        this.gameMaster = other.gameMaster;
        this.state = other.state;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.endCondition = other.endCondition;
        this.endScore = other.endScore;
    }

}
