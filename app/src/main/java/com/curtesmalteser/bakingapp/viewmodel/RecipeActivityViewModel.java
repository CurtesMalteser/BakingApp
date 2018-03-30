package com.curtesmalteser.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.curtesmalteser.bakingapp.data.db.FullRecipes;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

public class RecipeActivityViewModel extends ViewModel {
    // TODO: 25/03/2018 -->> Implement the ViewModel
    private LiveData<List<FullRecipes>> mRecipes;

    public RecipeActivityViewModel(){

    }

    public LiveData<List<FullRecipes>> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(LiveData<List<FullRecipes>> recipes) {
        mRecipes = recipes;
    }
}
