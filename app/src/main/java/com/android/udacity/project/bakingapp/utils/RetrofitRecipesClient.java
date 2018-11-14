package com.android.udacity.project.bakingapp.utils;

import com.android.udacity.project.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitRecipesClient {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> loadRecipes();
}
