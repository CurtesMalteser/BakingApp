package com.curtesmalteser.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.curtesmalteser.bakingapp.data.Repository;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

public class RecipeActivityViewModel extends ViewModel {

    private final LiveData<List<FullRecipes>> mRecipes;

    RecipeActivityViewModel(Repository repository) {
        mRecipes = repository.getAllRecipes();
    }

    public LiveData<List<FullRecipes>> getRecipes() {
        return mRecipes;
    }
}
