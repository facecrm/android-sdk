package com.ekidpro.facecrmsample.present;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aponl.apfacesdk.Util.Util;
import com.ekidpro.facecrmsample.listener.ViewUIInterface;
import com.ekidpro.facecrmsample.model.MemberResult;
import com.ekidpro.facecrmsample.network.NetworkClient;
import com.ekidpro.facecrmsample.network.NetworkInterface;
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
