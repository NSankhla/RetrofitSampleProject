package com.nsankhla.retrofitproject.Retrofit;


import com.nsankhla.retrofitproject.Model.RootObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyAPI {

    @GET("/tutorial/jsonparsetutorial.txt")
    Observable<RootObject> getPosts();

}
