package com.facecrm.sample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HistoryResult {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("data")
    @Expose
    public List<History> lstHistory = new ArrayList<>();

    @SerializedName("total_item")
    @Expose
    public int totalItem;

    public class History {
        @SerializedName("event_type")
        @Expose
        public String event_type;
        @SerializedName("created_at")
        @Expose
        public String created_at;
        @SerializedName("data")
        @Expose
        public String dataHistory;
    }
}
