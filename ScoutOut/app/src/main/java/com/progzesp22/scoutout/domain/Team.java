package com.progzesp22.scoutout.domain;

import org.json.JSONObject;

import java.util.List;

public class Team extends Entity{

    protected long id;
    protected long gameId;
    protected String name;
    protected String creator;
    protected List<String> members;

    public Team(long id, long gameId, String name, String creator, List<String> members) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.creator = creator;
        this.members = members;
    }

    public static Team fromJson(JSONObject json) {
        return new Team(json.optLong("id"),
                json.optLong("gameId"),
                json.optString("name"),
                json.optString("creator"),
                null); // TODO: parse members
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
