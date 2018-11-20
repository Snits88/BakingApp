package com.android.udacity.project.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.widget.RemoteViews;

import com.android.udacity.project.bakingapp.MainActivity;
import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.RecipeInfoActivity;
import com.android.udacity.project.bakingapp.model.Ingredient;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    private static Recipe recipe;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String widgetTitle = context.getString(R.string.ingredients);
        String widgetIngredientsList = context.getString(R.string.ingredients_list);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        views.setTextViewText(R.id.appwidget_ingredients, widgetTitle);
        views.setTextViewText(R.id.appwidget_ingredients_list, widgetIngredientsList);
        if(recipe != null && !StringUtils.isEmpty(recipe.getName()) && !CollectionUtils.isEmpty(recipe.getIngredients())) {
            // Recipe Set for The Widget; Start RecipeInfoActivity on click
            views.setTextViewText(R.id.appwidget_ingredients, recipe.getName());
            String list = buildIngredientsList();
            if (!StringUtils.isEmpty(list)) {
                views.setTextViewText(R.id.appwidget_ingredients_list, list);
            }
        }
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName())
                .setPackage(null)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_ingredients_list, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void addRecipeToWidget(Recipe recipeForWidget){
        recipe = recipeForWidget;
    }

    public static void removeRecipeFromWidget(){
        recipe = null;
    }

    public static String buildIngredientsList(){
        String IngredietnsList = "";
        if(recipe != null){
            List<Ingredient> ingredients = recipe.getIngredients();
            if(!CollectionUtils.isEmpty(ingredients)){
                for (Ingredient i: ingredients) {
                    IngredietnsList = IngredietnsList + i.getIngredient() + ", Qy: " + i.getQuantity() + ", Mi: " + i.getMeasure() + "\n";
                }
            }
        }
        return IngredietnsList;
    }

}

