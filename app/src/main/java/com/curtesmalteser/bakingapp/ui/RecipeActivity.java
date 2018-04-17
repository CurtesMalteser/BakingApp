package com.curtesmalteser.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
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
import com.curtesmalteser.bakingapp.viewmodel.RecipeActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.RecipesActivityViewModelFactory;
import com.curtesmalteser.bakingapp.widget.BakingAppWidget;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity
        implements RecipesAdapter.ListItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private final ArrayList<FullRecipes> mResultList = new ArrayList<>();

    private RecipesAdapter mRecipesAdapter;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Parcelable mStateRecyclerView;

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
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        if (savedInstanceState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mStateRecyclerView);

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
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            BakingAppWidget.updateAppWidget(this, appWidgetManager,
                    mAppWidgetId, fullRecipesModel);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateRecyclerView = mRecyclerView.getLayoutManager().onSaveInstanceState();

    }
}
