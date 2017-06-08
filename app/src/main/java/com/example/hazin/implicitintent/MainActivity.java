package com.example.hazin.implicitintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_IMAGE_REQUEST_CODE = 100;
    private static final int SELECT_PHOTO = 101;

    Button share;
    Button navigate;
    Button ph_call;
    Button send_email;
    Button take_picture;
    Button select_picture;

    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share = (Button) findViewById(R.id.share_name);
        share.setOnClickListener(this);

        navigate = (Button) findViewById(R.id.navigate_map);
        navigate.setOnClickListener(this);

        ph_call = (Button) findViewById(R.id.phone_call);
        ph_call.setOnClickListener(this);

        send_email = (Button) findViewById(R.id.sending_mail);
        send_email.setOnClickListener(this);

        take_picture = (Button) findViewById(R.id.take_picture);
        take_picture.setOnClickListener(this);

        select_picture = (Button) findViewById(R.id.select_picture);
        select_picture.setOnClickListener(this);

        mImage = (ImageView) findViewById(R.id.image_from_camera);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.share_name:
                onShare();
                break;

            case R.id.navigate_map:
                onNavigateMap();
                break;

            case R.id.phone_call:
                onPhoneCall();
                break;

            case R.id.sending_mail:
                onSendEmail();
                break;

            case R.id.take_picture:
                onTakePicture();
                break;

            case R.id.select_picture:
                onSelectPicture();
                break;

            default:
                break;
        }

    }


    private void onSelectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    private void onTakePicture() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == CAMERA_IMAGE_REQUEST_CODE) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            mImage.setImageBitmap(mphoto);
        } else if (requestCode == SELECT_PHOTO) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            mImage.setImageBitmap(yourSelectedImage);
        }
    }


    private void onSendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPhoneCall() {
        String number = "09974050105";
        Uri call = Uri.parse("tel:" + number);
        Intent phcall = new Intent(Intent.ACTION_CALL, call);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(phcall);
    }

    private void onNavigateMap() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=16.779357,96.154421"));
        startActivity(intent);
    }

    private void onShare() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(MainActivity.this)
                .setType("text/plain")
                .setText("Hello Min Ga Lar Par")
                .setChooserTitle("Testing")
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }
}
