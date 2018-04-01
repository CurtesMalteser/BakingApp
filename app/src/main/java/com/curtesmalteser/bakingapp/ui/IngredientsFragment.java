package com.curtesmalteser.bakingapp.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {


    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ui_ingredients, container, false);

        ButterKnife.bind(this, v);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(getActivity().getApplicationContext());
        DetailsActivityViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(DetailsActivityViewModel.class);

        viewModel.getRecipeById().observe(IngredientsFragment.this, fullRecipes ->
                {
                    if (fullRecipes != null)
                        for(Ingredient ingredient : fullRecipes.ingredientList)
                            Log.d("AJDB", "Ingredient: " + ingredient.getIngredient() +
                            " Meadure: " + ingredient.getMeasure());
                }
        );

        return v;
    }

}
