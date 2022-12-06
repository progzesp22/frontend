package com.progzesp22.scoutout.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.domain.Game;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private final List<Game> localDataSet;

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

    public GamesAdapter(List<Game> dataSet) {
        localDataSet = dataSet;
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


        switch(game.getState()){
            case CREATED:
                status.setText("configuring");
                button.setText("edit");
                button.setVisibility(View.VISIBLE);
                break;
            case PENDING:
                status.setText("waiting for players");
                button.setText("join");
                button.setVisibility(View.VISIBLE);
                break;
            case STARTED:
                status.setText("playing");
                button.setText("join");
                button.setVisibility(View.VISIBLE);
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
