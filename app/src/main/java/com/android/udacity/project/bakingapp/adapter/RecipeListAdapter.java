package com.android.udacity.project.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder>{

    private List<Recipe> repicesList;
    final private ListItemClickListener clickListener;

    public RecipeListAdapter(ListItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View recipeTextView = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeListAdapterViewHolder mvHolder = new RecipeListAdapterViewHolder(recipeTextView);
        return mvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapterViewHolder rViewHolder, int position) {
        if(repicesList != null){
            Recipe recipe = repicesList.get(position);
            String recipeName = recipe.getName();
            rViewHolder.textView.setText(recipeName);
            String imageUrl = recipe.getImage();

            //Set First Default Image for the recipe
            rViewHolder.imageView.setImageResource(R.drawable.default_cake);
            if (StringUtils.isEmpty(recipe.getImage())) {
                //No image for the recipe
                rViewHolder.imageView.setImageResource(R.drawable.cheesecake);
                if(!StringUtils.isEmpty(recipeName) && StringUtils.containsIgnoreCase(recipeName , BakingAppConstants.NUTELLA_PIE)){
                    rViewHolder.imageView.setImageResource(R.drawable.nutellapie);
                }
                if(!StringUtils.isEmpty(recipeName) && StringUtils.containsIgnoreCase(recipeName , BakingAppConstants.YELLOW_CAKE)){
                    rViewHolder.imageView.setImageResource(R.drawable.yellowcake);
                }
                if(!StringUtils.isEmpty(recipeName) && StringUtils.containsIgnoreCase(recipeName , BakingAppConstants.BROWNIES)){
                    rViewHolder.imageView.setImageResource(R.drawable.brownies);
                }
                if(!StringUtils.isEmpty(recipeName) && StringUtils.containsIgnoreCase(recipeName , BakingAppConstants.CHEESECAKE)){
                    rViewHolder.imageView.setImageResource(R.drawable.cheesecake);
                }
            } else {
                Picasso.with(rViewHolder.imageView.getContext())
                        .load(imageUrl)
                        .error(R.drawable.default_cake)
                        .placeholder(R.drawable.default_cake)
                        .into(rViewHolder.imageView);

            }
        }
    }

    @Override
    public int getItemCount() {
        return repicesList != null ? repicesList.size() : 0;
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        ImageView imageView;

        public RecipeListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recipe_name_tv);
            imageView = (ImageView) itemView.findViewById(R.id.recipe_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClicked(clickedPosition);
        }
    }

    public void setData(List<Recipe> recipesList) {
        this.repicesList = recipesList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener{
        void onListItemClicked(int clickedItemIndex);
    }
}
