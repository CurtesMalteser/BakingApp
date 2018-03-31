package com.curtesmalteser.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.curtesmalteser.bakingapp.R;

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

        setSupportActionBar(toolbar);
        toolbar.setTitle("lol");

        DetailsFragment detailsFragment = new DetailsFragment();
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

        if(isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredientsAndStepsContainer, ingredientsFragment)
                    .commit();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detailsContainer, detailsFragment)
                .commit();
    }
}
