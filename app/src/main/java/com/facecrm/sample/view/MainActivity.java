package com.facecrm.sample.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.face.detect.FaceCRM;
import com.face.detect.Listener.DetectFaceListener;
import com.face.detect.Listener.VerifyDetectFaceListener;
import com.face.detect.Model.APDataModel;
import com.facecrm.sample.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.btn_new)
    AppCompatButton btnNew;
    @BindView(R.id.btn_history)
    AppCompatButton btnHistory;
    @BindView(R.id.tv_id)
    AppCompatTextView tvName;
    @BindView(R.id.tv_name)
    AppCompatTextView tvEmail;
    @BindView(R.id.tv_db)
    AppCompatTextView tvPhone;
    @BindView(R.id.tv_collection)
    AppCompatTextView tvSex;
    @BindView(R.id.imv_full)
    ImageView imvFull;
    @BindView(R.id.imv_face)
    ImageView imvFace;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getToken();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initOnClick();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        FaceCRM.getsInstance().startCamera(this,
                R.id.color_blob_detection_activity_surface_view, true);

        FaceCRM.getsInstance().setDetectFaceListener(new DetectFaceListener() {
            @Override
            public void onDetectSuccess(final Bitmap imgFull, final Bitmap imgFace) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(MainActivity.this).load(imgFull).into(imvFull);
                                Glide.with(MainActivity.this).load(imgFace).into(imvFace);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onDetectFail(final String msgFail) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imvFull.setImageResource(R.drawable.imgae_default);
//                                imvFace.setImageResource(R.drawable.imgae_default);
//                                tvName.setText(msgFail);
//                            }
//                        });
//                    }
//                }).start();
            }
        });

        FaceCRM.getsInstance().setVerifyDetectFaceListener(new VerifyDetectFaceListener() {
            @Override
            public void onVerifyFaceSuccess(APDataModel data) {
                tvName.setText(data.getFaceId());
            }

            @Override
            public void onVerifyFaceFail(int code, String message) {
                if (code == 404)
                    tvName.setText("Face verification not found");
                else if (code == 500)
                    tvName.setText("Server verify face fail");
                else
                    tvName.setText("Detect face fail");
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
        FaceCRM.getsInstance().stopCamera();
    }

    private void initOnClick() {
        btnNew.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_new) {
            Intent intent = new Intent(MainActivity.this, CreateFaceActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
    }
}
