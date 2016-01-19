package com.example.user.finderskeepers;

//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.PixelFormat;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.viniciusdsl.cameraview.library.CameraView;
//import com.viniciusdsl.cameraview.library.listener.CameraListener;
//
//import java.io.File;
//
///**
// * Created by User on 1/18/2016.
// */
//public class MyCam extends Activity {
//
//
//    public CameraView cameraView;
//    public Button cameraButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.camera);
//
//        openSesame();
//
//
//    }
//
//    public void openSesame() {
//        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 1);
//
//    }
//
//}



import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tb_laota on 10/30/2015.
 */
public class MyCam extends Activity implements SurfaceHolder.Callback {
    Camera camera;
    @InjectView(R.id.surfaceView)
    SurfaceView surfaceView;
    @InjectView(R.id.btn_take_photo)
    FloatingActionButton btn_take_photo;
    SurfaceHolder surfaceHolder;
    PictureCallback jpegCallback;
    ShutterCallback shutterCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        ButterKnife.inject(this);
        surfaceHolder = surfaceView.getHolder();
        // Install a surfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);
        //deprecated setting, but required on android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btn_take_photo.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        jpegCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outputStream = null;
                File file_image = getDirc();
                if (!file_image.exists() && !file_image.mkdirs()) {
                    Toast.makeText(getApplication(), "Can't create directory to save image", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String photofile = "Cam_Demo" + date + ".jpg";
                String file_name = file_image.getPath() + File.separator + photofile;
                File picfile = new File(file_name);
                try {
                    outputStream = new FileOutputStream(picfile);
                    outputStream.write(data);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                } catch (IOException ex) {
                } finally {

                }
                Toast.makeText(getApplicationContext(), "Picture saved", Toast.LENGTH_SHORT).show();
                refreshCamera();
                refreshGallery(picfile);
            }
        };
    }

    //refresh gallery
    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            //preview surface does not exist
            return;
        }
        //stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
        }
        //set preview size and make any resize, rotate or
        //reformatting changes here
        //start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    public File getDirc() {
        File dics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(dics, "Camera_Demo");
    }

    public void captureImage() {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //open the camera
        try {
            camera = Camera.open();
        } catch (RuntimeException ex) {
        }
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        //modify parameter
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(352, 288);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            //The surface thas been created, now tell the camera where to draw
            //the preview
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}