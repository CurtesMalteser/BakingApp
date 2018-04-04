package com.curtesmalteser.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.curtesmalteser.bakingapp.data.Repository;

/**
 * Created by António "Curtes Malteser" Bastião on 30/03/2018.
 */

public class RecipesActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository mRepository;

    public RecipesActivityViewModelFactory(Repository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeActivityViewModel(mRepository);
    }
}
