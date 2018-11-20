package com.nsankhla.retrofitproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nsankhla.retrofitproject.Adapter.PostAdapter;
import com.nsankhla.retrofitproject.Model.RootObject;
import com.nsankhla.retrofitproject.Model.Worldpopulation;
import com.nsankhla.retrofitproject.Retrofit.MyAPI;
import com.nsankhla.retrofitproject.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    MyAPI myAPI;
    RecyclerView recycler_country;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_country = findViewById(R.id.recycler_country);

        //init Api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(MyAPI.class);

        GridLayoutManager llm = new GridLayoutManager(this, 2);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_country.setLayoutManager(llm);
        fatchData();


    }

    private void fatchData() {

        compositeDisposable.add(myAPI.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RootObject>() {
                    @Override
                    public void accept(RootObject rootObject) {
                        displaydata(rootObject.worldpopulation);
                    }
                }));

    }

    private void displaydata(List<Worldpopulation> worldpopulation) {
        PostAdapter adapter = new PostAdapter(this, worldpopulation);
        recycler_country.setAdapter(adapter);
    }


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
