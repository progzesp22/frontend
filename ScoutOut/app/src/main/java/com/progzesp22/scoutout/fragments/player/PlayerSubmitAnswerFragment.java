package com.progzesp22.scoutout.fragments.player;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerSubmitAnswerBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.progzesp22.scoutout.databinding.FragmentPlayerTasksBinding;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.MyCaptureActivity;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

public class PlayerSubmitAnswerFragment extends Fragment {
    private FragmentPlayerSubmitAnswerBinding binding;
    Task task;
    String qrText = null;
    Bitmap bitmap;


    public PlayerSubmitAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayerSubmitAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task task = model.getActiveTask();
        if (task == null) return;
        loadTask(task);

        binding.answerButton.setOnClickListener(view1 -> {
            Answer answer = buildAnswer();
            if(answer == null){
                return;
            }

            MainActivity.requestHandler.postAnswer(
                    answer,
                    response -> {
                        Toast.makeText(getContext(), "Wysłano", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG).show());
        });


        binding.scanQRButton.setOnClickListener(view2 -> scanCode());


        binding.photoButton.setOnClickListener(view1 -> {
                    int REQUEST_IMAGE_CAPTURE = 7; // random number xd
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        Bundle extras = data.getExtras();
        bitmap = (Bitmap) extras.get("data");
        binding.photo.setImageBitmap(bitmap);
        Log.d("Camera", "jest zdjęcie!");
    }

    private Answer buildAnswer(){
        Answer answer = new Answer(Entity.UNKNOWN_ID, "", task.getId(), Entity.UNKNOWN_ID, false, false);;
        switch(task.getType()){
            case TEXT:
                answer.setAnswer(binding.answerText.getText().toString());
                break;
            case PHOTO:
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                String response = Base64.getEncoder().encodeToString(bytes.toByteArray());
                answer.setAnswer(response);
                break;
            case QR_CODE:
                if(qrText == null){
                    Toast.makeText(requireContext(), "Nie zeskanowano żadnego kodu!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                answer.setAnswer(qrText);
                break;
            default:
                return null;
        }

        return answer;
    }

    private void loadTask(Task t){
        this.task = t;
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());

        binding.answerText.setVisibility(View.GONE);
        binding.photo.setVisibility(View.GONE);
        binding.photoButton.setVisibility(View.GONE);
        binding.scanQRButton.setVisibility(View.GONE);
        binding.qrResult.setVisibility(View.GONE);

        switch(task.getType()){
            case TEXT:
                binding.answerText.setVisibility(View.VISIBLE);
                break;
            case PHOTO:
                binding.photo.setVisibility(View.VISIBLE);
                binding.photoButton.setVisibility(View.VISIBLE);
                break;
            case QR_CODE:
                binding.scanQRButton.setVisibility(View.VISIBLE);
                binding.qrResult.setVisibility(View.VISIBLE);
                binding.qrResult.setText("Nie zeskanowano jeszcze kodu");
                break;
            case NAV_POS:
                break;
            case AUDIO:
                break;
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Włącz lampę błyskową, używając przycisku VolumeUp.");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(MyCaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            qrText = result.getContents();
            binding.qrResult.setText("Zeskanowano: " + qrText);
        }
    });
}