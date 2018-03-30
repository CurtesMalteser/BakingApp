package com.curtesmalteser.bakingapp.data;

import android.content.Context;

import com.curtesmalteser.bakingapp.AppExecutors;
import com.curtesmalteser.bakingapp.data.db.RecipeDB;
import com.curtesmalteser.bakingapp.data.network.RecipesNetworkDataSource;
import com.curtesmalteser.bakingapp.viewmodel.RecipesActivityViewModelFactory;

/**
 * Created by António "Curtes Malteser" Bastião on 30/03/2018.
 */

public class InjectorUtils {
    public static Repository provideRepository(Context context) {
        RecipeDB db = RecipeDB.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RecipesNetworkDataSource networkDataSource =
                RecipesNetworkDataSource.getsInstance(context.getApplicationContext(), executors);
        return Repository.getInstance(db.recipeClassDao(), networkDataSource, executors);
    }

    public static RecipesActivityViewModelFactory provideRecipesViewModelFactory(Context context){
        Repository repository = provideRepository(context.getApplicationContext());
        return new RecipesActivityViewModelFactory(context.getApplicationContext(), repository);
    }
}