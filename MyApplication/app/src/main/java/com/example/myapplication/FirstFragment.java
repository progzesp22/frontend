package com.example.myapplication;

import static java.lang.Math.round;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.text.DecimalFormat;
import java.util.Arrays;

public class FirstFragment extends Fragment implements SensorEventListener {

    private FragmentFirstBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        binding.buttonFirst.setOnClickListener(view1 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));

        binding.button2.setOnClickListener(view1 ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_requestDemoFragment3));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sensorManager.unregisterListener(this);
        binding = null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        DecimalFormat f = new DecimalFormat("0.00");
        float[] vals = sensorEvent.values;
        String text = "X: " + f.format(vals[0]) + "\nY: " + f.format(vals[1]) + "\nZ: " + f.format(vals[2]);
        binding.accelerometerText.setText(text);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}