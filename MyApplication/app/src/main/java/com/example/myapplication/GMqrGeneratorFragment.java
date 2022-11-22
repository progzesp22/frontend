package com.example.myapplication;

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

import com.example.myapplication.databinding.FragmentGmQrGeneratorBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class GMqrGeneratorFragment extends Fragment {
    private FragmentGmQrGeneratorBinding binding;


    public GMqrGeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmQrGeneratorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        BitMatrix mMatrix;
        Bitmap mBitmap = null;
        try {
            mMatrix = mWriter.encode(String.valueOf(Math.random()), BarcodeFormat.QR_CODE, 400, 400);
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
            startActivity(Intent.createChooser(intent, "Udostępnij kod QR"));
        });

        binding.save.setOnClickListener(view1 -> {
            MediaStore.Images.Media.insertImage(view.getContext().getApplicationContext().getContentResolver(), finalMBitmap, "Test", "Test");
            Snackbar mySnackbar = Snackbar.make(view, "Zapisano QR", 750);
            mySnackbar.show();

        });
    }

}