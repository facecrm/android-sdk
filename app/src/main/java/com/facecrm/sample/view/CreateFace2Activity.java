package com.facecrm.sample.view;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.face.detect.FaceCRM;
import com.facecrm.sample.R;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFace2Activity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    @BindView(R.id.cameraView)
    SurfaceView surfaceView;
    @BindView(R.id.imv)
    ImageView imageView;

    private Camera camera;
    private SurfaceHolder surfaceHolder;

    private Camera.PictureCallback captureImageCallback;

    private byte[] arrData;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.actvity_create_face);
        ButterKnife.bind(this);

        onClick();
        initControl();

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);

        captureImageCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(-90);
                mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                arrData = byteArrayOutputStream.toByteArray();

                Glide.with(CreateFace2Activity.this).load(mBitmap).into(imageView);
                refreshCamera();
            }
        };
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) return;

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    private void onClick() {
        btnNext.setOnClickListener(this);
        surfaceView.setOnClickListener(this);
    }

    private void initControl() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (mBitmap != null) {
                FaceCRM.getsInstance().detectByImage(null, mBitmap);
                mBitmap = null;
            }
        }
        if (v.getId() == R.id.cameraView) {
            try {
                captureImage(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void captureImage(View view) throws IOException {
        camera.takePicture(null, null, captureImageCallback);
    }

    public void changeOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            camera.setDisplayOrientation(0);
        else
            camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            changeOrientation();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        Camera.Parameters param;
        param = camera.getParameters();
        List<Camera.Size> allSizes = param.getSupportedPictureSizes();
        Camera.Size size = allSizes.get(0); // get top size
        for (int i = 0; i < allSizes.size(); i++) {
            if (allSizes.get(i).width > size.width)
                size = allSizes.get(i);
        }
//        param.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
        param.setPictureSize(size.width, size.height);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        changeOrientation();
    }
}
