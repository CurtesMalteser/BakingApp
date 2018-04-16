package com.curtesmalteser.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.ui.DetailActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */

public class BakingAppWidget extends AppWidgetProvider {


    private static final ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private static FullRecipes recipes = new FullRecipes();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, FullRecipes recipeName) {
        recipes = recipeName;
        mIngredientList.addAll(recipeName.ingredientList);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent i = new Intent(context, BakingWidgetService.class);

        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        Bundle b = new Bundle();
        b.putParcelableArrayList(context.getResources().getString(R.string.recipeId), mIngredientList );

        i.putExtra(context.getString(R.string.bundle), b);

        views.setTextViewText(R.id.recipeNameWidget, recipeName.bakingModel.getName());
        views.setRemoteAdapter(R.id.listRecipesWidget, i);
        views.setRemoteAdapter(R.id.listRecipesWidget, i);

        Intent startActivityIntent = new Intent(context, DetailActivity.class);
        startActivityIntent.putExtra(context.getString(R.string.recipe_id), recipeName.bakingModel.getId());
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listRecipesWidget, startActivityPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId, recipes);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

