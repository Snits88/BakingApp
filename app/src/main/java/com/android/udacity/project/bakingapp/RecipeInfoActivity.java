package com.android.udacity.project.bakingapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.udacity.project.bakingapp.adapter.RecipeStepAdapter;
import com.android.udacity.project.bakingapp.fragment.RecipeIngredientsFragment;
import com.android.udacity.project.bakingapp.fragment.RecipeStepFragment;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.model.Step;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeInfoActivity extends AppCompatActivity implements RecipeStepAdapter.ListStepItemClickListener {

    private AssetManager am;
    private Typeface tf;

    private boolean mTwoPane;
    private Recipe recipe;

    @BindView(R.id.btn_ingredients)
    Button btnIngredients;

    @BindView(R.id.recyclerview_recipe_step)
    RecyclerView recyclerStepView;

    private RecipeStepAdapter rsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        this.recipe = getIntent().getExtras().getParcelable(BakingAppConstants.RECIPE);
        //Check  if app is running on tablet or not
        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }
        // Font for Title and Description
        am = this.getAssets();
        tf = Typeface.createFromAsset(am, "fonts/GarmentDistrict-Regular.otf");
        btnIngredients.setTypeface(tf);
        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTwoPane) {
                    // Set Fragment with Ingredients Information
                    RecipeIngredientsFragment fragmentIngredients = new RecipeIngredientsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
                    fragmentIngredients.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragmentIngredients).commit();
                }else{
                    // Set Step Activity for Ingredients information
                    Intent intent = new Intent(RecipeInfoActivity.this, StepActivity.class);
                    intent.putExtra(BakingAppConstants.RECIPE, recipe);
                    startActivity(intent);
                }
            }
        });
        //Menage Adaper for Recipe Step Description
        rsAdapter = new RecipeStepAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerStepView.setLayoutManager(layoutManager);
        recyclerStepView.setHasFixedSize(true);
        recyclerStepView.setAdapter(rsAdapter);

        // Set Recipe Step List for adapter
        if (recipe != null) {rsAdapter.setData(recipe.getSteps());}
    }

    @Override
    public void onListStepItemClicked(int clickedItemIndex) {
        if(mTwoPane){
            // Set Fragment with Step Information
            RecipeStepFragment fragment = new RecipeStepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
            bundle.putParcelable(BakingAppConstants.STEP, recipe.getSteps().get(clickedItemIndex));
            bundle.putBoolean(BakingAppConstants.TWO_PANE, true);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
        }else{
            // Set Step Activity for Ste information
            Intent intent = new Intent(RecipeInfoActivity.this, StepActivity.class);
            intent.putExtra(BakingAppConstants.RECIPE, recipe);
            intent.putExtra(BakingAppConstants.STEP, recipe.getSteps().get(clickedItemIndex));
            startActivity(intent);
        }
    }
}
