package com.progzesp22.scoutout.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progzesp22.scoutout.MainActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GamesModel extends ViewModel {
    private MutableLiveData<List<Game>> games;
    private Game activeGame;

    private static final String TAG = "GamesModel";


    public LiveData<List<Game>> getGames() {
        if (games == null) {
            games = new MutableLiveData<>();
            fetch();
        }

        return games;
    }

    public void refresh() {
        if (games == null) {
            games = new MutableLiveData<>();
        }

        fetch();
    }


    public void setActiveGame(Game game) {
        activeGame = game;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    private void fetch() {
        MainActivity.requestHandler.getGames(response -> {
            List<Game> currentGames = new ArrayList<>();

            try {
                for (int i = 0; i < response.length(); i++) {
                    Game parsedGame = Game.fromJson(response.getJSONObject(i));
                    currentGames.add(parsedGame);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            games.setValue(currentGames);
        }, error -> {
            Log.e(TAG, "Error fetching Games: " + error.toString());
        });
    }
}
