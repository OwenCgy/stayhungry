package com.example.administrator.testloginscau.api;

import com.example.administrator.testloginscau.Interface.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private Retrofit retrofit;
    private static ApiService apiService;
    private static String baseUrl = "http://zfsoft.jlmu.cn/(zhux2wbi2vm5iv45g3v2x4bc)/";
    private static final int DEFAULT_TIMEOUT = 5;

    private Api() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApi(){
        if (apiService == null){
            new Api();
        }
        return apiService;
    }

}
