package com.curtesmalteser.bakingapp.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.curtesmalteser.bakingapp.data.Repository;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.Step;

/**
 * Created by António "Curtes Malteser" Bastião on 31/03/2018.
 */

public class DetailsActivityViewModel extends ViewModel {

    private LiveData<FullRecipes> mRecipes;
    private MutableLiveData<Step> mStep = new MutableLiveData<>();

    private final Repository mRepository;

    public DetailsActivityViewModel(Repository repository) {
        this.mRepository = repository;

    }

    public void setRecipes(int recipeId) {
        mRecipes = mRepository.getRecipeById(recipeId);
    }

    public LiveData<FullRecipes> getRecipeById() {
        return mRecipes;
    }

    public void setScreen(Step position) {
        mStep.setValue(position);
    }

    public LiveData<Step> getScreen(){
        return mStep;
    }
}
