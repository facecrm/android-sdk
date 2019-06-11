package com.ekidpro.facecrmsample.present;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aponl.apfacesdk.Util.Util;
import com.ekidpro.facecrmsample.listener.HistoryInterface;
import com.ekidpro.facecrmsample.listener.ViewUIInterface;
import com.ekidpro.facecrmsample.model.HistoryResult;
import com.ekidpro.facecrmsample.network.NetworkClient;
import com.ekidpro.facecrmsample.network.NetworkInterface;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HistoryPresenter implements HistoryInterface {

    private ViewUIInterface viewInterface;
    private String TAG = "CreateFacePresenter";
    private int currentPage = 0;

    public HistoryPresenter(ViewUIInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getHistory(int page) {
        if (currentPage != page) {
            getObservable(page).subscribeWith(getObserver());
            currentPage = page;
        }
    }

    private Observable<HistoryResult> getObservable(int page) {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getHistory(Util.shared().getToken(), 10, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<HistoryResult> getObserver() {
        return new DisposableObserver<HistoryResult>() {

            @Override
            public void onNext(@NonNull HistoryResult resultData) {
                Log.e(TAG, "OnNext=" + resultData);
                if (resultData.status == 200) {
                    Gson gson = new Gson();
                    viewInterface.displayUI(gson.toJson(resultData));
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
