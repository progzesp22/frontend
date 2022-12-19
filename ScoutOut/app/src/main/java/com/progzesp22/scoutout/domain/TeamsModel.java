package com.progzesp22.scoutout.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.progzesp22.scoutout.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TeamsModel extends ViewModel {
    private MutableLiveData<List<Team>> teams;
    private Team activeTeam;
    private Long previousGameId = -1L;

    private static final String TAG = "TeamsModel";


    public LiveData<List<Team>> getTeams(long gameId) {
        if (teams == null) {
            teams = new MutableLiveData<>();
            fetch(gameId);
        }
        return teams;
    }

    public void refresh(long gameId) {
        if (teams == null) {
            teams = new MutableLiveData<>();
        }

        if (previousGameId != gameId) {
            teams.setValue(new ArrayList<>());
        }

        previousGameId = gameId;
        fetch(gameId);
    }


    public void setActiveTeam(Team team) {
        activeTeam = team;
    }

    public Team getActiveTeam() {
        return activeTeam;
    }

    private void fetch(long gameId) {
        MainActivity.requestHandler.getTeams(gameId, response -> {
            List<Team> currentTeams = new ArrayList<>();

            try {
                for (int i = 0; i < response.length(); i++) {
                    Team parsedTeam = Team.fromJson(response.getJSONObject(i));
                    currentTeams.add(parsedTeam);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teams.setValue(currentTeams);
        }, error -> {
            Log.e(TAG, "Error fetching Teams: " + error.toString());
        });
    }

    public void addTeam(long gameId, String teamName, String userName) {
        Team newTeam = new Team(Team.UNKNOWN_ID, gameId, teamName, userName, new ArrayList<>());
        MainActivity.requestHandler.postTeams(newTeam, response -> {
            fetch(gameId);
        }, error -> {
            Log.e(TAG, "Error adding Team: " + error.toString());
        });
    }

    public void fetchTeamInfo(long teamId){
        MainActivity.requestHandler.getTeamInfo(teamId, response -> {
            List<Team> currentTeams = teams.getValue();
            if (currentTeams == null) {
                currentTeams = new ArrayList<>();
            }
            for (Team team : currentTeams) {
                if (team.getId() == teamId) {
                    try {
                        List<String> members = new ArrayList<>();
                        JSONArray membersJson = response.optJSONArray("members");
                        if (membersJson != null) {
                            for (int i = 0; i < membersJson.length(); i++) {
                                members.add(membersJson.getString(i));
                            }
                        }
                        team.setMembers(members);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            teams.setValue(currentTeams);
        }, error -> {
            Log.e(TAG, "Error fetching Team info: " + error.toString());
        });
    }
}
