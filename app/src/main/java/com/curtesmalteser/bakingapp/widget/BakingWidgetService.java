package com.curtesmalteser.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.curtesmalteser.bakingapp.R;
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

    private final Context mContext;
    private final ArrayList<Ingredient> ingredients;

    BakingRemoteViewFactory(Context applicationContext, Intent intent) {

        this.mContext = applicationContext;

        Bundle b = intent.getBundleExtra(applicationContext.getString(R.string.bundle));

        ingredients = b.getParcelableArrayList(applicationContext.getResources().getString(R.string.recipeId));
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

        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.singleRowIngredientWidget, fillInIntent);

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

