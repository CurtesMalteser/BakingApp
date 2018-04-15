package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detailsContainer)
    FrameLayout detailsContainer;

    @BindView(R.id.ingredientsAndStepsContainer)
    FrameLayout ingredientsAndStepsContainer;

    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private DetailsActivityViewModel mViewModel;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailsActivityViewModel.class);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        fragmentManager = getSupportFragmentManager();

        if (getIntent().hasExtra(getResources().getString(R.string.recipe_id))) {

            mViewModel.setRecipes(getIntent().getIntExtra(getResources().getString(R.string.recipe_id), 1));
            mViewModel.getRecipeById().observe(DetailActivity.this, fullRecipes ->
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


        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.detailsContainer, detailsFragment)
                    .commit();

            if (isTablet) {
                mViewModel.setShowIngredients(true);
                if (!ingredientsFragment.isVisible())
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredientsAndStepsContainer, ingredientsFragment)
                            .commit();

            }
        }

        mViewModel.getShowIngredients().observe(this, bool -> {
            if (bool) {
                if (isTablet) {
                    if (!ingredientsFragment.isVisible())
                        fragmentManager.beginTransaction()
                                .replace(R.id.ingredientsAndStepsContainer, ingredientsFragment)
                                .commit();
                } else {
                    if (!ingredientsFragment.isVisible())
                        fragmentManager.beginTransaction()
                                .replace(R.id.detailsContainer, ingredientsFragment)
                                .commit();
                }
            } else {
                if (isTablet) {
                    if (!stepsFragment.isVisible()) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.ingredientsAndStepsContainer, stepsFragment)
                                .commit();
                    }
                } else {
                    if (!stepsFragment.isVisible()) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.detailsContainer, stepsFragment)
                                .commit();
                    }
                }

            }
        });

        mNavigationView.setNavigationItemSelectedListener(menuItem -> {

            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.nav_recipes:
                    Intent i = new Intent(this, RecipeActivity.class);
                    startActivity(i);
                    finish();
                    return true;

                case R.id.nav_details:
                    if (!detailsFragment.isVisible()) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.detailsContainer, detailsFragment)
                                .commit();
                    }

                    return true;
                case R.id.nav_ingredients:
                    mViewModel.setShowIngredients(true);
                    return true;

                case R.id.nav_steps:
                    mViewModel.setStepScreen(0);

                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}