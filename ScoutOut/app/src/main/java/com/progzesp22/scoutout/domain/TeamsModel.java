package com.progzesp22.scoutout.domain;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.progzesp22.scoutout.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

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
        if (previousGameId != gameId) {
            fetch(gameId);
            previousGameId = gameId;
        }
        return teams;
    }

    public void refresh(long gameId) {
        if (teams == null) {
            teams = new MutableLiveData<>();
        }

        if (previousGameId != gameId) {
            teams.setValue(new ArrayList<>());
            previousGameId = gameId;
        }

        fetch(gameId);
    }


    public void setActiveTeam(Team team) {
        activeTeam = team;
    }

    public Team getActiveTeam() {
        return activeTeam;
    }

    public void teamWithPlayerExists(String name, long gameId, Consumer<Boolean> callback) {
        teams = new MutableLiveData<>();
        MainActivity.requestHandler.getTeams(gameId, response -> {
            List<Team> currentTeams = teams.getValue();
            if (currentTeams == null) {
                currentTeams = new ArrayList<>();
            }

            try {
                for (int i = 0; i < response.length(); i++) {
                    Team parsedTeam = Team.fromJson(response.getJSONObject(i));
                    boolean found = false;
                    for (Team team : currentTeams) {
                        if (team.getId() == parsedTeam.getId()) {
                            team.updateFrom(parsedTeam);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        currentTeams.add(parsedTeam);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teams.setValue(currentTeams);
            boolean exists = false;
            for(Team team : currentTeams) {
                if (team.getMembers().contains(name)) {
                    activeTeam = team;
                    exists = true;
                    break;
                }
            }
            callback.accept(exists);

        }, error -> {
            Log.e(TAG, "Error fetching Teams: " + error.toString());
        });
    }

    private void fetch(long gameId) {
        MainActivity.requestHandler.getTeams(gameId, response -> {
            List<Team> currentTeams = teams.getValue();
            if (currentTeams == null) {
                currentTeams = new ArrayList<>();
            }

            try {
                for (int i = 0; i < response.length(); i++) {
                    Team parsedTeam = Team.fromJson(response.getJSONObject(i));
                    boolean found = false;
                    for (Team team : currentTeams) {
                        if (team.getId() == parsedTeam.getId()) {
                            team.updateFrom(parsedTeam);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        currentTeams.add(parsedTeam);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teams.setValue(currentTeams);
        }, error -> {
            Log.e(TAG, "Error fetching Teams: " + error.toString());
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
