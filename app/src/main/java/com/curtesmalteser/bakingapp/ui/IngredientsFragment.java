package com.curtesmalteser.bakingapp.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {


    public IngredientsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView mIngredientsRecyclerView;

    private final ArrayList<Ingredient> mIngredientsList = new ArrayList<>();
    private IngredientsAdapter mIngredientsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ui_ingredients, container, false);

        ButterKnife.bind(this, v);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(getActivity().getApplicationContext());
        DetailsActivityViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(DetailsActivityViewModel.class);

        mIngredientsAdapter = new IngredientsAdapter(mIngredientsList);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        viewModel.getRecipeById().observe(IngredientsFragment.this, fullRecipes ->
                {
                    if (fullRecipes != null)
                        for (Ingredient ingredient : fullRecipes.ingredientList) {
                            mIngredientsList.add(ingredient);
                            mIngredientsAdapter.notifyDataSetChanged();
                        }
                }
        );

        return v;
    }
}
