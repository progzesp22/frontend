package com.progzesp22.scoutout.fragments.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.MyCaptureActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerSubmitAnswerBinding;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class PlayerSubmitAnswerFragment extends Fragment {
    private FragmentPlayerSubmitAnswerBinding binding;
    Task task;
    String qrText = null;
    String TAG = "PlayerSubmitAnswerFragment";


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
        binding.photoButton.setOnClickListener(view3 -> dispatchTakePictureIntent());
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "dispatchTakePictureIntent: ", ex);
                Toast toast = Toast.makeText(getContext(), R.string.error_file, Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraActivityResultLauncher.launch(takePictureIntent);
            }
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // There are no request codes
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                        bitmap = rotateImageIfRequired(requireContext(), bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        binding.photo.setImageBitmap(bitmap);
                        binding.photo.setVisibility(View.VISIBLE);
                        Log.d(TAG, "udało się zrobić zdjęcie! poggers");

                    }
                }
            });

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage){

        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            Log.e("rotateImageIfRequired", "rotateImageIfRequired: ", e);
        }
        ExifInterface ei;
        try {
            ei = new ExifInterface(input);
        } catch (IOException e) {
            Log.e("rotateImageIfRequired", "rotateImageIfRequired: ", e);
            return img;
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (data == null) return;
//        Bundle extras = data.getExtras();
//        bitmap = (Bitmap) extras.get("data");
//        binding.photo.setImageBitmap(bitmap);
//        Log.d("Camera", "jest zdjęcie!");
//    }

    private Answer buildAnswer(){
        Answer answer = new Answer(Entity.UNKNOWN_ID, "", task.getId(), Entity.UNKNOWN_ID, false, false);
        switch(task.getType()){
            case TEXT:
                answer.setAnswer(binding.answerText.getText().toString());
                break;
            case PHOTO:
                int targetW = 1024;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;

                int scaleFactor = Math.max(1, photoW/targetW);
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String response = Base64.getEncoder().encodeToString(bytes.toByteArray());
                answer.setAnswer(response);
                break;
            case QR_CODE:
                if(qrText == null){
                    Toast.makeText(requireContext(), R.string.qr_not_scanned, Toast.LENGTH_SHORT).show();
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
                binding.qrResult.setText(R.string.qr_not_scanned);
                break;
            case NAV_POS:
                break;
            case AUDIO:
                break;
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getResources().getString(R.string.flashlight_info));
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(MyCaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            qrText = result.getContents();
            binding.qrResult.setText(getString(R.string.qr_scanned) + " " + qrText);
        }
    });
}