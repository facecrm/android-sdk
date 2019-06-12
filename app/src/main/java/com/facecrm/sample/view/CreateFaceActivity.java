package com.facecrm.sample.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.face.detect.FaceCRM;
import com.face.detect.Listener.DetectFaceListener;
import com.face.detect.Listener.UploadFaceListener;
import com.face.detect.Model.APUpload;
import com.face.detect.Util.Util;
import com.facecrm.sample.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFaceActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private String encodeImage_1, encodeImage_2, encodeImage_3, encodeImage_4;

    @BindView(R.id.btn_next)
    AppCompatButton btnNext;
    @BindView(R.id.imv_1)
    ImageView imv1;
    @BindView(R.id.imv_2)
    ImageView imv2;
    @BindView(R.id.imv_3)
    ImageView imv3;
    @BindView(R.id.imv_4)
    ImageView imv4;
    @BindView(R.id.imv_close_1)
    ImageView imvDelete1;
    @BindView(R.id.imv_close_2)
    ImageView imvDelete2;
    @BindView(R.id.imv_close_3)
    ImageView imvDelete3;
    @BindView(R.id.imv_close_4)
    ImageView imvDelete4;
    @BindView(R.id.edt_name)
    AppCompatEditText edtName;
    @BindView(R.id.edt_phone)
    AppCompatEditText edtPhone;
    @BindView(R.id.imv_add)
    ImageView imvAdd;

    private Bitmap mBitmap;
    private String clientId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);

        onClick();
    }

    private void onClick() {
        btnNext.setOnClickListener(this);
        imvDelete1.setOnClickListener(this);
        imvDelete2.setOnClickListener(this);
        imvDelete3.setOnClickListener(this);
        imvDelete4.setOnClickListener(this);
        imvAdd.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FaceCRM.getsInstance().startCamera(CreateFaceActivity.this,
                R.id.color_blob_detection_activity_surface_view, false);

        FaceCRM.getsInstance().setDetectFaceListener(new DetectFaceListener() {
            @Override
            public void onDetectSuccess(Bitmap imgFull, Bitmap imgFace) {
                Log.e("onDetectSuccess", imgFace + "");
                mBitmap = imgFace;
            }

            @Override
            public void onDetectFail(String msgFail) {
                Log.e("onDetectFail", msgFail);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        FaceCRM.getsInstance().stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        FaceCRM.getsInstance().stopCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_add:
                if (mBitmap != null) {
                    if (imv1.getDrawable() == null) {
                        Glide.with(this).load(mBitmap).into(imv1);
                        encodeImage_1 = Util.shared().convertBitmapToBase64(mBitmap);
                        mBitmap = null;
                        return;
                    }
                    if (imv2.getDrawable() == null) {
                        Glide.with(this).load(mBitmap).into(imv2);
                        encodeImage_2 = Util.shared().convertBitmapToBase64(mBitmap);
                        mBitmap = null;
                        return;
                    }
                    if (imv3.getDrawable() == null) {
                        Glide.with(this).load(mBitmap).into(imv3);
                        encodeImage_3 = Util.shared().convertBitmapToBase64(mBitmap);
                        mBitmap = null;
                        return;
                    }
                    if (imv4.getDrawable() == null) {
                        Glide.with(this).load(mBitmap).into(imv4);
                        encodeImage_4 = Util.shared().convertBitmapToBase64(mBitmap);
                        mBitmap = null;
                        return;
                    }
                }
                break;
            case R.id.btn_next:
                if (encodeImage_1 != null && encodeImage_2 != null &&
                        encodeImage_3 != null && encodeImage_4 != null) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please waiting...");
                    progressDialog.show();
                    //todo call api create face
                    uploadImage(encodeImage_1, 1);
                }
                break;
            case R.id.imv_close_1:
                imv1.setImageDrawable(null);
                encodeImage_1 = null;
                break;
            case R.id.imv_close_2:
                imv2.setImageDrawable(null);
                encodeImage_2 = null;
                break;
            case R.id.imv_close_3:
                imv3.setImageDrawable(null);
                encodeImage_3 = null;
                break;
            case R.id.imv_close_4:
                imv4.setImageDrawable(null);
                encodeImage_4 = null;
                break;
        }
    }

    private void uploadImage(final String base64, final int numberImage) {
        FaceCRM.getsInstance().callFaceUploadApi(base64, new UploadFaceListener() {
            @Override
            public void onUploadFaceSuccess(APUpload data) {
                clientId = data.clientId;
                switch (numberImage) {
                    case 1:
                        uploadImage(encodeImage_2, 2);
                        break;
                    case 2:
                        uploadImage(encodeImage_3, 3);
                        break;
                    case 3:
                        uploadImage(encodeImage_4, 4);
                        break;
                    case 4:
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        finish();
                        break;
                }
            }

            @Override
            public void onUploadFaceFail(String message) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (numberImage == 1) {
                    imv1.setImageDrawable(null);
                    encodeImage_1 = null;
                }
                if (numberImage == 2) {
                    imv2.setImageDrawable(null);
                    encodeImage_2 = null;
                }
                if (numberImage == 3) {
                    imv1.setImageDrawable(null);
                    encodeImage_3 = null;
                }
                if (numberImage == 4) {
                    imv4.setImageDrawable(null);
                    encodeImage_4 = null;
                }
                Toast.makeText(CreateFaceActivity.this, "Upload image face fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
