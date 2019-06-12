package com.facecrm.sample.present;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.face.detect.Util.Util;
import com.facecrm.sample.listener.ViewUIInterface;
import com.facecrm.sample.model.MemberResult;
import com.facecrm.sample.network.NetworkClient;
import com.facecrm.sample.network.NetworkInterface;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateMemberPresenter implements ViewUIInterface.SignInListener {

    private ViewUIInterface viewInterface;
    private String TAG = "CreateMemberPresenter";

    public CreateMemberPresenter(ViewUIInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @SuppressLint("CheckResult")
    @Override
    public void postCreateMember(String name, String email, String phone, int sex) {
        getObservable(name, email, phone, sex).subscribeWith(getObserver());
    }

    private Observable<MemberResult> getObservable(String name, String email, String phone, int sex) {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .createMember(Util.shared().getToken(), name, email, phone, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<MemberResult> getObserver() {
        return new DisposableObserver<MemberResult>() {

            @Override
            public void onNext(@NonNull MemberResult resultData) {
                if (resultData.status == 200) {
                    Gson gson = new Gson();
                    viewInterface.displayUI(gson.toJson(resultData.dataMember));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                viewInterface.displayError("Error fetching data");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "Completed");
            }
        };
    }
}
