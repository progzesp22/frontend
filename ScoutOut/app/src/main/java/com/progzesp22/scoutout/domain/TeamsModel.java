package com.progzesp22.scoutout.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progzesp22.scoutout.MainActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TeamsModel extends ViewModel {
    private MutableLiveData<List<Team>> teams;
    private Team activeTeam;

    private static final String TAG = "TeamsModel";


    public LiveData<List<Team>> getTeams() {
        if (teams == null) {
            teams = new MutableLiveData<>();
            fetch();
        }
        return teams;
    }

    public void refresh() {
        if (teams == null) {
            teams = new MutableLiveData<>();
        }

        fetch();
    }


    public void setActiveTeam(Team team) {
        activeTeam = team;
    }

    public Team getActiveTeam() {
        return activeTeam;
    }

    private void fetch() {
        MainActivity.requestHandler.getTeams(response -> {
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
}
