package com.example.smokingappdraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BannerSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_setting);
    }

    public void btnSaveBannerOnClick(View view) {
        //save image
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            ImageView ivBanner = findViewById(R.id.ivBanner);
            ivBanner.setImageURI(selectedImage);
        }
    }

    public void btnUploadBannerOnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Sets the type as image/*. This ensures only components of type image are selected
        String[] mimeTypes = {"image/jpeg", "image/png"};  //Pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, 3);
    }
}



