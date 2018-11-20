package com.nsankhla.retrofitproject.Retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit ourInstance;

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        if (ourInstance == null) {
            ourInstance = new Retrofit.Builder()
                    .baseUrl("http://www.androidbegin.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return ourInstance;
    }
}
