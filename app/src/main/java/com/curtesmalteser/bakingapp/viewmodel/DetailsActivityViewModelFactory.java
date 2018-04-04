package com.curtesmalteser.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.curtesmalteser.bakingapp.data.Repository;

/**
 * Created by António "Curtes Malteser" Bastião on 31/03/2018.
 */

public class DetailsActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository mRepository;

    public DetailsActivityViewModelFactory(Repository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsActivityViewModel(mRepository);
    }
}
