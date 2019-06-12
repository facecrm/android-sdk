package com.facecrm.sample.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facecrm.sample.R;
import com.facecrm.sample.Utils;
import com.facecrm.sample.model.HistoryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<HistoryResult.History> lstData;

    public HistoryAdapter(Context context, List<HistoryResult.History> lstData) {
        this.mContext = context;
        this.lstData = lstData;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public HistoryAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setNotify(List<HistoryResult.History> countries) {
        this.lstData = countries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        HistoryResult.History user = lstData.get(position);
        viewHolder.tvCheckIn.setText(Utils.covertTime(user.created_at));
        viewHolder.tvName.setText("Type: " + user.event_type);
        try {
            if (user.dataHistory.equals("{}")) {
                viewHolder.tvEmail.setVisibility(View.GONE);
                viewHolder.tvPhone.setVisibility(View.GONE);
            } else {
                JSONObject object = new JSONObject(user.dataHistory);
                Log.e("data", "data");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return lstData.size();
    }

    // Static inner class to initialize the views of rows
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imv_avatar)
        ImageView imvAvatar;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_checkin)
        AppCompatTextView tvCheckIn;
        @BindView(R.id.tv_email)
        AppCompatTextView tvEmail;
        @BindView(R.id.tv_phone)
        AppCompatTextView tvPhone;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
