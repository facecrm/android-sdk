package com.facecrm.sample.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static Retrofit retrofit;

    public void NetworkClient() {

    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.connectTimeout(60, TimeUnit.SECONDS);
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//                    Request request = original.newBuilder()
//                            .header("Authorization", "token")
//                            .method(original.method(), original.body())
//                            .build();
//                    return chain.proceed(request);
//                }
//            });
            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.facecrm.co/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
        return retrofit;
    }
}
