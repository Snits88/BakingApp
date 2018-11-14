package com.android.udacity.project.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.udacity.project.bakingapp.adapter.RecipeListAdapter;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;
import com.android.udacity.project.bakingapp.utils.JSONRecipesDownloader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ListItemClickListener, JSONRecipesDownloader.PopulateRepicesList {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.failure_load)
    ImageView imageFailure;

    @BindView(R.id.connection_error_text)
    TextView connectionText;

    @BindView(R.id.card_view_error)
    CardView cardViewError;

    @BindView(R.id.recyclerview_recipe_name)
    RecyclerView recyclerView;

    private RecipeListAdapter rlAdapter;

    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rlAdapter = new RecipeListAdapter(this);

        imageFailure.setVisibility(View.GONE);
        cardViewError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(layoutManager);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(rlAdapter);
        //Json Load with Recipes
        if(isNetworkAvailable()) {
            JSONRecipesDownloader.retrieveJson(this, MainActivity.this);
        }else{
            failedLoad();
        }

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Recipe recipe = recipes.get(clickedItemIndex);
        // Prepare Intent for Recipe Ingredients and Step Info
        Intent intent = new Intent(MainActivity.this, RecipeInfoActivity.class);
        intent.putExtra(BakingAppConstants.RECIPE, recipe);
        startActivity(intent);

    }

    @Override
    public void populate(List<Recipe> recipes) {
        if (recipes == null) {
            failedLoad();
        } else {
            this.recipes = recipes;
            imageFailure.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            rlAdapter.setData(recipes);
            recyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, getString(R.string.all_recipes_loaded),
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void failedLoad() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        imageFailure.setVisibility(View.VISIBLE);
        cardViewError.setVisibility(View.VISIBLE);
    }
}
