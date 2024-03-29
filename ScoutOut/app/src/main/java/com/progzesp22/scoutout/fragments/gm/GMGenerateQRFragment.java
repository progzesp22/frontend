package com.progzesp22.scoutout.fragments.gm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentGmGenerateQrFragmentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;


public class GMGenerateQRFragment extends Fragment {
    private FragmentGmGenerateQrFragmentBinding binding;


    public GMGenerateQRFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmGenerateQrFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        BitMatrix mMatrix;
        Bitmap mBitmap = null;

        TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task task = tasksModel.getActiveTask();
        if(task == null || task.getType() != Task.TaskType.QR_CODE){
            return;
        }

        String answer = task.getCorrectAnswer();

        try {
            mMatrix = mWriter.encode(answer, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            mBitmap = mEncoder.createBitmap(mMatrix);
            binding.imageView.setImageBitmap(mBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        binding.imageView.setVisibility(View.VISIBLE);
        binding.titleText.setText(R.string.QRgenerationSuccess);
        final Bitmap finalMBitmap = mBitmap;
        binding.share.setOnClickListener(view1 -> {
            String path = MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), finalMBitmap, "Image Description", null);
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_qr)));
        });

        binding.save.setOnClickListener(view1 -> {
            MediaStore.Images.Media.insertImage(view.getContext().getApplicationContext().getContentResolver(), finalMBitmap, "Test", "Test");
            Snackbar mySnackbar = Snackbar.make(view, R.string.qr_saved, 750);
            mySnackbar.show();

        });
    }

}