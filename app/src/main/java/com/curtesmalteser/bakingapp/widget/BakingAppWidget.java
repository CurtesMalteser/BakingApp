package com.curtesmalteser.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.ui.DetailActivity;
import com.curtesmalteser.bakingapp.ui.RecipeActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */

public class BakingAppWidget extends AppWidgetProvider {


    private static ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private static FullRecipes recipes = new FullRecipes();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, FullRecipes recipeName) {
        recipes = recipeName;
        mIngredientList.addAll(recipeName.ingredientList);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakind_app_widget);

        Intent i = new Intent(context, BakingWidgetService.class);

        i.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

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

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Sets up the intent that points to the StackViewService that will
            // provide the views for this collection.
           // Intent intent = new Intent(context, BakingWidgetService.class);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            //RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.bakind_app_widget);
            //rv.setRemoteAdapter(R.id.listRecipesWidget, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            // TODO: 07/04/2018 set an empty view
            //rv.setEmptyView(R.id.stack_view, R.id.empty_view);


            //Intent intentRefreshWidget = new Intent(context, RecipeActivity.class);
            //intentRefreshWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentRefreshWidget, PendingIntent.FLAG_UPDATE_CURRENT);

            //rv.setOnClickPendingIntent(R.id.recipeNameWidget, pendingIntent);

            // This section makes it possible for items to have individualized behavior.
            // It does this by setting up a pending intent template. Individuals items of a collection
            // cannot set up their own pending intents. Instead, the collection as a whole sets
            // up a pending intent template, and the individual items set a fillInIntent
            // to create unique behavior on an item-by-item basis.
           /* Intent toastIntent = new Intent(context, BakingWidgetService.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
             //broadcasting TOAST_ACTION.
            toastIntent.setAction(BakingAppWidget.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);*/
            //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));*/
           /*PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);



            rv.setPendingIntentTemplate(R.id.listRecipesWidget, toastPendingIntent);*/

            /*Intent startActivityIntent = new Intent(context, RecipeActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.listRecipesWidget, startActivityPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, rv);*/

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeActivity.deleteTitlePref(context, appWidgetId);
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

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId, recipes);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

