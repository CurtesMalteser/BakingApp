package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    Fragment detailsFragment;
    Fragment ingredientsFragment;
    Fragment stepsFragment;

    private static final String TAG_DETAILS_FRAGMENT = "tagDetailsFragment";
    private static final String TAG_INGREDIENTS_FRAGMENT = "tagIngredientsFragment";
    private static final String TAG_STEPS_FRAGMENT = "tagStepsFragment";

    private static final String DETAILS_FRAGMENT = "detailsFragment";
    private static final String INGREDIENTS_FRAGMENT = "ingredientsFragment";
    private static final String STEPS_FRAGMENT = "stepsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(DetailsActivityViewModel.class);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        boolean isLandscape = getResources().getBoolean(R.bool.is_landscape);

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

        if (savedInstanceState == null) {

            detailsFragment = new DetailsFragment();
            ingredientsFragment = new IngredientsFragment();

            stepsFragment = new StepsFragment();

            if (!detailsFragment.isVisible())
                setFragment(fragmentManager, R.id.detailsContainer, detailsFragment, TAG_DETAILS_FRAGMENT);

            if (isTablet) {
                mViewModel.setShowIngredients(true);
                if (!ingredientsFragment.isVisible()) {
                    setFragment(fragmentManager, R.id.ingredientsAndStepsContainer, ingredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                }
            }

            mViewModel.getShowIngredients().observe(this, bool -> {
                if (bool) {
                    if (isTablet) {
                        if (!ingredientsFragment.isVisible())
                            setFragment(fragmentManager, R.id.ingredientsAndStepsContainer, ingredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                    } else {
                        if (!ingredientsFragment.isVisible())
                            setFragment(fragmentManager, R.id.detailsContainer, ingredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                    }
                } else {
                    if (isTablet) {
                        if (!stepsFragment.isVisible()) {

                            setFragment(fragmentManager,
                                    R.id.ingredientsAndStepsContainer, stepsFragment, TAG_STEPS_FRAGMENT
                            );
                        }
                    } else {
                        if (!stepsFragment.isVisible()) {
                            setFragment(fragmentManager, R.id.detailsContainer, stepsFragment, TAG_STEPS_FRAGMENT);
                        }
                        if (isLandscape) toolbar.setVisibility(View.GONE);
                    }

                }
            });

        } else {

            detailsFragment = getSupportFragmentManager().getFragment(savedInstanceState, DETAILS_FRAGMENT);
            ingredientsFragment = getSupportFragmentManager().getFragment(savedInstanceState, INGREDIENTS_FRAGMENT);
            stepsFragment = getSupportFragmentManager().getFragment(savedInstanceState, STEPS_FRAGMENT);

            mViewModel.getShowIngredients().observe(this, bool -> {
                if (bool) {
                    if (ingredientsFragment == null) ingredientsFragment = new IngredientsFragment();
                    if (isTablet) {
                        if (!ingredientsFragment.isVisible())
                            setFragment(fragmentManager, R.id.ingredientsAndStepsContainer, ingredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                    } else {
                        if (!ingredientsFragment.isVisible())
                            setFragment(fragmentManager, R.id.detailsContainer, ingredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                    }
                } else {
                    if (stepsFragment == null) stepsFragment = new StepsFragment();
                    if (isTablet) {
                        if (!stepsFragment.isVisible()) {
                            setFragment(fragmentManager, R.id.ingredientsAndStepsContainer, stepsFragment, TAG_STEPS_FRAGMENT);
                        }
                    } else {
                        if (!stepsFragment.isVisible()) {
                            setFragment(fragmentManager, R.id.detailsContainer, stepsFragment, TAG_STEPS_FRAGMENT);
                        }
                        if (isLandscape) toolbar.setVisibility(View.GONE);
                    }
                }
            });
        }


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
                    if (!isTablet)
                        if(detailsFragment == null) detailsFragment = new DetailsFragment();
                        if (!detailsFragment.isVisible()) {
                            setFragment(fragmentManager, R.id.detailsContainer, detailsFragment, TAG_DETAILS_FRAGMENT);
                        }

                    return true;
                case R.id.nav_ingredients:
                    mViewModel.setShowIngredients(true);
                    return true;

                case R.id.nav_steps:
                    mViewModel.setShowIngredients(false);
                    mViewModel.setStepScreen(0);

                    return true;
                default:
                    return false;
            }
        });
    }

    private void setFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment, String fragmentTag) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commit();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getSupportFragmentManager().findFragmentByTag(TAG_DETAILS_FRAGMENT) != null)
            getSupportFragmentManager().putFragment(outState, DETAILS_FRAGMENT, this.detailsFragment);

        if (getSupportFragmentManager().findFragmentByTag(TAG_INGREDIENTS_FRAGMENT) != null)
            getSupportFragmentManager().putFragment(outState, INGREDIENTS_FRAGMENT, this.ingredientsFragment);

        if (getSupportFragmentManager().findFragmentByTag(TAG_STEPS_FRAGMENT) != null)
            getSupportFragmentManager().putFragment(outState, STEPS_FRAGMENT, this.stepsFragment);
    }
}