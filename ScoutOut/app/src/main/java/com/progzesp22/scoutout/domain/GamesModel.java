package com.progzesp22.scoutout.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.progzesp22.scoutout.MainActivity;

import org.json.JSONException;

import java.text.ParseException;
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

            fetchGameDetails();
        }, error -> {
            Log.e(TAG, "Error fetching Games: " + error.toString());
        });
    }

    private void fetchGameDetails(){
        List<Game> currentGames = games.getValue();

        if (currentGames == null) {
            return;
        }

        for (Game game : currentGames) {
            MainActivity.requestHandler.getGame(game.getId(), response -> {
                try {
                    Game.EndCondition endCondition = Game.EndCondition.valueOf(response.getString("endCondition"));
                    game.setEndCondition(endCondition);
                    game.setStartTime(Game.dateFormat.parse(response.getString("startTime")));
                    if (endCondition == Game.EndCondition.TIME) {
                        game.setEndTime(Game.dateFormat.parse(response.getString("endTime")));
                    } else if (endCondition == Game.EndCondition.SCORE) {
                        game.setEndScore(response.getInt("endScore"));
                    }

                    games.setValue(currentGames);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.e(TAG, "Error fetching Game details: " + error.toString());
            });
        }
    }

    public void startGame(Game game){
        Game.GameState temp = game.getState();
        game.setState(Game.GameState.STARTED);
        MainActivity.requestHandler.patchGame(game, response -> {
            refresh();
        }, error -> {
            Log.e(TAG, "Error starting game: " + error.toString());
            game.setState(temp);
        });
    }

}
