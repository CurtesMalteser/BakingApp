package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    @BindView(R.id.toolbarDetails)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(this);
        DetailsActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailsActivityViewModel.class);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        mNavigationView.setNavigationItemSelectedListener(item -> {
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
        );

        fragmentManager = getSupportFragmentManager();

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

        fragmentManager.beginTransaction()
                .add(R.id.detailsContainer, detailsFragment)
                .commit();

        if (isTablet) {
            fragmentManager.beginTransaction()
                    .add(R.id.ingredientsAndStepsContainer, ingredientsFragment)
                    .commit();
        }

        viewModel.getScreen().observe(this, step ->
        {
            if (isTablet) {
                if (!stepsFragment.isVisible()) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.ingredientsAndStepsContainer, stepsFragment)
                            .commit();
                }
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.detailsContainer, stepsFragment)
                        .commit();
            }
        });

        // TODO: 01/04/2018 -->> Add click on Ingredients textview for tablet so when fragment isn't visible
        // we can navigate back to it
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
