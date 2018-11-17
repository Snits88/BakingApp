package com.android.udacity.project.bakingapp.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.model.Step;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder>{

    private List<Step> stepsList;
    final private ListStepItemClickListener clickListener;
    private AssetManager am;

    public RecipeStepAdapter(ListStepItemClickListener listener){
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_description_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View recipeTextView = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeStepAdapter.RecipeStepAdapterViewHolder mvHolder = new RecipeStepAdapter.RecipeStepAdapterViewHolder(recipeTextView);
        return mvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapterViewHolder holder, int position) {
        if(stepsList != null){
            Step step = stepsList.get(position);
            String stepShortDescription = step.getShortDescription();
            Integer stepId = step.getId();
            holder.textView.setText(BakingAppConstants.STEP_N+ stepId + ": " + stepShortDescription);
        }
    }

    @Override
    public int getItemCount() {
        return stepsList != null ? stepsList.size() : 0;
    }

    public class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;

        public RecipeStepAdapterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.step_description);
            am = itemView.getContext().getAssets();
            Typeface tf = Typeface.createFromAsset(am, "fonts/GarmentDistrict-Regular.otf");
            textView.setTypeface(tf);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListStepItemClicked(clickedPosition);
        }
    }
    public void setData(List<Step> recipesStepList) {
        if(recipesStepList.size() > 0 ){
            this.stepsList = recipesStepList;
            notifyDataSetChanged();
        }
    }

    public interface ListStepItemClickListener{
        void onListStepItemClicked(int clickedItemIndex);
    }
}
