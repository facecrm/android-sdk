package com.facecrm.sample.network;

import com.facecrm.sample.model.HistoryResult;
import com.facecrm.sample.model.MemberResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkInterface {

    @GET("faces/history")
    Observable<HistoryResult> getHistory(@Header("Authorization") String token,
                                         @Query("limit") int limit, @Query("offset") int offset);

    @FormUrlEncoded
    @POST("members/create")
    Observable<MemberResult> createMember(@Header("Authorization") String token,
                                          @Field("fullname") String fullName, @Field("email") String email,
                                          @Field("phone") String phone, @Field("sex") int sex);

    @FormUrlEncoded
    @POST("application/token-auth")
    Call<MemberResult> applicationVerify(@Header("Authorization") String md5_appId, @Field("device_id") String deviceId);
}
