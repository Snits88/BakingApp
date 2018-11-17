package com.android.udacity.project.bakingapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.adapter.IngredientListAdapter;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;

public class RecipeIngredientsFragment extends Fragment {

    private Recipe recipe;

    RecyclerView recyclerView;
    private IngredientListAdapter rIAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Adapter and Layout Menager For Ingredients
        rIAdapter = new IngredientListAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Retrive Recipe with Ingredients
        recipe = getArguments().getParcelable(BakingAppConstants.RECIPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.ingredient_fragment, container, false);
        // Menage Recycler View For Ingredients
        recyclerView = rootView.findViewById(R.id.recyclerview_recipe_ingredients);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(rIAdapter);
        if(recipe.getIngredients()!= null){
            rIAdapter.setData(recipe.getIngredients());
        }
        return rootView;
    }

}
