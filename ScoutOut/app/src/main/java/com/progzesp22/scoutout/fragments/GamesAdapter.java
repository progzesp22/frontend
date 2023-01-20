package com.progzesp22.scoutout.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.domain.UserModel;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private final List<Game> localDataSet;
    private final GamesModel model;
    private final UserModel userModel;
    private final TeamsModel teamsModel;
    private final NavController navController;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gameName;
        private final TextView gameStatus;
        private final Button button;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            gameName = (TextView) view.findViewById(R.id.gameName);
            gameStatus = (TextView) view.findViewById(R.id.status);
            button = (Button) view.findViewById(R.id.gameButton);
        }

        public TextView getNameTextView() {
            return gameName;
        }
        public TextView getStatusTextView() {
            return gameStatus;
        }
        public Button getButton(){return button;}
    }

    public GamesAdapter(List<Game> dataSet, NavController navController, GamesModel model, UserModel userModel, TeamsModel teamsModel) {
        localDataSet = dataSet;
        this.navController = navController;
        this.model = model;
        this.userModel = userModel;
        this.teamsModel = teamsModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.game_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Game game = localDataSet.get(position);
        viewHolder.getNameTextView().setText(game.getName());

        TextView status = viewHolder.getStatusTextView();
        TextView button = viewHolder.getButton();
        String username = userModel.getUsername();

        switch(game.getState()){
            case CREATED:
                status.setText("configuring");
                if (!username.equals(game.getGameMaster())){
                    button.setVisibility(View.GONE);
                } else {
                    button.setText("edit");
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(view1 -> {
                        model.setActiveGame(game);
                        navController.navigate(R.id.action_create_edit_game);
                    });
                }
                break;
            case PENDING:
                status.setText("waiting for players");
                button.setText("join");
                button.setVisibility(View.VISIBLE);
                if (username.equals(game.getGameMaster())){
                    button.setOnClickListener(view1 -> {
                        model.setActiveGame(game);
                        userModel.setUserType(UserModel.UserType.GM);
                        navController.navigate(R.id.action_userGamesFragment_to_GMWaitForPlayersFragment);
                    });
                } else {
                    button.setOnClickListener(view1 -> {
                        model.setActiveGame(game);
                        userModel.setUserType(UserModel.UserType.PLAYER);
                        navController.navigate(R.id.action_userGamesFragment_to_playerTeamsFragment);

                    });
                }
                break;
            case STARTED:
                status.setText("playing");
                button.setText("play");
                button.setVisibility(View.VISIBLE);
                if (username.equals(game.getGameMaster())){
                    button.setOnClickListener(view1 -> {
                        model.setActiveGame(game);
                        userModel.setUserType(UserModel.UserType.GM);
                        navController.navigate(R.id.action_userGamesFragment_to_gm_game_fragment);
                    });
                } else {
                    button.setOnClickListener(view1 -> {
                        teamsModel.teamWithPlayerExists(username, game.getId(),exists->{
                            if(exists){
                                model.setActiveGame(game);
                                userModel.setUserType(UserModel.UserType.PLAYER);
                                navController.navigate(R.id.action_userGamesFragment_to_listTasksFragment);
                            }else{
                                Toast.makeText(viewHolder.itemView.getContext(), "You are not in any team", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
                break;
            case FINISHED:
                status.setText("finished");
                button.setVisibility(View.GONE);
                break;
            default:
                status.setText("unknown");
                button.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
