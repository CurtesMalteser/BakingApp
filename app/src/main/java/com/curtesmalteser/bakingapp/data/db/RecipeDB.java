package com.curtesmalteser.bakingapp.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.data.model.Step;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

@Database(entities = {BakingModel.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDB extends RoomDatabase{
    public abstract RecipeDao recipeDao();

    private static final String DATABASE_NAME = "Recipes.db";

    private static final Object LOCK = new Object();
    private static volatile RecipeDB sInstance;

    public static RecipeDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDB.class, RecipeDB.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }
}
