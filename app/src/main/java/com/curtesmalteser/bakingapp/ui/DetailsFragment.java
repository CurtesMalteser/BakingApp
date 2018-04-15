package com.curtesmalteser.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.model.Step;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment
        implements StepsAdapter.ListItemClickListener {

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @BindView(R.id.stepsRecyclerView)
    RecyclerView mStepsRecyclerView;

    private ArrayList<Step> mStepsList = new ArrayList<>();
    private StepsAdapter mStepsAdapter;
    private DetailsActivityViewModel mViewModel;

    private Parcelable mStateRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //teste
        View v = inflater.inflate(R.layout.ui_detail, container, false);

        ButterKnife.bind(this, v);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(DetailsActivityViewModel.class);

        mStepsAdapter = new StepsAdapter(mStepsList, this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (savedInstanceState != null)
            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(mStateRecyclerView);

        mViewModel.getRecipeById().observe(DetailsFragment.this, fullRecipes ->
                {
                    if (fullRecipes != null)
                        mStepsList.clear();
                    mStepsList.addAll(fullRecipes.stepList);
                    mStepsAdapter.notifyDataSetChanged();
                }
        );
        return v;
    }

    @Override
    public void onListItemClick(int position) {
        if (position == 0) {
            Log.d("TAG", "onListItemClick: " + position);
            mViewModel.setShowIngredients(true);

        } else
            Log.d("TAG", "onListItemClick: " + position);
            mViewModel.setShowIngredients(false);
            mViewModel.setStepScreen(position - 1);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateRecyclerView = mStepsRecyclerView.getLayoutManager().onSaveInstanceState();
    }
}