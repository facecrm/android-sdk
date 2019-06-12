package com.facecrm.sample.present;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.face.detect.Util.Util;
import com.facecrm.sample.listener.HistoryInterface;
import com.facecrm.sample.listener.ViewUIInterface;
import com.facecrm.sample.model.HistoryResult;
import com.facecrm.sample.network.NetworkClient;
import com.facecrm.sample.network.NetworkInterface;
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
