package com.ekidpro.facecrmsample.view;

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

import com.aponl.apfacesdk.FaceCRM;
import com.aponl.apfacesdk.Listener.DetectFaceListener;
import com.aponl.apfacesdk.Listener.VerifyDetectFaceListener;
import com.aponl.apfacesdk.Model.APDataModel;
import com.ekidpro.facecrmsample.R;
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
            Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
    }

//    private void getToken() {
//        NetworkClient.getRetrofit().create(NetworkInterface.class)
//                .applicationVerify("ca55719568ae5416e34c260514ac4d3b", Util.shared().getDeviceId())
//                .enqueue(new Callback<MemberResult>() {
//                    @Override
//                    public void onResponse(Call<MemberResult> call, Response<MemberResult> response) {
//                        if (response.isSuccessful()) {
//                            if (response.body() != null && response.body().status == 200) {
//                                Util.shared().setToken(response.body().dataMember.token);
//                                SharedPreferences.Editor editor = pre.edit();
//                                editor.putString(Utils.TOKEN, response.body().dataMember.token);
//                                editor.apply();
//                            }
//                        } else {
//                            assert response.body() != null;
//                            Toast.makeText(MainActivity.this, "status= " + response.body().status, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MemberResult> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, "Error call api verify", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
