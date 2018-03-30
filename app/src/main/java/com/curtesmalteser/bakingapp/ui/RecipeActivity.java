package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.viewmodel.RecipeActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.RecipesActivityViewModelFactory;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity
        implements RecipesAdapter.ListItemClickListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<FullRecipes> mResultList = new ArrayList<>();

    private RecipesAdapter mRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Stetho.initializeWithDefaults(this);

        RecipesActivityViewModelFactory factory = InjectorUtils.provideRecipesViewModelFactory(this);
        RecipeActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeActivityViewModel.class);

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
                // TODO: 30/03/2018 -->> Check the API lifecycle on config changes 
                mResultList.clear();
                for (FullRecipes recipes : fullRecipes) {
                    mResultList.add(recipes);
                    mRecipesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getData() {

    }



    @Override
    public void onListItemClick(FullRecipes bakingModel) {
        Intent i = new Intent(this, DetailActivity.class);
        startActivity(i);
    }
}
