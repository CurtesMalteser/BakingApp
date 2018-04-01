package com.curtesmalteser.bakingapp.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.curtesmalteser.bakingapp.data.Repository;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 31/03/2018.
 */

public class DetailsActivityViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context mContext;

    private LiveData<FullRecipes> mRecipes;

    private final Repository mRepository;

    public DetailsActivityViewModel(Context context, Repository repository) {
        this.mContext = context;
        this.mRepository = repository;

    }

    public void setRecipes(int recipeId) {
        mRecipes = mRepository.getRecipeById(recipeId);
    }

    public LiveData<FullRecipes> getRecipeById() {
        return mRecipes;
    }
}
