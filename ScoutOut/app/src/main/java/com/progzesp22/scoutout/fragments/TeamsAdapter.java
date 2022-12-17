package com.progzesp22.scoutout.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;

import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    private final List<Team> localDataSet;
    private final TeamsModel model;
    private final NavController navController;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView teamName;
        private final TextView teamCreator;
        private final Button button;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            teamName = (TextView) view.findViewById(R.id.teamName);
            teamCreator = (TextView) view.findViewById(R.id.creator);
            button = (Button) view.findViewById(R.id.gameButton);
        }

        public TextView getNameTextView() {
            return teamName;
        }
        public TextView getCreatorTextView() {
            return teamCreator;
        }
        public Button getButton(){return button;}
    }

    public TeamsAdapter(List<Team> dataSet, NavController navController, TeamsModel model) {
        localDataSet = dataSet;
        this.navController = navController;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.teams_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Team team = localDataSet.get(position);
        viewHolder.getNameTextView().setText(team.getName());
        TextView creator = viewHolder.getCreatorTextView();
        TextView button = viewHolder.getButton();
        creator.setText(team.getCreator());
        button.setText("Join");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(view1 -> {
            Toast.makeText(view1.getContext(), "Joined successfully", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.playerTeamsFragment);
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
