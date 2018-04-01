package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.model.Step;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detailsContainer)
    FrameLayout detailsContainer;

    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(this);
        DetailsActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailsActivityViewModel.class);

        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(getResources().getString(R.string.recipe_id))) {

            viewModel.setRecipes(getIntent().getIntExtra(getResources().getString(R.string.recipe_id), 1));

            viewModel.getRecipeById().observe(DetailActivity.this, fullRecipes ->
                    {
                        if (fullRecipes != null) {
                            toolbar.setTitle(fullRecipes.bakingModel.getName());
                        }
                    }
            );
        }

        DetailsFragment detailsFragment = new DetailsFragment();
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        StepsFragment stepsFragment = new StepsFragment();
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredientsAndStepsContainer, ingredientsFragment)
                    .commit();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detailsContainer, detailsFragment)
                .commit();

        viewModel.getScreen().observe(this, step ->
        {
            if (!stepsFragment.isVisible()) {
                fragmentManager.beginTransaction()
                        .replace(R.id.ingredientsAndStepsContainer, stepsFragment)
                        .commit();
            }
        });

        // TODO: 01/04/2018 -->> Add click on Ingredients textview so when fragment isn't visible
        // we can navigate back to it
    }
}
