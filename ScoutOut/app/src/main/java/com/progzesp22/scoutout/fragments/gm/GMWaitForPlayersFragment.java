package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.progzesp22.scoutout.CustomExpandableListAdapter;
import com.progzesp22.scoutout.MockRequestHandler;
import com.progzesp22.scoutout.RequestInterface;
import com.progzesp22.scoutout.databinding.FragmentGMWaitForPlayersBinding;
import com.progzesp22.scoutout.domain.Team;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GMWaitForPlayersFragment extends Fragment {
    private FragmentGMWaitForPlayersBinding binding;
    //TODO: zmienić na odpowiednią klasę
    RequestInterface requestInterface = new MockRequestHandler();

    CustomExpandableListAdapter expandableListAdapter;

    Timer timer;
    int REFRESH_RATE = 5000;


    public GMWaitForPlayersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListAdapter = new CustomExpandableListAdapter(getContext());
        binding.teamsList.setAdapter(expandableListAdapter);
        refreshTeams();
        binding.refreshTeamsButton.setOnClickListener(view1 -> refreshTeams());
        binding.startButton.setOnClickListener((event)->startButtonPressed());
        setGamesName();
        String title = "\nWaiting for Players...\nTeams list:";
        binding.titleTextView.setText(title);
        timer = new Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                refreshTeams();
            }
        }, 0, REFRESH_RATE);
    }

    private void setGamesName(){
        requestInterface.getGame(RequestInterface.GAME_ID, response ->{
            try {
                String name = response.getString("name");
                binding.gameNameTextView.setText(name);
            } catch (JSONException e) {
                String name = "Game";
                binding.gameNameTextView.setText(name);
            }
        }, error -> {
            String name = "Game";
            binding.gameNameTextView.setText(name);
        });
    }
    //TODO: dodać przejście do następnego ekranu
    //trzeba też dodać timer.cancel(); bo samo przejście do następnego ekranu raczej nie zatrzymuje timera
    private void startButtonPressed(){
        Toast.makeText(getContext(), "Starting game", Toast.LENGTH_SHORT).show();
    }

    private void refreshTeams(){
        expandableListAdapter.getExpandableListTitle().clear();
        expandableListAdapter.getExpandableListDetail().clear();
        requestInterface.getTeams(RequestInterface.GAME_ID, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject x = response.getJSONObject(i);
                    int id = x.getInt("id");
                    requestInterface.getTeamInfo(id, response1 -> {
                        try {
                            Team team = Team.fromJson(response1);
                            String name = team.getName()+" ("+team.getMembers().size()+")";
                            expandableListAdapter.getExpandableListTitle().add(name);
                            expandableListAdapter.getExpandableListDetail().put(name, team.getMembers());
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),
                                    "An error occurred. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    },error-> Toast.makeText(getContext(),
                            "An error occurred. Please try again.",
                            Toast.LENGTH_SHORT).show());
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(),
                        "An error occurred. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getContext(),
                "An error occurred. Please try again.",
                Toast.LENGTH_SHORT).show());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGMWaitForPlayersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}