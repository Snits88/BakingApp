package com.android.udacity.project.bakingapp.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.udacity.project.bakingapp.MainActivity;
import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JSONRecipesDownloader {

    static List<Recipe> recipes;

    public interface PopulateRepicesList{
        void populate(List<Recipe> recipes);
        void failedLoad();
    }

    public static void retrieveJson(final Context context, final PopulateRepicesList callback){

        /*Create handle for the RetrofitInstance interface*/
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        RetrofitRecipesClient client = retrofitInstance.create(RetrofitRecipesClient.class);

        Call<List<Recipe>> call = client.loadRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                callback.populate(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                callback.failedLoad();
                t.printStackTrace();
            }
        });
    }
}
