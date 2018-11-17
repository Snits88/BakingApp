package com.android.udacity.project.bakingapp.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.model.Ingredient;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListAdapterViewHolder>{

    private AssetManager am;
    private Typeface tf;
    private List<Ingredient> iList;

    @NonNull
    @Override
    public IngredientListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        am = context.getAssets();
        tf = Typeface.createFromAsset(am, "fonts/GarmentDistrict-Regular.otf");
        int layoutIdForListItem = R.layout.ingredients_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View recipeTextView = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        IngredientListAdapterViewHolder mvHolder = new IngredientListAdapter.IngredientListAdapterViewHolder(recipeTextView);
        return mvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapterViewHolder holder, int position) {
        if(iList != null){
            holder.textViewDescription.setText(iList.get(position).getIngredient());
            holder.textViewQuantity.setText(BakingAppConstants.QUANTITY +  String.valueOf(iList.get(position).getQuantity()));
            holder.textViewMeasure.setText(BakingAppConstants.MEASURE + iList.get(position).getMeasure());
        }
    }

    @Override
    public int getItemCount() {
        return iList != null ? iList.size() : 0;
    }

    public class IngredientListAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDescription;
        TextView textViewQuantity;
        TextView textViewMeasure;

        public IngredientListAdapterViewHolder(View itemView) {
            super(itemView);
            textViewDescription = (TextView) itemView.findViewById(R.id.ingredients_description);
            textViewQuantity = (TextView) itemView.findViewById(R.id.ingredients_quantity);
            textViewMeasure = (TextView) itemView.findViewById(R.id.ingredients_measure);
            textViewDescription.setTypeface(tf);
            textViewQuantity.setTypeface(tf);
            textViewMeasure.setTypeface(tf);
        }
    }


    public void setData(List<Ingredient> ingredientList) {
        this.iList = ingredientList;
        notifyDataSetChanged();
    }
}
