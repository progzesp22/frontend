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
    private long maxScore = 0;
    protected String correctAnswer;

    public Task(long id, String name, String description, long gameId, TaskType type, long maxScore,  List<Task> prerequisites, String correctAnswer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameId = gameId;
        this.type = type;
        this.prerequisites = prerequisites;
        this.answers = new ArrayList<>();
        this.maxScore = maxScore;
        this.correctAnswer = correctAnswer;
    }

    public Task(Task other) {
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.gameId = other.gameId;
        this.type = other.type;
        this.prerequisites = new ArrayList<>(other.prerequisites);
        this.maxScore = other.maxScore;
        this.answers = new ArrayList<>(other.answers);
        this.correctAnswer = other.correctAnswer;
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

    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getCorrectAnswer() { return correctAnswer; }
    static public Task fromJson(JSONObject json) throws JSONException {
        return new Task(json.getInt("id"),
                json.getString("name"),
                json.getString("description"),
                json.getInt("gameId"),
                TaskType.valueOf(json.getString("type")),
                json.getLong("maxScore"),
                new ArrayList<>(),
                json.getString("correct_answer")); // TODO: parse prerequisites
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

    public void setMaxScore(long maxScore) {
        this.maxScore = maxScore;
    }

    public long getMaxScore(){
        return maxScore;
    }

    public enum TaskType {
        TEXT,
        PHOTO,
        QR_CODE,
        NAV_POS,
        AUDIO
    }

    public void updateFrom(Task other){
        this.name = other.name;
        this.description = other.description;
        this.type = other.type;
        this.maxScore = other.maxScore;
        this.correctAnswer = other.correctAnswer;
    }


}
