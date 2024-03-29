package com.android.udacity.project.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import com.android.udacity.project.bakingapp.idleresource.BakingAppIdleResource;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;
import com.android.udacity.project.bakingapp.utils.JSONRecipesDownloader;
import com.android.udacity.project.bakingapp.widget.RecipeIngredientsWidget;

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

    // The Idling Resource which will be null in production.
    @Nullable
    private BakingAppIdleResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link BakingAppIdleResource}.
     */
    @VisibleForTesting
    @NonNull
    public BakingAppIdleResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakingAppIdleResource();
        }
        return mIdlingResource;
    }

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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(rlAdapter);
        //Json Load with Recipes
        if(isNetworkAvailable()) {
            JSONRecipesDownloader.retrieveJson(this, MainActivity.this, getIdlingResource());
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
        // Update Application Widget
        updateWidgets(recipe);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Update Application Widget
        updateWidgets(null);
    }

    private void updateWidgets(Recipe recipe) {
        if(recipe != null){
            RecipeIngredientsWidget.addRecipeToWidget(recipe);
        }else{
            RecipeIngredientsWidget.removeRecipeFromWidget();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientsWidget.class));
        RecipeIngredientsWidget.updateAllAppWidget(this, appWidgetManager, appWidgetIds);
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
