package com.facecrm.sample.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facecrm.sample.R;
import com.facecrm.sample.adapter.HistoryAdapter;
import com.facecrm.sample.listener.ViewUIInterface;
import com.facecrm.sample.model.HistoryResult;
import com.facecrm.sample.present.HistoryPresenter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity implements ViewUIInterface {

    @BindView(R.id.recycler_view)
    RecyclerView rcvHistory;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;

    private HistoryAdapter historyAdapter;
    private HistoryPresenter historyPresenter;
    private LinearLayoutManager layoutManager;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean isLoading = false;
    private int page = 1;
    private List<HistoryResult.History> lstMember = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        hideSoftKeyboard(this);

        initView();
    }

    private void initView() {
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        historyPresenter = new HistoryPresenter(this);
        layoutManager = new LinearLayoutManager(this);
        rcvHistory.setLayoutManager(layoutManager);

        historyPresenter.getHistory(page);

        rcvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                totalItemCount = layoutManager.getItemCount();
                if (isLoading && (lstMember.size() - firstVisibleItem) <= visibleItemCount) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            historyPresenter.getHistory(page++);
                            isLoading = false;
                        }
                    }, 1000);
                } else {
                    Log.e("else", totalItemCount + "-" + firstVisibleItem + "-" + visibleItemCount);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void displayUI(String data) {
        Gson gson = new Gson();
        HistoryResult historyResult = gson.fromJson(data, HistoryResult.class);
        lstMember.addAll(historyResult.lstHistory);
        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(this, lstMember);
            rcvHistory.setAdapter(historyAdapter);
        } else {
            historyAdapter.setNotify(lstMember);
        }
        progressBar.setVisibility(View.GONE);
        isLoading = historyResult.totalItem > lstMember.size();
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
