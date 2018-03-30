package com.curtesmalteser.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.curtesmalteser.bakingapp.AppExecutors;
import com.curtesmalteser.bakingapp.data.db.RecipeClassDao;
import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.data.model.Step;
import com.curtesmalteser.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.ArrayList;

/**
 * Created by António "Curtes Malteser" Bastião on 30/03/2018.
 */

public class Repository {

    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final RecipeClassDao mDao;
    private final RecipesNetworkDataSource mRecipesNetworkDataSource;
    private final AppExecutors mExecutors;

    public Repository(RecipeClassDao mDao,
                      RecipesNetworkDataSource mRecipesNetworkDataSource,
                      AppExecutors mExecutors) {
        this.mDao = mDao;
        this.mRecipesNetworkDataSource = mRecipesNetworkDataSource;
        this.mExecutors = mExecutors;

        LiveData<ArrayList<BakingModel>> networkData = mRecipesNetworkDataSource.getRecipes();
        networkData.observeForever(bakingModels ->
                mExecutors.diskIO().execute(() -> {
                    for (int i = 0; i < bakingModels.size(); i++) {

                        ArrayList<Ingredient> ingredients = new ArrayList<>();
                        for (Ingredient ingredient : bakingModels.get(i).getIngredients()) {
                            ingredients.add(new Ingredient(ingredient.getQuantity(), ingredient.getMeasure(),
                                    ingredient.getIngredient(), bakingModels.get(i).getId()));
                        }

                        ArrayList<Step> steps = new ArrayList<>();
                        for (Step step : bakingModels.get(i).getSteps()) {
                            steps.add(new Step(step.getStepNumber(), step.getShortDescription(), step.getDescription(),
                                    step.getVideoURL(), step.getThumbnailURL(), bakingModels.get(i).getId()));
                        }

                        mDao.updateData(bakingModels.get(i), ingredients, steps);
                    }
                })
        );
    }

    synchronized static Repository getsInstance(RecipeClassDao recipeClassDao,
                                                RecipesNetworkDataSource recipesNetworkDataSource,
                                                AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(recipeClassDao,
                        recipesNetworkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    private synchronized void intializeData() {
        mExecutors.diskIO().execute(this::startFetchRecipes);
    }

    private void startFetchRecipes() {
        mRecipesNetworkDataSource.fetchRecipes();
    }
}
