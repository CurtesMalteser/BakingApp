package com.curtesmalteser.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.viewmodel.RecipeActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.RecipesActivityViewModelFactory;
import com.curtesmalteser.bakingapp.widget.BakingAppWidget;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity
        implements RecipesAdapter.ListItemClickListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<FullRecipes> mResultList = new ArrayList<>();

    private RecipesAdapter mRecipesAdapter;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private static final String PREFS_NAME
            = "BakingAppPrefs";
    private static final String PREF_PREFIX_KEY = "prefix_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Stetho.initializeWithDefaults(this);

        RecipesActivityViewModelFactory factory = InjectorUtils.provideRecipesViewModelFactory(this);
        RecipeActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeActivityViewModel.class);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

        mRecipesAdapter = new RecipesAdapter(this, mResultList, this);
        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (isTablet) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        viewModel.getRecipes().observe(RecipeActivity.this, fullRecipes -> {
            if (fullRecipes != null && fullRecipes.size() != 0) {
                mResultList.clear();
                for (FullRecipes recipes : fullRecipes) {
                    mResultList.add(recipes);
                    mRecipesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onListItemClick(FullRecipes fullRecipesModel) {
        if (mAppWidgetId != 0) {
            // When the button is clicked, save the string in our prefs and return that they
            // clicked OK.
            saveTitlePref(this, mAppWidgetId, fullRecipesModel.bakingModel.getName());

            // Push widget update to surface with newly set prefix
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            BakingAppWidget.updateAppWidget(this, appWidgetManager,
                    mAppWidgetId, fullRecipesModel);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);



            finish();

        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(getString(R.string.recipe_id), fullRecipesModel.bakingModel.getId());
            startActivity(i);
        }
    }

    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            // TODO: 04/04/2018 -->> Fix this preferences
            //return context.getString(R.string.appwidget_prefix_default);
            return "appwidget_prefix_default";
        }
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
    }
}
