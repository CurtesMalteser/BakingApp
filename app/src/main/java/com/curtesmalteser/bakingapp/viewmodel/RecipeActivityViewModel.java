package com.curtesmalteser.bakingapp.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.curtesmalteser.bakingapp.data.Repository;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

public class RecipeActivityViewModel extends ViewModel {

    private LiveData<List<FullRecipes>> mRecipes;

    private final Repository mRepository;

    public RecipeActivityViewModel(Repository repository) {
        this.mRepository = repository;
        mRecipes = mRepository.getAllRecipes();
    }

    public LiveData<List<FullRecipes>> getRecipes() {
        return mRecipes;
    }

   /* public void setRecipes(LiveData<ArrayList<FullRecipes>> recipes) {
        mRecipes = recipes;
    }*/
}
