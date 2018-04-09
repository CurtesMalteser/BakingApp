package com.curtesmalteser.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.curtesmalteser.bakingapp.data.Repository;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;

/**
 * Created by António "Curtes Malteser" Bastião on 31/03/2018.
 */

public class DetailsActivityViewModel extends ViewModel {

    private LiveData<FullRecipes> mRecipes;
    private MutableLiveData<Integer> mStep = new MutableLiveData<>();
    private MutableLiveData<Long> mVideoPosition = new MutableLiveData<>();

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

    public void setStepScreen(int position) {
        setVideoPosition(0);
        mStep.setValue(position);
    }

    public LiveData<Integer> getStepScreen() {
        return mStep;
    }


    public void setVideoPosition(long videoPosition) {
       mVideoPosition.setValue(videoPosition);
    }

    public MutableLiveData<Long> getVideoPosition() {
        return mVideoPosition;
    }
}
