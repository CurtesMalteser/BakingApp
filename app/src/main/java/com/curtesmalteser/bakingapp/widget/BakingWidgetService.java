package com.curtesmalteser.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.db.RecipeDB;
import com.curtesmalteser.bakingapp.data.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by António "Curtes Malteser" Bastião on 07/04/2018.
 */

public class BakingWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingRemoteViewFactory(this.getApplicationContext(), intent);
    }
}

class BakingRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    RecipeDB db;
    Context mContext;

    private int mAppWidgetId;

    ArrayList<Ingredient> ingredients;

    public BakingRemoteViewFactory(Context applicationContext, Intent intent) {

        this.mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        Bundle b = intent.getBundleExtra(applicationContext.getString(R.string.bundle));

        ingredients = b.getParcelableArrayList(applicationContext.getResources().getString(R.string.recipeId));

        db = RecipeDB.getInstance(applicationContext.getApplicationContext());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null)
            return 0;
        else
            return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.single_row_widget);
        rv.setTextViewText(R.id.singleRowIngredientTv, ingredients.get(position).getIngredient());

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putLong("position", position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.singleRowIngredientWidget, fillInIntent);
        Log.d("AJDB", "getViewAt: " + extras.getLong("position"));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

