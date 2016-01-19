package com.example.user.finderskeepers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.viniciusdsl.cameraview.library.CameraView;

public class MyVid extends Activity {
    public CameraView cameraView;
    public Button cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vid);

        openSesame();


    }

    public void openSesame() {
        Intent intent = new Intent (MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, 1);

    }

}
