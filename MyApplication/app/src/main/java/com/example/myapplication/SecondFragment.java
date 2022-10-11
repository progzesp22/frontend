package com.example.myapplication;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentSecondBinding;

import java.io.IOException;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer   player = null;

    private boolean mStartPlaying = true;
    private boolean mStartRecording = true;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        fileName = requireContext().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";


        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(view1 ->
                NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));

        binding.playButton.setOnClickListener(view1 ->{
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                binding.playButton.setText(getString(R.string.stop));
            } else {
                binding.playButton.setText(getString(R.string.play));
            }
            mStartPlaying = !mStartPlaying;
        });

        binding.recordingButton.setOnClickListener(view1 ->{
            onRecord(mStartRecording);
            if (mStartRecording) {
                binding.recordingButton.setText(getString(R.string.stop));
            } else {
                binding.recordingButton.setText(getString(R.string.record));
            }
            mStartRecording = !mStartRecording;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }

        binding = null;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
