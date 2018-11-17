package com.android.udacity.project.bakingapp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.udacity.project.bakingapp.fragment.RecipeIngredientsFragment;
import com.android.udacity.project.bakingapp.fragment.RecipeStepFragment;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.model.Step;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    private Recipe recipe;
    private Step clickedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        if(savedInstanceState == null){
            //Obtain Recipe and Step Selected from Intent
            this.clickedStep = getIntent().getExtras().getParcelable(BakingAppConstants.STEP);
            this.recipe = getIntent().getExtras().getParcelable(BakingAppConstants.RECIPE);
            if(clickedStep != null){
                // Specific Clicked Step in the passed Intent
                startStepFragment();
            }else{
                // No Step in the passed Intenet
                startIngredientesFragment();
            }
        }
    }

    private void startStepFragment(){
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
        bundle.putParcelable(BakingAppConstants.STEP, clickedStep);
        bundle.putBoolean(BakingAppConstants.TWO_PANE, false);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
    }

    private void startIngredientesFragment(){
        // Set Fragment with Ingredients Information
        RecipeIngredientsFragment fragmentIngredients = new RecipeIngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
        fragmentIngredients.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragmentIngredients).commit();
    }

}
